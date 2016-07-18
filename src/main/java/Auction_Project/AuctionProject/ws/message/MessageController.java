package Auction_Project.AuctionProject.ws.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.MessageDAO;

@RestController
@RequestMapping("/ws/message")
public class MessageController {

	@Autowired
	private MessageDAO messageDAO;
}
