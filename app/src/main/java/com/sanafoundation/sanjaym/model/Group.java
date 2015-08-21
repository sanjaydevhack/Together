package com.sanafoundation.sanjaym.model;

import android.graphics.Bitmap;

/**
 * Created by IM028 on 7/9/15.
 */
public class Group {

    private String group_name, group_icon;
    private Bitmap pic;

    public Group() {
    }

    public String getGroup_name() {
        return group_name;
    }

    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }

    public String getGroup_icon() {
        return group_icon;
    }

    public void setGroup_icon(String group_icon) {
        this.group_icon = group_icon;
    }

    public Bitmap getPic() {
        return pic;
    }

    public void setPic(Bitmap pic) {
        this.pic = pic;
    }
}
