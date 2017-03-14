package com.evistek.mediaserver.entity;

import java.util.Date;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */
public class HeatVideo
{
    private int id;

    private int videoId;

    private String videoName;

    private int categoryId;

    private Date time;

    private int downloadCount;

    private int compareCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public int getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(int downloadCount) {
        this.downloadCount = downloadCount;
    }

    public int getCompareCount() {
        return compareCount;
    }

    public void setCompareCount(int compareCount) {
        this.compareCount = compareCount;
    }
}
