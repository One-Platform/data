/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License i distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sinosoft.one.data.jade.statement;

import com.sinosoft.one.data.jade.annotation.SQLType;
import com.sinosoft.one.data.jade.dataaccess.DataAccess;
import com.sinosoft.one.data.jade.dataaccess.DataAccessImpl;
import com.sinosoft.one.data.jade.statement.Querier;
import com.sinosoft.one.data.jade.statement.StatementMetaData;
import com.sinosoft.one.data.jade.statement.StatementRuntime;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;

import javax.persistence.EntityManager;
import java.lang.reflect.Array;
import java.util.*;

/**
 * 实现 SELECT 查询。
 *
 * @author 王志亮 [qieqie.wang@gmail.com]
 * @author 廖涵 [in355hz@gmail.com]
 */
public class SelectQuerier implements Querier {

	private final RowMapper<?> rowMapper;

	private final Class<?> returnType;

	private final EntityManager em;

    private Log log = LogFactory.getLog(SelectQuerier.class);

	public SelectQuerier(EntityManager em, StatementMetaData metaData,
						 RowMapper<?> rowMapper) {
		this.em = em;
		this.returnType = metaData.getMethod().getReturnType();
		this.rowMapper = rowMapper;
	}

	public Object execute(SQLType sqlType, StatementRuntime... runtimes) {

		return execute(sqlType, (StatementRuntime) runtimes[0]);
	}

	public Object execute(SQLType sqlType, StatementRuntime runtime) {
		String sql = runtime.getSQL();
		Object[] args = runtime.getArgs();
        DataAccess dataAccess = new DataAccessImpl(em);
        List<?> listResult = null;
        Pageable pageable = null;
        Sort sort = null;
        boolean isPage = false;
        boolean isSort = false;
        Map<String, Object> paramMap = runtime.getParameters();
        for(String key : paramMap.keySet()){
            if(paramMap.get(key) instanceof Pageable){
                pageable = (Pageable) paramMap.get(key);
                isPage = true;
            }
            else if(paramMap.get(key) instanceof Sort){
                sort = (Sort) paramMap.get(key);
                isSort = true;
            }
        }
        if (isPage && !isSort) {
            if(returnType == Page.class ){
                String countSql = parseCountSql(sql);
                Page<?> page = dataAccess.selectByPage(pageable, sql, countSql, args, rowMapper);
                return page;
            }
            else {
                try {
                    log.error("The return type["+returnType+"] must be "+Page.class);
                    throw new Exception("The return type [\"+returnType+\"] is invalid");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (!isPage && isSort){
            return dataAccess.selectBySort(sort,sql,args,rowMapper);
        } else if (isPage && isSort){
            try {
                log.error("Can not use Params:["+Pageable.class+" and "+Sort.class+"at the same time.");
                throw new Exception("Can not use Params:["+Pageable.class+" and "+Sort.class+"at the same time.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {

			listResult = dataAccess.select(sql, args, rowMapper);
			final int sizeResult = listResult.size();

			// 将 Result 转成方法的返回类型
			if (returnType.isAssignableFrom(List.class)) {

				// 返回  List 集合
				return listResult;

			} else if (returnType.isArray() && byte[].class != returnType) {
				Object array = Array.newInstance(returnType.getComponentType(), sizeResult);
				if (returnType.getComponentType().isPrimitive()) {
					int len = listResult.size();
					for (int i = 0; i < len; i++) {
						Array.set(array, i, listResult.get(i));
					}
				} else {
					listResult.toArray((Object[]) array);
				}
				return array;

			} else if (Map.class.isAssignableFrom(returnType)) {
				// 将返回的  KeyValuePair 转换成  Map 对象
				// 因为entry.key可能为null，所以使用HashMap
				Map<Object, Object> map;
				if (returnType.isAssignableFrom(HashMap.class)) {

					map = new HashMap<Object, Object>(listResult.size() * 2);

				} else if (returnType.isAssignableFrom(Hashtable.class)) {

					map = new Hashtable<Object, Object>(listResult.size() * 2);

				} else {

					throw new Error(returnType.toString());
				}
				for (Object obj : listResult) {
					if (obj == null) {
						continue;
					}

					Map.Entry<?, ?> entry = (Map.Entry<?, ?>) obj;

					if (map.getClass() == Hashtable.class && entry.getKey() == null) {
						continue;
					}

					map.put(entry.getKey(), entry.getValue());
				}

				return map;

			} else if (returnType.isAssignableFrom(HashSet.class)) {

				// 返回  Set 集合
				return new HashSet<Object>(listResult);

			} else {

				if (sizeResult == 1) {
					// 返回单个  Bean、Boolean等类型对象
					return listResult.get(0);

				} else if (sizeResult == 0) {

					// 基础类型的抛异常，其他的返回null
					if (returnType.isPrimitive()) {
						String msg = "Incorrect result size: expected 1, actual " + sizeResult + ": "
								+ runtime.getMetaData();
						throw new EmptyResultDataAccessException(msg, 1);
					} else {
						return null;
					}

				} else {
					// IncorrectResultSizeDataAccessException
					String msg = "Incorrect result size: expected 0 or 1, actual " + sizeResult + ": "
							+ runtime.getMetaData();
					throw new IncorrectResultSizeDataAccessException(msg, 1, sizeResult);
				}
			}
		}
        return listResult;
	}

	private String parseCountSql(String sql) {
		sql = StringUtils.lowerCase(sql);
		int end = StringUtils.indexOf(sql, "from");
		String s = StringUtils.substring(sql,end);
		return "select count(1) " + s;
	}

}
