package com.evistek.mediaserver.service;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Video;
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
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/25.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-admins-authorities-data.sql", "classpath:test-videos-data.sql"})
public class VideoServiceTests {
    @Autowired
    private IVideoService videoService;

    @Before
    public void setup() {

    }

    @Test
    public void findVideo() {
        int result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(25);

        Video video1 = this.videoService.findVideoById(1);
        assertThat(video1).isNotNull();
        assertThat(video1.getName()).isEqualTo("冰川时代4：大陆漂移");
        assertThat(video1.getCategoryId()).isEqualTo(1);
        assertThat(video1.getCategoryName()).isEqualTo("电影");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(video1.getCreateTime()).isEqualTo(formatter.parse("2016-04-28 17:44:05"));
            assertThat(video1.getUpdateTime()).isEqualTo(formatter.parse("2016-04-29 17:44:09"));
            assertThat(video1.getReleaseTime()).isEqualTo(formatter.parse("2012-07-27 00:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(video1.getFormat()).isEqualTo("mp4");
        assertThat(video1.getWidth()).isEqualTo(1920);
        assertThat(video1.getHeight()).isEqualTo(1080);
        assertThat(video1.getSize()).isEqualTo(65051977);
        assertThat(video1.getDuration()).isEqualTo(128000);
        assertThat(video1.getActors()).isEqualTo("阿兹·安萨里/乔·贝哈/克里斯托弗·坎贝尔/阿兰·夏巴");
        assertThat(video1.getLocation()).isEqualTo("美国");
        assertThat(video1.getUrl()).isEqualTo("http://120.26.138.149:8080/video/电影/冰川时代4：大陆漂移.Ice.Age.Continental.Drift.2012.mp4");
        assertThat(video1.getLandscapeCoverUrl()).isNull();
        assertThat(video1.getPortraitCoverUrl()).isNull();
        assertThat(video1.getPreview1Url()).isEqualTo("http://120.26.138.149:8080/video/电影/冰川时代4：大陆漂移.Ice.Age.Continental.Drift.2012_pre1.jpg");
        assertThat(video1.getPreview2Url()).isNull();
        assertThat(video1.getPreview3Url()).isNull();
        assertThat(video1.getBrief()).isNull();
        assertThat(video1.getIntroduction()).isNotNull();
        assertThat(video1.getOwnerId()).isEqualTo(2);
        assertThat(video1.isAudit()).isTrue();
        assertThat(video1.getDownloadCount()).isEqualTo(59);

        Video video2 = this.videoService.findVideoByUrl("http://120.26.138.149:8080/video/MV/少女时代.Genie.mp4");
        assertThat(video2).isNotNull();
        assertThat(video2.getId()).isEqualTo(8);

        List<Video> videoList = this.videoService.findVideos();
        assertThat(videoList).isNotNull();
        assertThat(videoList.size()).isEqualTo(25);

        List<Video> videoList1 = this.videoService.findVideos(3, 10);
        assertThat(videoList1).isNotNull();
        assertThat(videoList1.size()).isEqualTo(5);

        List<Video> videoList2 = this.videoService.findVideosByAudit(true);
        assertThat(videoList2.size()).isEqualTo(25);
        List<Video> videoList3 = this.videoService.findVideosByAudit(false);
        assertThat(videoList3.size()).isEqualTo(0);
        List<Video> videoList4 = this.videoService.findVideosByAudit(true, 4, 10);
        assertThat(videoList4.size()).isEqualTo(0);

        List<Video> videoList5 = this.videoService.findVideosByCategoryId(2);
        assertThat(videoList5.size()).isEqualTo(3);
        assertThat(videoList5.get(0).getCategoryName()).isEqualTo("MV");

        List<Video> videoList6 = this.videoService.findVideosByCategoryId(2, 1, 2);
        assertThat(videoList6.size()).isEqualTo(2);

        List<Video> videoList7 = this.videoService.findVideosByCategoryName("MV");
        assertThat(videoList7.size()).isEqualTo(3);
        List<Video> videoList8 = this.videoService.findVideosByCategoryName("VR全景");
        assertThat(videoList8.size()).isEqualTo(13);

        List<Video> videoList9 = this.videoService.findVideosByCategoryName("VR全景", 2, 10);
        assertThat(videoList9.size()).isEqualTo(3);
    }

    @Test
    public void findVideoOrderBy() {
        List<Video> list = this.videoService.findVideos(new OrderBy(IVideoService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(25);
        assertThat(list.get(0).getId()).isEqualTo(2);

        List<Video> list1 = this.videoService.findVideos(2, 10,
                                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list1).isNotNull();
        assertThat(list1.size()).isEqualTo(10);
        assertThat(list1.get(0).getId()).isEqualTo(14);

        List<Video> list2 = this.videoService.findVideosByCategoryId(2,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list2.size()).isEqualTo(3);
        assertThat(list2.get(0).getId()).isEqualTo(7);

        List<Video> list3 = this.videoService.findVideosByCategoryId(2, 1, 2,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list3.size()).isEqualTo(2);
        assertThat(list3.get(0).getId()).isEqualTo(7);

        List<Video> list4 = this.videoService.findVideosByCategoryName("MV",
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list4.size()).isEqualTo(3);
        assertThat(list4.get(1).getId()).isEqualTo(6);

        List<Video> list5 = this.videoService.findVideosByCategoryName("VR全景", 2, 10,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list5.size()).isEqualTo(3);
        assertThat(list5.get(0).getId()).isEqualTo(15);

        List<Video> list6 = this.videoService.findVideosByAudit(true,
                            new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list6.size()).isEqualTo(25);
        assertThat(list6.get(24).getId()).isEqualTo(4);

        List<Video> list7 = this.videoService.findVideosByAudit(true, 3, 10,
                            new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC));
        assertThat(list7.size()).isEqualTo(5);
        assertThat(list7.get(0).getId()).isEqualTo(5);

        List<Video> list8 = this.videoService.findVideosByCategoryId(2, 1, 2,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), true);
        assertThat(list8.size()).isEqualTo(2);
        assertThat(list8.get(0).getId()).isEqualTo(7);

        List<Video> list9 = this.videoService.findVideosByCategoryId(2, 1, 2,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), false);
        assertThat(list9.size()).isEqualTo(0);

        List<Video> list10 = this.videoService.findVideosByCategoryName("VR全景", 2, 10,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), true);
        assertThat(list10.size()).isEqualTo(3);
        assertThat(list10.get(0).getId()).isEqualTo(15);

        List<Video> list11 = this.videoService.findVideosByCategoryName("VR全景", 1, 10,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), false);
        assertThat(list11.size()).isEqualTo(0);
    }

    @Test
    public void addVideo() {
        int result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(25);

        Video video = new Video();
        video.setCategoryId(2);
        video.setCategoryName("MV");
        video.setName("testMV");
        video.setActors("test,actorOne, actorTwo");
        video.setAudit(false);
        video.setCreateTime(new Date());
        video.setDuration(5 * 60 * 1000);
        video.setFormat("mp4");
        video.setWidth(1920);
        video.setHeight(1080);
        video.setSize(100 * 1024 * 1024);
        video.setBrief("a brief introduction");
        video.setDownloadCount(1);
        video.setUrl("http://120.26.138.149:8080/video/MV/testMV.mp4");
        video.setPreview1Url("http://120.26.138.149:8080/video/MV/testMV.jpg");
        video.setOwnerId(1);

        Video video1 = this.videoService.addVideo(video);
        assertThat(video1).isNotNull();
        assertThat(video1.getName()).isEqualTo("testMV");

        List<Video> list1 = this.videoService.findVideosByCategoryId(2, 1, 10,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), false);
        assertThat(list1.size()).isEqualTo(1);

        List<Video> list2 = this.videoService.findVideosByCategoryName("MV", 1, 10,
                new OrderBy(IVideoService.ORDER_DURATION, OrderBy.DESC), false);
        assertThat(list2.size()).isEqualTo(1);
    }

    @Test
    public void deleteVideo() {
        int result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(25);

        Video video = this.videoService.findVideoById(1);
        assertThat(video).isNotNull();
        assertThat(video.getName()).isEqualTo("冰川时代4：大陆漂移");

        result = this.videoService.deleteVideo(video);
        assertThat(result).isEqualTo(1);
        Video video1 = this.videoService.findVideoById(1);
        assertThat(video1).isNull();
        result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(24);

        result = this.videoService.deleteVideoById(2);
        assertThat(result).isEqualTo(1);
        Video video2 = this.videoService.findVideoById(1);
        assertThat(video2).isNull();
        result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(23);

        result = this.videoService.deleteVideoByUrl("http://120.26.138.149:8080/video/MV/少女时代.Genie.mp4");
        assertThat(result).isEqualTo(1);
        Video video3 = this.videoService.findVideoByUrl("http://120.26.138.149:8080/video/MV/少女时代.Genie.mp4");
        assertThat(video3).isNull();
        result = this.videoService.findVideoNumber();
        assertThat(result).isEqualTo(22);

        Video video4 = new Video();
        video4.setId(4);
        video4.setUrl("http://120.26.138.149:8080/video/电影/飞屋环游记.Up.2009.mp4");
        Video video5 = new Video();
        video5.setId(5);
        video5.setUrl("http://120.26.138.149:8080/video/电影/黑衣人3.Man.In.Black3.2012.mp4");
        Video video6 = new Video();
        video6.setId(6);
        video6.setUrl("http://120.26.138.149:8080/video/MV/黑眼豆豆演唱会.mp4");

        List<Video> deleteList = new ArrayList<>();
        deleteList.add(video4);
        deleteList.add(video5);
        deleteList.add(video6);
        result = this.videoService.deleteVideos(deleteList);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void updateVideo() {
        Video video = this.videoService.findVideoById(1);
        assertThat(video).isNotNull();
        assertThat(video.getName()).isEqualTo("冰川时代4：大陆漂移");
        assertThat(video.isAudit()).isTrue();
        assertThat(video.getDownloadCount()).isEqualTo(59);

        video.setName("testNewName");
        video.setAudit(false);
        video.setDownloadCount(60);
        int result = this.videoService.updateVideo(video);
        assertThat(result).isEqualTo(1);

        Video video1 = this.videoService.findVideoById(1);
        assertThat(video1).isNotNull();
        assertThat(video.getName()).isEqualTo("testNewName");
        assertThat(video.isAudit()).isFalse();
        assertThat(video.getDownloadCount()).isEqualTo(60);
    }

}
