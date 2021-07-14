package com.white.testapp1;

import android.content.Intent;

public class firebasemodel {
    String title;
    String Content;
//    Integer timestamp;
    public firebasemodel(){

    }
    public firebasemodel(String title, String Content){
        this.title=title;
//        this.timestamp=timestamp;
        this.Content=Content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

//    public Integer getTimestamp() {
//        return timestamp;
//    }
//
//    public void setTimestamp(Integer timestamp) {
//        this.timestamp = timestamp;
//    }
}
