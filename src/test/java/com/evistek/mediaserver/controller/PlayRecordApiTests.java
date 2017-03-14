package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.model.PlayRecordModel;
import com.evistek.mediaserver.service.IPlayRecordService;
import com.evistek.mediaserver.service.ITotalPlayRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 *
 * Created by ymzhao on 2017/1/18.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-users-data.sql", "classpath:test-videos-data.sql", "classpath:test-play-records-data.sql", "classpath:test-total_play_records-data.sql"})
public class PlayRecordApiTests {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private IPlayRecordService playRecordService;
    @Autowired
    private ITotalPlayRecordService totalPlayRecordService;
    private MockMvc mockMvc;
    private final String client = "com.evistek.gallery";

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void getPlayRecordsByUserId() throws Exception {
        String result = this.mockMvc
                .perform(get("/api/v2/play_records")
                .param("user_id", "5")
                .param("client", client))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PlayRecordModel> playRecordList = JSON.parseArray(result, PlayRecordModel.class);
        assertThat(playRecordList).isNotNull();
        assertThat(playRecordList.size()).isEqualTo(8);
        assertThat(playRecordList.get(0).getVideo().getName()).isEqualTo("飞屋环游记");

        String result1 = this.mockMvc
                .perform(get("/api/v2/play_records")
                        .param("user_id", "5")
                        .param("client", client)
                        .param("page", "2")
                        .param("page_size", "3"))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        List<PlayRecordModel> playRecordList1 = JSON.parseArray(result1, PlayRecordModel.class);
        assertThat(playRecordList1).isNotNull();
        assertThat(playRecordList1.size()).isEqualTo(3);
        assertThat(playRecordList1.get(0).getVideo().getName()).isEqualTo("少女时代.Genie.mp4");

        this.mockMvc
                .perform(get("/api/v2/play_records").param("user_id", "-1").param("client", client))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for user_id")));

        this.mockMvc
                .perform(get("/api/v2/play_records")
                        .param("user_id", "5")
                        .param("client", client)
                        .param("page", "-2")
                        .param("page_size", "-3"))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content().string(containsString("invalid value for page or page_size")));
    }

    @Test
    public void addPlayRecord() throws Exception {
        PlayRecord playRecord = new PlayRecord();
        playRecord.setUserId(1);
        playRecord.setVideoId(25);
        playRecord.setClient(client);

        Date startTime =  new Date((new Date()).getTime() - 3000);
        playRecord.setStartTime(new Date());
        playRecord.setClientVersion("2.0.1");
        playRecord.setVideoName("美国队长3预告1");

        String result = this.mockMvc
                .perform(post("/api/v2/play_records")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(playRecord)))
                .andDo(print())
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        PlayRecord playRecord1 = JSON.parseObject(result, PlayRecord.class);
        assertThat(playRecord1).isNotNull();
        assertThat(playRecord1.getId()).isEqualTo(25);
        assertThat(playRecord1.getVideoName()).isEqualTo("美国队长3预告1");

        PlayRecord record= this.totalPlayRecordService.findPlayRecord(1, 25 ,client);
        assertThat(record).isNotNull();
        assertThat(record.getVideoName()).isEqualTo("美国队长3预告1");

        Thread.sleep(3000);
        //updateDuration
        playRecord.setId(25);
        this.mockMvc
                .perform(put("/api/v2/play_records")
                        .contentType(MediaType.APPLICATION_JSON_UTF8)
                        .content(JSON.toJSONString(playRecord)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("success to update playDuration information")));
    }


    @Test
    public void deletePlayRecord() throws Exception {
        List<PlayRecord> playRecordList = this.playRecordService.findPlayRecordByUserId(5, client,
                new OrderBy(IPlayRecordService.ORDER_END_TIME, OrderBy.DESC));
        assertThat(playRecordList.size()).isEqualTo(8);
        assertThat(playRecordList.get(0).getId()).isEqualTo(13);
        assertThat(playRecordList.get(0).getVideoName()).isEqualTo("飞屋环游记");

        this.mockMvc.perform(delete("/api/v2/play_records/id/13"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("success to delete playRecord with id: " + playRecordList.get(0).getId())));

        PlayRecord playRecord1 = this.playRecordService.findPlayRecord(playRecordList.get(0).getUserId(), playRecordList.get(0).getVideoId(), client);
        assertThat(playRecord1).isNull();
    }

    @Test
    public void deletePlayRecords() throws Exception {
        PlayRecord playRecord1 = this.playRecordService.findPlayRecordById(1);
        PlayRecord playRecord2 = this.playRecordService.findPlayRecordById(2);
        PlayRecord playRecord3 = this.playRecordService.findPlayRecordById(3);
        assertThat(playRecord1.getVideoId()).isEqualTo(25);
        assertThat(playRecord2.getVideoId()).isEqualTo(8);
        assertThat(playRecord3.getVideoId()).isEqualTo(25);

        List<PlayRecord> deleteList = new ArrayList<>();
        deleteList.add(playRecord1);
        deleteList.add(playRecord2);
        deleteList.add(playRecord3);

        this.mockMvc.perform(delete("/api/v2/play_records").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSON.toJSONString(deleteList)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString("success to delete " + deleteList.size() + " playeRecords with userId: " + deleteList.get(0).getUserId())));

        PlayRecord playRecord4 = this.playRecordService.findPlayRecordById(deleteList.get(0).getId());
        assertThat(playRecord4).isNull();
    }

    @Test
    public void deletePlayRecordsByUserId() throws Exception {
        List<PlayRecord> playRecordList = this.playRecordService.findPlayRecordByUserId(16, client,
                new OrderBy(IPlayRecordService.ORDER_END_TIME, OrderBy.DESC));
        assertThat(playRecordList.size()).isEqualTo(12);

        this.mockMvc.perform(delete("/api/v2/play_records/user_id/16").param("client", client))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(containsString(
                                "success to delete " + playRecordList.size() + " playRecord(s) with user_id: "
                                        + 16)));

        List<PlayRecord> playRecordList1 = this.playRecordService.findPlayRecordByUserId(16, client,
                new OrderBy(IPlayRecordService.ORDER_END_TIME, OrderBy.DESC));
        assertThat(playRecordList1.size()).isEqualTo(0);


        this.mockMvc.perform(delete("/api/v2/play_records/user_id/-2").param("client", client))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .string(containsString("invalid value for user_id")));


        this.mockMvc.perform(delete("/api/v2/play_records/user_id/16").param("client", client))
                .andDo(print())
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .string(containsString(
                                "not found playRecord with user_id: " + 16)));
    }
}
