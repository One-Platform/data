package com.sinosoft.one.data.jade.dataaccess.procedure;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: chunliang.han
 * Date: 12-10-15
 * Time: 下午6:13
 * 存储过程结果集
 */
public interface ProcedureResult<T extends List<?>> {
      T getResult();
}
