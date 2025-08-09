package com.View;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Timer;
import java.util.TimerTask;

public class Referralpage1 extends Application {
    


    @Override
    public void start(Stage stage) {
        
        String uid = com.upnext.Model.Session.getCurrentUid();
        String referralCode = com.upnext.Model.ReferralService.getReferralCodeForUser(uid); 

     
        Button backButton = new Button("←");
        backButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        backButton.setStyle("-fx-background-color: transparent; -fx-text-fill: #0A1E4A;");
        backButton.setOnAction(e -> {
            try {
                new First().start(new Stage());
                stage.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        HBox topBar = new HBox(backButton);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(0, 0, 10, 0));

        Text title = new Text("Refer & Earn");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 28));
        title.setFill(Color.web("#0A1E4A"));

        HBox hb = new HBox(15);
        hb.setPadding(new Insets(10));
        hb.setAlignment(Pos.CENTER_LEFT);
        hb.setPrefHeight(170);

        String[] str = {
            "assets\\rf1.jpg",
            "assets\\rf222.jpeg",
            "assets\\rf3.jpg"
        };

        for (String path : str) {
            ImageView imgView = new ImageView(new Image(path));
            imgView.setFitWidth(300);
            imgView.setFitHeight(300);
            imgView.setPreserveRatio(false);
            imgView.setSmooth(true);
            imgView.setStyle("-fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.05), 10, 0, 0, 3);");
            hb.getChildren().add(imgView);
        }

        ScrollPane scrollPane = new ScrollPane(hb);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setPrefHeight(300);
        scrollPane.setStyle("-fx-background-color: transparent;");

     
        Text inviteText = new Text("Invite your friends to InfluPay and earn up to ₹50 when they sign up and use!");
        inviteText.setWrappingWidth(360);
        inviteText.setFont(Font.font("Arial", 14));
        inviteText.setFill(Color.web("#333"));

      
        Text tx1 = new Text("🔗 Referral Code:");
        tx1.setFont(Font.font("Arial", FontWeight.SEMI_BOLD, 16));
        tx1.setFill(Color.web("#111"));

        TextField referralField = new TextField("influ123");
        referralField.setEditable(false);
        referralField.setStyle("-fx-background-color: #f1f1f1; -fx-border-color: transparent; -fx-background-radius: 8; -fx-font-weight: bold; -fx-text-fill: #6A5ACD;");
        referralField.setMaxWidth(200);
        referralField.setAlignment(Pos.CENTER);

        Button Btn = new Button("📋 Copy Code");
        Btn.setStyle("-fx-background-color: #00BFFF; -fx-text-fill: white; -fx-background-radius: 8;");
        Btn.setOnAction(e -> {
            referralField.selectAll();
            referralField.copy();
        });

        VBox referralBox = new VBox(10, tx1, referralField, Btn);
        referralBox.setAlignment(Pos.CENTER);

        Button shareBtn = new Button("📨 Share via WhatsApp");
        shareBtn.setMaxWidth(200);
        shareBtn.setMaxHeight(500);
        shareBtn.setStyle("-fx-background-color: #0cce53ff; -fx-text-fill: white; -fx-background-radius: 8;");

        VBox vb1 = new VBox(shareBtn);
        vb1.setAlignment(Pos.CENTER);
        vb1.setPadding(new Insets(20, 0, 0, 0));

   
        Label pollingLabel = new Label("Waiting for your friend to sign up...");
        pollingLabel.setTextFill(Color.GRAY);
        pollingLabel.setVisible(false);
        vb1.getChildren().add(pollingLabel);

     
        shareBtn.setOnAction(e -> {
            try {
                String signupLink = "https://yourappdomain.com/signup?ref=" + referralCode;
                String message = "Join InfluPay and earn ₹50! Use my referral code: " + referralCode + " Sign up here: " + signupLink;
                String encodedMsg = URLEncoder.encode(message, StandardCharsets.UTF_8);
                String url = "https://wa.me/?text=" + encodedMsg;

                Desktop.getDesktop().browse(new URI(url));

                pollingLabel.setVisible(true); 
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        boolean verified = com.upnext.Model.ReferralChecker.checkReferralStatus(uid);
                        if (verified) {
                            timer.cancel();
                            javafx.application.Platform.runLater(() -> {
                                try {
                                    new Referralpage().start(new Stage());
                                    stage.close();
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            });
                        }
                    }
                }, 0, 5000);

                stage.setOnCloseRequest(event -> timer.cancel());

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

      
        VBox rootVBox = new VBox(20, topBar, title, scrollPane, inviteText, referralBox, vb1);
        rootVBox.setPadding(new Insets(20));
        rootVBox.setAlignment(Pos.TOP_CENTER);
        rootVBox.setStyle("-fx-background-color: #E8F0FE;");

        Scene scene = new Scene(rootVBox, 1020, 780);
        stage.setTitle("Refer & Earn");
        stage.setScene(scene);
         stage.setResizable(true);
        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.show();
    }
}
