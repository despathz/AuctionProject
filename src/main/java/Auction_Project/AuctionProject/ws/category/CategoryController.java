package Auction_Project.AuctionProject.ws.category;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.CategoryDAO;

@RestController
@RequestMapping("/ws/category")
public class CategoryController {
	
	@Autowired
	private CategoryDAO categoryDAO;
	
	@RequestMapping(value = "/parent/{parent_id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<Category> getSubCategories(@PathVariable long parent_id) {
		List<Category> list = new ArrayList<Category>();
		try {
			Category parent = categoryDAO.findById(parent_id);
			list = categoryDAO.findByParent(parent);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return list;
	}

}
