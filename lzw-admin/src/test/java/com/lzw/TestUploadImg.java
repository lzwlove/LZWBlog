package com.lzw;

import com.lzw.domain.ResponseResult;
import com.lzw.service.ArticleTagService;
import com.lzw.service.TagService;
import com.lzw.service.UploadService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;

@SpringBootTest
public class TestUploadImg {

    @Autowired
    private UploadService uploadService;
    @Autowired
    private ArticleTagService articleTagService;

    @Test
    public void Test01(){
        ResponseResult result = uploadService.uploadImg(null);
    }

    @Test
    public void Test02(){
        articleTagService.removeById(99L);
    }

}
