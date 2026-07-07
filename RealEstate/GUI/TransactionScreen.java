package gui;

import database.DBOperations;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import model.Property;
import model.Transaction;

import java.util.List;

public class TransactionScreen {

    private final DBOperations dbOps;
    private TableView<Transaction> table;
    private ComboBox<Property> propertyCombo;

    public TransactionScreen(DBOperations dbOps) {
        this.dbOps = dbOps;
    }

    public VBox getView() {
        VBox view = new VBox();
        view.setId("transaction-screen");
        view.setSpacing(10);
        view.setPadding(new Insets(10));

        HBox selectorBox = new HBox();
        selectorBox.setId("transaction-selector");
        selectorBox.setSpacing(10);
        selectorBox.setAlignment(Pos.CENTER_LEFT);

        Label selectLabel = new Label("Select Property:");
        selectLabel.setId("select-label");

        propertyCombo = new ComboBox<>();
        propertyCombo.setId("property-combo");
        propertyCombo.setPromptText("Choose a property");
        propertyCombo.setPrefWidth(300);

        Button refreshBtn = new Button("Refresh");
        refreshBtn.setId("refresh-btn");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        selectorBox.getChildren().addAll(selectLabel, propertyCombo, refreshBtn, spacer);

        // ---------- Transaction Table ----------
        table = new TableView<>();
        table.setId("transaction-table");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Columns
        TableColumn<Transaction, Integer> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getTransactionId()).asObject());
        idCol.setPrefWidth(60);

        TableColumn<Transaction, String> propertyCol = new TableColumn<>("Property");
        propertyCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProperty().getAddress()));
        propertyCol.setPrefWidth(200);

        TableColumn<Transaction, String> clientCol = new TableColumn<>("Client");
        clientCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(
                c.getValue().getClient().getFirstName() + " " + c.getValue().getClient().getLastName()));
        clientCol.setPrefWidth(150);

        TableColumn<Transaction, Double> amountCol = new TableColumn<>("Amount");
        amountCol.setCellValueFactory(c -> new javafx.beans.property.SimpleDoubleProperty(c.getValue().getAmount()).asObject());
        amountCol.setPrefWidth(100);

        TableColumn<Transaction, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTransactionType().name()));
        typeCol.setPrefWidth(80);

        TableColumn<Transaction, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTransactionDate().toString()));
        dateCol.setPrefWidth(120);

        table.getColumns().addAll(idCol, propertyCol, clientCol, amountCol, typeCol, dateCol);

        // ---------- Event Handlers ----------
        refreshBtn.setOnAction(e -> {
            refreshPropertyList();
            Property selected = propertyCombo.getValue();
            if (selected != null) {
                loadTransactions();
            }
        });

        propertyCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                loadTransactions();
            }
        });

        // Load initial data
        refreshPropertyList();

        view.getChildren().addAll(selectorBox, table);
        return view;
    }

    // ---------- Public Methods ----------
    public void refreshPropertyList() {
        try {
            List<Property> properties = dbOps.getAllProperties();
            propertyCombo.setItems(FXCollections.observableArrayList(properties));
            if (!properties.isEmpty()) {
                propertyCombo.getSelectionModel().selectFirst();
            }
        } catch (Exception e) {
            propertyCombo.setPlaceholder(new Label("Failed to load properties: " + e.getMessage()));
        }
    }

    public void loadTransactions() {
        Property selected = propertyCombo.getValue();
        if (selected == null) {
            table.setItems(FXCollections.observableArrayList());
            table.setPlaceholder(new Label("Select a property to view transactions"));
            return;
        }

        try {
            List<Transaction> transactions = dbOps.getTransactionsByProperty(selected.getPropertyId());
            table.setItems(FXCollections.observableArrayList(transactions));
            if (transactions.isEmpty()) {
                table.setPlaceholder(new Label("No transactions for this property"));
            } else {
                table.setPlaceholder(new Label(""));
            }
        } catch (Exception e) {
            table.setItems(FXCollections.observableArrayList());
            table.setPlaceholder(new Label("Error loading transactions: " + e.getMessage()));
        }
    }
}