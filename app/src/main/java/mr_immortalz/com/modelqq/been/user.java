package mr_immortalz.com.modelqq.been;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobObject;

/**
 * Created by slf on 2016/11/11.
 */

public class user extends BmobObject {
    private String user_id;
    private String name;
    private String lon;
    private String lat;

    public user() {
    }

    public user(String tableName) {
        super(tableName);
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }
}
