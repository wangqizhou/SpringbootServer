package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.service.impl.TotalPlayRecordServiceImpl;
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
 * Created by ymzhao on 2017/1/5.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql", "classpath:test-total_play_records-data.sql"})
public class TotalPlayRecordServiceTests {
    @Autowired
    private ITotalPlayRecordService totalPlayRecordService;

    @Before
    public void setup() {

    }

    @Test
    public void findPlayRecord() {
        List<PlayRecord> totalPlayRecords = this.totalPlayRecordService.findAllPlayRecords();
        assertThat(totalPlayRecords.size()).isEqualTo(22);

        List<PlayRecord> totalPlayRecords1  = this.totalPlayRecordService.findAllPlayRecords(new OrderBy(ITotalPlayRecordService.ORDER_DURATION, OrderBy.DESC));
        assertThat(totalPlayRecords1).isNotNull();
        assertThat(totalPlayRecords1.get(0).getVideoName()).isEqualTo("疯狂动物城_片段");

        List<PlayRecord> totalPlayRecords2 = this.totalPlayRecordService.findAllPlayRecords(3, 8);
        assertThat(totalPlayRecords2.size()).isEqualTo(6);

        List<PlayRecord> totalPlayRecords3 = this.totalPlayRecordService.findAllPlayRecords(3, 8, new OrderBy(ITotalPlayRecordService.ORDER_DURATION, OrderBy.DESC));
        assertThat(totalPlayRecords3.size()).isEqualTo(6);
        assertThat(totalPlayRecords3.get(3).getVideoName()).isEqualTo("少女时代.Genie.mp4");


        PlayRecord playRecord= this.totalPlayRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");
        assertThat(playRecord.getId()).isEqualTo(1);
        assertThat(playRecord.getClientVersion()).isEqualTo("2.0.0");
        assertThat(playRecord.getDuration()).isEqualTo(143023);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(playRecord.getStartTime()).isEqualTo(formatter.parse("2016-10-21 15:27:24"));
            assertThat(playRecord.getEndTime()).isEqualTo(formatter.parse("2016-10-21 15:29:47"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<PlayRecord> playRecordList = this.totalPlayRecordService.findPlayRecordByUserId(5,"com.evistek.gallery");
        assertThat(playRecordList).isNotNull();
        assertThat(playRecordList.size()).isEqualTo(8);

        List<PlayRecord> playRecordList1 = this.totalPlayRecordService.findPlayRecordByUserId(5,"com.evistek.gallery", new OrderBy(ITotalPlayRecordService.ORDER_DURATION, OrderBy.DESC));
        assertThat(playRecordList1).isNotNull();
        assertThat(playRecordList1.size()).isEqualTo(8);
        assertThat(playRecordList1.get(0).getId()).isEqualTo(11);

        List<PlayRecord> playRecordList2 = this.totalPlayRecordService.findPlayRecordByUserId(5,"com.evistek.gallery", 3, 3);
        assertThat(playRecordList2).isNotNull();
        assertThat(playRecordList2.size()).isEqualTo(2);

        List<PlayRecord> playRecordList3 = this.totalPlayRecordService.findPlayRecordByUserId(5, "com.evistek.gallery", 3,3, new OrderBy(IPlayRecordService.ORDER_DURATION, OrderBy.DESC));
        assertThat(playRecordList2).isNotNull();
        assertThat(playRecordList2.size()).isEqualTo(2);
        assertThat(playRecordList2.get(0).getVideoName()).isEqualTo("冰川时代4：大陆漂移");

        // 查询天(有无地区)
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2016-10-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
         List<StatisticUser> userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 1);
        assertThat(userList.size()).isEqualTo(6);
        assertThat(userList.get(2).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getLocation()).isEqualTo("上海 虹口");
        assertThat(userList.get(3).getCount()).isEqualTo(2);
        userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 1);
        assertThat(userList.size()).isEqualTo(6);
        assertThat(userList.get(2).getCount()).isEqualTo(2);
        assertThat(userList.get(3).getCount()).isEqualTo(2);

        List<StatisticUser> userList1 = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 1, 2, 3);
        assertThat(userList1.size()).isEqualTo(3);
        assertThat(userList1.get(2).getCount()).isEqualTo(1);
        assertThat(userList1.get(1).getLocation()).isEqualTo("上海 虹口");
        assertThat(userList1.get(2).getCount()).isEqualTo(1);
        userList1= this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 1, 2, 3);
        assertThat(userList1.size()).isEqualTo(3);
        assertThat(userList1.get(2).getCount()).isEqualTo(1);
        assertThat(userList1.get(0).getCount()).isEqualTo(2);



        //查询周(有无地区)
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(userList.size()).isEqualTo(5);
        assertThat(userList.get(0).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getCount()).isEqualTo(1);
        assertThat(userList.get(2).getGrade()).isEqualTo("201646");
        assertThat(userList.get(3).getLocation()).isEqualTo("上海 浦东新区");
        userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(userList.size()).isEqualTo(5);
        assertThat(userList.get(0).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getCount()).isEqualTo(1);
        assertThat(userList.get(2).getGrade()).isEqualTo("201646");
        assertThat(userList.get(4).getGrade()).isEqualTo("201701");

        List<StatisticUser> userList2 = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4, 2, 3);
        assertThat(userList2.size()).isEqualTo(2);
        assertThat(userList2.get(0).getCount()).isEqualTo(1);
        assertThat(userList2.get(1).getCount()).isEqualTo(1);
        assertThat(userList2.get(0).getGrade()).isEqualTo("201649");
        assertThat(userList2.get(0).getLocation()).isEqualTo("上海 浦东新区");
        userList2 = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4, 1, 3);
        assertThat(userList2.size()).isEqualTo(3);
        assertThat(userList2.get(0).getCount()).isEqualTo(2);
        assertThat(userList2.get(1).getCount()).isEqualTo(1);
        assertThat(userList2.get(0).getGrade()).isEqualTo("201642");
        assertThat(userList2.get(1).getGrade()).isEqualTo("201643");

        //查询月(有无地区)
        userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(userList.size()).isEqualTo(4);
        assertThat(userList.get(0).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getCount()).isEqualTo(1);
        assertThat(userList.get(2).getGrade()).isEqualTo("201612");
        assertThat(userList.get(2).getLocation()).isEqualTo("上海 浦东新区");
        userList = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(userList.size()).isEqualTo(4);
        assertThat(userList.get(0).getCount()).isEqualTo(2);
        assertThat(userList.get(1).getCount()).isEqualTo(1);
        assertThat(userList.get(2).getGrade()).isEqualTo("201612");
        assertThat(userList.get(3).getGrade()).isEqualTo("201701");

        List<StatisticUser> userList3 = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4, 2, 3);
        assertThat(userList3.size()).isEqualTo(1);
        assertThat(userList3.get(0).getCount()).isEqualTo(1);
        assertThat(userList3.get(0).getGrade()).isEqualTo("201701");
        userList3 = this.totalPlayRecordService.getStatisticActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4, 1, 3);
        assertThat(userList3.size()).isEqualTo(3);
        assertThat(userList3.get(0).getCount()).isEqualTo(2);
        assertThat(userList3.get(1).getCount()).isEqualTo(1);
        assertThat(userList3.get(2).getGrade()).isEqualTo("201612");
        assertThat(userList3.get(0).getGrade()).isEqualTo("201610");
    }

    @Test
    public void exportStatisticActiveUser() {
        Date date = new Date();
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-01-05");
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //months
        String statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select months, location, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%m') months,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, months)as a group by months, location;\"");
        statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_MONTH, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select months, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%m') months,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, months)as a group by months;\"");

        //weeks
        statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select weeks,starttime, endtime, location, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%u') weeks, date_sub(date_format(start_time,'%Y%m%d'), INTERVAL WEEKDAY(start_time) DAY) as starttime, date_sub(date_format(start_time,'%Y%m%d'),INTERVAL WEEKDAY(start_time) - 6 DAY) as endtime,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, weeks)as a group by weeks, location;\"");
        statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_WEEK, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select weeks,starttime, endtime, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%u') weeks, date_sub(date_format(start_time,'%Y%m%d'), INTERVAL WEEKDAY(start_time) DAY) as starttime, date_sub(date_format(start_time,'%Y%m%d'),INTERVAL WEEKDAY(start_time) - 6 DAY) as endtime,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, weeks)as a group by weeks;\"");

        //days
        statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_LOCATION, date, 4);
        assertThat(statement).isEqualTo("\"select days, location, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%m%d') days,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, days)as a group by days, location;\"");
        statement = totalPlayRecordService.exportXLSByActiveUser(TotalPlayRecordServiceImpl.GRADE_DAYS, TotalPlayRecordServiceImpl.QUERY_USUALLY, date, 4);
        assertThat(statement).isEqualTo("\"select days, count(*) as count from (select  DATE_FORMAT(start_time,'%Y%m%d') days,(select location from users where id = total_play_records.user_id) as location from total_play_records where PERIOD_DIFF(date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('2017-01-05' , '%Y%m' ) , date_format( start_time, '%Y%m' ) ) < 4  group by user_id, days)as a group by days;\"");
    }

    @Test
    public void addPlayRecord() {
        int result = this.totalPlayRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(22);

        PlayRecord playRecord = new PlayRecord();
        playRecord.setUserId(3);
        playRecord.setVideoId(25);
        playRecord.setStartTime(new Date());
        playRecord.setClient("com.evistek.gallery");
        playRecord.setClientVersion("2.0.1");
        playRecord.setVideoName("testPlayRecord");

        int addResult = this.totalPlayRecordService.addPlayRecord(playRecord);
        assertThat(addResult).isEqualTo(1);

        int addDelMonthsResult = this.totalPlayRecordService.addPlayRecord(playRecord, 1);

        int addAfterResult = this.totalPlayRecordService.findPlayRecordNumber();
        assertThat(addAfterResult).isEqualTo(1);
        PlayRecord playRecord1 = this.totalPlayRecordService.findPlayRecord(3, 25, "com.evistek.gallery");
        assertThat(playRecord1).isNotNull();
        assertThat(playRecord1.getVideoName()).isEqualTo("testPlayRecord");

    }

    @Test
    public void deletePlayRecord() {
        int result = this.totalPlayRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(22);

        PlayRecord playRecord= this.totalPlayRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");

        result = this.totalPlayRecordService.deletePlayRecord(playRecord);
        assertThat(result).isEqualTo(1);
        PlayRecord playRecord1 = this.totalPlayRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord1).isNull();
        result = this.totalPlayRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(21);

        result = this.totalPlayRecordService.deletePlayRecordsByUserId(5, "com.evistek.gallery");
        assertThat(result).isEqualTo(7);
        List<PlayRecord> playRecord2 = this.totalPlayRecordService.findPlayRecordByUserId(5, "com.evistek.gallery");
        assertThat(playRecord2).isEmpty();
        result = this.totalPlayRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(14);

        PlayRecord playRecord3 = new PlayRecord();
        playRecord3.setId(3);
        PlayRecord playRecord4 = new PlayRecord();
        playRecord4.setId(4);
        PlayRecord playRecord5 = new PlayRecord();
        playRecord5.setId(5);

        List<PlayRecord> deleteList = new ArrayList<>();
        deleteList.add(playRecord3);
        deleteList.add(playRecord4);
        deleteList.add(playRecord5);
        result = this.totalPlayRecordService.deletePlayRecordBatch(deleteList);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void updatePlayRecord() {
        PlayRecord playRecord= this.totalPlayRecordService.findPlayRecord(3, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");
        assertThat(playRecord.getId()).isEqualTo(24);
        assertThat(playRecord.getClientVersion()).isEqualTo("2.0.1");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(playRecord.getStartTime()).isEqualTo(formatter.parse("2017-01-06 11:26:24"));
            assertThat(playRecord.getEndTime()).isEqualTo(formatter.parse("2017-01-06 11:28:47"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        playRecord.setVideoName("testNewName");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int result = this.totalPlayRecordService.updatePlayRecord(playRecord);
        assertThat(result).isEqualTo(1);

        PlayRecord playRecord1 = this.totalPlayRecordService.findPlayRecord(3, 25 ,"com.evistek.gallery");;
        assertThat(playRecord1).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("testNewName");
    }
}
