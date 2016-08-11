package Auction_Project.AuctionProject.dto.image;

public class saveImageResponse {
	
	String imgA, imgB;
	
	long auction;

	public String getImgA() {
		return imgA;
	}

	public void setImgA(String imgA) {
		this.imgA = imgA;
	}

	public String getImgB() {
		return imgB;
	}

	public void setImgB(String imgB) {
		this.imgB = imgB;
	}

	public long getAuction() {
		return auction;
	}

	public void setAuction(long auction) {
		this.auction = auction;
	}
}
