package com.evistek.mediaserver.utils;

import org.apache.ibatis.session.RowBounds;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/27.
 */
@Component
public class PageHelper {

    /**
     * 生成MyBatis查询分页所需的RowBounds对象
     *
     * @param pageNum 分页序号，从1开始
     * @param pageSize 每页结果数目
     * @return RowBounds对象，可用作SqlSession的分页参数
     */
    public RowBounds getRowBounds(int pageNum, int pageSize) {
        int num = pageNum > 0 ? pageNum : RowBounds.NO_ROW_OFFSET;
        int size = pageSize > 0 ? pageSize : RowBounds.NO_ROW_LIMIT;
        return new RowBounds(size * (num - 1), size);
    }
}
