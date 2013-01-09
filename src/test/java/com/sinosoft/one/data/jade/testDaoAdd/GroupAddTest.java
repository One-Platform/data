package com.sinosoft.one.data.jade.testDaoAdd;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.model.Group;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;

/**
 * User: Chunliang.Han
 * Time: 12-9-5[下午4:34]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class GroupAddTest {
    @Autowired
    GroupDao groupDao;

    @Test
    public void addBatchGroupWithAnnoUseEntityParamTest(){
        groupDao.deleteAllGroup();
        List<Group> groups = new ArrayList<Group>();
        for(int i=0;i<3;i++){
            Group group = new Group();
            group.setId(""+i);
            group.setName("group"+i);
            groups.add(group);
        }
        groupDao.addBatchGroupWithAnnoUseEntityParam(groups);
        int count = groupDao.countGroup();
        assertEquals(3,count);
        List<Group> groupList = groupDao.selectAllGroup();
        Group group = groupList.get(0);
        assertEquals("0",group.getId());
        assertEquals("group0",group.getName());
        for(int i=0;i<3;i++){
            group = groupList.get(i);
            assertEquals(""+i,group.getId());
            assertEquals("group"+i,group.getName());
        }
        groupDao.deleteAllGroup();
    }

    @Test
    public void addGroupWithAnnoUseMultiParam(){
        String id = "1";
        String name = "group1";
        int oldCount = groupDao.countGroup();
        groupDao.addGroupWithAnnoUseMultiParam(id,name);
        int newCount = groupDao.countGroup();
        assertEquals(1,newCount - oldCount);
        List<Group> groupList = groupDao.selectAllGroup();
        Group group = groupList.get(0);
        assertEquals("1",group.getId());
        assertEquals("group1",group.getName());
        groupDao.deleteAllGroup();
    }
}
