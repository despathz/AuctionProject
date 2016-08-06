package Auction_Project.AuctionProject.dao;

import javax.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import Auction_Project.AuctionProject.ws.bid.Bid;

@Transactional
public interface BidDAO extends CrudRepository<Bid, Long>{

}
