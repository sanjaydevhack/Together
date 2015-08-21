package com.sanafoundation.sanjaym.model;

/**
 * Created by Sanjay on 8/1/15.
 */
public class GroupId {
   public static GroupId gid = new GroupId();

    String singleGroupId;

    public String getSingleGroupId() {
        return singleGroupId;
    }

    public void setSingleGroupId(String singleGroupId) {
        this.singleGroupId = singleGroupId;
    }
}
