package com.example.databaseproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.converter.DoubleStringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PayingManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Paying> payingList = FXCollections.observableArrayList();
    TableView<Paying> payingTableView = new TableView<>();

    public void readPaying() {
        payingList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT p.p_id, p.type, p.type_price, p.customer_id, p.date, IFNULL(c.currency, IFNULL(i.currency, a.currency)) as currency " +
                    "FROM paying p " +
                    "LEFT JOIN cash c ON p.p_id = c.p_id " +
                    "LEFT JOIN incurrence i ON p.p_id = i.p_id " +
                    "LEFT JOIN cards a ON p.p_id = a.p_id";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                payingList.add(new Paying(rs.getInt("p_id"), rs.getString("type"), rs.getDouble("type_price"), rs.getString("currency"), rs.getInt("customer_id"), rs.getString("date")));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        payingTableView.setItems(payingList);  // Update the TableView
    }

    public BorderPane createPayingManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane payingRoot = new BorderPane();
        payingRoot.setPrefSize(800, 600);
        HBox payingTopLayout = new HBox(10);
        payingTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addPayingButton = new Button("Add Paying");
        Button deletePayingButton = new Button("Delete Paying");
        Button searchPayingButton = new Button("Search for Paying");
        Button refreshPayingButton = new Button("Refresh");
        TextField searchPayingField = new TextField();
        searchPayingField.setPromptText("Enter Paying ID");
        Button homePayingButton = new Button("Home");
        homePayingButton.setOnAction(e -> primaryStage.setScene(homeScene));

        payingTopLayout.getChildren().addAll(homePayingButton, addPayingButton, deletePayingButton, searchPayingButton, searchPayingField, refreshPayingButton);

        payingTableView.setEditable(true);

        TableColumn<Paying, Integer> p_idCol = new TableColumn<>("ID");
        p_idCol.setPrefWidth(50);
        p_idCol.setResizable(false);
        p_idCol.setCellValueFactory(new PropertyValueFactory<>("p_id"));

        TableColumn<Paying, String> typeCol = new TableColumn<>("Type");
        typeCol.setPrefWidth(100);
        typeCol.setResizable(false);
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setOnEditCommit(event -> {
            Paying paying = event.getRowValue();
            paying.setType(event.getNewValue());
            updatePaying(paying);
        });

        TableColumn<Paying, Double> type_priceCol = new TableColumn<>("Price");
        type_priceCol.setPrefWidth(100);
        type_priceCol.setResizable(false);
        type_priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        type_priceCol.setCellValueFactory(new PropertyValueFactory<>("type_price"));
        type_priceCol.setOnEditCommit(event -> {
            Paying paying = event.getRowValue();
            paying.setType_price(event.getNewValue());
            updatePaying(paying);
        });

        TableColumn<Paying, String> currencyCol = new TableColumn<>("Currency");
        currencyCol.setPrefWidth(100);
        currencyCol.setResizable(false);
        currencyCol.setCellFactory(TextFieldTableCell.forTableColumn());
        currencyCol.setCellValueFactory(new PropertyValueFactory<>("currency"));
        currencyCol.setOnEditCommit(event -> {
            Paying paying = event.getRowValue();
            paying.setCurrency(event.getNewValue());
            updatePaying(paying);
        });

        TableColumn<Paying, Integer> customerIdCol = new TableColumn<>("Customer ID");
        customerIdCol.setPrefWidth(100);
        customerIdCol.setResizable(false);
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));
        customerIdCol.setOnEditCommit(event -> {
            Paying paying = event.getRowValue();
            paying.setCustomer_id(event.getNewValue());
            updatePaying(paying);
        });

        TableColumn<Paying, String> dateCol = new TableColumn<>("Date");
        dateCol.setPrefWidth(100);
        dateCol.setResizable(false);
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        payingTableView.getColumns().addAll(p_idCol, typeCol, type_priceCol, currencyCol, customerIdCol, dateCol);
        payingTableView.setItems(payingList);

        payingRoot.setTop(payingTopLayout);
        payingRoot.setCenter(payingTableView);

        addPayingButton.setOnAction(e -> showAddPayingDialog());
        deletePayingButton.setOnAction(e -> {
            String idText = searchPayingField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deletePaying(id);
                readPaying();
            }
        });
        searchPayingButton.setOnAction(e -> {
            String idText = searchPayingField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchPaying(id);
            }
        });
        refreshPayingButton.setOnAction(e -> readPaying());

        return payingRoot;
    }

    private void updatePaying(Paying paying) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE paying SET type=?, type_price=?, customer_id=?, date=? WHERE p_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, paying.getType());
            pstmt.setDouble(2, paying.getType_price());
            pstmt.setInt(3, paying.getCustomer_id());
            pstmt.setString(4, paying.getDate());
            pstmt.setInt(5, paying.getP_id());
            pstmt.executeUpdate();

            switch (paying.getType()) {
                case "Cash":
                    sql = "UPDATE cash SET currency=? WHERE p_id=?";
                    break;
                case "Incurrence":
                    sql = "UPDATE incurrence SET currency=? WHERE p_id=?";
                    break;
                case "Cards":
                    sql = "UPDATE cards SET currency=? WHERE p_id=?";
                    break;
                default:
                    throw new IllegalArgumentException("Invalid payment type");
            }
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, paying.getCurrency());
            pstmt.setInt(2, paying.getP_id());
            pstmt.executeUpdate();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deletePaying(int id) {
        try {
            Connection con = db.getConnection().connectDB();

            // First, delete from the child tables
            String[] childTables = {"cash", "incurrence", "cards"};
            for (String table : childTables) {
                String sql = "DELETE FROM " + table + " WHERE p_id=?";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, id);
                pstmt.executeUpdate();
            }

            // Then, delete from the parent table
            String sql = "DELETE FROM paying WHERE p_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchPaying(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT p.p_id, p.type, p.type_price, p.customer_id, p.date, IFNULL(c.currency, IFNULL(i.currency, a.currency)) as currency " +
                    "FROM paying p " +
                    "LEFT JOIN cash c ON p.p_id = c.p_id " +
                    "LEFT JOIN incurrence i ON p.p_id = i.p_id " +
                    "LEFT JOIN cards a ON p.p_id = a.p_id " +
                    "WHERE p.p_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                double type_price = rs.getDouble("type_price");
                int customer_id = rs.getInt("customer_id");
                String date = rs.getString("date");
                String currency = rs.getString("currency");
                showAlert(Alert.AlertType.INFORMATION, "Paying Found", "Details:\n" +
                        "Type: " + type + "\n" +
                        "Type Price: " + type_price + "\n" +
                        "Customer ID: " + customer_id + "\n" +
                        "Date: " + date + "\n" +
                        "Currency: " + currency);
            } else {
                showAlert(Alert.AlertType.WARNING, "Paying Not Found", "The paying with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddPayingDialog() {
        Stage addPayingStage = new Stage();
        VBox addPayingLayout = new VBox(10);
        addPayingLayout.setStyle("-fx-padding: 20;");

        TextField typeField = new TextField();
        typeField.setPromptText("Type");
        TextField type_priceField = new TextField();
        type_priceField.setPromptText("Type Price");
        TextField customerIdField = new TextField();
        customerIdField.setPromptText("Customer ID");
        DatePicker dateField = new DatePicker();
        dateField.setPromptText("Date");

        MenuButton typeMenuButton = new MenuButton("Select Type");
        MenuItem cashItem = new MenuItem("Cash");
        MenuItem incurrenceItem = new MenuItem("Incurrence");
        MenuItem cardsItem = new MenuItem("Cards");
        typeMenuButton.getItems().addAll(cashItem, incurrenceItem, cardsItem);

        MenuButton currencyMenuButton = new MenuButton("Select Currency");
        MenuItem dollarItem = new MenuItem("Dollar");
        MenuItem shekelItem = new MenuItem("Shekel");
        MenuItem dinarItem = new MenuItem("Dinar");
        currencyMenuButton.getItems().addAll(dollarItem, shekelItem, dinarItem);

        final String[] selectedCurrency = new String[1];

        dollarItem.setOnAction(e -> {
            selectedCurrency[0] = "Dollar";
            currencyMenuButton.setText("Dollar");
        });
        shekelItem.setOnAction(e -> {
            selectedCurrency[0] = "Shekel";
            currencyMenuButton.setText("Shekel");
        });
        dinarItem.setOnAction(e -> {
            selectedCurrency[0] = "Dinar";
            currencyMenuButton.setText("Dinar");
        });

        cashItem.setOnAction(e -> {
            typeField.setText("Cash");
            typeMenuButton.setText("Cash");
        });
        incurrenceItem.setOnAction(e -> {
            typeField.setText("Incurrence");
            typeMenuButton.setText("Incurrence");
        });
        cardsItem.setOnAction(e -> {
            typeField.setText("Cards");
            typeMenuButton.setText("Cards");
        });

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO paying (type, type_price, customer_id, date) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, typeField.getText());
                pstmt.setDouble(2, Double.parseDouble(type_priceField.getText()));
                pstmt.setInt(3, Integer.parseInt(customerIdField.getText()));
                pstmt.setString(4, dateField.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE));
                pstmt.executeUpdate();

                ResultSet rs = pstmt.getGeneratedKeys();
                int p_id = 0;
                if (rs.next()) {
                    p_id = rs.getInt(1);
                }

                switch (typeField.getText()) {
                    case "Cash":
                        sql = "INSERT INTO cash (p_id, currency) VALUES (?, ?)";
                        break;
                    case "Incurrence":
                        sql = "INSERT INTO incurrence (p_id, currency) VALUES (?, ?)";
                        break;
                    case "Cards":
                        sql = "INSERT INTO cards (p_id, currency) VALUES (?, ?)";
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid payment type");
                }
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, p_id);
                pstmt.setString(2, selectedCurrency[0]);
                pstmt.executeUpdate();

                payingList.add(new Paying(p_id, typeField.getText(), Double.parseDouble(type_priceField.getText()), selectedCurrency[0], Integer.parseInt(customerIdField.getText()), dateField.getValue().format(DateTimeFormatter.ISO_LOCAL_DATE)));
                con.close();
                readPaying();
                addPayingStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addPayingLayout.getChildren().addAll(typeMenuButton, typeField, type_priceField, customerIdField, dateField, currencyMenuButton, addBtn);
        Scene addPayingScene = new Scene(addPayingLayout);
        addPayingStage.setScene(addPayingScene);
        addPayingStage.setTitle("Add Paying");
        addPayingStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}