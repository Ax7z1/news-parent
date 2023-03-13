package com.web.news.service;

import com.web.news.pojo.News;

public interface NewsEditService {

    /**
     * 添加和编辑
     * @param news
     */
    public void save(News news);

    public void deleteNews(Integer id);
}
