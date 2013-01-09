package com.sinosoft.one.data.jade.dataaccess.procedure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chunliang.han
 * Date: 12-10-15
 * Time: 下午6:15
 * 调用存储过程Out参数返回值的Java类
 */
public class OutProcedureResult<T extends List<?>> implements ProcedureResult<T> {
    private final Class CONTENT_TYPE;
    private final int JDBC_TYPE;
    private int index;
    private T result;

    public OutProcedureResult(Class contentType,int type){
        this.CONTENT_TYPE = contentType;
        this.JDBC_TYPE = type;
    }
    public Class getContenType(){
        return this.CONTENT_TYPE;
    }
    public void setResult(T result){
        this.result = result;
    }
    public T getResult() {
        return result;
    }
    public int getJdbcType(){
        return JDBC_TYPE;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }
}
