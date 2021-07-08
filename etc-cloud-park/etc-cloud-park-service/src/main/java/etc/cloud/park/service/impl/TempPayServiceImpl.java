package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.TempOnlienDTO;
import etc.cloud.park.dto.TempPayDTO;
import etc.cloud.park.dto.TempTotalDTO;
import etc.cloud.park.mapper.TempPayMapper;
import etc.cloud.park.mode.vo.TempOnlineVO;
import etc.cloud.park.mode.vo.TempPayVO;
import etc.cloud.park.mode.vo.TempTotalMoneyVO;
import etc.cloud.park.mode.vo.TotalMoneyVO;
import etc.cloud.park.service.TempPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TempPayServiceImpl implements TempPayService {

    @Autowired
    private TempPayMapper tempPayMapper;

    @Override
    public Object SelectTempPay(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;

        //分解条件
        JSONObject tempPaySelect = js.getJSONObject("query");

        Integer payType = tempPaySelect.getInt("payType");
        String freeType = tempPaySelect.getStr("freeType");
        String discountName = tempPaySelect.getStr("discountName");
        String channel = tempPaySelect.getStr("channel");
        String outStart = tempPaySelect.getStr("outStart");
        String outEnd = tempPaySelect.getStr("outEnd");
        String carNumber = tempPaySelect.getStr("carNumber");
        String operator = tempPaySelect.getStr("operator");
        String tempStandard = tempPaySelect.getStr("tempStandard");
        String orderNumber = tempPaySelect.getStr("orderNumber");

        List<TempPayVO> tempPayVOS = tempPayMapper.getTempPayVO(currentSe, limit,
                payType,freeType,discountName,channel,TurnTimeStamp.TurnStamp(outStart),TurnTimeStamp.TurnStamp(outEnd),
                carNumber,operator,tempStandard,orderNumber);

        Integer total = tempPayMapper.SqlTotal();

        List<TempPayDTO> tempPayDTOList = new ArrayList<>();

        for(Integer i =0;i<tempPayVOS.size();i++)
        {
            TempPayDTO tempPayDTO=new TempPayDTO();

            tempPayDTO.setCarNumber(tempPayVOS.get(i).getCarNumber());
            tempPayDTO.setInTime(TurnTimeStamp.TurnStringSecond(tempPayVOS.get(i).getInTime()));
            tempPayDTO.setOutTime(TurnTimeStamp.TurnStringSecond(tempPayVOS.get(i).getOutTime()));
            tempPayDTO.setParkTime(tempPayVOS.get(i).getParkTime());
            tempPayDTO.setTempStandard(tempPayVOS.get(i).getTempStandardName());
            tempPayDTO.setMoney(tempPayVOS.get(i).getMoney());
            tempPayDTO.setDerateMoney(tempPayVOS.get(i).getDerateMoney());
            tempPayDTO.setRealMoney(tempPayVOS.get(i).getRealMoney());

            if(tempPayVOS.get(i).getIsOnline()==1)
            {
                tempPayDTO.setPayOnline(tempPayVOS.get(i).getRealMoney());
            }
            else
            {
                tempPayDTO.setPayOnline(0);
            }

            tempPayDTO.setDiscountName(tempPayVOS.get(i).getDiscountName());
            tempPayDTO.setOutOperator(tempPayVOS.get(i).getOutOperator());
            tempPayDTO.setOutChannel(tempPayVOS.get(i).getOutChannel());
            tempPayDTO.setFreeType(tempPayVOS.get(i).getFreeType());
            tempPayDTO.setImage(tempPayVOS.get(i).getImage());

            tempPayDTOList.add(tempPayDTO);
        }

        //返回检索列表，缺商户优惠方案名称，未建表
        List<String> channelNameList = tempPayMapper.getChannelName();
        List<String> freeTypeNameList = tempPayMapper.getFreeTypeName();
        List<String> tempStandardNameList = tempPayMapper.getTempStandardName();
        List<String> operatorNameList = tempPayMapper.getOperatorName();

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",tempPayDTOList)
                .data("channelName",channelNameList)
                .data("freeType",freeTypeNameList)
                .data("tempStandard",tempStandardNameList)
                .data("operator",operatorNameList);
    }

    @Override
    public Object SelectTempTotal(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;

        List<TempTotalMoneyVO> totalList = tempPayMapper.getTotalList(currentSe, limit);

        Integer total = tempPayMapper.SqlTotal();

        List<TotalMoneyVO> offLineTotal = tempPayMapper.getIsOnlineTotalList(currentSe, limit, 0);
        Integer offLineInt =0;

        List<TotalMoneyVO> onlineTotal = tempPayMapper.getIsOnlineTotalList(currentSe, limit, 1);
        Integer onLineInt=0;

        List<TempTotalDTO> tempTotalDTOList=new ArrayList<>();

        for(Integer i=0;i<totalList.size();i++)
        {
            TempTotalDTO tempTotalDTO=new TempTotalDTO();

            tempTotalDTO.setDate(totalList.get(i).getDate());
            tempTotalDTO.setMoney(totalList.get(i).getMoney());
            tempTotalDTO.setDiscountMoney(totalList.get(i).getDerateMoney());
            tempTotalDTO.setTotalRealMoney(totalList.get(i).getRealMoney());

            //暂为空
            tempTotalDTO.setFreeMoney(0);

            //线下人工收费
            if(offLineInt<offLineTotal.size()&& totalList.get(i).getDate().equals(offLineTotal.get(offLineInt).getCreateTime()))
            {
                tempTotalDTO.setOfflineMoney(offLineTotal.get(offLineInt).getRealMoney());
                offLineInt++;
            }
            else
            {
                tempTotalDTO.setOfflineMoney(0);
            }

            //线上收费
            if(onLineInt<onlineTotal.size()&& totalList.get(i).getDate().equals(onlineTotal.get(onLineInt).getCreateTime()))
            {
                tempTotalDTO.setOnlineMoney(onlineTotal.get(onLineInt).getRealMoney());
                onLineInt++;
            }
            else
            {
                tempTotalDTO.setOnlineMoney(0);
            }

            tempTotalDTOList.add(tempTotalDTO);
        }

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",tempTotalDTOList);
    }

    @Override
    public Object SelectOfflinePay(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;
        //分解条件
        JSONObject tempPaySelect = js.getJSONObject("query");

        Integer payType = tempPaySelect.getInt("payType");
        String freeType = tempPaySelect.getStr("freeType");
        String discountName = tempPaySelect.getStr("discountName");
        String channel = tempPaySelect.getStr("channel");
        String outStart = tempPaySelect.getStr("outStart");
        String outEnd = tempPaySelect.getStr("outEnd");
        String carNumber = tempPaySelect.getStr("carNumber");
        String operator = tempPaySelect.getStr("operator");
        String tempStandard = tempPaySelect.getStr("tempStandard");
        String orderNumber = tempPaySelect.getStr("orderNumber");

        List<TempPayVO> tempPayVOS = tempPayMapper.getTempOffline(currentSe, limit,
                payType,freeType,discountName,channel,TurnTimeStamp.TurnStamp(outStart),TurnTimeStamp.TurnStamp(outEnd),
                carNumber,operator,tempStandard,orderNumber);

        Integer total = tempPayMapper.SqlTotal();

        List<TempPayDTO> tempPayDTOList = new ArrayList<>();

        for(Integer i =0;i<tempPayVOS.size();i++)
        {
            TempPayDTO tempPayDTO=new TempPayDTO();

            tempPayDTO.setCarNumber(tempPayVOS.get(i).getCarNumber());
            tempPayDTO.setInTime(TurnTimeStamp.TurnStringSecond(tempPayVOS.get(i).getInTime()));
            tempPayDTO.setOutTime(TurnTimeStamp.TurnStringSecond(tempPayVOS.get(i).getOutTime()));
            tempPayDTO.setParkTime(tempPayVOS.get(i).getParkTime());
            tempPayDTO.setTempStandard(tempPayVOS.get(i).getTempStandardName());
            tempPayDTO.setMoney(tempPayVOS.get(i).getMoney());
            tempPayDTO.setDerateMoney(tempPayVOS.get(i).getDerateMoney());
            tempPayDTO.setRealMoney(tempPayVOS.get(i).getRealMoney());

            if(tempPayVOS.get(i).getIsOnline()==1)
            {
                tempPayDTO.setPayOnline(tempPayVOS.get(i).getRealMoney());
            }
            else
            {
                tempPayDTO.setPayOnline(0);
            }

            tempPayDTO.setDiscountName(tempPayVOS.get(i).getDiscountName());
            tempPayDTO.setOutOperator(tempPayVOS.get(i).getOutOperator());
            tempPayDTO.setOutChannel(tempPayVOS.get(i).getOutChannel());
            tempPayDTO.setFreeType(tempPayVOS.get(i).getFreeType());
            tempPayDTO.setImage(tempPayVOS.get(i).getImage());

            tempPayDTOList.add(tempPayDTO);
        }

        //返回检索列表，缺商户优惠方案名称，未建表
        List<String> channelNameList = tempPayMapper.getChannelName();
        List<String> freeTypeNameList = tempPayMapper.getFreeTypeName();
        List<String> tempStandardNameList = tempPayMapper.getTempStandardName();
        List<String> operatorNameList = tempPayMapper.getOperatorName();

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",tempPayDTOList)
                .data("channel",channelNameList)
                .data("freeType",freeTypeNameList)
                .data("tempStandard",tempStandardNameList)
                .data("operator",operatorNameList);
    }

    @Override
    public Object SelectOnlinePay(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;
        //分解条件
        JSONObject tempPaySelect = js.getJSONObject("query");

        Integer payType = tempPaySelect.getInt("payType");
        String discountName = tempPaySelect.getStr("discountName");
        String merchantName = tempPaySelect.getStr("merchantName");
        String payStart = tempPaySelect.getStr("payStart");
        String payEnd = tempPaySelect.getStr("payEnd");
        String carNumber = tempPaySelect.getStr("carNumber");
        String operator = tempPaySelect.getStr("operator");
        String tempStandard = tempPaySelect.getStr("tempStandard");
        String orderNumber = tempPaySelect.getStr("orderNumber");

        List<TempOnlineVO> tempOnlineVOList = tempPayMapper.getTempOnline(currentSe, limit, payType, merchantName, discountName,
                TurnTimeStamp.TurnStamp(payStart), TurnTimeStamp.TurnStamp(payEnd),
                carNumber, operator, tempStandard, orderNumber);

        Integer total = tempPayMapper.SqlTotal();

        List<TempOnlienDTO> tempOnlienDTOList=new ArrayList<>();

        for(Integer i=0;i<tempOnlineVOList.size();i++)
        {
            TempOnlienDTO tempOnlienDTO=new TempOnlienDTO();

            tempOnlienDTO.setCarNmuber(tempOnlineVOList.get(i).getCarNumber());
            tempOnlienDTO.setInTime(TurnTimeStamp.TurnStringSecond(tempOnlineVOList.get(i).getInTime()));
            tempOnlienDTO.setOutTime(TurnTimeStamp.TurnStringSecond(tempOnlineVOList.get(i).getOutTime()));
            tempOnlienDTO.setMerchantName(tempOnlineVOList.get(i).getMerchantName());
            tempOnlienDTO.setDiscountName(tempOnlineVOList.get(i).getDiscountName());
            tempOnlienDTO.setOrderNumber(tempOnlineVOList.get(i).getOrderNumber());
            tempOnlienDTO.setOperator(tempOnlineVOList.get(i).getOutOperator());
            tempOnlienDTO.setParkTime(tempOnlineVOList.get(i).getStayTime());
            tempOnlienDTO.setTempStandard(tempOnlineVOList.get(i).getTempStandardName());
            tempOnlienDTO.setImage(tempOnlineVOList.get(i).getImage());
            tempOnlienDTO.setPayTime(TurnTimeStamp.TurnStringSecond(tempOnlineVOList.get(i).getCreateTime()));
            tempOnlienDTO.setPayMoney(tempOnlineVOList.get(i).getRealMoney().toString());

            switch (tempOnlineVOList.get(i).getPayType())
            {
                case 0:
                    tempOnlienDTO.setPayType("微信");
                    break;
                case 1:
                    tempOnlienDTO.setPayType("支付宝");
                    break;
                case 2:
                    tempOnlienDTO.setPayType("etc");
                    break;
                case 3:
                    tempOnlienDTO.setPayType("银联");
                    break;
            }

            tempOnlienDTOList.add(tempOnlienDTO);
        }

        List<String> operatorNameList = tempPayMapper.getOperatorName();
        List<String> tempStandardNameList = tempPayMapper.getTempStandardName();

        List<String> discountNameList=new ArrayList<>();
        discountNameList.add("盒马免费2小时");
        discountNameList.add("华源减免20");

        List<String> merchantNameList=new ArrayList<>();
        merchantNameList.add("盒马");
        merchantNameList.add("华源");

        return MsgResponse.ok().data("total",total)
                .data("rows",tempOnlienDTOList)
                .data("operator",operatorNameList)
                .data("tempStandard",tempStandardNameList)
                .data("discountName",discountNameList)
                .data("merchantName",merchantNameList);
    }
}
