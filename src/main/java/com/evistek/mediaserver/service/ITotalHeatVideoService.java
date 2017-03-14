package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.StatisticVideo;

import java.util.Date;
import java.util.List;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */
public interface ITotalHeatVideoService {
    List<StatisticVideo> getHeatVideo(int query_grade, Date date, int num);

    List<StatisticVideo> getHeatVideo(int query_grade, Date date, int num, int pageNumber, int pageSize);

    List<StatisticVideo> getHeatVideoFilterZero(int query_grade, Date date, int num);

    List<StatisticVideo> getHeatVideoFilterZero(int query_grade, Date date, int num, int pageNumber, int pageSize);

    String exportXLSByHeatVideo(int query_grade, Date date, int num, int isFilter);
}
