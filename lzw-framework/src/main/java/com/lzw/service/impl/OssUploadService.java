package com.lzw.service.impl;

import com.google.gson.Gson;
import com.lzw.domain.ResponseResult;
import com.lzw.enums.AppHttpCodeEnum;
import com.lzw.exception.SystemException;
import com.lzw.service.UploadService;
import com.lzw.utils.PathUtils;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;

@Service
@Data
@ConfigurationProperties(prefix = "oss")
@Slf4j
public class OssUploadService implements UploadService {
    @Override
    public ResponseResult uploadImg(MultipartFile img) {
        //判断文件类型
        String filename = img.getOriginalFilename();
        if (!filename.endsWith(".jpg") && !filename.endsWith(".png")) {
            throw new SystemException(AppHttpCodeEnum.FILE_TYPE_ERROR);
        }
        String filePath = PathUtils.generateFilePath(filename);
        //上传到oss
        String url = uploadOSS(img,filePath);
        System.out.println("上传成功");
        log.info("============"+url+"+++++++++++++++");
        return ResponseResult.okResult(url);
    }

    private String accessKey;
    private String secretKey;
    private String bucket;

    /**
     * @param imgFile
     * @param filePath
     * @return
     */
    private String uploadOSS(MultipartFile imgFile, String filePath) {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;

        try {
            InputStream is = imgFile.getInputStream();

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(is, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return "http://rk31xqftu.hn-bkt.clouddn.com/"+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    //ignore
                }
            }
        } catch (Exception ex) {
            //ignore
        }
        return "上传失败";
    }

}
