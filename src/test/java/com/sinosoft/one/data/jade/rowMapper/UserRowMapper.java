package com.sinosoft.one.data.jade.rowMapper;

import com.sinosoft.one.data.jade.model.User1;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Chunliang.Han
 * Time: 12-9-5[下午5:51]
 */
public class UserRowMapper implements  RowMapper {
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        User1 user = new User1();
        user.setId(rs.getString(1));
        user.setName(rs.getString(2));
        return user ;
    }
}
