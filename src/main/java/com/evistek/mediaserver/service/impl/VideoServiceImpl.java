package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.dao.VideoDao;
import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import static com.evistek.mediaserver.dao.OrderBy.ASC;
import static com.evistek.mediaserver.dao.OrderBy.DESC;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/25.
 */
@Service
public class VideoServiceImpl implements IVideoService {
    private final VideoDao videoDao;
    private final UrlBuilder urlBuilder;
    private final Logger logger;

    @Autowired
    public VideoServiceImpl(VideoDao videoDao, UrlBuilder urlBuilder) {
        this.videoDao = videoDao;
        this.urlBuilder = urlBuilder;

        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public Video findVideoById(int id) {
        return this.videoDao.selectVideoById(id);
    }

    @Override
    public Video findVideoByUrl(String url) {
        return this.videoDao.selectVideoByUrl(url);
    }

    @Override
    public List<Video> findVideos() {
        return this.videoDao.selectVideos();
    }

    @Override
    public List<Video> findVideos(OrderBy orderBy) {
        return this.videoDao.selectVideos(validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideos(int pageNumber, int pageSize) {
        return this.videoDao.selectVideos(pageNumber, pageSize);
    }

    @Override
    public List<Video> findVideos(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.videoDao.selectVideos(pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByCategoryId(int categoryId) {
        return this.videoDao.selectVideosByCategoryId(categoryId);
    }

    @Override
    public List<Video> findVideosByCategoryId(int categoryId, OrderBy orderBy) {
        return this.videoDao.selectVideosByCategoryId(categoryId, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.videoDao.selectVideosByCategoryId(categoryId, pageNumber, pageSize);
    }

    @Override
    public List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.videoDao.selectVideosByCategoryId(categoryId, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        return this.videoDao.selectVideosByCategoryId(categoryId, pageNumber, pageSize, validateOrderBy(orderBy), audit);
    }

    @Override
    public List<Video> findVideosByCategoryName(String categoryName) {
        return this.videoDao.selectVideosByCategoryName(categoryName);
    }

    @Override
    public List<Video> findVideosByCategoryName(String categoryName, OrderBy orderBy) {
        return this.videoDao.selectVideosByCategoryName(categoryName, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.videoDao.selectVideosByCategoryName(categoryName, pageNumber, pageSize);
    }

    @Override
    public List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.videoDao.selectVideosByCategoryName(categoryName, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        return this.videoDao.selectVideosByCategoryName(categoryName, pageNumber, pageSize, validateOrderBy(orderBy), audit);
    }

    @Override
    public List<Video> findVideosByAudit(boolean audit) {
        return this.videoDao.selectVideosByAudit(audit);
    }

    @Override
    public List<Video> findVideosByAudit(boolean audit, OrderBy orderBy) {
        return this.videoDao.selectVideosByAudit(audit, validateOrderBy(orderBy));
    }

    @Override
    public List<Video> findVideosByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.videoDao.selectVideosByAudit(audit, pageNumber, pageSize);
    }

    @Override
    public List<Video> findVideosByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.videoDao.selectVideosByAudit(audit, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public int findVideoNumber() {
        return this.videoDao.selectVideoCount();
    }

    @Override
    public int findVideoNumberByAudit(boolean audit) {
        return this.videoDao.selectVideoCountByAudit(audit);
    }

    @Override
    public Video addVideo(Video video) {
        int result = this.videoDao.insertVideo(video);
        if (result == 1) {
            return this.videoDao.selectVideoByUrl(video.getUrl());
        } else  {
            logger.error("failed to add video with url: " + video.getUrl());
            return null;
        }
    }

    @Override
    public int deleteVideo(Video video) {
        return this.videoDao.deleteVideo(video);
    }

    @Override
    public int deleteVideoById(int id) {
        return this.videoDao.deleteVideoById(id);
    }

    @Override
    public int deleteVideoByUrl(String url) {
        return this.videoDao.deleteVideoByUrl(url);
    }

    @Override
    public int deleteVideos(List<Video> videoList) {
        return this.videoDao.deleteVideos(videoList);
    }

    @Override
    public int deleteVideoFromDisk(Video video) {
        int result = 0;

        File dir = new File(this.urlBuilder.buildPhysicalPath(video)).getParentFile();
        if (dir.exists() && dir.isDirectory()) {

            File[] files = dir.listFiles();
            if (files != null) {
                for(File f: files) {
                    if(f.delete()) {
                        result++;
                        this.logger.info("success to delete video from disk " + dir + f.getName());
                    } else {
                        this.logger.error("fail to delete video from disk " + dir + f.getName());
                    }
                }
            }

            if (dir.delete()) {
                this.logger.info("success to delete video from disk " + dir);
            } else {
                this.logger.error("fail to delete video from disk " + dir);
            }
        } else {
            this.logger.error("The video to be deleted from disk doesn't exist " + video.getName());
        }

        return result;
    }

    @Override
    public int updateVideo(Video video) {

        Video videoInDB = this.videoDao.selectVideoById(video.getId());
        if (video.getCategoryName() != null && video.getCategoryId() != 0
                && !video.getCategoryName().equals(videoInDB.getCategoryName())
                && video.getCategoryId() != videoInDB.getCategoryId()) {

            // move file on the disk to the directory with category name
            moveTo(videoInDB, video);
        }

        return this.videoDao.updateVideo(video);
    }

    private OrderBy validateOrderBy(OrderBy orderBy) {
        List<String> keyList = Arrays.asList(
                ORDER_ID,
                ORDER_CREATE_TIME,
                ORDER_SIZE,
                ORDER_DURATION,
                ORDER_DOWNLOAD
        );

        List<String> orderList = Arrays.asList(
                ASC,
                DESC
        );

        if (!keyList.contains(orderBy.getKey()) || !orderList.contains(orderBy.getOrder())) {
            logger.error("invalid parameters for ORDER BY: " + orderBy.getKey() + " " + orderBy.getOrder() + "."
                            + " Use ORDER BY id ASC as default."
                        );
            return  new OrderBy(ORDER_ID, ASC);
        }

        return orderBy;
    }

    private void moveTo(Video fromVideo, Video toVideo) {
        File toDir = new File(this.urlBuilder.buildPhysicalPath(toVideo)).getParentFile();
        if (!toDir.exists()) {
            toDir.mkdirs();
        }

        File fromDir = new File(this.urlBuilder.buildPhysicalPath(fromVideo)).getParentFile();
        File[] files = fromDir.listFiles();
        if (files != null) {
            for (File f : files) {
                f.renameTo(new File(toDir + File.separator + f.getName()));
            }
            fromDir.delete();
        }

        toVideo.setUrl(this.urlBuilder.buildUrl(toVideo));

        String format;
        String url = toVideo.getLandscapeCoverUrl();
        if (url != null) {
            format = url.substring(url.lastIndexOf(".") + 1);
            toVideo.setLandscapeCoverUrl(this.urlBuilder.buildPreviewUrl(toVideo, "_landscape", format));
        }

        url = toVideo.getPortraitCoverUrl();
        if (url != null) {
            format = url.substring(url.lastIndexOf(".") + 1);
            toVideo.setPortraitCoverUrl(this.urlBuilder.buildPreviewUrl(toVideo, "_portrait", format));
        }

        url = toVideo.getPreview1Url();
        if (url != null) {
            format = url.substring(url.lastIndexOf(".") + 1);
            toVideo.setPreview1Url(this.urlBuilder.buildPreviewUrl(toVideo, "_pre1", format));
        }

        url = toVideo.getPreview2Url();
        if (url != null) {
            format = url.substring(url.lastIndexOf(".") + 1);
            toVideo.setPreview2Url(this.urlBuilder.buildPreviewUrl(toVideo, "_pre2", format));
        }

        url = toVideo.getPreview3Url();
        if (url != null) {
            format = url.substring(url.lastIndexOf(".") + 1);
            toVideo.setPreview3Url(this.urlBuilder.buildPreviewUrl(toVideo, "_pre3", format));
        }
    }
}
