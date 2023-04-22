package com.example.community.dao;

import org.springframework.stereotype.Repository;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/4/18 21:19
 */
@Repository("alphaHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
