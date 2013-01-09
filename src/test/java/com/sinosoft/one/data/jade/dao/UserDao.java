package com.sinosoft.one.data.jade.dao;

import com.sinosoft.one.data.jade.annotation.SQL;
import com.sinosoft.one.data.jade.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-8-6
 * Time: 下午12:03
 * select methods
 */

public interface UserDao extends PagingAndSortingRepository<org.springframework.data.jpa.domain.sample.User, String> {

    @SQL("select * from t_user where id = ?1")
    public User selectById(String id);

    @SQL("select * from t_user where id in (?1)")
    public List<User> selectByIds(String[] ids);

    @SQL("select count(1) from t_user")
    public int countUser();

    @SQL("delete from t_user where id=?1")
    public void deleteById(String id);

    @SQL("delete from t_user")
    public void deleteUserAll();

    @SQL("delete from t_user where id=?1.id")
    public void deleteByUser(User user);
}
