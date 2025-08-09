

package com.View;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.upnext.Model.Firebaseconfig;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.List;
import java.util.Optional;

public class BrandPage {
    Stage BrandStage;
    Scene BrandScene;

    public void setBrandStage(Stage brandStage) {
        BrandStage = brandStage;
    }

    public void setBrandScene(Scene brandScene) {
        BrandScene = brandScene;
    }

    private final ObservableList<Shop> shops = FXCollections.observableArrayList();
    private TableView<Shop> table;

    public StackPane createBrandPage(Runnable back) {
        Label title = new Label("Shop Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);
        HBox titleBox = new HBox(title);
        titleBox.setPadding(new Insets(20));
        titleBox.setStyle("-fx-background-color: #144d4d;");
        titleBox.setAlignment(Pos.CENTER_LEFT);

        table = new TableView<>();
        table.setItems(shops);
        table.setPlaceholder(new Label("No shops registered"));

        TableColumn<Shop, String> shopNameCol = new TableColumn<>("Shop Name");
        shopNameCol.setCellValueFactory(cellData -> cellData.getValue().shopNameProperty());

        TableColumn<Shop, String> addressCol = new TableColumn<>("Address");
        addressCol.setCellValueFactory(cellData -> cellData.getValue().addressProperty());

        TableColumn<Shop, String> infoCol = new TableColumn<>("Info");
        infoCol.setCellValueFactory(cellData -> cellData.getValue().infoProperty());

        table.getColumns().addAll(shopNameCol, addressCol, infoCol);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        loadShopsFromFirebase();

        Button backbtn = new Button("Back");
        backbtn.setStyle("-fx-background-color: #144d4d; -fx-text-fill: white; -fx-font-weight: bold;");
        backbtn.setOnAction(event -> back.run());

        Button deleteButton = new Button("🗑 Delete Selected Shop");
        deleteButton.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteButton.setOnAction(e -> deleteSelectedShop());

        backbtn.setOnMouseEntered(e -> backbtn.setOpacity(0.8));
        backbtn.setOnMouseExited(e -> backbtn.setOpacity(1.0));

        VBox formBox = new VBox(10,
                new Label("Registered Shops"),
                table,
                deleteButton,
                backbtn
        );
        formBox.setPadding(new Insets(20));
        formBox.setAlignment(Pos.CENTER_LEFT);

        VBox mainLayout = new VBox(titleBox, formBox);
        mainLayout.setStyle("-fx-background-color: transparent;");

        ImageView backgroundImage = new ImageView(new Image("assets\\brandpage.jpg")); 
        backgroundImage.setFitWidth(1200);
        backgroundImage.setFitHeight(800);
        backgroundImage.setPreserveRatio(false);
        backgroundImage.setOpacity(0.5);

        FadeTransition bgFade = new FadeTransition(Duration.seconds(3), backgroundImage);
        bgFade.setFromValue(0.0);
        bgFade.setToValue(0.2);
        bgFade.play();

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1.2), mainLayout);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();

        TranslateTransition slideTable = new TranslateTransition(Duration.seconds(1), table);
        slideTable.setFromY(50);
        slideTable.setToY(0);

        FadeTransition fadeTable = new FadeTransition(Duration.seconds(1), table);
        fadeTable.setFromValue(0);
        fadeTable.setToValue(1);

        slideTable.play();
        fadeTable.play();

        StackPane root = new StackPane(backgroundImage, mainLayout);
        return root;
    }

    private void loadShopsFromFirebase() {
        try {
            Firebaseconfig.initialize();
            Firestore db = Firebaseconfig.getFirestore();

            ApiFuture<QuerySnapshot> future = db.collection("shops").get();
            List<QueryDocumentSnapshot> documents = future.get().getDocuments();

            shops.clear();
            for (QueryDocumentSnapshot doc : documents) {
                String name = doc.getString("name");  // ✅ Updated field name
                String address = doc.getString("address");
                String info = doc.getString("info");

                // Debug log
                System.out.println("Loaded shop: " + name + " | Address: " + address + " | Info: " + info);

                shops.add(new Shop(name, address, info));
            }

        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to load shop data from Firebase.");
        }
    }

    private void deleteSelectedShop() {
        Shop selectedShop = table.getSelectionModel().getSelectedItem();
        if (selectedShop == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a shop to delete.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Deletion");
        confirm.setHeaderText("Delete this shop?");
        confirm.setContentText("Are you sure you want to delete " + selectedShop.getShopName() + "?");

        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            Task<Void> deleteTask = new Task<>() {
                @Override
                protected Void call() throws Exception {
                    Firebaseconfig.initialize();
                    Firestore db = Firebaseconfig.getFirestore();

                    ApiFuture<QuerySnapshot> future = db.collection("shops")
                            .whereEqualTo("name", selectedShop.getShopName())  // ✅ Also updated here
                            .get();
                    List<QueryDocumentSnapshot> documents = future.get().getDocuments();
                    for (QueryDocumentSnapshot doc : documents) {
                        db.collection("shops").document(doc.getId()).delete();
                    }

                    return null;
                }
            };

            deleteTask.setOnSucceeded(e -> {
                shops.remove(selectedShop);
                showAlert(Alert.AlertType.INFORMATION, "Deleted", "Shop deleted successfully.");
            });

            deleteTask.setOnFailed(e -> {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete shop from Firebase.");
                deleteTask.getException().printStackTrace();
            });

            new Thread(deleteTask).start();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class Shop {
        private final SimpleStringProperty shopName;
        private final SimpleStringProperty address;
        private final SimpleStringProperty info;

        public Shop(String shopName, String address, String info) {
            this.shopName = new SimpleStringProperty(shopName);
            this.address = new SimpleStringProperty(address);
            this.info = new SimpleStringProperty(info);
        }

        public String getShopName() {
            return shopName.get();
        }

        public String getAddress() {
            return address.get();
        }

        public String getInfo() {
            return info.get();
        }

        public SimpleStringProperty shopNameProperty() {
            return shopName;
        }

        public SimpleStringProperty addressProperty() {
            return address;
        }

        public SimpleStringProperty infoProperty() {
            return info;
        }
    }
}

