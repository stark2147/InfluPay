
package com.upnext.Model;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileInputStream;
import java.io.IOException;

public class Firebaseconfig {

    private static boolean initialized = false;
    private static Firestore firestore;

    public static void initialize() {
        if (initialized) return;

        try {
          
           FileInputStream serviceAccount = new FileInputStream("src\\main\\resources\\firebase-service-account..json");


            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://myfxproject-9c5bb-default-rtdb.firebaseio.com") // realtime DB
                    .build();

            // ✅ Initialize app only once
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("✅ Firebase initialized successfully.");
            }

            firestore = FirestoreClient.getFirestore();
            initialized = true;

        } catch (IOException e) {
            System.out.println("❌ Firebase initialization failed:");
            e.printStackTrace();
        }
    }

    public static FirebaseDatabase getDatabase() {
        if (!initialized) {
            initialize();
        }
        return FirebaseDatabase.getInstance();
    }

    public static Firestore getFirestore() {
        if (!initialized) {
            initialize();
        }
        return firestore;
    }
}
