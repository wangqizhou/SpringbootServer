package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Admin;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by ymzhao on 2017/1/24.
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-email_group.sql", "classpath:test-admins-authorities-data.sql"})

public class EmailGroupServiceTests {
    @Autowired
    private IEmailGroupService emailGroupService;

    @Before
    public void setup() {

    }
    @Test
    public void findMember() {
        List<Admin> adminList = this.emailGroupService.findMembers();
        assertThat(adminList).isNotNull();
        assertThat(adminList.size()).isEqualTo(2);
        assertThat(adminList.get(1).getId()).isEqualTo(3);

        List<String> emailList = this.emailGroupService.findEmails();
        assertThat(emailList).isNotNull();
        assertThat(emailList.get(1)).isEqualTo("ymzhao@evistek.com");
    }

    @Test
    public void addMember() {
        Admin admin = new Admin();
        admin.setId(1);
        admin.setUsername("wqzhou");
        int result = this.emailGroupService.addMember(admin);
        assertThat(result).isEqualTo(1);
    }
    @Test
    public void delete() {
        Admin admin = new Admin();
        admin.setId(1);
        admin.setUsername("wqzhou");
        this.emailGroupService.addMember(admin);
        int result = this.emailGroupService.deleteMember(admin);
        assertThat(result).isEqualTo(1);
        int deleteResult = this.emailGroupService.deleteMember(2);
        assertThat(deleteResult).isEqualTo(1);
    }
}
