package etc.cloud.park.service;

import etc.cloud.park.mode.MonitoringCenterModel;

public interface MonthlyCarPayRulesService {
    //月租车规则设置的接口
    //查询
    Object inqueryMonthlyCarPayRulesHandler(String data);
    //新增
    Object addMonthlyCarPayRulesHandler( String data,String token);
    //修改
    Object alterMonthlyCarPayRulesHandler( String data,String token);
    //删除
    Object deleteMonthlyCarPayRulesHandler( String data,String token);
    //更新线上支付模式
    Object updateMonthlyCarPayModeHandler( String data,String token);
}
