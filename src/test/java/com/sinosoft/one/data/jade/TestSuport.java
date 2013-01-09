package com.sinosoft.one.data.jade;

import com.sinosoft.one.data.jade.dao.UserAddDao;
import com.sinosoft.one.data.jade.dao.UserDeleteDao;
import com.sinosoft.one.data.jade.model.User;
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

/**
 * User: Chunliang.Han
 * Time: 12-9-5[下午2:15]
 */
public class TestSuport {
    @Autowired
    UserAddDao userAddDao ;
    @Autowired
    UserDeleteDao userDeleteDao ;
    List<User> users = new ArrayList<User>();
    SimpleDateFormat sdf = new  SimpleDateFormat("yyyy-MM-dd");
    private void initUsers() throws Exception {
        for(int i=0;i<9;i++){
            User user = new User();
            Date date = sdf.parse("2012-01-0"+(i%10+1));
            user.setId("AAF00"+i).setAge(13+i).setName("user"+i).setGender(""+(i%2))
                    .setGroupIds("" + (i % 4 + 1)).setMoney((long) (111001L+i)).setBirthday(date);
            users.add(user);
        }
    }
    public void  init(){
        users.clear();
        try {
            this.initUsers();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        userAddDao.addBatchUserWithAnnoUseEntityParam(users) ;
    }
    public void  destroy(){
        users.clear();
        try {
            this.initUsers();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        userDeleteDao.deleteBatchUserWithAnnoUseEntityParam(users);
    }
}
