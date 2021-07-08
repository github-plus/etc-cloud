package etc.cloud.park.service.impl;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.ShareCarportDTO;
import etc.cloud.park.mapper.ShareInfoMapper;
import etc.cloud.park.mode.vo.ShareInfoMonthVO;
import etc.cloud.park.service.ShareInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Service
public class ShareInfoServiceImpl implements ShareInfoService {

    @Autowired
    private ShareInfoMapper shareInfoMapper;

    @Override
    public Object ShareInfo(String data)
    {
        return null;
    }

    @Override
    public Object ShareSelect(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");
        //分解条件
        JSONObject query = js.getJSONObject("query");

        Integer currentSe=(current-1)*limit;

        String carNumber = query.getStr("carNumber");
        String validityEndStart = query.getStr("validityEndStart");
        String validityEndEnd = query.getStr("validityEndEnd");
        String userName = query.getStr("userName");
        String room = query.getStr("room");
        String carport = query.getStr("carport");

        System.out.println("房间号"+room+"号");
        System.out.println("车牌号码"+carNumber);

        List<ShareInfoMonthVO> shareSelectWrapper = shareInfoMapper.getShareSelectWrapper(currentSe,
                limit,
                room,
                carNumber,
                userName,
                carport,
                TurnTimeStamp.TurnStamp(validityEndStart),
                TurnTimeStamp.TurnStamp(validityEndEnd));

        Integer total = shareInfoMapper.SqlTotal();

        //返回给前端的数据
        List<ShareCarportDTO> shareCarportDTOList=new ArrayList<>();

        for(Integer i=0;i<shareSelectWrapper.size();i++)
        {
            ShareCarportDTO shareCarportDTO=new ShareCarportDTO();

            shareCarportDTO.setMonthId(shareSelectWrapper.get(i).getMonthId());
            shareCarportDTO.setCarNumber(shareSelectWrapper.get(i).getCarNumber());
            shareCarportDTO.setMonthStandardName("月租车"+shareSelectWrapper.get(i).getMonthStandard().toString());
            shareCarportDTO.setValidityStart(TurnTimeStamp.TurnString(shareSelectWrapper.get(i).getValidityStart()));
            shareCarportDTO.setValidityEnd(TurnTimeStamp.TurnString(shareSelectWrapper.get(i).getValidityEnd()));
            shareCarportDTO.setUserName(shareSelectWrapper.get(i).getUserName());
            shareCarportDTO.setRoom(shareSelectWrapper.get(i).getRoom());
            shareCarportDTO.setCarPort(shareSelectWrapper.get(i).getCarPort());

            shareCarportDTOList.add(shareCarportDTO);
        }

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",shareCarportDTOList);
    }
}
