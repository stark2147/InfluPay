package com.View;

import javafx.animation.FadeTransition;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AboutAdmin {

    Stage SettingStage;
    Scene SettingScene;

    public void setSettingStage(Stage settingStage) {
        SettingStage = settingStage;
    }

    public void setSettingScene(Scene settingScene) {
        SettingScene = settingScene;
    }

    public StackPane createSettingPage(Runnable back) {
        // Top bar
        HBox topBar = new HBox();
        topBar.setStyle("-fx-background-color: #144d4d;");
        topBar.setPadding(new Insets(20));
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setSpacing(600);

        Label appName = new Label("InfluPay");
        appName.setTextFill(Color.WHITE);
        appName.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        Button logoutBtn = new Button("Logout");
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: white; -fx-font-weight: bold;");
        logoutBtn.setOnAction(event -> back.run());

        topBar.getChildren().addAll(appName, logoutBtn);

        
        VBox card = new VBox(20);
        card.setPadding(new Insets(30));
        card.setStyle(
            "-fx-background-color: white; " +
            "-fx-background-radius: 10; " +
            "-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 10, 0, 0, 5);"
        );
        card.setAlignment(Pos.TOP_LEFT);

        Text settingsTitle = new Text("Settings");
        settingsTitle.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        settingsTitle.setFill(Color.web("#144d4d"));

        VBox aboutBox = createLabeledField("About Info", "This app allows admins to manage users, influencers, transactions, and cashback operations.");
        VBox cashbackBox = createLabeledField("Cashback Info", "Cashback is processed within 7 business days after user verification.");

        Button saveBtn = new Button("Save");
        saveBtn.setStyle("-fx-background-color: #144d4d; -fx-text-fill: white; -fx-font-weight: bold;");
        saveBtn.setPadding(new Insets(8, 20, 8, 20));
        saveBtn.setOnAction(event -> System.out.println("Saved Successfully"));

        Button backbtn = new Button("Back");
        backbtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white; -fx-font-weight: bold;");
        backbtn.setPadding(new Insets(8, 20, 8, 20));
        backbtn.setOnAction(event -> back.run());

        HBox buttonsBox = new HBox(10, saveBtn, backbtn);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        card.getChildren().addAll(settingsTitle, aboutBox, cashbackBox, buttonsBox);

        VBox mainLayout = new VBox(20, topBar, card);
        mainLayout.setPadding(new Insets(30));
        mainLayout.setStyle("-fx-background-color: transparent;");

        
        Image bgImage = new Image("assets\\Aboutus.jpg"); 
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(1000);
        bgImageView.setFitHeight(700);
        bgImageView.setPreserveRatio(false);
        bgImageView.setOpacity(0.2); 

        StackPane root = new StackPane(bgImageView, mainLayout);

       
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), mainLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        return root;
    }

    private VBox createLabeledField(String labelText, String initialText) {
        Label label = new Label(labelText);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        TextArea textArea = new TextArea(initialText);
        textArea.setWrapText(true);
        textArea.setPrefRowCount(2);
        textArea.setStyle("-fx-background-radius: 6;");

        return new VBox(5, label, textArea);
    }
}

