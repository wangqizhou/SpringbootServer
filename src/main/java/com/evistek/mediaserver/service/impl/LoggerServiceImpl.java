package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.LoggerDao;
import com.evistek.mediaserver.entity.Logger;
import com.evistek.mediaserver.service.ILoggerService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Created by ymzhao on 2017/3/6.
 */
@Service
public class LoggerServiceImpl implements ILoggerService {
    private final LoggerDao loggerDao;
    private final org.slf4j.Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public LoggerServiceImpl (LoggerDao loggerDao) {
        this.loggerDao = loggerDao;
    }

    @Override
    public int findLoggersNumber() {
        return this.loggerDao.selectLoggerCount();
    }

    @Override
    public List<Logger> findLoggers(int pageNumber, int pageSize) {
        return this.loggerDao.selectLoggers(pageNumber, pageSize);
    }

    @Override
    public List<Logger> findLoggersByAction(String action, int pageNumber, int pageSize) {
        return this.loggerDao.selectLoggersByAction(action, pageNumber, pageSize);
    }

    @Override
    public List<Logger> findLoggersByOwner(String owner, int pageNumber, int pageSize) {
        return this.loggerDao.selectLoggersByOwner(owner, pageNumber, pageSize);
    }

    @Transactional
    @Override
    public int addLogger(Logger logger, int months) {
        int result = this.loggerDao.insertLogger(logger);
        int deleteFlag = 0;
        if (result > 0) {
            List<Logger> loggerList = this.loggerDao.selectLoggersBeforeMonth(logger.getTime(), months);
            if (!loggerList.isEmpty()) {
                deleteFlag = this.loggerDao.deleteLoggersBeforeMonth(logger.getTime(), months);
            } else {
                deleteFlag = 1;
            }
        }

        if (deleteFlag > 0) {
            //>0 proper execution
            return 1;
        } else {
            throw new RuntimeException();
        }
    }
}
