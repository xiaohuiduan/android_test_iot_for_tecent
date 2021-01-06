package cc.weno.location;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.tencent.iot.explorer.device.android.common.Status;

import org.json.JSONException;
import org.json.JSONObject;

import cc.weno.data_template.IotCloudUtil;


/**
 * 主页面，进行展示以及发送数据
 *
 * @author XiaoHui
 */
public class MainActivity extends AppCompatActivity {
    /**
     * 展示经度
     */
    private TextView xPositionView;
    /**
     * 展示纬度
     */
    private TextView yPositionView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        xPositionView = findViewById(R.id.x_position);
        yPositionView = findViewById(R.id.y_position);
        // 基本上现在的安卓机都需要申请位置权限了
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        // 获得位置数据并且发送数据到云平台
        getAndSendLocation();
    }

    private void getAndSendLocation() {
        // 获得GPS工具类
        GPSUtils gpsUtil = GPSUtils.getInstance(this);
        // 获得位置
        Location location = gpsUtil.getLocation();

        double positionX = location.getLatitude();
        double positionY = location.getLongitude();
        // 在手机页面上展示
        xPositionView.setText(String.valueOf(positionX));
        yPositionView.setText(String.valueOf(positionY));
        // IotCloudUtil
        IotCloudUtil iotCloudUtil = new IotCloudUtil(this);
        // 连接云平台
        iotCloudUtil.connect();

        // 等待几秒钟，连接成功
        try {
            Thread.sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 调用发送数据的函数需要传入JsonObject类型的数据
        JSONObject property = new JSONObject();
        try {
            property.put("position_x", (float) positionX);
            property.put("position_y", (float) positionY);
            // 发送数据
            Status status = iotCloudUtil.propertyReport(property, null);
            if (status == Status.OK){
                // 发送成功
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}