package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Device;

import java.util.List;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
public interface IDeviceService {
    int findDeviceNumber();
    Device findDeviceById(int id);
    Device findDeviceByImei(String imei);
    List<Device> findDevices();
    List<Device> findDevices(int pageNumber, int pageSize);
    List<Device> findDevicesByModel(String model);
    List<Device> findDevicesByModel(String model, int pageNumber, int pageSize);
    List<Device> findDevicesBySystem(String system);
    List<Device> findDevicesBySystem(String system, int pageNumber, int pageSize);
    List<Device> findDevicesByLocation(String location);
    List<Device> findDevicesByLocation(String location, int pageNumber, int pageSize);

    Device addDevice(Device device);

    int deleteDevice(Device device);
    int deleteDeviceById(int id);
    int deleteDeviceByImei(String imei);
    int deleteDevices(List<Device> deviceList);

    int updateDevice(Device device);
    int updateDeviceByImei(Device device);
}
