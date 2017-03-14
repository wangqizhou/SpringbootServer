package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.model.PlayRecordModel;
import com.evistek.mediaserver.service.IPlayRecordService;
import com.evistek.mediaserver.service.ITotalPlayRecordService;
import com.evistek.mediaserver.service.IUserService;
import com.evistek.mediaserver.service.IVideoService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by ymzhao on 2017/1/17.
 */
@RestController
@RequestMapping("/api/v2")
public class PlayRecordApi {
    private final IPlayRecordService playRecordService;
    private final ITotalPlayRecordService totalPlayRecordService;
    private final IUserService userService;
    private final IVideoService videoService;
    private final Logger logger;

    public PlayRecordApi(IPlayRecordService playRecordService, ITotalPlayRecordService totalPlayRecordService,
                         IUserService userService, IVideoService videoService) {
        this.playRecordService = playRecordService;
        this.totalPlayRecordService = totalPlayRecordService;
        this.userService = userService;
        this.videoService = videoService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/play_records", method = RequestMethod.GET)
    public String getPlayRecordsByUserId(@RequestParam(value = "user_id", required = true) int
                                                     userId,
                                         @RequestParam(value = "client", required = true) String client,
                                         @RequestParam(value = "page", required = false, defaultValue = "0") int
                                                 pageNumber,
                                         @RequestParam(value = "page_size", required = false, defaultValue = "0") int
                                                     pageSize,
                                         HttpServletResponse resp) {

        if (userId <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for user_id");
        }

        if (pageNumber < 0 || pageSize < 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for page: " + pageNumber + " or page_size: " + pageSize);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for page or page_size");
        }

        List<PlayRecord> playRecords = this.playRecordService.findPlayRecordByUserId(
                userId, client, pageNumber, pageSize, new OrderBy(IPlayRecordService.ORDER_END_TIME, OrderBy.DESC));

        List<PlayRecordModel> playRecordModels = new ArrayList<>();
        for (PlayRecord entity: playRecords) {

            PlayRecordModel model = new PlayRecordModel();
            model.setId(entity.getId());
            model.setUserId(entity.getUserId());
            model.setStartTime(entity.getStartTime());
            model.setEndTime(entity.getEndTime());
            model.setDuration(entity.getDuration());
            model.setClient(entity.getClient());
            model.setClientVersion(entity.getClientVersion());
            model.setVideo(this.videoService.findVideoById(entity.getVideoId()));

            playRecordModels.add(model);
        }

        return JSON.toJSONStringWithDateFormat(playRecordModels, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping(value = "/play_records", method = RequestMethod.POST)
    public String addPlayRecord(@RequestBody PlayRecord playRecord, HttpServletResponse resp) {
        PlayRecord result = this.playRecordService.addPlayRecord(playRecord);
        int resultTotal = -1;
        if (result != null) {
            PlayRecord newRecord = new PlayRecord();
            newRecord.setUserId(playRecord.getUserId());
            newRecord.setClient(playRecord.getClient());
            newRecord.setClientVersion(playRecord.getClientVersion());
            newRecord.setVideoId(playRecord.getVideoId());
            newRecord.setStartTime(playRecord.getStartTime());
            newRecord.setEndTime(playRecord.getEndTime());
            newRecord.setVideoName(playRecord.getVideoName());
            PlayRecord isExit = this.totalPlayRecordService.findPlayRecord(result.getUserId(), result.getVideoId(),
                    result.getStartTime(),result.getClient());
            if (isExit == null) {
                resultTotal = this.totalPlayRecordService.addPlayRecord(newRecord);
            }
        }
        if (resultTotal == 1) {
            return JSON.toJSONStringWithDateFormat(result, "yyyy-MM-dd HH:mm:ss");
        } else {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to add playRecord with video id: " + playRecord.getVideoId());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to add playRecord with video id: " + playRecord.getVideoId());
        }
    }

    @RequestMapping(value = "/play_records/id/{id}", method = RequestMethod.DELETE)
    public String deletePlayRecord(@PathVariable(value = "id") int id, HttpServletResponse resp) {

        if (id <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for id");
        }
        int result = this.playRecordService.deletePlayRecord(id);
        if (result > 0) {
            logger.info("status:" + HttpServletResponse.SC_OK + " OK " +
                    "message: " + "success to delete playRecord with id: " + id);
            return JSON.toJSONString("success to delete playRecord with id: " + id);
        } else {
            logger.error("status:" + HttpServletResponse.SC_INTERNAL_SERVER_ERROR + " INTERNAL SERVER ERROR " +
                    "message: " + "success to delete playRecord with id: " + id);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "fail to delete playRecord with id: " + id);
        }
    }

