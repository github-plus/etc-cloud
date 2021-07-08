package etc.cloud.gateway.service.impl;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import etc.cloud.auth.mapper.OrganManageMapper;

import etc.cloud.auth.mode.OrganManageModel;

import etc.cloud.auth.mode.OrganRelationModel;
import etc.cloud.auth.vo.OrganRelationVO;
import etc.cloud.auth.service.IOrganManageService;
import etc.cloud.commons.mode.MsgResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class OrganManageServiceImpl extends ServiceImpl<OrganManageMapper, OrganManageModel> implements IOrganManageService {

    @Autowired
    private OrganManageMapper organManageMapper;

    @Autowired
    private OrganRelationServiceImpl organRelationService;

    //添加组织部门
    public Object addOrgan(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //JSONArray array = jsonObject.getJSONArray("organManage");
        JSONObject jsonObject1 = jsonObject.getJSONObject("query");
        //获取organManageModel对象
        OrganManageModel organManageModel = new OrganManageModel();

        organManageModel.setOrganName(jsonObject1.getStr("organName"));
        organManageModel.setOrganType(jsonObject1.getInt("organType"));
        organManageModel.setOrganDescribe(jsonObject1.getStr("organDescribe"));
        organManageModel.setProvince(jsonObject1.getStr("province"));
        organManageModel.setCity(jsonObject1.getStr("city"));
        organManageModel.setCounty(jsonObject1.getStr("county"));
        organManageModel.setAddress(jsonObject1.getStr("address"));
        organManageModel.setTel(jsonObject1.getStr("tel"));

        String organId = UUID.randomUUID().toString();
        organManageModel.setOrganId(organId);
        //开始添加至数据库
        boolean add = this.save(organManageModel);
        if (add){
            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }

    //修改组织部门
    public Object updateOrgan(String data){

        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //JSONArray array = jsonObject.getJSONArray("organManage");
        JSONObject jsonObject1 = jsonObject.getJSONObject("query");
        //获取organManageModel对象
        OrganManageModel organManageModel = new OrganManageModel();

        organManageModel.setOrganId(jsonObject1.getStr("organId"));
        organManageModel.setOrganName(jsonObject1.getStr("organName"));
        organManageModel.setOrganType(jsonObject1.getInt("organType"));
        organManageModel.setOrganDescribe(jsonObject1.getStr("organDescribe"));
        organManageModel.setProvince(jsonObject1.getStr("province"));
        organManageModel.setCity(jsonObject1.getStr("city"));
        organManageModel.setCounty(jsonObject1.getStr("county"));
        organManageModel.setAddress(jsonObject1.getStr("address"));
        organManageModel.setTel(jsonObject1.getStr("tel"));
        //开始添加至数据库
        boolean add = this.updateById(organManageModel);
        if (add){
            return MsgResponse.ok();
        }
        return MsgResponse.error();
    }

    //删除组织部门
    public Object deleteOrgan(String data){
        //json数据解析
        JSONObject jsonObject = new JSONObject(data);

        //获取token
        String token = jsonObject.getStr("token");

        //要删除的部门id
        String organId = jsonObject.getStr("organId");

        //开始删除 删除组织
        boolean deleteOrgan = this.removeById(organId);

        //删除子层结构
        Boolean deletelevel = organManageMapper.deletelevel(organId);

        //删除失败
        return MsgResponse.ok().msg("删除成功");
    }

    //查询组织部门
    public Object findAllOrgan(){

        List<OrganRelationVO> allOrgan = organManageMapper.findAllOrgan();
        //查询总个数
        int total = allOrgan.size();

        //插入子部门
        for (int i = 0; i < total; i++){

            String organFather = allOrgan.get(i).getOrganId();
            //获得子层级
            List<OrganRelationVO> organRelationVOS = organManageMapper.findlevel(organFather);
            //将获得的子层结构插入父层中
            allOrgan.get(i).setSonlevel(organRelationVOS);
        }

        List<OrganRelationVO> levelOption = organManageMapper.findLevelOption();
        return MsgResponse.ok().data("total",total).data("rows",allOrgan).data("level",levelOption);
    }

}
