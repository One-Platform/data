package com.sinosoft.one.data.jade.statement.pagesqlsuite;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: kylin
 * Date: 12-8-16
 * Time: 下午2:44
 * To change this template use File | Settings | File Templates.
 */
public class SuiteSqlServer extends AbstractSuiteDataSourceSql implements SuiteDataSourceSql {

    protected List<String> orderProperties = new ArrayList<String>();
    private Log log = LogFactory.getLog(AbstractSuiteDataSourceSql.class);

    public String suiteSql(String sql,Pageable pageable,Sort sort) {
        return super.suiteSql(sql, pageable, sort);
    }

    //@Override
    String renderForPage(String sql, Pageable pageable) {
          StringBuilder newSql = new StringBuilder();
        int pointAtSelect = getPoint(sql,"select ","select\t","select\n","select\r") ;
        String sub1 = sql.substring(0,pointAtSelect+6);
        String sub2 = sql.substring(pointAtSelect+6);
        int pointAtFrom =  getPoint(sub2," from "," from\t","\tfrom ","\nfrom "," from\n");
        String sub3 = sub2.substring(0,pointAtFrom);
        Map<String,String> colNamesMap = getRealColNames(sub3,pageable.getSort());

        newSql.append("with t_rowtable ")
                .append("as(")
                .append(sub1)
                .append(" row_number() over(");
        renderForOrders(pageable.getSort(),colNamesMap, newSql);
        newSql.append(") as row_number,")
                .append(sub2)
                .append(") ")
                .append("select * from t_rowtable where row_number<=")
                .append(pageable.getPageSize()*(pageable.getPageNumber()+1))
                .append(" and row_number >")
                .append(pageable.getPageSize() * pageable.getPageNumber());
        renderForOrders(pageable, newSql);
        return newSql.toString();
    }

    //@Override
    String renderForSort(String sql, Sort sort) {
        StringBuilder newSql = new StringBuilder(sql);
        renderForOrders(sort,newSql) ;
        return newSql.toString();
    }
    private int getPoint(String target,String... strings){
        int pointAt = -1;
        for(String str : strings){
            pointAt = target.indexOf(str);
            if(pointAt!=-1) {
                break;
            }
        }
        return pointAt;
    }
    private Map<String,String> getRealColNames(String str,Sort sort){
        Map<String,String> realColNames = new HashMap<String, String>();
        String[] strings = str.split(",");
        Iterator iterator = sort.iterator();
        while(iterator.hasNext()){
            Sort.Order order = (Sort.Order)iterator.next();
            orderProperties.add(order.getProperty());
        }
        for(String orderProperty:orderProperties){
            realColNames.put(orderProperty,orderProperty) ;
            for(String colNameStr : strings){
                colNameStr = colNameStr.trim();
                if(colNameStr.endsWith(" "+orderProperty.trim())){
                     String[] colNames = colNameStr.split(" ");
                     realColNames.put(orderProperty,colNames[0].trim());
                }
            }
        }
        return realColNames;
    }
    private StringBuilder renderForOrders(Sort sort,Map<String,String> map,StringBuilder sb){
        if(StringUtils.contains(sb, "order\\s*by")){
            log.warn("\"order by\" has already exist in the sql statement,you shouldn't use params such as \"Pageable\" or \"Sort\"");
            return sb;
        } else if(sort!=null){
            Iterator iterator = sort.iterator();
            String sep = new String() ;
            sb.append(ORDER);
            while(iterator.hasNext()){
                sb.append(sep);
                Sort.Order order = (Sort.Order)iterator.next();
                sb.append(map.get(order.getProperty())).append(SPACE).append(order.getDirection());
                sep = COMMA;
            }
        }
        return sb;
    }
}
