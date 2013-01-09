package com.sinosoft.one.data.jade.parsers.parser.function;

import java.util.List;

import com.sinosoft.one.data.jade.parsers.parser.expression.Expression;
import com.sinosoft.one.data.jade.parsers.sqljep.ParseException;

public class UnixTimestamp extends AbstractFunction {

	public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		return System.currentTimeMillis() /1000;
	}

}
