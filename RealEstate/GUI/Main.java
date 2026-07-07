package gui;

import database.DBOperations;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        DBOperations dbOps = new DBOperations();
        LoginScreen login = new LoginScreen(dbOps, primaryStage);
        Scene scene = login.buildScene();        
        primaryStage.setScene(scene);
        primaryStage.setWidth(975);
        primaryStage.setHeight(650);
        primaryStage.setTitle("EstatePro - Login");
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}