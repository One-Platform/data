/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午02:18:18
 */
package com.sinosoft.one.data.jade.routing;

import java.sql.SQLException;

public interface Router {
    /**
     * 获取散列的列名。
     * 
     * @return 散列的列名
     */
    String getColumn();

    /**
     * 根据列名的值，计算重定向的散列名。 如果不需要重定向, 可以返回 <code>null</code>.
     * 
     * @param columnValue - 列名的值
     * @return 重定向的散列名
     * @throws SQLException
     */
    String doRoute(Object columnValue);
}
