package com.easy.app.demo.databinding;

/**
 * @author : zhousf
 * @description :
 * @date : 2017/4/11.
 */

public class Info{

    private String title;

    private String content;

    private String date;


    @Override
    public String toString() {
        return "Info[title="+title+",content="+content+",date="+date+"]";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
