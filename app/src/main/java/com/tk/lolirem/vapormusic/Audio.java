package com.tk.lolirem.vapormusic;


import java.io.Serializable;


public class Audio implements Serializable {

    private String data;
    private String title;
    private String album;
    private String artist;
    private String duration;
    private int mode;
    private int searched;
    private String search;

    public Audio(String data, String title, String album, String artist, String duration,int mode, int searched, String search) {
        this.data = data;
        this.title = title;
        this.album = album;
        this.artist = artist;
        this.duration = duration;
        this.mode = mode;
        this.searched = searched;
        this.search= search;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getSearched() {
        return searched;
    }

    public void setSearched(int searched) {
        this.searched = searched;
    }
    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
