package com.View;

import com.google.firebase.database.*;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Map;

public class UserPage {

    Stage UserStage;
    Scene UserScene;

    public void setUserStage(Stage userStage) {
        UserStage = userStage;
    }

    public void setUserScene(Scene userScene) {
        UserScene = userScene;
    }

    private final ObservableList<User> users = FXCollections.observableArrayList();

    public VBox createUserPage(Runnable back) {
        Label title = new Label("User Dashboard");
        title.setFont(Font.font("Arial", FontWeight.BOLD, 26));
        title.setTextFill(Color.WHITE);
        HBox titleBox = new HBox(title);
        titleBox.setPadding(new Insets(20));
        titleBox.setStyle("-fx-background-color: #144d4d;");
        titleBox.setAlignment(Pos.CENTER_LEFT);

        // TableView
        TableView<User> table = new TableView<>();
        table.setItems(users);

        TableColumn<User, String> userIdCol = new TableColumn<>("User ID");
        userIdCol.setCellValueFactory(new PropertyValueFactory<>("userId"));

        TableColumn<User, String> emailCol = new TableColumn<>("E-mail");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<User, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<User, String> phoneCol = new TableColumn<>("Phone");
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));

        TableColumn<User, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));

        table.getColumns().addAll(userIdCol, emailCol, nameCol, phoneCol, typeCol);
        table.setPrefHeight(300);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Back button
        Button backbtn = new Button("Back");
        backbtn.setStyle("-fx-background-color: #144d4d; -fx-text-fill: white; -fx-font-weight: bold;");
        backbtn.setOnAction(event -> back.run());
        backbtn.setOnMouseEntered(e -> backbtn.setOpacity(0.85));
        backbtn.setOnMouseExited(e -> backbtn.setOpacity(1.0));

        VBox formBox = new VBox(10,
                new Label("Registered Users"),
                table,
                backbtn
        );
        formBox.setPadding(new Insets(20));
        formBox.setAlignment(Pos.CENTER_LEFT);

        VBox mainLayout = new VBox(titleBox, formBox);
        mainLayout.setStyle("-fx-background-color: #d9e6e6;");

        // === Animations ===
        FadeTransition fadeInMain = new FadeTransition(Duration.seconds(1.2), mainLayout);
        fadeInMain.setFromValue(0);
        fadeInMain.setToValue(1);
        fadeInMain.play();

        TranslateTransition slide = new TranslateTransition(Duration.seconds(1), table);
        slide.setFromY(50);
        slide.setToY(0);
        slide.play();

        FadeTransition fade = new FadeTransition(Duration.seconds(1), table);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        // Load users from Firebase
        loadUserDataFromFirebase();

        return mainLayout;
    }

    private void loadUserDataFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Map<String, Object> userData = (Map<String, Object>) child.getValue();

                    String uid = child.getKey();
                    String email = getValue(userData, "email");
                    String name = getValue(userData, "name");
                    String phone = getValue(userData, "phone");
                    String type = getValue(userData, "type");

                    users.add(new User(uid, email, name, phone, type));
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Failed to fetch users: " + error.getMessage());
            }
        });
    }

    private String getValue(Map<String, Object> data, String key) {
        Object val = data.get(key);
        return val != null ? val.toString() : "N/A";
    }

    public static class User {
        private final SimpleStringProperty userId;
        private final SimpleStringProperty email;
        private final SimpleStringProperty name;
        private final SimpleStringProperty phone;
        private final SimpleStringProperty type;

        public User(String userId, String email, String name, String phone, String type) {
            this.userId = new SimpleStringProperty(userId);
            this.email = new SimpleStringProperty(email);
            this.name = new SimpleStringProperty(name);
            this.phone = new SimpleStringProperty(phone);
            this.type = new SimpleStringProperty(type);
        }

        public String getUserId() { return userId.get(); }
        public String getEmail() { return email.get(); }
        public String getName() { return name.get(); }
        public String getPhone() { return phone.get(); }
        public String getType() { return type.get(); }
    }
}



