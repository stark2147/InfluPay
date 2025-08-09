package com.View;

import com.google.firebase.database.*;
import com.upnext.Model.Firebaseconfig;
import com.upnext.Model.Influencer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.Stage;

import java.awt.Desktop;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class TrackVerification {

    private final ObservableList<Influencer> influencerList = FXCollections.observableArrayList();

    public VBox createTrackVerificationPage(Runnable back) {
        Firebaseconfig.initialize();

        Label heading = new Label("Influencer Verification");
        heading.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        heading.setTextFill(Color.DARKBLUE);

        TableView<Influencer> table = new TableView<>(influencerList);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<Influencer, String> uidCol = new TableColumn<>("UID");
        uidCol.setCellValueFactory(new PropertyValueFactory<>("uid"));

        TableColumn<Influencer, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        TableColumn<Influencer, String> linkCol = new TableColumn<>("Instagram Link");
        linkCol.setCellValueFactory(new PropertyValueFactory<>("instagramLink"));

        linkCol.setCellFactory(column -> new TableCell<>() {
            private final Hyperlink hyperlink = new Hyperlink();

            {
                hyperlink.setTextFill(Color.DARKBLUE);
                hyperlink.setOnAction(e -> {
                    String url = hyperlink.getText();
                    if (url != null && !url.isEmpty()) {
                        new Thread(() -> {
                            try {
                                HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
                                conn.setRequestMethod("HEAD");  // Use HEAD for Instagram
                                conn.setInstanceFollowRedirects(true);
                                conn.setConnectTimeout(5000);
                                conn.setReadTimeout(5000);
                                conn.connect();

                                int responseCode = conn.getResponseCode();
                                if (responseCode >= 200 && responseCode < 400) {
                                    if (Desktop.isDesktopSupported()) {
                                        Desktop.getDesktop().browse(new URI(url));
                                    } else {
                                        showAlert("Unsupported", "Desktop not supported on this platform.");
                                    }
                                } else {
                                    showAlert("Invalid Link", "Instagram link not accessible (code: " + responseCode + ")");
                                }
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                showAlert("Error", "Could not verify or open the Instagram link.");
                            }
                        }).start();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null || item.isEmpty()) {
                    setGraphic(null);
                } else {
                    hyperlink.setText(item);
                    setGraphic(hyperlink);
                }
            }
        });

        TableColumn<Influencer, Boolean> verifiedCol = new TableColumn<>("Verified");
        verifiedCol.setCellValueFactory(new PropertyValueFactory<>("verified"));

        TableColumn<Influencer, Void> actionCol = new TableColumn<>("Action");
        actionCol.setCellFactory(col -> new TableCell<>() {
            private final Button approveBtn = new Button("Approve");
            private final Button rejectBtn = new Button("Reject");

            {
                approveBtn.setStyle("-fx-background-color: green; -fx-text-fill: white;");
                rejectBtn.setStyle("-fx-background-color: red; -fx-text-fill: white;");
                approveBtn.setOnAction(e -> updateStatus(getTableView().getItems().get(getIndex()), true));
                rejectBtn.setOnAction(e -> updateStatus(getTableView().getItems().get(getIndex()), false));
            }

            private void updateStatus(Influencer inf, boolean status) {
                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("influencer")
                        .child(inf.getUid())
                        .child("verified");
                ref.setValueAsync(status);
                inf.setVerified(status); 
                table.refresh();
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(5, approveBtn, rejectBtn);
                    hbox.setAlignment(Pos.CENTER);
                    setGraphic(hbox);
                }
            }
        });

        table.getColumns().addAll(uidCol, emailCol, linkCol, verifiedCol, actionCol);
        loadInfluencers();

        Button backBtn = new Button("Back");
        backBtn.setOnAction(e -> back.run());
        backBtn.setStyle("-fx-background-color: #1d3557; -fx-text-fill: white;");

        Button addInfluencerBtn = new Button("Add Influencer");
        addInfluencerBtn.setStyle("-fx-background-color: #457b9d; -fx-text-fill: white;");
        addInfluencerBtn.setOnAction(e -> showAddInfluencerDialog());

        VBox layout = new VBox(20, heading, addInfluencerBtn, table, backBtn);
        layout.setPadding(new Insets(20));
        layout.setAlignment(Pos.TOP_CENTER);
        layout.setStyle("-fx-background-color: #f4f4f4;");
        return layout;
    }

    private void loadInfluencers() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("influencer");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                influencerList.clear();
                for (DataSnapshot child : snapshot.getChildren()) {
                    Influencer inf = child.getValue(Influencer.class);
                    if (inf != null) influencerList.add(inf);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                System.err.println("Error fetching influencer data: " + error.getMessage());
            }
        });
    }

    private void saveInfluencer(Influencer influencer) {
        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("influencer")
                .child(influencer.getUid());
        ref.setValueAsync(influencer);
        influencerList.add(influencer); 
    }

    private void showAddInfluencerDialog() {
        TextField uidField = new TextField();
        uidField.setPromptText("UID");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        TextField linkField = new TextField();
        linkField.setPromptText("Instagram Link");

        CheckBox verifiedBox = new CheckBox("Verified");

        Button submitBtn = new Button("Submit");
        submitBtn.setStyle("-fx-background-color: #2a9d8f; -fx-text-fill: white;");
        submitBtn.setOnAction(e -> {
            String uid = uidField.getText().trim();
            String email = emailField.getText().trim();
            String link = linkField.getText().trim();
            boolean verified = verifiedBox.isSelected();

            if (uid.isEmpty() || email.isEmpty() || link.isEmpty()) {
                showAlert("Error", "Please fill all fields.");
                return;
            }

            Influencer inf = new Influencer(uid, email, link, verified);
            saveInfluencer(inf);

            Stage stage = (Stage) submitBtn.getScene().getWindow();
            stage.getIcons().add(new Image("assets\\Applogo.jpg"));
            stage.close();
        });

        VBox vbox = new VBox(10, uidField, emailField, linkField, verifiedBox, submitBtn);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.CENTER);

        Stage dialog = new Stage();
        dialog.setTitle("Add Influencer");
        dialog.setScene(new Scene(vbox, 300, 250));
        dialog.show();
    }

    private void showAlert(String title, String message) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setTitle(title);
            alert.setContentText(message);
            alert.showAndWait();
        });
    }
}





