package com.web.news.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.web.news.pojo.Category;
import com.web.news.pojo.News;
import com.web.news.pojo.PageReq;
import com.web.news.pojo.PageResp;
import com.web.news.service.NewsQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NewsQueryController {

    @Autowired
    private NewsQueryService newsQueryService;

    @GetMapping("/queryNews")
    public List<News> queryNews(){

        return newsQueryService.findAllNews();
    }
    @GetMapping("/findAllCategory")
    public List<Category> findAllCategory(){
        return newsQueryService.findAllCategory();
    }

    @GetMapping("/listByPage")
    public PageResp<News> listByPage(PageReq pageReq){

        PageHelper.startPage(pageReq.getPage(),pageReq.getSize()); // 分页拦截

        Page<News> page = (Page<News>) newsQueryService.findAllNews();
        PageResp pageResp = new PageResp();
        pageResp.setTotal(page.getTotal());
        pageResp.setList(page.getResult());

        return pageResp;
    }

}
