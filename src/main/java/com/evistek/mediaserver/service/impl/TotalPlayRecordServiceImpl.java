package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.OrderBy;
import com.evistek.mediaserver.dao.TotalPlayRecordDao;
import com.evistek.mediaserver.entity.PlayRecord;
import com.evistek.mediaserver.entity.StatisticUser;
import com.evistek.mediaserver.service.ITotalPlayRecordService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static com.evistek.mediaserver.dao.OrderBy.ASC;
import static com.evistek.mediaserver.dao.OrderBy.DESC;

/**
 *
 * Created by ymzhao on 2017/1/4.
 */
@Service
public class TotalPlayRecordServiceImpl implements ITotalPlayRecordService{
    private final TotalPlayRecordDao totalPlayRecordDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    public static final int GRADE_MONTH = 0;
    public static final int GRADE_WEEK = 1;
    public static final int GRADE_DAYS = 2;
    public static final int QUERY_LOCATION = 1;
    public static final int QUERY_USUALLY = 2;
    @Autowired
    public TotalPlayRecordServiceImpl (TotalPlayRecordDao totalPlayRecordDao) {
        this.totalPlayRecordDao = totalPlayRecordDao;
    }

    @Override
    public int findPlayRecordNumber() {
        return this.totalPlayRecordDao.selectPlayRecordCount();
    }

    @Override
    public PlayRecord findPlayRecord(int userId, int videoId, String client) {
        return totalPlayRecordDao.selectPlayRecord(userId, videoId, client);
    }

    @Override
    public PlayRecord findPlayRecord(int userId, int videoId, Date startTime, String client) {
        return totalPlayRecordDao.selectPlayRecord(userId, videoId, startTime, client);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client) {
        return totalPlayRecordDao.selectPlayRecordsByUserId(userId, client);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, OrderBy orderBy) {
        return totalPlayRecordDao.selectPlayRecordsByUserId(userId, client, validateOrderBy(orderBy));
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize) {
        return totalPlayRecordDao.selectPlayRecordsByUserId(userId, client, pageNumber, pageSize);
    }

