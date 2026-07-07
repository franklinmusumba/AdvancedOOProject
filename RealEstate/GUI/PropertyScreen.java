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
import model.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

public class PropertyScreen {

    private final DBOperations dbOps;
    private final Agent loggedAgent;
    private TableView<Property> table;

    public PropertyScreen(DBOperations dbOps, Agent loggedAgent) {
        this.dbOps = dbOps;
        this.loggedAgent = loggedAgent;
    }

    public VBox getView() {
        VBox view = new VBox();
        view.setId("property-screen");
        view.setSpacing(10);
        view.setPadding(new Insets(10));

        HBox toolbar = new HBox();
        toolbar.setId("property-toolbar");
        toolbar.setSpacing(10);
        toolbar.setAlignment(Pos.CENTER_LEFT);

        Button addBtn = new Button("Add Property");
        addBtn.setId("add-property-btn");

        TextField searchField = new TextField();
        searchField.setId("search-field");
        searchField.setPromptText("Search by city...");

        Button searchBtn = new Button("Search");
        searchBtn.setId("search-btn");

        Button clearBtn = new Button("Clear");
        clearBtn.setId("clear-btn");

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setId("refresh-btn");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        toolbar.getChildren().addAll(addBtn, searchField, searchBtn, clearBtn, refreshBtn, spacer);

        table = new TableView<>();
        table.setId("property-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        
        TableColumn<Property, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getPropertyId()).asObject());
        idCol.setPrefWidth(60);

