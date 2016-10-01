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
	public List<BigInteger> getSuggestions(@PathVariable long id) {
		
		List<BigInteger> returnlist = new ArrayList<BigInteger>();
		List<BigInteger> neighbor = new LinkedList<BigInteger>();
		List<Double> neighborCos = new LinkedList<Double>();
		
//		for (int i = 0; i < 20; i++) {
//			neighbor.add(BigInteger.ZERO);
//			neighborCos.add(0.0);
//		}
		
		BigInteger userA = BigInteger.valueOf(id); 
		try {//calculate top 20 neighbors of user
	
			for(BigInteger i: getUserArray().keySet()) {
				List<BigInteger> userBAuctions = new LinkedList<BigInteger>();
				
				for (int j = 0; j < getUserArray().get(i).size(); j++)
					userBAuctions.add(getUserArray().get(i).get(j));
				
				int c_userA = getUserArray().get(userA).size();
				int c_userB = userBAuctions.size();
				List<BigInteger> commonAuctionList = userBAuctions;
				if (commonAuctionList.size() != 0)
					System.out.println("B auctions: " + userBAuctions);
				commonAuctionList.retainAll(getUserArray().get(userA)); 
				if (c_userA != 0 && c_userB != 0)
					System.out.println(getUserArray().get(userA).size() + " " + userA + " A has " + c_userA + " " + getUserArray().get(userA) + " B: " + c_userB + " " + i + "soo common: " + commonAuctionList.size() + " " + commonAuctionList);
				
				double cos_similarity = ((commonAuctionList.size()) / (Math.pow((c_userA * c_userB), 0.5)));
				
				if (commonAuctionList.size() != 0)
					System.out.println("Cosine similarity: " + cos_similarity);
				
				if (commonAuctionList.size() != 0) {
					neighborCos.add(cos_similarity);
					neighbor.add(i);
				}
				//System.out.println(neighbor);
				//sort the two lists
				

			}
			//find top 5 items to suggest
			//List<BigInteger> itemList = new LinkedList<BigInteger>();
			Map<BigInteger, List<BigInteger>> itemHaveBidders = new HashMap<BigInteger, List<BigInteger>>();
			for (BigInteger nearNeighbor: neighbor)  { //get all the items of the neighbors
				for (BigInteger item: userArray.get(nearNeighbor)) {
					System.out.println("neig " + nearNeighbor + " item " + item);
					if (!itemHaveBidders.containsKey(item)) {
						List<BigInteger> curNeighborList = new LinkedList<BigInteger>();
						curNeighborList.add(nearNeighbor);
						itemHaveBidders.put(item, curNeighborList);
					}
					else
					{
						List<BigInteger> curNeighborList = new LinkedList<BigInteger>();
						curNeighborList.addAll(itemHaveBidders.get(item)); //get the previous neighbors for this item
						curNeighborList.add(nearNeighbor);
						itemHaveBidders.put(item, curNeighborList);
					}
				}
			}
			//System.out.println(itemHaveBidders);
			
			for (BigInteger userAitem : getUserArray().get(userA)) { //remove all the userA items
				if (itemHaveBidders.containsKey(userAitem))
					itemHaveBidders.remove(userAitem);
			}
			//System.out.println(getUserArray().get(userA) + "  " + itemHaveBidders);

			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		for (int x = 10; x < 15; x++)
			returnlist.add(BigInteger.valueOf(x));
		System.out.println(returnlist);
		
		return returnlist;
	}

	public Map<BigInteger, List<BigInteger>> getUserArray() {
		return userArray;
	}

	public void setUserArray(Map<BigInteger, List<BigInteger>> userArray) {
		this.userArray = userArray;
	}
}
