package com.lty.zgj.driver.WebSocket;

import android.content.Context;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.lty.zgj.driver.core.config.Constant;
import com.lty.zgj.driver.core.tool.AppUtils;
import com.lty.zgj.driver.core.tool.MD5Util;
import com.lty.zgj.driver.core.tool.UUIDUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/7.
 */

public class WebSocketManager {


    private static final String THIS_FILE = "WebSocketManager";
    private String socketRequestStrMd5Json;


    /**
     * 获取全局的WebSocketManager
     */
    public WebSocketManager() {
    }

    public static WebSocketManager getInstance(Context context){
        return WebSocketManagerInstance.instance;
    }
    private static  class WebSocketManagerInstance{
        private static WebSocketManager instance=new WebSocketManager();
    }


    /**
     * @param msgId 业务消息类型  司机用户密码登录 0x101
     * @param token 司机用户token
     * @param bodyParamsMap  需要传递的 body 数据map 集合
     * @return
     */
    public String webSocketRequest(Context context, int msgId, String token, Map<String, Object> bodyParamsMap){

        WebSocketResponse.HeaderBean header = new WebSocketResponse.HeaderBean();
        WebSocketResponse.LappendBodyBean appendBody = new WebSocketResponse.LappendBodyBean();

        //设置请求头参数
        header.setMsgId(msgId); //设置 业务消息类型
        header.setMsgSn(String.valueOf(timestamp()));
//        header.setMsgSn("123");
        Log.e(THIS_FILE, "timestamp-----"+timestamp());
        header.setMsgVn(Constant.MSG_VN);//协议版本号
        //设置lappendBody参数
        appendBody.setDevSn(UUIDUtils.getUniquePsuedoID()); //设备唯一ID
//        appendBody.setDevSn("aaaaaaaaaa"); //设备唯一ID
        Log.e(THIS_FILE, "UUID-----"+UUIDUtils.getUniquePsuedoID());
        appendBody.setToken(token);
        appendBody.setDevType(0);//0:安卓。1:ios
        appendBody.setAppVn(AppUtils.getVersionName(context)); //设置app版本号
//        appendBody.setAppVn("1.5"); //设置app版本号

        //设置数据
        Map<String, Object> params = new HashMap<>();
        params.put("header", header);
        params.put("body", bodyParamsMap);
        params.put("appendBody", appendBody);

        JSONObject jsonObject = new JSONObject(params);
        String jsonString = jsonObject.toJSONString();
        Log.e(THIS_FILE, "WebSocketResponse--json---字符串---"+UUIDUtils.getUniquePsuedoID());

        return jsonString;
    }

    /**
     * 截取时间戳后八位
     * @return
     */
    public  int  timestamp(){
        long timeStamp = System.currentTimeMillis();
        String timeStampStr = String.valueOf(timeStamp);
        String timeStampMsgId = timeStampStr.substring(timeStampStr.length() - 8, timeStampStr.length());
        int msgId = Integer.parseInt(timeStampMsgId);
        return msgId;
    }


    public String sendWebSocketJson (Context context, int msgId, String token, Map<String, Object> bodyParamsMap){

        String socketRequestStr = webSocketRequest(context, msgId, token, bodyParamsMap);

        String md5 = MD5Util.getMD5_16(socketRequestStr.length()+"");//md5加密字符串

        String socketRequestStrMd5 = socketRequestStr + md5;

        if(socketRequestStrMd5.contains("#")){
            socketRequestStrMd5Json = "$$" + socketRequestStrMd5;
        }else {
            socketRequestStrMd5Json = "#" + socketRequestStrMd5;
        }
        return socketRequestStrMd5Json;
    }
}