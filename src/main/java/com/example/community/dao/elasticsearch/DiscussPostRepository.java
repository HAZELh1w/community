package com.example.community.dao.elasticsearch;

import com.example.community.entity.DiscussPost;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author hazel
 * @description 功能描述
 * @create 2023/5/13 21:58
 */
@Repository
public interface DiscussPostRepository extends ElasticsearchRepository<DiscussPost,Integer> {

}
