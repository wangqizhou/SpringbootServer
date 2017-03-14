package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Logger;

import java.util.List;

/**
 * Created by ymzhao on 2017/3/6.
 */
public interface ILoggerService {

    int findLoggersNumber();
    List<Logger> findLoggers(int pageNumber, int pageSize);
    List<Logger> findLoggersByAction(String action, int pageNumber, int pageSize);
    List<Logger> findLoggersByOwner(String owner, int pageNumber, int pageSize);
    int addLogger(Logger logger, int months);
}
