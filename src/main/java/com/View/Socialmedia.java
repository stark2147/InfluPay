package com.View;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.upnext.Model.Influencer;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.function.Consumer;

public class Socialmedia {

    private String username;
    private double amount;
    private String brand;
    private String date;

    public Socialmedia(String username, double amount, String brand, String date) {
        this.username = username;
        this.amount = amount;
        this.brand = brand;
        this.date = date;
    }

    public Scene createScene(Runnable goBack) {
        Text title = new Text("Social Media Cashback");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);

        Label followerLabel = new Label("👤 Follower Count");
        TextField followerField = new TextField();
        Label viewsLabel = new Label("👁️ Reel Views");
        TextField viewsField = new TextField();
        Label linkLabel = new Label("🔗 Reel/Post Link");
        TextField linkField = new TextField();

        Button submitBtn = new Button("Submit Cashback");
        submitBtn.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            checkCashbackStatus(uniqueKey(), alreadyClaimed -> {
                if (alreadyClaimed) {
                    showAlert(Alert.AlertType.WARNING, "Already Claimed", "You have already received cashback for this entry.");
                    return;
                }

                try {
                    int followers = Integer.parseInt(followerField.getText().trim());
                    int views = Integer.parseInt(viewsField.getText().trim());
                    String link = linkField.getText().trim();

                    if (!(link.startsWith("https://instagram.com") || link.startsWith("https://www.instagram.com"))) {
                        showAlert(Alert.AlertType.ERROR, "Invalid Link", "Please enter a valid Instagram link.");
                        return;
                    }

                    double cashback;
                    boolean isInfluencer = followers >= 10000;

                    if (isInfluencer) {
                        cashback = Math.min(views / 100.0, amount * 0.3);
                    } else {
                        cashback = amount * 0.05;
                    }

                    saveToFirebase(followers, views, link, cashback, isInfluencer);
                    updateCashbackStatus(uniqueKey());
                    showAlert(Alert.AlertType.INFORMATION, "Cashback Submitted", "Cashback: ₹" + cashback);
                    showCelebration(cashback);

                } catch (NumberFormatException ex) {
                    showAlert(Alert.AlertType.ERROR, "Invalid Input", "Please enter valid numbers for followers and views.");
                }
            });
        });

        Button backBtn = new Button("⬅ Back");
        backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        backBtn.setOnAction(e -> goBack.run());

        VBox form = new VBox(15, followerLabel, followerField, viewsLabel, viewsField, linkLabel, linkField, submitBtn, backBtn);
        form.setAlignment(Pos.CENTER_LEFT);
        form.setPadding(new Insets(30));
        form.setMaxWidth(400);
        form.setStyle("-fx-background-color: white; -fx-background-radius: 20;");

        VBox wrapper = new VBox(30, title, form);
        wrapper.setAlignment(Pos.CENTER);
        wrapper.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#667eea")),
                        new Stop(1, Color.web("#764ba2"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        return new Scene(wrapper, 1020, 780);
    }

    private String uniqueKey() {
        return username + "_" + brand + "_" + date.replaceAll("[^\\d]", "");
    }

    private void saveToFirebase(int followers, int views, String link, double cashback, boolean isInfluencer) {
        try {
            String firebaseUrl = "https://myfxproject-9c5bb-default-rtdb.firebaseio.com/cashbacks/" + uniqueKey() + ".json";
            String data = String.format(
                    "{\"username\":\"%s\",\"followers\":%d,\"views\":%d,\"link\":\"%s\",\"cashback\":%.2f,\"influencer\":%b,\"brand\":\"%s\",\"date\":\"%s\"}",
                    username, followers, views, link, cashback, isInfluencer, brand, date);

            HttpURLConnection conn = (HttpURLConnection) new URL(firebaseUrl).openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(data.getBytes());
            }

            conn.getResponseCode();
            conn.disconnect();

            if (isInfluencer) {
                saveInfluencerInfo(link);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveInfluencerInfo(String instagramLink) {
        try {
            String uid = username;
            String email = username + "@gmail.com";

            Influencer influencer = new Influencer(uid, email, instagramLink, false);

            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("influencer")
                    .child(uid);

            ref.setValueAsync(influencer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkCashbackStatus(String key, Consumer<Boolean> callback) {
        new Thread(() -> {
            try {
                URL url = new URL("https://myfxproject-9c5bb-default-rtdb.firebaseio.com/cashbacks/" + key + "/cashbackReceived.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String result = reader.readLine();
                boolean alreadyReceived = "true".equals(result);

                javafx.application.Platform.runLater(() -> callback.accept(alreadyReceived));
            } catch (Exception e) {
                e.printStackTrace();
                javafx.application.Platform.runLater(() -> callback.accept(false));
            }
        }).start();
    }

    private void updateCashbackStatus(String key) {
        new Thread(() -> {
            try {
                URL url = new URL("https://myfxproject-9c5bb-default-rtdb.firebaseio.com/cashbacks/" + key + "/cashbackReceived.json");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("PUT");
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json");

                try (OutputStream os = conn.getOutputStream()) {
                    os.write("true".getBytes());
                }

                conn.getResponseCode();
                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void showAlert(Alert.AlertType type, String title, String msg) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void showCelebration(double cashback) {
        Stage popup = new Stage();
        popup.setTitle("🌸 Cashback Credited!");

        Label message = new Label("₹" + cashback + " Cashback Successfully Credited!");
        message.setFont(Font.font("Arial", 22));
        message.setTextFill(Color.WHITE);

        Pane petalPane = new Pane();
        petalPane.setPrefSize(400, 300);

        VBox content = new VBox(20, petalPane, message);
        content.setAlignment(Pos.CENTER);
        content.setPadding(new Insets(20));
        content.setStyle("-fx-background-color: linear-gradient(to right, #ffafbd, #ffc3a0); -fx-background-radius: 20;");

        Scene scene = new Scene(content, 1020, 720);
        popup.setScene(scene);
        popup.setResizable(false);
        popup.show();

        for (int i = 0; i < 25; i++) {
            Circle petal = new Circle(6 + Math.random() * 4);
            petal.setFill(Color.color(Math.random(), Math.random() * 0.5 + 0.5, Math.random()));
            petal.setOpacity(0.8);
            petal.setLayoutX(20 + Math.random() * 360);
            petal.setLayoutY(-10);

            petalPane.getChildren().add(petal);

            TranslateTransition fall = new TranslateTransition(Duration.seconds(2 + Math.random()), petal);
            fall.setFromY(-10);
            fall.setToY(300 + Math.random() * 30);
            fall.setCycleCount(1);
            fall.setDelay(Duration.seconds(Math.random()));
            fall.play();
        }

        new Thread(() -> {
            try {
                Thread.sleep(4000);
                javafx.application.Platform.runLater(popup::close);
            } catch (InterruptedException ignored) {}
        }).start();
    }
}




