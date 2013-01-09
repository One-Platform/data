/*
 * 	This program is free software; you can redistribute it and/or modify it under the terms of 
 * the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, 
 * or (at your option) any later version. 
 * 
 * 	This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  
 * See the GNU General Public License for more details. 
 * 	You should have received a copy of the GNU General Public License along with this program; 
 * if not, write to the Free Software Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.sinosoft.one.data.jade.parsers.parser.function;

import java.util.Iterator;
import java.util.List;

import com.sinosoft.one.data.jade.parsers.parser.expression.Expression;
import com.sinosoft.one.data.jade.parsers.sqljep.ParseException;

public class Divide extends AbstractFunction {

	@SuppressWarnings("unchecked")
	public Comparable evaluate(List<Expression> list, Object[] parameters)
			throws ParseException {
		Comparable param1 = null;
		if(list.size()>=2){
			Iterator<Expression> it =list.iterator();
			param1 = it.next().evaluate(parameters);
			Comparable param2 = it.next().evaluate(parameters);
			param1 = com.sinosoft.one.data.jade.parsers.sqljep.function.Divide.div(param1, param2);
		}
		return param1;
	}

}
