package com.View;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.upnext.Model.Firebaseconfig;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashSet;
import java.util.function.Consumer;

public class Uploadpurchase {

    private final HashSet<String> firebaseBrands = new HashSet<>();

    public Scene createScene(Stage stage, String username, Runnable goBack) {
        loadShopsFromFirestore(() -> System.out.println("✅ Shop names loaded."));

        Text title = new Text("Upload Your Purchase Receipt");
        title.setFont(Font.font("Arial", 24));
        title.setFill(Color.WHITE);

        Label brandLabel = new Label("\uD83D\uDECD️ Shop Name");
        TextField brandField = new TextField();

        Label amountLabel = new Label("\uD83D\uDCB8 Amount Paid");
        TextField amountField = new TextField();

        Label dateLabel = new Label("\uD83D\uDCC5 Date of Purchase");
        DatePicker datePicker = new DatePicker();

        Button uploadBtn = new Button("\uD83D\uDCC4 Upload Bill Image");
        uploadBtn.setStyle("-fx-background-color: #ff8c00; -fx-text-fill: white;");
        uploadBtn.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Choose Receipt Image");
            chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
            File file = chooser.showOpenDialog(stage);
            if (file != null) uploadImageToFirebase(file);
        });

        Button submitBtn = new Button("\u2705 Submit");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");

        Button backBtn = new Button("⬅ Back");
        backBtn.setStyle("-fx-background-color: #3498db; -fx-text-fill: white;");
        backBtn.setOnAction(e -> goBack.run());

        VBox form = new VBox(15, brandLabel, brandField, amountLabel, amountField, dateLabel, datePicker, uploadBtn, submitBtn, backBtn);
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

        submitBtn.setOnAction(e -> {
            String brand = brandField.getText().trim().toLowerCase();
            String amountStr = amountField.getText().trim();

            if (brand.isEmpty() || amountStr.isEmpty() || datePicker.getValue() == null) {
                showAlert(Alert.AlertType.ERROR, "Missing Fields", "Please fill all details.");
                return;
            }

            if (!firebaseBrands.contains(brand)) {
                showAlert(Alert.AlertType.ERROR, "Shop Not Registered", "This shop is not registered. Cashback not allowed.");
                return;
            }

            double amount;
            try {
                amount = Double.parseDouble(amountStr);
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Invalid Amount", "Enter a valid number.");
                return;
            }

            String dateStr = datePicker.getValue().toString();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Influencer?");
            alert.setHeaderText("Are you an influencer?");
            alert.setContentText("Do you post content on Instagram about this purchase?");
            ButtonType yes = new ButtonType("Yes");
            ButtonType no = new ButtonType("No");
            alert.getButtonTypes().setAll(yes, no);

            alert.showAndWait().ifPresent(type -> {
                if (type == yes) {
                    Socialmedia sm = new Socialmedia(username, amount, brand, dateStr);
                    Scene smScene = sm.createScene(() -> stage.setScene(createScene(stage, username, goBack)));
                    stage.setScene(smScene);
                } else {
                    double cashback = amount * 0.05;
                    showAlert(Alert.AlertType.INFORMATION, "Cashback", "As a regular user, you get ₹" + cashback + " cashback.");
                    saveCashbackToFirebase(username, amount, cashback);
                    showCelebration(cashback);
                }
            });

        });

        return new Scene(wrapper, 1020, 780);
    }

    private void loadShopsFromFirestore(Runnable onComplete) {
        firebaseBrands.clear();
        Firebaseconfig.initialize();
        Firestore db = Firebaseconfig.getFirestore();

        if (db == null) {
            System.out.println("Firestore not initialized.");
            return;
        }

        ApiFuture<QuerySnapshot> future = db.collection("shops").get();
        try {
            QuerySnapshot snapshot = future.get();
            for (QueryDocumentSnapshot doc : snapshot) {
                String name = doc.getString("name");
                if (name != null) {
                    firebaseBrands.add(name.trim().toLowerCase());
                }
            }
            System.out.println("✅ Shops loaded from Firestore: " + firebaseBrands);
        } catch (Exception e) {
            System.out.println("Error loading shops: " + e.getMessage());
        }

        onComplete.run();
    }

    private void saveCashbackToFirebase(String username, double amount, double cashback) {
        try {
            String firebaseURL = "https://myfxproject-9c5bb-default-rtdb.firebaseio.com/cashbacks.json";
            String jsonData = "{" +
                    "\"username\":\"" + username + "\"," +
                    "\"amountPaid\":" + amount + "," +
                    "\"cashback\":" + cashback + "," +
                    "\"userType\":\"regular\"," +
                    "\"timestamp\":\"" + System.currentTimeMillis() + "\"" +
                    "}";

            URL url = new URL(firebaseURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonData.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            conn.getResponseCode();
            conn.disconnect();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void uploadImageToFirebase(File file) {
        
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showCelebration(double cashback) {
        Stage popup = new Stage();
        popup.setTitle("\uD83C\uDF38 Cashback Credited!");

        Label congrats = new Label("₹" + cashback + " Cashback Credited!");
        congrats.setFont(Font.font("Arial", 26));
        congrats.setTextFill(Color.WHITE);

        FlowPane flowers = new FlowPane();
        flowers.setHgap(10);
        flowers.setVgap(10);
        flowers.setAlignment(Pos.CENTER);
        flowers.setPrefWrapLength(350);

        for (int i = 0; i < 20; i++) {
            Label flower = new Label("\uD83C\uDF38");
            flower.setStyle("-fx-font-size: 28;");
            flowers.getChildren().add(flower);
        }

        VBox root = new VBox(15, flowers, congrats);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(20));
        root.setStyle("-fx-background-color: linear-gradient(to right, #fbc2eb, #a6c1ee);");

        Scene scene = new Scene(root, 400, 400);
        popup.setScene(scene);
        popup.show();

        new Thread(() -> {
            try {
                Thread.sleep(4000);
                javafx.application.Platform.runLater(popup::close);
            } catch (InterruptedException ignored) {}
        }).start();
    }
}





