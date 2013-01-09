package com.sinosoft.one.data.jade.parsers.parser.function;

import java.util.List;

import com.sinosoft.one.data.jade.parsers.parser.expression.Expression;
import com.sinosoft.one.data.jade.parsers.sqljep.ParseException;
import com.sinosoft.one.data.jade.parsers.util.ThreadLocalMap;

public class LastInsertId extends AbstractFunction implements ThreadLocalSettingFunction {

	public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		return null;
	}

	public void invoke() {
		ThreadLocalMap.put(LastInsertId.class.getName(), Boolean.TRUE);		
	}

}
