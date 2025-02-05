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

public class SaleManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Sale> saleList = FXCollections.observableArrayList();
    TableView<Sale> saleTableView = new TableView<>();

    public void readSale() {
        saleList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM sale";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                saleList.add(
                        new Sale(rs.getInt(1), rs.getInt(2), rs.getString(3), rs.getDouble(4), rs.getInt(5)));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        saleTableView.setItems(saleList);  // Update the TableView
    }

    public BorderPane createSaleManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane saleRoot = new BorderPane();
        saleRoot.setPrefSize(800, 600);
        HBox saleTopLayout = new HBox(10);
        saleTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addSaleButton = new Button("Add Sale");
        Button deleteSaleButton = new Button("Delete Sale");
        Button searchSaleButton = new Button("Search for Sale");
        Button refreshSaleButton = new Button("Refresh");
        TextField searchSaleField = new TextField();
        searchSaleField.setPromptText("Enter Sale Number");
        Button homeSaleButton = new Button("Home");
        homeSaleButton.setOnAction(e -> primaryStage.setScene(homeScene));

        saleTopLayout.getChildren().addAll(homeSaleButton, addSaleButton, deleteSaleButton, searchSaleButton, searchSaleField, refreshSaleButton);

        saleTableView.setEditable(true);

        TableColumn<Sale, Integer> sale_numberCol = new TableColumn<>("Sale Number");
        sale_numberCol.setPrefWidth(50);
        sale_numberCol.setResizable(false);
        sale_numberCol.setCellValueFactory(new PropertyValueFactory<>("sale_number"));

        TableColumn<Sale, Integer> quantityCol = new TableColumn<>("Quantity");
        quantityCol.setPrefWidth(100);
        quantityCol.setResizable(false);
        quantityCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        quantityCol.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        quantityCol.setOnEditCommit(event -> {
            Sale sale = event.getRowValue();
            sale.setQuantity(event.getNewValue());
            updateSale(sale);
        });

        TableColumn<Sale, String> periodCol = new TableColumn<>("Period");
        periodCol.setPrefWidth(100);
        periodCol.setResizable(false);
        periodCol.setCellFactory(TextFieldTableCell.forTableColumn());
        periodCol.setCellValueFactory(new PropertyValueFactory<>("period"));
        periodCol.setOnEditCommit(event -> {
            Sale sale = event.getRowValue();
            sale.setPeriod(event.getNewValue());
            updateSale(sale);
        });

        TableColumn<Sale, Double> s_rateCol = new TableColumn<>("S Rate");
        s_rateCol.setPrefWidth(100);
        s_rateCol.setResizable(false);
        s_rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        s_rateCol.setCellValueFactory(new PropertyValueFactory<>("s_rate"));
        s_rateCol.setOnEditCommit(event -> {
            Sale sale = event.getRowValue();
            sale.setS_rate(event.getNewValue());
            updateSale(sale);
        });

        TableColumn<Sale, Integer> productIdCol = new TableColumn<>("Product ID");
        productIdCol.setPrefWidth(100);
        productIdCol.setResizable(false);
        productIdCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        productIdCol.setOnEditCommit(event -> {
            Sale sale = event.getRowValue();
            sale.setProduct_id(event.getNewValue());
            updateSale(sale);
        });

        saleTableView.getColumns().addAll(sale_numberCol, quantityCol, periodCol, s_rateCol, productIdCol);
        saleTableView.setItems(saleList);

        saleRoot.setTop(saleTopLayout);
        saleRoot.setCenter(saleTableView);

        addSaleButton.setOnAction(e -> showAddSaleDialog());
        deleteSaleButton.setOnAction(e -> {
            String idText = searchSaleField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteSale(id);
                readSale();
            }
        });
        searchSaleButton.setOnAction(e -> {
            String idText = searchSaleField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchSale(id);
            }
        });
        refreshSaleButton.setOnAction(e -> readSale());

        return saleRoot;
    }

    private void updateSale(Sale sale) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE sale SET quantity=?, period=?, s_rate=?, product_id=? WHERE sale_number=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, sale.getQuantity());
            pstmt.setString(2, sale.getPeriod());
            pstmt.setDouble(3, sale.getS_rate());
            pstmt.setInt(4, sale.getProduct_id());
            pstmt.setInt(5, sale.getSale_number());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSale(int sale_number) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM sale WHERE sale_number=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, sale_number);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchSale(int sale_number) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM sale WHERE sale_number=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, sale_number);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int quantity = rs.getInt("quantity");
                String period = rs.getString("period");
                double s_rate = rs.getDouble("s_rate");
                int product_id = rs.getInt("product_id");
                showAlert(Alert.AlertType.INFORMATION, "Sale Found", "Details:\n" +
                        "Quantity: " + quantity + "\n" +
                        "Period: " + period + "\n" +
                        "S Rate: " + s_rate + "\n" +
                        "Product ID: " + product_id);
            } else {
                showAlert(Alert.AlertType.WARNING, "Sale Not Found", "The sale with number " + sale_number + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddSaleDialog() {
        Stage addSaleStage = new Stage();
        VBox addSaleLayout = new VBox(10);
        addSaleLayout.setStyle("-fx-padding: 20;");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Quantity");
        TextField periodField = new TextField();
        periodField.setPromptText("Period");
        TextField s_rateField = new TextField();
        s_rateField.setPromptText("S Rate");
        TextField productIdField = new TextField();
        productIdField.setPromptText("Product ID");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                int productId = Integer.parseInt(productIdField.getText());
                if (!isProductExist(productId)) {
                    showAlert(Alert.AlertType.ERROR, "Product Not Found", "The product with ID " + productId + " does not exist.");
                    return;
                }

                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO sale (quantity, period, s_rate, product_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(quantityField.getText()));
                pstmt.setString(2, periodField.getText());
                pstmt.setDouble(3, Double.parseDouble(s_rateField.getText()));
                pstmt.setInt(4, productId);
                pstmt.executeUpdate();
                saleList.add(new Sale(0, Integer.parseInt(quantityField.getText()), periodField.getText(), Double.parseDouble(s_rateField.getText()), productId));
                con.close();
                readSale();
                addSaleStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addSaleLayout.getChildren().addAll(quantityField, periodField, s_rateField, productIdField, addBtn);
        Scene addSaleScene = new Scene(addSaleLayout);
        addSaleStage.setScene(addSaleScene);
        addSaleStage.setTitle("Add Sale");
        addSaleStage.show();
    }

    private boolean isProductExist(int productId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT 1 FROM product WHERE id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, productId);
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
