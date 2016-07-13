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
			System.out.println(ex.getMessage());
		}
		return user;
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User registerUser( @RequestBody UserPostParams params) {
		User user = new User();
		System.out.println("!!! " + params.getUserExists());

		try {
			user = userDAO.findByUsername(params.getFindUser().getUsername());
			System.out.println("Found the user!!");
		}
		catch (Exception ex){
			System.out.println("caught an error!!");
			System.out.println(ex.getMessage());
		}
		if (params.getUserExists() == false) {
			System.out.println("will store the new user!");
			try {
				userDAO.save(params.getFindUser());
				System.out.println("Saved the user!!");
			}
			catch (Exception ex) {
				System.out.println(ex.getMessage());
			}
		}
		return user;
	}
}
