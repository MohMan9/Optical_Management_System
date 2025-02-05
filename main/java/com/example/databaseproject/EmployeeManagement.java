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

public class EmployeeManagement {
    dataBaseConnection db = new dataBaseConnection();
    ObservableList<Employee> EmployeesList = FXCollections.observableArrayList();
    TableView<Employee> employeeTableView = new TableView<>();

    public void readEmployee() {
        EmployeesList.clear();  // Clear the current list
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM employee";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                EmployeesList.add(
                        new Employee(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5),
                                rs.getString(6), rs.getString(7), rs.getString(8), rs.getString(9)));  // Include type
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        employeeTableView.setItems(EmployeesList);  // Update the TableView
    }

    public BorderPane createEmployeeManagementLayout(Stage primaryStage, Scene homeScene) {
        BorderPane employeeRoot = new BorderPane();
        employeeRoot.setPrefSize(800, 600);
        HBox employeeTopLayout = new HBox(10);
        employeeTopLayout.setStyle("-fx-padding: 10; -fx-alignment: center;");
        Button addEmployeeButton = new Button("Add Employee");
        Button deleteEmployeeButton = new Button("Delete Employee");
        Button searchEmployeeButton = new Button("Search for Employee");
        Button refreshEmployeeButton = new Button("Refresh");
        Button showSuppliersButton = new Button("Show Suppliers");
        TextField searchEmployeeField = new TextField();
        searchEmployeeField.setPromptText("Enter SSN");
        Button homeEmployeeButton = new Button("Home");
        homeEmployeeButton.setOnAction(e -> primaryStage.setScene(homeScene));

        employeeTopLayout.getChildren().addAll(homeEmployeeButton, addEmployeeButton, deleteEmployeeButton, searchEmployeeButton, searchEmployeeField, refreshEmployeeButton, showSuppliersButton);

        employeeTableView.setEditable(true);

        TableColumn<Employee, Integer> ssnCol = new TableColumn<>("SSN");
        ssnCol.setPrefWidth(75);
        ssnCol.setResizable(false);
        ssnCol.setCellValueFactory(new PropertyValueFactory<>("SSN"));

        TableColumn<Employee, String> nameCol = new TableColumn<>("Name");
        nameCol.setPrefWidth(75);
        nameCol.setResizable(false);
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setComparator(String::compareToIgnoreCase);
        nameCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setName(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> dobCol = new TableColumn<>("Date of Birth");
        dobCol.setPrefWidth(75);
        dobCol.setResizable(false);
        dobCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dobCol.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        dobCol.setComparator(String::compareToIgnoreCase);
        dobCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setDateOfBirth(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> dojCol = new TableColumn<>("Date of Join");
        dojCol.setPrefWidth(75);
        dojCol.setResizable(false);
        dojCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dojCol.setCellValueFactory(new PropertyValueFactory<>("dateOfJoin"));
        dojCol.setComparator(String::compareToIgnoreCase);
        dojCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setDateOfJoin(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, Integer> rateCol = new TableColumn<>("Rate");
        rateCol.setPrefWidth(75);
        rateCol.setResizable(false);
        rateCol.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        rateCol.setCellValueFactory(new PropertyValueFactory<>("rate"));
        rateCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setRate(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> emailCol = new TableColumn<>("Email");
        emailCol.setPrefWidth(165);
        emailCol.setResizable(false);
        emailCol.setCellFactory(TextFieldTableCell.forTableColumn());
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        emailCol.setComparator(String::compareToIgnoreCase);
        emailCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setEmail(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> phoneCol = new TableColumn<>("Phone Number");
        phoneCol.setPrefWidth(75);
        phoneCol.setResizable(false);
        phoneCol.setCellFactory(TextFieldTableCell.forTableColumn());
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        phoneCol.setComparator(String::compareToIgnoreCase);
        phoneCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setPhoneNumber(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> addressCol = new TableColumn<>("Address");
        addressCol.setPrefWidth(75);
        addressCol.setResizable(false);
        addressCol.setCellFactory(TextFieldTableCell.forTableColumn());
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        addressCol.setComparator(String::compareToIgnoreCase);
        addressCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setAddress(event.getNewValue());
            updateEmployee(employee);
        });

        TableColumn<Employee, String> typeCol = new TableColumn<>("Type");
        typeCol.setPrefWidth(75);
        typeCol.setResizable(false);
        typeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        typeCol.setComparator(String::compareToIgnoreCase);
        typeCol.setOnEditCommit(event -> {
            Employee employee = event.getRowValue();
            employee.setType(event.getNewValue());
            updateEmployee(employee);
        });

        employeeTableView.getColumns().addAll(ssnCol, nameCol, dobCol, dojCol, rateCol, emailCol, phoneCol, addressCol, typeCol);
        employeeTableView.setItems(EmployeesList);

        employeeRoot.setTop(employeeTopLayout);
        employeeRoot.setCenter(employeeTableView);

        addEmployeeButton.setOnAction(e -> showAddEmployeeDialog());
        deleteEmployeeButton.setOnAction(e -> {
            String ssnText = searchEmployeeField.getText();
            if (!ssnText.isEmpty()) {
                int ssn = Integer.parseInt(ssnText);
                deleteEmployee(ssn);
                readEmployee();
            }
        });
        searchEmployeeButton.setOnAction(e -> {
            String ssnText = searchEmployeeField.getText();
            if (!ssnText.isEmpty()) {
                int ssn = Integer.parseInt(ssnText);
                searchEmployee(ssn);
            }
        });
        refreshEmployeeButton.setOnAction(e -> readEmployee());

        showSuppliersButton.setOnAction(e -> {
            String ssnText = searchEmployeeField.getText();
            if (!ssnText.isEmpty()) {
                int ssn = Integer.parseInt(ssnText);
                showSuppliers(ssn);
            }
        });

        return employeeRoot;
    }

    private void updateEmployee(Employee employee) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "UPDATE employee SET Ename=?, date_of_birth=?, date_of_join=?, rate=?, address=?, email=?, phone_number=?, type=? WHERE snn=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, employee.getName());
            pstmt.setString(2, employee.getDateOfBirth());
            pstmt.setString(3, employee.getDateOfJoin());
            pstmt.setInt(4, employee.getRate());
            pstmt.setString(5, employee.getAddress());
            pstmt.setString(6, employee.getEmail());
            pstmt.setString(7, employee.getPhoneNumber());
            pstmt.setString(8, employee.getType());  // Include type
            pstmt.setInt(9, employee.getSSN());
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteEmployee(int ssn) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "DELETE FROM employee WHERE snn=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ssn);
            pstmt.executeUpdate();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void searchEmployee(int ssn) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM employee WHERE snn=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ssn);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String name = rs.getString("Ename");
                String dateOfBirth = rs.getString("date_of_birth");
                String dateOfJoin = rs.getString("date_of_join");
                int rate = rs.getInt("rate");
                String address = rs.getString("address");
                String email = rs.getString("email");
                String phoneNumber = rs.getString("phone_number");
                String type = rs.getString("type");  // Include type
                showAlert(Alert.AlertType.INFORMATION, "Employee Found", "Details:\n" +
                        "Name: " + name + "\n" +
                        "Date of Birth: " + dateOfBirth + "\n" +
                        "Date of Join: " + dateOfJoin + "\n" +
                        "Rate: " + rate + "\n" +
                        "Address: " + address + "\n" +
                        "Email: " + email + "\n" +
                        "Phone Number: " + phoneNumber + "\n" +
                        "Type: " + type);  // Include type
            } else {
                showAlert(Alert.AlertType.WARNING, "Employee Not Found", "The employee with SSN " + ssn + " was not found.");
            }
            rs.close();
            pstmt.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAddEmployeeDialog() {
        Stage addEmpStage = new Stage();
        VBox addEmpLayout = new VBox(10);
        addEmpLayout.setStyle("-fx-padding: 20;");

        TextField ssnField = new TextField();
        ssnField.setPromptText("SSN");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        DatePicker dbirthField = new DatePicker();
        dbirthField.setPromptText("Date of Birth");
        DatePicker djoinField = new DatePicker();
        djoinField.setPromptText("Date of Join");
        TextField rateField = new TextField();
        rateField.setPromptText("Rate");
        TextField addressField = new TextField();
        addressField.setPromptText("Address");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField phoneNumberField = new TextField();
        phoneNumberField.setPromptText("Phone Number");
        ComboBox<String> typeComboBox = new ComboBox<>();
        typeComboBox.getItems().addAll("Manager", "Optician");
        typeComboBox.setPromptText("Select Type");

        Button addBtn = new Button("Add");
        addBtn.setOnAction(e -> {
            try {
                Connection con = db.getConnection().connectDB();
                String sql = "INSERT INTO employee(snn, Ename, date_of_birth, date_of_join, rate, address, email, phone_number, type) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(ssnField.getText()));
                pstmt.setString(2, nameField.getText());
                pstmt.setString(3, dbirthField.getValue().toString());
                pstmt.setString(4, djoinField.getValue().toString());
                pstmt.setInt(5, Integer.parseInt(rateField.getText()));
                pstmt.setString(6, addressField.getText());
                pstmt.setString(7, emailField.getText());
                pstmt.setString(8, phoneNumberField.getText());
                pstmt.setString(9, typeComboBox.getValue());
                pstmt.executeUpdate();
                EmployeesList.add(new Employee(Integer.parseInt(ssnField.getText()), nameField.getText(), dbirthField.getValue().toString(),
                        djoinField.getValue().toString(), Integer.parseInt(rateField.getText()), addressField.getText(), emailField.getText(), phoneNumberField.getText(), typeComboBox.getValue()));
                con.close();
                addEmpStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        addEmpLayout.getChildren().addAll(ssnField, nameField, dbirthField, djoinField, rateField, addressField, emailField, phoneNumberField, typeComboBox, addBtn);
        Scene addEmpScene = new Scene(addEmpLayout);
        addEmpStage.setScene(addEmpScene);
        addEmpStage.setTitle("Add Employee");
        addEmpStage.show();
    }

    private void showSuppliers(int ssn) {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT * FROM sublayer WHERE employee_ssn=?";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setInt(1, ssn);
            ResultSet rs = pstmt.executeQuery();
            StringBuilder suppliers = new StringBuilder("Suppliers:\n");
            while (rs.next()) {
                suppliers.append("ID: ").append(rs.getInt("s_id"))
                        .append(", Name: ").append(rs.getString("s_name"))
                        .append(", Email: ").append(rs.getString("email"))
                        .append(", Phone Number: ").append(rs.getString("phone_number"))
                        .append("\n");
            }
            if (suppliers.length() == 10) {
                showAlert(Alert.AlertType.WARNING, "No Suppliers", "No suppliers found for the employee with SSN " + ssn + ".");
            } else {
                showAlert(Alert.AlertType.INFORMATION, "Suppliers", suppliers.toString());
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
