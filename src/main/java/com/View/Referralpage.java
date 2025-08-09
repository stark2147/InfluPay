package com.View;

import javafx.animation.*;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Referralpage extends Application {

    @Override
    public void start(Stage stage) {
    
        ImageView doneIcon = new ImageView(new Image("https://cdn-icons-png.flaticon.com/512/845/845646.png"));
        doneIcon.setFitWidth(60);
        doneIcon.setFitHeight(60);
        doneIcon.setScaleX(0);
        doneIcon.setScaleY(0); 

     
        Label sparkles = new Label("✨ ✨ ✨");
        sparkles.setFont(Font.font("Arial", 26));
        sparkles.setTextFill(Color.web("#FFC107"));

      
        Label heading = new Label("Referral Verified!");
        heading.setFont(Font.font("Arial", 30));
        heading.setTextFill(Color.web("#2E7D32"));

     
        Label subheading = new Label("Your referral has been successfully verified.");
        subheading.setFont(Font.font("Arial", 16));
        subheading.setTextFill(Color.web("#555"));

        Label amount = new Label("💰 You’ve earned ₹50 cashback.");
        amount.setFont(Font.font("Arial", 20));
        amount.setTextFill(Color.web("#000"));

       
        Button backButton = new Button("Back to Dashboard");
        backButton.setFont(Font.font("Arial", 14));
        backButton.setStyle(
            "-fx-background-color: #1976D2;" +
            "-fx-text-fill: white;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-background-radius: 8;"
        );

        VBox contentBox = new VBox(15, doneIcon, sparkles, heading, subheading, amount, backButton);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.setPadding(new Insets(40));

        StackPane root = new StackPane(contentBox);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #F5F9FF, #E3F2FD);");

        Scene scene = new Scene(root, 1020, 780);
        stage.setScene(scene);
        stage.setTitle("Referral Verified");

        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.setResizable(true);
        
        ScaleTransition doneZoomIn = new ScaleTransition(Duration.seconds(0.8), doneIcon);
        doneZoomIn.setFromX(0.0);
        doneZoomIn.setFromY(0.0);
        doneZoomIn.setToX(1.0);
        doneZoomIn.setToY(1.0);
        doneZoomIn.setCycleCount(1);
        doneZoomIn.setAutoReverse(false);

       
        TranslateTransition sparkleFloat = new TranslateTransition(Duration.seconds(1.5), sparkles);
        sparkleFloat.setFromY(0);
        sparkleFloat.setToY(-10);
        sparkleFloat.setCycleCount(Animation.INDEFINITE);
        sparkleFloat.setAutoReverse(true);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.0), contentBox);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

      
        fadeIn.play();
        doneZoomIn.play();
        sparkleFloat.play();

        stage.show();
    }

    
}