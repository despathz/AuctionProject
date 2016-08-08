package Auction_Project.AuctionProject.ws.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionDisplayResponse;
import Auction_Project.AuctionProject.ws.user.User;


@RestController
@RequestMapping("/ws/auction")
public class AuctionController {
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	@Autowired
	private UserDAO userDAO;

	@RequestMapping(value = "/{auctionID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AuctionDisplayResponse getByID(@PathVariable long auctionID) {
		AuctionDisplayResponse auctionResponse = new AuctionDisplayResponse(0);
		try {
			Auction auction = auctionDAO.findById(auctionID);
			User user = auction.getUser_seller_id();
			auctionResponse = new AuctionDisplayResponse(auction.getId(), auction.getName(), auction.getDescription(), auction.getCurrently(),
					auction.getFirst_bid(), auction.getBuy_price(), auction.getStarted(), auction.getEnds(), user.getUsername(), user.getId());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return auctionResponse;
	}
	
	@RequestMapping(value = "/createAuction", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Boolean registerUser( @RequestBody AuctionDisplayResponse new_auction) {	
		System.out.println("Hello");
		Auction auction = new Auction();
		try {
			auction.setName(new_auction.getName());
			auction.setDescription(new_auction.getDescription());
			auction.setStarted(new_auction.getStarted());
			auction.setEnds(new_auction.getEnds());
			auction.setCurrently(new_auction.getFirst_bid());
			auction.setFirst_bid(new_auction.getFirst_bid());
			auction.setBuy_price(new_auction.getBuy_price());
			User user = userDAO.findById(new_auction.getUser_id());
			auction.setUser_seller_id(user);
			auctionDAO.save(auction);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
}
