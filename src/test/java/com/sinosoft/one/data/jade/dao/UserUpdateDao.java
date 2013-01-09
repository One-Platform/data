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
public interface UserUpdateDao extends UserDao  {
    //2.1.1
    @SQL("update t_user set " +
            "name=:user.name,age=:user.age,birthday=:user.birthday,money=:user.money,gender=:user.gender,groupids=:user.groupIds " +
            " where id=:user.id")
    int updateUserWithAnnoUseEntityParam(@Param("user") User user);

    //2.1.2
    int updateUserWithSqlQueryUseEntityParam(@Param("user") User user);

    //2.1.3
    @SQL("update t_user set name=?2,age=?3,money=?4,gender=?5,groupids=?6,birthday=?7 where id=?1")
    int updateUserWithAnnoUseMultiParam(String id,String name,int age,long money,String gender,String groupid,Date birthday);

    //2.1.4
    int updateUserWithSqlQueryUseMultiParam(String id,String name,int age,long money,String gender,String groupid,Date birthday);

    //2.2.1
    @SQL("update t_user set " +
            "name=?1.name,age=?1.age,birthday=?1.birthday,money=?1.money,gender=?1.gender,groupids=?1.groupIds" +
            " where id=?1.id")
    int[] updateBatchUserWithAnnoUseEntityParam(List<User> user);

    //2.2.2
    int updateBatchUserWithSqlQueryUseEntityParam(List<User> user);

}
