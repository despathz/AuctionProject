package Auction_Project.AuctionProject.ws.message;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.MessageDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.message.InboxResponse;
import Auction_Project.AuctionProject.dto.message.SendMessageResponse;
import Auction_Project.AuctionProject.dto.message.SentResponse;
import Auction_Project.AuctionProject.dto.user.IdResponse;
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
	
	@RequestMapping(value = "/inbox", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<InboxResponse> inbox(@RequestBody IdResponse input_user) {
		List<InboxResponse> responseList = new ArrayList<InboxResponse>();
		List<Message> messageList = new ArrayList<Message>();
		try {
			User user = userDAO.findById(input_user.getId());
			messageList = messageDAO.findByReceiveUser(user);
			for (Iterator<Message> iterator = messageList.iterator(); iterator.hasNext();) {
				Message msg = iterator.next();
				InboxResponse response = new InboxResponse(msg.getId(), msg.getSentUser().getUsername(), msg.getTitle(), msg.getDate());
				responseList.add(response);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return responseList;
	}
	
	@RequestMapping(value = "/sent", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public List<SentResponse> sent(@RequestBody IdResponse input_user) {
		List<SentResponse> responseList = new ArrayList<SentResponse>();
		List<Message> messageList = new ArrayList<Message>();
		try {
			User user = userDAO.findById(input_user.getId());
			messageList = messageDAO.findBySentUser(user);
			for (Iterator<Message> iterator = messageList.iterator(); iterator.hasNext();) {
				Message msg = iterator.next();
				SentResponse response = new SentResponse(msg.getId(), msg.getReceiveUser().getUsername(), msg.getTitle(), msg.getDate());
				responseList.add(response);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return responseList;
	}
	
}
