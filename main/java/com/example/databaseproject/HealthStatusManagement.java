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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class HealthStatusManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<HealthStatus> healthStatusList = FXCollections.observableArrayList();
    TableView<HealthStatus> healthStatusTableView = new TableView<>();

    public void readHealthStatus() {
        healthStatusList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM health_status";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                healthStatusList.add(
                        new HealthStatus(rs.getInt("HS_id"), rs.getString("AX"), rs.getString("SPH"),
                                rs.getString("CYL"), rs.getInt("customer_id"), rs.getString("test_eye"), rs.getString("testdata"))
                );
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        healthStatusTableView.setItems(healthStatusList);  // Update the TableView
    }

    public BorderPane createHealthStatusManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane healthStatusRoot = new BorderPane();
        healthStatusRoot.setPrefSize(800, 600);
        HBox healthStatusTopLayout = new HBox(10);
        healthStatusTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addHealthStatusButton = new Button("Add Health Status");
        Button deleteHealthStatusButton = new Button("Delete Health Status");
        Button searchHealthStatusButton = new Button("Search for Health Status");
        Button refreshHealthStatusButton = new Button("Refresh");
        TextField searchHealthStatusField = new TextField();
        searchHealthStatusField.setPromptText("Enter Health Status ID");
        Button homeHealthStatusButton = new Button("Home");
        homeHealthStatusButton.setOnAction(e -> primaryStage.setScene(homeScene));

        healthStatusTopLayout.getChildren().addAll(homeHealthStatusButton, addHealthStatusButton, deleteHealthStatusButton, searchHealthStatusButton, searchHealthStatusField, refreshHealthStatusButton);

        healthStatusTableView.setEditable(true);

        TableColumn<HealthStatus, Integer> HS_idCol = new TableColumn<>("ID");
        HS_idCol.setPrefWidth(50);
        HS_idCol.setResizable(false);
        HS_idCol.setCellValueFactory(new PropertyValueFactory<>("HS_id"));

        TableColumn<HealthStatus, String> AXCol = new TableColumn<>("AX");
        AXCol.setPrefWidth(100);
        AXCol.setResizable(false);
        AXCol.setCellFactory(TextFieldTableCell.forTableColumn());
        AXCol.setCellValueFactory(new PropertyValueFactory<>("AX"));
        AXCol.setOnEditCommit(event -> {
            HealthStatus healthStatus = event.getRowValue();
            healthStatus.setAX(event.getNewValue());
            updateHealthStatus(healthStatus);
        });

        TableColumn<HealthStatus, String> SPHCol = new TableColumn<>("SPH");
        SPHCol.setPrefWidth(100);
        SPHCol.setResizable(false);
        SPHCol.setCellFactory(TextFieldTableCell.forTableColumn());
        SPHCol.setCellValueFactory(new PropertyValueFactory<>("SPH"));
        SPHCol.setOnEditCommit(event -> {
            HealthStatus healthStatus = event.getRowValue();
            healthStatus.setSPH(event.getNewValue());
            updateHealthStatus(healthStatus);
        });

        TableColumn<HealthStatus, String> CYLCol = new TableColumn<>("CYL");
        CYLCol.setPrefWidth(100);
        CYLCol.setResizable(false);
        CYLCol.setCellFactory(TextFieldTableCell.forTableColumn());
        CYLCol.setCellValueFactory(new PropertyValueFactory<>("CYL"));
        CYLCol.setOnEditCommit(event -> {
            HealthStatus healthStatus = event.getRowValue();
            healthStatus.setCYL(event.getNewValue());
            updateHealthStatus(healthStatus);
        });

        TableColumn<HealthStatus, Integer> customer_idCol = new TableColumn<>("Customer ID");
        customer_idCol.setPrefWidth(100);
        customer_idCol.setResizable(false);
        customer_idCol.setCellValueFactory(new PropertyValueFactory<>("customer_id"));

        TableColumn<HealthStatus, String> test_eyeCol = new TableColumn<>("Test Eye");
        test_eyeCol.setPrefWidth(100);
        test_eyeCol.setResizable(false);
        test_eyeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        test_eyeCol.setCellValueFactory(new PropertyValueFactory<>("test_eye"));
        test_eyeCol.setOnEditCommit(event -> {
            HealthStatus healthStatus = event.getRowValue();
            healthStatus.setTest_eye(event.getNewValue());
            updateHealthStatus(healthStatus);
        });

        TableColumn<HealthStatus, String> testdataCol = new TableColumn<>("Test Data");
        testdataCol.setPrefWidth(150);
        testdataCol.setResizable(false);
        testdataCol.setCellFactory(TextFieldTableCell.forTableColumn());
        testdataCol.setCellValueFactory(new PropertyValueFactory<>("testdata"));
        testdataCol.setOnEditCommit(event -> {
            HealthStatus healthStatus = event.getRowValue();
            healthStatus.setTestdata(event.getNewValue());
            updateHealthStatus(healthStatus);
        });

        healthStatusTableView.getColumns().addAll(HS_idCol, AXCol, SPHCol, CYLCol, customer_idCol, test_eyeCol, testdataCol);
        healthStatusTableView.setItems(healthStatusList);

        healthStatusRoot.setTop(healthStatusTopLayout);
        healthStatusRoot.setCenter(healthStatusTableView);

        addHealthStatusButton.setOnAction(e -> showAddHealthStatusDialog());
        deleteHealthStatusButton.setOnAction(e -> {
            String idText = searchHealthStatusField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteHealthStatus(id);
                readHealthStatus();
            }
        });
        searchHealthStatusButton.setOnAction(e -> {
            String idText = searchHealthStatusField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchHealthStatus(id);
            }
        });
        refreshHealthStatusButton.setOnAction(e -> readHealthStatus());

        return healthStatusRoot;
    }

    private void updateHealthStatus(HealthStatus healthStatus) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE health_status SET AX=?, SPH=?, CYL=?, customer_id=?, test_eye=?, testdata=? WHERE HS_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, healthStatus.getAX());
            pstmt.setString(2, healthStatus.getSPH());
            pstmt.setString(3, healthStatus.getCYL());
            pstmt.setInt(4, healthStatus.getCustomer_id());
            pstmt.setString(5, healthStatus.getTest_eye());
            pstmt.setString(6, healthStatus.getTestdata());
            pstmt.setInt(7, healthStatus.getHS_id());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteHealthStatus(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM health_status WHERE HS_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchHealthStatus(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM health_status WHERE HS_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String AX = rs.getString("AX");
                String SPH = rs.getString("SPH");
                String CYL = rs.getString("CYL");
                int customer_id = rs.getInt("customer_id");
                String test_eye = rs.getString("test_eye");
                String testdata = rs.getString("testdata");
                showAlert(Alert.AlertType.INFORMATION, "Health Status Found", "Details:\n" +
                        "AX: " + AX + "\n" +
                        "SPH: " + SPH + "\n" +
                        "CYL: " + CYL + "\n" +
                        "Customer ID: " + customer_id + "\n" +
                        "Test Eye: " + test_eye + "\n" +
                        "Test Data: " + testdata);
            } else {
                showAlert(Alert.AlertType.WARNING, "Health Status Not Found", "The health status with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddHealthStatusDialog() {
        Stage addHealthStatusStage = new Stage();
        VBox addHealthStatusLayout = new VBox(10);
        addHealthStatusLayout.setStyle("-fx-padding: 20;");

        TextField AXField = new TextField();
        AXField.setPromptText("AX");
        TextField SPHField = new TextField();
        SPHField.setPromptText("SPH");
        TextField CYLField = new TextField();
        CYLField.setPromptText("CYL");
        TextField customer_idField = new TextField();
        customer_idField.setPromptText("Customer ID");
        TextField testdataField = new TextField();
        testdataField.setPromptText("Test Data");

        MenuButton testEyeMenuButton = new MenuButton("Select Test Eye");
        MenuItem leftEyeItem = new MenuItem("Left");
        MenuItem rightEyeItem = new MenuItem("Right");
        testEyeMenuButton.getItems().addAll(leftEyeItem, rightEyeItem);

        final String[] selectedTestEye = new String[1];

        leftEyeItem.setOnAction(e -> {
            selectedTestEye[0] = "left";
            testEyeMenuButton.setText("Left");
        });

        rightEyeItem.setOnAction(e -> {
            selectedTestEye[0] = "right";
            testEyeMenuButton.setText("Right");
        });

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                int customerId = Integer.parseInt(customer_idField.getText());
                if (!isCustomerExists(customerId)) {
                    showAlert(Alert.AlertType.ERROR, "Error", "Customer with ID " + customerId + " does not exist.");
                    return;
                }

                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO health_status (AX, SPH, CYL, customer_id, test_eye, testdata) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, AXField.getText());
                pstmt.setString(2, SPHField.getText());
                pstmt.setString(3, CYLField.getText());
                pstmt.setInt(4, customerId);
                pstmt.setString(5, selectedTestEye[0]);
                pstmt.setString(6, testdataField.getText());
                pstmt.executeUpdate();
                healthStatusList.add(new HealthStatus(0, AXField.getText(), SPHField.getText(), CYLField.getText(), customerId, selectedTestEye[0], testdataField.getText()));
                con.close();
                readHealthStatus();
                addHealthStatusStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addHealthStatusLayout.getChildren().addAll(AXField, SPHField, CYLField, customer_idField, testdataField, testEyeMenuButton, addBtn);
        Scene addHealthStatusScene = new Scene(addHealthStatusLayout);
        addHealthStatusStage.setScene(addHealthStatusScene);
        addHealthStatusStage.setTitle("Add Health Status");
        addHealthStatusStage.show();
    }

    private boolean isCustomerExists(int customerId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT COUNT(*) FROM Customer WHERE c_id = ?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            pstmt.close();
            con.close();
            return count > 0;
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
