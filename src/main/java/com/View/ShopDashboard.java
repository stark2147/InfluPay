
package com.View;

import com.upnext.Model.Firebaseconfig;
import com.upnext.Model.Session;
import javafx.animation.FadeTransition;
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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ShopDashboard extends Application {

    private ImageView imageView;
    private VBox vb;
    private Stage primaryStage;
    private File selectedImage;
    private TextField nameField, addressField;
    private TextArea infoArea;
    private String selectedImageFileName = null;

    @Override
    public void start(Stage stage) {
        this.primaryStage = stage;

        Button backBtn = new Button("←");
        backBtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        backBtn.setStyle("-fx-background-color: #cab3b3ff; -fx-background-radius: 20;");
        backBtn.setOnAction(e -> {
            try {
                new Home().start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        Text titleText = new Text("Welcome Shop Owner");
        titleText.setFont(Font.font("Segoe UI", FontWeight.BOLD, 40));
        titleText.setFill(Color.web("#1d3557"));

        Button addBtn = new Button("➕");
        addBtn.setFont(Font.font("Arial", FontWeight.EXTRA_BOLD, 24));
        addBtn.setStyle("-fx-background-color: #076f0aff; -fx-text-fill: white; -fx-font-size: 22px; -fx-background-radius: 40px;");

        BorderPane topBar = new BorderPane();
        topBar.setLeft(backBtn);
        topBar.setCenter(titleText);
        topBar.setRight(addBtn);
        BorderPane.setAlignment(backBtn, Pos.CENTER_LEFT);
        BorderPane.setAlignment(addBtn, Pos.CENTER_RIGHT);
        BorderPane.setAlignment(titleText, Pos.CENTER);
        BorderPane.setMargin(backBtn, new Insets(10));
        BorderPane.setMargin(addBtn, new Insets(10));
        BorderPane.setMargin(titleText, new Insets(10, 0, 10, 0));

        vb = new VBox(15);
        vb.setPadding(new Insets(20));
        vb.setAlignment(Pos.TOP_CENTER);
        vb.setStyle("-fx-background-color: rgba(255,255,255,0.85); -fx-background-radius: 15;");

        addBtn.setOnAction(e -> showShopForm());

        VBox contentBox = new VBox(topBar, vb);
        contentBox.setSpacing(20);
        contentBox.setPadding(new Insets(20));

        Image backgroundImage = new Image(getClass().getResource("/assets/shopimage.jpg").toExternalForm());
        ImageView backgroundView = new ImageView(backgroundImage);
        backgroundView.setFitWidth(1020);
        backgroundView.setFitHeight(780);
        backgroundView.setPreserveRatio(false);
        backgroundView.setOpacity(0.7);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), backgroundView);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(0.3);
        fadeIn.play();

        StackPane root = new StackPane(backgroundView, contentBox);
        root.setPrefSize(800, 800);

        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Shop Dashboard");
         stage.setResizable(true);
       stage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        stage.show();

        showDefaultDashboard();
    }

    private void showShopForm() {
        vb.getChildren().clear();

        nameField = new TextField();
        nameField.setPromptText("Enter shop name");
        addressField = new TextField();
        addressField.setPromptText("Enter shop address");
        infoArea = new TextArea();
        infoArea.setPromptText("Enter shop info");

        imageView = new ImageView();
        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setVisible(false);

        Button chooseImage = new Button("Choose Image");
        chooseImage.setOnAction(e -> chooseImage(primaryStage));

        Button saveBtn = new Button("Save Shop Info");
        saveBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white;");
        saveBtn.setOnAction(e -> saveShopInfo());

        VBox form = new VBox(10,
                new Label("Shop Name"), nameField,
                new Label("Address"), addressField,
                new Label("Info"), infoArea,
                chooseImage, imageView, saveBtn);
        form.setAlignment(Pos.CENTER_LEFT);

        vb.getChildren().add(form);
    }

    
   


    private void chooseImage(Stage stage) {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Choose Shop Image");
    fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
    );
    File file = fileChooser.showOpenDialog(stage);
    if (file != null) {
        selectedImage = file;
        selectedImageFileName = file.getName(); 

        try {
            String destinationPath = "src/main/resources/assets/" + selectedImageFileName;
            File dest = new File(destinationPath);
            java.nio.file.Files.copy(file.toPath(), dest.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);

           
            Image img = new Image(getClass().getResource("/assets/" + selectedImageFileName).toExternalForm());
            imageView.setImage(img);
            imageView.setVisible(true);
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Failed to copy image: " + ex.getMessage());
        }
    }
}



    private void saveShopInfo() {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String info = infoArea.getText().trim();
        String currentUid = Session.getCurrentUid();

        if (name.isEmpty() || address.isEmpty() || info.isEmpty() || selectedImageFileName == null) {
            showAlert("Please fill all fields and choose an image.");
            return;
        }

        Map<String, Object> shopData = new HashMap<>();
        shopData.put("name", name);
        shopData.put("address", address);
        shopData.put("info", info);
        shopData.put("image", selectedImageFileName);
        shopData.put("uid", currentUid);

        new Thread(() -> {
            try {
                Firebaseconfig.getFirestore().collection("shops").add(shopData).get();
                Platform.runLater(() -> {
                    showAlert("Shop info saved to Firestore successfully!");
                    showDefaultDashboard();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> showAlert("Failed to save shop info: " + ex.getMessage()));
            }
        }).start();
    }

    private void showDefaultDashboard() {
        vb.getChildren().clear();
        String currentUid = Session.getCurrentUid();

        new Thread(() -> {
            try {
                var query = Firebaseconfig.getFirestore()
                        .collection("shops")
                        .whereEqualTo("uid", currentUid);
                var snapshot = query.get().get();

                Platform.runLater(() -> {
                    if (snapshot.isEmpty()) {
                        vb.getChildren().add(new Label("No shop info found for this user."));
                    } else {
                        for (var doc : snapshot.getDocuments()) {
                            showShopInfo(doc.getId(), doc.getData());
                        }
                    }

                Button goToDashboardBtn = new Button("Go to Dashboard");
                goToDashboardBtn.setStyle("-fx-background-color: #FFA726; -fx-text-fill: white; -fx-font-weight: bold;");
                goToDashboardBtn.setOnAction(e -> {
                    Stage stage = (Stage) goToDashboardBtn.getScene().getWindow();
                    Shopview.showDashboard(stage);
                });


                    VBox buttonWrapper = new VBox(goToDashboardBtn);
                    buttonWrapper.setAlignment(Pos.CENTER_RIGHT);
                    buttonWrapper.setPadding(new Insets(20, 0, 0, 0));
                    vb.getChildren().add(buttonWrapper);
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() -> vb.getChildren().add(new Label("Failed to load shop info.")));
            }
        }).start();
    }

    private void showShopInfo(String docId, Map<String, Object> data) {
        String name = (String) data.get("name");
        String address = (String) data.get("address");
        String info = (String) data.get("info");
        String imageFile = (String) data.get("image");

        Label nameLabel = new Label("Shop Name: " + name);
        Label addressLabel = new Label("Address: " + address);
        Label infoLabel = new Label("Info: " + info);

        // Image image = new Image("file:resources/assets/" + imageFile);
        Image image = new Image(getClass().getResource("/assets/" + imageFile).toExternalForm());

        ImageView imgView = new ImageView(image);
        imgView.setFitWidth(200);
        imgView.setFitHeight(150);

        VBox infoBox = new VBox(10, nameLabel, addressLabel, infoLabel);
        infoBox.setPadding(new Insets(10));

        HBox shopBox = new HBox(20, imgView, infoBox);
        shopBox.setAlignment(Pos.CENTER_LEFT);
        shopBox.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-padding: 10;");

        Button editBtn = new Button("Edit");
        editBtn.setOnAction(e -> editShopInfo(docId, name, address, info, imageFile));

        VBox container = new VBox(10, shopBox, editBtn);
        container.setPadding(new Insets(10));

        vb.getChildren().add(container);
    }

    private void editShopInfo(String docId, String name, String address, String info, String imageFileName) {
        vb.getChildren().clear();

        nameField = new TextField(name);
        addressField = new TextField(address);
        infoArea = new TextArea(info);

        selectedImageFileName = imageFileName;
        // imageView = new ImageView(new Image("file:resources/assets/" + imageFileName));
        imageView = new ImageView(new Image(getClass().getResource("/assets/" + imageFileName).toExternalForm()));

        imageView.setFitWidth(200);
        imageView.setFitHeight(150);
        imageView.setVisible(true);

        Button chooseImage = new Button("Change Image");
        chooseImage.setOnAction(e -> chooseImage(primaryStage));

        Button updateBtn = new Button("Update Info");
        updateBtn.setStyle("-fx-background-color: #2196F3; -fx-text-fill: white;");
        updateBtn.setOnAction(e -> updateShopInfo(docId));

        VBox form = new VBox(10,
                new Label("Shop Name"), nameField,
                new Label("Address"), addressField,
                new Label("Info"), infoArea,
                chooseImage, imageView, updateBtn);
        form.setAlignment(Pos.CENTER_LEFT);

        vb.getChildren().add(form);
    }

    private void updateShopInfo(String docId) {
        String name = nameField.getText().trim();
        String address = addressField.getText().trim();
        String info = infoArea.getText().trim();

        if (name.isEmpty() || address.isEmpty() || info.isEmpty() || selectedImageFileName == null) {
            showAlert("Please fill all fields and choose an image.");
            return;
        }

        Map<String, Object> shopData = new HashMap<>();
        shopData.put("name", name);
        shopData.put("address", address);
        shopData.put("info", info);
        shopData.put("image", selectedImageFileName);
        shopData.put("uid", Session.getCurrentUid());

        new Thread(() -> {
            try {
                Firebaseconfig.getFirestore().collection("shops").document(docId).set(shopData).get();
                Platform.runLater(() -> {
                    showAlert("Shop info updated successfully!");
                    showDefaultDashboard();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
                Platform.runLater(() -> showAlert("Update failed: " + ex.getMessage()));
            }
        }).start();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void openDashboardPage1() {
        try {
            new First().start(new Stage());
            primaryStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            showAlert("Failed to open DashboardPage1.");
        }
    }
}