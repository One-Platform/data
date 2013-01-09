package com.sinosoft.one.data.jade.testDaoSelect;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.UserSelectDao;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.sample.User;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Intro: Contrast Sort Between Jade And JPATest
 * User: Kylin
 * Date: 12-9-12
 * Time: 上午10:13
 * To change this template use File | Settings | File Templates.
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class ContrastSortBetweenJadeAndJPATest {

    @Autowired
    UserSelectDao userSelectDao;

    @Before
    public void initData(){
        List<User> users = new ArrayList<User>();
        for(int i=0; i<20; i++){
            User user = new User();
            user.setId(10000+i);
            user.setActive(true);
            user.setAge((int)(Math.random()*20));
            user.setEmailAddress("user"+i+"@sinosoft.com.cn");
            user.setFirstname("firstname"+(int)(Math.random()*20));
            user.setLastname("lastname"+(int)(Math.random()*20));
            users.add(user);
        }
        userSelectDao.insertDataForContrastSortBetweenJadeAndJPA(users);
    }

    @Test
    public void  contrastPageSortBetweenJadeAndJPATest(){
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"age"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"firstname"));
        Sort sort = new Sort(orders);
        Pageable pageable=new PageRequest(2,5, sort) ;
        userSelectDao.findAll(pageable);
        Page<User> jadePage = userSelectDao.findAllForJade(pageable);
        for(User user:jadePage.getContent()){
            System.out.println(user);
        }
        System.out.println("==========================");
        Page<User> jpaPage = userSelectDao.findAll(pageable);
        for(User user:jpaPage.getContent()){
            System.out.println(user);
        }
        assertEquals(jadePage.getContent().size(),jpaPage.getContent().size());
        for(int i = 0; i < jadePage.getContent().size(); i++){
            assertEquals(jadePage.getContent().get(i).getId(),jpaPage.getContent().get(i).getId());
            assertEquals(jadePage.getContent().get(i).getAge(),jpaPage.getContent().get(i).getAge());
            assertEquals(jadePage.getContent().get(i).getEmailAddress(),jpaPage.getContent().get(i).getEmailAddress());
            assertEquals(jadePage.getContent().get(i).getFirstname(),jpaPage.getContent().get(i).getFirstname());
            assertEquals(jadePage.getContent().get(i).getLastname(),jpaPage.getContent().get(i).getLastname());
        }
    }
    @Test
    public void  contrastSortBetweenJadeAndJPATest(){
        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        orders.add(new Sort.Order(Sort.Direction.DESC,"age"));
        orders.add(new Sort.Order(Sort.Direction.ASC,"firstname"));
        Sort sort = new Sort(orders);

        List<User> users1 = userSelectDao.findAllForJade(sort);
        for(User user:users1){
            System.out.println(user);
        }
        System.out.println("==========================");
        List<User> users2 = (List<User>) userSelectDao.findAll(sort);
        for(User user:users2){
            System.out.println(user);
        }
        assertEquals(users1.size(),users2.size());
        for(int i = 0; i < users1.size(); i++){
            assertEquals(users1.get(i).getId(),users2.get(i).getId());
            assertEquals(users1.get(i).getAge(),users2.get(i).getAge());
            assertEquals(users1.get(i).getEmailAddress(),users2.get(i).getEmailAddress());
            assertEquals(users1.get(i).getFirstname(),users2.get(i).getFirstname());
            assertEquals(users1.get(i).getLastname(),users2.get(i).getLastname());
        }
    }
    @After
    public void destroy(){
        userSelectDao.deleteDataForContrastSortBetweenJadeAndJPA();
    }
}
