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
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class StockManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Stock> StockList = FXCollections.observableArrayList();
    TableView<Stock> stockTableView = new TableView<>();

    public void readStock() {
        StockList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM stock";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                StockList.add(
                        new Stock(rs.getInt(1), rs.getString(3), rs.getInt(2)));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        stockTableView.setItems(StockList);  // Update the TableView
    }

    public BorderPane createStockManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane stockRoot = new BorderPane();
        stockRoot.setPrefSize(800, 600);
        HBox stockTopLayout = new HBox(10);
        stockTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addStockButton = new Button("Add Stock");
        Button deleteStockButton = new Button("Delete Stock");
        Button searchStockButton = new Button("Search for Stock");
        Button refreshStockButton = new Button("Refresh");
        Button showProductsButton = new Button("Show Products");
        TextField searchStockField = new TextField();
        searchStockField.setPromptText("Enter Stock ID");
        Button homeStockButton = new Button("Home");
        homeStockButton.setOnAction(e -> primaryStage.setScene(homeScene));

        stockTopLayout.getChildren().addAll(homeStockButton, addStockButton, deleteStockButton, searchStockButton, searchStockField, refreshStockButton, showProductsButton);

        stockTableView.setEditable(true);

        TableColumn<Stock, Integer> st_idCol = new TableColumn<>("ID");
        st_idCol.setPrefWidth(50);
        st_idCol.setResizable(false);
        st_idCol.setCellValueFactory(new PropertyValueFactory<>("ST_id"));

        TableColumn<Stock, String> sh_nameCol = new TableColumn<>("Name");
        sh_nameCol.setPrefWidth(100);
        sh_nameCol.setResizable(false);
        sh_nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        sh_nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        sh_nameCol.setOnEditCommit(event -> {
            Stock stock = event.getRowValue();
            stock.setName(event.getNewValue());
            updateStock(stock);
        });

        TableColumn<Stock, Integer> sizeCol = new TableColumn<>("Size");
        sizeCol.setPrefWidth(100);
        sizeCol.setResizable(false);
        sizeCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        sizeCol.setCellValueFactory(new PropertyValueFactory<>("size"));
        sizeCol.setOnEditCommit(event -> {
            Stock stock = event.getRowValue();
            stock.setSize(event.getNewValue());
            updateStock(stock);
        });

        stockTableView.getColumns().addAll(st_idCol, sh_nameCol, sizeCol);
        stockTableView.setItems(StockList);

        stockRoot.setTop(stockTopLayout);
        stockRoot.setCenter(stockTableView);

        addStockButton.setOnAction(e -> showAddStockDialog());
        deleteStockButton.setOnAction(e -> {
            String idText = searchStockField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteStock(id);
                readStock();
            }
        });
        searchStockButton.setOnAction(e -> {
            String idText = searchStockField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchStock(id);
            }
        });
        refreshStockButton.setOnAction(e -> readStock());

        showProductsButton.setOnAction(e -> {
            String idText = searchStockField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                showProducts(id);
            }
        });

        return stockRoot;
    }

    private void updateStock(Stock stock) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE stock SET shname=?, size=? WHERE ST_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, stock.getName());
            pstmt.setInt(2, stock.getSize());
            pstmt.setInt(3, stock.getST_id());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteStock(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM stock WHERE ST_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchStock(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM stock WHERE ST_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String size = rs.getString("size");
                String sh_name = rs.getString("shname");
                showAlert(Alert.AlertType.INFORMATION, "Stock Found", "Details:\n" +
                        "ID: " + id + "\n" +
                        "Name: " + sh_name + "\n" +
                        "Size: " + size + "\n"
                );
            } else {
                showAlert(Alert.AlertType.WARNING, "Stock Not Found", "The stock with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddStockDialog() {
        Stage addStockStage = new Stage();
        VBox addStockLayout = new VBox(10);
        addStockLayout.setStyle("-fx-padding: 20;");

        TextField sh_nameField = new TextField();
        sh_nameField.setPromptText("Name");
        TextField sizeField = new TextField();
        sizeField.setPromptText("Size");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO stock (shname, size) VALUES (?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, sh_nameField.getText());
                pstmt.setInt(2, Integer.parseInt(sizeField.getText()));
                pstmt.executeUpdate();
                StockList.add(new Stock(sh_nameField.getText(), Integer.parseInt(sizeField.getText())));
                con.close();
                readStock();
                addStockStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addStockLayout.getChildren().addAll(sh_nameField, sizeField, addBtn);
        Scene addStockScene = new Scene(addStockLayout);
        addStockStage.setScene(addStockScene);
        addStockStage.setTitle("Add Stock");
        addStockStage.show();
    }

    private void showProducts(int stockId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM product WHERE stock_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, stockId);
            ResultSet rs = pstmt.executeQuery();
            StringBuilder products = new StringBuilder("Products:\n");
            while (rs.next()) {
                products.append("ID: ").append(rs.getInt("id"))
                        .append(", Price: ").append(rs.getDouble("price"))
                        .append(", Brand: ").append(rs.getString("brand"))
                        .append(", Color: ").append(rs.getString("color"))
                        .append("\n");
            }
            if (products.length() == 10) {
                showAlert(Alert.AlertType.WARNING, "No Products", "No products found for the stock with ID " + stockId + ".");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Products", products.toString());
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
