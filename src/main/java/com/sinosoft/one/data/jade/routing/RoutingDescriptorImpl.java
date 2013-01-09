/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午05:22:22
 */
package com.sinosoft.one.data.jade.routing;

public class RoutingDescriptorImpl implements RoutingDescriptor {
    protected String dbName;

    protected Router dbRouter, tableRouter;


    public String getDbName() {
        return dbName;
    }


    public Router getDbRouter() {
        return dbRouter;
    }


    public Router getTableRouter() {
        return tableRouter;
    }

    /**
     * 设置散表的路由。
     * 
     * @param dbRouter - 散表的路由
     */
    public RoutingDescriptor setPartitionRouter(Router partitionRouter) {
        this.tableRouter = partitionRouter;
        return this;
    }
}
