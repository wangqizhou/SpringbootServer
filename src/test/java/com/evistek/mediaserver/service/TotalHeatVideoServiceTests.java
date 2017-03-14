package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.StatisticVideo;
import com.evistek.mediaserver.service.impl.TotalHeatVideoServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * Created by ymzhao on 2017/1/12.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-categories-data.sql", "classpath:test-videos-data.sql", "classpath:test-total_heat_videos-data.sql"})
public class TotalHeatVideoServiceTests {
    @Autowired
    private ITotalHeatVideoService totalHeatVideoService;

    @Before
    public void setup() {

    }

    @Test
    public void findStatisticHeatVideo() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        List<StatisticVideo> statisticVideoList = new ArrayList<StatisticVideo>();

        //month
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(44);
        assertThat(statisticVideoList.get(4).getVideoName()).isEqualTo("黑衣人3");
        assertThat(statisticVideoList.get(4).getTotalCount()).isEqualTo(88);
        assertThat(statisticVideoList.get(4).getComCount()).isEqualTo(13);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4, 3, 11);
        assertThat(statisticVideoList.size()).isEqualTo(11);
        assertThat(statisticVideoList.get(4).getVideoName()).isEqualTo("黑衣人3");
        assertThat(statisticVideoList.get(4).getTotalCount()).isEqualTo(88);
        assertThat(statisticVideoList.get(4).getComCount()).isEqualTo(0);

        //过滤掉0
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(12);
        assertThat(statisticVideoList.get(2).getVideoName()).isEqualTo("阿凡达");
        assertThat(statisticVideoList.get(2).getComCount()).isEqualTo(9);
        assertThat(statisticVideoList.get(2).getTotalCount()).isEqualTo(59);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4, 3, 5);
        assertThat(statisticVideoList.size()).isEqualTo(2);
        assertThat(statisticVideoList.get(1).getVideoName()).isEqualTo("美国队长3预告");
        assertThat(statisticVideoList.get(1).getComCount()).isEqualTo(3);
        assertThat(statisticVideoList.get(1).getTotalCount()).isEqualTo(83);

        //week
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(132);
        assertThat(statisticVideoList.get(16).getVideoName()).isEqualTo("黑夜传说4：觉醒");
        assertThat(statisticVideoList.get(16).getComCount()).isEqualTo(4);
        assertThat(statisticVideoList.get(16).getTotalCount()).isEqualTo(92);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4, 3, 50);
        assertThat(statisticVideoList.size()).isEqualTo(32);
        assertThat(statisticVideoList.get(16).getVideoName()).isEqualTo("《极限挑战2》VR首秀");
        assertThat(statisticVideoList.get(16).getComCount()).isEqualTo(0);
        assertThat(statisticVideoList.get(16).getTotalCount()).isEqualTo(65);

        //过滤掉0
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(37);
        assertThat(statisticVideoList.get(17).getVideoName()).isEqualTo("美国队长3预告");
        assertThat(statisticVideoList.get(17).getComCount()).isEqualTo(15);
        assertThat(statisticVideoList.get(17).getTotalCount()).isEqualTo(66);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4, 3, 13);
        assertThat(statisticVideoList.size()).isEqualTo(11);
        assertThat(statisticVideoList.get(7).getVideoName()).isEqualTo("美国队长3预告");
        assertThat(statisticVideoList.get(7).getComCount()).isEqualTo(1);
        assertThat(statisticVideoList.get(7).getTotalCount()).isEqualTo(80);

        //day
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(748);
        assertThat(statisticVideoList.get(2).getVideoName()).isEqualTo("阿凡达");
        assertThat(statisticVideoList.get(2).getComCount()).isEqualTo(1);
        assertThat(statisticVideoList.get(2).getTotalCount()).isEqualTo(51);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideo(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4, 3, 300);
        assertThat(statisticVideoList.size()).isEqualTo(148);
        assertThat(statisticVideoList.get(2).getVideoName()).isEqualTo("挪威.奥斯陆");
        assertThat(statisticVideoList.get(2).getComCount()).isEqualTo(0);
        assertThat(statisticVideoList.get(2).getTotalCount()).isEqualTo(30);

        //过滤掉0
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4);
        assertThat(statisticVideoList.size()).isEqualTo(77);
        assertThat(statisticVideoList.get(3).getVideoName()).isEqualTo("疯狂动物城_片段");
        assertThat(statisticVideoList.get(3).getComCount()).isEqualTo(5);
        //分页
        statisticVideoList = this.totalHeatVideoService.getHeatVideoFilterZero(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4, 3, 13);
        assertThat(statisticVideoList.size()).isEqualTo(13);
        assertThat(statisticVideoList.get(2).getVideoName()).isEqualTo("黑夜传说4：觉醒");
        assertThat(statisticVideoList.get(2).getComCount()).isEqualTo(1);
        assertThat(statisticVideoList.get(2).getTotalCount()).isEqualTo(100);
    }

    @Test
    public void exportStatisticHeatVideo() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //months
        String statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4, TotalHeatVideoServiceImpl.UNFILTER);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(time,'%Y%m') months,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by months, video_id;\"");
        statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_MONTH, date, 4, TotalHeatVideoServiceImpl.FILTER_ZERO);
        assertThat(statement).isEqualTo("\"select * from (select  DATE_FORMAT(time,'%Y%m') months,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by months, video_id) as a where a.com_count > 0;\"");

        //weeks
        statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4, TotalHeatVideoServiceImpl.UNFILTER);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(time,'%Y%u') weeks, date_sub(date_format(time,'%Y%m%d'),INTERVAL WEEKDAY(time) DAY) as starttime,  date_sub(date_format(time,'%Y%m%d'), INTERVAL WEEKDAY(time) - 6 DAY) as endtime,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by weeks, video_id;\"");
        statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_WEEK, date, 4, TotalHeatVideoServiceImpl.FILTER_ZERO);
        assertThat(statement).isEqualTo("\"select * from (select  DATE_FORMAT(time,'%Y%u') weeks, date_sub(date_format(time,'%Y%m%d'),INTERVAL WEEKDAY(time) DAY) as starttime,  date_sub(date_format(time,'%Y%m%d'), INTERVAL WEEKDAY(time) - 6 DAY) as endtime,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by weeks, video_id) as a where a.com_count > 0;\"");

        //days
        statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4, TotalHeatVideoServiceImpl.UNFILTER);
        assertThat(statement).isEqualTo("\"select  DATE_FORMAT(time,'%Y%m%d') days,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by days, video_id;\"");
        statement = totalHeatVideoService.exportXLSByHeatVideo(TotalHeatVideoServiceImpl.GRADE_DAYS, date, 4, TotalHeatVideoServiceImpl.FILTER_ZERO);
        assertThat(statement).isEqualTo("\"select * from (select  DATE_FORMAT(time,'%Y%m%d') days,video_id, video_name, category_id, max(download_count) as total_count, sum(compare_count) as com_count from total_heat_videos where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( time, '%Y%m' ) ) < 4  group by days, video_id) as a where a.com_count > 0;\"");
    }
}
