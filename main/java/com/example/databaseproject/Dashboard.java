package com.example.databaseproject;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class Dashboard {
    dataBaseConnection db = new dataBaseConnection();

    public BorderPane createDashboardLayout(Stage primaryStage, Scene homeScene) {
        BorderPane dashboardRoot = new BorderPane();
        dashboardRoot.setPrefSize(1200, 800);

        // Top Layout with Home Button
        HBox topLayout = new HBox(10);
        topLayout.setPadding(new Insets(10));
        topLayout.setAlignment(Pos.CENTER_LEFT);
        Button homeButton = new Button("Home");
        homeButton.setOnAction(e -> primaryStage.setScene(homeScene));
        topLayout.getChildren().add(homeButton);

        // Overview section
        HBox overviewSection = new HBox(20);
        overviewSection.setPadding(new Insets(20));
        overviewSection.getChildren().addAll(
                createOverviewCard("Total Employees", getTotalEmployees()),
                createOverviewCard("Total Customers", getTotalCustomers()),
                createOverviewCard("Average Employee Rate", getAverageEmployeeRate()),
                createOverviewCard("Best Payment Day", getBestPaymentDay()),
                createOverviewCard("Employee with Max Rate", getEmployeeWithMaxRate()),
                createOverviewCard("Best Payment Year", getBestPaymentYear())
        );

        // Chart section
        VBox chartSection = new VBox(20);
        chartSection.setPadding(new Insets(20));
        chartSection.getChildren().addAll(
                createPaymentsChart("Daily Payments", "day"),
                createPaymentsChart("Monthly Payments", "month"),
                createPaymentsChart("Yearly Payments", "year")
        );

        // Recent transactions section
        VBox recentTransactionsSection = new VBox(20);
        recentTransactionsSection.setPadding(new Insets(20));
        Label recentTransactionsLabel = new Label("Recent Transactions");
        TableView<Transaction> transactionsTable = createTransactionsTable();
        recentTransactionsSection.getChildren().addAll(recentTransactionsLabel, transactionsTable);

        // Combine sections
        dashboardRoot.setTop(overviewSection);
        dashboardRoot.setCenter(chartSection);
        dashboardRoot.setBottom(recentTransactionsSection);
        dashboardRoot.setRight(topLayout);
        return dashboardRoot;
    }

    private VBox createOverviewCard(String title, String value) {
        VBox card = new VBox(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: white;");
        Label titleLabel = new Label(title);
        Label valueLabel = new Label(value);
        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
    }

    private String getTotalEmployees() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT COUNT(*) AS total FROM employee";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("total");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getTotalCustomers() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT COUNT(*) AS total FROM customer";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("total");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0";
    }

    private String getAverageEmployeeRate() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT AVG(rate) AS average FROM employee";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return String.format("%.2f", rs.getDouble("average"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.00";
    }

    private String getBestPaymentDay() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT date, SUM(type_price) AS total FROM paying GROUP BY date ORDER BY total DESC LIMIT 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("date");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private String getEmployeeWithMaxRate() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT Ename FROM employee ORDER BY rate DESC LIMIT 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("Ename");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private String getBestPaymentYear() {
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT YEAR(date) AS year, SUM(type_price) AS total FROM paying GROUP BY YEAR(date) ORDER BY total DESC LIMIT 1";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getString("year");
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "N/A";
    }

    private PieChart createPaymentsChart(String title, String type) {
        PieChart chart = new PieChart();
        chart.setTitle(title);

        try {
            Connection con = db.getConnection().connectDB();
            String sql = "";
            if (type.equals("day")) {
                sql = "SELECT date, SUM(type_price) AS total FROM paying GROUP BY date";
            } else if (type.equals("month")) {
                sql = "SELECT DATE_FORMAT(date, '%Y-%m') AS month, SUM(type_price) AS total FROM paying GROUP BY month";
            } else if (type.equals("year")) {
                sql = "SELECT YEAR(date) AS year, SUM(type_price) AS total FROM paying GROUP BY year";
            }

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String label = "";
                if (type.equals("day")) {
                    label = rs.getString("date");
                } else if (type.equals("month")) {
                    label = rs.getString("month");
                } else if (type.equals("year")) {
                    label = rs.getString("year");
                }
                PieChart.Data data = new PieChart.Data(label, rs.getDouble("total"));
                chart.getData().add(data);

                // Add tooltip to chart data
                Tooltip tooltip = new Tooltip("Total: " + rs.getDouble("total"));
                Tooltip.install(data.getNode(), tooltip);
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return chart;
    }

    private TableView<Transaction> createTransactionsTable() {
        TableView<Transaction> table = new TableView<>();
        table.setEditable(false);

        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setMinWidth(200);
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));

        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setMinWidth(200);
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));

        TableColumn<Transaction, String> amountColumn = new TableColumn<>("Amount");
        amountColumn.setMinWidth(200);
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));

        table.getColumns().addAll(dateColumn, typeColumn, amountColumn);
        table.setItems(getTransactions());

        return table;
    }

    private ObservableList<Transaction> getTransactions() {
        ObservableList<Transaction> transactions = FXCollections.observableArrayList();
        try {
            Connection con = db.getConnection().connectDB();
            String sql = "SELECT date, type, type_price FROM paying ORDER BY date DESC LIMIT 10";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                transactions.add(new Transaction(rs.getString("date"), rs.getString("type"), rs.getString("type_price")));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return transactions;
    }

    public static class Transaction {
        private final StringProperty date;
        private final StringProperty type;
        private final StringProperty amount;

        public Transaction(String date, String type, String amount) {
            this.date = new SimpleStringProperty(date);
            this.type = new SimpleStringProperty(type);
            this.amount = new SimpleStringProperty(amount);
        }

        public String getDate() {
            return date.get();
        }

        public void setDate(String date) {
            this.date.set(date);
        }

        public StringProperty dateProperty() {
            return date;
        }

        public String getType() {
            return type.get();
        }

        public void setType(String type) {
            this.type.set(type);
        }

        public StringProperty typeProperty() {
            return type;
        }

        public String getAmount() {
            return amount.get();
        }

        public void setAmount(String amount) {
            this.amount.set(amount);
        }

        public StringProperty amountProperty() {
            return amount;
        }
    }
}
