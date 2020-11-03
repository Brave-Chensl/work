package com.chensl.reptile.domain;

public class Movie {

    private double rate;    //评分
    private String title;   //电影名称
    private String directedBy; //导演
    private String summary;   //剧情简介

    @Override
    public String toString() {
        return "Movie{" +
                "rate=" + rate +
                ", title='" + title + '\'' +
                ", directedBy='" + directedBy + '\'' +
                ", summary='" + summary + '\'' +
                '}';
    }

    public double getRate() {
        return rate;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getDirectedBy() {
        return directedBy;
    }

    public void setDirectedBy(String directedBy) {
        this.directedBy = directedBy;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
