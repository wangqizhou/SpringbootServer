package com.evistek.mediaserver.service;

import com.evistek.mediaserver.entity.Device;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2016/12/26.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
@Sql({"classpath:test-schema.sql", "classpath:test-devices-data.sql"})
public class DeviceServiceTests {

    @Autowired
    private IDeviceService deviceService;

    @Before
    public void setup() {

    }

    @Test
    public void findDevice() {
        int result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(87);

        Device device = this.deviceService.findDeviceById(1);
        assertThat(device).isNotNull();
        assertThat(device.getModel()).isEqualTo("D10");
        assertThat(device.getSystem()).isEqualTo("android 5.1");
        assertThat(device.getLocation()).isEqualTo("上海市");
        assertThat(device.getClient()).isNull();
        assertThat(device.getClientVersion()).isNull();
        assertThat(device.getAccessTime()).isNull();
        assertThat(device.getImei()).isEqualTo("123460000000909");

        Device device1 = this.deviceService.findDeviceByImei("868800024058988");
        assertThat(device1).isNotNull();
        assertThat(device1.getModel()).isEqualTo("m2 note");

        List<Device> deviceList = this.deviceService.findDevices();
        assertThat(deviceList).isNotNull();
        assertThat(deviceList.size()).isEqualTo(87);

        List<Device> deviceList1 = this.deviceService.findDevices(1, 10);
        assertThat(deviceList1).isNotNull();
        assertThat(deviceList1.size()).isEqualTo(10);

        List<Device> deviceList2 = this.deviceService.findDevices(9, 10);
        assertThat(deviceList2).isNotNull();
        assertThat(deviceList2.size()).isEqualTo(7);
        assertThat(deviceList2.get(0).getId()).isEqualTo(81);

        List<Device> deviceList3 = this.deviceService.findDevicesBySystem("android 6.0.1");
        assertThat(deviceList3).isNotNull();
        assertThat(deviceList3.size()).isEqualTo(21);

        List<Device> deviceList4 = this.deviceService.findDevicesBySystem("android 6.0.1", 3, 10);
        assertThat(deviceList4).isNotNull();
        assertThat(deviceList4.size()).isEqualTo(1);

        List<Device> deviceList5 = this.deviceService.findDevicesByModel("See K550");
        assertThat(deviceList5).isNotNull();
        assertThat(deviceList5.size()).isEqualTo(10);

        List<Device> deviceList6 = this.deviceService.findDevicesByModel("See K550", 1, 10);
        assertThat(deviceList6).isNotNull();
        assertThat(deviceList6.size()).isEqualTo(10);

        List<Device> deviceList7 = this.deviceService.findDevicesByModel("See K550", 2, 10);
        assertThat(deviceList7).isNotNull();
        assertThat(deviceList7.size()).isEqualTo(0);

        List<Device> deviceList8 = this.deviceService.findDevicesByLocation("上海市");
        assertThat(deviceList8).isNotNull();
        assertThat(deviceList8.size()).isEqualTo(22);

        List<Device> deviceList9 = this.deviceService.findDevicesByLocation("上海市", 3, 10);
        assertThat(deviceList9).isNotNull();
        assertThat(deviceList9.size()).isEqualTo(2);
    }

    @Test
    public void addDevice() {
        int result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(87);

        Device device = new Device();
        device.setModel("M1L");
        device.setSystem("android 7.0.0");
        device.setLocation("北京市");
        device.setImei("123456789123456");
        try {
            device.setAccessTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .parse("2016-12-26 17:58:00")
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Device dev = this.deviceService.addDevice(device);
        assertThat(dev).isNotNull();

        result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(88);

        Device device1 = this.deviceService.findDeviceByImei("123456789123456");
        assertThat(device1).isNotNull();
        try {
            assertThat(device1.getAccessTime()).isEqualTo(
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-12-26 17:58:00")
            );
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void deleteDevice() {
        int result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(87);

        Device device = this.deviceService.findDeviceById(1);
        assertThat(device).isNotNull();
        result = this.deviceService.deleteDevice(device);
        assertThat(result).isEqualTo(1);
        result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(86);

        result = this.deviceService.deleteDeviceById(2);
        assertThat(result).isEqualTo(1);
        result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(85);

        result = this.deviceService.deleteDeviceByImei("866192020192835");
        assertThat(result).isEqualTo(1);
        result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(84);

        List<Device> deviceList = this.deviceService.findDevicesByLocation("上海市");
        assertThat(deviceList).isNotNull();
        assertThat(deviceList.size()).isEqualTo(21);
        result = this.deviceService.deleteDevices(deviceList);
        assertThat(result).isEqualTo(21);
        result = this.deviceService.findDeviceNumber();
        assertThat(result).isEqualTo(63);
    }

    @Test
    public void updateDevice() {
        Device device = this.deviceService.findDeviceById(1);
        assertThat(device).isNotNull();
        assertThat(device.getModel()).isEqualTo("D10");
        assertThat(device.getSystem()).isEqualTo("android 5.1");
        assertThat(device.getLocation()).isEqualTo("上海市");
        assertThat(device.getClient()).isNull();
        assertThat(device.getClientVersion()).isNull();
        assertThat(device.getAccessTime()).isNull();
        assertThat(device.getImei()).isEqualTo("123460000000909");

        device.setLocation("北京市");
        device.setSystem("android 6.0.1");
        int result = this.deviceService.updateDevice(device);
        assertThat(result).isEqualTo(1);

        Device device1 = this.deviceService.findDeviceById(1);
        assertThat(device1).isNotNull();
        assertThat(device1.getLocation()).isEqualTo("北京市");
        assertThat(device1.getSystem()).isEqualTo("android 6.0.1");
    }
}
