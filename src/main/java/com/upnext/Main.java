package com.upnext;

import com.View.Firstpage;
import com.upnext.Model.Firebaseconfig;

import javafx.application.Application;

public class Main {
    public static void main(String[] args) {
        Firebaseconfig.initialize();
        System.out.println("Hello world!");
        Application.launch(Firstpage.class,args);
    }
}