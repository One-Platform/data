package com.sinosoft.one.data.jade.statement.pagesqlsuite;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * Created with IntelliJ IDEA.
 * User: kylin
 * Date: 12-8-16
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class SuiteMySql extends AbstractSuiteDataSourceSql implements SuiteDataSourceSql {

    public String suiteSql(String sql,Pageable pageable,Sort sort) {
        return super.suiteSql(sql, pageable, sort);
    }
    //@Override
    String renderForPage(String sql,Pageable pageable){
        StringBuilder newSql = new StringBuilder(sql);
        renderForOrders(pageable,newSql) ;
        newSql.append(SPACE);
        newSql.append(" limit ")
                .append((pageable.getPageNumber())*pageable.getPageSize())
                .append(",").append(pageable.getPageSize());
        return newSql.toString();
    }
    //@Override
    String renderForSort(String sql,Sort sort){
        StringBuilder newSql = new StringBuilder(sql);
        renderForOrders(sort,newSql) ;
        return newSql.toString();
    }
}
