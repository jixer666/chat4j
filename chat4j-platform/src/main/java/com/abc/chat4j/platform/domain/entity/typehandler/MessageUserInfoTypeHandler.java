package com.abc.chat4j.platform.domain.entity.typehandler;

import cn.hutool.json.JSONUtil;
import com.abc.chat4j.im.domain.entity.MessageUserInfo;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(MessageUserInfo.class)  // 指定处理的 Java 类型
@MappedJdbcTypes(JdbcType.VARCHAR)   // 指定处理的 JDBC 类型
public class MessageUserInfoTypeHandler extends BaseTypeHandler<MessageUserInfo> {
    
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, MessageUserInfo parameter, JdbcType jdbcType) 
            throws SQLException {
        ps.setString(i, JSONUtil.toJsonStr(parameter));
    }
    
    @Override
    public MessageUserInfo getNullableResult(ResultSet rs, String columnName) 
            throws SQLException {
        String json = rs.getString(columnName);
        return JSONUtil.toBean(json, MessageUserInfo.class);
    }
    
    @Override
    public MessageUserInfo getNullableResult(ResultSet rs, int columnIndex) 
            throws SQLException {
        String json = rs.getString(columnIndex);
        return JSONUtil.toBean(json, MessageUserInfo.class);
    }
    
    @Override
    public MessageUserInfo getNullableResult(CallableStatement cs, int columnIndex) 
            throws SQLException {
        String json = cs.getString(columnIndex);
        return JSONUtil.toBean(json, MessageUserInfo.class);
    }
}