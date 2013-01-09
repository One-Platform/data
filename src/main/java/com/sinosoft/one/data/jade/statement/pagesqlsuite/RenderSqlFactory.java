package com.sinosoft.one.data.jade.statement.pagesqlsuite;

/**
 * Created with IntelliJ IDEA.
 * User: kylin
 * Date: 12-8-16
 * Time: 下午2:39
 * To change this template use File | Settings | File Templates.
 */
public class RenderSqlFactory {

    private static final String ORACLE=":oracle:";
    private static final String MYSQL=":mysql:";
    private static final String SQL_SERVER=":sqlserver:";

    public static SuiteDataSourceSql createPageSql(String URL) {
        if(URL.contains(ORACLE)){
            return new SuiteOracle();
        }   else if(URL.contains(MYSQL)) {
            return new SuiteMySql();
        }   else if (URL.contains(SQL_SERVER)) {
            return new SuiteSqlServer();
        }
        return null;
    }

}
