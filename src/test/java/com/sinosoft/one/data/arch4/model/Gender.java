package com.sinosoft.one.data.arch4.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Intro: Gender Entity for Jade Test
 * User: Kylin
 * Date: 12-9-4
 * Time: 下午3:17
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "t_code_gender")
public class Gender implements Serializable {

    @Id
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
