package com.upnext.Model;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.api.core.ApiFuture;

import java.util.HashMap;
import java.util.Map;

public class ReferralService {

    // Save referred user at signup
    public void saveReferredUser(String uid, String name, String email, String phone, String referralCode) {
        Firestore db = Firebaseconfig.getFirestore();

        Map<String, Object> user = new HashMap<>();
        user.put("uid", uid);
        user.put("name", name);
        user.put("email", email);
        user.put("phone", phone);
        user.put("referralCodeUsed", referralCode != null ? referralCode : "");
        user.put("hasPaid", false);
        user.put("referralVerified", false);

        ApiFuture<WriteResult> future = db.collection("Users").document(uid).set(user);

        try {
            WriteResult result = future.get(); // Wait for completion
            System.out.println("✅ User saved in Firestore at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.out.println("❌ Firestore write error: " + e.getMessage());
        }
    }

    // Called after referred user completes condition (like signup or payment)
    public void markReferralAsVerified(String uid) {
        Firestore db = Firebaseconfig.getFirestore();

        Map<String, Object> updates = new HashMap<>();
        updates.put("hasPaid", true);
        updates.put("referralVerified", true);

        ApiFuture<WriteResult> future = db.collection("Users").document(uid).update(updates);

        try {
            WriteResult result = future.get();
            System.out.println("✅ Referral marked verified at: " + result.getUpdateTime());
        } catch (Exception e) {
            System.out.println("❌ Error marking referral verified: " + e.getMessage());
        }
    }

    // 🔍 Get referral code for the current user
    public static String getReferralCodeForUser(String uid) {
        Firestore db = Firebaseconfig.getFirestore();
        try {
            return db.collection("Users").document(uid).get().get().getString("referralCode");
        } catch (Exception e) {
            System.out.println("❌ Failed to fetch referral code: " + e.getMessage());
            return null;
        }
    }
}
