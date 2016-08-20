package Auction_Project.AuctionProject.ws.search;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.CategoryDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionSearchResponse;
import Auction_Project.AuctionProject.ws.auction.Auction;

@RestController
@RequestMapping("/ws/search")
public class SearchController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@RequestMapping(value = "/category/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuctionSearchResponse> browseCategory(@PathVariable long id) {
		List<AuctionSearchResponse> results = new ArrayList<AuctionSearchResponse>();
		try {
			List<Auction> auctions = categoryDAO.findAuctions(id, new PageRequest(0,5));
			for (int i = 0; i <auctions.size(); i++) {
				AuctionSearchResponse result = new AuctionSearchResponse();
				Auction auction = auctions.get(i);
				result.setId(auction.getId());
				result.setName(auction.getName());
				result.setCurrently(auction.getCurrently());
				result.setBuy_price(auction.getBuy_price());
				result.setSeller_id(auction.getUser_seller_id().getId());
				result.setRating(auction.getUser_seller_id().getSellerRating());
				result.setSeller_username(auction.getUser_seller_id().getUsername());
				result.setLocation(auction.getLocation());
				boolean status = false;
				if (auction.getStarted() != null && auction.getEnds().after(new Date()) && auction.getCurrently() != auction.getBuy_price()) {
					status = true;
				}
				result.setStatus(status);
				results.add(result);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return results;
	}

}
