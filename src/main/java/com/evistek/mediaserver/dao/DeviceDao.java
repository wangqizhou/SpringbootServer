package com.evistek.mediaserver.dao;

import com.evistek.mediaserver.entity.Device;
import com.evistek.mediaserver.utils.PageHelper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@Component
public class DeviceDao {
    private final SqlSession sqlSession;
    private final PageHelper pageHelper;

    public DeviceDao(SqlSession sqlSession, PageHelper pageHelper) {
        this.sqlSession = sqlSession;
        this.pageHelper = pageHelper;
    }

    public int selectDeviceCount() {
        return this.sqlSession.selectOne("selectDeviceCount");
    }

    public Device selectDeviceById(int id) {
        return this.sqlSession.selectOne("selectDeviceById", id);
    }

    public Device selectDeviceByImei(String imei) {
        return this.sqlSession.selectOne("selectDeviceByImei", imei);
    }

    public List<Device> selectDevices() {
        return this.sqlSession.selectList("selectDevices");
    }

    public List<Device> selectDevices(int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectDevices", null,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Device> selectDevicesByModel(String model) {
        return this.sqlSession.selectList("selectDevicesByModel", model);
    }

    public List<Device> selectDevicesByModel(String model, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectDevicesByModel", model,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Device> selectDevicesBySystem(String system) {
        return this.sqlSession.selectList("selectDevicesBySystem", system);
    }

    public List<Device> selectDevicesBySystem(String system, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectDevicesBySystem", system,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public List<Device> selectDevicesByLocation(String location) {
        return this.sqlSession.selectList("selectDevicesByLocation", location);
    }

    public List<Device> selectDevicesByLocation(String location, int pageNumber, int pageSize) {
        return this.sqlSession.selectList("selectDevicesByLocation", location,
                this.pageHelper.getRowBounds(pageNumber, pageSize));
    }

    public int insertDevice(Device device) {
        return this.sqlSession.insert("insertDevice", device);
    }

    public int deleteDevice(Device device) {
        return this.sqlSession.delete("deleteDevice", device);
    }

    public int deleteDeviceById(int id) {
        return this.sqlSession.delete("deleteDeviceById", id);
    }

    public int deleteDeviceByImei(String imei) {
        return this.sqlSession.delete("deleteDeviceByImei", imei);
    }

    public int deleteDevices(List<Device> deviceList) {
        return this.sqlSession.delete("deleteDevices", deviceList);
    }

    public int updateDevice(Device device) {
        return this.sqlSession.update("updateDevice", device);
    }

    public int updateDeviceByImei(Device device) {
        return this.sqlSession.update("updateDeviceByImei", device);
    }
}
