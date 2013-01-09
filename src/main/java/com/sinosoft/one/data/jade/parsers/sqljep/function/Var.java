package com.sinosoft.one.data.jade.parsers.sqljep.function;

import com.sinosoft.one.data.jade.parsers.sqljep.ASTFunNode;
import com.sinosoft.one.data.jade.parsers.sqljep.JepRuntime;
import com.sinosoft.one.data.jade.parsers.sqljep.ParseException;

public class Var extends PostfixCommand implements Declare{
	private String identity;
	public Var(String identity){
		this.identity = identity;
	}
	
	public Comparable<?>[] evaluate(ASTFunNode node, JepRuntime runtime)
			throws ParseException {
		node.childrenAccept(runtime.ev, null);
		Comparable<?>  param = runtime.stack.pop();
		return new Comparable<?>[]{param};
	}

	public int getNumberOfParameters() {
		return 1;
	}

	public Comparable<?> getResult(Comparable<?>... comparables)
			throws ParseException {
		return comparables[0];
	}
	
	public boolean isDeclare(){
		return true;
	}

	public void declare(JepRuntime runtime,Comparable<?> comparable) {
		runtime.vars.put(identity, comparable);
	}

}
