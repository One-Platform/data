package com.sinosoft.one.data.jade.dataaccess.procedure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chunliang.han
 * Date: 12-10-15
 * Time: 下午6:17
 * 存放ResultSet的存储过程结果集.
 */
public class ResultSetProcedureResult<T extends List<?>> implements ProcedureResult<T> {
    private final Class CONTENT_TYPE;
    private T result;

    public ResultSetProcedureResult(Class contentType){
        this.CONTENT_TYPE = contentType;
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
}
