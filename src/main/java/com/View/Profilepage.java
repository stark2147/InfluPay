package com.View;

import com.google.firebase.database.*;
import com.upnext.Model.Firebaseconfig;
import com.upnext.Model.Session;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Profilepage extends Application {

    private Label nameLabel = new Label();
    private Label emailLabel = new Label();
    private Label phoneLabel = new Label();
    private Label typeLabel = new Label();

    @Override
    public void start(Stage primaryStage) {
        Firebaseconfig.initialize();

        VBox userInfoBox = new VBox(15, nameLabel, emailLabel, phoneLabel, typeLabel);
        userInfoBox.setAlignment(Pos.CENTER_LEFT);
        userInfoBox.setPadding(new Insets(30));
        userInfoBox.setStyle("-fx-font-size: 16px; -fx-background-color: white; -fx-background-radius: 10;");

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
        Text tx1 = new Text("\uD83D\uDCB3  Wallet");
        tx1.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        tx1.setFill(Color.WHITE);
    
        tx1.setFont(Font.font("Arial", FontWeight.BOLD, 22));
        tx1.setFill(Color.WHITE);
        tx1.setOnMouseClicked(e -> {
             try {
             Stage walletStage = new Stage();
             new Walletpage().show(walletStage);
             primaryStage.close(); 
            } catch (Exception ex) {
            ex.printStackTrace();
              }
        });

        VBox container = new VBox(30, userInfoBox, backBtn,tx1);
        container.setAlignment(Pos.CENTER);
        container.setPadding(new Insets(40));

        BorderPane root = new BorderPane(container);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to bottom right, #43cea2, #185a9d);");

        Scene scene = new Scene(root, 1020, 780);
        primaryStage.setTitle("Profile - Influpay");
        primaryStage.setScene(scene);
      primaryStage.setResizable(true);
       primaryStage.getIcons().add(new Image("assets\\newAppLogo.jpg"));

        primaryStage.show();

        loadUserData();
    }

    private void loadUserData() {
        String uid = Session.getCurrentUid();

        if (uid == null || uid.isEmpty()) {
            showError("Session UID not found. Please log in again.");
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    showError("User data not found for UID: " + uid);
                    return;
                }

                String name = snapshot.child("name").getValue(String.class);
                String email = snapshot.child("email").getValue(String.class);
                String phone = snapshot.child("phone").getValue(String.class);
                String type = snapshot.child("type").getValue(String.class);

                Platform.runLater(() -> {
                    nameLabel.setText("👤 Name: " + (name != null ? name : ""));
                    emailLabel.setText("📧 Email: " + (email != null ? email : ""));
                    phoneLabel.setText("📞 Phone: " + (phone != null ? phone : ""));
                    typeLabel.setText("🎯 User Type: " + (type != null ? type : ""));
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                showError("Error loading user data: " + error.getMessage());
            }
        });
    }

    private void showError(String message) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
  
}

