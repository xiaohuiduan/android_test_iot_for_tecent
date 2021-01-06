package cc.weno.data_template;


import com.tencent.iot.explorer.device.android.common.Status;
import com.tencent.iot.explorer.device.android.data_template.TXDataTemplateClient;
import com.tencent.iot.explorer.device.android.data_template.TXDataTemplateDownStreamCallBack;
import com.tencent.iot.explorer.device.android.mqtt.TXMqttActionCallBack;
import com.tencent.iot.explorer.device.android.mqtt.TXMqttRequest;
import com.tencent.iot.explorer.device.android.utils.AsymcSslUtils;

import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicInteger;

import cc.weno.location.MainActivity;


/**
 * 连接云平台的类
 *
 * @author XiaoHui
 */
public class IotCloudUtil {

    /**
     * 服务器网址
     */
    public static String mBrokerURL = "ssl://iotcloud-mqtt.gz.tencentdevices.com:8883";
    /**
     * 产品ID
     */
    public static String mProductID = "9JXQQW7SR5";
    /**
     * 设备名称
     */
    public static String mDevName = "test_device";
    /**
     * 设备密钥
     */
    public static String mDevPSK = "pCIUP7zhTp7snmfxb/72+g==";
    /**
     * data.json的名字
     */
    public static String mJsonFileName = "data.json";
    /**
     * MQTTAction的回调
     */
    private TXMqttActionCallBack mMqttActionCallBack = null;
    /**
     * 下行消息的回调
     */
    private TXDataTemplateDownStreamCallBack mDownStreamCallBack = null;

    /**
     * MQTT连接实例
     */
    private TXDataTemplateClient mMqttConnection;
    /**
     * Activity实例
     */
    private MainActivity context;

    /**
     * 请求ID
     */
    private static AtomicInteger requestID = new AtomicInteger(199);


    public IotCloudUtil(MainActivity context) {
        this.context = context;
        mDownStreamCallBack = new MyDownCallback();
        mMqttActionCallBack = new MyMQttCallBack();
    }

    /**
     * 建立MQTT连接
     */
    public void connect() {
        // 创建连接client
        mMqttConnection = new TXDataTemplateClient(context, mBrokerURL, mProductID, mDevName, mDevPSK,
                null, null, mMqttActionCallBack,
                mJsonFileName, mDownStreamCallBack);
        // 设置连接参数
        MqttConnectOptions options = new MqttConnectOptions();
        // 连接超时
        options.setConnectionTimeout(8);
        // 保持活跃的时间间隔
        options.setKeepAliveInterval(240);
        // 是否自动重连
        options.setAutomaticReconnect(true);
        // 因为我们是使用密钥登录，所以需要设置这个
        options.setSocketFactory(AsymcSslUtils.getSocketFactory());
        // 建立Request请求
        TXMqttRequest mqttRequest = new TXMqttRequest("connect", requestID.getAndIncrement());
        // 建立连接
        mMqttConnection.connect(options, mqttRequest);
    }

    /**
     * 断开MQTT连接
     */
    public void disconnect() {
        TXMqttRequest mqttRequest = new TXMqttRequest("disconnect", requestID.getAndIncrement());
        mMqttConnection.disConnect(mqttRequest);
    }

    /**
     * 发送消息
     *
     * @param property 消息内容
     * @param metadata 属性的metadata，目前只包含各个属性对应的时间戳，可以为NULL
     * @return 状态
     */
    public Status propertyReport(JSONObject property, JSONObject metadata) {
        return mMqttConnection.propertyReport(property, metadata);
    }


    /**
     * MQTT的回调函数，暂时不考虑
     */
    public static class MyMQttCallBack extends TXMqttActionCallBack {

        @Override
        public void onConnectCompleted(Status status, boolean reconnect, Object userContext, String msg) {

        }

        @Override
        public void onConnectionLost(Throwable cause) {

        }

        @Override
        public void onDisconnectCompleted(Status status, Object userContext, String msg) {
        }

        @Override
        public void onPublishCompleted(Status status, IMqttToken token, Object userContext, String errMsg) {
        }

        @Override
        public void onSubscribeCompleted(Status status, IMqttToken asyncActionToken, Object userContext, String errMsg) {

        }

        @Override
        public void onMessageReceived(final String topic, final MqttMessage message) {
        }
    }

    /**
     * 实现下行消息处理的回调接口，暂时不考虑
     */
    private static class MyDownCallback extends TXDataTemplateDownStreamCallBack {

        @Override
        public void onReplyCallBack(String msg) {
        }

        @Override
        public void onGetStatusReplyCallBack(JSONObject data) {

        }

        @Override
        public JSONObject onControlCallBack(JSONObject msg) {

            return null;
        }

        @Override
        public JSONObject onActionCallBack(String actionId, JSONObject params) {

            return null;
        }
    }

}
