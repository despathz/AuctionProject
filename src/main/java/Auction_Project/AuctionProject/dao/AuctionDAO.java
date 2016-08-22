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
	
	@Query(value = "SELECT * FROM Auction a WHERE (a.currently BETWEEN ?1 AND ?2 OR a.buy_price BETWEEN ?1 AND ?2) AND MATCH(a.description, a.name) AGAINST('comic' IN BOOLEAN MODE)",
	nativeQuery = true)
	public List<Auction> searchAuctions(float min, float max);
	
	public List<Auction> findBySeller(User user_seller_id);
	public Long countByIdAndStarted(long id, Date started);
}
