package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.*;
import etc.cloud.park.mapper.DeviceInfoMapper;
import etc.cloud.park.mode.vo.ChannelVO;
import etc.cloud.park.mode.vo.DeviceInfoVO;
import etc.cloud.park.mode.vo.DeviceLogVO;
import etc.cloud.park.mode.vo.ShareInfoMonthVO;
import etc.cloud.park.service.DeviceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class DeviceInfoServiceImpl implements DeviceInfoService {

    @Autowired
    private DeviceInfoMapper deviceInfoMapper;

    @Override
    public Object SelectDeviceInfo(String data)
    {
        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //获取页数和每页获取数据数
        Integer current = jsonObject.getInt("current");
        Integer limit = jsonObject.getInt("limit");

        //获取数据库数据起始位置
        Integer currentSe=(current-1)*limit;

        JSONObject query = jsonObject.getJSONObject("query");

        //获取前端传来的判断条件
        Integer channelId = query.getInt("channelId");

        System.out.println("通道ID"+channelId);

        List<DeviceInfoVO> deviceInfoVOS = deviceInfoMapper.selectDeviceInfoVO(currentSe, limit,channelId);

        Integer total = deviceInfoMapper.SqlTotal();

        List<DeviceInfoDTO> deviceInfoDTOList=new ArrayList<>();

        for(Integer i=0;i<deviceInfoVOS.size();i++)
        {
            DeviceInfoDTO deviceInfoDTO=new DeviceInfoDTO();

            deviceInfoDTO.setChannelName(deviceInfoVOS.get(i).getChanName());
            deviceInfoDTO.setDeviceName(deviceInfoVOS.get(i).getDeviceName());
            switch (deviceInfoVOS.get(i).getState())
            {
                case 0:
                    deviceInfoDTO.setState("在线");
                    break;
                case 1:
                    deviceInfoDTO.setState("离线");
                    break;
            }

            deviceInfoDTOList.add(deviceInfoDTO);
        }

        //获取通道数据库名队列
        List<ChannelVO> channelVOS = deviceInfoMapper.getChannelName();
        List<ChannelDTO> channelDTOList =new ArrayList<>();

        for(Integer i=0;i<channelVOS.size();i++)
        {
            ChannelDTO channelDTO=new ChannelDTO();

            channelDTO.setChannelId(channelVOS.get(i).getPkChanId());
            channelDTO.setChannelName(channelVOS.get(i).getChanName());

            channelDTOList.add(channelDTO);
        }

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",deviceInfoDTOList)
                .data("channel",channelDTOList);
    }

    @Override
    public Object SelectDeviceLog(String data)
    {
        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //获取页数和每页获取数据数
        Integer current = jsonObject.getInt("current");
        Integer limit = jsonObject.getInt("limit");

        //获取数据库数据起始位置
        Integer currentSe=(current-1)*limit;

        JSONObject query = jsonObject.getJSONObject("query");

        //获取前端传来的判断条件
        Integer channelId = query.getInt("channelId");
        Integer deviceId = query.getInt("deviceId");
        String offlineStart = query.getStr("offlineStart");
        String offlineEnd = query.getStr("offlineEnd");

        List<DeviceLogVO> deviceLogVOS = deviceInfoMapper.selectDeviceLog(currentSe, limit, channelId, deviceId,
                TurnTimeStamp.TurnStamp(offlineStart),TurnTimeStamp.TurnStamp(offlineEnd));

        Integer total = deviceInfoMapper.SqlTotal();

        List<DeviceLogDTO> deviceLogDTOList=new ArrayList<>();

        for(Integer i=0;i<deviceLogVOS.size();i++)
        {
            DeviceLogDTO deviceLogDTO=new DeviceLogDTO();

            deviceLogDTO.setChannelName(deviceLogVOS.get(i).getChanName());
            deviceLogDTO.setDeviceName(deviceLogVOS.get(i).getDeviceName());
            deviceLogDTO.setOffline(TurnTimeStamp.TurnStringSecond(deviceLogVOS.get(i).getOffline()));
            switch (deviceLogVOS.get(i).getContent())
            {
                case 0:
                    deviceLogDTO.setContent("异常");
                    break;
                case 1:
                    deviceLogDTO.setContent("恢复正常");
                    break;
            }

            deviceLogDTOList.add(deviceLogDTO);
        }

        //获取通道数据库名队列
        List<ChannelVO> channelVOS = deviceInfoMapper.getChannelName();
        List<ChannelDTO> channelDTOList =new ArrayList<>();

        for(Integer i=0;i<channelVOS.size();i++)
        {
            ChannelDTO channelDTO=new ChannelDTO();

            channelDTO.setChannelId(channelVOS.get(i).getPkChanId());
            channelDTO.setChannelName(channelVOS.get(i).getChanName());

            channelDTOList.add(channelDTO);
        }

        List<DeviceInfoVO> deviceInfoVOList = deviceInfoMapper.getDeviceInfo();
        List<LogDeviceDTO> logDeviceDTOList =new ArrayList<>();

        for(Integer i =0;i<deviceInfoVOList.size();i++)
        {
            LogDeviceDTO logDeviceDTO=new LogDeviceDTO();

            logDeviceDTO.setDeviceId(deviceInfoVOList.get(i).getPkDeviceId());
            logDeviceDTO.setDeviceName(deviceInfoVOList.get(i).getDeviceName());

            logDeviceDTOList.add(logDeviceDTO);
        }

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",deviceLogDTOList)
                .data("channel",channelDTOList)
                .data("device",logDeviceDTOList);
    }
}
