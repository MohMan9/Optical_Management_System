package com.example.databaseproject;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class MainApplication extends Application {
    EmployeeManagement employeeManagement = new EmployeeManagement();
    CustomerManagement customerManagement = new CustomerManagement();
    SublayerManagement sublayerManagement = new SublayerManagement();
    StockManagement stockManagement = new StockManagement();
    PayingManagement payingManagement = new PayingManagement();
    ProductManagement productManagement = new ProductManagement();
    SaleManagement saleManagement = new SaleManagement();
    HealthStatusManagement healthStatusManagement = new HealthStatusManagement();
    Dashboard dashboard = new Dashboard();

    Scene homeScene, employeeScene, customerScene, sublayerScene, stockScene, payingScene, productScene, saleScene, healthStatusScene, dashboardScene;
    String userType;

    @Override
    public void start(Stage primaryStage) {
        showLoginScreen(primaryStage);
    }

    private void showLoginScreen(Stage primaryStage) {
        LoginUI loginUI = new LoginUI();
        loginUI.setOnLoginSuccess(() -> {
            userType = loginUI.getUserType();
            showMainUI(primaryStage);
        });
        Scene loginScene = loginUI.createLoginScene(primaryStage);
        primaryStage.setScene(loginScene);
        primaryStage.setTitle("Login");
        primaryStage.show();
    }

    private void showMainUI(Stage primaryStage) {
        employeeManagement.readEmployee();
        customerManagement.readCustomer();
        sublayerManagement.readSublayer();
        stockManagement.readStock();
        payingManagement.readPaying();
        productManagement.readProduct();
        saleManagement.readSale();

        primaryStage.setTitle("Management System");

        // Create the sidebar menu
        ListView<String> menu = new ListView<>();
        menu.getItems().addAll("Dashboard", "Employees", "Customers", "Sublayer", "Stock", "Paying", "Product", "Sale", "Health Status", "Log Out");
        menu.setStyle("-fx-font-size: 16px; -fx-padding: 10;");

        // Create the center layout with the image
        BorderPane centerLayout = new BorderPane();
        Image image = new Image(getClass().getResource("/pj.jpg").toExternalForm());
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(1200);  // Adjust the size as needed
        imageView.setFitHeight(800);  // Adjust the size as needed
        imageView.setPreserveRatio(false); // Stretch to fill the area
        centerLayout.setCenter(imageView);

        // Create the main layout
        BorderPane root = new BorderPane();
        root.setLeft(menu);
        root.setCenter(centerLayout);

        homeScene = new Scene(root, 1200, 800);
        homeScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());

        primaryStage.setScene(homeScene);
        primaryStage.show();

        // Initialize other scenes
        employeeScene = new Scene(employeeManagement.createEmployeeManagementLayout(primaryStage, homeScene), 900, 600);
        customerScene = new Scene(customerManagement.createCustomerManagementLayout(primaryStage, homeScene), 900, 600);
        sublayerScene = new Scene(sublayerManagement.createSublayerManagementLayout(primaryStage, homeScene), 900, 600);
        stockScene = new Scene(stockManagement.createStockManagementLayout(primaryStage, homeScene), 900, 600);
        payingScene = new Scene(payingManagement.createPayingManagementLayout(primaryStage, homeScene), 900, 600);
        productScene = new Scene(productManagement.createProductManagementLayout(primaryStage, homeScene), 900, 600);
        saleScene = new Scene(saleManagement.createSaleManagementLayout(primaryStage, homeScene), 900, 600);
        healthStatusScene = new Scene(healthStatusManagement.createHealthStatusManagementLayout(primaryStage, homeScene), 900, 600);
        dashboardScene = new Scene(dashboard.createDashboardLayout(primaryStage, homeScene), 1200, 800);

        // Apply CSS to other scenes
        employeeScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        customerScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        sublayerScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        stockScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        payingScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        productScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        saleScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
        healthStatusScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());
       // dashboardScene.getStylesheets().add(getClass().getResource("/dp.css").toExternalForm());

        // Handle menu item selection
        menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(userType.equals("Manager")) {
                switch (newValue) {
                    case "Dashboard":
                        primaryStage.setScene(dashboardScene);
                        break;
                    case "Employees":
                        primaryStage.setScene(employeeScene);
                        break;
                    case "Customers":
                        primaryStage.setScene(customerScene);
                        break;
                    case "Sublayer":
                        primaryStage.setScene(sublayerScene);
                        break;
                    case "Stock":
                        primaryStage.setScene(stockScene);
                        break;
                    case "Paying":
                        primaryStage.setScene(payingScene);
                        break;
                    case "Product":
                        primaryStage.setScene(productScene);
                        break;
                    case "Sale":
                        primaryStage.setScene(saleScene);
                        break;
                    case "Health Status":
                        primaryStage.setScene(healthStatusScene);
                        break;
                    case "Log Out":
                        logOut(primaryStage);
                        break;


                }
            }else if(userType.equals("Optician")){

                switch (newValue) {
                    case "Dashboard":
                        primaryStage.setScene(dashboardScene);
                        break;
                    case "Customers":
                        primaryStage.setScene(customerScene);
                        break;
                    case "Stock":
                        primaryStage.setScene(stockScene);
                        break;
                    case "Paying":
                        primaryStage.setScene(payingScene);
                        break;
                    case "Product":
                        primaryStage.setScene(productScene);
                        break;
                    case "Sale":
                        primaryStage.setScene(saleScene);
                        break;
                    case "Health Status":
                        primaryStage.setScene(healthStatusScene);
                        break;
                    case "Log Out":
                        logOut(primaryStage);
                        break;

                }

            }
        });

        // Show or hide "Add User" button based on user type
        if ("Manager".equals(userType)) {
            menu.getItems().add("Add User");
            menu.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if ("Add User".equals(newValue)) {
                    showAddUserDialog();
                }
            });
        }
    }

    private void logOut(Stage primaryStage) {
        // Clear user-specific data if necessary
        userType = null;
        showLoginScreen(primaryStage);
    }

    private void showAddUserDialog() {
        Stage addUserStage = new Stage();
        addUserStage.setTitle("Add New User");

        VBox addUserLayout = new VBox(10);
        addUserLayout.setPadding(new Insets(10, 10, 10, 10));

        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField employeeIdField = new TextField();
        employeeIdField.setPromptText("Employee ID");

        Button addUserBtn = new Button("Add User");
        addUserBtn.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            int employeeId = Integer.parseInt(employeeIdField.getText());

            if (addUser(username, password, employeeId)) {
                showAlert(Alert.AlertType.INFORMATION, "User Added", "New user added successfully!");
                addUserStage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to add new user.");
            }
        });

        addUserLayout.getChildren().addAll(new Label("Username:"), usernameField, new Label("Password:"), passwordField, new Label("Employee ID:"), employeeIdField, addUserBtn);

        Scene addUserScene = new Scene(addUserLayout, 300, 200);
        addUserStage.setScene(addUserScene);
        addUserStage.show();
    }

    private boolean addUser(String username, String password, int employeeId) {
        boolean isSuccess = false;
        try {
            Connection con = new dataBaseConnection().getConnection().connectDB();
            String sql = "INSERT INTO user (username, password, employee_id) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setInt(3, employeeId);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                isSuccess = true;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isSuccess;
    }

    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
