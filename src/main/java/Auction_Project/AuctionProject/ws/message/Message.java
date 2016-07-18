package Auction_Project.AuctionProject.ws.message;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import Auction_Project.AuctionProject.ws.user.User;

@Entity
public class Message {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String title, text, date;
	
	private Boolean isRead;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "sentUser_id")
	private User sentUser;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "receiveUser_id")
	private User receiveUser;
	
	public Message() {
		
	}

	public Message(String title, String text, String date, Boolean isRead, User sentUser, User receiveUser) {
		this.title = title;
		this.text = text;
		this.date = date;
		this.isRead = isRead;
		this.sentUser = sentUser;
		this.receiveUser = receiveUser;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public Boolean getIsRead() {
		return isRead;
	}

	public void setIsRead(Boolean isRead) {
		this.isRead = isRead;
	}

	public User getSentUser() {
		return sentUser;
	}

	public void setSentUser(User sentUser) {
		this.sentUser = sentUser;
	}

	public User getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(User receiveUser) {
		this.receiveUser = receiveUser;
	}
	
	
}
