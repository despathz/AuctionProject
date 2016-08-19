package Auction_Project.AuctionProject.dto.auction;

import java.util.Date;

public class UserAuctionsListResponse {
	
	private String name;
	private Date started, ends;
	
	public UserAuctionsListResponse(String name, Date started, Date ends) {
		this.name = name;
		this.started = started;
		this.ends = ends;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
	
}
