package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.dao.PlayRecordDao;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.service.IPlayRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.evistek.mediaserver.dao.OrderBy.ASC;
import static com.evistek.mediaserver.dao.OrderBy.DESC;

/**
 *
 * Created by ymzhao on 2016/12/30.
 */
@Service
public class PlayRecordServiceImpl implements IPlayRecordService{
    private final PlayRecordDao playRecordDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PlayRecordServiceImpl (PlayRecordDao playRecordDao) {
        this.playRecordDao = playRecordDao;
    }

    @Override
    public int findPlayRecordNumber() {
        return this.playRecordDao.selectPlayRecordCount();
    }

    @Override
    public PlayRecord findPlayRecordById(int id) {
        return playRecordDao.selectPlayRecordById(id);
    }

    @Override
    public PlayRecord findPlayRecord(int userId, int videoId, String client) {
        return playRecordDao.selectPlayRecord(userId, videoId, client);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client) {
        return playRecordDao.selectPlayRecordsByUserId(userId, client);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, OrderBy orderBy) {
        return playRecordDao.selectPlayRecordsByUserId(userId, client, validateOrderBy(orderBy));
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize) {
        return playRecordDao.selectPlayRecordsByUserId(userId, client, pageNumber, pageSize);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy) {
        return playRecordDao.selectPlayRecordsByUserId(userId, client, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public PlayRecord addPlayRecord(PlayRecord record) {
        int result = 0;
        PlayRecord playRecord = findPlayRecord(record.getUserId(), record.getVideoId(), record.getClient());
        record.setStartTime(new Date());
        if (playRecord == null) {
           result = playRecordDao.insertPlayRecord(record);
        } else {
            record.setId(playRecord.getId());
            result = playRecordDao.updatePlayRecord(record);
        }
        if (result == 1) {
            return this.playRecordDao.selectPlayRecord(record.getUserId(), record.getVideoId(), record.getClient());
        }
        return null;
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    @Override
    public int addPlayRecord(PlayRecord record, int months) {
        record.setStartTime(new Date());
        PlayRecord playRecord = findPlayRecord(record.getUserId(), record.getVideoId(), record.getClient());
        boolean clearFlag = false;
        if (playRecord == null) {
            int flag = playRecordDao.insertPlayRecord(record);
            if (flag > 0) {
                clearFlag = true;
            } else {
                return 0;
            }
        } else {
            record.setId(playRecord.getId());
            int updateFlag = playRecordDao.updatePlayRecord(record);
            if (updateFlag > 0) {
                clearFlag = true;
            } else {
                return 0;
            }
        }
        if (clearFlag) {
            List<PlayRecord> playRecordList = playRecordDao.selectRecordsBeforeMonth(record.getStartTime(), months);
            if (!playRecordList.isEmpty()) {
                int deleteFlag = playRecordDao.deleteRecordsBeforeMonth(record.getStartTime(), months);
                if (deleteFlag > 0) {
                    return 1;
                } else {
                    throw new RuntimeException();
                }
            } else {
                return 1;
            }
        } else {
            return 0;
        }
    }

    @Override
    public int deletePlayRecord(int id) {
        return playRecordDao.deletePlayRecordById(id);
    }


    @Override
    public int deletePlayRecord(PlayRecord record) {
        return playRecordDao.deletePlayRecord(record);
    }

    @Override
    public int deletePlayRecordsByUserId(int userId, String client) {
        return playRecordDao.deletePlayRecordsByUserId(userId, client);
    }

    @Override
    public int deletePlayRecordBatch(List<PlayRecord> playRecordsList) {
        return playRecordDao.deletePlayRecords(playRecordsList);
    }

    @Override
    public int updatePlayRecord(PlayRecord newRecord) {
        PlayRecord playRecord = findPlayRecord(newRecord.getUserId(), newRecord.getVideoId(), newRecord.getClient());
        if (playRecord != null) {
            newRecord.setEndTime(new Date());
            long duration = newRecord.getEndTime().getTime() - playRecord.getStartTime().getTime();
            newRecord.setDuration(duration);
            newRecord.setId(playRecord.getId());
            return playRecordDao.updateDuration(newRecord);
        }
        return 0;
    }

    private OrderBy validateOrderBy(OrderBy orderBy) {
        List<String> keyList = Arrays.asList(
                ORDER_ID,
                ORDER_START_TIME,
                ORDER_END_TIME,
                ORDER_DURATION
        );

        List<String> orderList = Arrays.asList(
                ASC,
                DESC
        );

        if (!keyList.contains(orderBy.getKey()) || !orderList.contains(orderBy.getOrder())) {
            logger.error("invalid parameters for ORDER BY: " + orderBy.getKey() + " " + orderBy.getOrder() + "."
                    + " Use ORDER BY id ASC as default."
            );
            return  new OrderBy(ORDER_ID, ASC);
        }

        return orderBy;
    }
}
