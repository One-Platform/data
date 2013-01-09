package com.sinosoft.one.data.arch4;

import static org.junit.Assert.assertEquals;

import com.sinosoft.one.data.arch4.service.PageService;
import com.sinosoft.one.data.arch4.model.Gender;
import ins.framework.common.Page;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * User: Chunliang.Han
 * Time: 12-9-18[下午4:07]
 */
@DirtiesContext
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations ={"classpath:applicationContext-arch.xml","classpath:applicationContext-hibernate.xml","classpath:applicationContext-arch-service.xml"} )

public class PageTest {
    @Autowired
    PageService pageService ;
    @Test
    public void test1(){
        Page page = pageService.find1();
        assertEquals(page.getPageSize(),3);
        assertEquals(9,page.getTotalCount());
        List<Gender> genders= page.getResult();
        for(Gender gender:genders){
            System.out.println("id:"+gender.getId()+",name:"+gender.getName());
        }
    }
    @Test
    public void test2(){
        Page page = pageService.find2();
        assertEquals(page.getPageSize(),3);
        assertEquals(9,page.getTotalCount());
        List<Gender> genders= page.getResult();
        for(Gender gender:genders){
            System.out.println("id:"+gender.getId()+",name:"+gender.getName());
        }
    }
    @Test
    public void test3(){
        Page page = pageService.find3();
        assertEquals(page.getPageSize(),3);
        assertEquals(3,page.getTotalCount());
        List<Gender> genders= page.getResult();
        for(Gender gender:genders){
            System.out.println("id:"+gender.getId()+",name:"+gender.getName());
        }
    }
    @Test
    public void test4(){
        Page page = pageService.find4();
        assertEquals(page.getPageSize(),3);
        assertEquals(9,page.getTotalCount());
        List<Gender> genders= page.getResult();
        for(Gender gender:genders){
            System.out.println("id:"+gender.getId()+",name:"+gender.getName());
        }
    }
    @Test
    public void test5(){
        Page page = pageService.find5();
        assertEquals(page.getPageSize(),3);
        assertEquals(27,page.getTotalCount());
        List<Gender> genders= page.getResult();
        for(Gender gender:genders){
            System.out.println("id:"+gender.getId()+",name:"+gender.getName());
        }
    }
}
