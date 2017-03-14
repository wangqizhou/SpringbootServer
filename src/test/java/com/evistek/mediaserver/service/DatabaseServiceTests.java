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

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/23.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-admins-authorities-data.sql"})
public class DatabaseServiceTests {

    @Autowired
    private IDatabaseService databaseService;
    @Autowired
    private IAdminService adminService;

    @Before
    public void setup() {

    }

    @Test
    public void backup() {
        String file = "test-backup.sql";
        String backupPath = this.databaseService.getBackupPath();
        assertThat(backupPath).isNotNull();
        File backupFile = new File(backupPath + file);
        if (backupFile.exists()) {
            backupFile.delete();
        }
        assertThat(backupFile.exists()).isFalse();

        int result = this.databaseService.backup(file);
        assertThat(result).isEqualTo(0);
        backupFile = new File(backupPath + file);
        assertThat(backupFile.exists()).isTrue();
    }

    @Test
    public void restore() {
        String file = "test-backup.sql";
        String backupPath = this.databaseService.getBackupPath();
        File backupFile = new File(backupPath + file);
        if (!backupFile.exists()) {
            int result = this.databaseService.backup(file);
            assertThat(result).isEqualTo(0);
            assertThat(backupFile.exists()).isTrue();
        }

        Admin admin = this.adminService.findAdminByName("wxzhang");
        assertThat(admin).isNotNull();
        int result = this.adminService.deleteAdmin(admin);
        assertThat(result).isEqualTo(1);

        int result1 = this.databaseService.restore(file);
        assertThat(result1).isEqualTo(0);
        Admin admin1 = this.adminService.findAdminByName("wxzhang");
        assertThat(admin1).isNotNull();
    }

    @Test
    public void export() {
        String file = "test-export.xls";
        String exportPath = this.databaseService.getExportPath();
        assertThat(exportPath).isNotNull();

        File exportFile = new File(exportPath + file);
        if (exportFile.exists()) {
            exportFile.delete();
        }
        assertThat(exportFile.exists()).isFalse();

        String statement = "\"SELECT * FROM admins\"";
        int result = this.databaseService.exportXls(statement, file);
        assertThat(result).isEqualTo(0);
        assertThat(exportFile.exists()).isTrue();
    }
}
