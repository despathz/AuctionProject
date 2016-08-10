package Auction_Project.AuctionProject.ws.auction;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import Auction_Project.AuctionProject.ws.user.User;

@Entity
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private String name;
	@NotNull 
	@Lob
	private String description;
	
	private float currently, first_bid;
	
	private float buy_price;
	
	private Date started, ends;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "user_seller_id")
	private User user_seller_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCurrently() {
		return currently;
	}

	public void setCurrently(float currently) {
		this.currently = currently;
	}

	public float getFirst_bid() {
		return first_bid;
	}

	public void setFirst_bid(float first_bid) {
		this.first_bid = first_bid;
	}

	public float getBuy_price() {
		return buy_price;
	}

	public void setBuy_price(float buy_price) {
		this.buy_price = buy_price;
	}

	public Date getStarted() {
		return started;
	}

	public void setStarted(Date started) {
		this.started = started;
	}

	public Date getEnds() {
		return ends;
	}

	public void setEnds(Date ends) {
		this.ends = ends;
	}

	public User getUser_seller_id() {
		return user_seller_id;
	}

	public void setUser_seller_id(User user_seller_id) {
		this.user_seller_id = user_seller_id;
	}
	
}