        TableColumn<Property, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getAddress()));
        addressCol.setPrefWidth(200);

        TableColumn<Property, String> cityCol = new TableColumn<>("City");
        cityCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCity()));
        cityCol.setPrefWidth(100);

        TableColumn<Property, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getPrice()).asObject());
        priceCol.setPrefWidth(100);

        TableColumn<Property, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getType().name()));
        typeCol.setPrefWidth(100);

        TableColumn<Property, String> statusCol = new TableColumn<>("Status");
        statusCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getStatus().name()));
        statusCol.setPrefWidth(100);

        
        TableColumn<Property, Void> actionsCol = new TableColumn<>("Actions");
        actionsCol.setPrefWidth(220);
        actionsCol.setCellFactory(col -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button sellBtn = new Button("Sell");
            private final Button deleteBtn = new Button("Delete");

            {
                editBtn.setId("edit-btn");
                sellBtn.setId("sell-btn");
                deleteBtn.setId("delete-btn");

                editBtn.setOnAction(e -> {
                    Property p = getTableView().getItems().get(getIndex());
                    showEditDialog(p);
                });

                sellBtn.setOnAction(e -> {
                    Property p = getTableView().getItems().get(getIndex());
                    showSellDialog(p);
                });

                deleteBtn.setOnAction(e -> {
                    Property p = getTableView().getItems().get(getIndex());
                    deleteProperty(p);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox box = new HBox(5, editBtn, sellBtn, deleteBtn);
                    setGraphic(box);
                }
            }
        });

        table.getColumns().addAll(idCol, addressCol, cityCol, priceCol, typeCol, statusCol, actionsCol);

       
        refreshTable();

        addBtn.setOnAction(e -> showAddDialog());
        refreshBtn.setOnAction(e -> refreshTable());

        searchBtn.setOnAction(e -> {
            String city = searchField.getText().trim();
            if (!city.isEmpty()) {
                table.setItems(FXCollections.observableArrayList(dbOps.findPropertiesByCity(city)));
            } else {
                refreshTable();
            }
        });

        clearBtn.setOnAction(e -> {
            searchField.clear();
            refreshTable();
        });

        
        searchField.setOnAction(e -> {
            String city = searchField.getText().trim();
            if (!city.isEmpty()) {
                table.setItems(FXCollections.observableArrayList(dbOps.findPropertiesByCity(city)));
            } else {
                refreshTable();
            }
        });

        view.getChildren().addAll(toolbar, table);
        return view;
    }

    public void refreshTable() {
        table.setItems(FXCollections.observableArrayList(dbOps.getAllProperties()));
    }

    public void showAddDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Property");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white;");

        TextField addressField = new TextField();
        addressField.setPromptText("Address");

        TextField cityField = new TextField();
        cityField.setPromptText("City");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");

        ComboBox<PropertyType> typeCombo = new ComboBox<>();
        typeCombo.getItems().setAll(PropertyType.values());
        typeCombo.setPromptText("Property Type");

        ComboBox<PropertyStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().setAll(PropertyStatus.values());
        statusCombo.setValue(PropertyStatus.AVAILABLE);

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String priceText = priceField.getText().trim();
            PropertyType type = typeCombo.getValue();
            PropertyStatus status = statusCombo.getValue();

            if (address.isEmpty() || city.isEmpty() || priceText.isEmpty() || type == null || status == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid price format.");
                alert.showAndWait();
                return;
            }

            Property newProperty = new Property(0, address, city, price, loggedAgent, type, status);
            int generatedId = dbOps.insertProperty(newProperty);
            if (generatedId != -1) {
                Alert success = new Alert(Alert.AlertType.INFORMATION, "Property added successfully with ID: " + generatedId);
                success.showAndWait();
                dialog.close();
                refreshTable();
            } else {
                Alert error = new Alert(Alert.AlertType.ERROR, "Failed to add property.");
                error.showAndWait();
            }
        });

        form.getChildren().addAll(addressField, cityField, priceField, typeCombo, statusCombo, saveBtn);
        Scene scene = new Scene(form, 350, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void showEditDialog(Property property) {
        Stage dialog = new Stage();
        dialog.setTitle("Edit Property - ID: " + property.getPropertyId());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white;");

        TextField addressField = new TextField(property.getAddress());
        addressField.setPromptText("Address");

        TextField cityField = new TextField(property.getCity());
        cityField.setPromptText("City");

        TextField priceField = new TextField(String.valueOf(property.getPrice()));
        priceField.setPromptText("Price");

        ComboBox<PropertyType> typeCombo = new ComboBox<>();
        typeCombo.getItems().setAll(PropertyType.values());
        typeCombo.setValue(property.getType());

        ComboBox<PropertyStatus> statusCombo = new ComboBox<>();
        statusCombo.getItems().setAll(PropertyStatus.values());
        statusCombo.setValue(property.getStatus());

        Button saveBtn = new Button("Update");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        saveBtn.setOnAction(e -> {
            String address = addressField.getText().trim();
            String city = cityField.getText().trim();
            String priceText = priceField.getText().trim();
            PropertyType type = typeCombo.getValue();
            PropertyStatus status = statusCombo.getValue();

            if (address.isEmpty() || city.isEmpty() || priceText.isEmpty() || type == null || status == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "All fields are required.");
                alert.showAndWait();
                return;
            }

            double price;
            try {
                price = Double.parseDouble(priceText);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid price format.");
                alert.showAndWait();
                return;
            }

            Property updatedProperty = new Property(
                property.getPropertyId(), address, city, price, loggedAgent, type, status
            );
            boolean success = dbOps.updateProperty(updatedProperty);
            if (success) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Property updated successfully.");
                alert.showAndWait();
                dialog.close();
                refreshTable();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to update property.");
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(addressField, cityField, priceField, typeCombo, statusCombo, saveBtn);
        Scene scene = new Scene(form, 350, 400);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // ---------- SELL PROPERTY ----------
    private void showSellDialog(Property property) {
        Stage dialog = new Stage();
        dialog.setTitle("Record Sale/Rent for " + property.getAddress());
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));
        form.setStyle("-fx-background-color: white;");

        // Client selection
        Label clientLabel = new Label("Select Client:");
        ComboBox<Client> clientCombo = new ComboBox<>();
        List<Client> allClients = dbOps.getAllClients();
        clientCombo.getItems().addAll(allClients);
        clientCombo.setPromptText("Select a client");
        clientCombo.setPrefWidth(300);

        Button addClientBtn = new Button("+ New Client");
        addClientBtn.setStyle("-fx-background-color: #27ae60; -fx-text-fill: white;");
        addClientBtn.setOnAction(e -> {
            Client newClient = showAddClientDialog();
            if (newClient != null) {
                allClients.add(newClient);
                clientCombo.getItems().add(newClient);
                clientCombo.setValue(newClient);
            }
        });

        HBox clientBox = new HBox(10, clientCombo, addClientBtn);

        // Transaction type
        Label typeLabel = new Label("Transaction Type:");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("Sale", "Rent");
        typeCombo.setValue("Sale");

        // Amount
        Label amountLabel = new Label("Amount:");
        TextField amountField = new TextField(String.valueOf(property.getPrice()));
        amountField.setPromptText("Amount");

        Button recordBtn = new Button("Record Transaction");
        recordBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white; -fx-font-weight: bold;");

        recordBtn.setOnAction(e -> {
            Client selectedClient = clientCombo.getValue();
            String transactionType = typeCombo.getValue();
            String amountText = amountField.getText().trim();

            if (selectedClient == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a client.");
                alert.showAndWait();
                return;
            }
            if (amountText.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Please enter an amount.");
                alert.showAndWait();
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountText);
            } catch (NumberFormatException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid amount format.");
                alert.showAndWait();
                return;
            }

            // Determine transaction type and new status
            TransactionType transactionTypeEnum;
            PropertyStatus newStatus;
            if ("Sale".equalsIgnoreCase(transactionType)) {
                transactionTypeEnum = TransactionType.SALE;
                newStatus = PropertyStatus.SOLD;
            } else {
                transactionTypeEnum = TransactionType.RENT;
                newStatus = PropertyStatus.RENTED;
            }

            // Create and insert transaction
            Transaction transaction = new Transaction(
                0, property, selectedClient,
                Date.valueOf(LocalDate.now()), amount, transactionTypeEnum
            );
            int transactionId = dbOps.insertTransaction(transaction);

            if (transactionId != -1) {
                // Update property status
                Property updatedProperty = new Property(
                    property.getPropertyId(), property.getAddress(), property.getCity(),
                    property.getPrice(), property.getManagingAgent(), property.getType(), newStatus
                );
                boolean statusUpdated = dbOps.updateProperty(updatedProperty);

                if (statusUpdated) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Transaction recorded!\nProperty status updated to: " + newStatus.name());
                    alert.showAndWait();
                    dialog.close();
                    refreshTable();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR,
                        "Transaction recorded but status update failed.");
                    alert.showAndWait();
                    refreshTable();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to record transaction.");
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(
            clientLabel, clientBox,
            typeLabel, typeCombo,
            amountLabel, amountField,
            recordBtn
        );

        Scene scene = new Scene(form, 400, 350);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // ---------- Helper: Add Client Dialog ----------
    private Client showAddClientDialog() {
        Stage dialog = new Stage();
        dialog.setTitle("Add New Client");
        dialog.initModality(javafx.stage.Modality.APPLICATION_MODAL);

        VBox form = new VBox(10);
        form.setPadding(new Insets(20));

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Phone Number");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        Button saveBtn = new Button("Save Client");
        saveBtn.setStyle("-fx-background-color: #1e3a5f; -fx-text-fill: white;");

        final Client[] createdClient = {null};

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

            Client client = new Client(0, firstName, lastName, phone, email);
            int id = dbOps.insertClient(client);
            if (id != -1) {
                createdClient[0] = new Client(id, firstName, lastName, phone, email);
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Client added successfully!");
                alert.showAndWait();
                dialog.close();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to add client.");
                alert.showAndWait();
            }
        });

        form.getChildren().addAll(firstNameField, lastNameField, phoneField, emailField, saveBtn);
        Scene scene = new Scene(form, 350, 350);
        dialog.setScene(scene);
        dialog.showAndWait();

        return createdClient[0];
    }

    // ---------- DELETE PROPERTY ----------
    private void deleteProperty(Property property) {
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
            "Delete property at " + property.getAddress() + "?\nThis action cannot be undone.",
            ButtonType.YES, ButtonType.NO);
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.YES) {
                boolean success = dbOps.deleteProperty(property.getPropertyId());
                Alert result = new Alert(Alert.AlertType.INFORMATION,
                    success ? "Property deleted successfully" : "Failed to delete property");
                result.showAndWait();
                if (success) refreshTable();
            }
        });
    }
}