package com.upnext.Model;

import com.View.Referralpage;
import com.google.cloud.firestore.*;

public class ReferralChecker {

    public static boolean checkReferralStatus(String uid) {
        Firestore db = Firebaseconfig.getFirestore();

        db.collection("Users").document(uid).addSnapshotListener((snapshot, e) -> {
            if (e != null || snapshot == null || !snapshot.exists()) {
                System.out.println(" Error listening to referral status.");
                return;
            }

            Boolean hasPaid = snapshot.getBoolean("hasPaid");
            Boolean referralVerified = snapshot.getBoolean("referralVerified");

            if (Boolean.TRUE.equals(hasPaid) && Boolean.TRUE.equals(referralVerified)) {
                System.out.println("🎉 Referral Verified! Navigating...");

                // ✅ Open Referral Verified Page
                Referralpage page = new Referralpage();
                try {
                    page.start(new javafx.stage.Stage());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
        return false;
    }
}
