package cn.framework.nacos.controller;

import cn.framework.nacos.config.DBConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 测试
 * @author LiuJingWei
 */
@Slf4j
@RestController
public class TestController {

    @Autowired
    private DBConfig dbConfig;

    @RequestMapping("/getDBUrl")
    public String getDBUrl(){
        log.info("当前配置:" + dbConfig.toString());
        return dbConfig.getUrl();
    }
}
