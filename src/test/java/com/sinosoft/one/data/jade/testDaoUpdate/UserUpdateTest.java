package com.sinosoft.one.data.jade.testDaoUpdate;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.dao.UserAddDao;
import com.sinosoft.one.data.jade.dao.UserUpdateDao;
import com.sinosoft.one.data.jade.model.Group;
import com.sinosoft.one.data.jade.model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.enterprise.inject.New;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Chunliang.Han
 * Time: 12-8-6[下午12:03]
 * To change this template use File | Settings | File Templates.
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class UserUpdateTest extends TestSuport {

    @Autowired
    UserUpdateDao userUpdateDao;
    @Autowired
    UserAddDao userAddDao;
    @Autowired
    GroupDao groupDao;
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
        super.init();
    }

    @Test
    public void updateUserWithAnnoUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-31");
        User user = new User();
        user.setId("AAF000");
        user.setName("user0_new");
        user.setAge(14);
        user.setBirthday(date);
        user.setGender("1");
        user.setGroupIds("2");
        user.setMoney(11100177);
        int i = userUpdateDao.updateUserWithAnnoUseEntityParam(user);
        assertEquals(1,i);
        user = userUpdateDao.selectById(user.getId());
        assertEquals("user0_new",user.getName());
        assertEquals(14,user.getAge());
        assertEquals(date,user.getBirthday());
        assertEquals("1",user.getGender());
        assertEquals("2",user.getGroupIds());
        assertEquals(11100177,user.getMoney());
    }

    @Test
    public void updateUserWithSqlQueryUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-31");
        User user = new User();
        user.setId("AAF001");
        user.setName("user1_new");
        user.setAge(14);
        user.setBirthday(date);
        user.setGender("1");
        user.setGroupIds("2");
        user.setMoney(11100177);
        int i = userUpdateDao.updateUserWithSqlQueryUseEntityParam(user);
        assertEquals(1,i);
        user = userUpdateDao.selectById(user.getId());
        assertEquals("user1_new",user.getName());
        assertEquals(14,user.getAge());
        assertEquals(date,user.getBirthday());
        assertEquals("1",user.getGender());
        assertEquals("2",user.getGroupIds());
        assertEquals(11100177,user.getMoney());
    }

    @Test
    public void updateUserWithAnnoUseMultiParamTest() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-31");
        String id = "AAF002";
        String name = "user2_new";
        int age = 14;
        String gender = "1";
        String groupIds = "2";
        long money = 11100177L;
        int i = userUpdateDao.updateUserWithAnnoUseMultiParam(id,name,age,money,gender,groupIds,date);
        assertEquals(1,i);
        User user = userUpdateDao.selectById(id);
        assertEquals("user2_new",user.getName());
        assertEquals(14,user.getAge());
        assertEquals(date,user.getBirthday());
        assertEquals("1",user.getGender());
        assertEquals("2",user.getGroupIds());
        assertEquals(11100177,user.getMoney());
    }

    @Test
    public void updateUserWithSqlQueryUseMultiParamTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-21");
        String id = "AAF003";
        String name = "user3_new";
        int age = 14;
        String gender = "1";
        String groupIds = "2";
        long money = 11100177L;
        int i = userUpdateDao.updateUserWithSqlQueryUseMultiParam(id, name, age, money, gender, groupIds, date);
        assertEquals(1,i);
        User user = userUpdateDao.selectById(id);
        assertEquals("user3_new",user.getName());
        assertEquals(14,user.getAge());
        assertEquals(date,user.getBirthday());
        assertEquals("1",user.getGender());
        assertEquals("2",user.getGroupIds());
        assertEquals(11100177,user.getMoney());
    }

    @Test
    public void updateBatchUserWithAnnoUseEntityParamTest() throws Exception{
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=4;i<=6;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAF00"+i).setAge(15+i).setName("new_user"+i);
            users.add(user);
        }
        int[] count = userUpdateDao.updateBatchUserWithAnnoUseEntityParam(users);
        assertEquals(3,count.length);
        String[] ids = {"AAF004","AAF005","AAF006"};
        List<User> userList = userUpdateDao.selectByIds(ids);
        assertEquals(3,userList.size());
        for(int i=4;i<=6;i++){
            assertEquals(15+i,userList.get(i-4).getAge());
            assertEquals("new_user"+i,userList.get(i-4).getName());
        }
    }

    @Test
    public void updateBatchUserWithSqlQueryUseEntityParamTest() throws Exception{
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=7;i<=8;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAF00"+i).setAge(15+i).setName("new_user"+i);
            users.add(user);
        }
        int count = userUpdateDao.updateBatchUserWithSqlQueryUseEntityParam(users);
        assertEquals(2,count);
        String[] ids = {"AAF007","AAF008"};
        List<User> userList = userUpdateDao.selectByIds(ids);
        assertEquals(2,userList.size());
        for(int i=7;i<=8;i++){
            assertEquals(15+i,userList.get(i-7).getAge());
            assertEquals("new_user"+i,userList.get(i-7).getName());
        }
    }

    @After
    public void destroy(){
        userAddDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
