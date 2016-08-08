package Auction_Project.AuctionProject.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.category.Category;

@Transactional
public interface CategoryDAO extends CrudRepository<Category, Long>{

	public Long countByName(String name);
	public Category findById(long id);
	public Category findByName(String name);
	public List<Category>findByParent(Category parent);
}
