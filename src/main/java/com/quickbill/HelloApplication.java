package com.quickbill;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    public static void main(String[] args) {
        launch();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("window.fxml"));
      //  stage.getIcons().add(new Image(HelloApplication.class.getResourceAsStream("logo.png")));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 600);
        stage.setResizable(true);
        stage.setTitle("QuickBill");
 //       stage.initStyle(StageStyle.UNDECORATED);
        stage.setMaximized(false);
        stage.setMinHeight(630);
        stage.setMinWidth(1250);
        stage.setScene(scene);
        stage.show();
    }

}