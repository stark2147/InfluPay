package com.View;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.upnext.Model.Session;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class Shoplogin {

    private static final String API_KEY = "AIzaSyBg6JgsNY_66jwXYfNp_8ra8mZkGhfHoAA";

    public Scene createScene(Stage primaryStage) {
        Text title = new Text("Shopkeeper Login");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#2d1844ff"));

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your shop email");

        Label passwordLabel = new Label("🔒 Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Label messageLabel = new Label();

        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        loginButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        loginButton.setMaxWidth(Double.MAX_VALUE);

        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 12px; -fx-background-color: #94bee8ff; -fx-text-fill: white;");
        backButton.setOnAction(e->{
             try {
                new Home().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Hyperlink registerLink = new Hyperlink("Don't have an account? Register here.");
        registerLink.setTextFill(Color.BLUE);
        registerLink.setBorder(Border.EMPTY);
        registerLink.setOnAction(event -> {
            Shopsignup signup = new Shopsignup();
            Scene signupScene = signup.createScene(primaryStage);
            primaryStage.setScene(signupScene);
        });

        loginButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Please enter email and password.");
                return;
            }

            try {
                String userId = authenticateWithFirebase(email, password, messageLabel);
                if (userId != null) {
                   
                    Session.setCurrentUid(userId);

                    messageLabel.setTextFill(Color.GREEN);
                    messageLabel.setText("Login successful!");

                    ShopDashboard dashboard = new ShopDashboard();
                    dashboard.start(primaryStage);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Login failed: " + ex.getMessage());
            }
        });

        VBox formBox = new VBox(10, emailLabel, emailField, passwordLabel, passwordField, loginButton, messageLabel, registerLink,backButton);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(300);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 5);");

        VBox layout = new VBox(30, title, formBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#43cea2")), new Stop(1, Color.web("#185a9d"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        return new Scene(layout, 1020, 780);
    }

    private String authenticateWithFirebase(String email, String password, Label messageLabel) throws IOException {
        String firebaseUrl = "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY;

        String payload = "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"returnSecureToken\":true"
                + "}";

        HttpURLConnection conn = (HttpURLConnection) new URL(firebaseUrl).openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes(StandardCharsets.UTF_8));
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        String response = readStream(is);

        if (responseCode == 200) {
            JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();
            return jsonObject.get("localId").getAsString();
        } else {
            JsonObject errorObj = JsonParser.parseString(response).getAsJsonObject();
            String errorMessage = errorObj.getAsJsonObject("error").get("message").getAsString();
            messageLabel.setText("Error: " + errorMessage);
            return null;
        }
    }

    private String readStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();
        return response.toString();
    }
}




