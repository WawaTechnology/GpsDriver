package com.example.unsan.gpsdriver;

/**
 * Created by Unsan on 7/5/18.
 */

public class CustomerEngChinese  {
    public CustomerEngChinese() {
    }

    String RestChinese,RestEnglish;

    public String getRestChinese() {
        return RestChinese;
    }

    public CustomerEngChinese(String restChinese, String restEnglish) {
        RestChinese = restChinese;
        RestEnglish = restEnglish;
    }

    public void setRestChinese(String restChinese) {
        RestChinese = restChinese;
    }

    public String getRestEnglish() {
        return RestEnglish;
    }

    public void setRestEnglish(String restEnglish) {
        RestEnglish = restEnglish;
    }
}
