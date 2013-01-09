/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 下午01:48:01
 */

package com.sinosoft.one.data.jade.routing;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.collections.map.LRUMap;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.sinosoft.one.data.jade.parsers.mysql.parser.sql.MysqlParser;
import com.sinosoft.one.data.jade.parsers.parser.ParseException;
import com.sinosoft.one.data.jade.parsers.parser.dbobject.Column;
import com.sinosoft.one.data.jade.parsers.parser.dbobject.Table;
import com.sinosoft.one.data.jade.parsers.parser.expression.BaseExpressionList;
import com.sinosoft.one.data.jade.parsers.parser.expression.ColumnExpression;
import com.sinosoft.one.data.jade.parsers.parser.expression.ComparisonExpression;
import com.sinosoft.one.data.jade.parsers.parser.expression.ConstantExpression;
import com.sinosoft.one.data.jade.parsers.parser.expression.Expression;
import com.sinosoft.one.data.jade.parsers.parser.expression.FunctionExpression;
import com.sinosoft.one.data.jade.parsers.parser.expression.ParameterExpression;
import com.sinosoft.one.data.jade.parsers.parser.function.Function;
import com.sinosoft.one.data.jade.parsers.parser.function.UnknowableFunction;
import com.sinosoft.one.data.jade.parsers.parser.statement.DMLStatement;
import com.sinosoft.one.data.jade.parsers.parser.statement.DeleteStatement;
import com.sinosoft.one.data.jade.parsers.parser.statement.InsertStatement;
import com.sinosoft.one.data.jade.parsers.parser.statement.SelectStatement;
import com.sinosoft.one.data.jade.parsers.parser.statement.Statement;
import com.sinosoft.one.data.jade.parsers.parser.statement.UpdateStatement;

public class SQLParseInfo {

    // 输出日志
    protected static final Log logger = LogFactory.getLog(SQLParseInfo.class);

    // 匹配 LIMIT n, m 语法
    protected static Pattern LIMIT_PATTERN = Pattern.compile("\\s+LIMIT\\s+(\\d+|\\?)(?:,\\s*(\\d+|\\?))?\\s*$", Pattern.CASE_INSENSITIVE);

    // 常用函数列表
    protected static Map<String, Function> functionMap = new HashMap<String, Function>();

    static {
        // TODO: [NEED-FIX] 避免在解析这些函数时出错，并不实际解析函数本身。这意味着会忽略函数参数中的问号,
        // 会造成某些语句的参数顺序解析错误。
        functionMap.put("MOD", new UnknowableFunction());
        functionMap.put("CONCAT", new UnknowableFunction());
        functionMap.put("CONCAT_WS", new UnknowableFunction());
        functionMap.put("NOW", new UnknowableFunction());
        functionMap.put("SYSDATE", new UnknowableFunction());
        functionMap.put("CURRENT_TIMESTAMP", new UnknowableFunction());
        functionMap.put("CURRENT_DATE", new UnknowableFunction());
        functionMap.put("ADDDATE", new UnknowableFunction());
        // 如果添加, 会忽略以 'year' 'month' 'day' 'hour' 'minute' 'second'
        // 'microsecond' 为名称
        // 的列查询条件。
        // functionMap.put("YEAR", new UnknowableFunction());
        // functionMap.put("MONTH", new UnknowableFunction());
        // functionMap.put("WEEK", new UnknowableFunction());
        // functionMap.put("DAY", new UnknowableFunction());
        // functionMap.put("HOUR", new UnknowableFunction());
        // functionMap.put("MINUTE", new UnknowableFunction());
        // functionMap.put("SECOND", new UnknowableFunction());
        // functionMap.put("MICROSECOND", new UnknowableFunction());
        functionMap.put("ASCII", new UnknowableFunction());
        functionMap.put("NEXTVAL", new UnknowableFunction());
        functionMap.put("INSERT", new UnknowableFunction());
        functionMap.put("INSTR", new UnknowableFunction());
        functionMap.put("SUBSTRING", new UnknowableFunction());
        functionMap.put("DATABASE", new UnknowableFunction());
        functionMap.put("LAST_INSERT_ID", new UnknowableFunction());
    }

    // 缓存语句的解析信息
    /* sql->SQLParseInfo */
    protected static LRUMap cacheInfo = new LRUMap(1024); // TODO: [NEED-CODE]

    // 大小需要配置

    public static SQLParseInfo getParseInfo(String sql) {

        SQLParseInfo info;

        // 进入全局锁创建共享信息对象。
        synchronized (cacheInfo) {

            info = (SQLParseInfo) cacheInfo.get(sql);

            if (info == null) {

                // 创建 XceParseInfo 实例是一个不耗时的操作。
                info = new SQLParseInfo(sql);
                cacheInfo.put(sql, info);
            }
        }

        // 解析所给的语句, 也许比较耗时。
        info.parseInfoIfNecessary();

        return info;
    }

