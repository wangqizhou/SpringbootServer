package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.HeatContentSearchInfo;
import com.evistek.mediaserver.entity.StatisticVideo;
import com.evistek.mediaserver.entity.Video;
import com.evistek.mediaserver.service.IDatabaseService;
import com.evistek.mediaserver.service.ITotalHeatVideoService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.service.impl.TotalHeatVideoServiceImpl;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import com.evistek.mediaserver.utils.UrlBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by evis on 2017/1/5.
 */

@RestController
public class VideoController {

    private final IVideoService videoService;
    private final ITotalHeatVideoService totalHeatVideoService;
    private final IDatabaseService databaseService;
    private final UrlBuilder urlBuilder;
    private final Logger logger;
    private final OpLogger opLogger;

    public VideoController(OpLogger opLogger, IVideoService videoService, ITotalHeatVideoService totalHeatVideoService,
                           IDatabaseService databaseService, UrlBuilder urlBuilder) {
        this.videoService = videoService;
        this.totalHeatVideoService = totalHeatVideoService;
        this.databaseService = databaseService;
        this.urlBuilder = urlBuilder;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/videos", method = RequestMethod.GET)
    public String getVideos(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<Video> videos = videoService.findVideos(pageNumber, pageSize, new OrderBy(IVideoService.ORDER_DOWNLOAD, OrderBy.DESC));
        int total = videoService.findVideoNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE,
                "query videos: " + videos.size());

        return TablePageHelper.toJSONString(total, videos);
    }

    @RequestMapping(value = "/videos/number", method = RequestMethod.GET)
    public String getVideoNumber(@RequestParam(value = "audit", required = false) Boolean audit,
                                 HttpServletRequest request) {

        this.opLogger.info(request, OpLogger.ACTION_QUERY_RESOURCE, "query video number");

        if (audit != null) {
            return JSON.toJSONString(this.videoService.findVideoNumberByAudit(audit));
        } else {
            return JSON.toJSONString(this.videoService.findVideoNumber());
        }
    }

    @RequestMapping(value = "/videos", method = RequestMethod.PUT)
    public String updateVideo(@RequestBody Video video, HttpServletRequest request){
        String msg = "success";
        if (this.videoService.updateVideo(video) != 1) {
            logger.error("updateVideo failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_UPDATE_RESOURCE,
                "update information for video with url: " + video.getUrl());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/videos", method = RequestMethod.DELETE)
    public String deleteVideo(@RequestBody String id, HttpServletRequest request){
        String msg = "success";
        Video video = videoService.findVideoById(Integer.parseInt(id));
        this.videoService.deleteVideoFromDisk(video);
        if (this.videoService.deleteVideo(video) != 1) {
            logger.error("deleteVideo failed.");
            msg = "fail";
        }

        this.opLogger.warn(request, OpLogger.ACTION_DELETE_RESOURCE,
                "delete video with url: " + video.getUrl());

        return JSON.toJSONString(msg);
    }

    @RequestMapping(value = "/videos/hot", method = RequestMethod.GET)
    public String getHeatContent(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            @RequestParam(value = "range", required = false) String range,
            @RequestParam(value = "date", required = false) String dateString,
            @RequestParam(value = "granularity", required = false) String granularity,
            @RequestParam(value = "flag", required = false) String flag,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int query_grade;
        switch (granularity) {
            case "日":
                query_grade = TotalHeatVideoServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = TotalHeatVideoServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = TotalHeatVideoServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = TotalHeatVideoServiceImpl.GRADE_DAYS;
                break;
        }

        int num = Integer.parseInt(range.substring(0, range.indexOf("个")));

        List<StatisticVideo> heatVideos;
        int total;
        if (flag.equals("是")) {
            heatVideos = this.totalHeatVideoService.getHeatVideoFilterZero(query_grade, date, num, pageNumber, pageSize);
            total = totalHeatVideoService.getHeatVideoFilterZero(query_grade, date, num).size();
        } else {
            heatVideos = this.totalHeatVideoService.getHeatVideo(query_grade, date, num, pageNumber, pageSize);
            total = totalHeatVideoService.getHeatVideo(query_grade, date, num).size();
        }

        this.opLogger.info(request, OpLogger.ACTION_QUERY_HOT_RESOURCE, "query hot videos");

        return TablePageHelper.toJSONString(total, heatVideos);
    }

    @RequestMapping(value = "/videos/hot", method = RequestMethod.POST)
    public String exportHeatContentXml(@RequestBody HeatContentSearchInfo info){

        int query_grade;
        switch (info.granularity) {
            case "日":
                query_grade = TotalHeatVideoServiceImpl.GRADE_DAYS;
                break;
            case "周":
                query_grade = TotalHeatVideoServiceImpl.GRADE_WEEK;
                break;
            case "月":
                query_grade = TotalHeatVideoServiceImpl.GRADE_MONTH;
                break;
            default:
                query_grade = TotalHeatVideoServiceImpl.GRADE_DAYS;
                break;
        }

        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(info.date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int num = Integer.parseInt(info.range.substring(0, info.range.indexOf("个")));

        int filter;
        if (info.flag.equals("是")) {
            filter = TotalHeatVideoServiceImpl.FILTER_ZERO;
        } else {
            filter = TotalHeatVideoServiceImpl.UNFILTER;
        }

        String statement = totalHeatVideoService.exportXLSByHeatVideo(query_grade, date, num, filter);

        String filename = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSS").format(new Date()) + ".xls";
        File exportFile = new File(databaseService.getExportPath() + filename);
        if (exportFile.exists()) {
            exportFile.delete();
        }
        int result = databaseService.exportXls(statement, filename);
        if (result != -1) {
            return JSON.toJSONString(this.urlBuilder.buildUrl(filename));
        } else {
            return JSON.toJSONString("err");
        }

    }
}
