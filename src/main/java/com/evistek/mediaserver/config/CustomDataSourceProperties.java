package com.evistek.mediaserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
@Component
@ConfigurationProperties(prefix = "custom.datasource")
public class CustomDataSourceProperties {
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;
    private String backupPathWindows;
    private String backupPathLinux;
    private String exportRelativePathWindows;
    private String exportRelativePathLinux;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBackupPathWindows() {
        return backupPathWindows;
    }

    public void setBackupPathWindows(String backupPathWindows) {
        this.backupPathWindows = backupPathWindows;
    }

    public String getBackupPathLinux() {
        return backupPathLinux;
    }

    public void setBackupPathLinux(String backupPathLinux) {
        this.backupPathLinux = backupPathLinux;
    }

    public String getExportRelativePathWindows() {
        return exportRelativePathWindows;
    }

    public void setExportRelativePathWindows(String exportRelativePathWindows) {
        this.exportRelativePathWindows = exportRelativePathWindows;
    }

    public String getExportRelativePathLinux() {
        return exportRelativePathLinux;
    }

    public void setExportRelativePathLinux(String exportRelativePathLinux) {
        this.exportRelativePathLinux = exportRelativePathLinux;
    }
}
