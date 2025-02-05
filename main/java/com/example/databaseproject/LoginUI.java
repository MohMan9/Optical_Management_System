package com.example.databaseproject;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginUI {

    dataBaseConnection db = new dataBaseConnection();
    private Runnable onLoginSuccess;
    private String userType;

    public Scene createLoginScene(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // Set background image
        Image image = new Image(getClass().getResource("/pj.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(100, 100, true, true, false, true));
        grid.setBackground(new Background(backgroundImage));

        Label userName = new Label("Username or Employee ID:");
        userName.setStyle("-fx-text-fill: white;"); // Set text color to white for better visibility
        grid.add(userName, 0, 1);

        TextField userTextField = new TextField();
        grid.add(userTextField, 1, 1);

        Label pw = new Label("Password:");
        pw.setStyle("-fx-text-fill: white;"); // Set text color to white for better visibility
        grid.add(pw, 0, 2);

        PasswordField pwBox = new PasswordField();
        grid.add(pwBox, 1, 2);

        Button loginButton = new Button("Sign in");
        grid.add(loginButton, 1, 3);

        loginButton.setOnAction(e -> {
            String usernameOrId = userTextField.getText();
            String password = pwBox.getText();
            if (login(usernameOrId, password)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome!");
                if (onLoginSuccess != null) {
                    onLoginSuccess.run();
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username/employee ID or password.");
            }
        });

        return new Scene(grid, 300, 275);
    }

    public void setOnLoginSuccess(Runnable onLoginSuccess) {
        this.onLoginSuccess = onLoginSuccess;
    }

    public String getUserType() {
        return userType;
    }

    private boolean login(String usernameOrId, String password) {
        boolean isValid = false;
        try {
            Connection con = db.getConnection().connectDB();
            String sql;
            PreparedStatement pstmt;

            if (isNumeric(usernameOrId)) {
                sql = "SELECT e.type FROM user u JOIN employee e ON u.employee_id = e.snn WHERE u.employee_id = ? AND u.password = ?";
                pstmt = con.prepareStatement(sql);
                pstmt.setInt(1, Integer.parseInt(usernameOrId));
            } else {
                sql = "SELECT e.type FROM user u JOIN employee e ON u.employee_id = e.snn WHERE u.username = ? AND u.password = ?";
                pstmt = con.prepareStatement(sql);
                pstmt.setString(1, usernameOrId);
            }

            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                userType = rs.getString("type");
                isValid = true;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isValid;
    }

    private boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
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
