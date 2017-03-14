package com.evistek.mediaserver.service.impl;

import com.evistek.mediaserver.dao.DeviceDao;
import com.evistek.mediaserver.entity.Device;
import com.evistek.mediaserver.service.IDeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@Service
public class DeviceServiceImpl implements IDeviceService {
    private final DeviceDao deviceDao;

    @Autowired
    public DeviceServiceImpl(DeviceDao deviceDao) {
        this.deviceDao = deviceDao;
    }

    @Override
    public int findDeviceNumber() {
        return this.deviceDao.selectDeviceCount();
    }

    @Override
    public Device findDeviceById(int id) {
        return this.deviceDao.selectDeviceById(id);
    }

    @Override
    public Device findDeviceByImei(String imei) {
        return this.deviceDao.selectDeviceByImei(imei);
    }

    @Override
    public List<Device> findDevices() {
        return this.deviceDao.selectDevices();
    }

    @Override
    public List<Device> findDevices(int pageNumber, int pageSize) {
        return this.deviceDao.selectDevices(pageNumber, pageSize);
    }

    @Override
    public List<Device> findDevicesByModel(String model) {
        return this.deviceDao.selectDevicesByModel(model);
    }

    @Override
    public List<Device> findDevicesByModel(String model, int pageNumber, int pageSize) {
        return this.deviceDao.selectDevicesByModel(model, pageNumber, pageSize);
    }

    @Override
    public List<Device> findDevicesBySystem(String system) {
        return this.deviceDao.selectDevicesBySystem(system);
    }

    @Override
    public List<Device> findDevicesBySystem(String system, int pageNumber, int pageSize) {
        return this.deviceDao.selectDevicesBySystem(system, pageNumber, pageSize);
    }

    @Override
    public List<Device> findDevicesByLocation(String location) {
        return this.deviceDao.selectDevicesByLocation(location);
    }

    @Override
    public List<Device> findDevicesByLocation(String location, int pageNumber, int pageSize) {
        return this.deviceDao.selectDevicesByLocation(location, pageNumber, pageSize);
    }

    @Override
    public Device addDevice(Device device) {
        int result = this.deviceDao.insertDevice(device);
        if (result == 1) {
            Device dev = this.deviceDao.selectDeviceByImei(device.getImei());
            if (dev != null) {
                return dev;
            }
        }

        return null;
    }

    @Override
    public int deleteDevice(Device device) {
        return this.deviceDao.deleteDevice(device);
    }

    @Override
    public int deleteDeviceById(int id) {
        return this.deviceDao.deleteDeviceById(id);
    }

    @Override
    public int deleteDeviceByImei(String imei) {
        return this.deviceDao.deleteDeviceByImei(imei);
    }

    @Override
    public int deleteDevices(List<Device> deviceList) {
        return this.deviceDao.deleteDevices(deviceList);
    }

    @Override
    public int updateDevice(Device device) {
        Device dev = this.findDeviceById(device.getId());
        if (dev != null) {
            return this.deviceDao.updateDevice(device);
        } else {
            return this.deviceDao.insertDevice(device);
        }
    }

    @Override
    public int updateDeviceByImei(Device device) {
        Device dev = this.findDeviceByImei(device.getImei());
        if (dev != null) {
            return this.deviceDao.updateDeviceByImei(device);
        } else {
            return this.deviceDao.insertDevice(device);
        }
    }
}
