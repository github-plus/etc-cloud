package etc.cloud.gateway.rest;


import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import etc.cloud.auth.mapper.OrganManageMapper;
import etc.cloud.auth.mode.OrganRelationModel;
import etc.cloud.commons.mode.MsgResponse;
import etc.cloud.gateway.service.impl.LoginFilterServiceImpl;
import etc.cloud.gateway.service.impl.OrganRelationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("etc-cloud/organ")
@CrossOrigin
public class OrganRelationController {

    @Autowired
    private OrganRelationServiceImpl organRelationService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private LoginFilterServiceImpl loginFilterService;

    @Autowired
    private OrganManageMapper organManageMapper;

    //增加层级
    @PostMapping("/addlevel")
    public MsgResponse addLevel(@RequestBody String data){

        //获取token
        String token = request.getHeader("X-Token");
        //查询该账号是否有登录
        String account = loginFilterService.userFilter(token);

        if (account == null){
            return MsgResponse.error().msg("登录已过期");
        }
        //查询该账号是否有这个接口的权限
        Boolean menu = loginFilterService.findMenu(account, 18);
        if(menu == null) return MsgResponse.error().msg("该账号没有这个权限");


        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //JSONArray array = jsonObject.getJSONArray("organRelation");
        JSONObject jsonObject1 = jsonObject.getJSONObject("organRelation");
        //获取OrganRelationModel对象
        OrganRelationModel organRelationModel = new OrganRelationModel();
        System.out.println("要添加的子层："+data);
            organRelationModel.setOrganFather(jsonObject1.getStr("organFather"));
            organRelationModel.setOrganId(jsonObject1.getStr("organSon"));
        String organSon = organManageMapper.findOrganName(jsonObject1.getStr("organSon"));
        //存进去名称
        organRelationModel.setOrganName(organSon);

        boolean add = organRelationService.save(organRelationModel);

        if (add){
            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }

}