    @RequestMapping(value = "/play_records/user_id/{user_id}", method = RequestMethod.DELETE)
    public String deletePlayRecordsByUserId(@PathVariable(value = "user_id") int userId,
                                            @RequestParam(value = "client", required = true) String client,
                                            HttpServletResponse resp) {
        if (userId <= 0) {
            logger.error("status:" + HttpServletResponse.SC_NOT_ACCEPTABLE + " NOT ACCEPTABLE " +
                    "message: " + "invalid value for user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_ACCEPTABLE,
                    "invalid value for user_id");
        }

        int result = this.playRecordService.deletePlayRecordsByUserId(userId, client);
        if (result > 0) {
            logger.info("status:" + HttpServletResponse.SC_OK + " OK " +
                    "message: " + "success to delete " + result + " playRecord(s) with user_id: " + userId);
            return JSON.toJSONString("success to delete " + result + " playRecord(s) with user_id: " + userId);
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND " +
                    "message: " + "not found playRecord with user_id: " + userId);
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_NOT_FOUND,
                    "not found playRecord with user_id: " + userId);
        }
    }

    @RequestMapping(value = "/play_records", method = RequestMethod.DELETE)
    public String deletePlayRecords(@RequestBody List<PlayRecord> playRecordList, HttpServletResponse resp) {
        int result = this.playRecordService.deletePlayRecordBatch(playRecordList);
        if (result > 0) {
            logger.info("status:" + HttpServletResponse.SC_OK + "OK" + "message: " + "success to delete " + result +
                    " playRecords with userId" + playRecordList.get(0).getUserId());
            return JSON.toJSONString("success to delete " + result +
                            " playeRecords with userId: " + playRecordList.get(0).getUserId());
        } else {
            logger.error("status:" + HttpServletResponse.SC_NOT_FOUND + " NOT FOUND " + "message:" + "not found " +
                    "playRecord with userId: " + playRecordList.get(0).getUserId());
            return HttpErrorMessage.build(resp, HttpServletResponse.SC_NOT_FOUND, "not found playRecord with " +
                    "userId" + playRecordList.get(0).getUserId());
        }
    }

    @RequestMapping(value = "/play_records", method = RequestMethod.PUT)
    public String updatePlayDuration(@RequestBody PlayRecord playRecord, HttpServletResponse resp) {
        PlayRecord playRecordInDB = playRecordService.findPlayRecord(playRecord.getUserId(), playRecord.getVideoId(), playRecord.getClient());
        int result = this.playRecordService.updatePlayRecord(playRecord);
        int totalResult = -1;
        if (result == 1) {
            if (playRecordInDB != null) {
                PlayRecord newRecord = new PlayRecord();
                newRecord.setUserId(playRecord.getUserId());
                newRecord.setClient(playRecord.getClient());
                newRecord.setClientVersion(playRecord.getClientVersion());
                newRecord.setVideoId(playRecord.getVideoId());
                newRecord.setStartTime(playRecord.getStartTime());
                newRecord.setEndTime(playRecord.getEndTime());
                newRecord.setVideoName(playRecord.getVideoName());
                newRecord.setStartTime(playRecordInDB.getStartTime());
                totalResult = this.totalPlayRecordService.updatePlayRecord(newRecord);
            }
        }
        if (totalResult != 1) {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to update playDuration with id: " + playRecord.getId() +
                    " and userId: " + playRecord.getUserId());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to update playDuration with id: " + playRecord.getId() +
                            " and userId: " + playRecord.getUserId());
        }

        return JSON.toJSONString("success to update playDuration information");
    }

}
