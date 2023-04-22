package com.example.community.dao;

import com.example.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/21 19:04
 */
@Mapper
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    //@Param用于给参数取别名
    //如果方法只有一个参数并且，在<if>里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);


}
