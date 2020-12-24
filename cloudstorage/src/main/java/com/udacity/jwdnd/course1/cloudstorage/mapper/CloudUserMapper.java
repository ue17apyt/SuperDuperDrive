package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudUser;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface CloudUserMapper {

    @Select("SELECT * FROM USERS WHERE username = #{username}")
    CloudUser findUserByUsername(String username);

    @Insert("INSERT INTO USERS (username, salt, password, firstName, lastName)" +
            "VALUES (#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertUser(CloudUser cloudUser);

}