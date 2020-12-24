package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudCredential;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CloudCredentialMapper {

    @Select("SELECT * FROM CREDENTIALS WHERE userid = #{userId}")
    List<CloudCredential> findAllCredentialsByUserId(Integer userId);

    @Select("SELECT * FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    CloudCredential findCredentialById(Integer credentialId);

    @Insert("INSERT INTO CREDENTIALS (url, username, userkey, password, userid) " +
            "VALUES (#{url}, #{username}, #{userKey}, #{password}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "credentialId")
    int insertCredential(CloudCredential credential);

    @Delete("DELETE FROM CREDENTIALS WHERE credentialid = #{credentialId}")
    int deleteCredentialById(Integer credentialId);

    @Update("UPDATE CREDENTIALS SET url = #{url}, username = #{username}, userkey = #{userKey}, password = #{password} " +
            "WHERE credentialid = #{credentialId}")
    int updateCredential(CloudCredential credential);

}