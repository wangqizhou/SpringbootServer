package com.evistek.mediaserver.entity;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */
public class StatisticVideo {
    private String grade;

    private String startTime;

    private String endTime;

    private int videoId;

    private String videoName;

    private int categoryId;

    private int totalCount;

    private int comCount;

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public int getVideoId() {
        return videoId;
    }

    public void setVideoId(int videoId) {
        this.videoId = videoId;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getComCount() {
        return comCount;
    }

    public void setComCount(int comCount) {
        this.comCount = comCount;
    }
}
