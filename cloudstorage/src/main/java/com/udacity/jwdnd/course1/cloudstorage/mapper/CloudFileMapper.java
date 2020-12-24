package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.CloudFile;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CloudFileMapper {

    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<CloudFile> findAllFilesByUserId(Integer userId);

    @Select("SELECT * FROM FILES WHERE fileid = #{fileId}")
    CloudFile findFileById(@Param("fileId") Integer fileId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{filename}")
    CloudFile findDuplicateFile(Integer userId, String filename);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) " +
            "VALUES (#{filename}, #{contentType}, #{filesize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertFile(CloudFile file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteFileById(Integer fileId);

}