
 package com.upnext.Model;
import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class Shopdetailpage {

    public StackPane getShopDetailsPane(String name, String address, String info, String imageFile, Runnable onBack) {
        
       ImageView background = new ImageView(new Image(getClass().getResourceAsStream("/assets/shopimage.jpg")));

        background.setFitWidth(1020);
        background.setFitHeight(720);
        background.setPreserveRatio(false);
        background.setEffect(new BoxBlur(15, 15, 3)); 

        VBox infoBox = new VBox(15);
        infoBox.setPadding(new Insets(30));
        infoBox.setAlignment(Pos.CENTER);
        infoBox.setMaxWidth(450);
        infoBox.setStyle("-fx-background-color: rgba(255, 255, 255, 0.25); " +
                         "-fx-background-radius: 25; " +
                         "-fx-border-radius: 25; " +
                         "-fx-border-color: white; " +
                         "-fx-border-width: 2px;");

        Label title = new Label("📍 Shop Details");
        title.setStyle("-fx-font-size: 32px; -fx-font-weight: bold; -fx-text-fill: purple;");

        Label nameLabel = new Label("Name: " + name);
        Label addressLabel = new Label("Address: " + address);
        Label infoLabel = new Label("Info: " + info);

        for (Label lbl : new Label[]{nameLabel, addressLabel, infoLabel}) {
            lbl.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
        }

        ImageView shopImage;
        try {
            shopImage = new ImageView(new Image(getClass().getResourceAsStream("/assets/" + imageFile)));
        } catch (Exception e) {
            shopImage = new ImageView();
        }
        shopImage.setFitWidth(250);
        shopImage.setFitHeight(250);
        shopImage.setPreserveRatio(true);

    

        infoBox.getChildren().addAll(title, shopImage, nameLabel, addressLabel, infoLabel);

        
        FadeTransition fade = new FadeTransition(Duration.seconds(1.0), infoBox);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        
        StackPane layout = new StackPane(background, infoBox);
        StackPane.setAlignment(infoBox, Pos.CENTER);
        layout.setPadding(new Insets(30));

        return layout;
    }
}