package Auction_Project.AuctionProject.dao;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.user.User;

@Transactional
public interface AuctionDAO extends CrudRepository<Auction, Long>{
	
	public Auction findById(long id);
	
	@Query(value = "SELECT * FROM Auction a INNER JOIN auction_category c WHERE a.id = c.auction_id AND c.category_id = ?4 AND (a.currently BETWEEN ?1 AND ?2 OR a.buy_price BETWEEN ?1 AND ?2) AND MATCH(a.description) AGAINST(?3)",
	nativeQuery = true)
	public List<Auction> searchAuctions(float min, float max, String keywords, long category);
	
	public List<Auction> findBySeller(User user_seller_id);
	public Long countByIdAndStarted(long id, Date started);
}
