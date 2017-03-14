package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.ICategoryService;
import com.evistek.mediaserver.service.IUploadService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/12.
 */
@Service("UploadVideoService")
public class UploadVideoServiceImpl implements IUploadService{
    private final IVideoService videoService;
    private final ICategoryService categoryService;
    private final UrlBuilder urlBuilder;
    private final String SOURCE_DIR;
    private final Logger logger;

    public UploadVideoServiceImpl(IVideoService videoService, ICategoryService categoryService,
                                  UrlBuilder urlBuilder) {
        this.videoService = videoService;
        this.categoryService = categoryService;
        this.urlBuilder = urlBuilder;

        this.logger = LoggerFactory.getLogger(getClass());
        this.SOURCE_DIR = System.getProperty("catalina.base") + File.separator +
                "webapps" + File.separator + "ROOT";
    }

    @Override
    public int save(MultipartFile file, Object object) {
        return 0;
    }

    @Override
    public int save(MultipartFile[] files, Object object) {
        Video video = (Video) object;

        for(MultipartFile file: files) {
            if (file.getContentType().toLowerCase().startsWith("video")) {
                parseVideoInfo(file, video);
            }
        }

        Video addedVideo;
        if ((addedVideo = this.videoService.addVideo(video)) == null) {
            this.logger.error("fail to add video info to db");
            return -1;
        }

        checkDirectory(addedVideo);
        int result = 0;
        for(int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String contentType = file.getContentType();
            if (contentType.toLowerCase().startsWith("image")) {
                String suffix = generateSuffix(i);
                if (savePreviewFile(file, addedVideo, suffix) == -1) {
                    result = -1;
                    break;
                }

            } else if (contentType.toLowerCase().startsWith("video")) {
                if (saveVideoFile(file, addedVideo) == -1) {
                    result = -1;
                    break;
                }
            } else {
                result = -1;
                this.logger.error("unsupported content type: " + file.getContentType()
                                + " with origin name: " + file.getOriginalFilename());
            }
        }

        if (result == 0) {
            if (this.videoService.updateVideo(addedVideo) == 1) {
                // 更新信息到入参对象中
                video.setUrl(addedVideo.getUrl());
                video.setLandscapeCoverUrl(addedVideo.getLandscapeCoverUrl());
                video.setPortraitCoverUrl(addedVideo.getPortraitCoverUrl());
                video.setPreview1Url(addedVideo.getPreview1Url());
                video.setPreview2Url(addedVideo.getPreview2Url());
                video.setPreview3Url(addedVideo.getPreview3Url());
                return 0;
            }
        }

        this.logger.error("fail to save file, delete them all");
        delete(files, addedVideo);

        return -1;
    }

    private Video parseVideoInfo(MultipartFile file, Video video) {

        if (video.getCategoryId() <= 0) {
            int categoryId = this.categoryService.findCategoryId(video.getCategoryName(), "video");
            video.setCategoryId(categoryId);
        }

        Date now = new Date();
        video.setCreateTime(now);
        video.setUpdateTime(now);

        String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
        video.setFormat(format);

        video.setSize(file.getSize());
        video.setAudit(false);
        video.setDownloadCount(0);
        video.setUrl(urlBuilder.buildUrl(video));
        return video;
    }

    private int saveVideoFile(MultipartFile part, Video video) {
        video.setUrl(urlBuilder.buildUrl(video));

        return saveFile(part, urlBuilder.buildPhysicalPath(video));
    }

    private int savePreviewFile(MultipartFile part, Video video, String suffix) {
        String format = part.getOriginalFilename().substring(part.getOriginalFilename().lastIndexOf(".") + 1);
        String url = urlBuilder.buildPreviewUrl(video, suffix, format);

        switch (suffix) {
            case "_landscape":
                video.setLandscapeCoverUrl(url);
                break;
            case "_portrait":
                video.setPortraitCoverUrl(url);
                break;
            case "_pre1":
                video.setPreview1Url(url);
                break;
            case "_pre2":
                video.setPreview2Url(url);
                break;
            case "_pre3":
                video.setPreview3Url(url);
                break;
        }

        return saveFile(part, urlBuilder.buildPreviewPhysicalPath(video, suffix, format));
    }

    private int saveFile(MultipartFile part, String destFile) {
        File file = new File(destFile);
        try {
            part.transferTo(file);
            this.logger.info("save file: " + part.getOriginalFilename() +
                    " to " + destFile);
        } catch (IOException e) {
            e.printStackTrace();

            if (file.exists()) {
                file.delete();
            }

            this.logger.error("fail to save file: " + part.getOriginalFilename() +
                              " to " + destFile);

            return -1;
        }

        return 0;
    }

    private void delete(MultipartFile[] files, Video video) {
        for(int i = 0; i < files.length; i++) {
            MultipartFile file = files[i];
            String contentType = file.getContentType();
            if (contentType.toLowerCase().startsWith("image")) {
                String suffix = generateSuffix(i);
                String format = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);
                File previewFile = new File(urlBuilder.buildPreviewPhysicalPath(video, suffix, format));
                if (previewFile.exists() && previewFile.isFile()) {
                    previewFile.delete();
                }

            } else if (contentType.toLowerCase().startsWith("video")) {
                File videoFile = new File(urlBuilder.buildPhysicalPath(video));
                if (videoFile.exists() && videoFile.isFile()) {
                    videoFile.delete();
                }
            }
        }

        this.videoService.deleteVideo(video);
    }

    private String generateSuffix(int index) {
        String suffix = "_";
        switch (index) {
            case 0:
                suffix += "landscape";
                break;
            case 1:
                suffix += "portrait";
                break;
            case 2:
            case 3:
            case 4:
                suffix += ("pre" + (index - 1));
                break;
        }

        return suffix;
    }

    private int checkDirectory(Video video) {
        String dir = new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("video").append(File.separator)
                .append(video.getCategoryName()).append(File.separator)
                .append(video.getId())
                .toString();

        File file = new File(dir);
        if (!file.exists()) {
            logger.info("Create dir: " + dir);
            file.mkdirs();
        }
        return 0;
    }

}
