package com.evistek.mediaserver.controller;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Device;
import com.evistek.mediaserver.service.IDeviceService;
import com.evistek.mediaserver.utils.OpLogger;
import com.evistek.mediaserver.utils.TablePageHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by evis on 2017/1/3.
 */

@RestController
public class DeviceController {

    private final IDeviceService deviceService;
    private final Logger logger;
    private final OpLogger opLogger;

    public DeviceController (OpLogger opLogger, IDeviceService deviceService) {
        this.deviceService = deviceService;
        this.logger = LoggerFactory.getLogger(this.getClass());
        this.opLogger = opLogger;
        this.opLogger.setTag(getClass());
    }

    @RequestMapping(value = "/devices", method = RequestMethod.GET)
    public String getAllDevices(
            @RequestParam(value = "limit", required = false, defaultValue = "0") int limit,
            @RequestParam(value = "offset", required = false, defaultValue = "0") int offset,
            HttpServletRequest request){
        int pageSize = limit;
        int pageNumber = offset / pageSize + 1;

        List<Device> devices = deviceService.findDevices(pageNumber, pageSize);
        int total = this.deviceService.findDeviceNumber();

        this.opLogger.info(request, OpLogger.ACTION_QUERY_DEVICE, "query devices: " + devices.size());

        return TablePageHelper.toJSONString(total, devices);
    }

    @RequestMapping(value = "/devices", method = RequestMethod.PUT)
    public String updateDevice(@RequestBody Device device, HttpServletRequest request){
        int flag = this.deviceService.updateDevice(device);
        String success = JSON.toJSONString("success");
        String error = JSON.toJSONString("error");

        this.opLogger.info(request, OpLogger.ACTION_UPDATE_DEVICE,
                "update info for device with id: " + device.getId());

        if (flag == 1) {
            return success;
        } else {
            return error;
        }
    }
}
