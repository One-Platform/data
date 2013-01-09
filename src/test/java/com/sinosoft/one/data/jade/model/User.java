package com.sinosoft.one.data.jade.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * Intro: User Entity for Jade Test
 * User: Kylin
 * Date: 12-9-4
 * Time: 下午3:08
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private String id;
    private String name;
    private int age;
    private Date birthday;
    private long money;
    private String gender;
    private String groupIds;

    public String getId() {
        return id;
    }

    public User setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public User setName(String name) {
        this.name = name;
        return this;
    }

    public int getAge() {
        return age;
    }

    public User setAge(int age) {
        this.age = age;
        return this;
    }

    public Date getBirthday() {
        return birthday;
    }

    public User setBirthday(Date birthday) {
        this.birthday = birthday;
        return this;
    }

    public long getMoney() {
        return money;
    }

    public User setMoney(long money) {
        this.money = money;
        return this;
    }

    public String getGender() {
        return gender;
    }

    public User setGender(String gender) {
        this.gender = gender;
        return this;
    }

    public String getGroupIds() {
        return groupIds;
    }

    public User setGroupIds(String gorupIds) {
        this.groupIds = gorupIds;
        return this;
    }
}
