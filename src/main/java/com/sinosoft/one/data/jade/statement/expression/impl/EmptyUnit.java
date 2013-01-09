package com.sinosoft.one.data.jade.statement.expression.impl;

import com.sinosoft.one.data.jade.statement.expression.ExprResolver;
import com.sinosoft.one.data.jade.statement.expression.ExqlContext;
import com.sinosoft.one.data.jade.statement.expression.ExqlUnit;

/**
 * 输出空白的语句单元, 代替空的表达式。
 * 
 * @author han.liao
 */
public class EmptyUnit implements ExqlUnit {


    public boolean isValid(ExprResolver exprResolver) {
        // Empty unit is always valid.
        return true;
    }


    public void fill(ExqlContext exqlContext, ExprResolver exprResolver) throws Exception {
        // Do nothing.
    }
}
