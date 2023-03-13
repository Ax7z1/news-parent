package com.web.news.service.Controller;

import com.web.news.pojo.News;
import com.web.news.pojo.RespBean;
import com.web.news.service.NewsEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class NewsEditController {

    @Autowired
    private NewsEditService newsEditService;

    @PostMapping("/saveNews")
    public RespBean saveNews(@RequestBody News news){
        System.out.println(news.getId());
        newsEditService.save(news);
        return RespBean.ok("保存成功");
    }

    @DeleteMapping("/deleteNews/{id}")
    public RespBean deleteNews(@PathVariable Integer id){
        newsEditService.deleteNews(id);
        return RespBean.ok("删除成功");
    }
}
