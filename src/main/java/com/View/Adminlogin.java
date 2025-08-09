package com.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.*;
import javafx.stage.Stage;

public class Adminlogin {

    private final String ADMIN_EMAIL = "admin@influpay.com";
    private final String ADMIN_PASSWORD = "admin123";

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Influpay Admin Login");

        // Title
        Text title = new Text("Admin Panel");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#0d142fff"));

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("admin@influpay.com");

        Label passLabel = new Label("🔒 Password:");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Enter password");

        Button loginBtn = new Button("Login");
        loginBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        loginBtn.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passField.getText().trim();

            if (email.equals(ADMIN_EMAIL) && password.equals(ADMIN_PASSWORD)) {
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, Admin!");
                AdminDashboard dashboard = new AdminDashboard();
                dashboard.start(primaryStage); // Navigate to dashboard
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials.");
            }
        });
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 12px; -fx-background-color: #aed0f3ff; -fx-text-fill: white;");
        backButton.setOnAction(e->{
             try {
                new Home().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        VBox form = new VBox(15, emailLabel, emailField, passLabel, passField, loginBtn,backButton);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(20));
        form.setMaxWidth(300);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");

        VBox layout = new VBox(30, title, form);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(50));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#43cea2")), new Stop(1, Color.web("#185a9d"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(layout, 1020, 780);
        primaryStage.setScene(scene);
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}

