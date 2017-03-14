package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.StatisticVideo;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */
@Component
public class TotalHeatVideoDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public TotalHeatVideoDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public List<StatisticVideo> selectHeatVideoByMonth(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByMonth", map);
    }

    public List<StatisticVideo> selectHeatVideoByMonth(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByMonth", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticVideo> selectHeatVideoByWeek(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByWeek", map);
    }

    public List<StatisticVideo> selectHeatVideoByWeek(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByWeek", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticVideo> selectHeatVideoByDay(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByDay", map);
    }

    public List<StatisticVideo> selectHeatVideoByDay(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByDay", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticVideo> selectHeatVideoByMonthFilterZero(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByMonthFilterZero", map);
    }

    public List<StatisticVideo> selectHeatVideoByMonthFilterZero(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByMonthFilterZero", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticVideo> selectHeatVideoByWeekFilterZero(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByWeekFilterZero", map);
    }

    public List<StatisticVideo> selectHeatVideoByWeekFilterZero(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByWeekFilterZero", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<StatisticVideo> selectHeatVideoByDayFilterZero(Date date, int num) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByDayFilterZero", map);
    }

    public List<StatisticVideo> selectHeatVideoByDayFilterZero(Date date, int num, int pageNumber, int pageSize) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("date", date);
        map.put("num", num);
        return this.sqlSession.selectList("selectHeatVideoByDayFilterZero", map, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }
}
