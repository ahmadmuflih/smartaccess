package com.a4nesia.baso.smartaccess.utils;

public class Utils {
    public static String getFirstLetters(String text){
        text = text.toUpperCase();
        String[] myName = text.split(" ");
        String name = "";
        for (int i = 0; i < myName.length; i++) {
            if(i<2)
                name += myName[i].charAt(0);
        }
        return name;
    }
}
