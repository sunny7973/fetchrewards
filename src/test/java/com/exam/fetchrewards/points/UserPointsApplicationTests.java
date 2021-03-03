package com.exam.fetchrewards.points;

import com.exam.fetchrewards.points.model.UserRewards;
import com.exam.fetchrewards.points.repo.UserRepo;
import com.exam.fetchrewards.points.service.PointServiceV2;
import org.junit.Assert;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@AutoConfigureMockMvc
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
class UserPointsApplicationTests {



	@Autowired
	UserRepo repo;
	@Autowired
	PointServiceV2 service;

	@Order(1)
	@Test
	public void saveReadTest(){

		UserRewards entry = repo.save(new UserRewards( "Prasad", 100, new Date()));
		Optional<UserRewards> expected = repo.findById(entry.getId());
		Assert.assertEquals(entry.getPayerName(), expected.get().getPayerName());
		Assert.assertEquals(entry.getPoints(), expected.get().getPoints());
	}

	@Order(2)
	@Test
	public void deductPointsTest(){
		UserRewards entry1 = repo.save(new UserRewards("DANNON", 300,  new Date()));
		UserRewards entry2 = repo.save(new UserRewards("UNILEVER", 200, new Date()));
		UserRewards entry3 = repo.save(new UserRewards( "DANNON", -200, new Date()));
		UserRewards entry5 = repo.save(new UserRewards( "MILLER COORS", 10000, new Date()));
		UserRewards entry6 = repo.save(new UserRewards( "DANNON", 1000, new Date()));

		List<UserRewards> test = service.deductPoints(5000);
		for(UserRewards reward : test){
			System.out.print("name : "+reward.getPayerName());
			System.out.println(" points: "+reward.getPointsredeemed());
		}
		Assert.assertEquals("","");
		balancePointsTest();
	}

	//@Order(3)
	//@Test
	public void balancePointsTest(){
		//deductPointsTest();
		List<UserRewards> testi = repo.findAll();
		System.out.println("*************************************");
		Map<String, Integer> testing = testi.stream().collect(Collectors.groupingBy(r -> r.getPayerName(), Collectors.summingInt(r -> r.getPoints() - Math.abs(r.getPointsredeemed()))));

		for(Map.Entry reward : testing.entrySet()){
			System.out.print("name : "+reward.getKey());
			System.out.println(" points: "+(reward.getValue()));
		}
		Assert.assertEquals("","");
	}

}
