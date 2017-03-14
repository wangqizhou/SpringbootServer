package com.evistek.mediaserver.service;

/**
 *
 * Created by ymzhao on 2016/12/27.
 */

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.entity.Image;
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
@Sql({"classpath:test-schema.sql", "classpath:test-admins-authorities-data.sql", "classpath:test-images-data.sql"})
public class ImageServiceTests
{
    @Autowired
    private IImageService imageService;

    @Before
    public void setup() {

    }

    @Test
    public void findImage() {
        int result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(69);

        Image image1 = this.imageService.findImageById(1);
        assertThat(image1).isNotNull();
        assertThat(image1.getName()).isEqualTo("b16a8f1a69194a058bad1e1c14bdd322");
        assertThat(image1.getCategoryId()).isEqualTo(4);
        assertThat(image1.getCategoryName()).isEqualTo("景物");
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            assertThat(image1.getCreateTime()).isEqualTo(formatter.parse("2016-06-23 11:40:38"));
            assertThat(image1.getUpdateTime()).isEqualTo(formatter.parse("2016-06-23 11:40:38"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assertThat(image1.getFormat()).isEqualTo("jpg");
        assertThat(image1.getWidth()).isEqualTo(1920);
        assertThat(image1.getHeight()).isEqualTo(1080);
        assertThat(image1.getSize()).isEqualTo(481786);
        assertThat(image1.getUrl()).isEqualTo("http://120.26.138.149:8080/image/景物/b16a8f1a69194a058bad1e1c14bdd322.jpg");
        assertThat(image1.getThumbnail()).isEqualTo("http://120.26.138.149:8080/image/景物/b16a8f1a69194a058bad1e1c14bdd322-thumb.jpg");
        assertThat(image1.getOwnerId()).isEqualTo(3);
        assertThat(image1.isAudit()).isTrue();
        assertThat(image1.getDownloadCount()).isEqualTo(0);

        Image image2 = this.imageService.findImageByUrl("http://120.26.138.149:8080/image/景物/9597b2323ae04631a633bd9362e0e608.jpg");
        assertThat(image2).isNotNull();
        assertThat(image2.getId()).isEqualTo(8);

        List<Image> imageList = this.imageService.findImages();
        assertThat(imageList).isNotNull();
        assertThat(imageList.size()).isEqualTo(69);

        imageList = this.imageService.findImages(7, 10);
        assertThat(imageList).isNotNull();
        assertThat(imageList.size()).isEqualTo(9);

        List<Image> imageList1 = this.imageService.findImagesByAudit(true);
        assertThat(imageList1.size()).isEqualTo(69);
        List<Image> imageList2 = this.imageService.findImagesByAudit(false);
        assertThat(imageList2.size()).isEqualTo(0);

        List<Image> ImageList3 = this.imageService.findImagesByAudit(true, 3, 30);
        assertThat(ImageList3.size()).isEqualTo(9);

        List<Image> imageList4 = this.imageService.findImagesByCategoryId(4);
        assertThat(imageList4.size()).isEqualTo(45);
        assertThat(imageList4.get(1).getCategoryName()).isEqualTo("景物");

        List<Image> ImageList5 = this.imageService.findImagesByCategoryId(4, 2, 25);
        assertThat(ImageList5.size()).isEqualTo(20);

        List<Image> imageList6 = this.imageService.findImagesByCategoryName("人物");
        assertThat(imageList6.size()).isEqualTo(8);
        List<Image> imageList7 = this.imageService.findImagesByCategoryName("建筑");
        assertThat(imageList7.size()).isEqualTo(4);

        List<Image> ImageList8 = this.imageService.findImagesByCategoryName("动物", 1, 13);
        assertThat(ImageList8.size()).isEqualTo(12);
        }

    @Test
    public void findImageOrderBy() {
        List<Image> list = this.imageService.findImages(new OrderBy(IImageService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(69);
        assertThat(list.get(0).getId()).isEqualTo(55);

        List<Image> list1 = this.imageService.findImages(3, 10,
                new OrderBy(IImageService.ORDER_UPDATE_TIME, OrderBy.DESC));
        assertThat(list1).isNotNull();
        assertThat(list1.size()).isEqualTo(10);
        assertThat(list1.get(0).getId()).isEqualTo(48);

        List<Image> list2 = this.imageService.findImagesByCategoryId(4,
                new OrderBy(IImageService.ORDER_UPDATE_TIME, OrderBy.DESC));
        assertThat(list2.size()).isEqualTo(45);
        assertThat(list2.get(0).getId()).isEqualTo(68);

        List<Image> list3 = this.imageService.findImagesByCategoryId(4, 5, 10,
                new OrderBy(IImageService.ORDER_UPDATE_TIME, OrderBy.DESC));
        assertThat(list3.size()).isEqualTo(5);
        assertThat(list3.get(0).getName()).isEqualTo("9e0e9623c922403aa2f4cf7d0995b94d");

        List<Image> list4 = this.imageService.findImagesByCategoryName("景物",
                new OrderBy(IImageService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list4.size()).isEqualTo(45);
        assertThat(list4.get(9).getId()).isEqualTo(4);

        List<Image> list5 = this.imageService.findImagesByCategoryName("动物", 2, 10,
                new OrderBy(IImageService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list5.size()).isEqualTo(2);
        assertThat(list5.get(0).getId()).isEqualTo(60);

        List<Image> list6 = this.imageService.findImagesByAudit(true,
                new OrderBy(IImageService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list6.size()).isEqualTo(69);
        assertThat(list6.get(24).getId()).isEqualTo(25);

        List<Image> list7 = this.imageService.findImagesByAudit(true, 6, 10,
                new OrderBy(IImageService.ORDER_SIZE, OrderBy.DESC));
        assertThat(list7.size()).isEqualTo(10);
        assertThat(list7.get(0).getId()).isEqualTo(17);
    }

    @Test
    public void addImage() {
        int result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(69);

        Image image = new Image();
        image.setName("testImage");
        image.setCategoryId(2);
        image.setCategoryName("image");
        image.setCreateTime(new Date());
        image.setUpdateTime(image.getCreateTime());
        image.setFormat("jpg");
        image.setWidth(1920);
        image.setHeight(1080);
        image.setSize(100 * 1024 * 1024);
        image.setDownloadCount(1);
        image.setUrl("http://120.26.138.149:8080/image/MV/testMV.jpg");
        image.setThumbnail("http://120.26.138.149:8080/image/MV/testMV.jpg");
        image.setAudit(true);
        image.setOwnerId(1);

        int addResult = this.imageService.addImage(image);
        assertThat(addResult).isEqualTo(1);
        int addAfterResult = this.imageService.findImageNumber();
        assertThat(addAfterResult).isEqualTo(result + 1);
        Image image1 = this.imageService.findImageByUrl("http://120.26.138.149:8080/image/MV/testMV.jpg");
        assertThat(image1).isNotNull();
        assertThat(image1.getName()).isEqualTo("testImage");

    }

    @Test
    public void deleteImage() {
        int result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(69);

        Image image = this.imageService.findImageById(1);
        assertThat(image).isNotNull();
        assertThat(image.getName()).isEqualTo("b16a8f1a69194a058bad1e1c14bdd322");

        result = this.imageService.deleteImage(image);
        assertThat(result).isEqualTo(1);
        Image image1 = this.imageService.findImageById(1);
        assertThat(image1).isNull();
        result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(68);

        result = this.imageService.deleteImageById(2);
        assertThat(result).isEqualTo(1);
        Image image2 = this.imageService.findImageById(2);
        assertThat(image2).isNull();
        result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(67);

        result = this.imageService.deleteImageByUrl("http://120.26.138.149:8080/image/景物/7e41427353124cb29e6c107eb39ea227.jpg");
        assertThat(result).isEqualTo(1);
        Image image3 = this.imageService.findImageByUrl("http://120.26.138.149:8080/image/景物/7e41427353124cb29e6c107eb39ea227.jpg");
        assertThat(image3).isNull();
        result = this.imageService.findImageNumber();
        assertThat(result).isEqualTo(66);

        Image image4 = new Image();
        image4.setId(9);
        image4.setUrl("http://120.26.138.149:8080/image/景物/89aa61302d424380b1241f0a68f86a6f.jpg");
        Image image5 = new Image();
        image5.setId(10);
        image5.setUrl("http://120.26.138.149:8080/image/景物/fb0351ba425e46749b61b8c1fd8641f5.jpg");
        Image image6 = new Image();
        image6.setId(11);
        image6.setUrl("http://120.26.138.149:8080/image/景物/945d78f2d28140f8b0e37c273e14b37a.jpg");

        List<Image> deleteList = new ArrayList<>();
        deleteList.add(image4);
        deleteList.add(image5);
        deleteList.add(image6);
        result = this.imageService.deleteImages(deleteList);
        assertThat(result).isEqualTo(3);
    }

    @Test
    public void updateImage() {
        Image image = this.imageService.findImageById(1);
        assertThat(image).isNotNull();
        assertThat(image.getName()).isEqualTo("b16a8f1a69194a058bad1e1c14bdd322");
        assertThat(image.isAudit()).isTrue();
        assertThat(image.getDownloadCount()).isEqualTo(0);

        image.setName("testNewName");
        image.setAudit(false);
        image.setDownloadCount(60);
        int result = this.imageService.updateImage(image);
        assertThat(result).isEqualTo(1);

        Image image1 = this.imageService.findImageById(1);
        assertThat(image1).isNotNull();
        assertThat(image.getName()).isEqualTo("testNewName");
        assertThat(image.isAudit()).isFalse();
        assertThat(image.getDownloadCount()).isEqualTo(60);

    }
}
