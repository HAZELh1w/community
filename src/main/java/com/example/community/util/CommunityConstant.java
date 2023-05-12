package com.example.community.util;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/24 20:48
 */
public interface CommunityConstant {
    int ACTIVATION_SUCCESS = 0;
    int ACTIVATION_REPEAT = 1;
    int ACTIVATION_FAILURE = 2;

    int DEFAULT_EXPIRED_SECONDS = 3600 * 12;

    int REMEMBER_EXPIRED_SECONDS = 3600 * 24 * 100;

    //实体类型
    int ENTITY_TYPE_POST = 1;
    int ENTITY_TYPE_COMMENT = 2;
    int ENTITY_TYPE_USER = 3;

    //主题
    String TOPIC_COMMENT = "comment";
    String TOPIC_LIKE = "like";
    String TOPIC_FOLLOW = "follow";

    //系统用户ID
    int SYSTEM_USER_ID = 1;

}
