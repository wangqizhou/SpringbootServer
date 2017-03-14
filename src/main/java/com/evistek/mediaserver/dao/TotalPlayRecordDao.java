package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by ymzhao on 2017/1/4.
 */
@Component
public class TotalPlayRecordDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public TotalPlayRecordDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectPlayRecordCount() {
        return this.sqlSession.selectOne("selectTotalPlayRecordCount");
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client){
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.selectList("selectTotalPlayRecordsByUserId", mapParameter);
    };

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("client", client);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectTotalPlayRecordsByUserIdOrderBy", map);
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, int pageNumber, int pageSize) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.selectList("selectTotalPlayRecordsByUserId", mapParameter, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("client", client);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectTotalPlayRecordsByUserIdOrderBy", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<PlayRecord> selectRecordsBeforeMonth(Date date, int month){
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.selectList("selectTotalRecordsBeforeMonth", mapParameter);
    }

    //TODO: to be deleted
    public PlayRecord selectPlayRecord(int userId, int videoId, String client){
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("userId", userId);
        mapParameter.put("videoId", videoId);
        mapParameter.put("client", client);
        return this.sqlSession.selectOne("selectTotalPlayRecord", mapParameter);
    }

    public PlayRecord selectPlayRecord(int userId, int videoId, Date startTime, String client){
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("userId", userId);
        mapParameter.put("videoId", videoId);
        mapParameter.put("startTime", startTime);
        mapParameter.put("client", client);
        return this.sqlSession.selectOne("selectOnlyTotalPlayRecord", mapParameter);
    }

    public List<PlayRecord> selectTotalPlayRecords() {
        return this.sqlSession.selectList("selectTotalPlayRecords");
    }

    public List<PlayRecord> selectTotalPlayRecords(OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectTotalPlayRecordsOrderBy", map);
    }

    public List<PlayRecord> selectTotalPlayRecords(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectTotalPlayRecords", null, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<PlayRecord> selectTotalPlayRecords(int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectTotalPlayRecordsOrderBy", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int insertPlayRecord(PlayRecord playRecord) {
        return this.sqlSession.insert("insertTotalPlayRecord", playRecord);
    }

    public int deleteRecordsBeforeMonth (Date date, int month) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.delete("deleteTotalRecordsBeforeMonth", mapParameter);
    }

    public int deletePlayRecord (PlayRecord playRecord) {
        return this.sqlSession.delete("deleteTotalPlayRecord", playRecord);
    }

    public int deletePlayRecordsByUserId(int userId, String client) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.delete("deleteTotalPlayRecordsByUserId", mapParameter);
    }

    public int deletePlayRecords(List<PlayRecord> playRecordList) {
        return this.sqlSession.delete("deleteTotalPlayRecords", playRecordList);
    }

    public int updatePlayRecord(PlayRecord newRecord) {
        return this.sqlSession.update("updateTotalPlayRecord", newRecord);
    }

    public int updateDuration(PlayRecord newRecord) {
        return this.sqlSession.update("updateTotalDuration", newRecord);
    }

    public List<StatisticUser> getStatisticActiveUserByMonths(Date date, int num, String query_type) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByMonths",mapParameter);
    }

    public List<StatisticUser> getStatisticActiveUserByMonths(Date date, int num, String query_type, int pageNumber, int pageSize) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByMonths",mapParameter, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticUser> getStatisticActiveUserByWeeks(Date date, int num, String query_type) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByWeeks",mapParameter);
    }

    public List<StatisticUser> getStatisticActiveUserByWeeks(Date date, int num, String query_type, int pageNumber, int pageSize) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByWeeks",mapParameter, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticUser> getStatisticActiveUserByDays(Date date, int num, String query_type) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByDays",mapParameter);
    }

    public List<StatisticUser> getStatisticActiveUserByDays(Date date, int num, String query_type, int pageNumber, int pageSize) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("num", num);
        mapParameter.put("query_type", query_type);
        return this.sqlSession.selectList("getStatisticActiveUserByDays",mapParameter, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }
}
