package com.rg.smarts.infrastructure.mapper.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@Component
@MappedJdbcTypes(JdbcType.VARCHAR)
@MappedTypes({Object.class}) // 声明支持所有类型
public class JsonTypeHandler<T> extends BaseTypeHandler<T> {
    
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private Class<T> type;

    // 必须有无参构造器
    public JsonTypeHandler() {
    }

    // MyBatis会调用这个构造器
    public JsonTypeHandler(Class<T> type) {
        this.type = type;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, T parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (JsonProcessingException e) {
            throw new SQLException("Error converting object to JSON", e);
        }
    }

    @Override
    public T getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return parseJson(json);
    }

    @Override
    public T getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return parseJson(json);
    }

    @Override
    public T getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return parseJson(json);
    }

    private T parseJson(String json) {
        if (json == null || json.isEmpty()) {
            return null;
        }
        try {
            // 如果type不为null（通过构造器传入），使用具体类型
            // 否则使用TypeReference处理复杂泛型（如List<Long>）
            return type != null 
                ? objectMapper.readValue(json, type)
                : objectMapper.readValue(json, new TypeReference<T>() {});
        } catch (IOException e) {
            throw new RuntimeException("Error parsing JSON string: " + json, e);
        }
    }
}