package pl.piomin.microservices.gps.api;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.gps.model.Account;
import pl.piomin.microservices.gps.model.Order_track;
import pl.piomin.microservices.gps.util.MongodbUtil;
import pl.piomin.microservices.gps.util.RedisUtil;

@RestController
public class Api {

	@Autowired
	private RedisUtil redisUtil;

	@Autowired
	private MongodbUtil mongodbUtil;

	private List<Account> accounts;
	
	protected Logger logger = Logger.getLogger(Api.class.getName());
	
	public Api() {
		accounts = new ArrayList<>();
		accounts.add(new Account(1, 1, "111111"));
		accounts.add(new Account(2, 2, "222222"));
		accounts.add(new Account(3, 3, "333333"));
		accounts.add(new Account(4, 4, "444444"));
		accounts.add(new Account(5, 1, "555555"));
		accounts.add(new Account(6, 2, "666666"));
		accounts.add(new Account(7, 2, "777777"));
	}
	
	@RequestMapping("/accounts/{number}")
	public Account findByNumber(@PathVariable("number")String number, @RequestBody Map map) {
//		String number = "";
		logger.info(String.format("Account.findByNumber(%s)", number));

		System.out.println("####  RequestBody #######///////////#### map ####"+map);




		return accounts.stream().filter(it -> it.getNumber().equals(number)).findFirst().get();
	}
	
	@RequestMapping("/accounts/customer/{customer}")
	public List<Account> findByCustomer(@PathVariable("customer") Integer customerId) {




		logger.info(String.format("Account.findByCustomer(%s)", customerId));
		return accounts.stream().filter(it -> it.getCustomerId().intValue()==customerId.intValue()).collect(Collectors.toList());
	}

	@RequestMapping("/redis")
	public List<Account> redis() {
		redisUtil.set("track","update");

		System.out.println(redisUtil.get("track"));
		return accounts;
	}

	@RequestMapping("/mongodb")
	public Map mongodb() {
		Map map = new HashMap();
		/*map.put("track","asdf");
		mongodbUtil.add(map,"track");*/

		Order_track order_track = new Order_track();
		order_track.setOrder_id("1");
		order_track.setLat(112.33f);
		order_track.setLon(65.22f);
		order_track.setTime(new Date().toString());
		order_track.setPoint("start");

		mongodbUtil.addTrack(order_track,"track");


		return map;
	}

	@RequestMapping("/mongodb/list")
	public List<Order_track> mongodblist() {
		return mongodbUtil.queryTrack("1","track");
	}

	@RequestMapping("/accounts")
	public List<Account> findAll() {
		logger.info("Account.findAll()");
		return accounts;
	}

	public static void main(String[] args) {
		String operationName = "/eureka/apps/ACCOUNT-SERVICE/192.168.204.46:account-service:2225";
		String tmep = ".jpg,.jpeg,.js,.css,.png,.bmp,.gif,.ico,.mp3,.mp4,.html,.svg,/eureka/apps";

		int suffixIdx2 = operationName.lastIndexOf("/");
		if (suffixIdx2 > -1 && tmep.contains(operationName.substring(0,suffixIdx2))){
			System.out.println("true");
		}

	}
}
