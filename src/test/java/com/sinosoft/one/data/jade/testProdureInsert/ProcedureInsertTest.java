package com.sinosoft.one.data.jade.testProdureInsert;

import com.sinosoft.one.data.jade.dao.ProcedureInsertDao;
import com.sinosoft.one.data.jade.model.TestProcedureInsertModel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class ProcedureInsertTest {
    @Autowired
    private ProcedureInsertDao procedureInsertDao;

    @Test
    public void testProcedureInsert() {
        TestProcedureInsertModel model = new TestProcedureInsertModel("3", "carvin3", new Timestamp(System.currentTimeMillis()));
        procedureInsertDao.insertProcedure(model);
    }
}
