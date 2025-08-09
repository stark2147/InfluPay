package com.View;

import javafx.animation.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Random;

public class Aboutapp extends javafx.application.Application {

    private String username;

    public Aboutapp() {
        this.username = "User";
    }

    public Aboutapp(String username) {
        this.username = username;
    }

    @Override
    public void start(Stage stage) {
        StackPane root = new StackPane();
        VBox contentBox = new VBox(25);
        contentBox.setPadding(new Insets(30));
        contentBox.setAlignment(Pos.TOP_CENTER);

        LinearGradient gradient1 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#43cea2")),
                new Stop(1, Color.web("#185a9d")));

        LinearGradient gradient2 = new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.web("#66a6ff")),
                new Stop(1, Color.web("#fddb92")));

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

        ImageView logo = new ImageView(new Image("assets\\newAppLogo.jpg"));
        logo.setFitWidth(100);
        logo.setPreserveRatio(true);
        logo.setEffect(new DropShadow(10, Color.GRAY));

        Label title = new Label("Influpay");
        title.setStyle("-fx-font-size: 36px; -fx-font-weight: bold; -fx-text-fill: white;");

        VBox card1 = createCard("🏪 Shop-Based Cashback",
                "Upload a bill from our partnered shops and receive instant cashback.");
        VBox card2 = createCard("📱 Social Media Cashback",
                "Post your shopping experience on Instagram or Facebook. Tag the shop or brand. "
                        + "Get cashback based on views and engagement.");
        VBox card3 = createCard("👥 Referral Cashback",
                "Invite your friends using your referral link. When they sign up and complete a payment, you receive cashback!");

        Button backBtn = new Button("← Back to Dashboard");
        backBtn.setStyle("-fx-background-color: white; -fx-text-fill: #00796b; -fx-font-size: 14px; -fx-padding: 10 20; -fx-background-radius: 8;");
        backBtn.setOnAction(e -> First.showDashboard(stage, username));

        contentBox.getChildren().addAll(logo, title, card1, card2, card3, backBtn);
        root.getChildren().add(contentBox);

        Scene scene = new Scene(root, 1020, 780);
        stage.setScene(scene);
        stage.setTitle("About Influpay");
         stage.setResizable(true); 
        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.show();
    }

    private static VBox createCard(String heading, String desc) {
        Label head = new Label(heading);
        head.setStyle("-fx-font-size: 18px; -fx-font-weight: bold; -fx-text-fill: #004d40;");
        Label content = new Label(desc);
        content.setStyle("-fx-font-size: 14px; -fx-text-fill: #004d40;");
        content.setWrapText(true);

        VBox box = new VBox(8, head, content);
        box.setPadding(new Insets(15));
        box.setAlignment(Pos.TOP_LEFT);
        box.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-radius: 10;");
        box.setEffect(new DropShadow(4, Color.LIGHTGRAY));
        return box;
    }
}
