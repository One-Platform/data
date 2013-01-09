package com.sinosoft.one.data.jade.parsers.parser.function;

import java.util.List;

import com.sinosoft.one.data.jade.parsers.parser.expression.Expression;
import com.sinosoft.one.data.jade.parsers.sqljep.ParseException;
import com.sinosoft.one.data.jade.parsers.util.StringUtil;

public class Ascii extends AbstractFunction {

	@SuppressWarnings("unchecked")
    public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		if(list.size()==0){
			return null;
		}
			
		Comparable param = list.get(0).evaluate(parameters);
		if(param == null){
			return null;
		}
		String str = String.valueOf(param);
		if(StringUtil.isEmpty(str)){
			return 0;
		}else{
		}
		return (int)str.charAt(0);
	}
}
