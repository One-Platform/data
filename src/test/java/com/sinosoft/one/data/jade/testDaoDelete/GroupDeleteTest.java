package com.sinosoft.one.data.jade.testDaoDelete;

import com.sinosoft.one.data.jade.dao.GroupDao;
import com.sinosoft.one.data.jade.model.Group;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Intro:
 * User: Kylin
 * Date: 12-9-6
 * Time: 上午9:51
 * To change this template use File | Settings | File Templates.
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class GroupDeleteTest {

    @Autowired
    GroupDao groupDao;

    @Before
    public void initData(){
        List<Group> groups = new ArrayList<Group>();
        for(int i=0;i<4;i++){
            Group group = new Group();
            group.setId("g"+i);
            group.setName("group"+i);
            groups.add(group);
        }
        groupDao.addBatchGroupWithAnnoUseEntityParam(groups);
    }

    @Test
    public void deleteGroupWithAnnoByIdTest(){
        String[] ids = {"g0"};
        List<Group> groupList = groupDao.selectGroupById(ids);
        assertEquals(1,groupList.size());
        groupDao.deleteGroupWithAnnoById(ids[0]);
        groupList = groupDao.selectGroupById(ids);
        assertEquals(0,groupList.size());
    }

    @Test
    public void deleteBatchGroupWithAnnoUseEntityParamTest(){
        List<Group> groups = new ArrayList<Group>();
        String[] ids = new String[3];
        for(int i=1;i<3;i++){
            Group group = new Group();
            group.setId("g"+i);
            group.setName("new_group"+i);
            groups.add(group);
            ids[i-1] = "g"+i;
        }
        List<Group> groupList = groupDao.selectGroupById(ids);
        assertEquals(2,groupList.size());
        groupDao.deleteBatchGroupWithAnnoUseEntityParam(groups);
        groupList = groupDao.selectGroupById(ids);
        assertEquals(0,groupList.size());
    }
    @After
    public void destroyData(){
        groupDao.deleteAllGroup();
    }
}
