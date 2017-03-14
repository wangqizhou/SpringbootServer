package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Logger;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import javax.smartcardio.CardTerminal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ymzhao on 2017/3/6.
 */
@Component
public class LoggerDao {
    private SqlSession sqlSession;
    private PageHelper pageHelper;

    public LoggerDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectLoggerCount() {
        return this.sqlSession.selectOne("selectLoggersCount");
    }

    public List<Logger> selectLoggers(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectLoggers", null, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Logger> selectLoggersByAction(String action, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectLoggersByAction", action, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Logger> selectLoggersByOwner(String owner, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectLoggersByOwner", owner, this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Logger> selectLoggersBeforeMonth(Date date, int month) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.selectList("selectLoggersBeforeMonth", mapParameter);
    }

    public int insertLogger(Logger logger) {
        return this.sqlSession.insert("insertLogger", logger);
    }

    public int deleteLoggersBeforeMonth (Date date, int month) {
        Map<String, Object> mapParameter = new HashMap<String, Object>();
        mapParameter.put("date", date);
        mapParameter.put("month", month);
        return this.sqlSession.delete("deleteLoggersBeforeMonth", mapParameter);
    }
}
