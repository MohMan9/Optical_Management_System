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

public class SublayerManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Supplier> supplierList = FXCollections.observableArrayList();
    TableView<Supplier> supplierTableView = new TableView<>();

    public void readSublayer() {
        supplierList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM sublayer";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                supplierList.add(
                        new Supplier(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5)));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        supplierTableView.setItems(supplierList);  // Update the TableView
    }

    public BorderPane createSublayerManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane supplierRoot = new BorderPane();
        supplierRoot.setPrefSize(800, 600);
        HBox supplierTopLayout = new HBox(10);
        supplierTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addSupplierButton = new Button("Add Supplier");
        Button deleteSupplierButton = new Button("Delete Supplier");
        Button searchSupplierButton = new Button("Search for Supplier");
        Button refreshSupplierButton = new Button("Refresh");
        TextField searchSupplierField = new TextField();
        searchSupplierField.setPromptText("Enter Supplier ID");
        Button homeSupplierButton = new Button("Home");
        homeSupplierButton.setOnAction(e -> primaryStage.setScene(homeScene));

        supplierTopLayout.getChildren().addAll(homeSupplierButton, addSupplierButton, deleteSupplierButton, searchSupplierButton, searchSupplierField, refreshSupplierButton);

        supplierTableView.setEditable(true);

        TableColumn<Supplier, Integer> s_idCol = new TableColumn<>("ID");
        s_idCol.setPrefWidth(50);
        s_idCol.setResizable(false);
        s_idCol.setCellValueFactory(new PropertyValueFactory<>("s_id"));

        TableColumn<Supplier, String> s_nameCol = new TableColumn<>("Name");
        s_nameCol.setPrefWidth(100);
        s_nameCol.setResizable(false);
        s_nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        s_nameCol.setCellValueFactory(new PropertyValueFactory<>("s_name"));
        s_nameCol.setOnEditCommit(event -> {
            Supplier supplier = event.getRowValue();
            supplier.setS_name(event.getNewValue());
            updateSublayer(supplier);
        });

        TableColumn<Supplier, String> emailSublayerCol = new TableColumn<>("Email");
        emailSublayerCol.setPrefWidth(150);
        emailSublayerCol.setResizable(false);
        emailSublayerCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailSublayerCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailSublayerCol.setOnEditCommit(event -> {
            Supplier supplier = event.getRowValue();
            supplier.setEmail(event.getNewValue());
            updateSublayer(supplier);
        });

        TableColumn<Supplier, String> phoneSublayerCol = new TableColumn<>("Phone Number");
        phoneSublayerCol.setPrefWidth(100);
        phoneSublayerCol.setResizable(false);
        phoneSublayerCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneSublayerCol.setCellValueFactory(new PropertyValueFactory<>("phone_number"));
        phoneSublayerCol.setOnEditCommit(event -> {
            Supplier supplier = event.getRowValue();
            supplier.setPhone_number(event.getNewValue());
            updateSublayer(supplier);
        });

        TableColumn<Supplier, Integer> employeeSSNCol = new TableColumn<>("Employee SSN");
        employeeSSNCol.setPrefWidth(100);
        employeeSSNCol.setResizable(false);
        employeeSSNCol.setCellValueFactory(new PropertyValueFactory<>("employee_ssn"));

        supplierTableView.getColumns().addAll(s_idCol, s_nameCol, emailSublayerCol, phoneSublayerCol, employeeSSNCol);
        supplierTableView.setItems(supplierList);

        supplierRoot.setTop(supplierTopLayout);
        supplierRoot.setCenter(supplierTableView);

        addSupplierButton.setOnAction(e -> showAddSublayerDialog());
        deleteSupplierButton.setOnAction(e -> {
            String idText = searchSupplierField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteSublayer(id);
                readSublayer();
            }
        });
        searchSupplierButton.setOnAction(e -> {
            String idText = searchSupplierField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchSublayer(id);
            }
        });
        refreshSupplierButton.setOnAction(e -> readSublayer());

        return supplierRoot;
    }

    private void updateSublayer(Supplier supplier) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE sublayer SET s_name=?, email=?, phone_number=?, employee_ssn=? WHERE s_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, supplier.getS_name());
            pstmt.setString(2, supplier.getEmail());
            pstmt.setString(3, supplier.getPhone_number());
            pstmt.setInt(4, supplier.getEmployee_ssn());
            pstmt.setInt(5, supplier.getS_id());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteSublayer(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM sublayer WHERE s_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchSublayer(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM sublayer WHERE s_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String s_name = rs.getString("s_name");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                int employeeSSN = rs.getInt("employee_ssn");
                showAlert(Alert.AlertType.INFORMATION, "Sublayer Found", "Details:\n" +
                        "Name: " + s_name + "\n" +
                        "Email: " + email + "\n" +
                        "Phone Number: " + phoneNumber + "\n" +
                        "Employee SSN: " + employeeSSN);
            } else {
                showAlert(Alert.AlertType.WARNING, "Sublayer Not Found", "The sublayer with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddSublayerDialog() {
        Stage addSublayerStage = new Stage();
        VBox addSublayerLayout = new VBox(10);
        addSublayerLayout.setStyle("-fx-padding: 20;");

        TextField s_idField = new TextField();
        s_idField.setPromptText("ID");
        TextField s_nameField = new TextField();
        s_nameField.setPromptText("Name");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        TextField employeeSSNField = new TextField();
        employeeSSNField.setPromptText("Employee SSN");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO sublayer (s_id, s_name, email, phone_number, employee_ssn) VALUES (?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(s_idField.getText()));
                pstmt.setString(2, s_nameField.getText());
                pstmt.setString(3, emailField.getText());
                pstmt.setString(4, phoneNumberField.getText());
                pstmt.setInt(5, Integer.parseInt(employeeSSNField.getText()));
                pstmt.executeUpdate();
                supplierList.add(new Supplier(Integer.parseInt(s_idField.getText()), s_nameField.getText(), emailField.getText(), phoneNumberField.getText(), Integer.parseInt(employeeSSNField.getText())));
                con.close();
                readSublayer();
                addSublayerStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addSublayerLayout.getChildren().addAll(s_idField, s_nameField, emailField, phoneNumberField, employeeSSNField, addBtn);
        Scene addSublayerScene = new Scene(addSublayerLayout);
        addSublayerStage.setScene(addSublayerScene);
        addSublayerStage.setTitle("Add Sublayer");
        addSublayerStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
