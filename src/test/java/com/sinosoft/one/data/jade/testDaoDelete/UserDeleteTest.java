package com.sinosoft.one.data.jade.testDaoDelete;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.dao.UserAddDao;
import com.sinosoft.one.data.jade.dao.UserDeleteDao;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * User: Chunliang.Han
 * Time: 12-8-6[下午12:03]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class UserDeleteTest extends TestSuport{

    @Autowired
    UserDeleteDao userDeleteDao;
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
    public void deleteUserWithAnnoUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-01");
        User user = new User();
        user.setId("AAF000");
        user.setName("user0");
        user.setAge(13);
        user.setBirthday(date);
        user.setGender("0");
        user.setGroupIds("1");
        user.setMoney(111001);
        int i = userDeleteDao.deleteUserWithAnnoUseEntityParam(user);
        assertEquals(1,i);
        user = userDeleteDao.selectById(user.getId());
        assertNull(user);
    }

    @Test
    public void deleteUserWithSqlQueryUseEntityParamTest() throws Exception{
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        User user = new User();
        user.setId("AAF001");
        user.setName("user1");
        user.setAge(14);
        user.setBirthday(date);
        user.setGender("1");
        user.setGroupIds("2");
        user.setMoney(111002);
        int i = userDeleteDao.deleteUserWithSqlQueryUseEntityParam(user);
        assertEquals(1,i);
        user = userDeleteDao.selectById(user.getId());
        assertNull(user);
    }

    @Test
    public void deleteUserWithAnnoByIdTest(){
        String id = "AAF002";
        int i = userDeleteDao.deleteUserWithAnnoById(id);
        assertEquals(1,i);
        User user = userDeleteDao.selectById(id);
        assertNull(user);
    }

    @Test
    public void deleteUserWithSqlQueryByIdTest(){
        String id = "AAF003";
        int i = userDeleteDao.deleteUserWithAnnoById(id);
        assertEquals(1,i);
        User user = userDeleteDao.selectById(id);
        assertNull(user);
    }

    @Test
    public void deleteBatchUserWithAnnoUseEntityParamTest() throws Exception{
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=4;i<=6;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAF00"+i).setAge(13+i).setName("user"+i).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
        int[] count = userDeleteDao.deleteBatchUserWithAnnoUseEntityParam(users);
        assertEquals(3,count.length);
        String[] ids = {"AAF004","AAF005","AAF006"};
        List<User> userList = userDeleteDao.selectByIds(ids);
        assertEquals(0,userList.size());
    }

    @Test
    public void deleteBatchUserWithSqlQueryUseEntityParamTest() throws Exception{
        List<User> users = new ArrayList<User>();
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        for(int i=7;i<=8;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAF00"+i).setAge(13+i).setName("user"+i).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
        int count = userDeleteDao.deleteBatchUserWithSqlQueryUseEntityParam(users);
        assertEquals(2,count);
        String[] ids = {"AAF007","AAF008"};
        List<User> userList = userDeleteDao.selectByIds(ids);
        assertEquals(0,userList.size());
    }

    @After
    public void destroy(){
        userAddDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
