package com.example.community.dao;

import com.example.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/30 14:09
 */
@Mapper
public interface CommentMapper {
    List<Comment> selectCommentsByEntity(int entityType,int entityId,int offset,int limit);

    int selectCountByEntity(int entityType,int entityId);

    int insertComment(Comment comment);

    Comment selectCommentById(int id);
}
