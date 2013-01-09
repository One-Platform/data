/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午04:08:04
 */
package com.sinosoft.one.data.jade.routing.router;

import java.text.MessageFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinosoft.one.data.jade.routing.Router;

public class DirectRouter implements Router {

    // 输出日志
    protected static final Log logger = LogFactory.getLog(DirectRouter.class);

    protected String column, pattern;

    /**
     * 创建散表配置记录。
     * 
     * @param column - 依赖的列
     * @param pattern - 散列的名称模板
     */
    public DirectRouter(String column, String pattern) {
        this.column = column;
        this.pattern = pattern;
    }


    public String getColumn() {
        return column;
    }

    /**
     * 设置配置的列。
     * 
     * @param column - 配置的列
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * 返回数据表的名称模板。
     * 
     * @return 数据表的名称模板
     */
    public String getPattern() {
        return pattern;
    }

    /**
     * 设置数据表的名称模板。
     * 
     * @param pattern - 数据表的名称模板
     */
    public void setPattern(String pattern) {
        this.pattern = pattern;
    }


    public String doRoute(Object columnValue) {

        if (pattern != null) {

            String name = MessageFormat.format(pattern, columnValue);

            // 输出日志
            if (logger.isDebugEnabled()) {
                if (columnValue != null) {
                    logger.debug("Routing on [" + column + " = " + columnValue + ", "
                            + columnValue.getClass() + "]: " + name);
                } else {
                    logger.debug("Routing to: " + name);
                }
            }

            return name;
        }

        return null;
    }
}
