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
import Auction_Project.AuctionProject.dao.ImageDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionSearchResponse;
import Auction_Project.AuctionProject.dto.category.CategoryResponse;
import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.category.Category;
import Auction_Project.AuctionProject.ws.image.AuctionImage;

@RestController
@RequestMapping("/ws/search")
public class SearchController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private ImageDAO imageDAO;
	
	@RequestMapping(value = "/category/matches/{id}", method = RequestMethod.GET)
	public long browseCategoryMatches(@PathVariable long id) {
		Category cat = categoryDAO.findById(id);
		return cat.getAuctions().size();
	}
	
	@RequestMapping(value = "/category/{id}/{page}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuctionSearchResponse> browseCategory(@PathVariable long id, @PathVariable int page) {
		List<AuctionSearchResponse> results = new ArrayList<AuctionSearchResponse>();
		try {
			List<Auction> auctions = categoryDAO.findAuctions(id, new PageRequest(page-1,2));
			for (int i = 0; i < auctions.size(); i++) {
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
				
				List<Category> catList = auction.getCategories();
				List<CategoryResponse> catRes = new ArrayList<CategoryResponse>();
				for (int j = 0; j < catList.size(); j++) {
					CategoryResponse cat = new CategoryResponse();
					cat.setId(catList.get(j).getId());
					cat.setName(catList.get(j).getName());
					catRes.add(cat);
				}
				result.setCategories(catRes);
				
				List<AuctionImage> imgs = imageDAO.findByAuctionId(auction);
				if (imgs.get(0).getImgPath().equals(""))
					result.setImg(imgs.get(1).getImgPath());
				else
					result.setImg(imgs.get(0).getImgPath());
					
				results.add(result);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return results;
	}

}