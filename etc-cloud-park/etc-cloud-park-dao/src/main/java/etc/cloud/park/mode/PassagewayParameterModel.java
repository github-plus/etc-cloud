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
@TableName(value = "channel_param")
public class PassagewayParameterModel {
    private int channelParamId;//	通道参数ID :1 进口 2出口	Int
    private int isUnusual;//	是否开启异常等待:0不等待 1 等待	Int
    private int unusualTime;//	异常秒数	Int
    private int defaultCenterId;//	默认处理监控中心编号	Int
    private int overTime;//	超时时间	Int
    private int overCenterId;//	超时处理监控中心	Int
    private int monthDim;//	月租车车牌模糊对比：0  6位对比 1  5位对比  2  4位对比   	Int
    private int identification;//	识别可信度	Int
    private int isCorrect;//	识别可信度低于N是否纠正：0不纠正 1纠正	Int
    private int isOpenTempCross;//	是否开启临时车通行时间表：0 否 1 是（默认值0）	Int
    private String tempInTime;//	允许临时车通行时间段（默认值00:00-00:00）	String
    private String voiceControlTimeFirst;//	音量控制首段时间（默认时间00：00-00:00）	String
    private int voiceControlFirst;//	语音控制（音量）	Int
    private String voiceControlTimeSecond;//	音量控制二段时间（默认时间00：00-00:00）	String
    private int voiceControlSecond;//	语音控制（音量）	Int
    private int chanPlaceType;//	通道画面类型：0 九宫格画面 1 四格画面 2 六格画面 3 十二格画面	Int
    private int chanPlace;//	通道画面位置(九宫格)：1-12	Int
    private String showScreen;//  万能语音显示文字： String




    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
