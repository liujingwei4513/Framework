package cn.framework.spring.properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.Set;


/**
 * 扩展properties文件解析功能
 * @remark：对数据库的password的密文，进行解密处理
 * @author: luojun
 * @email: luojun@cardoor.cn
 * @date: 2016年6月28日
 * @version v1.0.0
 */
public class EncryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private Set<String> encryptedProps = Collections.emptySet();

	public void setEncryptedProps(Set<String> encryptedProps) {
		this.encryptedProps = encryptedProps;
	}

	@Override
	protected String convertProperty(String propertyName, String propertyValue) {
		// 将处理过的值传给父类继续处理
		return super.convertProperty(propertyName, propertyValue);
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		// 正常执行属性文件加载
		super.postProcessBeanFactory(beanFactory);
	}

	protected Resource[] locations;

	@Override
	public void setLocation(Resource location) {
		// 由于location是父类私有，所以需要记录到本类的locations中
		super.setLocation(location);
		this.locations = new Resource[] { location };
	}
}