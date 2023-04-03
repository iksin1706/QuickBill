package com.quickbill.controllers;

import com.quickbill.security.PasswordUtils;
import com.quickbill.services.DbService;
import com.quickbill.models.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

public class LoginController {

    @FXML
    private Button loginButton;
    @FXML
    private TextField loginPrompt;
    @FXML
    private PasswordField passwordPrompt;
    @FXML
    private Button registerButton;
    @FXML
    private Label info;
    private DbService dbService;
    private WindowController windowController;

    //setting dbservice and windowController
    public void setUp(DbService dbService, WindowController windowController) {
        this.dbService = dbService;
        this.windowController = windowController;
    }

    //closing app
    @FXML
    void exitApp(MouseEvent event) {
        System.exit(0);
    }

    //trying to loging and showing info if something wrong
    @FXML
    public void logIn(MouseEvent mouseEvent) {
        String login = loginPrompt.getText();
        String password = passwordPrompt.getText();

        User user = dbService.getUser(login);

        if (user == null) {
            showInfo("Nie ma użytkownika o takiej nazwie");
            return;
        }
        if (!PasswordUtils.checkPassword(password,user.getPassword())) {
            showInfo("Błedne hasło");
            return;
        }
        windowController.login(user);
        System.out.println("Użytkownik zalogowany");
    }
    //showing info
    private void showInfo(String text) {
        info.setText(text);
        info.setStyle("visibility:  true;");
    }
    //changning window to register
    @FXML
    void signin(MouseEvent event) {
        windowController.changeWindow("register");
    }
}

