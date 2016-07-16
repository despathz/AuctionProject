package Auction_Project.AuctionProject.dao;

import java.util.List;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import Auction_Project.AuctionProject.ws.user.User;



@Transactional
public interface UserDAO extends CrudRepository<User, Long>{

	public User findByUsernameAndPassword(String username, String password);
	public User findById(long id);
	public User findByUsername(String username);
	public User findByEmail(String email);
	public List<User> findBySuperuser(boolean superuser);
}
