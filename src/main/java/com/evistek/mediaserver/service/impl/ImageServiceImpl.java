package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.ImageDao;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.service.IImageService;
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
 *
 * Created by ymzhao on 2016/12/26.
 */
@Service
public class ImageServiceImpl implements IImageService {
    private final ImageDao imageDao;
    private final UrlBuilder urlBuilder;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public ImageServiceImpl(ImageDao imageDao, UrlBuilder urlBuilder) {
        this.imageDao = imageDao;
        this.urlBuilder = urlBuilder;
    }

    @Override
    public Image findImageById(int id) {
        return this.imageDao.selectImageById(id);
    }

    @Override
    public Image findImageById(int id, boolean audit) {
        return this.imageDao.selectImageById(id, audit);
    }

    @Override
    public Image findImageByUrl(String url) {
        return this.imageDao.selectImageByUrl(url);
    }

    @Override
    public List<Image> findImages() {
        return this.imageDao.selectImages();
    }

    @Override
    public List<Image> findImages(OrderBy orderBy) {
        return this.imageDao.selectImages(validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImages(int pageNumber, int pageSize) {
        return this.imageDao.selectImages(pageNumber, pageSize);
    }

    @Override
    public List<Image> findImages(int pageNumber, int pageSize, OrderBy orderBy) {
        return this.imageDao.selectImages(pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImages(int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        return this.imageDao.selectImagesByAudit(true, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByCategoryId(int categoryId) {
        return this.imageDao.selectImagesByCategoryId(categoryId);
    }

    @Override
    public List<Image> findImagesByCategoryId(int categoryId, OrderBy orderBy) {
        return this.imageDao.selectImagesByCategoryId(categoryId, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize) {
        return this.imageDao.selectImagesByCategoryId(categoryId, pageNumber, pageSize);
    }

    @Override
    public List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.imageDao.selectImagesByCategoryId(categoryId, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByCategoryId(int categoryId, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        return this.imageDao.selectImagesByCategoryId(categoryId, pageNumber, pageSize, validateOrderBy(orderBy), audit);
    }

    @Override
    public List<Image> findImagesByCategoryName(String categoryName) {
        return this.imageDao.selectImagesByCategoryName(categoryName);
    }

    @Override
    public List<Image> findImagesByCategoryName(String categoryName, OrderBy orderBy) {
        return this.imageDao.selectImagesByCategoryName(categoryName, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize) {
        return this.imageDao.selectImagesByCategoryName(categoryName, pageNumber, pageSize);
    }

    @Override
    public List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.imageDao.selectImagesByCategoryName(categoryName, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByCategoryName(String categoryName, int pageNumber, int pageSize, OrderBy orderBy, boolean audit) {
        return this.imageDao.selectImagesByCategoryName(categoryName, pageNumber, pageSize, validateOrderBy(orderBy), audit);
    }

    @Override
    public List<Image> findImagesByAudit(boolean audit) {
        return this.imageDao.selectImagesByAudit(audit);
    }

    @Override
    public List<Image> findImagesByAudit(boolean audit, OrderBy orderBy) {
        return this.imageDao.selectImagesByAudit(audit, validateOrderBy(orderBy));
    }

    @Override
    public List<Image> findImagesByAudit(boolean audit, int pageNumber, int pageSize) {
        return this.imageDao.selectImagesByAudit(audit, pageNumber, pageSize);
    }

    @Override
    public List<Image> findImagesByAudit(boolean audit, int pageNumber, int pageSize, OrderBy orderBy) {
        return this.imageDao.selectImagesByAudit(audit, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public int findImageNumber() {
        return this.imageDao.selectImageCount();
    }

    @Override
    public int findImageNumberByAudit(boolean audit) {
        return this.imageDao.selectImageCountByAudit(audit);
    }

    @Override
    public int addImage(Image image) {
        return this.imageDao.insertImage(image);
    }

    @Override
    public int deleteImage(Image image) {
        return this.imageDao.deleteImage(image);
    }

    @Override
    public int deleteImageById(int id) {
        return this.imageDao.deleteImageById(id);
    }

    @Override
    public int deleteImageByUrl(String url) {
        return this.imageDao.deleteImageByUrl(url);
    }

    @Override
    public int deleteImages(List<Image> imageList) {
        return this.imageDao.deleteImages(imageList);
    }

    @Override
    public int deleteFromDisk(Image image) {
        int result = 0;

        File imageFile = new File(this.urlBuilder.buildPhysicalPath(image));
        if (imageFile.exists() && imageFile.isFile()) {
            if (imageFile.delete()) {
                this.logger.info("success to delete image from disk " + imageFile);
                result++;
            } else {
                this.logger.error("fail to delete image from disk " + imageFile);
            }
        }

        File thumbnail = new File(this.urlBuilder.buildThumbnailPhysicalPath(image));
        if (thumbnail.exists() && thumbnail.isFile()) {
            if (thumbnail.delete()) {
                this.logger.info("success to delete image from disk " + thumbnail);
                result++;
            } else {
                this.logger.error("fail to delete image from disk " + thumbnail);
            }
        }

        return result;
    }

    @Override
    public int updateImage(Image image) {
        Image imageInDB = this.imageDao.selectImageById(image.getId());

        if (image.getCategoryId() != 0 && image.getCategoryName() != null
                && image.getCategoryId() != imageInDB.getCategoryId()
                && !image.getCategoryName().equals(imageInDB.getCategoryName())) {
            // move file on the disk to the directory with category name
            moveTo(imageInDB, image);
        }

        return this.imageDao.updateImage(image);
    }

    private void moveTo(Image fromImage, Image toImage) {
        File toDir = new File(this.urlBuilder.buildPhysicalPath(toImage)).getParentFile();
        if (!toDir.exists()) {
            toDir.mkdirs();
        }

        File imageFile = new File(this.urlBuilder.buildPhysicalPath(fromImage));
        imageFile.renameTo(new File(this.urlBuilder.buildPhysicalPath(toImage)));

        File thumbnailFile = new File(this.urlBuilder.buildThumbnailPhysicalPath(fromImage));
        thumbnailFile.renameTo(new File(this.urlBuilder.buildThumbnailPhysicalPath(toImage)));

        toImage.setUrl(this.urlBuilder.buildUrl(toImage));
        toImage.setThumbnail(this.urlBuilder.buildThumbnailUrl(toImage));
    }

    private OrderBy validateOrderBy(OrderBy orderBy) {
        List<String> keyList = Arrays.asList(
                ORDER_ID,
                ORDER_CREATE_TIME,
                ORDER_UPDATE_TIME,
                ORDER_SIZE,
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
}
