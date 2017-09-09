package com.example.ph_data01221240053.movies;

import java.io.Serializable;
/**********************************************************************************************************************************************/

public class Movie implements Serializable {
private String videoPath;
    private String posterPath;
    private String title;
    private String overView;
    private String videwPath;
    private String date;
    private String reviews;
    private boolean  favorir=false;
    private double  vote;
    private int id;
    /**********************************************************************************************************************************************/

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setFavorir(boolean favorir) {
        this.favorir = favorir;
    }

    public boolean isFavorir() {
        return favorir;
    }

    public void setReviews(String reviews) {
        this.reviews = reviews;
    }
    public String getReviews() {
        return reviews;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOverView(String overView) {
        this.overView = overView;
    }

    public String getOverView() {
        return overView;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setVote(double vote) {
        this.vote = vote;
    }

    public double getVote() {
        return vote;
    }

    public void setId(int id) {
        this.id = id;
    }


    public int getId() {
        return id;
    }


}