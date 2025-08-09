package com.View;

import com.upnext.Controller.Controller;
import com.upnext.Model.Session;

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
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;

public class Home extends Application {

    Controller authController = new Controller();

    @Override
    public void start(Stage myStage) {
        Text welcomeText = new Text("Welcome to InfluPay");
        welcomeText.setFont(Font.font("Verdana", FontWeight.BOLD, 26));
        welcomeText.setFill(Color.BLACK);
        //welcomeText.setUnderline(true);

        Text loginTitle = new Text("USER LOGIN");
        loginTitle.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        loginTitle.setFill(Color.BLACK);

        ImageView userIcon;
        URL imageUrl = getClass().getResource("/assets/profile img.png");
        if (imageUrl != null) {
            Image image = new Image(imageUrl.toExternalForm(), 70, 70, true, true);
            userIcon = new ImageView(image);
        } else {
            System.out.println("Image not found: /assets/logo2.jpg");
            userIcon = new ImageView();
        }

        Text usernameLabel = new Text("Username:");
        usernameLabel.setFont(Font.font("Arial", 20));
        TextField usernameField = new TextField();
        usernameField.setPromptText("Username");
        usernameField.setMaxWidth(250);

        Text passwordLabel = new Text("Password:");
        passwordLabel.setFont(Font.font(20));
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");
        passwordField.setMaxWidth(250);

        Label resultlb = new Label();

       
        Button adminLoginButton = new Button("Admin Login");
        adminLoginButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        adminLoginButton.setStyle("-fx-background-color : #1ea747ff; -fx-text-fill: white; -fx-background-radius :20;");
        adminLoginButton.setOnAction(e -> {
            Adminlogin adminLogin = new Adminlogin();
            adminLogin.start(myStage); 
        });


        Button loginButton = new Button("Login");
        loginButton.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        loginButton.setStyle("-fx-background-color : #1ea747ff; -fx-text-fill: white; -fx-background-radius :20;");
        loginButton.setOnAction(e -> {
            String email = usernameField.getText();
            String password = passwordField.getText();
            boolean success = authController.signInWithEmailAndPassword(email, password);

            if (success) {
    resultlb.setText("Login successful");

    Platform.runLater(() -> {
        try {
            String usernameOnly = email.split("@")[0].toLowerCase();  
            Session.setUsername(usernameOnly);
            new First(usernameOnly).start(new Stage()); 
            myStage.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    });
}
 else {
                resultlb.setText("Login Failed");
            }
        });

        Button shopbtn = new Button("Shop Login");
        shopbtn.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        shopbtn.setStyle("-fx-background-color:#1ea747ff; -fx-text-fill:white; -fx-background-radius:20");
        shopbtn.setOnAction(e -> {
            Shoplogin shopLogin = new Shoplogin();
            Scene shopScene = shopLogin.createScene(myStage);
            myStage.setScene(shopScene);
            myStage.setTitle("Influpay - Shopkeeper Login");
        });

        HBox loginButtons = new HBox(15, loginButton, adminLoginButton);
        loginButtons.setAlignment(Pos.CENTER);

        VBox vb1 = new VBox(10, shopbtn);
        vb1.setAlignment(Pos.CENTER);

        Label newUserLabel = new Label("New User ?");
        newUserLabel.setFont(Font.font(20));
        Button signupLink = new Button("Signup");
        signupLink.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        signupLink.setStyle("-fx-background-color : #1ea747ff; -fx-text-fill: white; -fx-background-radius :20;");
        signupLink.setOnAction(e -> {
            Signup signup = new Signup();
            signup.start(myStage);
        });

        HBox signupBox = new HBox(5, newUserLabel, signupLink);
        signupBox.setAlignment(Pos.CENTER);


        HBox hb = new HBox(10, usernameLabel, usernameField);
        hb.setAlignment(Pos.CENTER);

        HBox hb1 = new HBox(10, passwordLabel, passwordField);
        hb1.setAlignment(Pos.CENTER);

        VBox vb = new VBox(20, welcomeText, loginTitle, userIcon, hb, hb1, loginButtons, vb1, signupBox, resultlb);
        vb.setPadding(new Insets(20));
        vb.setAlignment(Pos.CENTER);
        vb.setBackground(new Background(new BackgroundFill(
                new LinearGradient(0, 0, 1, 1, true, CycleMethod.NO_CYCLE,
                        new Stop(0, Color.web("#43cea2")), new Stop(1, Color.web("#185a9d"))),
                CornerRadii.EMPTY, Insets.EMPTY)));

        BorderPane root = new BorderPane();
        root.setCenter(vb);
       

        Scene scene = new Scene(root, 1020,780);
        myStage.setTitle("Influpay - User Login");
        myStage.setScene(scene);
        myStage.getIcons().add(new Image("assets\\newAppLogo.jpg"));
        myStage.setResizable(true);
        myStage.show();
    }
}




