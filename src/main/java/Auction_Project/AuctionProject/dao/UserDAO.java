package Auction_Project.AuctionProject.dao;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import Auction_Project.AuctionProject.ws.user.User;



@Transactional
public interface UserDAO extends CrudRepository<User, Long>{

	public User findByUsernameAndPassword(String username, String password);
}
