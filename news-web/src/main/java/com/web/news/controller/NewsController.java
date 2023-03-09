package com.web.news.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class NewsController {

    @Autowired
    private RestTemplate restTemplate;

    public String queryURL = "http://localhost:8899/news-query/";
    public String editURL = "http://localhost:8899/news-edit/";


    @GetMapping("/queryNews")
    public ModelAndView queryNews(){
        List newsList = restTemplate.getForObject(queryURL+"queryNews", List.class);
        List categoryList = restTemplate.getForObject(queryURL+"findAllCategory",List.class);

        ModelAndView mv = new ModelAndView();
        mv.addObject("newsList",newsList);
        mv.addObject("categoryList",categoryList);

        mv.setViewName("index");

        return mv;
    }
}
