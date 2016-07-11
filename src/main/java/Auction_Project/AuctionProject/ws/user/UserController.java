package Auction_Project.AuctionProject.ws.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.UserDAO;;

@RestController
@RequestMapping("/ws/user")
public class UserController {

	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User getByUsernameAndPassword(@RequestBody User input_user){
		User user = new User();
		try {
			user = userDAO.findByUsernameAndPassword(input_user.getUsername(), input_user.getPassword());
		}
		catch (Exception ex){
			user.setUsername("afafafa");
		}
		return user;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean registerUser( @RequestBody User input_user) {
		try {
			userDAO.save(input_user);
		}
		catch (Exception ex) {
			System.out.println("Could not register user.");
			return false;
		}
		return true;
	}
}
