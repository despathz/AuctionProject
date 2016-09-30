package Auction_Project.AuctionProject.ws.knn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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
	
	private HashMap<Long, List<Long>> userArray;

	@Scheduled(fixedRate = 360000)
	public void updateArray() {
		
		List<User> userList = userDAO.findAll();
		HashMap<Long, List<Long>> newUserArray = new HashMap<Long, List<Long>>();
		
		Iterator<User> it = userList.iterator();
		while (it.hasNext())
		{
			Long user_id = it.next().getId();
			List<Long> auctionList = auctionDAO.userAuctions(user_id); //get the auctions this user_id user had bidden
			newUserArray.put(user_id, auctionList);//store <user_id(long), auctionList(list of long)>
		}
		setUserArray(newUserArray);
		
	}
	
	@RequestMapping(value = "/getSuggestions/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<AuctionDisplayResponse> getSuggestions(@PathVariable long id) {
		
		HashMap<Long, List<Long>> curUserArray = getUserArray();
		System.out.println("Hey :) " + userArray.size() + curUserArray.size());
		User user = new User();
		List<AuctionDisplayResponse> returnlist = new ArrayList<AuctionDisplayResponse>();
		
		Long[][] userCosSimilarity = new Long[20][2]; //[userBid][CosSimilarity]
		for (int i = 0; i < 20; i++) {
			userCosSimilarity[i][0] = 0L;
			userCosSimilarity[i][1] = 0L;
		}
		
		try {
			user = userDAO.findById(id); 
			List<Long> userAAuctions = new LinkedList<Long>(); //get the auctions the user had bidden
			System.out.println("1");
			Iterator <Long> listItem = curUserArray.get(user.getId()).iterator();
			System.out.println("2");
			while(listItem.hasNext()) {
				System.out.println("3");
				Long num = listItem.next();
				System.out.println("4");
				userAAuctions.add(num);
				System.out.println("5");
			}
			long piou = 7077L;
			List<Long> userAAAuctions = curUserArray.get(piou);
			System.out.println(userAAuctions + " - " + userAAAuctions + " : " + (user.getId()==piou) );
			//calculate top 20 neighbors of user
			
			Iterator<Map.Entry<Long, List<Long>>> itB = curUserArray.entrySet().iterator();
			while (itB.hasNext()) {
				Map.Entry<Long, List<Long>> userBEntry = itB.next();
				Long userB = userBEntry.getKey();
				List<Long> userBAuctions = userBEntry.getValue();
				List<Long> commonAuctionList = userAAuctions;
				commonAuctionList.retainAll(userBAuctions); 
				Long c_userA = (long) userAAuctions.size();
				Long c_userB = (long) userBAuctions.size();
				if (c_userB > 3)
					System.out.println(user.getId() + "A has " + c_userA + " " + userAAuctions + " B: " + c_userB + " " + userBAuctions + " soo common: " + commonAuctionList.size());
				
				Long cos_similarity = (long) (commonAuctionList.size() / Math.pow((c_userA * c_userB), 1/2));
				if (commonAuctionList.size() != 0)
					System.out.println("SQ " + cos_similarity + commonAuctionList.size());
				if (userCosSimilarity[19][1] < cos_similarity) {
					userCosSimilarity[19][1] = cos_similarity; //add in the fifth place of the array
					userCosSimilarity[19][0] = userB;
					
					Arrays.sort(userCosSimilarity, new Comparator<Long[]>() {  //sort the array by cosine similarity
		            public int compare(final Long[] entry1, final Long[] entry2) {
		                final Long cos1 = entry1[1];
		                final Long cos2 = entry2[1];
		                return cos1.compareTo(cos2);
		            }
			        });

				}
			}
			System.out.println(userCosSimilarity[0][0] + " and " + userCosSimilarity[0][1]);
			//find top 5 items to suggest
			
		}
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}
		
		return returnlist;
	}

	public HashMap<Long, List<Long>> getUserArray() {
		return userArray;
	}

	public void setUserArray(HashMap<Long, List<Long>> userArray) {
		this.userArray = userArray;
	}
}
