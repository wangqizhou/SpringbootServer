package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Image;

import java.util.List;

/**
 *
 * Created by ymzhao on 2016/12/26.
 */
public interface IImageService {
     String ORDER_ID = "id";
     String ORDER_CREATE_TIME = "create_time";
     String ORDER_UPDATE_TIME = "update_time";
     String ORDER_SIZE = "size";
     String ORDER_DOWNLOAD = "download_count";

     Image findImageById(int id);
     Image findImageById(int id, boolean audit);
     Image findImageByUrl(String url);
     List<Image> findImages();
     List<Image> findImages(OrderBy orderBy);
     List<Image> findImages(int pageNumber, int pageSize);
     List<Image> findImages(int pageNumber, int pageSize, OrderBy orderBy);
     List<Image> findImages(int pageNumber, int pageSize, OrderBy orderBy, boolean audit);
     List<Image> findImagesByCategoryId(int categoryId);
     List<Image> findImagesByCategoryId(int categoryId, OrderBy orderBy);
     List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize);
     List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy);
     List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit);
     List<Image> findImagesByCategoryName(String categoryName);
     List<Image> findImagesByCategoryName(String categoryName, OrderBy orderBy);
     List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize);
     List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy);
     List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit);
     List<Image> findImagesByAudit(boolean audit);
     List<Image> findImagesByAudit(boolean audit, OrderBy orderBy);
     List<Image> findImagesByAudit(boolean audit, int pageNumber, int pageSize);
     List<Image> findImagesByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy);
     int findImageNumber();
     int findImageNumberByAudit(boolean audit);

     int addImage(Image image);

     int deleteImage(Image image);
     int deleteImageById(int id);
     int deleteImageByUrl(String url);
     int deleteImages(List<Image> imageList);

    /**
     * 从磁盘上删除文件，包括图片及其thumbnail
     *
     * @param image 所要删除的图片
     * @return 返回实际删除的文件个数。 比如图片及其thumbnail都删除，则返回2
     */
     int deleteFromDisk(Image image);

     int updateImage(Image image);
}
