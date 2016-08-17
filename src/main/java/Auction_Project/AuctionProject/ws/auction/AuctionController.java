package Auction_Project.AuctionProject.ws.auction;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dao.CategoryDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionDisplayResponse;
import Auction_Project.AuctionProject.dto.auction.AuctionSaveResponse;
import Auction_Project.AuctionProject.dto.category.CategoryResponse;
import Auction_Project.AuctionProject.ws.category.Category;
import Auction_Project.AuctionProject.ws.user.User;


@RestController
@RequestMapping("/ws/auction")
public class AuctionController {
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private CategoryDAO categoryDAO;

	@RequestMapping(value = "/begin/{auctionID}", method = RequestMethod.GET)
	public Date begin(@PathVariable long auctionID) {
		Date date = new Date();
		try {
			Auction auction = auctionDAO.findById(auctionID);
			auction.setStarted(date);
			auctionDAO.save(auction);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return date;
	}

	@RequestMapping(value = "/{auctionID}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public AuctionDisplayResponse getByID(@PathVariable long auctionID) {
		AuctionDisplayResponse auctionResponse = new AuctionDisplayResponse(0);
		try {
			Auction auction = auctionDAO.findById(auctionID);
			User user = auction.getUser_seller_id();
			auctionResponse.setId(auction.getId());
			auctionResponse.setName(auction.getName());
			auctionResponse.setDescription(auction.getDescription());
			auctionResponse.setCurrently(auction.getCurrently());
			auctionResponse.setFirst_bid(auction.getFirst_bid());
			auctionResponse.setBuy_price(auction.getBuy_price());
			auctionResponse.setStarted(auction.getStarted());
			auctionResponse.setEnds(auction.getEnds());
			auctionResponse.setCreator(user.getUsername());
			auctionResponse.setUser_id(user.getId());
			auctionResponse.setCountry(auction.getCountry());
			auctionResponse.setLocation(auction.getLocation());
			auctionResponse.setLongitude(auction.getLongitude());
			auctionResponse.setLatitude(auction.getLatitude());
			
			List<Category> cat = auctionDAO.findCategories(auctionID);
			List<CategoryResponse> reCat = new ArrayList<CategoryResponse>();
			for (int i = 0; i < cat.size(); i++) {
				CategoryResponse newCat = new CategoryResponse();
				newCat.setId(cat.get(i).getId());
				newCat.setName(cat.get(i).getName());
				reCat.add(newCat);
			}
			auctionResponse.setCategories(reCat);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return auctionResponse;
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public long createAuction(@RequestBody AuctionSaveResponse new_auction) {	
		Auction auction = new Auction();
		Auction returned = new Auction();
		try {
			auction.setName(new_auction.getName());
			auction.setDescription(new_auction.getDescription());
			auction.setStarted(new_auction.getStarted());
			auction.setEnds(new_auction.getEnds());
			auction.setCurrently(new_auction.getFirst_bid());
			auction.setFirst_bid(new_auction.getFirst_bid());
			auction.setBuy_price(new_auction.getBuy_price());
			auction.setCreated(new Date());
			auction.setLocation(new_auction.getLocation());
			auction.setLatitude(new_auction.getLatitude());
			auction.setLongitude(new_auction.getLongitude());
			auction.setCountry(new_auction.getCountry());
			User user = userDAO.findById(new_auction.getUser_id());
			auction.setUser_seller_id(user);
			
			Set<Category> catset = new HashSet<Category>();
			for (int i = 0; i < new_auction.getCategoryList().size(); i++) {
				Category cat = categoryDAO.findById(new_auction.getCategoryList().get(i).getId());
				catset.add(cat);
			}
			auction.setCategories(catset);
			returned = auctionDAO.save(auction);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return -1;
		}
		return returned.getId();
	}
	
	@RequestMapping(value = "/edit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean editAuction(@RequestBody AuctionSaveResponse new_auction) {
		try {
			Auction auction = auctionDAO.findById(new_auction.getId());
			auction.setName(new_auction.getName());
			auction.setDescription(new_auction.getDescription());
			auction.setStarted(new_auction.getStarted());
			auction.setEnds(new_auction.getEnds());
			auction.setCurrently(new_auction.getFirst_bid());
			auction.setFirst_bid(new_auction.getFirst_bid());
			auction.setBuy_price(new_auction.getBuy_price());
			auction.setLocation(new_auction.getLocation());
			auction.setLatitude(new_auction.getLatitude());
			auction.setLongitude(new_auction.getLongitude());
			auction.setCountry(new_auction.getCountry());
			
			Set<Category> catset = auction.getCategories();
			catset.clear();
			for (int i = 0; i < new_auction.getCategoryList().size(); i++) {
				Category cat = categoryDAO.findById(new_auction.getCategoryList().get(i).getId());
				catset.add(cat);
			}
			auction.setCategories(catset);
			
			auctionDAO.save(auction);
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
}
