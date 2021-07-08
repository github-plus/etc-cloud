package etc.cloud.auth.vo;


import lombok.Data;

@Data
public class HomeVO {

    //项目总数
    private int projectTotal;

    //车场总数
    private int parkTotal;

    //车位总数
    private int carTotal;

    //剩余车位
    private int residueCarTotal;

    //每个停车场的车位总数
    private int carSum;

    //月租车车位
    private int monthCarTotal;

    //临时车位
    private int tempCarTotal;

}
