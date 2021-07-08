package etc.cloud.park.mode;

import com.baomidou.mybatisplus.annotation.TableName;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Data
@Accessors(chain = true)
@TableName(value = "channel")
public class PassagewayModel {
    //通道ID
    private int pkChanId;
    //监控中心ID
    private int centerId;
    //通道名称
    private String chanName;
    //通道类型：0 大车场 1 小车场
    private int chanType;
    //进出通道：0 标准进  1 标准出
    private int enLeType;
    //主要识别车牌的设备IP
    private String deMainReIp;
    //主要识别车牌的设备端口
    private int deMainReport;
    //副识别车牌的设备IP
    private String deSndReIp;
    //副识别车牌的设备端口
    private int deSndRePort;
    //显示屏IP(Vs为ViewScreen)
    private String deVsIp;
    //显示屏端口
    private int deVsPort;
    //	ETCIP
    private String deEtcIp;
    //	ETC端口
    private int deEtcPort;
    //	所属区域（停车场多块划分）：0 A 1 B 3 C 4 D
    private int beArea;
    //	通道参数id（外键）1进2出
    private int channelParamId;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
