package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;

import java.util.List;

/**
 *
 * Created by ymzhao on 2016/12/30.
 */
public interface IPlayRecordService {
    String ORDER_ID = "id";
    String ORDER_START_TIME = "start_time";
    String ORDER_END_TIME = "end_time";
    String ORDER_DURATION = "duration";

    int findPlayRecordNumber();
    PlayRecord findPlayRecordById(int id);
    PlayRecord findPlayRecord(int userId, int videoId, String client);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, OrderBy orderBy);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy);

    PlayRecord addPlayRecord(PlayRecord record);
    int addPlayRecord(PlayRecord record, int months);

    int deletePlayRecord(int id);
    int deletePlayRecord(PlayRecord record);
    int deletePlayRecordsByUserId(int userId, String client);
    int deletePlayRecordBatch(List<PlayRecord> playRecordsList);

    int updatePlayRecord(PlayRecord newRecord);


}
