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
import javafx.util.converter.IntegerStringConverter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class ProductManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Product> productList = FXCollections.observableArrayList();
    TableView<Product> productTableView = new TableView<>();

    public void readProduct() {
        productList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM product";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                productList.add(
                        new Product(rs.getInt(1), rs.getDouble(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        productTableView.setItems(productList);  // Update the TableView
    }

    public BorderPane createProductManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane productRoot = new BorderPane();
        productRoot.setPrefSize(800, 600);
        HBox productTopLayout = new HBox(10);
        productTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addProductButton = new Button("Add Product");
        Button deleteProductButton = new Button("Delete Product");
        Button searchProductButton = new Button("Search for Product");
        Button refreshProductButton = new Button("Refresh");
        TextField searchProductField = new TextField();
        searchProductField.setPromptText("Enter Product ID");
        Button homeProductButton = new Button("Home");
        homeProductButton.setOnAction(e -> primaryStage.setScene(homeScene));

        productTopLayout.getChildren().addAll(homeProductButton, addProductButton, deleteProductButton, searchProductButton, searchProductField, refreshProductButton);

        productTableView.setEditable(true);

        TableColumn<Product, Integer> idCol = new TableColumn<>("ID");
        idCol.setPrefWidth(50);
        idCol.setResizable(false);
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Product, Double> priceCol = new TableColumn<>("Price");
        priceCol.setPrefWidth(100);
        priceCol.setResizable(false);
        priceCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setPrice(event.getNewValue());
            updateProduct(product);
        });

        TableColumn<Product, String> brandCol = new TableColumn<>("Brand");
        brandCol.setPrefWidth(100);
        brandCol.setResizable(false);
        brandCol.setCellFactory(TextFieldTableCell.forTableColumn());
        brandCol.setCellValueFactory(new PropertyValueFactory<>("brand"));
        brandCol.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setBrand(event.getNewValue());
            updateProduct(product);
        });

        TableColumn<Product, String> colorCol = new TableColumn<>("Color");
        colorCol.setPrefWidth(100);
        colorCol.setResizable(false);
        colorCol.setCellFactory(TextFieldTableCell.forTableColumn());
        colorCol.setCellValueFactory(new PropertyValueFactory<>("color"));
        colorCol.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setColor(event.getNewValue());
            updateProduct(product);
        });

        TableColumn<Product, Integer> stockIdCol = new TableColumn<>("Stock ID");
        stockIdCol.setPrefWidth(100);
        stockIdCol.setResizable(false);
        stockIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        stockIdCol.setCellValueFactory(new PropertyValueFactory<>("stock_id"));
        stockIdCol.setOnEditCommit(event -> {
            Product product = event.getRowValue();
            product.setStock_id(event.getNewValue());
            updateProduct(product);
        });

        productTableView.getColumns().addAll(idCol, priceCol, brandCol, colorCol, stockIdCol);
        productTableView.setItems(productList);

        productRoot.setTop(productTopLayout);
        productRoot.setCenter(productTableView);

        addProductButton.setOnAction(e -> showAddProductDialog());
        deleteProductButton.setOnAction(e -> {
            String idText = searchProductField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteProduct(id);
                readProduct();
            }
        });
        searchProductButton.setOnAction(e -> {
            String idText = searchProductField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchProduct(id);
            }
        });
        refreshProductButton.setOnAction(e -> readProduct());

        return productRoot;
    }

    private void updateProduct(Product product) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE product SET price=?, brand=?, color=?, stock_id=? WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setDouble(1, product.getPrice());
            pstmt.setString(2, product.getBrand());
            pstmt.setString(3, product.getColor());
            pstmt.setInt(4, product.getStock_id());
            pstmt.setInt(5, product.getId());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteProduct(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM product WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchProduct(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM product WHERE id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                double price = rs.getDouble("price");
                String brand = rs.getString("brand");
                String color = rs.getString("color");
                int stock_id = rs.getInt("stock_id");
                showAlert(Alert.AlertType.INFORMATION, "Product Found", "Details:\n" +
                        "Price: " + price + "\n" +
                        "Brand: " + brand + "\n" +
                        "Color: " + color + "\n" +
                        "Stock ID: " + stock_id);
            } else {
                showAlert(Alert.AlertType.WARNING, "Product Not Found", "The product with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddProductDialog() {
        Stage addProductStage = new Stage();
        VBox addProductLayout = new VBox(10);
        addProductLayout.setStyle("-fx-padding: 20;");

        TextField priceField = new TextField();
        priceField.setPromptText("Price");
        TextField brandField = new TextField();
        brandField.setPromptText("Brand");
        TextField colorField = new TextField();
        colorField.setPromptText("Color");
        TextField stockIdField = new TextField();
        stockIdField.setPromptText("Stock ID");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                int stockId = Integer.parseInt(stockIdField.getText());
                if (!isStockExist(stockId)) {
                    showAlert(Alert.AlertType.ERROR, "Stock Not Found", "The stock with ID " + stockId + " does not exist.");
                    return;
                }

                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO product (price, brand, color, stock_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setDouble(1, Double.parseDouble(priceField.getText()));
                pstmt.setString(2, brandField.getText());
                pstmt.setString(3, colorField.getText());
                pstmt.setInt(4, stockId);
                pstmt.executeUpdate();
                productList.add(new Product(0, Double.parseDouble(priceField.getText()), brandField.getText(), colorField.getText(), stockId));
                con.close();
                readProduct();
                addProductStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addProductLayout.getChildren().addAll(priceField, brandField, colorField, stockIdField, addBtn);
        Scene addProductScene = new Scene(addProductLayout);
        addProductStage.setScene(addProductScene);
        addProductStage.setTitle("Add Product");
        addProductStage.show();
    }

    private boolean isStockExist(int stockId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT 1 FROM stock WHERE ST_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, stockId);
            ResultSet rs = pstmt.executeQuery();
            boolean exists = rs.next();
            rs.close();
            pstmt.close();
            con.close();
            return exists;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
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
