package com.bitc.full505_team2_project;

import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Full505Team2ProjectApplication {

	public static void main(String[] args) {

		SpringApplication.run(Full505Team2ProjectApplication.class, args)
				.getBean(Full505Team2ProjectApplication.class).test();

	}

	public void test() {
		//세션 시작
		ChromeOptions options = new ChromeOptions();
		//페이지가 로드될 때까지 대기
		//Normal: 로드 이벤트 실행이 반환 될 때 까지 기다린다.
		options.setPageLoadStrategy(PageLoadStrategy.NORMAL);

		WebDriver driver = new ChromeDriver(options);
		//driver1, 상세보기 페이지 때문에 하나 더 만듦
		WebDriver driver1 = new ChromeDriver(options);
	}
}
