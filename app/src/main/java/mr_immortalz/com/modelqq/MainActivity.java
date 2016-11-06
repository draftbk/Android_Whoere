package mr_immortalz.com.modelqq;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.LinkedList;

import master.flame.danmaku.danmaku.model.Danmaku;
import mr_immortalz.com.modelqq.been.Info;
import mr_immortalz.com.modelqq.custom.CustomViewPager;
import mr_immortalz.com.modelqq.custom.RadarViewGroup;
import mr_immortalz.com.modelqq.slideWord.BarrageRelativeLayout;
import mr_immortalz.com.modelqq.utils.FixedSpeedScroller;
import mr_immortalz.com.modelqq.utils.LogUtil;
import mr_immortalz.com.modelqq.utils.ZoomOutPageTransformer;

public class MainActivity extends Activity implements ViewPager.OnPageChangeListener, RadarViewGroup.IRadarClickListener {

    private CustomViewPager viewPager;
    private RelativeLayout ryContainer;
    private RadarViewGroup radarViewGroup;
    private LocationManager manager;
    private double lat;
    private double lon;
    private Location location;
    private int[] mImgs = {R.drawable.len, R.drawable.leo, R.drawable.lep,
            R.drawable.leq, R.drawable.ler, R.drawable.les, R.drawable.mln, R.drawable.mmz, R.drawable.mna,
            R.drawable.mnj, R.drawable.leo, R.drawable.leq, R.drawable.les, R.drawable.lep};
    private String[] mNames = {"ImmortalZ", "唐马儒", "王尼玛", "张全蛋", "蛋花", "王大锤", "叫兽", "哆啦A梦"};
    private int mPosition;
    private FixedSpeedScroller scroller;
    private SparseArray<Info> mDatas = new SparseArray<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getGps();
        initView();
        initData();
        initSlide();

        /**
         * 将Viewpager所在容器的事件分发交给ViewPager
         */
        ryContainer.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return viewPager.dispatchTouchEvent(event);
            }
        });
        ViewpagerAdapter mAdapter = new ViewpagerAdapter();
        viewPager.setAdapter(mAdapter);
        //设置缓存数为展示的数目
        viewPager.setOffscreenPageLimit(mImgs.length);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        //设置切换动画
        viewPager.setPageTransformer(true, new ZoomOutPageTransformer());
        viewPager.addOnPageChangeListener(this);
        setViewPagerSpeed(250);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                radarViewGroup.setDatas(mDatas);
            }
        }, 1500);
        radarViewGroup.setiRadarClickListener(this);
    }

    private void initSlide() {
        BarrageRelativeLayout mBarrageRelativeLayout = (BarrageRelativeLayout) findViewById(R.id.barrageView);

        String[] itemText = {"zhangphil@csdn 0", "zhangphil 1", "zhang phil 2","zhang 3","phil 4","zhangphil ... 5", "***zhangphil 6", "zhang phil csdn 7", "zhang ... phil 8", "phil... 9", "http://blog.csdn.net/zhangphil 10"};
        LinkedList<String> texts=new LinkedList<String>();
        for(int i=0;i<itemText.length;i++){
            texts.add(itemText[i]);
        }
        mBarrageRelativeLayout.setBarrageTexts(texts);

        mBarrageRelativeLayout.show(BarrageRelativeLayout.RANDOM_SHOW);
    }


    private void initData() {

        for (int i = 0; i < mImgs.length; i++) {
            Info info = new Info();
            info.setPortraitId(mImgs[i]);
            info.setAge(((int) Math.random() * 25 + 16) + "岁");
            info.setName(mNames[(int) (Math.random() * mNames.length)]);
            info.setSex(i % 3 == 0 ? false : true);
            info.setDistance(Math.round((Math.random() * 10) * 100) / 100);
            mDatas.put(i, info);
        }
    }

    private void initView() {
        viewPager = (CustomViewPager) findViewById(R.id.vp);
        radarViewGroup = (RadarViewGroup) findViewById(R.id.radar);
        ryContainer = (RelativeLayout) findViewById(R.id.ry_container);
    }

    private void getGps() {
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //检查权限
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        //如果要用GPS就把下面的NETWORK_PROVIDER改成GPS_PROVIDER,但是GPS不稳定

        location = manager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 1, locationLinstener);
    }
    LocationListener locationLinstener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            showLocation(location);
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
    private void showLocation(Location location) {
        lat=location.getLatitude();
        lon=location.getLongitude();
    }
    /**
     * 设置ViewPager切换速度
     *
     * @param duration
     */
    private void setViewPagerSpeed(int duration) {
        try {
            Field field = ViewPager.class.getDeclaredField("mScroller");
            field.setAccessible(true);
            scroller = new FixedSpeedScroller(MainActivity.this, new AccelerateInterpolator());
            field.set(viewPager, scroller);
            scroller.setmDuration(duration);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mPosition = position;
    }

    @Override
    public void onPageSelected(int position) {
        radarViewGroup.setCurrentShowItem(position);
        LogUtil.m("当前位置 " + mPosition);
        LogUtil.m("速度 " + viewPager.getSpeed());
        //当手指左滑速度大于2000时viewpager右滑（注意是item+2）
        if (viewPager.getSpeed() < -1800) {

            viewPager.setCurrentItem(mPosition + 2);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        } else if (viewPager.getSpeed() > 1800 && mPosition > 0) {
            //当手指右滑速度大于2000时viewpager左滑（注意item-1即可）
            viewPager.setCurrentItem(mPosition - 1);
            LogUtil.m("位置 " + mPosition);
            viewPager.setSpeed(0);
        }
        Danmaku danmaku=new Danmaku("nihai");
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onRadarItemClick(int position) {
        viewPager.setCurrentItem(position);
    }


    class ViewpagerAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            final Info info = mDatas.get(position);
            //设置一大堆演示用的数据，麻里麻烦~~
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.viewpager_layout, null);
            ImageView ivPortrait = (ImageView) view.findViewById(R.id.iv);
            ImageView ivSex = (ImageView) view.findViewById(R.id.iv_sex);
            TextView tvName = (TextView) view.findViewById(R.id.tv_name);
            TextView tvDistance = (TextView) view.findViewById(R.id.tv_distance);
            tvName.setText(info.getName());
            tvDistance.setText(info.getDistance() + "km");
            ivPortrait.setImageResource(info.getPortraitId());
            if (info.getSex()) {
                ivSex.setImageResource(R.drawable.girl);
            } else {
                ivSex.setImageResource(R.drawable.boy);
            }
            ivPortrait.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "这是 " + info.getName() + " >.<", Toast.LENGTH_SHORT).show();
                }
            });
            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return mImgs.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

    }

}
