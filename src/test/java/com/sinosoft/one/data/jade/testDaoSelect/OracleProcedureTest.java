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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * User: Chunliang.Han
 * Time: 12-8-6[下午12:03]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class OracleProcedureTest extends TestSuport {
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
        OutProcedureResult<List<Group>> outProcedureResult = new OutProcedureResult<List<Group>>(Group.class,oracle.jdbc.OracleTypes.CURSOR);
        userSelectDao.testPrcResultSingle1("1", outProcedureResult);
        assertEquals(1,outProcedureResult.getResult().size());
        assertEquals("1",outProcedureResult.getResult().get(0).getId());
        assertEquals("group1",outProcedureResult.getResult().get(0).getName());
    }
    @Test
    public void testPrcResultMany(){
        OutProcedureResult<List<Group>> outSetProcedureResult1 = new OutProcedureResult<List<Group>>(Group.class,oracle.jdbc.OracleTypes.CURSOR);
        OutProcedureResult<List<User>> outSetProcedureResult2 = new OutProcedureResult<List<User>>(User.class,oracle.jdbc.OracleTypes.CURSOR);
        OutProcedureResult<List<Group>> outSetProcedureResult3 = new OutProcedureResult<List<Group>>(Group.class,oracle.jdbc.OracleTypes.CURSOR);
        OutProcedureResult<List<String>> outSetProcedureResult4 = new OutProcedureResult<List<String>>(String.class,oracle.jdbc.OracleTypes.CURSOR);
        userSelectDao.testPrcResultMany1(outSetProcedureResult1, outSetProcedureResult2, outSetProcedureResult3, outSetProcedureResult4);
        assertEquals(4,outSetProcedureResult1.getResult().size());
        assertEquals("3",outSetProcedureResult1.getResult().get(2).getId());
        assertEquals("group3",outSetProcedureResult1.getResult().get(2).getName());

        assertEquals(9,outSetProcedureResult2.getResult().size());
        assertEquals("AAF002",outSetProcedureResult2.getResult().get(2).getId());
        assertEquals("user2",outSetProcedureResult2.getResult().get(2).getName());

        assertEquals(4,outSetProcedureResult3.getResult().size());
        assertEquals("3",outSetProcedureResult3.getResult().get(2).getId());
        assertEquals("group3",outSetProcedureResult3.getResult().get(2).getName());

        assertEquals(4,outSetProcedureResult4.getResult().size());
        assertEquals("group3",outSetProcedureResult4.getResult().get(2));
    }
    @After
    public void destroy(){
        booleanCheckDao.clear();
        userSelectDao.deleteUserAll();
        groupDao.deleteAllGroup();
    }
}
