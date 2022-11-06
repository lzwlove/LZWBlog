package com.lzw;

import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@SpringBootTest(classes = LZWBlogApplication.class)
@ConfigurationProperties(prefix = "oss")
public class OSSTest {

    private String accessKey;
    private String secretKey;
    private String bucket;

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    @Test
    public void testOss() {
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        cfg.resumableUploadAPIVersion = Configuration.ResumableUploadAPIVersion.V2;// 指定分片上传版本
        //...其他参数参考类注释

        UploadManager uploadManager = new UploadManager(cfg);
        //...生成上传凭证，然后准备上传
        // String accessKey = "JIF46dT5rupTwoQLHIWJPagYwH5AvbJ7Xsh5RamR";
        // String secretKey = "wCQ6D14-XsHisLLX8QgS_Am2VTsCFYqsy5UMgGst";
        // String bucket = "lzw512-blog";

        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = "2022/lzw.png";

        try {
            // byte[] uploadBytes = "hello qiniu cloud".getBytes("utf-8");
            // ByteArrayInputStream byteInputStream = new ByteArrayInputStream(uploadBytes);

            InputStream is=new FileInputStream("C:\\Users\\lzw\\Desktop\\命令.png");

            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);

            try {
                Response response = uploadManager.put(is, key, upToken, null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
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

    }
    // http://rk31xqftu.hn-bkt.clouddn.com/2022/lzw.png
}
