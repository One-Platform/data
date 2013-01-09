package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.User;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * User: Chunliang.Han
 * Time: 12-9-4[下午4:57]
 */
public interface UserDeleteDao extends UserDao  {
    //3.1.1
    @SQL("delete from t_user where id=:user.id")
    int deleteUserWithAnnoUseEntityParam(@Param("user") User user);

    //3.1.2
    int deleteUserWithSqlQueryUseEntityParam(@Param("user") User user);

    //3.1.3
    @SQL("delete from t_user where id=?1")
    int deleteUserWithAnnoById(String id);

    //3.1.4
    int deleteUserWithSqlQueryById(String id);

    //3.2.1
    @SQL("delete from t_user where id=?1.id")
    int[] deleteBatchUserWithAnnoUseEntityParam(List<User> user);

    //3.2.2
    int deleteBatchUserWithSqlQueryUseEntityParam(List<User> user);

}
