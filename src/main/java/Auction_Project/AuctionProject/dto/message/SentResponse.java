package Auction_Project.AuctionProject.dto.message;

public class SentResponse {

	private long id;
	private String to, title, date;
	
	public SentResponse() {
	}
	
	public SentResponse(long id, String to, String title, String date) {
		super();
		this.id = id;
		this.to = to;
		this.title = title;
		this.date = date;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
}
