package com.zzh;

import com.zzh.dao.userDao;
import com.zzh.domain.User;
import com.zzh.service.userService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Springboot1ApplicationTests {
	@Autowired
	private userService userService;
	@Test
	void selectAllTest() {
		System.out.println(userService.selectAll());
	}

	@Test
	void insertTest()
	{
		User user = new User();
		user.setAddress("提莫街");
		user.setUserName("提莫");
		user.setPassword("03100310+a");
		user.setEmail("365832435@qq.com");
		user.setNickname("扶她");
		user.setPhone("13219109119");
		userService.insert(user);
	}


	@Test
	void deleteByNameTest()
	{
		System.out.println(userService.deleteById(1621060884816211970L));
	}
}
