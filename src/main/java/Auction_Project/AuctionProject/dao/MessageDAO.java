package Auction_Project.AuctionProject.dao;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.message.Message;
import Auction_Project.AuctionProject.ws.user.User;

@Transactional
public interface MessageDAO extends CrudRepository<Message, Long>{

	public List<Message> findByReceiveUser(User receiveUser);
	public List<Message> findBySentUser(User sentUser);
}
