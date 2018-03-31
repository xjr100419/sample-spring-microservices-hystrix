package pl.piomin.microservices.gps.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.skywalking.apm.toolkit.trace.ActiveSpan;
import org.skywalking.apm.toolkit.trace.Trace;
import org.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pl.piomin.microservices.gps.model.Account;

@RestController
public class Api {

	@Value("${PORT}")
	int port;
	
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
	@Trace(operationName="findByNumber")
	public Account findByNumber(@PathVariable("number")String number, @RequestBody Map map) {
//		String number = "";
		logger.info(String.format("Account.findByNumber(%s)", number));

		System.out.println("####  RequestBody #######///////////#### map ####"+map);

		String tracId = TraceContext.traceId();
		System.out.println("####  findByCustomer #######///////////#### tracId ####"+tracId);

		ActiveSpan.tag("service","findByCustomer");
		ActiveSpan.tag("orderid",number);


		return accounts.stream().filter(it -> it.getNumber().equals(number)).findFirst().get();
	}
	
	@RequestMapping("/accounts/customer/{customer}")
	public List<Account> findByCustomer(@PathVariable("customer") Integer customerId) {


		String tracId = TraceContext.traceId();
		System.out.println("####  findByCustomer #######///////////#### tracId ####"+tracId);

		ActiveSpan.tag("service","findByCustomer");

		logger.info(String.format("Account.findByCustomer(%s)", customerId));
		if (port%2 == 0) {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return accounts.stream().filter(it -> it.getCustomerId().intValue()==customerId.intValue()).collect(Collectors.toList());
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
