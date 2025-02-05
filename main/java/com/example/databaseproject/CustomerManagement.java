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

public class CustomerManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Customer> CustomersList = FXCollections.observableArrayList();
    TableView<Customer> customerTableView = new TableView<>();

    public void readCustomer() {
        CustomersList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM Customer";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                CustomersList.add(
                        new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4),
                                rs.getString(5), rs.getString(6), rs.getString(7),
                                rs.getString(8), rs.getString(9)));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        customerTableView.setItems(CustomersList);  // Update the TableView
    }

    public BorderPane createCustomerManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane customerRoot = new BorderPane();
        customerRoot.setPrefSize(800, 600);
        HBox customerTopLayout = new HBox(10);
        customerTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button showPayment = new Button("Show Payment");
        Button showHealth = new Button("Show Health Status");

        Button addCustomerButton = new Button("Add Customer");
        Button deleteCustomerButton = new Button("Delete Customer");
        Button searchCustomerButton = new Button("Search for Customer");
        Button refreshCustomerButton = new Button("Refresh");
        TextField searchCustomerField = new TextField();
        searchCustomerField.setPromptText("Enter Customer ID");
        Button homeCustomerButton = new Button("Home");
        homeCustomerButton.setOnAction(e -> primaryStage.setScene(homeScene));

        customerTopLayout.getChildren().addAll(homeCustomerButton, addCustomerButton, deleteCustomerButton,
                searchCustomerButton, searchCustomerField, refreshCustomerButton, showPayment, showHealth);

        customerTableView.setEditable(true);

        showPayment.setOnAction(e -> {
            String idText = searchCustomerField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                showCustomerPayment(id);
            }
        });

        showHealth.setOnAction(e -> {
            String idText = searchCustomerField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                showCustomerHealthStatus(id);
            }
        });

        if (customerTableView.getColumns().isEmpty()) {
            TableColumn<Customer, Integer> c_idCol = new TableColumn<>("ID");
            c_idCol.setPrefWidth(50);
            c_idCol.setResizable(false);
            c_idCol.setCellValueFactory(new PropertyValueFactory<>("c_id"));

            TableColumn<Customer, String> firstCol = new TableColumn<>("First Name");
            firstCol.setPrefWidth(100);
            firstCol.setResizable(false);
            firstCol.setCellFactory(TextFieldTableCell.forTableColumn());
            firstCol.setCellValueFactory(new PropertyValueFactory<>("first"));
            firstCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setFirst(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> midCol = new TableColumn<>("Middle Name");
            midCol.setPrefWidth(100);
            midCol.setResizable(false);
            midCol.setCellFactory(TextFieldTableCell.forTableColumn());
            midCol.setCellValueFactory(new PropertyValueFactory<>("mid"));
            midCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setMid(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> lastCol = new TableColumn<>("Last Name");
            lastCol.setPrefWidth(100);
            lastCol.setResizable(false);
            lastCol.setCellFactory(TextFieldTableCell.forTableColumn());
            lastCol.setCellValueFactory(new PropertyValueFactory<>("last"));
            lastCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setLast(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> dobCustomerCol = new TableColumn<>("Date of Birth");
            dobCustomerCol.setPrefWidth(100);
            dobCustomerCol.setResizable(false);
            dobCustomerCol.setCellFactory(TextFieldTableCell.forTableColumn());
            dobCustomerCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
            dobCustomerCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setDateOfBirth(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> addressCustomerCol = new TableColumn<>("Address");
            addressCustomerCol.setPrefWidth(150);
            addressCustomerCol.setResizable(false);
            addressCustomerCol.setCellFactory(TextFieldTableCell.forTableColumn());
            addressCustomerCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            addressCustomerCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setAddress(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> genderCustomerCol = new TableColumn<>("Gender");
            genderCustomerCol.setPrefWidth(50);
            genderCustomerCol.setResizable(false);
            genderCustomerCol.setCellFactory(TextFieldTableCell.forTableColumn());
            genderCustomerCol.setCellValueFactory(new PropertyValueFactory<>("gender"));
            genderCustomerCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setGender(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> emailCustomerCol = new TableColumn<>("Email");
            emailCustomerCol.setPrefWidth(150);
            emailCustomerCol.setResizable(false);
            emailCustomerCol.setCellFactory(TextFieldTableCell.forTableColumn());
            emailCustomerCol.setCellValueFactory(new PropertyValueFactory<>("email"));
            emailCustomerCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setEmail(event.getNewValue());
                updateCustomer(customer);
            });

            TableColumn<Customer, String> phoneCustomerCol = new TableColumn<>("Phone Number");
            phoneCustomerCol.setPrefWidth(100);
            phoneCustomerCol.setResizable(false);
            phoneCustomerCol.setCellFactory(TextFieldTableCell.forTableColumn());
            phoneCustomerCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
            phoneCustomerCol.setOnEditCommit(event -> {
                Customer customer = event.getRowValue();
                customer.setPhoneNumber(event.getNewValue());
                updateCustomer(customer);
            });

            customerTableView.getColumns().addAll(c_idCol, firstCol, midCol, lastCol, dobCustomerCol, addressCustomerCol, genderCustomerCol, emailCustomerCol, phoneCustomerCol);
        }

        customerTableView.setItems(CustomersList);
        customerRoot.setTop(customerTopLayout);
        customerRoot.setCenter(customerTableView);

        addCustomerButton.setOnAction(e -> showAddCustomerDialog());
        deleteCustomerButton.setOnAction(e -> {
            String idText = searchCustomerField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                deleteCustomer(id);
                readCustomer();
            }
        });
        searchCustomerButton.setOnAction(e -> {
            String idText = searchCustomerField.getText();
            if (!idText.isEmpty()) {
                int id = Integer.parseInt(idText);
                searchCustomer(id);
            }
        });
        refreshCustomerButton.setOnAction(e -> readCustomer());

        return customerRoot;
    }

    private void updateCustomer(Customer customer) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE Customer SET firstName=?, mid=?, lastName=?, date_of_birth=?, address=?, gender=?, email=?, phone_number=? WHERE c_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, customer.getFirst());
            pstmt.setString(2, customer.getMid());
            pstmt.setString(3, customer.getLast());
            pstmt.setString(4, customer.getDateOfBirth());
            pstmt.setString(5, customer.getAddress());
            pstmt.setString(6, customer.getGender());
            pstmt.setString(7, customer.getEmail());
            pstmt.setString(8, customer.getPhoneNumber());
            pstmt.setInt(9, customer.getC_id());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteCustomer(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM Customer WHERE c_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchCustomer(int id) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM Customer WHERE c_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String middleName = rs.getString("mid");
                String lastName = rs.getString("lastName");
                String dateOfBirth = rs.getString("date_of_birth");
                String address = rs.getString("address");
                String gender = rs.getString("gender");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                showAlert(Alert.AlertType.INFORMATION, "Customer Found", "Details:\n" +
                        "First Name: " + firstName + "\n" +
                        "Middle Name: " + middleName + "\n" +
                        "Last Name: " + lastName + "\n" +
                        "Date of Birth: " + dateOfBirth + "\n" +
                        "Address: " + address + "\n" +
                        "Gender: " + gender + "\n" +
                        "Email: " + email + "\n" +
                        "Phone Number: " + phoneNumber);
            } else {
                showAlert(Alert.AlertType.WARNING, "Customer Not Found", "The customer with ID " + id + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomerPayment(int customerId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM paying WHERE customer_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder paymentDetails = new StringBuilder();
            while (rs.next()) {
                int p_id = rs.getInt("p_id");
                String type = rs.getString("type");
                double type_price = rs.getDouble("type_price");
                paymentDetails.append("Payment ID: ").append(p_id).append("\n");
                paymentDetails.append("Type: ").append(type).append("\n");
                paymentDetails.append("Type Price: ").append(type_price).append("\n\n");
            }

            if (paymentDetails.length() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Payment Details", paymentDetails.toString());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Payments Found", "This customer has no payments.");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomerHealthStatus(int customerId) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM health_status WHERE customer_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, customerId);
            ResultSet rs = pstmt.executeQuery();

            StringBuilder healthDetails = new StringBuilder();
            while (rs.next()) {
                int hs_id = rs.getInt("HS_id");
                String ax = rs.getString("AX");
                String sph = rs.getString("SPH");
                String cyl = rs.getString("CYL");
                String test_eye = rs.getString("test_eye");
                String testdata = rs.getString("testdata");
                healthDetails.append("Health Status ID: ").append(hs_id).append("\n");
                healthDetails.append("AX: ").append(ax).append("\n");
                healthDetails.append("SPH: ").append(sph).append("\n");
                healthDetails.append("CYL: ").append(cyl).append("\n");
                healthDetails.append("Test Date : ").append(testdata).append("\n");
                healthDetails.append("Test Eye: ").append(test_eye).append("\n\n");
            }

            if (healthDetails.length() > 0) {
                showAlert(Alert.AlertType.INFORMATION, "Health Status Details", healthDetails.toString());
            } else {
                showAlert(Alert.AlertType.INFORMATION, "No Health Status Found", "This customer has no health status records.");
            }

            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddCustomerDialog() {
        Stage addCustStage = new Stage();
        VBox addCustLayout = new VBox(10);
        addCustLayout.setStyle("-fx-padding: 20;");

        TextField firstNameField = new TextField();
        firstNameField.setPromptText("First Name");
        TextField middleNameField = new TextField();
        middleNameField.setPromptText("Middle Name");
        TextField lastNameField = new TextField();
        lastNameField.setPromptText("Last Name");
        DatePicker dateOfBirthField = new DatePicker();
        dateOfBirthField.setPromptText("Date of Birth");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField genderField = new TextField();
        genderField.setPromptText("Gender");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO Customer (firstName, mid, lastName, date_of_birth, address, gender, email, phone_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setString(1, firstNameField.getText());
                pstmt.setString(2, middleNameField.getText());
                pstmt.setString(3, lastNameField.getText());
                pstmt.setString(4, dateOfBirthField.getValue().toString());
                pstmt.setString(5, addressField.getText());
                pstmt.setString(6, genderField.getText());
                pstmt.setString(7, emailField.getText());
                pstmt.setString(8, phoneNumberField.getText());
                pstmt.executeUpdate();
                CustomersList.add(new Customer(firstNameField.getText(), middleNameField.getText(), lastNameField.getText(),
                        dateOfBirthField.getValue().toString(), addressField.getText(), genderField.getText(), emailField.getText(), phoneNumberField.getText()));
                con.close();
                readCustomer();
                addCustStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addCustLayout.getChildren().addAll(firstNameField, middleNameField, lastNameField, dateOfBirthField, addressField, genderField, emailField, phoneNumberField, addBtn);
        Scene addCustScene = new Scene(addCustLayout);
        addCustStage.setScene(addCustScene);
        addCustStage.setTitle("Add Customer");
        addCustStage.show();
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
