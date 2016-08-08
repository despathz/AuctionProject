package Auction_Project.AuctionProject.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.category.Category;

@Transactional
public interface CategoryDAO extends CrudRepository<Category, Long>{

	public Long countByName(String name);
}
