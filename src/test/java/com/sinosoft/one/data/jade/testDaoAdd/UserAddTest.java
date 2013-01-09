package com.sinosoft.one.data.jade.testDaoAdd;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.dao.UserAddDao;
import com.sinosoft.one.data.jade.model.Group;
import com.sinosoft.one.data.jade.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * User: Chunliang.Han
 * Time: 12-9-4[下午5:37]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class UserAddTest extends TestSuport {
    @Autowired
    UserAddDao userAddDao ;
    @Autowired
    GroupDao  groupDao;
    @Before
    public void init(){
        userAddDao.deleteUserAll();
        groupDao.deleteAllGroup();

        List<Group> groups = new ArrayList<Group>();
        for(int i=0;i<4;i++){
            Group group = new Group();
            group.setId(""+(i+1));
            group.setName("group"+(i+1));
            groups.add(group);
        }
        groupDao.addBatchGroupWithAnnoUseEntityParam(groups);

    }
    @Test
    public void addUserWithAnnoUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-01");
        User user = new User()
            .setId("AAB001").setAge(13).setName("user1").setGender("1")
            .setGroupIds("1").setMoney(111001L).setBirthday(date);
        int oldCount = userAddDao.countUser();
        int count = userAddDao.addUserWithAnnoUseEntityParam(user);
        //测试更新的返回值
        assertEquals(1,count);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了一条数据
        assertEquals(1,newCount-oldCount);
        //确认增加数据的准确性
        user = userAddDao.selectById(user.getId());
        assertEquals(13,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("1",user.getGender() );
        assertEquals("1",user.getGroupIds());
        assertEquals(111001L,user.getMoney());
        assertEquals(date,user.getBirthday());
        //删除增加的记录
        userAddDao.deleteByUser(user);
    }
    @Test
    public void addUserWithSqlQueryUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-01");
        User user = new User()
                .setId("AAB001").setAge(13).setName("user1").setGender("1")
                .setGroupIds("1").setMoney(111001L).setBirthday(date);
        int oldCount = userAddDao.countUser();
        int count = userAddDao.addUserWithSqlQueryUseEntityParam(user);
        //测试更新的返回值
        assertEquals(1,count);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了一条数据
        assertEquals(1,newCount-oldCount);
        //确认增加数据的准确性
        user = userAddDao.selectById(user.getId());
        assertEquals(13,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("1",user.getGender() );
        assertEquals("1",user.getGroupIds());
        assertEquals(111001L,user.getMoney());
        assertEquals(date,user.getBirthday());
        //删除增加的记录
        userAddDao.deleteByUser(user);
    }
    @Test
    public void addUserWithAnnoUseMultiParamTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-01");
        int oldCount = userAddDao.countUser();
        String id = "AAB001";
        String name = "user1";
        int age = 13;
        String gender = "1";
        String groupIds = "1";
        Long money = 111001L;

        int count = userAddDao.addUserWithAnnoUseMultiParam(id, name, age, money, gender, groupIds, date);
        //测试更新的返回值
        assertEquals(1,count);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了一条数据
        assertEquals(1,newCount-oldCount);
        //确认增加数据的准确性
        User user = userAddDao.selectById(id);
        assertEquals(age,user.getAge());
        assertEquals(name,user.getName());
        assertEquals(gender ,user.getGender() );
        assertEquals(groupIds ,user.getGroupIds());
        assertEquals((long)money,user.getMoney());
        assertEquals(date,user.getBirthday());
        //删除增加的记录
        userAddDao.deleteByUser(user);
    }
    @Test
    public void addUserWithSqlQueryUseMultiParamTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-01");
        int oldCount = userAddDao.countUser();
        String id = "AAB001";
        String name = "user1";
        int age = 13;
        String gender = "1";
        String groupIds = "1";
        Long money = 111001L;

        int count = userAddDao.addUserWithSqlQueryUseMultiParam(id, name, age, money, gender, groupIds, date);
        //测试更新的返回值
        assertEquals(1,count);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了一条数据
        assertEquals(1,newCount-oldCount);
        //确认增加数据的准确性
        User user = userAddDao.selectById(id);
        assertEquals(age,user.getAge());
        assertEquals(name,user.getName());
        assertEquals(gender ,user.getGender() );
        assertEquals(groupIds ,user.getGroupIds());
        assertEquals((long)money,user.getMoney());
        assertEquals(date,user.getBirthday());
        //删除增加的记录
        userAddDao.deleteByUser(user);
    }
    @Test
    public void addBatchUsersWithAnnoUseEntityParamTest()throws Exception{
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<3;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAB00"+i).setAge(17+i).setName("user"+(i+1)).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
        int oldCount = userAddDao.countUser();
        Integer[] counts = userAddDao.addBatchUsersWithAnnoUseEntityParam(users);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了3条数据
        assertEquals(3,newCount-oldCount);
        assertEquals(3,counts.length);
        for(int i=0;i<counts.length;i++){
            assertEquals((Integer)1,counts[i]) ;
        }
        //确认增加数据的准确性
        User user = userAddDao.selectById(users.get(0).getId());
        Date date = sdf.parse("2012-01-01");
        assertEquals(17,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("0",user.getGender() );
        assertEquals("1",user.getGroupIds());
        assertEquals(111001L,user.getMoney());
        assertEquals(date,user.getBirthday());
        userAddDao.deleteUserAll();
    }

    @Test
    public void addBatchUserWithAnnoUseEntityParamTest()throws Exception{
        userAddDao.deleteUserAll();
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<3;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAB00"+i).setAge(17+i).setName("user"+(i+1)).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
        int oldCount = userAddDao.countUser();
        int[] counts = userAddDao.addBatchUserWithAnnoUseEntityParam(users);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了3条数据
        assertEquals(3,newCount-oldCount);
        assertEquals(3,counts.length);
        for(int i=0;i<counts.length;i++){
              assertEquals(1,counts[i]) ;
        }
        //确认增加数据的准确性
        User user = userAddDao.selectById(users.get(0).getId());
        Date date = sdf.parse("2012-01-01");
        assertEquals(17,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("0",user.getGender() );
        assertEquals("1",user.getGroupIds());
        assertEquals(111001L,user.getMoney());
        assertEquals(date,user.getBirthday());
        userAddDao.deleteUserAll();
    }
    @Test
    public void addBatchUserWithSqlQueryUseEntityParamTest()throws Exception{
        userAddDao.deleteUserAll();
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=0;i<3;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAB00"+i).setAge(17+i).setName("user"+(i+1)).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
        int oldCount = userAddDao.countUser();
        int counts = userAddDao.addBatchUserWithSqlQueryUseEntityParam(users);
        int newCount = userAddDao.countUser();
        //确认数据库中只增加了3条数据
        assertEquals(3,newCount-oldCount);
        assertEquals(3,counts);
        //确认增加数据的准确性
        User user = userAddDao.selectById(users.get(0).getId());
        Date date = sdf.parse("2012-01-01");
        assertEquals(17,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("0",user.getGender() );
        assertEquals("1",user.getGroupIds());
        assertEquals(111001L,user.getMoney());
        assertEquals(date,user.getBirthday());
        userAddDao.deleteUserAll();
    }
    @After
    public void destroy(){
        userAddDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
