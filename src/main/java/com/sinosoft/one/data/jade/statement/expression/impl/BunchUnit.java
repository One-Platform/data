package com.sinosoft.one.data.jade.statement.expression.impl;

import com.sinosoft.one.data.jade.statement.expression.ExprResolver;
import com.sinosoft.one.data.jade.statement.expression.ExqlContext;
import com.sinosoft.one.data.jade.statement.expression.ExqlUnit;

import java.util.List;

/**
 * 顺序输出子单元的语句单元, 例如一个语句段。
 * 
 * @author han.liao
 */
public class BunchUnit implements ExqlUnit {

    private final List<ExqlUnit> units;

    /**
     * 构造顺序输出子单元的语句单元。
     * 
     * @param units - 子单元列表
     */
    public BunchUnit(List<ExqlUnit> units) {
        this.units = units;
    }


    public boolean isValid(ExprResolver exprResolver) {

        // 顺序检查子单元
        for (ExqlUnit unit : units) {

            if (!unit.isValid(exprResolver)) {
                return false;
            }
        }

        // 子单元全部有效
        return true;
    }


    public void fill(ExqlContext exqlContext, ExprResolver exprResolver) throws Exception {

        // 顺序输出子单元
        for (ExqlUnit unit : units) {
            unit.fill(exqlContext, exprResolver);
        }
    }
}
