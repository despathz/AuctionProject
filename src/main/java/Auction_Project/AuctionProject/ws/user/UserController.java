package Auction_Project.AuctionProject.ws.user;

import java.util.ArrayList;
import java.util.List;

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
	
	@RequestMapping(value = "/register/checkUsername", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User registerCheckUsername( @RequestBody User input_user) {
		User user = new User();
		try {
			user = userDAO.findByUsername(input_user.getUsername());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return user;
	}
	
	@RequestMapping(value = "/register/checkEmail", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public User registerCheckEmail( @RequestBody User input_user) {
		User user = new User();
		try {
			user = userDAO.findByEmail(input_user.getEmail());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return user;
	}		
		
	@RequestMapping(value = "/register", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean registerUser( @RequestBody User input_user) {	
		try {
			userDAO.save(input_user);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getUsers() {
		List<User> userList = new ArrayList<User>();
		try {
			userList = userDAO.findBySuperuser(false);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return userList;
	}
	
}
