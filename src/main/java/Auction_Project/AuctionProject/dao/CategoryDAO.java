package Auction_Project.AuctionProject.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.category.Category;

@Transactional
public interface CategoryDAO extends CrudRepository<Category, Long>{

	public Long countByNameAndParent(String name, Category parent);
	public Category findById(long id);
	public List<Category>findByParent(Category parent);
	public Category findByNameAndParent(String name, Category parent);
}
