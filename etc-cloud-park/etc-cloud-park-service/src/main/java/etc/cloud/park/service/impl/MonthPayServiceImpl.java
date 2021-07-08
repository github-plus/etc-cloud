package etc.cloud.park.service.impl;

import cn.hutool.json.JSONObject;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.commons.tool.TurnTimeStamp;
import etc.cloud.park.dto.MonthPayDTO;
import etc.cloud.park.dto.MonthTotalDTO;
import etc.cloud.park.mapper.MonthPayMapper;
import etc.cloud.park.mode.vo.MonthPayVO;
import etc.cloud.park.mode.vo.TotalMoneyVO;
import etc.cloud.park.service.MonthPayService;
import org.bouncycastle.util.Times;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class MonthPayServiceImpl implements MonthPayService {

    @Autowired
    private MonthPayMapper monthPayMapper;

/*    private TurnTimeStamp turnTimeStamp;*/

    @Override
    public Object SelectMonthPay(String data)
    {
        //json数据解析
        JSONObject js = new JSONObject(data);

        //获取token
        String token = js.getStr("token");
        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe=(current-1)*limit;


        //分解条件
        JSONObject monthSelect = js.getJSONObject("query");

        System.out.println("输出发包"+monthSelect);

        Integer payType = monthSelect.getInt("payType");
        Integer monthStandard = monthSelect.getInt("monthStandard");
        Integer isOnline = monthSelect.getInt("isOnline");
        String room = monthSelect.getStr("room");
        String payStart = monthSelect.getStr("payStart");
        String payEnd = monthSelect.getStr("payEnd");
        String carNumber = monthSelect.getStr("carNumber");
        String operator = monthSelect.getStr("operator");
        Integer payState = monthSelect.getInt("payState");
        String orderNumber = monthSelect.getStr("orderNumber");

        List<MonthPayVO> monthPayVOS = monthPayMapper.getMonthPayVO(currentSe, limit,
                payType,monthStandard,isOnline,room,
                TurnTimeStamp.TurnStamp(payStart), TurnTimeStamp.TurnStamp(payEnd),
                carNumber,operator,payState, orderNumber);


        Integer total=monthPayMapper.SqlTotal();

        if(monthPayVOS.isEmpty())
        {
            System.out.println("发现错误");
            return null;
        }
        else
        {
            System.out.println("队列"+monthPayVOS);
        }

        //转化到dto
        List<MonthPayDTO> monthPayDTOList = new ArrayList<>();
        for(Integer i=0;i<monthPayVOS.size();i++)
        {
            MonthPayDTO monthPayDTO=new MonthPayDTO();

            monthPayDTO.setCarNumber(monthPayVOS.get(i).getCarNumber());
            monthPayDTO.setRoom(monthPayVOS.get(i).getRoom());
            monthPayDTO.setPayTime(TurnTimeStamp.TurnStringSecond(monthPayVOS.get(i).getPayTime()));
            monthPayDTO.setMonthMoney(monthPayVOS.get(i).getMonthStandard().toString()+"元/月");

            if(monthPayVOS.get(i).getPayType()!=null) {
                switch (monthPayVOS.get(i).getPayType()) {
                    case 0:
                        monthPayDTO.setPayType("微信");
                        break;
                    case 1:
                        monthPayDTO.setPayType("支付宝");
                        break;
                    case 2:
                        monthPayDTO.setPayType("银联");
                        break;
                    case 3:
                        monthPayDTO.setPayType("其他转账");
                        break;
                    default:
                        monthPayDTO.setPayType("线下支付");
                        break;
                }
            }
            else
            {
                monthPayDTO.setPayType("线下支付");
            }

            switch (monthPayVOS.get(i).getPayState())
            {
                case 0:
                    monthPayDTO.setPayState("支付失败");
                    break;
                case 1:
                    monthPayDTO.setPayState("支付成功");
                    break;
            }
            switch (monthPayVOS.get(i).getIsOnline())
            {
                case 0:
                    monthPayDTO.setIsOnline("线下支付");
                    break;
                case 1:
                    monthPayDTO.setIsOnline("线上支付");
                    break;
            }

            monthPayDTO.setRealMoney(monthPayVOS.get(i).getRealMoney());
            monthPayDTO.setRoom(monthPayVOS.get(i).getRoom());
            monthPayDTO.setOriginalValidity(TurnTimeStamp.TurnString(monthPayVOS.get(i).getOriginalValidity()));
            monthPayDTO.setValidityStart(TurnTimeStamp.TurnString(monthPayVOS.get(i).getValidityStart()));
            monthPayDTO.setValidityEnd(TurnTimeStamp.TurnString(monthPayVOS.get(i).getValidityEnd()));
            monthPayDTO.setOrderNumber(monthPayVOS.get(i).getOrderNumber());
            monthPayDTO.setOperator(monthPayVOS.get(i).getEmpName());
            monthPayDTO.setOrderNumber(monthPayVOS.get(i).getOrderNumber());

            monthPayDTOList.add(monthPayDTO);
        }

        return MsgResponse.ok()
                .data("total",total)
                .data("rows",monthPayDTOList);
    }

    @Override
    public Object getTotal(String data)
    {
        JSONObject js = new JSONObject(data);

        Integer current = js.getInt("current");
        Integer limit = js.getInt("limit");

        Integer currentSe = (current - 1) * limit;

        List<TotalMoneyVO> totalList = monthPayMapper.getTotalList(currentSe, limit);

        Integer total = monthPayMapper.SqlTotal();

        List<MonthTotalDTO> monthTotalDTOList = new ArrayList<>();

        //创建不同支付方式的获取列表
        List<TotalMoneyVO> wcTotalList = monthPayMapper.getTypeTotalList(currentSe, limit, 0);
        Integer wcInt = 0;

        List<TotalMoneyVO> aliTotalList = monthPayMapper.getTypeTotalList(currentSe, limit, 1);
        Integer aliInt = 0;

        //修改isOnline
        List<TotalMoneyVO> offlineTotalList = monthPayMapper.getOfflineTotalList(currentSe, limit);
        Integer offlienInt = 0;

        List<TotalMoneyVO> bankTotalList = monthPayMapper.getTypeTotalList(currentSe, limit, 4);
        Integer bankInt = 0;

        List<TotalMoneyVO> moneyTotalList = monthPayMapper.getTypeTotalList(currentSe, limit, 5);
        Integer moneyInt = 0;


        //进行转换添加到DTO队列，传递给前端
        for (Integer i = 0; i < totalList.size(); i++) {
            MonthTotalDTO monthTotalDTO = new MonthTotalDTO();

            monthTotalDTO.setDate(totalList.get(i).getCreateTime());
            monthTotalDTO.setTotal(totalList.get(i).getRealMoney());

            //微信
            if (wcInt < wcTotalList.size() && monthTotalDTO.getDate().equals(wcTotalList.get(wcInt).getCreateTime()))
            {
                monthTotalDTO.setWeChat(wcTotalList.get(wcInt).getRealMoney());
                wcInt++;
            }
            else
                {
                monthTotalDTO.setWeChat(0);
            }

            //支付宝
            if (aliInt < aliTotalList.size() && monthTotalDTO.getDate().equals(aliTotalList.get(aliInt).getCreateTime()))
            {
                monthTotalDTO.setALi(aliTotalList.get(aliInt).getRealMoney());
                aliInt++;
            }
            else
                {
                monthTotalDTO.setALi(0);
            }

            //线下支付
            if (offlienInt < offlineTotalList.size() && monthTotalDTO.getDate().equals(offlineTotalList.get(offlienInt).getCreateTime()))
            {
                monthTotalDTO.setOffline(offlineTotalList.get(offlienInt).getRealMoney());
                offlienInt++;
            }
            else
            {
                monthTotalDTO.setOffline(0);
            }

            //银行代缴
            if (bankInt < bankTotalList.size() && monthTotalDTO.getDate().equals(bankTotalList.get(bankInt).getCreateTime()))
            {
                monthTotalDTO.setBank(bankTotalList.get(bankInt).getRealMoney());
                bankInt++;
            }
            else
            {
                monthTotalDTO.setBank(0);
            }

            //现金
            if (moneyInt < moneyTotalList.size() && monthTotalDTO.getDate().equals(moneyTotalList.get(moneyInt).getCreateTime()))
            {
                monthTotalDTO.setMoney(bankTotalList.get(moneyInt).getRealMoney());
                moneyInt++;
            }
            else
            {
                monthTotalDTO.setMoney(0);
            }

            monthTotalDTOList.add(monthTotalDTO);
        }

        //向前端返回值
        return MsgResponse.ok().data("total", total)
                .data("rows", monthTotalDTOList);
    }
}
