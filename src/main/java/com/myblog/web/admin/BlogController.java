package com.myblog.web.admin;

import com.myblog.po.Blog;
import com.myblog.po.User;
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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class BlogController {
//    public static  final  String INPUT="/admin/publish";
//    public static  final  String LIST="/admin/manage";
//    public static  final  String REDIRECT_LIST="redirect:/admin/manage";

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @GetMapping("/manage")
    public String Manage(@PageableDefault(size = 5,sort ={"updateTime"},direction = Sort.Direction.DESC)
                                     Pageable pageable,  BlogQuery blogQuery, Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
        return  "/admin/manage";
    }


    @PostMapping("/manage/search")
    public String search(@PageableDefault(size = 5,sort ={"updateTime"},direction = Sort.Direction.DESC) Pageable pageable, BlogQuery blogQuery, Model model){
        model.addAttribute("page",blogService.listBlog(pageable,blogQuery));
    return  "/admin/manage :: blogList";
    }

    @GetMapping("/manage/publish")
    public String input(Model model){
        setTypeAndTag(model);
        model.addAttribute("blog",new Blog());
        return "/admin/publish";
    }

    private void  setTypeAndTag(Model model){
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }

    @GetMapping("/manage/{id}/publish")
    public String editInput(@PathVariable Long id, Model model){
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();
        model.addAttribute("blog",blog);
        return "/admin/publish";
    }


    @PostMapping("/publish")
    public String post(Blog blog, RedirectAttributes attributes, HttpSession session){
        blog.setUser((User) session.getAttribute("user")) ;
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId()==null){
            b=blogService.saveBlog(blog);
        }else {
            b=blogService.updateBlog(blog.getId(),blog);
        }

        if (b==null){
            attributes.addFlashAttribute("message","操作失败");
        }else {
            attributes.addFlashAttribute("message","操作成功");
        }
        return "redirect:/admin/manage";
    }

    @GetMapping("/manage/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes attributes){
        blogService.deleteBlog(id);
        attributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/manage";
    }

    }
