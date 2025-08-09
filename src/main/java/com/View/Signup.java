
package com.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upnext.Model.Firebaseconfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class Signup {

    public void start(Stage primaryStage) {
        Firebaseconfig.initialize(); 

        Label titleLabel = new Label("Create Your InfluPay Account");
        titleLabel.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");

        Label emailLabel = new Label("📧 Email:");
        TextField emailField = new TextField();
        emailField.setPromptText("Enter your email");

        Label passwordLabel = new Label("🔒 Password:");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter your password");

        Label nameLabel = new Label("👤 Full Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Enter your full name");

        Label phoneLabel = new Label("📞 Phone Number:");
        TextField phoneField = new TextField();
        phoneField.setPromptText("Enter your phone number");

        Label typeLabel = new Label("🎯 User Type:");
        ComboBox<String> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll("influencer", "regular");
        typeCombo.setPromptText("Select user type");

        Button signupButton = new Button("Sign Up");
        signupButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-weight: bold;");


        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> {
            try {
                String username = emailLabel.getText().split("@")[0];
                new First(username).start(new Stage());
                primaryStage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        signupButton.setOnAction(e -> {
            String email = emailField.getText().trim();
            String password = passwordField.getText().trim();
            String name = nameField.getText().trim();
            String phone = phoneField.getText().trim();
            String type = typeCombo.getValue();

            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || phone.isEmpty() || type == null) {
                showAlert(Alert.AlertType.ERROR, "Validation Error", "All fields are required.");
                return;
            }

            try {
                UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                        .setEmail(email)
                        .setPassword(password);

                UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
                String uid = userRecord.getUid();

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("users")
                        .child(uid);

                Map<String, Object> userMap = new HashMap<>();
                userMap.put("uid", uid);
                userMap.put("email", email);
                userMap.put("name", name);
                userMap.put("phone", phone);
                userMap.put("type", type);

                ref.setValueAsync(userMap);

                showAlert(Alert.AlertType.INFORMATION, "Success", "User registered successfully!");

                

            
                com.upnext.Model.Session.setCurrentUid(uid);


                String usernameOnly = email.split("@")[0];
                new First(usernameOnly).start(primaryStage);


            } catch (Exception ex) {
                ex.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Signup Failed", ex.getMessage());
            }
        });

        


        Button backButton = new Button("Back to Login");
        backButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        backButton.setOnAction(e -> new Home().start(primaryStage));

        VBox form = new VBox(10,
                titleLabel,
                emailLabel, emailField,
                passwordLabel, passwordField,
                nameLabel, nameField,
                phoneLabel, phoneField,
                typeLabel, typeCombo,
                signupButton, backButton
        );
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(30));
        form.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        form.setMaxWidth(400);

        BorderPane root = new BorderPane(form);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #43cea2, #185a9d);");

        Scene scene = new Scene(root, 1020, 780);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Influpay - Signup");
        primaryStage.setResizable(true);
        primaryStage.getIcons().add(new Image("assets\\newAppLogo.jpg"));

        primaryStage.show();
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}


