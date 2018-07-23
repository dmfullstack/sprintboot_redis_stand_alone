package com.h2h.redis_stand_alone.controller;

import com.h2h.redis_stand_alone.kit.RedisKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/redis")
public class FavoriteController{

    @Autowired
	private RedisKit redisKit;

	@PostMapping("/set")
	public ResponseEntity set(@RequestBody Map map) {
		String key = map.get("key").toString();
		Object value = map.get("value");
		redisKit.setCacheObject(key,value,-1);
		return new ResponseEntity(HttpStatus.OK);
	}

	@GetMapping("/get/{key}")
	public ResponseEntity get(@PathVariable String key) {
		return new ResponseEntity(redisKit.getCacheObject(key),HttpStatus.OK);
	}

	@PostMapping("/bit")
	public ResponseEntity setBit(@RequestBody Map map){
		try {
			String key = map.get("key").toString();
			Long offset = Long.valueOf(map.get("offset").toString());
			Boolean value = (Boolean)map.get("value");
			boolean setbit = redisKit.setbit(key, offset, value);
			return new ResponseEntity(HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("{key}/{offset}")
	public Object getBit(@PathVariable String key,@PathVariable Long offset){
		try {
			boolean value = redisKit.getbit(key, offset);
			return new ResponseEntity(value,HttpStatus.OK);
		}catch (Exception e){
			e.printStackTrace();
			return new ResponseEntity(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}