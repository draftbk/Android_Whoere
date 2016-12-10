package mr_immortalz.com.modelqq;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import mr_immortalz.com.modelqq.been.user;
import mr_immortalz.com.modelqq.tools.LocationTools;

public class WelcomeActivity extends AppCompatActivity {
    private LocationManager manager;
    private boolean isFirst=true;
    private user thisUser;
    private Double lat;
    private Double lon;
    private ArrayList<String> otherID=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "1b2551067b01b0765269eb6f4c4efd2c");
        thisUser=new user();
        initGPS();
        getGps();
//        final Intent it = new Intent(WelcomeActivity.this, MainActivity.class); //你要转向的Activity
//        Timer timer = new Timer();
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//                startActivity(it); //执行
//                finish();
//            }
//        };
//        timer.schedule(task, 1000 * 5);
    }
    private void getGps() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //如果要用GPS就把下面的NETWORK_PROVIDER改成GPS_PROVIDER,但是GPS不稳定
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, locationLinstener);
    }
    LocationListener locationLinstener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (isFirst){
                loadLocation(location);
                isFirst=false;
            }
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
    private void loadLocation(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();
        thisUser.setUser_id((lat+"").substring(2,4)+(lon+"").substring(2,4)+(Math.random()*(10000)));
        thisUser.setName("路人甲");
        thisUser.setLat(lat+"");
        thisUser.setLon(lon+"");
        thisUser.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                if(e==null){
                    delete();
                }else{
                    toast("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    private void delete() {
        thisUser.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e==null){
                    Intent it = new Intent(WelcomeActivity.this, MainActivity.class); //你要转向的Activity
                    startActivity(it); //执行
                    finish();
                }else{

                }
            }

        });
    }

    private void toast(String s) {
        Toast.makeText(WelcomeActivity.this,s,Toast.LENGTH_SHORT).show();
    }
    private void initGPS() {
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(WelcomeActivity.this, "请打开GPS",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new android.content.DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {

                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(intent); // 设置完成后返回到原来的界面
                            finish();
                        }
                    });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            } );
            dialog.show();
        } else {
            // 弹出Toast
//          Toast.makeText(TrainDetailsActivity.this, "GPS is ready",
//                  Toast.LENGTH_LONG).show();
//          // 弹出对话框
//          new AlertDialog.Builder(this).setMessage("GPS is ready")
//                  .setPositiveButton("OK", null).show();
        }
    }
}
