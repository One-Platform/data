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
package com.sinosoft.one.data.jade.parsers.parser.expression;

import com.sinosoft.one.data.jade.parsers.sqljep.function.Comparative;

/**
 * 可比较表达式
 * @author <a href=mailto:piratebase@sina.com>Struct chen</a>
 *
 */
public class ComparisonExpression extends Expression {
	
	private int comparison;
	private Expression expression;

	@SuppressWarnings("unchecked")
	@Override
	public Comparable evaluate(Object[] parameters) {
		if(expression != null){
			Comparable<?> comparable = expression.evaluate(parameters);
			if(comparable != null){
				return new Comparative(comparison,comparable);
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public Expression reverse(){
		this.setComparison(Comparative.reverseComparison(this.getComparison()));
		return this;
	}
	
	public int getComparison() {
		return comparison;
	}

	public void setComparison(int function) {
		this.comparison = function;
	}

	public boolean isRealtime(){
		return expression!= null?expression.isRealtime():false;
	}
	
	public Expression getExpression() {
		return expression;
	}

	public void setExpression(Expression expression) {
		this.expression = expression;
	}

	@Override
	protected void toString(StringBuilder builder) {
		if(expression instanceof ConstantExpression){
			ConstantExpression cos = (ConstantExpression)expression;
			if(cos.evaluate(null) == null){
				builder.append(comparison == Comparative.NotEquivalent?" is not null":" is null");
				return;
			}
		}
		builder.append(" ");
		builder.append(Comparative.getComparisonName(comparison));
		builder.append(" ");
		if(expression != null){
			expression.toString(builder);
		}
	}
}
