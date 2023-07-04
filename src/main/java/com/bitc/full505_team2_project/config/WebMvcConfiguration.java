package com.bitc.full505_team2_project.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

/* @Configuration : 스프링 프레임워크에 해당 파일이 설정 파일임을 알려주는 어노테이션
* WebMvcConfigurer : 스프링 프레임워크에서 사용하는 설정 정보가 있는 인터페이스, 사용자 설정이 필요할 경우 해당 인터페이스를 상속 받아 수정
* */
@Configuration
public class WebMvcConfiguration {
  @Bean // 최신 방식 3.0 (Spring 6)
  public MultipartResolver multipartResolver(){
    return new StandardServletMultipartResolver();
  }

  @Bean
  public MultipartConfigElement multipartConfigElement(){
    MultipartConfigFactory factory = new MultipartConfigFactory();
    // 업로드 파일의 크기 설정
    factory.setMaxRequestSize(DataSize.ofBytes(10 * 1024 *1024));
    factory.setMaxFileSize(DataSize.ofBytes(10 * 1024 * 1024));

    return factory.createMultipartConfig();
  }

}
