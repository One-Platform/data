package com.sinosoft.one.data.jade.model;

/**
 * User: Chunliang.Han
 * Time: 12-9-6[上午10:45]
 */
public class SomePropertis {
    private String userId;
    private String userName;
    private String groupName;
    private String genderName;

    public String getGenderName() {
        return genderName;
    }
    public void setGenderName(String genderName) {
        this.genderName = genderName;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getGroupName() {
        return groupName;
    }
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    @Override
    public String toString() {
        return "{userId:"+userId+",userName:"+userName+",groupName:"+groupName+",genderName:"+genderName+"}";
    }
}
