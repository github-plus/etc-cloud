package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.LogDTO;
import etc.cloud.park.mapper.FreeTypeMapper;
import etc.cloud.park.mapper.LogMapper;
import etc.cloud.park.mode.FreeTypeModel;
import etc.cloud.park.mode.LogModel;
import etc.cloud.park.service.FreeTypeService;
import etc.cloud.park.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, LogModel> implements LogService {
    @Autowired
    private LogMapper logMapper;
    //新增日志
    @Override
    public Object addLog(LogModel logModel) {
        //职工ID,模块名称,
        int i = logMapper.insertLog(logModel);
        if(i!=-1) return MsgResponse.ok();
        else {return MsgResponse.error();}
    }
    //查询日志
    @Override
    public Object inquiryOfLog(String data) {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //分解数据
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");
        JSONObject jsonObject = js.getJSONObject("stus");
        //计算起始位置
        Integer currentSe=(current-1)*limit;
        //接收实体类属性
        String empId = jsonObject.getStr("empId");
        String community = jsonObject.getStr("community");
        String modeName = jsonObject.getStr("modeName");
        String impName = jsonObject.getStr("impName");
        String opType = jsonObject.getStr("opType");
        String opContent = jsonObject.getStr("opContent");
        String carNumber = jsonObject.getStr("carNumber");
        String opResult = jsonObject.getStr("opResult");
        String beginTime = jsonObject.getStr("beginTime");
        String endTime = jsonObject.getStr("endTime");


        //Mapper中的方法取数据，然后存入Model类
        List<LogModel> list = logMapper.inquiryOfLog(
                currentSe,
                limit,
                empId,
                community,
                modeName,
                impName,
                opType,
                opContent,
                carNumber,
                opResult,
                TurnTimeStamp.TurnStamp(beginTime),
                TurnTimeStamp.TurnStamp(endTime));

        List<LogDTO> logDTOList=new ArrayList<>();

        for(Integer i=0;i<list.size();i++){
            LogDTO logDTO = new LogDTO();
            logDTO.setPkId(list.get(i).getPkId());
            logDTO.setEmpId(list.get(i).getEmpId());
            logDTO.setCarNumber(list.get(i).getCarNumber());
            logDTO.setCommunity(list.get(i).getCommunity());
            logDTO.setImpName(list.get(i).getImpName());
            logDTO.setModeName(list.get(i).getModeName());
            logDTO.setOpContent(list.get(i).getOpContent());
            logDTO.setOpResult(list.get(i).getOpResult());
            logDTO.setOpTime(TurnTimeStamp.TurnString(list.get(i).getOpTime()));
            logDTO.setOpType(list.get(i).getOpType());

            //存入DTO队列
            logDTOList.add(logDTO);
        }

        return MsgResponse.ok()
                .data("total",logDTOList.size())
                .data("rows",logDTOList);
    }
}
