package com.sinosoft.one.data.jade.testProdureInsert;

import com.sinosoft.one.data.jade.dao.ProcedureInsertDao;
import com.sinosoft.one.data.jade.model.TestProcedureInsertModel;
import junit.framework.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * Created with IntelliJ IDEA.
 * User: carvin
 * Date: 12-12-6
 * Time: 下午2:37
 * To change this template use File | Settings | File Templates.
 */
@DirtiesContext
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
@TransactionConfiguration(transactionManager = "transactionManager")
public class ProcedureInsertTest extends AbstractTransactionalJUnit4SpringContextTests {
    @Autowired
    private ProcedureInsertDao procedureInsertDao;

    @Test
    public void testProcedureInsert() {
        TestProcedureInsertModel model = new TestProcedureInsertModel(RandomStringUtils.randomAlphabetic(8), "carvin3", new java.util.Date());
        String id = model.getId();
       // procedureInsertDao.insertProcedure(model);
        TestProcedureInsertModel resultModel = procedureInsertDao.selectTestProcedureInsertModel(id);
        Assert.assertTrue(resultModel != null);
        Assert.assertEquals(id, resultModel.getId());

        model = new TestProcedureInsertModel(RandomStringUtils.randomAlphabetic(8), "carvin3", new Timestamp(System.currentTimeMillis()));
        id = model.getId();
       // procedureInsertDao.insertProcedure(model);
        resultModel = procedureInsertDao.selectTestProcedureInsertModel(id);
        Assert.assertTrue(resultModel != null);
        Assert.assertEquals(id, resultModel.getId());

        model = new TestProcedureInsertModel(RandomStringUtils.randomAlphabetic(8), "carvin3", new Date(System.currentTimeMillis()));
        id = model.getId();
      //  procedureInsertDao.insertProcedure(model);
        resultModel = procedureInsertDao.selectTestProcedureInsertModel(id);
        Assert.assertTrue(resultModel != null);
        Assert.assertEquals(id, resultModel.getId());
    }

    @After
    public void tearDown(){
        procedureInsertDao.deleteAll();
    }
}
