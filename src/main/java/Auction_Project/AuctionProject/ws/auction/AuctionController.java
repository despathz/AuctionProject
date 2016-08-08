package Auction_Project.AuctionProject.ws.auction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionDisplayResponse;
import Auction_Project.AuctionProject.ws.user.User;


@RestController
@RequestMapping("/ws/auction")
public class AuctionController {
	
	@Autowired
	private AuctionDAO auctionDAO;

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
	public Boolean registerUser( @RequestBody Auction new_auction) {	
		try {
			auctionDAO.save(new_auction);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
}
