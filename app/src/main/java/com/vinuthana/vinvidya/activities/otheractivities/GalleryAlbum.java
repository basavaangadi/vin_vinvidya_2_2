package com.vinuthana.vinvidya.activities.otheractivities;

/**
 * Created by KISHAN on 08-15-2017.
 */

public class GalleryAlbum {
    private String name;
    private int numOfSongs;
    private int thumbnail;

    public GalleryAlbum() {

    }

    public GalleryAlbum(String name, int numOfSongs, int thumbnail) {
        this.name = name;
        this.numOfSongs = numOfSongs;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfSongs() {
        return numOfSongs;
    }

    public void setNumOfSongs(int numOfSongs) {
        this.numOfSongs = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
