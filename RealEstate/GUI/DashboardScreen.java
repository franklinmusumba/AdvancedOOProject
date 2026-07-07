package gui;

import database.DBOperations;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Agent;

public class DashboardScreen {

    private final Stage primaryStage;
    private final Agent loggedAgent;
    private final DBOperations dbOps;
    private BorderPane view;

    public DashboardScreen(Stage primaryStage, Agent loggedAgent, DBOperations dbOps) {
        this.primaryStage = primaryStage;
        this.loggedAgent = loggedAgent;
        this.dbOps = dbOps;
        buildView();
    }

    public BorderPane getView() {
        return view;
    }

    private void buildView() {
        view = new BorderPane();
        view.setId("dashboard-root");

        HBox topBar = new HBox();
        topBar.setId("top-bar");
        topBar.setSpacing(15);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 20, 10, 20));

        Label welcome = new Label("Welcome, " + loggedAgent.getFirstName() + " " + loggedAgent.getLastName());
        welcome.setId("welcome-label");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button addAgentBtn = new Button("+ Add Agent");
        addAgentBtn.setId("add-agent-btn");
        boolean isAdmin = "admin@example.com".equals(loggedAgent.getEmail());
        addAgentBtn.setVisible(isAdmin);
        addAgentBtn.setManaged(isAdmin);
        addAgentBtn.setOnAction(e -> showAddAgentDialog());

        Button logoutBtn = new Button("Logout");
        logoutBtn.setId("logout-btn");
        logoutBtn.setOnAction(e -> {
            LoginScreen login = new LoginScreen(dbOps, primaryStage);
            Scene scene = login.buildScene();
            primaryStage.setScene(scene);
            primaryStage.setTitle("EstatePro - Login");
        });

        topBar.getChildren().addAll(welcome, spacer, addAgentBtn, logoutBtn);

        VBox navPanel = new VBox();
        navPanel.setId("nav-panel");
        navPanel.setSpacing(10);
        navPanel.setPadding(new Insets(20, 10, 20, 10));

        Button propertiesBtn = createNavButton("Properties");
        Button clientsBtn = createNavButton("Clients");
        Button transactionsBtn = createNavButton("Transactions");

        navPanel.getChildren().addAll(propertiesBtn, clientsBtn, transactionsBtn);

        VBox centerContent = new VBox();
        centerContent.setId("center-content");
        centerContent.setPadding(new Insets(20));
        centerContent.setSpacing(20);

        // Default: show Properties
        PropertyScreen propertyScreen = new PropertyScreen(dbOps, loggedAgent);
        centerContent.getChildren().add(propertyScreen.getView());

        propertiesBtn.setOnAction(e -> {
            centerContent.getChildren().clear();
            centerContent.getChildren().add(new PropertyScreen(dbOps, loggedAgent).getView());
        });

        clientsBtn.setOnAction(e -> {
            centerContent.getChildren().clear();
            centerContent.getChildren().add(new ClientScreen(dbOps).getView());
        });

        transactionsBtn.setOnAction(e -> {
            centerContent.getChildren().clear();
            centerContent.getChildren().add(new TransactionScreen(dbOps).getView());
        });

        view.setTop(topBar);
        view.setLeft(navPanel);
        view.setCenter(centerContent);
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setId("nav-button");
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setAlignment(Pos.CENTER_LEFT);
        return btn;
    }

    private void showAddAgentDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Agent");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");

        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();
            String password = passwordField.getText();

            if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "First Name, Last Name, Email and Password are required.");
                alert.showAndWait();
                return;
            }

            Agent newAgent = new Agent(0, firstName, lastName, phone, email, password);
            int generatedId = dbOps.insertAgent(newAgent);

            if (generatedId != -1) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Agent added successfully with ID: " + generatedId);
                success.showAndWait();
                dialog.close();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to add agent. Check logs.");
                error.showAndWait();
            }
        });

        form.getChildren().addAll(firstNameField, lastNameField, phoneField, emailField, passwordField, saveBtn);
        Scene scene = new Scene(form, 350, 350);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
}