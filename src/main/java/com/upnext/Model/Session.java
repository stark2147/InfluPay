package com.upnext.Model;

public class Session {
    private static String currentUid;
    private static String username;  // ✅ Add this line

    public static void setCurrentUid(String uid) {
        currentUid = uid;
    }

    public static String getCurrentUid() {
        return currentUid;
    }

    // ✅ Add these new methods for username
    public static void setUsername(String uname) {
        username = uname;
    }

    public static String getUsername() {
        return username;
    }
}
