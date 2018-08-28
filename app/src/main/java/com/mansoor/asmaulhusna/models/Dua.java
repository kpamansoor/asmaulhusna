package com.mansoor.asmaulhusna.models;

/**
 * Created by L4208412 on 29/6/2018.
 */

public class Dua {
    private String duaName,duaArabic,duaEnglish,duaBenefits;

    public Dua(String duaName, String duaArabic, String duaEnglish, String duaBenefits) {
        this.duaName = duaName;
        this.duaArabic = duaArabic;
        this.duaEnglish = duaEnglish;
        this.duaBenefits = duaBenefits;
    }

    public Dua() {
    }

    public String getDuaName() {
        return duaName;
    }

    public void setDuaName(String duaName) {
        this.duaName = duaName;
    }

    public String getDuaArabic() {
        return duaArabic;
    }

    public void setDuaArabic(String duaArabic) {
        this.duaArabic = duaArabic;
    }

    public String getDuaEnglish() {
        return duaEnglish;
    }

    public void setDuaEnglish(String duaEnglish) {
        this.duaEnglish = duaEnglish;
    }

    public String getDuaBenefits() {
        return duaBenefits;
    }

    public void setDuaBenefits(String duaBenefits) {
        this.duaBenefits = duaBenefits;
    }
}
