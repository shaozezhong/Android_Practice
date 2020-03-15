package com.example.myapplication.channel;

public class ChannelBean {

    String src;
    int typeView;

    public ChannelBean(String src, int typeView) {
        this.src = src;
        this.typeView = typeView;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getTypeView() {
        return typeView;
    }

    public void setTypeView(int typeView) {
        this.typeView = typeView;
    }
}
