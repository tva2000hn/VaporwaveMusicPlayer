package com.tk.lolirem.vapormusic;
import android.os.AsyncTask;

import org.jsoup.Jsoup;

import java.io.IOException;

public class VersionChecker extends AsyncTask<String, String, String> {

    private String newVersion;

    @Override
    protected String doInBackground(String... params) {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.tk.lolirem.vapormusic" + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return newVersion;
    }
}