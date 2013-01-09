/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午04:10:12
 */
package com.sinosoft.one.data.jade.routing.router;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.BadSqlGrammarException;

import com.sinosoft.one.data.jade.routing.Router;

public class RangeRouter implements Router {

    // 输出日志
    protected static final Log logger = LogFactory.getLog(RangeRouter.class);

    // 匹配日期
    private static final Pattern PATTERN = Pattern.compile("\\{([^\\{\\}]+)\\}");

    protected String column, pattern;

    /**
     * 创建散表配置记录。
     * 
     * @param column - 依赖的列
     * @param pattern - 散列的名称模板
     */
    public RangeRouter(String column, String pattern) {
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

        if (pattern != null && columnValue != null) {

            Date timeValue = convert(columnValue);

            String name = format(pattern, timeValue);

            // 输出日志
            if (logger.isDebugEnabled()) {
                logger.debug("Routing on [" + column + " = "
                        + new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(timeValue) + ", "
                        + columnValue.getClass() + "]: " + name);
            }

            return name;
        }

        return null;
    }

    private Date convert(Object columnValue) {

        if (columnValue instanceof Date) {

            // 处理日期: java.util.Date, java.sql.Date, java.sql.Timestamp 的值
            return (Date) columnValue;

        } else {

            try {
                // 转换成字符串处理
                return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").parse(String
                        .valueOf(columnValue));

            } catch (ParseException e) {

                // 输出日志
                if (logger.isWarnEnabled()) {
                    logger.warn("Column \'" + column // NL
                            + "\' must be date/time, but: " + columnValue);
                }

                throw new BadSqlGrammarException("RangeRouter.convert", "Column \'" + column
                        + "\' must be date/time, but: " + columnValue, null);
            }
        }
    }

    private static String format(String pattern, Date timeValue) {

        // 输出日期
        Matcher matcher = PATTERN.matcher(pattern);
        if (matcher.find()) {

            StringBuilder builder = new StringBuilder(pattern.length());

            int index = 0;

            do {
                // 提取参数名称
                final SimpleDateFormat format = new SimpleDateFormat(matcher.group(1).trim());

                // 拼装参数值
                builder.append(pattern.substring(index, matcher.start()));
                builder.append(format.format(timeValue));

                index = matcher.end();

            } while (matcher.find());

            // 拼装最后一段
            builder.append(pattern.substring(index));

            return builder.toString();
        }

        return pattern;
    }

    public static void main(String... args) {

        System.out.println(format("log_{yyyy}", new Date())); // 按年
        System.out.println(format("log_{yyyy_MM}", new Date())); // 按月
        System.out.println(format("log_{yyyy_ww}", new Date())); // 按周
        System.out.println(format("log_{yyyy_MM_dd}", new Date())); // 按天
    }

}
