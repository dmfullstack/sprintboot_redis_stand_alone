package com.h2h.redis_stand_alone;

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
	 * 位图操作 保存
	 */
	public void setBit() {
		try {
			boolean setbit = redisKit.setbit("bit:test", 123, true);
			if(setbit){
				System.out.println("redis位图数据存储成功！");
			}else{
				System.out.println("redis抽风，数据保存失败！");
			}
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
