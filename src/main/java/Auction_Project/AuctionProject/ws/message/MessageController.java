package Auction_Project.AuctionProject.ws.message;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.MessageDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.message.SendMessageResponse;
import Auction_Project.AuctionProject.ws.user.User;

@RestController
@RequestMapping("/ws/message")
public class MessageController {

	@Autowired
	private MessageDAO messageDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value = "/send", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean sendMessage(@RequestBody SendMessageResponse input) {
		User sentUser = new User();
		User receiveUser = new User();
		sentUser = userDAO.findById(input.getFrom());
		receiveUser = userDAO.findById(input.getTo());
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String msgDate = dateFormat.format(date);
		Message msg = new Message(input.getTitle(), input.getText(), msgDate, false, sentUser, receiveUser);
		try {
			messageDAO.save(msg);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
}
