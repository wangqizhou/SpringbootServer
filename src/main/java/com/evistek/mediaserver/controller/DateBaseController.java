package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.service.IDatabaseService;
import com.evistek.mediaserver.utils.OpLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by evis on 2016/12/20.
 */

@RestController
public class DateBaseController {
    private final IDatabaseService databaseService;
    private final Logger logger;
    private final OpLogger opLogger;

    public DateBaseController(OpLogger opLogger, IDatabaseService databaseService) {
        this.databaseService = databaseService;
        this.logger = LoggerFactory.getLogger(getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/db/backup", method = RequestMethod.GET)
    public List<FileInfo> getBackup(HttpServletRequest request){
        String dir = this.databaseService.getBackupPath();
        if (dir == null) {
            logger.error("backup fail to get backup dir");
            return new ArrayList<>();
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_DATABASE_BACKUP, "query db backups");

        return GetFileName(dir);
    }

    @RequestMapping(value = "/db/backup", method = RequestMethod.POST)
    public String backup(HttpServletRequest request){
        String date = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss").format(new Date());
        String fileName = date + ".sql";

        int flag = this.databaseService.backup(fileName);
        String msg = "success";
        if (flag == 0) {
            logger.info("backup success to file: " + fileName);
        } else {
            logger.error("backup fail");
            msg = "fail";
        }

        this.opLogger.info(request, OpLogger.ACTION_BACKUP_DATABASE, "backup db");

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/db/restore/{name}", method = RequestMethod.POST)
    public String restore(@PathVariable("name") String name, HttpServletRequest request){
        File file = new File(this.databaseService.getBackupPath() + name);
        String msg = "success";

        if (file.exists()) {
            int flag = databaseService.restore(name);
            if (flag == 0) {
                logger.info("restore success to file: " + name);
            } else {
                logger.error("restore fail");
                msg = "fail";
            }
        }

        this.opLogger.warn(request, OpLogger.ACTION_RESTORE_DATABASE, "restore db to: " + name);

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/db/delete/{name}", method = RequestMethod.DELETE)
    public String deleteDB(@PathVariable("name") String name, HttpServletRequest request){
        File file = new File(this.databaseService.getBackupPath() + name);

        String msg = "";
        if (file.exists()) {
            msg = file.delete() ? "success" : "fail";
            logger.info("delete success. File: " + name);
        } else {
            msg = "deleteDB fail. File doesn't exit: " + name;
            logger.error("delete fail. File: " + name);
        }

        this.opLogger.warn(request, OpLogger.ACTION_DELETE_BACKUP, "delete db backup: " + name);

        return JSON.toJSONString(msg);
    }

    private List<FileInfo> GetFileName(String path) {
        List<FileInfo> list = new ArrayList<FileInfo>();
        File rootDir = new File(path);
        String[] fileList = rootDir.list();
        for (int i = 0; i < fileList.length; i++) {
            FileInfo fp = new FileInfo();
            String dir = rootDir.getAbsolutePath() + File.separator + fileList[i];
            File file = new File(dir);
            long time = file.lastModified();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date= new Date(time);
            String createTime = sdf.format(date);
            fp.setCreateTime(createTime);
            fp.setFilePath(fileList[i]);
            list.add(fp);
        }
        if (list.size() > 0) {
            return list;
        }
        return null;
    }

    class FileInfo {
        private String filePath;
        private String createTime;

        public String getFilePath() {
            return filePath;
        }

        public void setFilePath(String filePath) {
            this.filePath = filePath;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }
    }
}
