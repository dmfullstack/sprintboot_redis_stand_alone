package com.h2h.redis_stand_alone;

import com.h2h.redis_stand_alone.kit.RedisKit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RedisStandAloneApplication {

	public static void main(String[] args) {
		SpringApplication.run(RedisStandAloneApplication.class, args);
	}

//	@Bean
//	public RedisKit getRedisKit(){
//		return new RedisKit();
//	}
}
