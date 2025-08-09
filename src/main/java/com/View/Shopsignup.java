package com.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;  // ✅ Import this for JSON parsing

import com.upnext.Model.Session;

public class Shopsignup {

    private static final String API_KEY = "AIzaSyBg6JgsNY_66jwXYfNp_8ra8mZkGhfHoAA";

    public Scene createScene(Stage primaryStage) {
        Text title = new Text("Shopkeeper Signup");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 30));
        title.setFill(Color.web("#ff6600"));

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        Label passwordLabel = new Label("🔒 Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Create a password");

        Label messageLabel = new Label();
        messageLabel.setTextFill(Color.RED);

        Button signupButton = new Button("Sign Up");
        signupButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        signupButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        signupButton.setMaxWidth(Double.MAX_VALUE);

        Hyperlink loginLink = new Hyperlink("Already have an account? Login here.");
        loginLink.setTextFill(Color.BLUE);
        loginLink.setBorder(Border.EMPTY);
        loginLink.setOnAction(event -> {
            Shoplogin login = new Shoplogin();
            Scene loginScene = login.createScene(primaryStage);
            primaryStage.setScene(loginScene);
            primaryStage.getIcons().add(new Image("assets\\Applogo.jpg"));
        });

        signupButton.setOnAction(e -> {
            String email = emailField.getText();
            String password = passwordField.getText();
            try {
                String uid = registerWithFirebase(email, password); // ✅ now returns UID
                if (uid != null) {
                    messageLabel.setTextFill(Color.GREEN);
                    messageLabel.setText("Signup successful!");

                    Session.setCurrentUid(uid);  // ✅ Save UID

                    ShopDashboard dashboard = new ShopDashboard();
                    dashboard.start(primaryStage);  // ✅ Redirect to dashboard
                } else {
                    messageLabel.setTextFill(Color.RED);
                    messageLabel.setText("Signup failed. Email may already be in use.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                messageLabel.setTextFill(Color.RED);
                messageLabel.setText("Signup failed: " + ex.getMessage());
            }
        });

        VBox formBox = new VBox(10, emailLabel, emailField, passwordLabel, passwordField,
                signupButton, messageLabel, loginLink);
        formBox.setAlignment(Pos.CENTER);
        formBox.setPadding(new Insets(20));
        formBox.setMaxWidth(300);
        formBox.setStyle("-fx-background-color: white; -fx-background-radius: 12; "
                + "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.15), 10, 0, 0, 5);");

        VBox layout = new VBox(30, title, formBox);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(40));
        layout.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#558e73ff")), new Stop(1, Color.web("#1c2a45ff"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        return new Scene(layout, 1020, 780);
    }

    private String registerWithFirebase(String email, String password) throws IOException {
        String urlStr = "https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY;

        String payload = "{"
                + "\"email\":\"" + email + "\","
                + "\"password\":\"" + password + "\","
                + "\"returnSecureToken\":true"
                + "}";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(payload.getBytes());
            os.flush();
        }

        int responseCode = conn.getResponseCode();
        InputStream is = (responseCode == 200) ? conn.getInputStream() : conn.getErrorStream();
        String response = readStream(is);

        System.out.println("Firebase Response: " + response);  // ✅ Optional Debug

        if (responseCode == 200) {
            JSONObject json = new JSONObject(response);  // ✅ Parse response properly
            String userId = json.getString("localId");  
            System.out.println("New user ID: " + userId);
            return userId;
        } else {
            System.err.println("Signup failed: " + response);
            return null;
        }
    }

    private String readStream(InputStream is) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) sb.append(line);
        return sb.toString();
    }
}