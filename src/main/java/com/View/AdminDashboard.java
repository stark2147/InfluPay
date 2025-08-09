package com.View;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class AdminDashboard {

    Stage DashboardStage;
    Scene BrandScene, UserScene, SettingScene, DashboardScene, TrackVerificationScene;

    public void start(Stage primaryStage) {
        // Top bar title
        Text title = new Text("Welcome, Admin");
        title.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        title.setFill(Color.web("#1d3557"));

        FadeTransition fadeInTitle = new FadeTransition(Duration.seconds(2), title);
        fadeInTitle.setFromValue(0.0);
        fadeInTitle.setToValue(1.0);

        TranslateTransition slideInTitle = new TranslateTransition(Duration.seconds(2), title);
        slideInTitle.setFromX(-200);
        slideInTitle.setToX(0);

        ParallelTransition titleAnimation = new ParallelTransition(fadeInTitle, slideInTitle);
        titleAnimation.play();

        HBox topBar = new HBox(title);
        topBar.setAlignment(Pos.TOP_CENTER);
        topBar.setPadding(new Insets(30, 0, 20, 40));

        // Sidebar Buttons
        Button brandbtn = createSidebarButton("🏬 Shop Management");
        brandbtn.setOnAction(event -> {
            intializeProfilepage();
            DashboardStage.setScene(BrandScene);
        });

        Button userbtn = createSidebarButton("👤 Manage user");
        userbtn.setOnAction(event -> {
            intializeUserPage();
            DashboardStage.setScene(UserScene);
        });

        Button authbtn = createSidebarButton("✅ Track Verification");
        authbtn.setOnAction(event -> {
            intializeTrackverificationpage();
            DashboardStage.setScene(TrackVerificationScene);
        });

        Button settingbtn = createSidebarButton(" About Us");
        settingbtn.setOnAction(event -> {
            intializeSettingpage();
            DashboardStage.setScene(SettingScene);
        });

        Button logout = createSidebarButton("LogOut");
        logout.setOnAction(e -> {
            try {
                new Home().start(new Stage());
                ((Stage) logout.getScene().getWindow()).close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Text sidebarTitle = new Text("📊 Dashboard Menu");
        sidebarTitle.setFont(Font.font("Segoe UI", FontWeight.BOLD, 18));
        sidebarTitle.setFill(Color.web("#1d3557"));

        FadeTransition fadeMenu = new FadeTransition(Duration.seconds(2), sidebarTitle);
        fadeMenu.setFromValue(0.0);
        fadeMenu.setToValue(1.0);

        TranslateTransition slideMenu = new TranslateTransition(Duration.seconds(2), sidebarTitle);
        slideMenu.setFromX(150);
        slideMenu.setToX(0);

        ParallelTransition sidebarAnim = new ParallelTransition(fadeMenu, slideMenu);
        sidebarAnim.play();

        VBox centerContent = new VBox(20, topBar);
        centerContent.setAlignment(Pos.TOP_LEFT);
        centerContent.setPadding(new Insets(0, 20, 0, 40));

        VBox sidebar = new VBox(20, sidebarTitle, brandbtn, userbtn, authbtn, settingbtn, logout);
        sidebar.setAlignment(Pos.TOP_CENTER);
        sidebar.setPadding(new Insets(60, 30, 0, 30));
        sidebar.setStyle("-fx-background-color: #dce9e3;");

        Image bgImage = new Image(getClass().getResourceAsStream("/assets/Admindash.jpg"));
        ImageView bgImageView = new ImageView(bgImage);
        bgImageView.setFitWidth(1020);
        bgImageView.setFitHeight(780);
        bgImageView.setPreserveRatio(false);
        bgImageView.setOpacity(0.0);

        FadeTransition bgFade = new FadeTransition(Duration.seconds(4), bgImageView);
        bgFade.setFromValue(0.0);
        bgFade.setToValue(0.5);
        bgFade.setCycleCount(1);
        bgFade.play();

        BorderPane root = new BorderPane();
        root.setCenter(centerContent);
        root.setRight(sidebar);
        root.setStyle("-fx-background-color: transparent;");

        StackPane stack = new StackPane(bgImageView, root);

        this.DashboardStage = primaryStage;
        DashboardScene = new Scene(stack, 1020, 720);
        DashboardStage.setTitle("Admin Dashboard");
        DashboardStage.setScene(DashboardScene);
        DashboardStage.setResizable(true);
        DashboardStage.getIcons().add(new Image(getClass().getResourceAsStream("assets\\newAppLogo.jpg")));
        DashboardStage.show();
    }

    private Button createSidebarButton(String text) {
        Button btn = new Button(text);
        btn.setFont(Font.font("Segoe UI", FontWeight.SEMI_BOLD, 14));
        btn.setPrefWidth(170);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: #457b9d; -fx-text-fill: white; -fx-background-radius: 8;");
        btn.setEffect(new DropShadow(3, Color.rgb(0, 0, 0, 0.25)));

        btn.setOnMouseEntered(e -> btn.setStyle(
                "-fx-background-color: #2c6e91; -fx-text-fill: white; -fx-background-radius: 8;"));
        btn.setOnMouseExited(e -> btn.setStyle(
                "-fx-background-color: #457b9d; -fx-text-fill: white; -fx-background-radius: 8;"));

        return btn;
    }

    private void intializeProfilepage() {
        BrandPage profile = new BrandPage();
        profile.setBrandStage(DashboardStage);
        BrandScene = new Scene(profile.createBrandPage(() -> handleback()), 1020, 720);
        profile.setBrandScene(BrandScene);
    }

    private void intializeUserPage() {
        UserPage user = new UserPage();
        user.setUserScene(DashboardScene);
        UserScene = new Scene(user.createUserPage(() -> handleback()), 1020, 720);
        user.setUserScene(UserScene);
    }

    private void intializeTrackverificationpage() {
        TrackVerification track = new TrackVerification();
        VBox trackPane = track.createTrackVerificationPage(() -> handleback());
        TrackVerificationScene = new Scene(trackPane, 1020, 720);
    }

    private void intializeSettingpage() {
        Aboutus about = new Aboutus();
        about.setSettingStage(DashboardStage);
        StackPane settingPane = about.createSettingPage(() -> handleback());
        SettingScene = new Scene(settingPane, 1020, 720);
        about.setSettingScene(SettingScene);
    }

    private void handleback() {
        DashboardStage.setScene(DashboardScene);
    }
}






