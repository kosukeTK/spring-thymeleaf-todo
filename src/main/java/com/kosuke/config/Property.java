package com.kosuke.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
//@Configuration
//@PropertySource(value = {"classpath:application.properties"})
//@ConfigurationProperties(prefix = "spring.datasource")
public class Property {
	
	@Value("${base.dir:C:\\Users\\torit\\OneDrive\\デスクトップ}")
	private String baseDir;

	public String getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}

}
