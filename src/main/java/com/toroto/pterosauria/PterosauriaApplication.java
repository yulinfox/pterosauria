package com.toroto.pterosauria;

import com.toroto.pterosauria.utils.SpringFactoryUtil;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Toroto
 */
@SpringBootApplication(scanBasePackages = {"com.toroto.pterosauria"})
@MapperScan(basePackages = {"com.toroto.pterosauria.domain.db"}, annotationClass = Mapper.class)
public class PterosauriaApplication {

	public static void main(String[] args) {
		SpringFactoryUtil.setContext(SpringApplication.run(PterosauriaApplication.class, args));
	}

}
