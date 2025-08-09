package com.upnext.Model;
import javafx.beans.property.SimpleStringProperty;

public class UserPage$User {
   private final SimpleStringProperty userId;
   private final SimpleStringProperty email;
   private final SimpleStringProperty password;
   private final SimpleStringProperty action;

   public UserPage$User(String userId, String email, String password, String action) {
      this.userId = new SimpleStringProperty(userId);
      this.email = new SimpleStringProperty(email);
      this.password = new SimpleStringProperty(password);
      this.action = new SimpleStringProperty(action);
   }

   public String getUserId() {
      return this.userId.get();
   }

   public String getEmail() {
      return this.email.get();
   }

   public String getPassword() {
      return this.password.get();
   }

   public String getAction() {
      return this.action.get();
   }
}