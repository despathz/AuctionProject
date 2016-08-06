package Auction_Project.AuctionProject.dto.bid;

import java.util.Date;

public class BidListResponse {
	
	private long bidder_id;
	
	private String bidder_username;
	
	private Date bid_time;
	
	private float amount;

	public long getBidder_id() {
		return bidder_id;
	}

	public void setBidder_id(long bidder_id) {
		this.bidder_id = bidder_id;
	}

	public String getBidder_username() {
		return bidder_username;
	}

	public void setBidder_username(String bidder_username) {
		this.bidder_username = bidder_username;
	}

	public Date getBid_time() {
		return bid_time;
	}

	public void setBid_time(Date bid_time) {
		this.bid_time = bid_time;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public BidListResponse(long bidder_id, String bidder_username, Date bid_time, float amount) {
		this.bidder_id = bidder_id;
		this.bidder_username = bidder_username;
		this.bid_time = bid_time;
		this.amount = amount;
	}

}
