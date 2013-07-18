package com.sinosoft.one.data.jade.testDaoSelect;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.BooleanCheckDao;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.dao.UserSelectDao;
import com.sinosoft.one.data.jade.model.Group;
import com.sinosoft.one.data.jade.model.SomePropertis;
import com.sinosoft.one.data.jade.model.User;
import com.sinosoft.one.data.jade.model.User1;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * User: Chunliang.Han
 * Time: 12-8-6[下午12:03]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class SelectTest extends TestSuport {
    @Autowired
    UserSelectDao userSelectDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    BooleanCheckDao booleanCheckDao;
    @Before
    public void init(){
        booleanCheckDao.clear();
        booleanCheckDao.insert("1","0","false");
        booleanCheckDao.insert("2","1","true");
        booleanCheckDao.insert("3","-1","false");
        booleanCheckDao.insert("4","2","true");
        userSelectDao.deleteUserAll();
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
    public void testSelectUserByCondition() {
        List<User> users = userSelectDao.selectUserByCondition("id='AAF000'");
        Assert.assertEquals(1, users.size());
        Assert.assertEquals("user0", users.get(0).getName());
    }

    //4.1.1
    @Test
    public void selectUserWithSqlQueryByIdTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        User user = userSelectDao.selectUserWithSqlQueryById("AAF001");
        assertNotNull(user);
        assertEquals(14,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("1",user.getGender() );
        assertEquals("2",user.getGroupIds());
        assertEquals(111002L,user.getMoney());
        assertEquals(date,user.getBirthday());
    }

    //4.1.2
    @Test
    public void selectUserNameWithAnnoByIdTest() throws Exception{
        String name = userSelectDao.selectUserNameWithAnnoById("AAF001");
        assertNotNull(name);
        assertEquals("user1",name);
    }
    //4.1.3
    @Test
    public void selectUserAgeWithSqlQueryByIdTest() throws Exception{
        int age = userSelectDao.selectUserAgeWithSqlQueryById("AAF001");
        assertNotNull(age);
        assertEquals(14,age);
    }
    //4.1.4
    @Test
     public void selectUserAgeWithAnnoByIdTest() throws Exception{
        Integer age = userSelectDao.selectUserAgeWithAnnoById("AAF001");
        assertNotNull(age);
        assertEquals((Integer)14,age);
    }
    //4.1.5
    @Test
    public void selectUserBirthdayWithSqlQueryByIdTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        Date date1 = userSelectDao.selectUserBirthdayWithSqlQueryById("AAF001");
        assertNotNull(date1);
        assertEquals(date,date1);
    }
    //4.1.6
    @Test
    public void selectUserMoneyWithAnnoByIdTest() throws Exception{
        Long money = userSelectDao.selectUserMoneyWithAnnoById("AAF001");
        assertNotNull(money);
        assertEquals((Long)111002L,money);
    }
    //4.1.7
    @Test
    public void selectUserMoneyWithSqlQUeryByIdTest() throws Exception{
        long money = userSelectDao.selectUserMoneyWithSqlQUeryById("AAF001");
        assertNotNull(money);
        assertEquals(111002L,money);
    }
    //4.1.8 and 4.1.9    非0的数字（可以为字符串类型）返回true,为0的返回false
    @Test
    public void selectBooleanIntWithAnnoByIdTest(){
        boolean isTrue = false;
        Boolean isTrue1 = false;
        isTrue1 = booleanCheckDao.selectBooleanIntWithAnnoById("2") ;
        assertTrue(isTrue1) ;
        isTrue = booleanCheckDao.selectBooleanIntWithAnnoById("1") ;
        assertTrue(!isTrue) ;
        isTrue = booleanCheckDao.selectBooleanIntWithAnnoById("3") ;
        assertTrue(isTrue) ;
        isTrue = booleanCheckDao.selectBooleanIntWithAnnoById("4") ;
        assertTrue(isTrue) ;
    }
    //4.1.10
    @Test
    public void selectBooleanStrWithAnnoByIdTest(){
        boolean isTrue = false;
        boolean flag = false;
        try{
            isTrue = booleanCheckDao.selectBooleanStrWithAnnoById("2");
            assertTrue(isTrue) ;
        } catch (Exception e) {
            assertTrue(!flag) ;
        }
    }
    //4.2.1
    @Test
    public void selectUserWithAnnoByGroupidTest() throws Exception{
        Set<String> groups =  new HashSet<String>(3);
        groups.add("1");
        groups.add("2");
        groups.add("3");
        List <User> users = userSelectDao.selectUserWithAnnoByGroupid(groups);
        assertNotNull(users);
        assertEquals(7,users.size());
        assertEquals("AAF000",users.get(0).getId());
        assertEquals("user0",users.get(0).getName());
        for(int i=0;i<users.size();i++){
            User user = users.get(i) ;
            assertNotNull(user.getAge());
            assertNotNull(user.getId());
            assertNotNull(user.getName());
            assertNotNull(user.getGender());
            assertNotNull(user.getGroupIds());
            assertNotNull(user.getMoney());
            assertNotNull(user.getBirthday());
        }
    }
    //4.2.2
    @Test
    public void selectUserWithAnnoByGroupidTest1() throws Exception{
        List<String> groups =  new ArrayList<String>(3);
        groups.add("1");
        groups.add("2");
        groups.add("3");
        List <User> users = userSelectDao.selectUserWithAnnoByGroupid(groups);
        assertNotNull(users);
        assertEquals(7,users.size());
        assertEquals("AAF000",users.get(0).getId());
        assertEquals("user0",users.get(0).getName());
        for(int i=0;i<users.size();i++){
            User user = users.get(i) ;
            assertNotNull(user.getAge());
            assertNotNull(user.getId());
            assertNotNull(user.getName());
            assertNotNull(user.getGender());
            assertNotNull(user.getGroupIds());
            assertNotNull(user.getMoney());
            assertNotNull(user.getBirthday());
        }
    }
    //4.2.3
    @Test
    public void selectUserWithAnnoByGroupidTest2() throws Exception{
        String[] groups =  new String[]{"1","2","3"};
        List <User> users = userSelectDao.selectUserWithAnnoByGroupid(groups);
        assertNotNull(users);
        assertEquals(7,users.size());
        assertEquals("AAF000",users.get(0).getId());
        assertEquals("user0",users.get(0).getName());
        for(int i=0;i<users.size();i++){
            User user = users.get(i) ;
            assertNotNull(user.getAge());
            assertNotNull(user.getId());
            assertNotNull(user.getName());
            assertNotNull(user.getGender());
            assertNotNull(user.getGroupIds());
            assertNotNull(user.getMoney());
            assertNotNull(user.getBirthday());
        }
    }


    //4.2.4
    @Test
    public void selectUserWithAnnoByIdAndNameTest() throws Exception{
        Map<String,Object> idAndName = new HashMap<String, Object>();
        idAndName.put("id","AAF001");
        idAndName.put("name","user1");
        User user = userSelectDao.selectUserWithAnnoByIdAndName(idAndName);
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        assertNotNull(user);
        assertEquals(14,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("1",user.getGender() );
        assertEquals("2",user.getGroupIds());
        assertEquals(111002L,user.getMoney());
        assertEquals(date,user.getBirthday());
    }
    //4.2.5
    @Test
    public void selectUsersWithAnnoByIdAndNameTest() throws Exception{
        Map<String,Object> idAndName = new HashMap<String, Object>();
        idAndName.put("id","AAF001");
        idAndName.put("name","user1");
        List<User> users = userSelectDao.selectUsersWithAnnoByIdAndName(idAndName);
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        User user = users.get(0);
        assertNotNull(user);
        assertEquals(14,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("1",user.getGender() );
        assertEquals("2",user.getGroupIds());
        assertEquals(111002L,user.getMoney());
        assertEquals(date,user.getBirthday());
    }
    //4.2.6
    @Test
    public void selectUsersWithAnnoByGnameAndUbirthdayTest() throws Exception{

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        Object[] idAndName = new Object[]{"group2",date};
        List<User> users = userSelectDao.selectUsersWithAnnoByGnameAndUbirthday(idAndName);
        assertEquals(1,users.size());
        User user = users.get(0);
        assertNotNull(user);
        assertEquals(0,user.getAge());
        assertEquals("user1",user.getName());
        assertEquals("AAF001",user.getId());
        assertNull(user.getGender() );
        assertNull(user.getGroupIds());
        assertEquals(0L,user.getMoney());
        assertNull(user.getBirthday());
    }
    //4.2.7
    //单个对象接收返回值时，返回值不允许为超过一个的多对象集合
    @Test
    public void selectUserWithAnnoByGnameAndUbirthdayTest() throws Exception{

        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2012-01-02");
        Object[] idAndName = new Object[]{"group2",date};
        boolean flag = true;
        try{
            User user = userSelectDao.selectUserWithAnnoByGnameAndUbirthday(idAndName);
        }
        catch (java.lang.RuntimeException e){
            //e.printStackTrace();
            flag = false ;
        }
        assertEquals(false,flag);
    }

    //4.3.1
    @Test
    public void selectUsersWithSqlQueryForPageTest() throws Exception{

        Pageable pageable=new PageRequest(1,5,Direction.DESC,"gender_name") ;

        Page<SomePropertis> page = userSelectDao.selectUsersWithSqlQueryForPage(pageable);
        assertNotNull(page);
        assertNotNull(page.getContent());
        assertEquals(1,page.getNumber());            //当前页码
        assertEquals(2,page.getTotalPages());        //总共的页数
        assertEquals(5,page.getSize());               //每页大小
        assertEquals(9,page.getTotalElements());     //总条目数
        assertEquals(4,page.getNumberOfElements());  //当前页的条目数
        for(int i=0;i<page.getContent().size();i++){
            SomePropertis prop= page.getContent().get(i) ;
            System.out.println(prop) ;
            assertNotNull(prop.getGenderName());
            assertNotNull(prop.getGroupName());
            assertNotNull(prop.getUserId());
            assertNotNull(prop.getUserName());
        }
    }

    @Test
    public void selectUsersWithAnooForSortTest() throws Exception {
        Sort.Order order1 = new Sort.Order(Direction.DESC,"id");
        Sort sort1 = new Sort(order1);
        Sort.Order order = new Sort.Order(Direction.ASC,"gender");
        Sort sort = new Sort(order);
        List<User> users = userSelectDao.selectUsersWithAnooForSort("AAF00%",sort.and(sort1));
        assertNotNull(users);
        for(User user : users){
            System.out.println();
            System.out.print("[id:"+user.getId()+"]-");
            System.out.print("[name:"+user.getName()+"]-");
            System.out.print("[gender:"+user.getGender()+"]-");
            System.out.print("[groupIds:"+user.getGroupIds()+"]-");
            System.out.print("[age:"+user.getAge()+"]-");
            System.out.print("[money:"+user.getMoney()+"]");
            System.out.println();
        }
    }
    //4.3.2
    @Test
    public void selectUser1WithAnnoByGroupidTest() throws Exception{
        Set<String> groups =  new HashSet<String>(3);
        groups.add("1");
        groups.add("2");
        groups.add("3");
        List <User1> users = userSelectDao.selectUser1WithAnnoByGroupid(groups);
        assertNotNull(users);
        assertEquals(7,users.size());
        for(int i=0;i<users.size();i++){
            User1 user = users.get(i) ;
            assertNotNull(user.getId());
            assertNotNull(user.getName());
        }
    }
    //4.3.3
    @Test
    public void selectUserForActiveSqlTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        List <User> users = userSelectDao.selectUserForActiveSql("user","0","AAF000");
        assertNotNull(users);
        assertEquals(1,users.size());
        User user1 = users.get(0);
        assertEquals("AAF000",user1.getId());
        Date date1 = sdf.parse("2012-01-0"+(1));
        assertEquals(date1,user1.getBirthday());
        assertEquals(""+(0),user1.getGender());
        assertEquals("" + (1),user1.getGroupIds());
        assertEquals((long) (111001L),user1.getMoney());
        assertEquals(13,user1.getAge());
        assertEquals("user0",user1.getName());

        users = userSelectDao.selectUserForActiveSql("user","1","AAF000");
        assertNotNull(users);
        assertTrue(users.size()==9);
        for(int i=0;i<users.size();i++){
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            User user = users.get(i) ;
            assertEquals("AAF00"+i,user.getId());
            assertEquals(date,user.getBirthday());
            assertEquals(""+(i%2),user.getGender());
            assertEquals("" + (i % 4 + 1),user.getGroupIds());
            assertEquals((long) (111001L+i),user.getMoney());
            assertEquals(13+i,user.getAge());
            assertEquals("user"+i,user.getName());
        }
    }
    //4.3.4
    @Test
    public void selectUserForActiveComplexSqlTest() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        List <User> users = userSelectDao.selectUserForActiveComplexSql("user","0","AAF000","user0");
        assertNotNull(users);
        assertEquals(1,users.size());
        User user1 = users.get(0);
        assertEquals("AAF000",user1.getId());
        Date date1 = sdf.parse("2012-01-0"+(1));
        assertEquals(date1,user1.getBirthday());
        assertEquals(""+(0),user1.getGender());
        assertEquals("" + (1),user1.getGroupIds());
        assertEquals((long) (111001L),user1.getMoney());
        assertEquals(13,user1.getAge());
        assertEquals("user0",user1.getName());

        users = userSelectDao.selectUserForActiveComplexSql("user","1","AAF000","user0");
        assertNotNull(users);
        assertTrue(users.size()==9);
        for(int i=0;i<users.size();i++){
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            User user = users.get(i) ;
            assertEquals("AAF00"+i,user.getId());
            assertEquals(date,user.getBirthday());
            assertEquals(""+(i%2),user.getGender());
            assertEquals("" + (i % 4 + 1),user.getGroupIds());
            assertEquals((long) (111001L+i),user.getMoney());
            assertEquals(13+i,user.getAge());
            assertEquals("user"+i,user.getName());
        }
    }
    //4.3.5
    @Test
    public void selectUserForActiveComplexSql1Test() throws Exception{
        SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
        User user1 =  userSelectDao.selectUserForActiveComplexSql1(new int[]{0,1,0});
        assertNotNull(user1);
        assertEquals("AAF000",user1.getId());
        Date date1 = sdf.parse("2012-01-01");
        assertEquals(date1,user1.getBirthday());
        assertEquals("0",user1.getGender());
        assertEquals("1",user1.getGroupIds());
        assertEquals((long) (111001L),user1.getMoney());
        assertEquals(13,user1.getAge());
        assertEquals("user0",user1.getName());

    }
    @After
    public void destroy(){
        booleanCheckDao.clear();
        userSelectDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
