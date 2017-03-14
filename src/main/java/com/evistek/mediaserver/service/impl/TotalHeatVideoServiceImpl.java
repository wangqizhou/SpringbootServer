package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.TotalHeatVideoDao;
import com.evistek.mediaserver.entity.StatisticVideo;
import com.evistek.mediaserver.service.ITotalHeatVideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */
@Service
public class TotalHeatVideoServiceImpl implements ITotalHeatVideoService{
    private final TotalHeatVideoDao totalHeatVideoDao;
    public static final int GRADE_MONTH = 0;
    public static final int GRADE_WEEK = 1;
    public static final int GRADE_DAYS = 2;
    public static final int FILTER_ZERO = 3;
    public static final int UNFILTER = 4;
    @Autowired
    public TotalHeatVideoServiceImpl (TotalHeatVideoDao totalHeatVideoDao) {
        this.totalHeatVideoDao = totalHeatVideoDao;
    }
    @Override
    public List<StatisticVideo> getHeatVideo(int query_grade, Date date, int num) {
        List<StatisticVideo> heatVideoList = new ArrayList<StatisticVideo>();
        switch (query_grade) {
            case TotalPlayRecordServiceImpl.GRADE_MONTH:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByMonth(date, num);
                break;
            case TotalPlayRecordServiceImpl.GRADE_WEEK:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByWeek(date, num);
                break;
            case TotalPlayRecordServiceImpl.GRADE_DAYS:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByDay(date, num);
                break;
        }
        if (!heatVideoList.isEmpty()) {
            return heatVideoList;
        }
        return null;
    }

    @Override
    public List<StatisticVideo> getHeatVideo(int query_grade, Date date, int num, int pageNumber, int pageSize) {
        List<StatisticVideo> heatVideoList = new ArrayList<StatisticVideo>();
        switch (query_grade) {
            case TotalPlayRecordServiceImpl.GRADE_MONTH:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByMonth(date, num, pageNumber, pageSize);
                break;
            case TotalPlayRecordServiceImpl.GRADE_WEEK:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByWeek(date, num, pageNumber, pageSize);
                break;
            case TotalPlayRecordServiceImpl.GRADE_DAYS:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByDay(date, num, pageNumber, pageSize);
                break;
        }
        if (!heatVideoList.isEmpty()) {
            return heatVideoList;
        }
        return null;
    }

    @Override
    public List<StatisticVideo> getHeatVideoFilterZero(int query_grade, Date date, int num) {
        List<StatisticVideo> heatVideoList = new ArrayList<StatisticVideo>();
        switch (query_grade) {
            case TotalPlayRecordServiceImpl.GRADE_MONTH:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByMonthFilterZero(date, num);
                break;
            case TotalPlayRecordServiceImpl.GRADE_WEEK:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByWeekFilterZero(date, num);
                break;
            case TotalPlayRecordServiceImpl.GRADE_DAYS:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByDayFilterZero(date, num);
                break;
        }
        if (!heatVideoList.isEmpty()) {
            return heatVideoList;
        }
        return null;
    }

    @Override
    public List<StatisticVideo> getHeatVideoFilterZero(int query_grade, Date date, int num, int pageNumber, int pageSize) {
        List<StatisticVideo> heatVideoList = new ArrayList<StatisticVideo>();
        switch (query_grade) {
            case TotalPlayRecordServiceImpl.GRADE_MONTH:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByMonthFilterZero(date, num, pageNumber, pageSize);
                break;
            case TotalPlayRecordServiceImpl.GRADE_WEEK:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByWeekFilterZero(date, num, pageNumber, pageSize);
                break;
            case TotalPlayRecordServiceImpl.GRADE_DAYS:
                heatVideoList = totalHeatVideoDao.selectHeatVideoByDayFilterZero(date, num, pageNumber, pageSize);
                break;
        }
        if (!heatVideoList.isEmpty()) {
            return heatVideoList;
        }
        return null;
    }

    @Override
    public String exportXLSByHeatVideo(int query_grade, Date date, int num, int isFilter) {
        String dateFormat = "%Y%m";
        String queryKey = "";
        String weeksFields = "";
        String filterStart = "";
        String filterEnd = "";
        switch (query_grade) {
            case GRADE_MONTH:
                dateFormat = "%Y%m";
                queryKey = "months";
                break;
            case GRADE_WEEK:
                dateFormat = "%Y%u";
                queryKey = "weeks";
                weeksFields = ", date_sub(date_format(time,'%Y%m%d'),INTERVAL WEEKDAY(time) DAY) as starttime,  date_sub(date_format(time,"
                        + "'%Y%m%d'), INTERVAL WEEKDAY(time) - 6 DAY) as endtime";
                break;
            case GRADE_DAYS:
                dateFormat = "%Y%m%d";
                queryKey = "days";
                break;
        }
        if (isFilter == FILTER_ZERO) {
            filterStart = "select * from (";
            filterEnd = ") as a where a.com_count > 0";
        }
        String queryDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("queryDate" + queryDate);
        String statement = "\"" + filterStart + "select  DATE_FORMAT(time,'" + dateFormat + "') " + queryKey + weeksFields
                + ",video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from"
                + " total_heat_videos where PERIOD_DIFF(date_format('" + queryDate
                + "' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('" + queryDate + "' , "
                + "'%Y%m' ) , date_format( time, '%Y%m' ) ) < " + num + "  group by " + queryKey +", video_id" + filterEnd + ";\"";
        return statement;
    }
}
