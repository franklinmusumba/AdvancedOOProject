package gui;
import database.DBOperations;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Client;
public class ClientScreen {
    private final DBOperations dbOps;
    private TableView<Client> table;
    
    public ClientScreen(DBOperations dbOps) {
        this.dbOps = dbOps;
    }

    public VBox getView() {
        VBox view = new VBox();
        view.setId("client-screen");
        view.setSpacing(10);
        view.setPadding(new Insets(10));

        // ---------- Toolbar ----------
        HBox toolbar = new HBox();
        toolbar.setId("client-toolbar");
        toolbar.setSpacing(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Client");
        addBtn.setId("add-client-btn");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setId("refresh-btn");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(addBtn, refreshBtn, spacer);

        // ---------- Table ----------
        table = new TableView<>();
        table.setId("client-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns
        TableColumn<Client, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        idCol.setPrefWidth(60);

        TableColumn<Client, String> firstNameCol = new TableColumn<>("First Name");
        firstNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getFirstName()));
        firstNameCol.setPrefWidth(150);

        TableColumn<Client, String> lastNameCol = new TableColumn<>("Last Name");
        lastNameCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getLastName()));
        lastNameCol.setPrefWidth(150);

        TableColumn<Client, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getPhone()));
        phoneCol.setPrefWidth(120);

        TableColumn<Client, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getEmail()));
        emailCol.setPrefWidth(200);

        // Actions column
        TableColumn<Client, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(150);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setId("edit-btn");
                deleteBtn.setId("delete-btn");

                editBtn.setOnAction(e -> {
                    Client c = getTableView().getItems().get(getIndex());
                    showEditDialog(c);
                });

                deleteBtn.setOnAction(e -> {
                    Client c = getTableView().getItems().get(getIndex());
                    deleteClient(c);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, editBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, firstNameCol, lastNameCol, phoneCol, emailCol, actionsCol);

        // Load initial data
        refreshTable();

        // ---------- Event Handlers ----------
        addBtn.setOnAction(e -> showAddDialog());
        refreshBtn.setOnAction(e -> refreshTable());

        view.getChildren().addAll(toolbar, table);
        return view;
    }

    private void refreshTable() {
        table.setItems(FXCollections.observableArrayList(dbOps.getAllClients()));
    }

    private void showAddDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Client");
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

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "First Name and Last Name are required.");
                alert.showAndWait();
                return;
            }

            Client newClient = new Client(0, firstName, lastName, phone, email);
            int generatedId = dbOps.insertClient(newClient);
            if (generatedId != -1) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Client added successfully with ID: " + generatedId);
                success.showAndWait();
                dialog.close();
                refreshTable();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to add client.");
                error.showAndWait();
            }
        });

        form.getChildren().addAll(firstNameField, lastNameField, phoneField, emailField, saveBtn);
        Scene scene = new Scene(form, 350, 350);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // ---------- EDIT CLIENT ----------
    public void showEditDialog(Client client) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Client - ID: " + client.getId());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white;");

        TextField firstNameField = new TextField(client.getFirstName());
        firstNameField.setPromptText("First Name");

        TextField lastNameField = new TextField(client.getLastName());
        lastNameField.setPromptText("Last Name");

        TextField phoneField = new TextField(client.getPhone());
        phoneField.setPromptText("Phone Number");

        TextField emailField = new TextField(client.getEmail());
        emailField.setPromptText("Email");

        Button saveBtn = new Button("Update");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String firstName = firstNameField.getText().trim();
            String lastName = lastNameField.getText().trim();
            String phone = phoneField.getText().trim();
            String email = emailField.getText().trim();

            if (firstName.isEmpty() || lastName.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "First Name and Last Name are required.");
                alert.showAndWait();
                return;
            }

            Client updatedClient = new Client(client.getId(), firstName, lastName, phone, email);
            boolean success = dbOps.updateClient(updatedClient);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Client updated successfully.");
                alert.showAndWait();
                dialog.close();
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update client.");
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(firstNameField, lastNameField, phoneField, emailField, saveBtn);
        Scene scene = new Scene(form, 350, 350);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    
    public void deleteClient(Client client) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete " + client.getFirstName() + " " + client.getLastName() + "?\nThis action cannot be undone.",
            ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = dbOps.deleteClient(client.getId());
                Alert result = new Alert(Alert.AlertType.INFORMATION,
                    success ? "Client deleted successfully" : "Failed to delete client");
                result.showAndWait();
                if (success) refreshTable();
            }
        });
    }
}
