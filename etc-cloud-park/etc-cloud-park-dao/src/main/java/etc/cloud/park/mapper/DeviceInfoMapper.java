package etc.cloud.park.mapper;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import etc.cloud.commons.constants.CommonConstants;
import etc.cloud.park.mode.DeviceInfoModel;
import etc.cloud.park.mode.vo.ChannelVO;
import etc.cloud.park.mode.vo.DeviceInfoVO;
import etc.cloud.park.mode.vo.DeviceLogVO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
@DS(value = CommonConstants.DB_NAME)
public interface DeviceInfoMapper extends BaseMapper<DeviceInfoModel> {
    @Select("SELECT * FROM device_info " +
            "LEFT JOIN channel on channel_id=pk_chan_id " +
            "limit #{currentSe},#{limitSe}")
    List<DeviceInfoVO> getDeviceInfoVO(@Param(value = "currentSe") Integer currentSe,
                                       @Param(value = "limitSe") Integer limitSe);

    @Select("SELECT * FROM channel")
    List<ChannelVO> getChannelName();

    @Select("SELECT pk_device_id,device_name FROM device_info")
    List<DeviceInfoVO> getDeviceInfo();

    @Select({"<script>"+
            "SELECT SQL_CALC_FOUND_ROWS * FROM device_info " +
            "LEFT JOIN channel on channel_id=pk_chan_id " +
            "WHERE 1=1 " +
            "<if test='channelId!=null'>and pk_chan_id = #{channelId} </if>" +
            "limit #{currentSe},#{limitSe}" +
            "</script>"})
    List<DeviceInfoVO> selectDeviceInfoVO(@Param(value = "currentSe") Integer currentSe,
                                          @Param(value = "limitSe") Integer limitSe,
                                          @Param(value = "channelId") Integer channelId);

    @Select("SELECT FOUND_ROWS()")
    Integer SqlTotal();

    @Select({"<script>" +
            "SELECT SQL_CALC_FOUND_ROWS * FROM device_log " +
            "LEFT JOIN device_info on device_log.device_id = device_info.pk_device_id " +
            "LEFT JOIN channel on device_info.channel_id=channel.pk_chan_id " +
            "WHERE 1=1 "+
            "<if test='channelId!=null'>and channel_id = #{channelId} </if>" +
            "<if test='deviceId!=null'>and device_id = #{deviceId} </if>" +
            "<if test='offlineStart'>and UNIX_TIMESTAMP(offline) &gt;= UNIX_TIMESTAMP(#{offlineStart}) </if>" +
            "<if test='offlineEnd'>and UNIX_TIMESTAMP(offline) &lt;= UNIX_TIMESTAMP(#{offlineEnd}) </if>" +
            "limit #{currentSe},#{limitSe}" +
            "</script>"})
        List<DeviceLogVO> selectDeviceLog(@Param(value = "currentSe") Integer currentSe,
                                          @Param(value = "limitSe") Integer limitSe,
                                          @Param(value = "channelId") Integer channelId,
                                          @Param(value = "deviceId") Integer deviceId,
                                          @Param(value = "offlineStart")Timestamp offlineStart,
                                          @Param(value = "offlineEnd") Timestamp offlineEnd);
}
