package com.evistek.mediaserver.controller.api.v2;

import com.alibaba.fastjson.JSON;
import com.evistek.mediaserver.entity.Device;
import com.evistek.mediaserver.service.IDeviceService;
import com.evistek.mediaserver.utils.HttpErrorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

/**
 * Author: Weixiang Zhang
 * Email: wxzhang@evistek.com
 * Created on 2017/1/5.
 */
@RestController
@RequestMapping("/api/v2")
public class DeviceApi {

    private final IDeviceService deviceService;
    private final Logger logger;

    public DeviceApi(IDeviceService deviceService) {
        this.deviceService = deviceService;
        this.logger = LoggerFactory.getLogger(getClass());
    }

    @RequestMapping(value = "/devices", method = RequestMethod.PUT)
    public String updateDevice(@RequestBody Device device, HttpServletResponse resp) {
        int result = this.deviceService.updateDeviceByImei(device);
        if (result != 1) {
            logger.error("status:" + HttpServletResponse.SC_BAD_REQUEST + " BAD REQUEST " +
                    "message: " + "fail to update device with id: " + device.getId() +
                                    " and IMEI: " + device.getImei());
            return HttpErrorMessage.build(resp,
                    HttpServletResponse.SC_BAD_REQUEST,
                    "fail to update device with id: " + device.getId() +
                            " and IMEI: " + device.getImei());
        }

        return JSON.toJSONString("success to update device information");
    }
}
