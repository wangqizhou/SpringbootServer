package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.config.CustomDataSourceProperties;
import com.evistek.mediaserver.service.IDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
@Service
public class DatabaseServiceImpl implements IDatabaseService{
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final CustomDataSourceProperties dataSourceProperties;
    private final boolean isWindows;
    private String host;
    private String port;
    private String username;
    private String password;
    private String database;
    private String backupPath;
    private String exportPath;

    @Autowired
    public DatabaseServiceImpl(CustomDataSourceProperties dataSourceProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.host = this.dataSourceProperties.getHost();
        this.port = this.dataSourceProperties.getPort();
        this.username = this.dataSourceProperties.getUsername();
        this.password = this.dataSourceProperties.getPassword();
        this.database = this.dataSourceProperties.getDatabase();

        this.isWindows = System.getProperty("os.name").toLowerCase().startsWith("windows");
        this.backupPath = isWindows ?
                            this.dataSourceProperties.getBackupPathWindows() :
                            this.dataSourceProperties.getBackupPathLinux();

        File backupDir = new File(this.backupPath);
        if (!backupDir.exists()) {
            if(!backupDir.mkdirs()) {
                logger.error("Failed to make directory: " + this.backupPath);
            }
        }

        if (System.getProperty("catalina.base") != null) {
            this.exportPath = System.getProperty("catalina.base").concat(File.separator)
                    .concat("webapps").concat(File.separator)
                    .concat("ROOT").concat(File.separator)
                    .concat(isWindows ?
                            this.dataSourceProperties.getExportRelativePathWindows() :
                            this.dataSourceProperties.getExportRelativePathLinux()).concat(File.separator);
        } else {
            // Can't get path of embedded tomcat in Spring Boot
            this.exportPath = new StringBuilder()
                    .append(isWindows ? "d:" : "/root").append(File.separator)
                    .append(isWindows ?
                            this.dataSourceProperties.getExportRelativePathWindows() :
                            this.dataSourceProperties.getExportRelativePathLinux()).append(File.separator)
                    .toString();
        }

        File exportDir = new File(this.exportPath);
        if (!exportDir.exists()) {
            if (!exportDir.mkdirs()) {
                logger.error("Failed to make directory: " + this.exportPath);
            }
        }
    }

    private String buildBackupScript(String file) {
        return new StringBuilder()
                .append("mysqldump").append(" ")
                .append("--default-character-set=utf8mb4").append(" ")
                .append("-h ").append(host).append(" ")
                .append("-u").append(username).append(" ")
                .append("-p").append(password).append(" ")
                .append(database).append(" ")
                .append(">").append(file)
                .toString();
    }

    private String buildRestoreScript(String file) {
        return new StringBuilder()
                .append("mysql").append(" ")
                .append("-u").append(username).append(" ")
                .append("-p").append(password).append(" ")
                .append(database).append(" ")
                .append("<").append(file)
                .toString();
    }

    private String buildExportScript(String statement, String file) {
        return new StringBuilder()
                .append("mysql").append(" ")
                .append("-h ").append(host).append(" ")
                .append("-u").append(username).append(" ")
                .append("-p").append(password).append(" ")
                .append(database).append(" ")
                .append("-P ").append(port).append(" ")
                .append("-e ").append(statement).append(" ")
                .append(">").append("\"" + file + "\"").append(" ")
                .append("&&").append(" ")
                .append("iconv").append(" ")
                .append("-f").append("utf8").append(" ")
                .append("-t").append("gb2312").append(" ")
                .append("-o ").append(file).append(" ")
                .append(file)
                .toString();
    }

    private void executeScript(String script) {
        Process process = null;
        try {
            process = this.isWindows ?
                        Runtime.getRuntime().exec(new String[] {"cmd", "/c", script}) :
                        Runtime.getRuntime().exec(new String[] {"sh", "-c", script});
            process.waitFor();
        } catch (IOException e) {
            logger.error("Failed to execute script");
            e.printStackTrace();
        } catch (InterruptedException e) {
            logger.error("Failed to execute script");
            e.printStackTrace();
        } finally {
            if (process != null) {
                process.destroy();
            }
        }
    }

    @Override
    public int backup(String file) {
        String fileFullPath = this.backupPath + file;
        executeScript(buildBackupScript(fileFullPath));
        File f = new File(fileFullPath);
        if (f.exists()) {
            logger.info("Database backup succeeded: " + fileFullPath);
            return 0;
        } else {
            logger.error("Database backup failed: " + fileFullPath);
            return -1;
        }
    }

    @Override
    public int restore(String file) {
        String fileFullPath = this.backupPath + file;
        File f = new File(fileFullPath);
        if (f.exists()) {
            executeScript(buildRestoreScript(fileFullPath));
            logger.info("Database restore succeeded: " + fileFullPath);
            return 0;
        } else {
            logger.error("Database restore failed. File doesn't exist: " + fileFullPath);
            return -1;
        }
    }

    @Override
    public int exportXls(String statement, String file) {
        String fileFullPath = this.exportPath + file;
        executeScript(buildExportScript(statement, fileFullPath));
        File f = new File(fileFullPath);
        if (f.exists()) {
            logger.info("Database export succeeded: " + fileFullPath);
            return 0;
        } else {
            logger.error("Database export failed: " + fileFullPath);
            return -1;
        }
    }

    @Override
    public String getBackupPath() {
        return this.backupPath;
    }

    @Override
    public String getExportPath() {
        return this.exportPath;
    }
}
