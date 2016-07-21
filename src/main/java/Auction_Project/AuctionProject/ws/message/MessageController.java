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
import Auction_Project.AuctionProject.dto.message.MessageIDResponse;
import Auction_Project.AuctionProject.dto.message.MessageListResponse;
import Auction_Project.AuctionProject.dto.message.SendMessageResponse;
import Auction_Project.AuctionProject.dto.message.SentResponse;
import Auction_Project.AuctionProject.dto.message.ViewMessageResponse;
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
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String msgDate = dateFormat.format(date);		
		try {
			sentUser = userDAO.findById(input.getFrom());
			receiveUser = userDAO.findById(input.getTo());
			Message msg = new Message(input.getTitle(), input.getText(), msgDate, false, false, false, sentUser, receiveUser);
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
			messageList = messageDAO.findByReceiveUserAndInboxDelete(user, false);
			for (Iterator<Message> iterator = messageList.iterator(); iterator.hasNext();) {
				Message msg = iterator.next();
				InboxResponse response = new InboxResponse(msg.getId(), msg.getSentUser().getUsername(), msg.getTitle(), msg.getDate(), msg.getIsRead());
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
			messageList = messageDAO.findBySentUserAndSentDelete(user, false);
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
	
	@RequestMapping(value = "inbox/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean inboxDelete(@RequestBody MessageListResponse messages) {
		Long[] ids = messages.getIds();
		try {
			for (int i = 0; i < ids.length; i++) {
				Message msg = messageDAO.findById(ids[i]);
				if (msg.getSentDelete() == true)
					messageDAO.delete(msg);
				else {
					msg.setInboxDelete(true);
					messageDAO.save(msg);
				}
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "sent/delete", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean SentDelete(@RequestBody MessageListResponse messages) {
		Long[] ids = messages.getIds();
		try {
			for (int i = 0; i < ids.length; i++) {
				Message msg = messageDAO.findById(ids[i]);
				if (msg.getInboxDelete() == true)
					messageDAO.delete(msg);
				else {
					msg.setSentDelete(true);
					messageDAO.save(msg);
				}
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "markRead", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean markRead(@RequestBody MessageListResponse messages) {
		Long[] ids = messages.getIds();
		try {
			for (int i = 0; i < ids.length; i++) {
				Message msg = messageDAO.findById(ids[i]);
				msg.setIsRead(true);
				messageDAO.save(msg);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "markUnRead", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean markUnRead(@RequestBody MessageListResponse messages) {
		Long[] ids = messages.getIds();
		try {
			for (int i = 0; i < ids.length; i++) {
				Message msg = messageDAO.findById(ids[i]);
				msg.setIsRead(false);
				messageDAO.save(msg);
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
			return false;
		}
		return true;
	}
	
	@RequestMapping(value = "inbox/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long inboxCount(@RequestBody IdResponse input_user) {
		Long c = new Long(0);
		try {
			User user = userDAO.findById(input_user.getId());
			c = messageDAO.countByReceiveUserAndInboxDelete(user, false);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return c;
	}
	
	@RequestMapping(value = "sent/count", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Long sentCount(@RequestBody IdResponse input_user) {
		Long c = new Long(0);
		try {
			User user = userDAO.findById(input_user.getId());
			c = messageDAO.countBySentUserAndSentDelete(user, false);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return c;
	}
	
	@RequestMapping(value = "view", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public ViewMessageResponse viewMessage(@RequestBody MessageIDResponse messageID) {
		ViewMessageResponse responseMessage = new ViewMessageResponse();
		try {
			Message msg = messageDAO.findById(messageID.getId());
			responseMessage = new ViewMessageResponse(msg.getTitle(), msg.getReceiveUser().getUsername(), msg.getSentUser().getUsername(), msg.getText());
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return responseMessage;
	}
}
