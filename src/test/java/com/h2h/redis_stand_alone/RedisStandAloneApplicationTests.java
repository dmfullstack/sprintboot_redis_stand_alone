package com.h2h.redis_stand_alone;

import com.h2h.redis_stand_alone.entity.PrintParam;
import com.h2h.redis_stand_alone.kit.RedisKit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisStandAloneApplicationTests {

	@Autowired
	private RedisKit redisKit;

	@Test
	/**
	 *  保存
	 */
	public void setString() {
		try {
			PrintParam printParam = new PrintParam();
			printParam.setUserId(1L);
			printParam.setMchId(2L);
			printParam.setStart("2012-01-01");
			printParam.setEnd("2016-01-02");
			redisKit.setCacheObject("printParam",printParam,-1);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	/**
	 *  保存
	 */
	public void getString() {
		try {
			Object printParam = redisKit.getCacheObject("printParam");
			System.out.println(printParam);
		}catch (Exception e){
			e.printStackTrace();
		}

	}


	@Test
	/**
	 * 位图操作 保存
	 */
	public void setBit() {
		try {
			//返回的是替换掉的 value
			boolean setbit = redisKit.setbit("bit:test", 123, true);
		}catch (Exception e){
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * 位图操作 查询
	 */
	public void getBit() {
		try {
			boolean getbit = redisKit.getbit("bit:test", 123);
			System.out.println("查询结果为："+getbit);
		}catch (Exception e){
			System.out.println("redis抽风，查询失败!");
			e.printStackTrace();
		}

	}

	@Test
	/**
	 * 位图操作 计算给定字符串中，被设置为 1 的比特位的数量
	 */
	public void bitcount() {
		try {
			long count = redisKit.bitCount("ccc");
			System.out.println("查询结果为："+count);
		}catch (Exception e){
			System.out.println("redis抽风，查询失败!");
			e.printStackTrace();
		}

	}

}
