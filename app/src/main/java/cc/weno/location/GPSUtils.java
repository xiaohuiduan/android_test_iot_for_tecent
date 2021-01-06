package cc.weno.location;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;


/**
 * 获得GPS的位置信息。
 * @author XiaoHui
 */
public class GPSUtils {
    private static String TAG = GPSUtils.class.getSimpleName();

    private static GPSUtils mInstance;
    private Context mContext;
    private static LocationListener mLocationListener = new LocationListener() {


        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }


        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    private GPSUtils(Context context) {
        this.mContext = context;
    }

    public static GPSUtils getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new GPSUtils(context.getApplicationContext());
        }
        return mInstance;
    }

    /**
     * 获取地理位置，先根据GPS获取，再根据网络获取
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    public Location getLocation() {
        Location location = null;
        try {
            if (mContext == null) {
                return null;
            }
            LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            if (locationManager == null) {
                return null;
            }
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location == null) {
                    location = getLocationByNetwork();
                }
            } else {    //从网络获取经纬度
                location = getLocationByNetwork();
            }

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return location;
    }

    /**
     * 判断是否开启了GPS或网络定位开关
     *
     * @return
     */
    public boolean isLocationProviderEnabled() {
        boolean result = false;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager == null) {
            return result;
        }
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            result = true;
        }
        return result;
    }

    /**
     * 获取地理位置，先根据GPS获取，再根据网络获取
     *
     * @return
     */
    @SuppressLint("MissingPermission")
    private Location getLocationByNetwork() {
        Location location = null;
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, mLocationListener);
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        return location;
    }
}

