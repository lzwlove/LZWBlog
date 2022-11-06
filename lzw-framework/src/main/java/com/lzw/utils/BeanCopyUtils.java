package com.lzw.utils;

import com.lzw.domain.entity.Article;
import com.lzw.domain.vo.HotArticleVo;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BeanCopyUtils {

    private BeanCopyUtils() {
    }

    public static <V> V copyBean(Object source,Class<V> clazz) {
        //创建目标对象
        V result = null;
        try {
            result = clazz.newInstance();
            //实现属性copy
            BeanUtils.copyProperties(source, result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //返回结果
        return result;
    }
    public static <O,V> List<V> copyBeanList(List<O> list,Class<V> clazz){
        return list.stream()
                .map(o -> copyBean(o, clazz))
                .collect(Collectors.toList());
    }

    /*
        public static List copyBeanList(List source,Class clazz){
            Object result=null;
            List list=null;
            try {
                // result = clazz.newInstance();
                list=new ArrayList();
                for (Object o : source) {
                    result = clazz.newInstance();//就是这里！！！
                    BeanUtils.copyProperties(o,result);
                    list.add(result);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return list;
        }
    */

    // public static void main(String[] args) {
    //     Article article1 = new Article();
    //     article1.setId(12L);
    //     article1.setViewCount(1000L);
    //     Article article2 = new Article();
    //     article2.setId(8L);
    //     article2.setViewCount(56L);
    //     List list=new ArrayList();
    //     list.add(article1);
    //     list.add(article2);
    //     List list1 = copyBeanList(list, HotArticleVo.class);
    //     list1.forEach(System.out::println);
    // }
}
