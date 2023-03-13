package com.web.news.service.impl;

import com.web.news.mapper.NewsMapper;
import com.web.news.pojo.News;
import com.web.news.service.NewsEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Date;

@Service
public class NewsEditServiceImpl implements NewsEditService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public void save(News news) {
        //判断 news 是否有id 来判断是添加还是编辑。添加没有id 编辑有id
        if (ObjectUtils.isEmpty(news.getId())){
            news.setCreatedate(new Date());
            newsMapper.insert(news);
        }else{
            newsMapper.updateByPrimaryKey(news);
        }

    }

    @Override
    public void deleteNews(Integer id) {
        newsMapper.deleteByPrimaryKey(id);
    }
}
