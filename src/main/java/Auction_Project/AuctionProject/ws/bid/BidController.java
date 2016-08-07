package Auction_Project.AuctionProject.ws.bid;



import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dao.BidDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.bid.BidListResponse;
import Auction_Project.AuctionProject.dto.bid.NewBidResponse;
import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.user.User;

@RestController
@RequestMapping("/ws/bid")
public class BidController {
	
	@Autowired
	private BidDAO bidDAO;
	@Autowired
	private UserDAO userDAO;
	@Autowired
	private AuctionDAO auctionDAO;
	
	@RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean addBid(@RequestBody NewBidResponse input_bid) {
		Bid bid = new Bid();
		bid.setAmount(input_bid.getAmount());
		bid.setBid_time(new Date());
		try {
			User user = userDAO.findById(input_bid.getBidder());
			Auction auction = auctionDAO.findById(input_bid.getAuction());
			bid.setBidder(user);
			bid.setAuctionId(auction);
			bidDAO.save(bid);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "/forAuction/{auctionID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<BidListResponse> getBids(@PathVariable long auctionID) {
		List<BidListResponse> responseList = new ArrayList<BidListResponse>();
		List<Bid> bidList = new ArrayList<Bid>();
		try {
			Auction auction = auctionDAO.findById(auctionID);
			Pageable top = new PageRequest(0, 1);
			bidList = bidDAO.findByAuctionIdOrderByAmountDesc(auction, top);
			for (Iterator<Bid> iterator = bidList.iterator(); iterator.hasNext();) {
				Bid bid = iterator.next();
				User user = bid.getBidder();
				BidListResponse bidResponse = new BidListResponse(user.getId(), bid.getId(), user.getUsername(), bid.getBid_time(), bid.getAmount());
				responseList.add(bidResponse);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return responseList;
	}

}
