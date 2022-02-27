package com.myblog.web;

import com.myblog.service.BlogService;
import com.myblog.service.TagService;
import com.myblog.service.TypeService;
import com.myblog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class IndexController {
        @Autowired
        private BlogService blogService;

        @Autowired
        private TypeService typeService;

        @Autowired
        private TagService tagService;

        @GetMapping("/")
        public String index(@PageableDefault(size = 8,sort ={"updateTime"},direction = Sort.Direction.DESC)
                                            Pageable pageable, Model model){
            model.addAttribute("page",blogService.listBlog(pageable));
            model.addAttribute("types",typeService.listTypeTop(6));//分类数量
            model.addAttribute("tags",tagService.listTagTop(10));//分类数量
            model.addAttribute("RecommendBlogs",blogService.listRecommendBlogTop(8));
            return  "index";
        }

        @GetMapping("/search")
        public String search(@PageableDefault(size = 8,sort ={"updateTime"},direction = Sort.Direction.DESC)
                                             Pageable pageable, @RequestParam String query, Model model){
//           存在sql注入漏洞
            model.addAttribute("page",blogService.listBlog("%"+query+"%",pageable));
            model.addAttribute("query",query);

            return "search";
        }

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id, Model model){
            model.addAttribute("blog",blogService.getBlog(id));
            return  "blog";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model){
            model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
            return "_fragments :: newblogList";
    }

}
