package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Admin;

import java.util.List;

/**
 * Created by ymzhao on 2017/1/24.
 */
public interface IEmailGroupService {
    List<String> findEmails();

    List<Admin> findMembers();

    int addMember(Admin admin);

    int deleteMember(Admin admin);

    int deleteMember(int id);
}
