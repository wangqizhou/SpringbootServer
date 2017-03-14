package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.EmailGroupDao;
import com.evistek.mediaserver.entity.Admin;
import com.evistek.mediaserver.service.IEmailGroupService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ymzhao on 2017/1/24.
 */
@Service
public class EmailGroupServiceImpl implements IEmailGroupService {
    private final EmailGroupDao emailGroupDao;

    public EmailGroupServiceImpl(EmailGroupDao emailGroupDao) {
        this.emailGroupDao = emailGroupDao;
    }

    @Override
    public List<String> findEmails() {
        List<Admin> admins = this.emailGroupDao.selectMembers();
        List<String> emailList = new ArrayList<>();
        if (!admins.isEmpty()) {
            for(Admin admin : admins) {
                emailList.add(admin.getEmail());
            }
            return emailList;
        }
        return null;
    }

    @Override
    public List<Admin> findMembers() {
        return this.emailGroupDao.selectMembers();
    }

    @Override
    public int addMember(Admin admin) {
        return this.emailGroupDao.insert(admin);
    }

    @Override
    public int deleteMember(Admin admin) {
        return this.emailGroupDao.delete(admin.getId());
    }

    @Override
    public int deleteMember(int id) {
        return this.emailGroupDao.delete(id);
    }
}
