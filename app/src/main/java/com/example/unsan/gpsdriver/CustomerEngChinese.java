package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 7/5/18.
 */

public class CustomerEngChinese  {
    String chinese;
    Object english;

    public CustomerEngChinese() {
    }



    public String getChinese() {
        return chinese;
    }

    public CustomerEngChinese(String chinese, Object english) {
        this.chinese = chinese;
        this.english = english;
    }

    public void setChinese(String chinese) {
        this.chinese = chinese;

    }

    public Object getEnglish() {
        return english;
    }

    public void setEnglish(Object english) {
        this.english = english;
    }
}
