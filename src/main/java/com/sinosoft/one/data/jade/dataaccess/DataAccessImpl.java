/*
 * Copyright 2009-2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinosoft.one.data.jade.dataaccess;

import com.sinosoft.one.data.jade.dataaccess.procedure.OutProcedureResult;
import com.sinosoft.one.data.jade.dataaccess.procedure.ResultSetProcedureResult;
import com.sinosoft.one.data.jade.rowmapper.RowMapperFactory;
import com.sinosoft.one.data.jade.statement.pagesqlsuite.RenderSqlFactory;
import com.sinosoft.one.data.jade.statement.pagesqlsuite.SuiteDataSourceSql;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import java.math.BigDecimal;
import java.sql.*;
import java.util.*;

/**
 *
 * @author 王志亮 [qieqie.wang@gmail.com]
 * @author 廖涵 [in355hz@gmail.com]
 */
@Transactional(readOnly = true)
public class DataAccessImpl implements DataAccess, Repository {

    private Log log = LogFactory.getLog(DataAccessImpl.class);
    private final EntityManager em;
    public DataAccessImpl(EntityManager em) {
        this.em = em;
    }

    /**
     * 普通查询 2012-08-16
     */
    public<T> List<T> select(String sql,Object[] args,RowMapper<?> rowMapper) {
        log.info(sql);
        Session session = em.unwrap(Session.class);
        SelectWork<T> work = new SelectWork<T>(sql,args,rowMapper);
        session.doWork(work);
        return work.results;
    }

    /**
     * 分页查询 2012-08-16
     */
    public<T> Page<T> selectByPage(Pageable pageable,String sql,String countSql, Object[] args, RowMapper<?> rowMapper) {
        Session session = em.unwrap(Session.class);
        SingleColumnRowMapper<BigDecimal> scrm = new SingleColumnRowMapper<BigDecimal>();
        List<BigDecimal> totals = select(countSql,args,scrm);
        RenderSqlWork psw = new RenderSqlWork(sql,pageable,null) ;
        session.doWork(psw);
        sql = psw.getSql();
        List<T> content = select(sql,args,rowMapper);
        if(content == null){
            content = new ArrayList<T>();
        }
        Object o = totals.get(0);
        Number num = (Number)o;
        Page<T> page = new PageImpl<T>(content, pageable, num.longValue());
        return page;
    }

    /**
     * 排序查询
     */
    public <T> List<?> selectBySort(Sort sort, String sql, Object[] args, RowMapper<?> rowMapper) {
        Session session = em.unwrap(Session.class);
        RenderSqlWork psw = new RenderSqlWork(sql,null,sort) ;
        session.doWork(psw);
        sql = psw.getSql();
        List<T> content = select(sql,args,rowMapper);
        if(content == null){
            content = new ArrayList<T>();
        }
        return content;
    }

    /**
     * 更新 2012-08-16
     */
    @Transactional
    public int update(String sql, Object[] args, KeyHolder generatedKeyHolder) {
        log.info(sql);
        Session session = em.unwrap(Session.class);
        UpdateWork work = new UpdateWork(sql, args, generatedKeyHolder);
        session.doWork(work);
        return work.number;
    }

    // TODO: 批量处理
    public int[] batchUpdate(String sql, List<Object[]> argsList) {
        int[] updated = new int[argsList.size()];
        int i = 0;
        for (Object[] args : argsList) {
            updated[i++] = update(sql, args, null);
        }
        return updated;
    }

    /**
     * 存储过程 2012-10-12
     */
    public void call(String sql, Object[] args, RowMapperFactory rowMapperFactory, ResultSetProcedureResult[] rsprs) {
        Session session = em.unwrap(Session.class);
        CallWork work = new CallWork(sql, args, rsprs,rowMapperFactory);
        session.doWork(work);
    }

