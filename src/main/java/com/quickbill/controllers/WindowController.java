package com.quickbill.controllers;

import com.quickbill.models.User;
import com.quickbill.services.DbService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.StackPane;

import java.io.IOException;

public class WindowController {

    private DbService dbService;
    @FXML
    private StackPane container;
    private User user;

    public void initialize() throws IOException {
        dbService = new DbService();
        changeWindow("login");
    }

    public void changeWindow(String fxml) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxml + ".fxml"));
            Parent root = loader.load();
            container.getChildren().clear();
            container.getChildren().addAll(root);
            if (fxml == "main") {
                MainController mainController = loader.getController();
                mainController.setUp(dbService, this, user);
            }
            if (fxml == "register") {
                RegisterController registerController = loader.getController();
                registerController.setUp(dbService, this);
            }
            if (fxml == "login") {
                LoginController loginController = loader.getController();
                loginController.setUp(dbService, this);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void login(User user) {
        this.user = user;
        changeWindow("main");

    }
}
