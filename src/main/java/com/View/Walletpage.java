package com.View;

import com.google.firebase.database.*;
import com.upnext.Model.Firebaseconfig;
import com.upnext.Model.Session;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Walletpage {

    private Label lb1 = new Label("₹0.00"); // Cashback
    private Label lb3 = new Label("₹0.00"); // Referral earnings

    public void show(Stage stage) {
        StackPane root = new StackPane();
        VBox vb2 = new VBox(30);
        vb2.setPadding(new Insets(30));
        vb2.setAlignment(Pos.TOP_CENTER);

        // Animated background
        LinearGradient gradient1 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#43cea2")), new Stop(1, Color.web("#185a9d")));
        LinearGradient gradient2 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#66a6ff")), new Stop(1, Color.web("#fddb92")));

        BackgroundFill fill1 = new BackgroundFill(gradient1, CornerRadii.EMPTY, Insets.EMPTY);
        BackgroundFill fill2 = new BackgroundFill(gradient2, CornerRadii.EMPTY, Insets.EMPTY);
        Background bg1 = new Background(fill1);
        Background bg2 = new Background(fill2);

        Timeline bgSwitch = new Timeline(
                new KeyFrame(Duration.ZERO, e -> root.setBackground(bg1)),
                new KeyFrame(Duration.seconds(7), e -> root.setBackground(bg2)),
                new KeyFrame(Duration.seconds(14), e -> root.setBackground(bg1))
        );
        bgSwitch.setCycleCount(Animation.INDEFINITE);
        bgSwitch.play();

        // Floating animation circles
        for (int i = 0; i < 25; i++) {
            Circle circle = new Circle(2, Color.WHITE);
            circle.setOpacity(0.2);
            Random rand = new Random();
            circle.setTranslateX(rand.nextInt(600) - 300);
            circle.setTranslateY(rand.nextInt(700) - 350);

            TranslateTransition transition = new TranslateTransition(Duration.seconds(10 + rand.nextInt(10)), circle);
            transition.setByY(-500);
            transition.setCycleCount(Animation.INDEFINITE);
            transition.setAutoReverse(false);
            transition.play();
            root.getChildren().add(circle);
        }

        // UI elements
        Label title = new Label("Wallet");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 36));
        title.setTextFill(Color.WHITE);

        Label lb = new Label("Cashback");
        lb.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lb.setTextFill(Color.web("#004d40"));
        lb1.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lb1.setTextFill(Color.web("#004d40"));

        VBox cashbackBox = new VBox(10, lb, lb1);
        cashbackBox.setPadding(new Insets(20));
        cashbackBox.setAlignment(Pos.CENTER_LEFT);
        cashbackBox.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        cashbackBox.setEffect(new DropShadow(6, Color.LIGHTGRAY));

        Label lb2 = new Label("Referral Earnings");
        lb2.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        lb2.setTextFill(Color.web("#004d40"));
        lb3.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        lb3.setTextFill(Color.web("#004d40"));

        VBox referralBox = new VBox(10, lb2, lb3);
        referralBox.setPadding(new Insets(20));
        referralBox.setAlignment(Pos.CENTER_LEFT);
        referralBox.setStyle("-fx-background-color: white; -fx-background-radius: 12;");
        referralBox.setEffect(new DropShadow(6, Color.LIGHTGRAY));

        Button backButton = new Button("← Back to Profile");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        backButton.setStyle("-fx-background-color: white; -fx-text-fill: #00796b; -fx-padding: 10 20; -fx-background-radius: 8;");
        backButton.setOnAction(e -> new Profilepage().start(stage));

        vb2.getChildren().addAll(title, cashbackBox, referralBox, backButton);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.2), vb2);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        root.getChildren().add(vb2);

        Scene scene = new Scene(root, 1020, 780);
        stage.setScene(scene);
        stage.setTitle("Wallet Page");
        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.setResizable(true);
        stage.show();

        loadEarningsFromFirebase();
    }

    private void loadEarningsFromFirebase() {
        String currentUsername = Session.getUsername();
        if (currentUsername == null || currentUsername.trim().isEmpty()) {
            System.err.println("Username not set in session!");
            return;
        }

        DatabaseReference cashbackRef = Firebaseconfig.getDatabase().getReference("cashbacks");

        cashbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                int totalCashback = 0;
                int totalReferral = 0;

                for (DataSnapshot child : snapshot.getChildren()) {
                    String username = child.child("username").getValue(String.class);

                    if (username != null && username.trim().equalsIgnoreCase(currentUsername.trim())) {
                        Integer cashback = child.child("cashback").getValue(Integer.class);
                        Integer referral = child.child("referralEarnings").getValue(Integer.class);

                        if (cashback != null) totalCashback += cashback;
                        if (referral != null) totalReferral += referral;
                    }
                }

                final int finalCashback = totalCashback;
                final int finalReferral = totalReferral;

                Platform.runLater(() -> {
                    lb1.setText("₹" + finalCashback + ".00");
                    lb3.setText("₹" + finalReferral + ".00");
                });
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Platform.runLater(() -> {
                    lb1.setText("Error");
                    lb3.setText("Error");
                });
            }
        });
    }
}
