package com.example.lavtc_photo;

import android.net.Uri;

public class Image {

    private String index;
    private String filename;
    private Uri uri;


    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public void setIndex(String index) { this.index = index; }
    public String getIndex() {
        return index;
    }
}
