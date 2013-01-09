package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.User;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * User: Chunliang.Han
 * Time: 12-9-4[下午4:51]
 */
public interface UserAddDao extends UserDao {

    //1.1.1
    @SQL("insert into t_user values(:user.id,:user.name,:user.age,:user.birthday,:user.money,:user.gender,:user.groupIds)")
    int addUserWithAnnoUseEntityParam(@Param("user") User user);

    //1.1.2
    int addUserWithSqlQueryUseEntityParam(@Param("user") User user);

    //1.1.3
    @SQL("insert into t_user(id,name,age,money,gender,groupIds,birthday) values(?1,?2,?3,?4,?5,?6,?7)")
    int addUserWithAnnoUseMultiParam(String id,String name,int age,long money,String gender,String groupid,Date birthday);

    //1.1.4
    int addUserWithSqlQueryUseMultiParam(String id,String name,int age,long money,String gender,String groupid,Date birthday);

    //1.2.1
    @SQL("insert into t_user values(:user.id,:user.name,:user.age,:user.birthday,:user.money,:user.gender,:user.groupIds)")
    Integer[] addBatchUsersWithAnnoUseEntityParam(@Param("user") List<User> user);

    //1.2.2
    @SQL("insert into t_user values(:user.id,:user.name,:user.age,:user.birthday,:user.money,:user.gender,:user.groupIds)")
    int[] addBatchUserWithAnnoUseEntityParam(@Param("user") List<User> user);

    //1.2.3
    @SQL("insert into t_user values(:user.id,:user.name,:user.age,:user.birthday,:user.money,:user.gender,:user.groupIds)")
    int addBatchUserWithSqlQueryUseEntityParam(@Param("user") List<User> user);
}
