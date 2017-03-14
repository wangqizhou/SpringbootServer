package com.evistek.mediaserver.controller;

import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.service.ITotalPlayRecordService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/4.
 */

@RestController
public class PlayRecordController {

    private final Logger logger;
    private final ITotalPlayRecordService totalPlayRecordService;
    private final OpLogger opLogger;

    public PlayRecordController (OpLogger opLogger, ITotalPlayRecordService totalPlayRecordService) {
        this.totalPlayRecordService = totalPlayRecordService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/playRecords", method = RequestMethod.GET)
    public String getPlayRecords(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = limit > 0 ? (offset / pageSize + 1) : 0;
        List<PlayRecord> playRecords = totalPlayRecordService.findAllPlayRecords(pageNumber,pageSize);
        int total = totalPlayRecordService.findPlayRecordNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_PLAY_RECORD, "query play records: " + playRecords.size());

        return TablePageHelper.toJSONString(total, playRecords);
    }
}
