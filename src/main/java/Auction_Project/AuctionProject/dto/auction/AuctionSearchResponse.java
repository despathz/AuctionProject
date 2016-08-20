package Auction_Project.AuctionProject.dto.auction;

public class AuctionSearchResponse {

	private long id, seller_id;
	
	private String name, seller_username, location;
	
	private boolean status;
	
	private float currently, buy_price;
	
	private int rating;

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


	public long getSeller_id() {
		return seller_id;
	}

	public void setSeller_id(long seller_id) {
		this.seller_id = seller_id;
	}

	public String getSeller_username() {
		return seller_username;
	}

	public void setSeller_username(String seller_username) {
		this.seller_username = seller_username;
	}
	

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public float getCurrently() {
		return currently;
	}

	public void setCurrently(float currently) {
		this.currently = currently;
	}

	public float getBuy_price() {
		return buy_price;
	}

	public void setBuy_price(float buy_price) {
		this.buy_price = buy_price;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}
	
	
}
