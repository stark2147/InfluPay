package com.View;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.upnext.Model.Firebaseconfig;
import com.upnext.Model.Shopdetailpage;
import javafx.animation.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.List;

public class First extends Application {
    private VBox shopContainer;
    private String username;
    private ScrollPane scrollPane;
    private VBox layout;

    public First(String username) {
        this.username = username;
    }

    public First() {
        this.username = "User";
    }

    @Override
    public void start(Stage stage) {
        Text tx = new Text("Influpay");
        tx.setFont(Font.font("Arial", FontWeight.BOLD, 40));
        tx.setFill(Color.web("#0A1E4A"));

        Text tx1 = new Text("Welcome, " + username);
        tx1.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        tx1.setFill(Color.DARKBLUE);

        FadeTransition fade = new FadeTransition(Duration.seconds(1.5), tx1);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        VBox vb = new VBox(20, tx, tx1);
        vb.setAlignment(Pos.TOP_CENTER);

        Text tx2 = new Text("5 Steps to Earn Cashback");
        tx2.setFont(Font.font("Arial", FontWeight.BOLD, 30));

        HBox stepsBox = new HBox(10);
        stepsBox.setAlignment(Pos.CENTER_LEFT);
        stepsBox.setPadding(new Insets(10));
        stepsBox.setMaxHeight(250);

        ScrollPane stepsScroll = new ScrollPane(stepsBox);
        stepsScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        stepsScroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        stepsScroll.setFitToHeight(true);

        String[] stepImages = {
                "/assets/ig1.jpg",
                "/assets/i2.jpg",
                "/assets/ig3.jpg",
                "/assets/i4.jpg",
                "/assets/i5.jpg"
        };

        String[] stepTitles = {
                "1:Shop from partner shop",
                "2:Upload bill",
                "3:Online content post",
                "4:Earn cashback from views",
                "5:Cashback in wallet"
        };

        for (int i = 0; i < stepImages.length; i++) {
            VBox imgBox = new VBox(5);
            imgBox.setAlignment(Pos.CENTER);

            InputStream is = getClass().getResourceAsStream(stepImages[i]);
            if (is == null) {
                System.out.println("Step image missing: " + stepImages[i]);
                continue;
            }

            ImageView img = new ImageView(new Image(is));
            img.setFitHeight(170);
            img.setFitWidth(170);

            Text lbl = new Text(stepTitles[i]);
            lbl.setFont(Font.font("Arial", 12));

            imgBox.getChildren().addAll(img, lbl);
            stepsBox.getChildren().add(imgBox);

            TranslateTransition slide = new TranslateTransition(Duration.seconds(0.8), imgBox);
            slide.setFromX(-300);
            slide.setToX(0);
            slide.setDelay(Duration.seconds(0.2 * i));
            slide.play();
        }

        shopContainer = new VBox(15);
        shopContainer.setAlignment(Pos.CENTER);
        shopContainer.setPadding(new Insets(10));
        loadShopsFromFirestore();

        Button earnBtn = new Button("Earn Cashback");
        earnBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 10;");
        earnBtn.setPrefHeight(50);
        earnBtn.setPrefWidth(250);
        earnBtn.setFont(Font.font("Arial", FontWeight.BOLD, 20));

        HBox hb4 = new HBox(earnBtn);
        hb4.setAlignment(Pos.CENTER);
        hb4.setPadding(new Insets(20, 0, 20, 0));

        HBox navBar = new HBox(100);
        navBar.setAlignment(Pos.CENTER);
        navBar.setPadding(new Insets(10));
        navBar.setStyle("-fx-background-color: #F5F5F5;");

        String[][] icons = {
                {"\uD83C\uDFE0", "Home"},
              //  {"\uD83D\uDCF7", "Scanner"},
                {"\uD83C\uDF81", "Refer"},
                {"\uD83D\uDC64", "Profile"},
                {"\u2139", "Info"},
                {"\uD83D\uDEAA", "Logout"}
        };

        for (String[] iconInfo : icons) {
            VBox navItem = new VBox(5);
            navItem.setAlignment(Pos.CENTER);
            navItem.setPadding(new Insets(5, 10, 5, 10));

            Label icon = new Label(iconInfo[0]);
            icon.setStyle("-fx-font-size: 30px;");
            Label label = new Label(iconInfo[1]);

            navItem.getChildren().addAll(icon, label);

            navItem.setOnMouseEntered(e -> navItem.setStyle("-fx-background-color: #D6EAF8; -fx-background-radius: 10;"));
            navItem.setOnMouseExited(e -> navItem.setStyle("-fx-background-color: transparent;"));

            switch (iconInfo[1]) {
                case "Profile":
                    icon.setOnMouseClicked(e -> {
                        try {
                            new Profilepage().start(new Stage());
                            ((Stage) icon.getScene().getWindow()).close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;
                case "Refer":
                    icon.setOnMouseClicked(e -> {
                        try {
                            new Referralpage1().start(new Stage());
                            ((Stage) icon.getScene().getWindow()).close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;
                case "Logout":
                    icon.setOnMouseClicked(e -> {
                        try {
                            new Home().start(new Stage());
                            ((Stage) icon.getScene().getWindow()).close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;
                case "Info":
                    icon.setOnMouseClicked(e -> {
                        try {
                            new Aboutapp(username).start(new Stage());
                            ((Stage) icon.getScene().getWindow()).close();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    });
                    break;

                
    
            }

            navBar.getChildren().add(navItem);
        }

        layout = new VBox(30, vb, tx2, stepsScroll, shopContainer, hb4, navBar);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: linear-gradient(to bottom right, #E3F2FD, #BBDEFB);");

        scrollPane = new ScrollPane(layout);
        scrollPane.setFitToWidth(true);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color:transparent;");

        Platform.runLater(() -> {
            new AnimationTimer() {
                @Override
                public void handle(long now) {
                    scrollPane.setVvalue(0);
                    stop();
                }
            }.start();
        });

        Scene scene = new Scene(scrollPane, 1020, 780);

        earnBtn.setOnAction(e -> {
            Uploadpurchase uploadPage = new Uploadpurchase();
            Scene uploadScene = uploadPage.createScene(stage, username, () -> stage.setScene(scene));
            stage.setScene(uploadScene);
        });

        stage.setScene(scene);
        stage.setTitle("Influpay");
        stage.setResizable(true);
        stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.show();
    }

    private void loadShopsFromFirestore() {
        new Thread(() -> {
            Firestore db = Firebaseconfig.getFirestore();
            try {
                ApiFuture<QuerySnapshot> future = db.collection("shops").get();
                List<QueryDocumentSnapshot> documents = future.get().getDocuments();
                Platform.runLater(() -> {
                    HBox currentRow = new HBox(20);
                    currentRow.setAlignment(Pos.CENTER);
                    int count = 0;
                    for (QueryDocumentSnapshot doc : documents) {
                        String name = doc.getString("name");
                        String imageFile = doc.getString("image");
                        String address = doc.getString("address");
                        String info = doc.getString("info");

                        if (imageFile == null || imageFile.isEmpty()) continue;

                        try {
                            InputStream is = getClass().getResourceAsStream("/assets/" + imageFile);
                            if (is == null) {
                                System.out.println("Image missing for shop: " + name + " (" + imageFile + ")");
                                continue;
                            }

                            ImageView imageView = new ImageView(new Image(is));
                            imageView.setFitHeight(150);
                            imageView.setFitWidth(150);

                            Label nameLabel = new Label(name);
                            VBox shopBox = new VBox(5, imageView, nameLabel);
                            shopBox.setAlignment(Pos.CENTER);
                            shopBox.setOnMouseClicked(event -> showShopDetails(name, address, info, imageFile));

                            currentRow.getChildren().add(shopBox);
                            count++;
                            if (count % 4 == 0) {
                                shopContainer.getChildren().add(currentRow);
                                currentRow = new HBox(20);
                                currentRow.setAlignment(Pos.CENTER);
                            }
                        } catch (Exception imgErr) {
                            System.out.println("Image loading error for " + name + ": " + imgErr.getMessage());
                        }
                    }

                    if (!currentRow.getChildren().isEmpty()) {
                        shopContainer.getChildren().add(currentRow);
                    }

                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showShopDetails(String name, String address, String info, String imageFile) {
        Shopdetailpage detailPage = new Shopdetailpage();
        StackPane detailPane = detailPage.getShopDetailsPane(name, address, info, imageFile, null);

        Button backButton = new Button("← Back");
        backButton.setOnAction(e -> scrollPane.setContent(layout));

        VBox wrapper = new VBox(10, backButton, detailPane);
        wrapper.setAlignment(Pos.TOP_LEFT);
        wrapper.setPadding(new Insets(10));

        scrollPane.setContent(wrapper);
    }

    public static void showDashboard(Stage stage, String username) {
        First dashboard = new First(username);
        try {
            dashboard.start(stage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
