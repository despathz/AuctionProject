package Auction_Project.AuctionProject.dao;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.category.Category;
import Auction_Project.AuctionProject.ws.user.User;

@Transactional
public interface AuctionDAO extends CrudRepository<Auction, Long>{
	
	public Auction findById(long id);
	
	@Query("SELECT c FROM Auction a INNER JOIN a.categories c WHERE a.id = ?1")
	public List<Category> findCategories(long id);
	
	public List<Auction> findBySeller(User user_seller_id);
	public Long countByIdAndStarted(long id, Date started);
}
