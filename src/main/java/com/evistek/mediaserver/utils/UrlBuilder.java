package com.evistek.mediaserver.utils;

import com.evistek.mediaserver.config.CustomDataSourceProperties;
import com.evistek.mediaserver.config.CustomServerProperties;
import com.evistek.mediaserver.entity.Image;
import com.evistek.mediaserver.entity.Product;
import com.evistek.mediaserver.entity.Video;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/16.
 */
@Component
public class UrlBuilder {
    private final CustomServerProperties serverProperties;
    private final CustomDataSourceProperties dataSourceProperties;
    private final String SOURCE_DIR;
    private final boolean isWindows;

    public UrlBuilder(CustomServerProperties serverProperties, CustomDataSourceProperties dataSourceProperties) {
        this.serverProperties = serverProperties;
        this.dataSourceProperties = dataSourceProperties;
        this.SOURCE_DIR = System.getProperty("catalina.base") + File.separator +
                "webapps" + File.separator + "ROOT";
        this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
    }

    public String buildUrl(Video video) {
        return new StringBuilder()
                .append("http://")
                .append(this.serverProperties.getIp()).append(":")
                .append(this.serverProperties.getPort()).append("/")
                .append("video").append("/")
                .append(video.getCategoryName()).append("/")
                .append(video.getId()).append("/")
                .append(video.getName()).append(".").append(video.getFormat())
                .toString();
    }

    public String buildPreviewUrl(Video video, String suffix, String imageFormat) {
        return new StringBuilder()
                .append("http://")
                .append(this.serverProperties.getIp()).append(":")
                .append(this.serverProperties.getPort()).append("/")
                .append("video").append("/")
                .append(video.getCategoryName()).append("/")
                .append(video.getId()).append("/")
                .append(video.getName()).append(suffix).append(".").append(imageFormat)
                .toString();
    }

    public String buildPhysicalPath(Video video) {
        return new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("video").append(File.separator)
                .append(video.getCategoryName()).append(File.separator)
                .append(video.getId()).append(File.separator)
                .append(video.getName()).append(".").append(video.getFormat())
                .toString();
    }

    public String buildPreviewPhysicalPath(Video video, String suffix, String imageFormat) {
        return new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("video").append(File.separator)
                .append(video.getCategoryName()).append(File.separator)
                .append(video.getId()).append(File.separator)
                .append(video.getName()).append(suffix).append(".").append(imageFormat)
                .toString();
    }

    public String buildUrl(Image image) {
        return new StringBuilder()
                .append("http://")
                .append(this.serverProperties.getIp()).append(":")
                .append(this.serverProperties.getPort()).append("/")
                .append("image").append("/")
                .append(image.getCategoryName()).append("/")
                .append(image.getName()).append(".").append(image.getFormat())
                .toString();
    }

    public String buildThumbnailUrl(Image image) {
        return new StringBuilder()
                .append("http://")
                .append(this.serverProperties.getIp()).append(":")
                .append(this.serverProperties.getPort()).append("/")
                .append("image").append("/")
                .append(image.getCategoryName()).append("/")
                .append(image.getName()).append("-thumb").append(".").append(image.getFormat())
                .toString();
    }

    public String buildPhysicalPath(Image image) {
        return new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("image").append(File.separator)
                .append(image.getCategoryName()).append(File.separator)
                .append(image.getName()).append(".").append(image.getFormat())
                .toString();
    }

    public String buildThumbnailPhysicalPath(Image image) {
        return new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("image").append(File.separator)
                .append(image.getCategoryName()).append(File.separator)
                .append(image.getName()).append("-thumb").append(".").append(image.getFormat())
                .toString();
    }

    public String buildUrl(Product product) {
        return new StringBuilder()
                .append("http://")
                .append(this.serverProperties.getIp()).append(":")
                .append(this.serverProperties.getPort()).append("/")
                .append("product").append("/")
                .append(product.getCategoryName()).append("/")
                .append(product.getName()).append(".").append("jpg")
                .toString();
    }

    public String buildPhysicalPath(Product product) {
        return new StringBuilder()
                .append(SOURCE_DIR).append(File.separator)
                .append("product").append(File.separator)
                .append(product.getCategoryName()).append(File.separator)
                .append(product.getName()).append(".").append("jpg")
                .toString();
    }

    public String buildUrl(String filename) {
        return new StringBuilder()
                .append("http://")
                .append(getIP())
                .append(":").append(this.serverProperties.getPort()).append("/")
                .append(this.isWindows ?
                        this.dataSourceProperties.getExportRelativePathWindows() :
                        this.dataSourceProperties.getExportRelativePathLinux()).append("/")
                .append(filename)
                .toString();
    }

    private String getIP() {
        String ip = "localhost";
        try {
            InetAddress addr = InetAddress.getLocalHost();
            if (this.isWindows) {
                ip = addr.getHostAddress();
            } else {
                ip = this.serverProperties.getIp();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ip;
    }
}
