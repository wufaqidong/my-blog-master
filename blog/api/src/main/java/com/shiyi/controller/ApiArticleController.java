package com.shiyi.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.shiyi.annotation.AccessLimit;
import com.shiyi.annotation.BusinessLogger;
import com.shiyi.common.ResponseResult;
import com.shiyi.dto.ArticleInsertDTO;
import com.shiyi.service.ApiArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * @author blue
 * @Description: 博客前台博客文章控制器
 * @Date 2021-07-25 19:04
 */
@RestController
@RequestMapping("/v1/article")
@Api(tags = "文章API-V1")
@RequiredArgsConstructor
public class ApiArticleController {

    private final ApiArticleService articleService;

    @BusinessLogger(value = "首页-用户访问首页",type = "查询",desc = "查询所有文章")
    @GetMapping(value = "/")
    @ApiOperation(value = "文章列表", httpMethod = "GET", response = ResponseResult.class, notes = "文章列表")
    public ResponseResult selectArticleList(Integer categoryId,Integer tagId,String orderByDescColumn) {
        return  articleService.selectArticleList(categoryId,tagId,orderByDescColumn);
    }

    @BusinessLogger(value = "门户-用户查看文章详情",type = "查询",desc = "查看文章详情")
    @GetMapping(value = "/info")
    @ApiOperation(value = "文章详情", httpMethod = "GET", response = ResponseResult.class, notes = "文章详情")
    public ResponseResult selectArticleInfo(Integer id) {
        return articleService.selectArticleInfo(id);
    }

    @GetMapping(value = "/selectUserInfoByArticleId")
    @ApiOperation(value = "根据文章id获取作者信息", httpMethod = "GET", response = ResponseResult.class, notes = "根据文章id获取作者信息")
    public ResponseResult selectUserInfoByArticleId(Integer id) {
        return articleService.selectUserInfoByArticleId(id);
    }

    @GetMapping(value = "/search")
    @ApiOperation(value = "用户搜索文章", httpMethod = "GET", response = ResponseResult.class, notes = "用户搜索文章")
    public ResponseResult searchArticle(String keyword) {
        return articleService.searchArticle(keyword);
    }

    @BusinessLogger(value = "首页-归档",type = "查询",desc = "归档")
    @GetMapping(value = "/archive")
    @ApiOperation(value = "归档", httpMethod = "GET", response = ResponseResult.class, notes = "归档")
    public ResponseResult archive() {
        return  articleService.archive();
    }


    @AccessLimit
    @BusinessLogger(value = "门户-文章点赞",type = "查询",desc = "文章点赞")
    @GetMapping(value = "/like")
    @ApiOperation(value = "文章点赞", httpMethod = "GET", response = ResponseResult.class, notes = "文章点赞")
    public ResponseResult articleLike(Integer articleId) {
        return articleService.articleLike(articleId);
    }

    @BusinessLogger(value = "文章详情-校验公众号验证码",type = "查询",desc = "校验公众号验证码")
    @GetMapping(value = "/checkCode")
    @ApiOperation(value = "校验公众号验证码", httpMethod = "GET", response = ResponseResult.class, notes = "校验公众号验证码")
    public ResponseResult checkCode(String code) {
        return articleService.checkCode(code);
    }

    @SaCheckLogin
    @PostMapping(value = "/")
    @BusinessLogger(value = "添加文章",type = "添加",desc = "添加文章")
    @ApiOperation(value = "添加文章", httpMethod = "POST", response = ResponseResult.class, notes = "添加文章")
    public ResponseResult insertArticle(@RequestBody ArticleInsertDTO dto) {
        return articleService.insertArticle(dto);
    }

    @SaCheckLogin
    @PutMapping(value = "/")
    @BusinessLogger(value = "修改我的文章",type = "修改",desc = "修改我的文章")
    @ApiOperation(value = "修改我的文章", httpMethod = "PUT", response = ResponseResult.class, notes = "修改我的文章")
    public ResponseResult updateMyArticle(@RequestBody ArticleInsertDTO dto) {
        return articleService.updateMyArticle(dto);
    }

    @SaCheckLogin
    @GetMapping(value = "/selectArticleByUserId")
    @BusinessLogger(value = "根据用户id获取文章",type = "查询",desc = "根据用户id获取文章")
    @ApiOperation(value = "根据用户id获取文章", httpMethod = "GET", response = ResponseResult.class, notes = "根据用户id获取文章")
    public ResponseResult selectArticleByUserId(String userId,Integer type) {
        return articleService.selectArticleByUserId(userId,type);
    }

    @SaCheckLogin
    @DeleteMapping(value = "/")
    @BusinessLogger(value = "删除我的文章",type = "删除",desc = "删除我的文章")
    @ApiOperation(value = "删除我的文章", httpMethod = "DELETE", response = ResponseResult.class, notes = "删除我的文章")
    public ResponseResult deleteMyArticle(Long id) {
        return articleService.deleteMyArticle(id);
    }

    @SaCheckLogin
    @GetMapping(value = "/selectMyArticleInfo")
    @BusinessLogger(value = "我的文章详情",type = "查询",desc = "我的文章详情")
    @ApiOperation(value = "我的文章详情", httpMethod = "GET", response = ResponseResult.class, notes = "我的文章详情")
    public ResponseResult selectMyArticleInfo(Long id) {
        return articleService.selectMyArticleInfo(id);
    }

}
