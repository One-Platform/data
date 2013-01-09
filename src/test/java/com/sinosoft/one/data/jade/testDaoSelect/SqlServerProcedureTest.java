package com.sinosoft.one.data.jade.testDaoSelect;

import com.sinosoft.one.data.jade.TestSuport;
import com.sinosoft.one.data.jade.dao.BooleanCheckDao;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.dao.UserSelectDao;
import com.sinosoft.one.data.jade.dataaccess.procedure.OutProcedureResult;
import com.sinosoft.one.data.jade.dataaccess.procedure.ResultSetProcedureResult;
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

import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.*;

/**
 * User: Chunliang.Han
 * Time: 12-8-6[下午12:03]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
@Ignore
public class SqlServerProcedureTest extends TestSuport {
    @Autowired
    UserSelectDao userSelectDao;
    @Autowired
    GroupDao groupDao;
    @Autowired
    BooleanCheckDao booleanCheckDao;
    @Before
    public void init(){
        booleanCheckDao.clear();
        booleanCheckDao.insert("1", "0", "false");
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
    public void testPrcOutSingle(){
        OutProcedureResult<List<String>> outProcedureResult1 = new OutProcedureResult<List<String>>(String.class, Types.VARCHAR);
        userSelectDao.testPrcOutSingle("1", outProcedureResult1);
        assertEquals(1, outProcedureResult1.getResult().size());
        assertEquals("group1", outProcedureResult1.getResult().get(0));
    }
    @Test
    public void testPrcResultSingle(){
        ResultSetProcedureResult<List<Group>> ResultSetProcedureResult1 = new ResultSetProcedureResult<List<Group>>(Group.class);
        userSelectDao.testPrcResultSingle("1", ResultSetProcedureResult1);
        assertEquals(1,ResultSetProcedureResult1.getResult().size());
        assertEquals("1",ResultSetProcedureResult1.getResult().get(0).getId());
        assertEquals("group1",ResultSetProcedureResult1.getResult().get(0).getName());
    }
    @Test
    public void testPrcResultMany(){
        ResultSetProcedureResult<List<Group>> resultSetProcedureResult1 = new ResultSetProcedureResult<List<Group>>(Group.class);
        ResultSetProcedureResult<List<User>> resultSetProcedureResult2 = new ResultSetProcedureResult<List<User>>(User.class);
        ResultSetProcedureResult<List<Group>> resultSetProcedureResult3 = new ResultSetProcedureResult<List<Group>>(Group.class);
        ResultSetProcedureResult<List<String>> resultSetProcedureResult4 = new ResultSetProcedureResult<List<String>>(String.class);
        userSelectDao.testPrcResultMany(resultSetProcedureResult1, resultSetProcedureResult2, resultSetProcedureResult3, resultSetProcedureResult4);
        assertEquals(4,resultSetProcedureResult1.getResult().size());
        assertEquals("3",resultSetProcedureResult1.getResult().get(2).getId());
        assertEquals("group3",resultSetProcedureResult1.getResult().get(2).getName());

        assertEquals(9,resultSetProcedureResult2.getResult().size());
        assertEquals("AAF002",resultSetProcedureResult2.getResult().get(2).getId());
        assertEquals("user2",resultSetProcedureResult2.getResult().get(2).getName());

        assertEquals(4,resultSetProcedureResult3.getResult().size());
        assertEquals("3",resultSetProcedureResult3.getResult().get(2).getId());
        assertEquals("group3",resultSetProcedureResult3.getResult().get(2).getName());

        assertEquals(4,resultSetProcedureResult4.getResult().size());
        assertEquals("group3",resultSetProcedureResult4.getResult().get(2));
    }
    @Test
    public void testPrcComplexResult(){
        String id = "1";
        ResultSetProcedureResult<List<Group>> resultSetProcedureResult1 = new ResultSetProcedureResult<List<Group>>(Group.class);
        OutProcedureResult<List<String>> oprt1 = new OutProcedureResult<List<String>>(String.class,Types.VARCHAR);
        OutProcedureResult<List<String>> oprt2 = new OutProcedureResult<List<String>>(String.class,Types.VARCHAR);
        userSelectDao.testPrcComplexResult(id,oprt1,oprt2,resultSetProcedureResult1);
        assertEquals(4,resultSetProcedureResult1.getResult().size());
        assertEquals("3",resultSetProcedureResult1.getResult().get(2).getId());
        assertEquals("group3",resultSetProcedureResult1.getResult().get(2).getName());
        assertEquals("1",oprt1.getResult().get(0));
        assertEquals("group1",oprt2.getResult().get(0));
    }
    @After
    public void destroy(){
        booleanCheckDao.clear();
        userSelectDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
