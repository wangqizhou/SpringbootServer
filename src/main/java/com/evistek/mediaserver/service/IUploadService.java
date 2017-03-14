package com.evistek.mediaserver.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/11.
 */
public interface IUploadService {


    /**
     * 保存文件到服务器磁盘，并更新相关信息到数据库
     *
     * @param file 要保存的文件
     * @param object 与文件相关的附件信息，比如Video、Image或者Product对象
     * @return 如果正常保存则返回0， 异常返回-1
     */
    int save(MultipartFile file, Object object);

    /**
     * 一个保存多个文件到服务器磁盘，并更新相关信息到数据库
     *
     * @param files 要保存的文件数组
     * @param object 与文件相关的附件信息，比如Video、Image或者Product对象
     * @return 如果正常保存则返回0， 异常返回-1
     */
    int save(MultipartFile[] files, Object object);
}
