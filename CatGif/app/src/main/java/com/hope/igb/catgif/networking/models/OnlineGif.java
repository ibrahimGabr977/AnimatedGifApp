package com.hope.igb.catgif.networking.models;

public class OnlineGif {

    private String id, category, fileInfo, downloadUrl;
    private int likes_count;


    OnlineGif() {
    }

    public OnlineGif(String id, String category, String fileInfo, String downloadUrl, int likes_count) {
        this.id = id;
        this.category = category;
        this.fileInfo = fileInfo;
        this.downloadUrl = downloadUrl;
        this.likes_count = likes_count;
    }

    public String getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public String getFileInfo() {
        return fileInfo;
    }

    public int getLikes_count() {
        return likes_count;
    }


    public String getDownloadUrl() {
        return downloadUrl;
    }


    public void setLikes_count(int likes_count) {
        this.likes_count = likes_count;
    }
}