    @Override
    public List<PlayRecord> findPlayRecordByUserId(int userId, String client, int pageNumber, int pageSize, OrderBy orderBy) {
        return totalPlayRecordDao.selectPlayRecordsByUserId(userId, client, pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public int addPlayRecord(PlayRecord record) {
        PlayRecord playRecord = findPlayRecord(record.getUserId(), record.getVideoId(), record.getStartTime(), record.getClient());
        record.setStartTime(new Date());
        if (playRecord == null) {
            return totalPlayRecordDao.insertPlayRecord(record);
        } else {
            record.setId(playRecord.getId());
            return totalPlayRecordDao.updatePlayRecord(record);
        }
    }

    @Transactional(readOnly = false, propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class, isolation = Isolation.SERIALIZABLE)
    @Override
    public int addPlayRecord(PlayRecord record, int months) {
        record.setStartTime(new Date());
        PlayRecord totalPlayRecord = findPlayRecord(record.getUserId(), record.getVideoId(), record.getClient());
        boolean clearFlag = false;
        if (totalPlayRecord == null) {
            int flag = totalPlayRecordDao.insertPlayRecord(record);
            if (flag > 0) {
                clearFlag = true;
            } else {
                return 0;
            }
        } else {
            record.setId(totalPlayRecord.getId());
            int updateFlag = totalPlayRecordDao.updatePlayRecord(record);
            if (updateFlag > 0) {
                clearFlag = true;
            } else {
                return 0;
            }
        }
        if (clearFlag) {
            List<PlayRecord> playRecordList = totalPlayRecordDao.selectRecordsBeforeMonth(record.getStartTime(), months);
            if (!playRecordList.isEmpty()) {
                int deleteFlag = totalPlayRecordDao.deleteRecordsBeforeMonth(record.getStartTime(), months);
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
    public int deletePlayRecord(PlayRecord record) {
        return totalPlayRecordDao.deletePlayRecord(record);
    }

    @Override
    public int deletePlayRecordsByUserId(int userId, String client) {
        return totalPlayRecordDao.deletePlayRecordsByUserId(userId, client);
    }

    @Override
    public int deletePlayRecordBatch(List<PlayRecord> playRecordsList) {
        return totalPlayRecordDao.deletePlayRecords(playRecordsList);
    }

    @Override
    public int updatePlayRecord(PlayRecord newRecord) {
        PlayRecord totalPlayRecord = findPlayRecord(newRecord.getUserId(), newRecord.getVideoId(), newRecord.getStartTime(),
                newRecord.getClient());
        if (totalPlayRecord != null) {
            newRecord.setEndTime(new Date());
            long duration = newRecord.getEndTime().getTime() - totalPlayRecord.getStartTime().getTime();
            newRecord.setDuration(duration);
            newRecord.setId(totalPlayRecord.getId());
            return totalPlayRecordDao.updateDuration(newRecord);
        }
        return 0;
    }


    @Override
    public List<PlayRecord> findAllPlayRecords() {
        return totalPlayRecordDao.selectTotalPlayRecords();
    }

    @Override
    public List<PlayRecord> findAllPlayRecords(OrderBy orderBy) {
        return totalPlayRecordDao.selectTotalPlayRecords(validateOrderBy(orderBy));
    }

    @Override
    public List<PlayRecord> findAllPlayRecords(int pageNumber, int pageSize) {
        return totalPlayRecordDao.selectTotalPlayRecords(pageNumber, pageSize);
    }

    @Override
    public List<PlayRecord> findAllPlayRecords(int pageNumber, int pageSize, OrderBy orderBy) {
        return totalPlayRecordDao.selectTotalPlayRecords(pageNumber, pageSize, validateOrderBy(orderBy));
    }

    @Override
    public List<StatisticUser> getStatisticActiveUser(int query_grade, int query_type, Date date, int num) {
        List<StatisticUser> userList = new ArrayList<StatisticUser>();
        switch (query_grade) {
            case GRADE_MONTH:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByMonths(date, num, "months, location");
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByMonths(date, num, "months");
                }
                break;
            case GRADE_WEEK:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByWeeks(date, num, "weeks, location");
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByWeeks(date, num, "weeks");
                }
                break;
            case GRADE_DAYS:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByDays(date, num, "days, location");
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByDays(date, num, "days");
                }
                break;
        }
        if (!userList.isEmpty()) {
            return userList;
        }
        return null;
    }

    @Override
    public List<StatisticUser> getStatisticActiveUser(int query_grade, int query_type, Date date, int num, int pageNumber, int pageSize) {
        List<StatisticUser> userList = new ArrayList<StatisticUser>();
        switch (query_grade) {
            case GRADE_MONTH:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByMonths(date, num, "months, location", pageNumber, pageSize);
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByMonths(date, num, "months", pageNumber, pageSize);
                }
                break;
            case GRADE_WEEK:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByWeeks(date, num, "weeks, location", pageNumber, pageSize);
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByWeeks(date, num, "weeks", pageNumber, pageSize);
                }
                break;
            case GRADE_DAYS:
                if (query_type == QUERY_LOCATION) {
                    userList = totalPlayRecordDao.getStatisticActiveUserByDays(date, num, "days, location", pageNumber, pageSize);
                } else {
                    userList = totalPlayRecordDao.getStatisticActiveUserByDays(date, num, "days", pageNumber, pageSize);
                }
                break;
        }
        if (!userList.isEmpty()) {
            return userList;
        }
        return null;
    }

    @Override
    public String exportXLSByActiveUser(int query_grade, int query_type, Date date, int num) {
        String dateFormat = "%Y%m";
        String queryKey = "";
        String location = "";
        if (query_type == QUERY_LOCATION) {
            location = ", location";
        }
        String weeksFields = "";
        String weeksOutFields = "";
        switch (query_grade) {
            case GRADE_MONTH:
                dateFormat = "%Y%m";
                queryKey = "months";
                break;
            case GRADE_WEEK:
                dateFormat = "%Y%u";
                queryKey = "weeks";
                weeksFields = ", date_sub(date_format(start_time,'%Y%m%d'), INTERVAL WEEKDAY(start_time) DAY) as starttime, date_sub(date_format(start_time,"
                        + "'%Y%m%d'),INTERVAL WEEKDAY(start_time) - 6 DAY) as endtime";
                weeksOutFields = ",starttime, endtime";
                break;
            case GRADE_DAYS:
                dateFormat = "%Y%m%d";
                queryKey = "days";
                break;
        }
        String queryDate = new SimpleDateFormat("yyyy-MM-dd").format(date);
        System.out.println("queryDate" + queryDate);
        String statement = "\"select " + queryKey + weeksOutFields +location + ", count(*) as count from (select  DATE_FORMAT(start_time,'" + dateFormat + "') " +
                queryKey + weeksFields + ",(select location from users where id = total_play_records.user_id) as location from total_play_records where "
                + "PERIOD_DIFF(date_format('" + queryDate + "' , '%Y%m' ) , date_format( start_time, '%Y%m' ) )> -1 && PERIOD_DIFF( date_format('" + queryDate + "' , "
                + "'%Y%m' ) , date_format( start_time, '%Y%m' ) ) < " + num + "  group by user_id, "+ queryKey + ")as a group by "+ queryKey + location +";\"";
        return statement;
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
