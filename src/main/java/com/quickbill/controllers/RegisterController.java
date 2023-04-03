package com.quickbill.controllers;

import com.quickbill.models.User;
import com.quickbill.security.PasswordUtils;
import com.quickbill.services.DbService;
import com.quickbill.helpers.EmailValidator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class RegisterController {

    @FXML
    private TextField emailPrompt;
    @FXML
    private TextField loginPrompt;
    @FXML
    private PasswordField passwordPrompt1;
    @FXML
    private PasswordField passwordPrompt2;
    @FXML
    private Button registerUserButton;
    @FXML
    private Label info;
    private DbService dbService;
    private WindowController windowController;

    public void setUp(DbService dbService, WindowController windowController) {
        this.dbService = dbService;
        this.windowController = windowController;
    }

    @FXML
    void exitApp(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    void logout(MouseEvent event) {
        windowController.changeWindow("login");
    }

    //checking is everything valid and adding user to database
    public void register(MouseEvent mouseEvent) {
        String login = loginPrompt.getText();
        String email = emailPrompt.getText();
        String password1 = passwordPrompt1.getText();
        String password2 = passwordPrompt2.getText();

        User user = dbService.getUser(login);
        User user2 = dbService.getUser(email);
        if (user != null) {
            showInfo("Istnieje już użytkownik o takiej nazwie");
            return;
        }
        if (user2 != null) {
            showInfo("Istnieje już użytkownik z takim emailem");
            return;
        }

        if (login.length() < 5) {
            showInfo("Login musi mieć przynajmniej 5 znaków");
            return;
        }
        if (!EmailValidator.isValid(email)) {
            showInfo("Nieprawidłowy email");
            return;
        }
        if (password1.length() < 6) {
            showInfo("Hasło musi mieć przynajmniej 6 znaków");
            return;
        }
        if (!password1.equals(password2)) {
            showInfo("Hasła różnią się od siebie");
            System.out.println("Hasła sie różnia");
            return;
        }


        password1=PasswordUtils.hashPassword(password1);
        dbService.addUser(login, password1, email);
        windowController.changeWindow("login");
    }

    private void showInfo(String text) {
        info.setText(text);
        info.setStyle("visibility:  true;");
    }
}

