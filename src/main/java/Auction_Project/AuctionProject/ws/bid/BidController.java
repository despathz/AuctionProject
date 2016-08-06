package Auction_Project.AuctionProject.ws.bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Auction_Project.AuctionProject.dao.BidDAO;

@RestController
@RequestMapping("/ws/bid")
public class BidController {
	
	@Autowired
	private BidDAO bidDAO;
	
	
	

}
