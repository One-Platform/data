package com.sinosoft.one.data.jade.testDaoUpdate;

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
 * Date: 12-9-5
 * Time: 下午8:47
 * To change this template use File | Settings | File Templates.
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-context-jade.xml")
public class GroupUpdateTest {

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
    public void updateGroupWithAnnoUseEntityParamTest(){
        String[] ids = {"g0"};
        String name = "new_group0";
        groupDao.updateGroupWithAnnoUseMultiParam(ids[0],name);
        List<Group> groupList = groupDao.selectGroupById(ids);
        assertEquals(1,groupList.size());
        assertEquals(name,groupList.get(0).getName());
    }

    @Test
    public void updateBatchGroupWithAnnoUseEntityParamTest(){
        List<Group> groups = new ArrayList<Group>();
        String[] ids = new String[3];
        for(int i=1;i<4;i++){
            Group group = new Group();
            group.setId("g"+i);
            group.setName("new_group"+i);
            groups.add(group);
            ids[i-1] = "g"+i;
        }
        groupDao.updateBatchGroupWithAnnoUseEntityParam(groups);
        List<Group> groupList = groupDao.selectGroupById(ids);
        assertEquals(3,groupList.size());
        for(int i=0;i<groupList.size();i++){
            assertEquals(groups.get(i).getName(),groupList.get(i).getName());
        }
    }

    @After
    public void destroyData(){
        groupDao.deleteAllGroup();
    }
}
