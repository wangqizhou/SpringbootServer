package com.evistek.mediaserver.entity;

import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/25.
 */
@Component
public class Video {
    private int id;
    private String name;
    private int categoryId;
    private String categoryName;
    private Date createTime;
    private Date updateTime;
    private Date releaseTime;
    private String format;
    private int width;
    private int height;
    private long size;
    private long duration;
    private String actors;
    private String location;
    private String url;
    private String landscapeCoverUrl;
    private String portraitCoverUrl;
    private String preview1Url;
    private String preview2Url;
    private String preview3Url;
    private String brief;
    private String introduction;
    private int ownerId;
    private boolean audit;
    private long downloadCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getActors() {
        return actors;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLandscapeCoverUrl() {
        return landscapeCoverUrl;
    }

    public void setLandscapeCoverUrl(String landscapeCoverUrl) {
        this.landscapeCoverUrl = landscapeCoverUrl;
    }

    public String getPortraitCoverUrl() {
        return portraitCoverUrl;
    }

    public void setPortraitCoverUrl(String portraitCoverUrl) {
        this.portraitCoverUrl = portraitCoverUrl;
    }

    public String getPreview1Url() {
        return preview1Url;
    }

    public void setPreview1Url(String preview1Url) {
        this.preview1Url = preview1Url;
    }

    public String getPreview2Url() {
        return preview2Url;
    }

    public void setPreview2Url(String preview2Url) {
        this.preview2Url = preview2Url;
    }

    public String getPreview3Url() {
        return preview3Url;
    }

    public void setPreview3Url(String preview3Url) {
        this.preview3Url = preview3Url;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public boolean isAudit() {
        return audit;
    }

    public void setAudit(boolean audit) {
        this.audit = audit;
    }

    public long getDownloadCount() {
        return downloadCount;
    }

    public void setDownloadCount(long downloadCount) {
        this.downloadCount = downloadCount;
    }
}
