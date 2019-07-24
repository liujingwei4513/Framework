package cn.framework.nacos.config;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import lombok.Getter;
import lombok.ToString;
import org.springframework.stereotype.Component;

/**
 * 数据库配置类
 * @author LiuJingWei
 */
@Getter
@ToString
@Component
@NacosPropertySource(dataId = "database",groupId = "DEFAULT_GROUP",autoRefreshed = true)
public class DBConfig {

    @NacosValue(value = "${driver_class_name}", autoRefreshed = true)
    private String driverClassName;

    @NacosValue(value = "${url}", autoRefreshed = true)
    private String url;

    @NacosValue(value = "${user_name}", autoRefreshed = true)
    private String username;

    @NacosValue(value = "${password}", autoRefreshed = true)
    private String password;
}
