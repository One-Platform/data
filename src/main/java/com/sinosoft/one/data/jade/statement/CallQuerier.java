package com.sinosoft.one.data.jade.statement;

import com.sinosoft.one.data.jade.annotation.SQLType;
import com.sinosoft.one.data.jade.dataaccess.DataAccess;
import com.sinosoft.one.data.jade.dataaccess.DataAccessImpl;
import com.sinosoft.one.data.jade.dataaccess.procedure.OutProcedureResult;
import com.sinosoft.one.data.jade.dataaccess.procedure.ProcedureResult;
import com.sinosoft.one.data.jade.dataaccess.procedure.ResultSetProcedureResult;
import com.sinosoft.one.data.jade.rowmapper.RowMapperFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

/**
 * User: chunliang.han
 * Date: 12-10-11
 * Time: 下午5:36
 * 此Querier类用于处理存储过程的调用
 */
public class CallQuerier implements Querier {

    private final RowMapperFactory rowMapperFactory;
    private final EntityManager em;
    private Log log = LogFactory.getLog(SelectQuerier.class);

    public CallQuerier(EntityManager em,RowMapperFactory rowMapperFactory) {
        this.em = em;
        this.rowMapperFactory = rowMapperFactory;
    }

    public Object execute(SQLType sqlType, StatementRuntime... runtimes) {
        StatementRuntime runtime = runtimes[0];
        String sql = runtime.getSQL();
        Object[] args = runtime.getArgs();
        Map<String,Object> paramsMap = runtime.getParameters();
        int count = (Integer)paramsMap.get("*rscount*");
        ResultSetProcedureResult[] rsprs = new ResultSetProcedureResult[count];
        for(int i=0;i<count;i++){
            rsprs[i] = (ResultSetProcedureResult) paramsMap.get("*rs"+(i+1)+"*");
        }
        DataAccess dataAccess = new DataAccessImpl(em);
        dataAccess.call(sql, args, rowMapperFactory, rsprs);
        return null;
    }
}
