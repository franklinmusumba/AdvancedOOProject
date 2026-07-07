package gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import database.DBOperations;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.shape.Circle;
import model.Agent;


public class LoginScreen {

    private final DBOperations dbOps;
    private final Stage primaryStage;

    public LoginScreen(DBOperations dbOps, Stage primaryStage) {
        this.dbOps = dbOps;
        this.primaryStage = primaryStage;
    }

    public Scene buildScene() {
        String css = getClass().getResource("style.css").toExternalForm();

        VBox leftPane = new VBox();
        leftPane.setPrefSize(487.5, 650);
        leftPane.setId("leftScreen");
        leftPane.setAlignment(Pos.CENTER);
        leftPane.setSpacing(15);
        VBox.setVgrow(leftPane, Priority.ALWAYS);
        Label companyName = new Label("EstatePro");
        companyName.setId("title");
        
        Image logo = new Image(getClass().getResourceAsStream("RealEstateLogo.jpeg"));
        ImageView view = new ImageView(logo);
        view.setFitHeight(50);
        view.setFitWidth(50);
        
        Circle clip = new Circle();
        clip.setCenterX(25);
        clip.setCenterY(25);
        clip.setRadius(25);
        view.setClip(clip);
        
        leftPane.getChildren().addAll(view,companyName);

        VBox rightPane = new VBox();
        rightPane.setId("rightPane");
        rightPane.setPrefSize(487.5, 650);
        rightPane.setAlignment(Pos.CENTER);
        VBox.setVgrow(rightPane, Priority.ALWAYS);
        VBox form = new VBox();
        form.setPrefSize(390, 450);
        form.setId("form");
        form.setPadding(new Insets(50));
        form.setSpacing(25);
        form.setAlignment(Pos.CENTER);

        Label formTitle = new Label("Sign In");
        formTitle.setId("form-title");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        emailField.setPrefSize(290, 40);
        emailField.setId("email-field");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Password");
        passField.setPrefSize(290, 40);
        passField.setId("password-field");

        HBox row3 = new HBox();
        row3.setPrefSize(290, 40);
        row3.setId("lastRow");
        row3.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(row3, Priority.ALWAYS);
        
        Button sign = new Button("Sign In");
        sign.setPrefSize(120, 40);
        sign.setOnAction(e->{
            DBOperations dbo = new DBOperations();
            String email = emailField.getText();
            String password = passField.getText();
            Agent loggedAgent = dbo.authenticate(email, password);
            if(loggedAgent!=null){
                DashboardScreen dbs = new DashboardScreen(primaryStage, loggedAgent, dbOps);
                Scene scene = new Scene(dbs.getView(),1024,600);
                scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
                primaryStage.setScene(scene);
            }
            else{
                System.out.println("no");
            }
        });
        
        sign.setId("sign-in");

        row3.getChildren().addAll(sign);
        row3.setAlignment(Pos.CENTER);
        
        form.getChildren().addAll(formTitle, emailField, passField, row3);
        rightPane.getChildren().add(form);

        HBox mainScreen = new HBox();
        mainScreen.getChildren().addAll(leftPane, rightPane);
        HBox.setHgrow(mainScreen, Priority.ALWAYS);
        Scene scene = new Scene(mainScreen);
        scene.getStylesheets().add(css);

        return scene;
    }
}