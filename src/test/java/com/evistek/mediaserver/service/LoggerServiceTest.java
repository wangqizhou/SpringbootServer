package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Logger;
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
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


/**
 * Created by ymzhao on 2017/3/7.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-loggers-data.sql"})

public class LoggerServiceTest {

    @Autowired
    private ILoggerService loggerService;

    @Before
    public void setup() {

    }

    @Test
    public void addLoggers () {
        Logger logger = new Logger();
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse("2017-03-09");
            logger.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        logger.setAction("test");
        logger.setOwner("ymzhao");
        logger.setMessage("print informaton of message!");
        int result = this.loggerService.addLogger(logger, 1);
        assertThat(result).isEqualTo(1);

        assertThat(this.loggerService.findLoggersNumber()).isEqualTo(11);
        List<Logger> loggerList = this.loggerService.findLoggers(3, 3);
        assertThat(loggerList.size()).isEqualTo(3);

        List<Logger> loggerList1 = this.loggerService.findLoggersByAction("AUDIT_RESOURCE", 2, 4);
        assertThat(loggerList1.size()).isEqualTo(2);

        List<Logger> loggerList2 = this.loggerService.findLoggersByOwner("ymzhao", 1, 3);
        assertThat(loggerList2.size()).isEqualTo(3);
    }
}
