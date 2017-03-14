package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.service.IImageService;
import com.evistek.mediaserver.service.IUploadService;
import com.evistek.mediaserver.utils.DirectoryChecker;
import com.evistek.mediaserver.utils.UrlBuilder;
import net.coobird.thumbnailator.Thumbnails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/11.
 */
@Service("UploadImageService")
public class UploadImageServiceImpl implements IUploadService{
    private static final int THUMBNAIL_SIZE = (int) (512 * 0.75);
    private final IImageService imageService;
    private final UrlBuilder urlBuilder;
    private final DirectoryChecker dirChecker;
    private final Logger logger;

    public UploadImageServiceImpl(IImageService imageService,
                                  UrlBuilder urlBuilder, DirectoryChecker dirChecker) {
        this.imageService = imageService;
        this.urlBuilder = urlBuilder;
        this.dirChecker = dirChecker;

        this.logger = LoggerFactory.getLogger(getClass());
    }

    @Override
    public int save(MultipartFile[] file, Object object) {
        return 0;
    }

    @Override
    public int save(MultipartFile file, Object object) {
        Image image = (Image) object;
        parseImageInfo(file, image);

        if (saveFile(file, image) == 0) {
            if(this.imageService.addImage(image) != 1) {
                File imageFile = new File(this.urlBuilder.buildPhysicalPath(image));
                if (imageFile.exists()) {
                    imageFile.delete();
                }

                File thumbnailFile = new File(this.urlBuilder.buildThumbnailPhysicalPath(image));
                if (thumbnailFile.exists()) {
                    thumbnailFile.delete();
                }

                return -1;
            }

            return 0;
        }

        return -1;
    }

    private Image parseImageInfo(MultipartFile file, Image image) {
        Date now = new Date();
        String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        int width = 0;
        int height = 0;
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            width = bufferedImage.getWidth();
            height = bufferedImage.getHeight();
        } catch (IOException e) {
            e.printStackTrace();
        }

        image.setName(rename(file.getOriginalFilename()));
        image.setCreateTime(now);
        image.setUpdateTime(now);
        image.setFormat(format);
        image.setWidth(width);
        image.setHeight(height);
        image.setSize(file.getSize());
        image.setAudit(false);
        image.setDownloadCount(0);

        image.setUrl(this.urlBuilder.buildUrl(image));
        image.setThumbnail(this.urlBuilder.buildThumbnailUrl(image));

        return image;
    }

    private int saveFile(MultipartFile file, Image image) {
        File savedFile = new File(this.urlBuilder.buildPhysicalPath(image));
        try {
            this.dirChecker.check("image");
            file.transferTo(savedFile);
            Thumbnails.of(savedFile)
                    .size(THUMBNAIL_SIZE, THUMBNAIL_SIZE)
                    .toFile(this.urlBuilder.buildThumbnailPhysicalPath(image));
            this.logger.info("save file: " + file.getOriginalFilename() + " to " + savedFile);
        } catch (IOException e) {
            e.printStackTrace();

            if (savedFile.exists()) {
                savedFile.delete();
            }

            File thumbnail = new File(this.urlBuilder.buildThumbnailPhysicalPath(image));
            if (thumbnail.exists()) {
                thumbnail.delete();
            }

            this.logger.error("fail to save file: " + file.getOriginalFilename() + " to " + savedFile);

            return -1;
        }

        return 0;
    }

    private synchronized String rename(String fileName) {
        // Only return UUID now
        return UUID.randomUUID().toString().replace("-", "");
    }
}
