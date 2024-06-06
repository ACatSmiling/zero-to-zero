package cn.zero.cloud.platform.kafka.common.mapper;


import java.io.IOException;

/**
 * @author Xisun Wang
 * @since 2024/3/8 15:21
 */
public interface EntityMapper {

    String mapToString(Object object) throws IOException;

    <T> T mapToObject(String source, Class<T> clazz) throws IOException;

    <T> T convertValue(Object fromValue, Class<T> toValueType);
}