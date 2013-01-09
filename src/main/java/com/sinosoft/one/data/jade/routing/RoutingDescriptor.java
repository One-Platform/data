/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午02:17:21
 */
package com.sinosoft.one.data.jade.routing;

public interface RoutingDescriptor {
    /**
     * 返回配置的数据源名称: dbName.
     * 
     * @return 配置的数据源名称: dbName
     */
    String getDbName();

    /**
     * 获取一个散库的路由, 用于计算散库后的数据库: Pattern 名称。
     * 
     * @return 散库的路由, 返回 <code>null</code> 表示不需要散库
     */
    Router getDbRouter();

    /**
     * 获取一个散表的路由, 用于计算散表后的数据表名称。
     * 
     * @return 散表的路由, 返回 <code>null</code> 表示不需要散表
     */
    Router getTableRouter();
}
