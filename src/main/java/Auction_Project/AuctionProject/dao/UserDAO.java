package Auction_Project.AuctionProject.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.user.User;



@Transactional
public interface UserDAO extends CrudRepository<User, Long>{

	public User findByUsernameAndPassword(String username, String password);
	public User findByUsername(String username);
	
	@Query("select u from User u where u.superuser = false")
	public  List<User> getUsers();
}
