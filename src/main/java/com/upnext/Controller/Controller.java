// package com.upnext.Controller;
// import java.io.BufferedReader;
// import java.io.InputStreamReader;
// import java.io.OutputStream;
// import java.net.HttpURLConnection;
// import java.net.URL;

// public class Controller {

//     public static final String API_KEY = "AIzaSyBg6JgsNY_66jwXYfNp_8ra8mZkGhfHoAA";

    
//     public boolean signInWithEmailAndPassword(String email, String password) {
//         try {
//             URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY);
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("POST");
//             conn.setRequestProperty("Content-Type", "application/json");
//             conn.setDoOutput(true);
//             String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);
//             try (OutputStream os = conn.getOutputStream()) {
//                 os.write(payload.getBytes());
//             }
//             int responseCode = conn.getResponseCode();
//             if (responseCode == 200) {
//                 return true;
//             } else {
//                 try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
//                     String line;
//                     while ((line = br.readLine()) != null) {
//                         System.out.println(line);
//                     }
//                 }
//                 return false;
//             }
//         } catch (Exception ex) {
//             ex.printStackTrace();
//             return false;
//         }
//     }

    
//     public boolean signUpWithEmailAndPassword(String email, String password) {
//         try {
//             URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY);
//             HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//             conn.setRequestMethod("POST");
//             conn.setRequestProperty("Content-Type", "application/json");
//             conn.setDoOutput(true);
//             String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);
//             try (OutputStream os = conn.getOutputStream()) {
//                 os.write(payload.getBytes());
//             }
//             int responseCode = conn.getResponseCode();
//             if (responseCode == 200) {
//                 return true;
//             } else {
//                 try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
//                     String line;
//                     while ((line = br.readLine()) != null) {
//                         System.out.println(line);
//                     }
//                 }
//                 return false;
//             }
//         } catch (Exception ex) {
//             ex.printStackTrace();
//             return false;
//         }
//     }
// }

package com.upnext.Controller;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class Controller {

    public static final String API_KEY = "AIzaSyBg6JgsNY_66jwXYfNp_8ra8mZkGhfHoAA";

    // ✅ Login method
    public boolean signInWithEmailAndPassword(String email, String password) {
        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format(
                    "{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}",
                    email, password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                // ✅ Read the response
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }

                // ✅ Parse JSON and extract UID
                JSONObject json = new JSONObject(response.toString());
                String uid = json.getString("localId");

                // ✅ Store UID in session
                com.upnext.Model.Session.setCurrentUid(uid);
                System.out.println("✅ UID stored in session: " + uid); // debug

                return true;

            } else {
                // Print error response
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println(" Login error: " + line);
                    }
                }
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    // ✅ Signup method
    public boolean signUpWithEmailAndPassword(String email, String password) {
        try {
            URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=" + API_KEY);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String payload = String.format("{\"email\":\"%s\",\"password\":\"%s\",\"returnSecureToken\":true}", email, password);

            try (OutputStream os = conn.getOutputStream()) {
                os.write(payload.getBytes());
            }

            int responseCode = conn.getResponseCode();
            if (responseCode == 200) {
                return true;
            } else {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(conn.getErrorStream()))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        System.out.println("❌ Signup error: " + line);
                    }
                }
                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
