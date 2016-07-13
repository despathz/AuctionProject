package Auction_Project.AuctionProject.ws.user;

import javax.validation.constraints.NotNull;

public class UserPostParams {
	
	private User findUser;

	@NotNull
    private Boolean userExists;
    
	public User getFindUser() {
		return findUser;
	}
	public void setFindUser(User findUser) {
		this.findUser = findUser;
	}

	public Boolean getUserExists() {
		return userExists;
	}
	public void setUserExists(Boolean userExists) {
		this.userExists = userExists;
	}
}
