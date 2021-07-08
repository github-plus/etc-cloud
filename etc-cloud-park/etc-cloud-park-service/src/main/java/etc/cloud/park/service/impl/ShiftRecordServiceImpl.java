package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.ShiftRecordDTO;
import etc.cloud.park.mapper.ShiftRecordMapper;
import etc.cloud.park.mode.ShiftRecordModel;
import etc.cloud.park.service.ShiftRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShiftRecordServiceImpl implements ShiftRecordService {

    @Autowired
    private ShiftRecordMapper shiftRecordMapper;

    public Object SelectShiftRecord(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;

        //分解条件
        JSONObject query = js.getJSONObject("query");

        String operatorName = query.getStr("operatorName");
        String loginStart = query.getStr("loginStart");
        String loginEnd = query.getStr("loginEnd");

        List<ShiftRecordModel> shiftRecord = shiftRecordMapper.getShiftRecord(currentSe, limit,
                TurnTimeStamp.TurnStamp(loginStart),TurnTimeStamp.TurnStamp(loginEnd),operatorName);

        Integer total = shiftRecordMapper.SqlTotal();

        List<ShiftRecordDTO> shiftRecordDTOList =new ArrayList<>();

        for(Integer i=0;i<shiftRecord.size();i++)
        {
            ShiftRecordDTO shiftRecordDTO=new ShiftRecordDTO();

            shiftRecordDTO.setOperatorName(shiftRecord.get(i).getOperatorName());
            shiftRecordDTO.setLoginTime(TurnTimeStamp.TurnString(shiftRecord.get(i).getLoginTime()));
            shiftRecordDTO.setExitTime(TurnTimeStamp.TurnString(shiftRecord.get(i).getExitTime()));
            shiftRecordDTO.setDerateMoney(shiftRecord.get(i).getDerateMoney());
            shiftRecordDTO.setMoney(shiftRecord.get(i).getMoney());
            shiftRecordDTO.setInDeal(shiftRecord.get(i).getInDeal());
            shiftRecordDTO.setOpenGate(shiftRecord.get(i).getOpenGate());
            shiftRecordDTO.setOutDeal(shiftRecord.get(i).getOutDeal());

            shiftRecordDTOList.add(shiftRecordDTO);
        }

        List<String> operatorNameList = shiftRecordMapper.getOperatorName();

        return MsgResponse.ok().data("total",total)
                .data("rows",shiftRecordDTOList)
                .data("operatorName",operatorNameList);
    }
}
