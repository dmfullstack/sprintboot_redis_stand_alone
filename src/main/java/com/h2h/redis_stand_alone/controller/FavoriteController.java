package com.h2h.redis_stand_alone.controller;

import com.h2h.redis_stand_alone.kit.RedisKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/redis")
public class FavoriteController{
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoriteController.class);

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
	private RedisKit redisKit;

	@RequestMapping("/set")
	public Object set() {
		ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
		valueOperations.set("a","123");
		return 200;
	}

	@GetMapping("/get/{key}")
	public Object get(@PathVariable String key) {
		ValueOperations<String,Object> valueOperations = redisTemplate.opsForValue();
		String s = valueOperations.get(key).toString();
		return s;
	}

	@PostMapping("/bit")
	public Object setBit(@RequestBody Map map){
		try {
			String key = map.get("key").toString();
			Long offset = Long.valueOf(map.get("offset").toString());
			Boolean value = (Boolean)map.get("value");
			boolean setbit = redisKit.setbit(key, offset, value);
			if(setbit){
				return new ResponseEntity(HttpStatus.OK);
			}
		}catch (Exception e){
			e.printStackTrace();
		}

		return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@GetMapping("{key}/{offset}")
	public Object getBit(@PathVariable String key,@PathVariable Long offset){
		try {
			boolean value = redisKit.getbit(key, offset);
			return new ResponseEntity(value,HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}