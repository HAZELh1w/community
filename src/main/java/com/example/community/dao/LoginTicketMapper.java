package com.example.community.dao;

import com.example.community.entity.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/25 15:24
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {

    @Insert({
            "insert into login_ticket (user_id,ticket,status,expired) ",
            "values(#{userId},#{ticket},#{status},#{expired})"
    })
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insertLoginTicket(LoginTicket loginTicket);

    @Select({
            "select id,user_id,ticket,status,expired ",
            "from login_ticket where ticket=#{ticket}"
    })
    LoginTicket selectByTicket(String ticket);

//    顺带演示基于注解的动态sql(可以去掉)
    @Update({
            "<script>",
            "update login_ticket set status=#{status} where ticket=#{ticket}",
            "<if test=\"ticket!=null\">",
            "and 1=1",
            "</if>",
            "</script>"
    })
    int updateStatus(String ticket,int status);
}
