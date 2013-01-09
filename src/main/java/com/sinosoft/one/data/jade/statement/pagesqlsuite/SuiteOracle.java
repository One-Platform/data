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
public class SuiteOracle extends AbstractSuiteDataSourceSql implements SuiteDataSourceSql {

    public String suiteSql(String sql,Pageable pageable,Sort sort) {
        return super.suiteSql(sql,pageable, sort);
    }
    //@Override
    String renderForPage(String sql,Pageable pageable){
        StringBuilder newSql = new StringBuilder();
        newSql.append("select * from (select a.*, rownum rn from(")
                .append(sql);
        renderForOrders(pageable,newSql).append(SPACE);
        newSql.append(") a where rownum <=")
                .append(pageable.getPageSize()*(pageable.getPageNumber()+1))
                .append(") where rn >")
                .append(pageable.getPageSize() * pageable.getPageNumber());
        return newSql.toString();
    }
    //@Override
    String renderForSort(String sql,Sort sort){
        StringBuilder newSql = new StringBuilder(sql);
        renderForOrders(sort,newSql) ;
        return newSql.toString();
    }
}
