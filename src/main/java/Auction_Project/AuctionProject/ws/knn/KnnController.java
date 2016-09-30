package Auction_Project.AuctionProject.ws.knn;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.auction.AuctionDisplayResponse;
import Auction_Project.AuctionProject.ws.user.User;
import Auction_Project.AuctionProject.dao.AuctionDAO;
@EnableScheduling
@RestController
@RequestMapping("/ws/knn")
public class KnnController {
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	private Map<BigInteger, List<BigInteger>> userArray;


	@Scheduled(fixedRate = 360000)
	public void updateArray() {
		
		List<User> userList = userDAO.findAll();
		Map<BigInteger, List<BigInteger>> newUserArray = new HashMap<BigInteger, List<BigInteger>>();
		
		Iterator<User> it = userList.iterator();
		while (it.hasNext())
		{
			BigInteger user_id = BigInteger.valueOf(it.next().getId());
			List<BigInteger> auctionList = auctionDAO.userAuctions(user_id); //get the auctions this user_id user had bidden
			newUserArray.put(user_id, auctionList);//store <user_id(long), auctionList(list of long)>
		}
		setUserArray(newUserArray);
		System.out.println("array ok" + newUserArray.size() + newUserArray);
		
	}
	
	@RequestMapping(value = "/getSuggestions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuctionDisplayResponse> getSuggestions(@PathVariable long id) {
		
		List<AuctionDisplayResponse> returnlist = new ArrayList<AuctionDisplayResponse>();
		Map<BigInteger, List<BigInteger>> curUserArray = new HashMap<BigInteger, List<BigInteger>>();
		curUserArray.putAll(getUserArray());
		System.out.println("Hey :) " + userArray.size() + curUserArray.size());
		User user = new User();
		
		List<BigInteger> neighbor = new LinkedList<BigInteger>();
		List<Double> neighborCos = new LinkedList<Double>();
		
		for (int i = 0; i < 20; i++) {
			neighbor.add(BigInteger.ZERO);
			neighborCos.add(0.0);
		}
		
		try {
			BigInteger userA = BigInteger.valueOf(id);
			
			//calculate top 20 neighbors of user

			System.out.println("B");
			for(BigInteger i: curUserArray.keySet()) {
				List<BigInteger> userBAuctions = new LinkedList<BigInteger>();
				
				for (int j = 0; j < curUserArray.get(i).size(); j++)
					userBAuctions.add(curUserArray.get(i).get(j));
				
				int c_userA = curUserArray.get(userA).size();
				int c_userB = userBAuctions.size();
				List<BigInteger> commonAuctionList = userBAuctions;
				if (commonAuctionList.size() != 0)
					System.out.println("B auctions: " + userBAuctions);
				commonAuctionList.retainAll(curUserArray.get(userA)); 
				if (c_userA != 0 && c_userB != 0)
					System.out.println(curUserArray.get(userA).size() + " " + userA + " A has " + c_userA + " " + curUserArray.get(userA) + " B: " + c_userB + " " + i + "soo common: " + commonAuctionList.size() + " " + commonAuctionList);
				
				double cos_similarity = ((commonAuctionList.size()) / (Math.pow((c_userA * c_userB), 0.5)));
				
				if (neighborCos.get(19) < cos_similarity) {
					neighborCos.add(19, cos_similarity);
					neighbor.add(19, i);
				}
					
				//sort the two lists

			}
			//find top 5 items to suggest
			
			
			System.out.println("Bye :) " + userArray.size() + curUserArray.size());
			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return returnlist;
	}

	public Map<BigInteger, List<BigInteger>> getUserArray() {
		return userArray;
	}

	public void setUserArray(Map<BigInteger, List<BigInteger>> userArray) {
		this.userArray = userArray;
	}
}