    // 当前的 SQL 语句
    protected final String sql;

    // 当前的查询结果
    protected boolean dml;

    protected boolean select, insert, update, delete;

    // 查询的数据表
    protected Table[] tables;

    // 查询的所有命名参数
    protected HashMap<Column, Object> namedParams = new HashMap<Column, Object>();

    // 命名参数的索引
    protected ArrayList<Column> indexParams = new ArrayList<Column>();

    protected int paramOffset = 0, paramCount = 0; // 全部命名参数个数

    protected boolean parsingFailed = false, inited = false;

    /**
     * 默认的构造函数。
     */
    public SQLParseInfo(String sql) {
        this.sql = sql;
    }

    /**
     * 获取解析的原始语句。
     * 
     * @return 原始语句
     */
    public String getSQL() {
        return sql;
    }

    /**
     * 检查解析信息是否已经初始化。
     * 
     * @return 解析信息是否初始化
     */
    public boolean isInited() {
        return inited;
    }

    /**
     * 检查语句解析是否失败。
     * 
     * @return 语句解析是否失败
     */
    public boolean isParsingFailed() {
        return parsingFailed;
    }

    /**
     * 当必要的时候，从指定的语句解析信息。
     */
    protected void parseInfoIfNecessary() {

        // 下面进行耗时操作，单独锁定记录本身。
        if (inited) {
            return;
        }
        synchronized (this) {
            if (inited) {
                return;
            }
            parseInfo();
            inited = true;
        }
    }

    /**
     * 从指定的语句解析信息。
     */
    private void parseInfo() {

        // 输出日志
        if (logger.isDebugEnabled()) {
            logger.debug("First parsing SQL: " + sql);
        }

        String parseSql = sql;

        // 解决 LIMIT n, m 语法的解析问题
        Matcher matcher = LIMIT_PATTERN.matcher(sql);
        if (matcher.find()) {
            parseSql = sql.substring(0, matcher.start());

            // 如果 n 的值是 '?'
            if ("?".equals(matcher.group(1))) {
                paramCount++;
            }
            // 如果 m 的值是 '?'
            if ("?".equals(matcher.group(2))) {
                paramCount++;
            }
        }

        // 使用 Amoeba 自带的解析器分析查询语句。
        MysqlParser parser = new MysqlParser(new StringReader(parseSql));

        // 设置函数列表。
        parser.setFunctionMap(functionMap);

        Statement stmt = null;
        try {
            stmt = parser.doParse();
        } catch (ParseException e) {
            parsingFailed = true;

            // 语句解析失败，写出日志。
            if (logger.isDebugEnabled()) {
                logger.debug("Parsing SQL failed: " + sql, e);

            } else if (logger.isWarnEnabled()) {
                logger.warn("Parsing SQL failed: " + sql);
            }
        }

        dml = (stmt instanceof DMLStatement);

        if (dml) {
            // 解析 SELECT / INSERT / UPDATE / DELETE 语句
            select = (stmt instanceof SelectStatement);
            insert = (stmt instanceof InsertStatement);
            update = (stmt instanceof UpdateStatement);
            delete = (stmt instanceof DeleteStatement);

            DMLStatement dmlStmt = (DMLStatement) stmt;

            tables = dmlStmt.getTables();

            // 处理生成的表达式
            parseExpr(dmlStmt.getExpression(), null);

            // 记录参数数目，注意: UPDATE 语句的实际参数数目要大于表达式中提取的参数数目。因此，真实地参数索引应该是:
            // index = paramIndex + indexParams.size() - paramCount - 1
            paramOffset = indexParams.size() - stmt.getParameterCount() - 1;
            paramCount += stmt.getParameterCount();
        }

    }

    /**
     * 检查是否为: INSERT / UPDATE / DELETE / SELECT 语句。
     * 
     * @return 是否为数据操作语句
     */
    public boolean isDML() {
        return dml;
    }

    /**
     * 检查是否为 : SELECT 语句。
     * 
     * @return 是否为查询语句
     */
    public boolean isSelect() {
        return select;
    }

    /**
     * 检查是否为 : INSERT 语句。
     * 
     * @return 是否为查询语句
     */
    public boolean isInsert() {
        return insert;
    }

    /**
     * 检查是否为 : UPDATE 语句。
     * 
     * @return 是否为查询语句
     */
    public boolean isUpdate() {
        return update;
    }

    /**
     * 检查是否为 : DELETE 语句。
     * 
     * @return 是否为查询语句
     */
    public boolean isDelete() {
        return delete;
    }

