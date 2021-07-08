package etc.cloud.commons.mode;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.gson.Gson;
import lombok.*;


import java.util.HashMap;
import java.util.Map;

/**
 * @author xlm
 */
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@AllArgsConstructor
//@NoArgsConstructor
@Data
public class MsgResponse {


    @JsonIgnoreProperties(ignoreUnknown = true)
    private Map<String, Object> data = new HashMap<String, Object>();

    private String msg;

    private String token;
    @NonNull
    private Integer code;
    private boolean flag;


    //登陆成功的静态方法
    public static MsgResponse login(String token) {
        return MsgResponse.builder().token(token).code(200).flag(true).build();
    }

    public static MsgResponse ofFail(String msg) {
        return MsgResponse.builder().code(500).msg(msg).flag(false).build();
    }
    /*
    public static MsgResponse ofSuccessMsg() {
        return MsgResponse.builder().msg("sucess").code(200).flag(true).build();
    }

    public static MsgResponse success(Object data) {
        return MsgResponse.builder().msg("sucess").code(200).flag(true).data(data).build();
    }

    public static MsgResponse ofSuccessMsg(String msg, Object data) {
        return MsgResponse.builder().msg(msg).data(data).code(200).flag(true).build();
    }

    public static MsgResponse ofFail(int code, String msg) {
        return MsgResponse.builder().code(code).msg(msg).flag(false).build();
    }



    public static MsgResponse ofThrowable(int code, Throwable throwable) {
        return MsgResponse.builder().msg(throwable.getClass().getName() + ", " + throwable.getMessage()).code(code).flag(false).build();
    }

     */


    @Override
    public String toString() {
        return new Gson().toJson(this);
    }

    //把构造方法私有
    private MsgResponse() {}

    //成功的静态方法
    public static MsgResponse ok(){
        MsgResponse msgResponse = new MsgResponse();
        msgResponse.setFlag(true);
        msgResponse.setCode(ResultCode.SUCCESS);
        msgResponse.setMsg("成功");
        return msgResponse;
    }


    //失败的静态方法
    public static MsgResponse error(){
        MsgResponse msgResponse = new MsgResponse();
        msgResponse.setFlag(false);
        msgResponse.setCode(ResultCode.ERROR);
        msgResponse.setMsg("失败");
        return msgResponse;
    }

    public MsgResponse success(Boolean success){
        this.setFlag(success);
        return this;
    }

    public MsgResponse msg(String msg){
        this.setMsg(msg);
        return this;
    }

    public MsgResponse token(String token){
        this.setToken(token);
        return this;
    }

    public MsgResponse code(Integer code){
        this.setCode(code);
        return this;
    }

    public MsgResponse data(String key, Object value){
        this.data.put(key, value);

        return this;
    }

    public MsgResponse data(Map<String, Object> map){
        this.setData(map);
        return this;
    }
}
