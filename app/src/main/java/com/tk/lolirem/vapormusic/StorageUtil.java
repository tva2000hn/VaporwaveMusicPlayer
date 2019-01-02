package com.tk.lolirem.vapormusic;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.lang.reflect.Type;


public class StorageUtil {

    private final String STORAGE = " com.tk.lolirem.vapormusic.STORAGE";
    private SharedPreferences preferences;
    private Context context;

    public StorageUtil(Context context) {
        this.context = context;
    }

    public void storeAudio(ArrayList<Audio> arrayList) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);

        editor.putString("audioArrayList", json);

        editor.apply();
    }

    public ArrayList<Audio> loadAudio() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);
        Gson gson = new Gson();
        String json = preferences.getString("audioArrayList",null);
        Type type = new TypeToken<ArrayList<Audio>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void storeAudioIndex(int index) {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("audioIndex", index);
        editor.apply();
    }

    public int loadAudioIndex() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);
        return preferences.getInt("audioIndex", -1);//return -1 if no data found
    }

    public void clearCachedAudioPlaylist() {
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.commit();
    }
    public void killthefile(){
        preferences = context.getSharedPreferences(STORAGE, Context.MODE_MULTI_PROCESS);

        SharedPreferences.Editor editor = preferences.edit();

        editor.remove("audioArrayList");
        editor.commit();
    }
}
