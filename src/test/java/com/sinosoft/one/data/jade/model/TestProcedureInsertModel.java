package com.sinosoft.one.data.jade.model;


import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-12-6
 * Time: 下午2:33
 * To change this template use File | Settings | File Templates.
 */
public class TestProcedureInsertModel {
    private String id;
    private String name;
    private Object createTime;

    public TestProcedureInsertModel() {

    }

    public TestProcedureInsertModel(String id, String name, Object createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Object createTime) {
        this.createTime = createTime;
    }
}