    /**
     * 检查是否特殊查询，特殊查询是类似： SELECT @@LAST_INSERT_ID / SELECT LAST_INSERT_ID() /
     * SELECT FOUND_ROWS() 此类的查询，必须与前一个查询连用。
     * 
     * @return 是否特殊查询
     */
    public boolean isUnswitch() {

        // 暂时没有办法做更加复杂的检测。
        return (select && (0 == tables.length));
    }

    /**
     * 返回查询的数据表。
     * 
     * @return 查询的数据表
     */
    public Table[] getTables() {
        return tables;
    }

    /**
     * 返回参数数目。
     * 
     * @return 参数数目
     */
    public int getParamCount() {
        return paramCount;
    }

    /**
     * 返回指定索引位置的列。
     * 
     * @param paramIndex - 参数索引
     * @return 索引位置的列
     */
    public Column getColumn(int paramIndex) {

        final int size = indexParams.size();

        int index = paramOffset + paramIndex;

        if ((index < 0) || (index >= size)) {
            return null; // 没有对应的列。
        }

        return indexParams.get(index);
    }

    /**
     * 返回指定列的索引位置。
     * 
     * @param column - 指定列
     * @return 列的索引位置
     */
    public int getColumnIndex(Column column) {

        int index = indexParams.indexOf(column);

        if (index >= 0) {
            return index - paramOffset;
        }

        return -1;
    }

    /**
     * 检查对应列的查询参数是否存在。
     * 
     * @param column - 对应的列
     * @return 参数是否存在
     */
    public boolean containsParam(Column column) {

        return namedParams.containsKey(column);
    }

    /**
     * 获得对应列的参数值。
     * 
     * @param column - 对应的列
     * @return 参数值
     */
    public Object getParam(Column column) {

        return namedParams.get(column);
    }

    /**
     * 获得内部函数映射表。
     * 
     * @return 内部函数映射表
     */
    public static Map<String, Function> getFunctionMap() {
        return functionMap;
    }

    /**
     * 创建一个 {@link Table} 对象。
     */
    public static Table newTable(String tableName) {
        Table table = new Table();
        table.setName(tableName);
        return table;
    }

    /**
     * 创建一个 {@link Table} 对象。
     */
    public static Table newTable(String tableName, String aliasName) {
        Table table = new Table();
        table.setName(tableName);
        table.setAlias(aliasName);
        return table;
    }

    /**
     * 创建一个 {@link Column} 对象。
     */
    public static Column newColumn(String composeName) {
        final int index = composeName.indexOf('.');
        if (index >= 0) {
            return newColumn(composeName.substring(0, index), // NL
                    composeName.substring(index + 1));
        }
        return newColumn((Table) null, composeName);
    }

    /**
     * 创建一个 {@link Column} 对象。
     */
    public static Column newColumn(String tableName, String columnName) {
        Column column = new Column();
        column.setTable(newTable(tableName));
        column.setName(columnName.toUpperCase());
        return column;
    }

    /**
     * 创建一个 {@link Column} 对象。
     */
    public static Column newColumn(Table table, String columnName) {
        Column column = new Column();
        column.setTable(table);
        column.setName(columnName.toUpperCase());
        return column;
    }

    /**
     * 处理生成的表达式。
     * 
     * @param expr - 生成的表达式
     * @param column - 当前的列, 可能为 <code>null</code>.
     */
    protected void parseExpr(Expression expr, Column column) {

        if (expr instanceof BaseExpressionList) {

            BaseExpressionList exprList = (BaseExpressionList) expr;

            List<Expression> list = exprList.getAllExpression();

            for (Expression exp : list) {
                parseExpr(exp, null);
            }

        } else if (expr instanceof ColumnExpression) {

            ColumnExpression columnExpr = (ColumnExpression) expr;

            column = columnExpr.getColumn();

            parseExpr(columnExpr.getExpression(), column);

        } else if (expr instanceof ComparisonExpression) {

            ComparisonExpression compExpr = (ComparisonExpression) expr;

            parseExpr(compExpr.getExpression(), column);

        } else if (expr instanceof ParameterExpression) {

            indexParams.add(column);
            namedParams.put(column, null);

        } else if (expr instanceof ConstantExpression) {

            // 注：evaluate 返回常量的值。
            namedParams.put(column, expr.evaluate(null));

        } else if (expr instanceof FunctionExpression) {

            FunctionExpression funcExpr = (FunctionExpression) expr;

            List<Expression> list = funcExpr.getArgList();

            for (Expression exp : list) {
                parseExpr(exp, null);
            }
        }
    }

    // 简单的测试
    public static void main(String... args) throws IOException {

        Matcher matcher = LIMIT_PATTERN.matcher("SELECT * FROM dual LIMIT ?, ?");
        if (matcher.find()) {
            System.out.println(matcher.group(1));
            System.out.println(matcher.group(2));
        }
    }
}
