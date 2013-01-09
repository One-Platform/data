package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.TestProcedureInsertModel;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-12-6
 * Time: 下午2:35
 * To change this template use File | Settings | File Templates.
 */
public interface ProcedureInsertDao extends PagingAndSortingRepository<User, String> {
    @SQL("{ call testinsert(?1.id, ?1.name, ?1.createTime) }")
    void insertProcedure(TestProcedureInsertModel model);
}
