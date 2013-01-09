package com.sinosoft.one.data.arch4.service.Impl;

import com.sinosoft.one.data.arch4.service.PageService;
import com.sinosoft.one.data.arch4.model.Gender;
import ins.framework.common.Page;
import ins.framework.common.QueryRule;
import ins.framework.dao.GenericDaoHibernate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * User: Chunliang.Han
 * Time: 12-9-18[下午4:07]
 */
@Service
public class PageServiceImpl extends GenericDaoHibernate<Gender, String> implements PageService {
     public Page find1(){
         Page page = findByHql("from Gender",1,3);
         return page;
     }

    public Page find2() {
        Page page=findByHqlNoLimit("from Gender",1,3);
        return page;
    }
    public Page find3() {
        List<String> hqls = new ArrayList<String>();
        hqls.add("from Gender");
        hqls.add("from Gender");
        hqls.add("from Gender");
        List<List<Object>> args = new ArrayList<List<Object>>();
        List<Object> arg1 = new ArrayList<Object>();
        //arg1.add(1) ;
        List<Object> arg2 = new ArrayList<Object>();
        //arg2.add(1) ;
        List<Object> arg3 = new ArrayList<Object>();
        //arg3.add(1) ;
        args.add(arg1);
        args.add(arg2);
        args.add(arg3);
        Page page=findTopUnionByHqls(hqls,3,args) ;
        return page;
    }
    public Page find4() {
        QueryRule queryRule = QueryRule.getInstance();
        Page page=find(queryRule, 1, 3);
        return page;
    }
    public Page find5() {
        List<String> hqls = new ArrayList<String>();
        hqls.add("from Gender");
        hqls.add("from Gender");
        hqls.add("from Gender");
        List<List<Object>> args = new ArrayList<List<Object>>();
        List<Object> arg1 = new ArrayList<Object>();
        //arg1.add(1) ;
        List<Object> arg2 = new ArrayList<Object>();
        //arg2.add(1) ;
        List<Object> arg3 = new ArrayList<Object>();
        //arg3.add(1) ;
        args.add(arg1);
        args.add(arg2);
        args.add(arg3);
        Page page=findUnionByHqls(hqls,args,1,3);
        return page;
    }
}
