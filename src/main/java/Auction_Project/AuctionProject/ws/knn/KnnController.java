package Auction_Project.AuctionProject.ws.knn;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

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
	public BigInteger[] getSuggestions(@PathVariable long id) {
		
		BigInteger[] recommendedItems =  {BigInteger.valueOf(10),BigInteger.valueOf(11),BigInteger.valueOf(12), BigInteger.valueOf(13), BigInteger.valueOf(14)};
		BigInteger[] neighbor =  new BigInteger[20];
		Double[] neighborCos =  new Double[20];
		Arrays.fill(neighbor, BigInteger.ZERO);
		Arrays.fill(neighborCos, 0.0);
		
		BigInteger userA = BigInteger.valueOf(id); 
		
		if (getUserArray().get(userA).size() == 0) //if the user hasn't bids to any auctions
		{
			Integer totalAuctions = auctionDAO.countAuctions();
			for (int x = 0; x < 5; x++) {
				while (true) {
					Random rand = new Random();
					int  n = rand.nextInt(totalAuctions) + 1;
					if (!userArray.get(userA).contains(BigInteger.valueOf(n)))
					{
						recommendedItems[x] = BigInteger.valueOf(n);
						break;
					}
				}
			}
			return recommendedItems;
		}
		
		try {//calculate top 20 neighbors of user
			int position = 0;
			for(BigInteger i: getUserArray().keySet()) {
				List<BigInteger> userBAuctions = new LinkedList<BigInteger>();
				
				for (int j = 0; j < getUserArray().get(i).size(); j++)
					userBAuctions.add(getUserArray().get(i).get(j));
				
				int c_userA = getUserArray().get(userA).size();
				int c_userB = userBAuctions.size();
				List<BigInteger> commonAuctionList = userBAuctions;
				commonAuctionList.retainAll(getUserArray().get(userA)); 

				double cos_similarity = ((commonAuctionList.size()) / (Math.pow((c_userA * c_userB), 0.5)));
				
				if ((position == 20) && (commonAuctionList.size() != 0) && (neighborCos[19] < cos_similarity)) {
					neighborCos[19] = cos_similarity;
					neighbor[19] = i;
					
					for (int pos_i = 0; pos_i < 20; pos_i++) { //sort list with cosine similarity
						for (int pos_j = 0; pos_j < 20; pos_j++) {
							if (neighborCos[pos_i] > neighborCos[pos_j]) {
								double temp = neighborCos[pos_i];
								BigInteger btemp = neighbor[pos_i];
								neighborCos[pos_i] = neighborCos[pos_j];
								neighbor[pos_i] = neighbor[pos_j];
								neighborCos[pos_j] = temp;
								neighbor[pos_j] = btemp;
							}
						}
					}
				}
				else if ((commonAuctionList.size() != 0) && (position != 20)) {
					neighborCos[position] = cos_similarity;
					neighbor[position] = i;
					position++;
				}
			}
			//find top 5 items to suggest
			Map<BigInteger, List<BigInteger>> itemHaveBidders = new HashMap<BigInteger, List<BigInteger>>();
			for (BigInteger nearNeighbor: neighbor)  { //get all the items of the neighbors
				for (BigInteger item: userArray.get(nearNeighbor)) {
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
			
			for (BigInteger userAitem : getUserArray().get(userA)) { //remove all the userA items
				if (itemHaveBidders.containsKey(userAitem))
					itemHaveBidders.remove(userAitem);
			}

			int total = itemHaveBidders.keySet().size();
			BigInteger[] sortedItems =  new BigInteger[total];
			Integer[] popularItems = new Integer[total];
			Arrays.fill(sortedItems, BigInteger.ZERO);
			Arrays.fill(popularItems, 0);
			total = 0;
			for (BigInteger key : itemHaveBidders.keySet()) {
				int size = itemHaveBidders.get(key).size();
				popularItems[total] = size;
				sortedItems[total]= key;
				total++;
			}
			
			//sort items according to the amount of nearest neighbors that have bids on the item
			for (int pos_i = 0; pos_i < 20; pos_i++) { //sort list with cosine similarity
				for (int pos_j = 0; pos_j < 20; pos_j++) {
					if (popularItems[pos_i] > popularItems[pos_j]) {
						int temp = popularItems[pos_i];
						BigInteger btemp = sortedItems[pos_i];
						popularItems[pos_i] = popularItems[pos_j];
						sortedItems[pos_i] = sortedItems[pos_j];
						popularItems[pos_j] = temp;
						sortedItems[pos_j] = btemp;
					}
				}
			}
			
			for (int x = 0; x < 5; x++)
			{
				if (sortedItems[x] != BigInteger.ZERO)
					recommendedItems[x] = sortedItems[x];
				else
				{
					Integer totalAuctions = auctionDAO.countAuctions();
					while (true) {
						Random rand = new Random();
						int  n = rand.nextInt(totalAuctions) + 1;
						if (!Arrays.asList(sortedItems).contains(BigInteger.valueOf(n)) && !userArray.get(userA).contains(BigInteger.valueOf(n)))
						{
							recommendedItems[x] = BigInteger.valueOf(n);
							break;
						}
					}
				}
			}
			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return recommendedItems;
	}

	
	
	public Map<BigInteger, List<BigInteger>> getUserArray() {
		return userArray;
	}

	public void setUserArray(Map<BigInteger, List<BigInteger>> userArray) {
		this.userArray = userArray;
	}
}
