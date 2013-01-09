/**
 * @author 54chen(陈臻) [chenzhen@xiaomi.com czhttp@gmail.com]
 * @since 2011-3-9 上午11:43:42
 */

package com.sinosoft.one.data.jade.routing;

import com.sinosoft.one.data.jade.parsers.parser.dbobject.Column;
import com.sinosoft.one.data.jade.parsers.parser.dbobject.Table;

import com.sinosoft.one.data.jade.statement.Interpreter;
import com.sinosoft.one.data.jade.statement.StatementRuntime;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.BadSqlGrammarException;

import java.util.Map;

@Order(9000)
public class RewriteSQLInterpreter implements Interpreter {
    private static final Log logger = LogFactory.getLog(RewriteSQLInterpreter.class);

    private RoutingConfigurator routingConfigurator;


    public void interpret(StatementRuntime runtime) {
        if (runtime.getMetaData().getShardByIndex() < 0) {
            return;
        }
        String sql = runtime.getSQL();
        SQLParseInfo parseInfo = SQLParseInfo.getParseInfo(sql);
        // 从查询的数据表获取路由配置。
        Table[] tables = parseInfo.getTables();

        RoutingInfo routingInfo = null;
        //
        if (tables != null) {
            int beginIndex = 0;
            if (parseInfo.isInsert() && tables.length > 1) {
                // INSERT ... SELECT 查询
                beginIndex = 1;
            }

            // 查找散表配置
            for (int i = beginIndex; i < tables.length; i++) {
                RoutingDescriptor descriptor = routingConfigurator.getDescriptor(tables[i].getName());
                if (descriptor != null) {
                    routingInfo = new RoutingInfo(tables[i], descriptor);
                    break;
                }
            }
        }
        if (routingInfo == null) {
            return;
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("Find routing info: " + routingInfo.byTable + ", " + routingInfo.getDbRouterColumn());
            }
        }

        String forwardTableName = null;
        // String forwardDbPattern = null;

        if (routingInfo.getTableRouter() != null) {

            // 用语句信息的常量进行散表。
            Column column = routingInfo.getTableRouterColumn();
            Object columnValue = null;

            if (column != null) {
                columnValue = findShardParamValue(runtime, column);
                if (columnValue == null) {
                    throw new BadSqlGrammarException("sharding", parseInfo.getSQL(), null);
                }
            }

            // 获得散表的名称
            forwardTableName = routingInfo.getTableRouter().doRoute(columnValue);
        } else {
            if (logger.isDebugEnabled()) {
                logger.debug("table router is null for sql \"" + sql + "\"");
            }
        }

        String byTableName = routingInfo.byTable.getName();
        final String sqlRewrited;
        if ((forwardTableName != null) && !forwardTableName.equals(byTableName)) {

            // 使用 SqlRewriter 拆分语句，进行所需的查找和替换。
            sqlRewrited = SqlRewriter.rewriteSqlTable(sql, byTableName, forwardTableName);

            // 输出重写日志
            if (logger.isDebugEnabled()) {
                logger.debug("Rewriting SQL: \n  From: " + sql + "\n  To:   " + sqlRewrited);
            }
        } else {
            sqlRewrited = sql;
        }
        runtime.setSQL(sqlRewrited);
    }



    class RoutingInfo {

        private Table byTable;

        private RoutingDescriptor descriptor;

        public RoutingInfo(Table table, RoutingDescriptor descriptor) {
            this.byTable = table;
            this.descriptor = descriptor;
        }

        public Router getDbRouter() {
            return descriptor.getDbRouter();
        }

        public Router getTableRouter() {
            return descriptor.getTableRouter();
        }

        private Column dbRouterColumn;

        public Column getDbRouterColumn() {
            if (dbRouterColumn != null) {
                return dbRouterColumn;
            }
            Router dbRouter = getDbRouter();
            if (dbRouter == null) {
                return null;
            }

            String columnName = dbRouter.getColumn();

            if (columnName != null) {

                // 保存匹配的数据列
                Column columnForDBPartition = new Column();
                columnForDBPartition.setName(columnName.toUpperCase());
                columnForDBPartition.setTable(byTable);
                this.dbRouterColumn = columnForDBPartition;
            }
            return dbRouterColumn;
        }

        private Column tableRouterColumn;

        public Column getTableRouterColumn() {
            if (tableRouterColumn != null) {
                return tableRouterColumn;
            }
            Router tableRouter = getTableRouter();
            if (tableRouter == null) {
                return null;
            }

            String columnName = tableRouter.getColumn();

            if (columnName != null) {

                // 保存匹配的数据列
                Column tableRouterColumn = new Column();
                tableRouterColumn.setName(columnName.toUpperCase());
                tableRouterColumn.setTable(byTable);
                this.tableRouterColumn = tableRouterColumn;
            }
            return tableRouterColumn;
        }
    }

    // 查找散表参数
    protected static Object findShardParamValue(StatementRuntime runtime, Column column) {
        if (runtime.getMetaData().getShardByIndex() < 0) {
            throw new BadSqlGrammarException("interpreter.findShardParamValue@ShardByIndex < 0", "SQL [" + runtime.getSQL()
                    + "] shardByIndex: " // NL
                    + runtime.getMetaData().getShardByIndex(), null);
        }
        Object value = null;
        int sqlIndex = runtime.getMetaData().getShardByIndex() + 1;
        Map<String, Object> parameters = runtime.getParameters();
        value = parameters.get(":" + sqlIndex);
        if (value == null) {
            throw new BadSqlGrammarException("interpreter.findShardParamValue@ShardParam", "SQL [" + runtime.getSQL()
                    + "] Query without shard parameter: " + runtime.getParameters().toString(), null);
        }
        return value;
    }

    public RoutingConfigurator getRoutingConfigurator() {
        return routingConfigurator;
    }

    public void setRoutingConfigurator(RoutingConfigurator routingConfigurator) {
        this.routingConfigurator = routingConfigurator;
    }

}
