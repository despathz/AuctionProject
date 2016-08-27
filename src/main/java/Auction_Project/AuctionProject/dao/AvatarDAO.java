package Auction_Project.AuctionProject.dao;

import Auction_Project.AuctionProject.ws.image.Avatar;
import Auction_Project.AuctionProject.ws.user.User;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

@Transactional
public interface AvatarDAO extends CrudRepository<Avatar, Long>{
	
	public Avatar findById(long id);
	public Avatar findByUserId(User user);
}
