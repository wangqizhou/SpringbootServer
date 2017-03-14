package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *
 * Created by ymzhao on 2016/12/30.
 */
@Component
public class PlayRecordDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public PlayRecordDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectPlayRecordCount() {
    return this.sqlSession.selectOne("selectPlayRecordCount");
    }

    public PlayRecord selectPlayRecordById(int id) {
        return this.sqlSession.selectOne("selectPlayRecordById", id);
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client){
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.selectList("selectPlayRecordsByUserId", mapParameter);
    };

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("client", client);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectPlayRecordsByUserIdOrderBy", map);
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, int pageNumber, int pageSize) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.selectList("selectPlayRecordsByUserId", mapParameter, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<PlayRecord> selectPlayRecordsByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userId", userId);
        map.put("client", client);
        map.put("key", orderBy.getKey());
        map.put("order", orderBy.getOrder());
        return this.sqlSession.selectList("selectPlayRecordsByUserIdOrderBy", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<PlayRecord> selectRecordsBeforeMonth(Date date, int month){
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.selectList("selectRecordsBeforeMonth", mapParameter);
    }

    public PlayRecord selectPlayRecord(int userId, int videoId, String client){
        Map<String, Object> mapParameter = new HashMap<>();
        mapParameter.put("userId", userId);
        mapParameter.put("videoId", videoId);
        mapParameter.put("client", client);

        return this.sqlSession.selectOne("selectPlayRecord", mapParameter);
    }

    public int insertPlayRecord(PlayRecord playRecord) {
        return this.sqlSession.insert("insertPlayRecord", playRecord);
    }

    public int deleteRecordsBeforeMonth (Date date, int month) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.delete("deleteRecordsBeforeMonth", mapParameter);
    }

    public int deletePlayRecord (PlayRecord playRecord) {
        return this.sqlSession.delete("deletePlayRecord", playRecord);
    }

    public int deletePlayRecordById(int id) {
        return this.sqlSession.delete("deletePlayRecordById", id);
    }

    public int deletePlayRecordsByUserId(int userId, String client) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("userId", userId);
        mapParameter.put("client", client);
        return this.sqlSession.delete("deletePlayRecordsByUserId", mapParameter);
    }

    public int deletePlayRecords(List<PlayRecord> playRecordList) {
        return this.sqlSession.delete("deletePlayRecords", playRecordList);
    }

    public int updatePlayRecord(PlayRecord newRecord) {
        return this.sqlSession.update("updatePlayRecord", newRecord);
    }

    public int updateDuration(PlayRecord newRecord) {
        return this.sqlSession.update("updateDuration", newRecord);
    }
}
