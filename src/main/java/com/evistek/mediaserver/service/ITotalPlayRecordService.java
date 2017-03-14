package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.entity.StatisticUser;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by ymzhao on 2017/1/4.
 */
public interface ITotalPlayRecordService {
    String ORDER_ID = "id";
    String ORDER_START_TIME = "start_time";
    String ORDER_END_TIME = "end_time";
    String ORDER_DURATION = "duration";

    int findPlayRecordNumber();
    PlayRecord findPlayRecord(int userId, int videoId, String client);
    PlayRecord findPlayRecord(int userId, int videoId, Date startTime, String client);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, OrderBy orderBy);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize);
    List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy);

    int addPlayRecord(PlayRecord record);

    /**
     * 添加播放记录的同时，删除当前时间点之前months个月的播放记录
     * @param months 指的是删除几个月之前的数
     * @return 添加且删除成功返回1，失败返回0
     */
    int addPlayRecord(PlayRecord record, int months);

    int deletePlayRecord(PlayRecord record);
    int deletePlayRecordsByUserId(int userId, String client);
    int deletePlayRecordBatch(List<PlayRecord> playRecordsList);

    int updatePlayRecord(PlayRecord newRecord);

    List<PlayRecord> findAllPlayRecords();
    List<PlayRecord> findAllPlayRecords(OrderBy orderBy);
    List<PlayRecord> findAllPlayRecords(int pageNumber, int pageSize);
    List<PlayRecord> findAllPlayRecords(int pageNumber, int pageSize, OrderBy orderBy);

    /**
     * 根据播放记录获取播放视频的活跃用户的信息
     * @param query_grade 查询的粒度，分为月、周、日
     * @param query_type 查询的类型，主要分为按地区查询和不按地区进行查询
     * @param date 查询活跃用户所选的时间点，可以选当前时间，也可选之前的时间
     * @param num 查询的时间跨度，主要指的是查询的date之前月份的个数
     * @return 返回的是活跃用户具体数据的集合
     */
    List<StatisticUser> getStatisticActiveUser(int query_grade, int query_type, Date date, int num);
    List<StatisticUser> getStatisticActiveUser(int query_grade, int query_type, Date date, int num, int pageNumber, int pageSize);

    /**
     * 导出活跃用户数据为xls文件的中的statement
     * @param query_grade 查询的粒度，分为月、周、日
     * @param query_type 查询的类型，主要分为按地区查询和不按地区进行查询
     * @param date 查询活跃用户所选的时间点，可以选当前时间，也可选之前的时间
     * @param num 查询的时间跨度，主要指的是查询的date之前月份的个数
     * @return 返回的是所需导出的活跃用户的statement语句
     * */
    String exportXLSByActiveUser(int query_grade, int query_type, Date date, int num);

}
