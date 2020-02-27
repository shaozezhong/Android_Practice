package com.example.myapplication.allbean;

public class RecommendBean {
    private String title;
    private String img;
    private String comment_couont;
    private String share_url;
    private String time;


    public String getTime() {
        return time;
    }


    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
    public void setTime(String time) {
        this.time = time;
    }

    public String getComment_couont() {
        return comment_couont;
    }

    public void setComment_couont(String comment_couont) {
        this.comment_couont = comment_couont;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
