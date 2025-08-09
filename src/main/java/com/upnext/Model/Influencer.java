package com.upnext.Model;

public class Influencer {
    private String uid;
    private String email;
    private String instagramLink;
    private boolean verified;

    public Influencer() {} // Required for Firebase

    public Influencer(String uid, String email, String instagramLink, boolean verified) {
        this.uid = uid;
        this.email = email;
        this.instagramLink = instagramLink;
        this.verified = verified;
    }

    public String getUid() { return uid; }
    public void setUid(String uid) { this.uid = uid; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getInstagramLink() { return instagramLink; }
    public void setInstagramLink(String instagramLink) { this.instagramLink = instagramLink; }

    public boolean isVerified() { return verified; }
    public void setVerified(boolean verified) { this.verified = verified; }
}