    private void setParams(PreparedStatement ps,Object[] args) throws SQLException{
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                Object arg = args[i];
                if (arg instanceof SqlParameterValue) {
                    SqlParameterValue paramValue = (SqlParameterValue) arg;
                    StatementCreatorUtils.setParameterValue(ps, i + 1, paramValue,
                            paramValue.getValue());
                } else {
                    StatementCreatorUtils.setParameterValue(ps, i + 1,
                            SqlTypeValue.TYPE_UNKNOWN, arg);
                }
            }
        }
    }

    /**
     * Connection查询操作的类
     */
    private class SelectWork<T> implements Work {
        public String sql;
        public Object[] args;
        public RowMapper<?> rowMapper;
        List<T> results = new ArrayList<T>();
        public SelectWork(String sql,Object[] args,RowMapper<?> rowMapper){
            this.sql = sql;
            this.args = args;
            this.rowMapper = rowMapper;
        }
        @SuppressWarnings("unchecked")
        public void execute(Connection con) throws SQLException {
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = null;
            setParams(ps,args);
            int rowNum = 0;
            rs = ps.executeQuery();
            while (rs.next()) {
                results.add((T) rowMapper.mapRow(rs, rowNum++)) ;
            }
        }
    }

    /**
     * Connection调用存储过程的类
     */
    private class CallWork implements Work {
        public String sql;
        public Object[] args;
        public RowMapperFactory rowMapperFactory;
        public ResultSetProcedureResult[] rsprs;

        public CallWork(String sql,Object[] args,ResultSetProcedureResult[] rsprs,RowMapperFactory rowMapperFactory){
            this.sql = sql;
            this.args = args;
            this.rowMapperFactory = rowMapperFactory;
            this.rsprs = rsprs;
        }
        @SuppressWarnings("unchecked")
        public void execute(Connection con) throws SQLException {
            CallableStatement callableStatement= con.prepareCall(sql);
            List<OutProcedureResult> oprts = new ArrayList<OutProcedureResult>();
            for(int i=0,len = args.length;i<len;i++){
                if(args[i] instanceof OutProcedureResult){
                    OutProcedureResult oprt = (OutProcedureResult) args[i];
                    oprt.setIndex(i+1);
                    callableStatement.registerOutParameter(i+1,oprt.getJdbcType());
                    oprts.add(oprt);
                } else {
                    Object arg = args[i];
                    if(arg instanceof java.util.Date) {
                        java.util.Date tempDate = (java.util.Date)arg;
                        java.sql.Timestamp timestamp = new Timestamp(tempDate.getTime());
                        callableStatement.setObject(i+1, timestamp);
                    } else {
                        callableStatement.setObject(i+1,args[i]);
                    }
                }
            }
            callableStatement.execute();
            int i = 0;
            do {
                if(i< rsprs.length){
                    ResultSetProcedureResult rsprt = rsprs[i++];
                    ResultSet rs = callableStatement.getResultSet();
                    List results = new ArrayList();
                    RowMapper rowMapper = rowMapperFactory.getRowMapper(rsprt.getContenType());
                    int rowNum = 0;
                    while (rs.next()) {
                        results.add(rowMapper.mapRow(rs, rowNum++)) ;
                    }
                    rsprt.setResult(results);
                }
            } while(callableStatement.getMoreResults());

            for(i=0;i<oprts.size();i++){
                OutProcedureResult oprt = oprts.get(i);
                List results = new ArrayList();
                Object object = callableStatement.getObject(oprt.getIndex());
                if(object instanceof ResultSet){
                    ResultSet rs = (ResultSet) object;
                    RowMapper rowMapper = rowMapperFactory.getRowMapper(oprt.getContenType());
                    int rowNum = 0;
                    while (rs.next()) {
                        results.add(rowMapper.mapRow(rs, rowNum++)) ;
                    }
                    oprt.setResult(results);
                }else{
                    results.add(object);
                    oprt.setResult(results);
                }
            }
        }
    }

    /**
     * Connection更新操作的类
     */
    private class UpdateWork implements Work {
        public String sql;
        public Object[] args;
        public KeyHolder generatedKeyHolder;
        int number;
        public UpdateWork(String sql,Object[] args,KeyHolder generatedKeyHolder){
            this.sql = sql;
            this.args = args;
            this.generatedKeyHolder = generatedKeyHolder;
        }
        public void execute(Connection con) throws SQLException {
            boolean returnKeys = generatedKeyHolder != null;
            PreparedStatement ps = con.prepareStatement(sql);
            if (returnKeys) {
                ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = con.prepareStatement(sql);
            }
            setParams(ps,args);
            number = ps.executeUpdate();
        }
    }
    /**
     * Connection分页排序的类
     */
    private class RenderSqlWork implements Work {
        private String sql;
        private Pageable pageable;
        private Sort sort;

        public String getSql(){
            return sql;
        }
        RenderSqlWork(String sql, Pageable pageable, Sort sort){
            this.sql = sql;
            this.pageable = pageable;
            this.sort = sort;
        }
        public void execute(Connection con) throws SQLException {
            String URl = con.getMetaData().getURL().toLowerCase();
            SuiteDataSourceSql dps = RenderSqlFactory.createPageSql(URl);
            if(sort == null){
                sql = dps.suiteSql(sql,pageable,null);
            }else{
                sql = dps.suiteSql(sql,null,sort);
            }
        }
    }
}
