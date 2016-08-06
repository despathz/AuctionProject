package Auction_Project.AuctionProject.ws.bid;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.user.User;

@Entity
public class Bid {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@NotNull
	private float amount;
	
	@NotNull
	private Date bid_time;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "bidder")
	private User bidder;
	
	@NotNull
	@ManyToOne
	@JoinColumn(name = "auction_id")
	private Auction auction_id;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Date getBid_time() {
		return bid_time;
	}

	public void setBid_time(Date bid_time) {
		this.bid_time = bid_time;
	}

	public User getBidder() {
		return bidder;
	}

	public void setBidder(User bidder) {
		this.bidder = bidder;
	}

	public Auction getAuction_id() {
		return auction_id;
	}

	public void setAuction_id(Auction auction_id) {
		this.auction_id = auction_id;
	}

	public Bid(long id, float amount, Date bid_time, User bidder, Auction auction_id) {
		this.id = id;
		this.amount = amount;
		this.bid_time = bid_time;
		this.bidder = bidder;
		this.auction_id = auction_id;
	}

	public Bid() {
	}
	
}
