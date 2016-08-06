package Auction_Project.AuctionProject.dao;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import Auction_Project.AuctionProject.ws.auction.Auction;

@Transactional
public interface AuctionDAO extends CrudRepository<Auction, Long>{
	
	public Auction findById(long id);
}
