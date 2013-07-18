package com.sinosoft.one.data.jade.statement.pagesqlsuite;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;

/**
 * User: Chunliang.Han
 * Time: 12-9-13[下午3:50]
 */
public abstract class AbstractSuiteDataSourceSql {
    private Log log = LogFactory.getLog(AbstractSuiteDataSourceSql.class);
    public static final String COMMA = ",";
    public static final String SPACE = " ";
    public static final String ORDER = " order by ";

    protected String suiteSql(String sql,Pageable pageable,Sort sort) {
//        sql = sql.toLowerCase()
        if(pageable!=null){
            return renderForPage(sql,pageable);
        } else {
            return renderForSort(sql,sort);
        }
    }

    abstract String renderForPage(String sql, Pageable pageable) ;
    abstract String renderForSort(String sql, Sort sort) ;

    protected StringBuilder renderForOrders(Pageable pageable,StringBuilder sb){
        Sort sort =  pageable.getSort();
        renderForOrders(sort,sb);
        return sb;
    }
    protected StringBuilder renderForOrders(Sort sort,StringBuilder sb){
        if(StringUtils.contains(sb,"order\\s*by")){
            log.warn("\"order by\" has already exist in the sql statement,you shouldn't use params such as \"Pageable\" or \"Sort\"");
            return sb;
        } else if(sort!=null){
            Iterator iterator = sort.iterator();
            String sep = new String() ;
            sb.append(ORDER);
            while(iterator.hasNext()){
                sb.append(sep);
                Sort.Order order = (Sort.Order)iterator.next();
                sb.append(order.getProperty()).append(SPACE).append(order.getDirection());
                sep = COMMA;
            }
        }
        return sb;
    }
}
