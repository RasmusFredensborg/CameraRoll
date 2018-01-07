package com.rasmusdev.cameraroll;

public class GalleryItem {
    private String path;

    public GalleryItem(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
