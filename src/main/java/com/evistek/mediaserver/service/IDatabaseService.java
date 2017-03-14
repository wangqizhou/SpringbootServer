package com.evistek.mediaserver.service;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/22.
 */
public interface IDatabaseService {
    /**
     * 备份数据库
     *
     * @param file 仅是保存的文件名, 扩展名是.sql，不是全路径名称
     * @return 备份成功返回0，失败返回-1
     */
    int backup(String file);

    /**
     * 从备份文件中还原数据库
     *
     * @param file 仅是保存的文件名, 扩展名是.sql，不是全路径名称
     * @return 备份成功返回0，失败返回-1
     */
    int restore(String file);

    /**
     * 将SQL执行结果导出到xls文件
     *
     * @param statement 要执行的SQL语句
     * @param file 仅是保存的文件名，扩展名是.xls，不是全路径名称
     * @return 导出成功返回0，失败返回-1
     */
    int exportXls(String statement, String file);


    /**
     * 获取数据库备份文件的绝对路径
     *
     * @return windows或者linux下的绝对路径
     */
    String getBackupPath();

    /**
     * 获取数据库导出xls文件的绝对路径
     *
     * @return windows或者linux下的绝对路径
     */
    String getExportPath();
}
