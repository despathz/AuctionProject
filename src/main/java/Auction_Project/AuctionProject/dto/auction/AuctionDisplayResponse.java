package Auction_Project.AuctionProject.dto.auction;

import java.util.Date;

public class AuctionDisplayResponse {
	
	private long id;
	
	private String name, description;
	
	private float currently, first_bid;
	
	private float buy_price;
	
	private Date started, ends;
	
	private String creator; //for display
	private long user_id; //for display
	
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
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public long getUser_id() {
		return user_id;
	}
	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
	public AuctionDisplayResponse(long id, String name, String description, float currently, float first_bid,
			float buy_price, Date started, Date ends, String creator, long user_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.currently = currently;
		this.first_bid = first_bid;
		this.buy_price = buy_price;
		this.started = started;
		this.ends = ends;
		this.creator = creator;
		this.user_id = user_id;
	}
	
	public AuctionDisplayResponse() {
		
	}
	
	public AuctionDisplayResponse(long id) {
		this.id = id;
	}

}
