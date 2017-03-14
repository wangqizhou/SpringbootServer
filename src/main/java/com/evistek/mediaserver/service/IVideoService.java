package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Video;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/25.
 */
public interface IVideoService {
    String ORDER_ID = "id";
    String ORDER_CREATE_TIME = "create_time";
    String ORDER_SIZE = "size";
    String ORDER_DURATION = "duration";
    String ORDER_DOWNLOAD = "download_count";

    Video findVideoById(int id);
    Video findVideoByUrl(String url);
    List<Video> findVideos();
    List<Video> findVideos(OrderBy orderBy);
    List<Video> findVideos(int pageNumber, int pageSize);
    List<Video> findVideos(int pageNumber, int pageSize, OrderBy orderBy);
    List<Video> findVideosByCategoryId(int categoryId);
    List<Video> findVideosByCategoryId(int categoryId, OrderBy orderBy);
    List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize);
    List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy);
    List<Video> findVideosByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit);
    List<Video> findVideosByCategoryName(String categoryName);
    List<Video> findVideosByCategoryName(String categoryName, OrderBy orderBy);
    List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize);
    List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy);
    List<Video> findVideosByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit);
    List<Video> findVideosByAudit(boolean audit);
    List<Video> findVideosByAudit(boolean audit, OrderBy orderBy);
    List<Video> findVideosByAudit(boolean audit, int pageNumber, int pageSize);
    List<Video> findVideosByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy);
    int findVideoNumber();
    int findVideoNumberByAudit(boolean audit);

    Video addVideo(Video video);

    int deleteVideo(Video video);
    int deleteVideoById(int id);
    int deleteVideoByUrl(String url);
    int deleteVideos(List<Video> videoList);

    /**
     * 从磁盘上删除文件，包括视频文件及其封面图、预览图等
     *
     * @param video 所要删除的视频
     * @return 返回实际删除的文件个数。 比如，完全删除了视频文件、landscape封面图，portrait封面图、3个预览图，则返回6
     */
    int deleteVideoFromDisk(Video video);

    int updateVideo(Video video);
}
