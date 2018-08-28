package com.mansoor.asmaulhusna.models;

/**
 * Created by L4208412 on 25/6/2018.
 */

public class Verse {


    private int id;
    private String ayath;
    private String surath;
    private String arabicText;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getAyath() {
        return ayath;
    }

    public void setAyath(String ayath) {
        this.ayath = ayath;
    }

    public String getSurath() {
        return surath;
    }

    public void setSurath(String surath) {
        this.surath = surath;
    }

    public String getArabicText() {
        return arabicText;
    }

    public void setArabicText(String arabicText) {
        this.arabicText = arabicText;
    }

    public String getEnglishText() {
        return englishText;
    }

    public void setEnglishText(String englishText) {
        this.englishText = englishText;
    }

    private String englishText;


}
