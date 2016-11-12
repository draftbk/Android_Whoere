package mr_immortalz.com.modelqq.been;

import java.util.Date;

import cn.bmob.v3.BmobObject;

/**
 * Created by slf on 2016/11/12.
 */

public class Chat extends BmobObject {
    private String user_id;
    private String content;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
