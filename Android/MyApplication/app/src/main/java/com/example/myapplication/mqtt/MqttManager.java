package com.example.myapplication.mqtt;

import android.content.Context;
import android.util.Log;

import com.example.myapplication.event.FireEvent;
import com.example.myapplication.event.HumanEvent;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

/**
 * Mqtt工具
 */
public class MqttManager {

    //MQTT相关配置
    //服务器地址（协议+地址+端口号）
    private final static String HOST = "HOST";   //修改改为你的host
    private final static String CLIENTID = "CLIENTID";  //修改为你的clientId

    private final static String USERNAME = "USERNAME";   //修改为你的用户名
    private final static String PASSWORD = "PASSWORD";   // 修改为你的密码

    //服务质量,0最多一次，1最少一次，2只一次
    private final static int QOS = 0;

    private Context mContext;
    private static MqttManager mqttManager;
    private MqttAndroidClient mqttClient;
    private MqttConnectOptions mqttConnectOptions;

    /**
     * 构造函数
     */
    private MqttManager(Context context) {
        this.mContext = context;
        initMqtt();
    }

    /**
     * 单例模式
     */
    public static MqttManager getInstance(Context context) {
        if (mqttManager == null) {
            mqttManager = new MqttManager(context);
        }
        return mqttManager;
    }

    /**
     * 初始化
     */
    private void initMqtt() {
        //创建Mqtt客户端
        mqttClient = new MqttAndroidClient(mContext, HOST, CLIENTID);
        mqttClient.setCallback(mqttCallback); //设置订阅消息的回调

        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true); //设置是否清除缓存
        mqttConnectOptions.setConnectionTimeout(30); //设置超时时间，单位：秒
        mqttConnectOptions.setKeepAliveInterval(60); //设置心跳包发送间隔，单位：秒
        mqttConnectOptions.setUserName(USERNAME); //设置用户名
        mqttConnectOptions.setPassword(PASSWORD.toCharArray()); //设置密码
    }

    /**
     * MQTT是否连接成功的监听
     */
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken asyncActionToken) {
            Log.i("--->mqtt", "连接成功 ");
            try {
                //订阅主题，参数：主题、服务质量
                subTopic("/gxtyIZJ9otJ/myapp/user/sendDHT111", QOS);
                subTopic("/gxtyIZJ9otJ/myapp/user/sendFire", QOS);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
            exception.printStackTrace();
        }
    };

    /**
     * 订阅主题的回调
     */
    private MqttCallback mqttCallback = new MqttCallback() {
        @Override
        public void connectionLost(Throwable cause) {
            cause.printStackTrace();
        }

        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            if(topic.equals("/gxtyIZJ9otJ/myapp/user/sendDHT111")){
                EventBus.getDefault().post(new HumanEvent());
            }else{
                EventBus.getDefault().post(new FireEvent());
            }
            Log.i("--->mqtt", "收到消息： " +topic+"-----" +new String(message.getPayload()) + "\tToString:" + message.toString());
            //收到其他客户端的消息后，响应给对方告知消息已到达或者消息有问题等
            //response("message arrived:"+message);
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken token) {
            Log.i("--->mqtt", "deliveryComplete");
        }
    };

    /**
     * 建立mqtt连接，连接MQTT服务器
     */
    public boolean connectServer() {
        try {
            if ((mqttClient != null) && (!mqttClient.isConnected())) {
                mqttClient.connect(mqttConnectOptions, null, iMqttActionListener);
                //注册
                EventBus.getDefault().register(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 发布消息
     */

    //  订阅主题
    public void subTopic(String topic, int qos) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(topic, qos);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 发布消息
     */
    public void pubTopic(String topic,String payLoad) {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
                mqttClient.publish(topic,payLoad.getBytes(),QOS,false);
            } else {
                Log.i("--->mqtt","mqttAndroidClient is Null or is not connected");
            }
        } catch (Exception e) {
            Log.i("--->mqtt","publish MqttException:" + e.getMessage());
            e.printStackTrace();
        }
    }


    /**
     * 断开链接
     */
    public void disconnect() {
        try {
            if (mqttClient != null) {
                mqttClient.unregisterResources();
                mqttClient.disconnect();
                mqttClient = null;
                EventBus.getDefault().unregister(mContext);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}