package com.evistek.mediaserver.service;

/**
 *
 * Created by ymzhao on 2016/12/30.
 */

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql", "classpath:test-play-records-data.sql"})
public class PlayRecordServiceTests
{
    @Autowired
    private IPlayRecordService playRecordService;

    @Before
    public void setup() {

    }

    @Test
    public void findPlayRecord() {
        PlayRecord playRecord= this.playRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");
        assertThat(playRecord.getId()).isEqualTo(1);
        assertThat(playRecord.getClientVersion()).isEqualTo("2.0.0");
        assertThat(playRecord.getDuration()).isEqualTo(143023);
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(playRecord.getStartTime()).isEqualTo(formatter.parse("2016-10-20 15:27:24"));
            assertThat(playRecord.getEndTime()).isEqualTo(formatter.parse("2016-10-20 15:29:47"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<PlayRecord> playRecordList = this.playRecordService.findPlayRecordByUserId(5,"com.evistek.gallery");
        assertThat(playRecordList).isNotNull();
        assertThat(playRecordList.size()).isEqualTo(8);

        List<PlayRecord> playRecordList1 = this.playRecordService.findPlayRecordByUserId(5,"com.evistek.gallery", new OrderBy(IPlayRecordService.ORDER_START_TIME, OrderBy.DESC));
        assertThat(playRecordList1).isNotNull();
        assertThat(playRecordList1.size()).isEqualTo(8);
        assertThat(playRecordList1.get(0).getId()).isEqualTo(13);

        List<PlayRecord> playRecordList2 = this.playRecordService.findPlayRecordByUserId(5,"com.evistek.gallery", 3, 3);
        assertThat(playRecordList2).isNotNull();
        assertThat(playRecordList2.size()).isEqualTo(2);

        List<PlayRecord> playRecordList3 = this.playRecordService.findPlayRecordByUserId(5, "com.evistek.gallery", 3,3, new OrderBy(IPlayRecordService.ORDER_DURATION, OrderBy.DESC));
        }

    @Test
    public void addPlayRecord() {
        int result = this.playRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(22);

        PlayRecord playRecord = new PlayRecord();
        playRecord.setUserId(3);
        playRecord.setVideoId(25);
        playRecord.setStartTime(new Date());
        playRecord.setClient("com.evistek.gallery");
        playRecord.setClientVersion("2.0.1");
        playRecord.setVideoName("testPlayRecord");

        PlayRecord addResult = this.playRecordService.addPlayRecord(playRecord);

        assertThat(addResult.getUserId()).isEqualTo(3);

        int addDelMonthsResult = this.playRecordService.addPlayRecord(playRecord, 1);

        int addAfterResult = this.playRecordService.findPlayRecordNumber();
        assertThat(addAfterResult).isEqualTo(1);
        PlayRecord playRecord1 = this.playRecordService.findPlayRecord(3, 25, "com.evistek.gallery");
        assertThat(playRecord1).isNotNull();
        assertThat(playRecord1.getVideoName()).isEqualTo("testPlayRecord");

    }

    @Test
    public void deletePlayRecord() {
        int result = this.playRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(22);

        PlayRecord record = new PlayRecord();
        record.setId(3);
        assertThat(this.playRecordService.deletePlayRecord(record));

        PlayRecord playRecord= this.playRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");

        result = this.playRecordService.deletePlayRecord(playRecord);
        assertThat(result).isEqualTo(1);
        PlayRecord playRecord1 = this.playRecordService.findPlayRecord(5, 25 ,"com.evistek.gallery");
        assertThat(playRecord1).isNull();
        result = this.playRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(20);

        result = this.playRecordService.deletePlayRecordsByUserId(5, "com.evistek.gallery");
        assertThat(result).isEqualTo(7);
        List<PlayRecord> playRecord2 = this.playRecordService.findPlayRecordByUserId(5, "com.evistek.gallery");
        assertThat(playRecord2).isEmpty();
        result = this.playRecordService.findPlayRecordNumber();
        assertThat(result).isEqualTo(13);

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
        result = this.playRecordService.deletePlayRecordBatch(deleteList);
        assertThat(result).isEqualTo(2);
    }

    @Test
    public void updatePlayRecord() {
        PlayRecord playRecord= this.playRecordService.findPlayRecord(3, 25 ,"com.evistek.gallery");
        assertThat(playRecord).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("美国队长3预告");
        assertThat(playRecord.getId()).isEqualTo(24);
        assertThat(playRecord.getClientVersion()).isEqualTo("2.0.1");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(playRecord.getStartTime()).isEqualTo(formatter.parse("2017-01-06 11:27:24"));
            assertThat(playRecord.getEndTime()).isEqualTo(formatter.parse("2017-01-06 11:29:47"));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        playRecord.setVideoName("testNewName");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        int result = this.playRecordService.updatePlayRecord(playRecord);
        assertThat(result).isEqualTo(1);

        PlayRecord playRecord1 = this.playRecordService.findPlayRecord(3, 25 ,"com.evistek.gallery");;
        assertThat(playRecord1).isNotNull();
        assertThat(playRecord.getVideoName()).isEqualTo("testNewName");
    }
}
