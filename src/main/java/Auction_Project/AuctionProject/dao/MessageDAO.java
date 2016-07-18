package Auction_Project.AuctionProject.dao;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import Auction_Project.AuctionProject.ws.message.Message;

@Transactional
public interface MessageDAO extends CrudRepository<Message, Long>{

}
