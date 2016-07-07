package Auction_Project.AuctionProject.ws.user;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

	@RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public User getUser() {
		User user = new User();
		user.setUsername("dimitris");
		user.setPassword("pass123");
		System.out.println("works");
		return user;
	}
}
