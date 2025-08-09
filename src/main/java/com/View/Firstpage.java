package com.View;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Firstpage extends Application {

    Scene page1Scene;
    Stage primaryStage;

    @Override
    public void start(Stage stage) {
        
        ImageView logo = new ImageView(new Image("assets\\Influpay.png", 300, 300, true, true));
        logo.setOpacity(0);

        
        Label appName = new Label("Influpay");
        appName.setFont(Font.font("Verdana", FontWeight.BOLD, 30));
        appName.setTextFill(Color.ANTIQUEWHITE);
        appName.setOpacity(0);

      
        Label tagline = new Label("Earn from your influence");
        tagline.setFont(Font.font("Arial", FontWeight.NORMAL, 16));
        tagline.setTextFill(Color.BLACK);
        tagline.setOpacity(0);

        VBox content = new VBox(15, logo, appName, tagline);
        content.setAlignment(Pos.CENTER);

        StackPane root = new StackPane(content);
        //root.setBackground(new Background(new BackgroundFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,new Stop(0, Color.web("#667eea")),new Stop(1, Color.web("#764ba2"))), CornerRadii.EMPTY, Insets.EMPTY)));
        //root.setStyle("-fx-background-color: linear-gradient(to bottom, #c2f0c2, #f7f5aa);");
        root.setBackground(new Background(new BackgroundFill(new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,new Stop(0, Color.web("#43cea2")), new Stop(1, Color.web("#185a9d"))),CornerRadii.EMPTY, Insets.EMPTY)));

        Scene scene = new Scene(root, 1020,780);
        stage.setScene(scene);
        stage.setTitle("Influpay - Splash Screen");
        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.setResizable(true); 
        stage.show();

        page1Scene = scene;
        primaryStage = stage;

    
        FadeTransition fadeLogo = new FadeTransition(Duration.seconds(1), logo);
        fadeLogo.setFromValue(0);
        fadeLogo.setToValue(1);

        TranslateTransition slideApp = new TranslateTransition(Duration.seconds(1), appName);
        slideApp.setFromY(20);
        slideApp.setToY(0);
        slideApp.setDelay(Duration.seconds(1));

        FadeTransition fadeApp = new FadeTransition(Duration.seconds(1), appName);
        fadeApp.setFromValue(0);
        fadeApp.setToValue(1);
        fadeApp.setDelay(Duration.seconds(1));

        FadeTransition fadeTag = new FadeTransition(Duration.seconds(1), tagline);
        fadeTag.setFromValue(0);
        fadeTag.setToValue(1);
        fadeTag.setDelay(Duration.seconds(2));

       
        fadeLogo.play();
        slideApp.play();
        fadeApp.play();
        fadeTag.play();

        PauseTransition delay = new PauseTransition(Duration.seconds(4));
        delay.setOnFinished(e -> openLogin(stage));
        delay.play();
    }



    private void openLogin(Stage stage) {
        Home loginPage = new Home();
        try {
            loginPage.start(stage); 
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
