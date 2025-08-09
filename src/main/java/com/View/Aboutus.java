package com.View;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Aboutus {

    Stage settingStage;
    Scene settingScene;

    public void setSettingStage(Stage settingStage) {
        this.settingStage = settingStage;
    }

    public void setSettingScene(Scene settingScene) {
        this.settingScene = settingScene;
    }

    public StackPane createSettingPage(Runnable backAction) {

        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/assets/modified about us page.png")));
        imageView.setPreserveRatio(true);
        imageView.setFitWidth(1020);

        
        VBox imageContainer = new VBox(imageView);
        imageContainer.setPadding(new Insets(20));
        imageContainer.setAlignment(Pos.CENTER);

       
        ScrollPane scrollPane = new ScrollPane(imageContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setPannable(true);

       
        Button backButton = new Button("← Back");
        backButton.setStyle("-fx-font-size: 16px; -fx-background-color: #2c3e50; -fx-text-fill: white;");
        backButton.setOnAction(e -> backAction.run());

    
        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10));
        topBar.setStyle("-fx-background-color: #34495e;");

        
        VBox content = new VBox(topBar, scrollPane);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        StackPane root = new StackPane(content);
        return root;
    }
}


