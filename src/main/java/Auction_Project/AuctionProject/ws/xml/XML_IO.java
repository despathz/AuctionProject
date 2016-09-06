package Auction_Project.AuctionProject.ws.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dao.BidDAO;
import Auction_Project.AuctionProject.dao.CategoryDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.ws.auction.Auction;
import Auction_Project.AuctionProject.ws.bid.Bid;
import Auction_Project.AuctionProject.ws.category.Category;
import Auction_Project.AuctionProject.ws.image.Avatar;
import Auction_Project.AuctionProject.ws.user.User;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/ws/xml")
public class XML_IO {

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	@Autowired
	private BidDAO bidDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@RequestMapping(value = "/produce/{auction_id}", method = RequestMethod.GET)
	public String create(@PathVariable long auction_id) {
		Auction auction = new Auction();
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();
			
			String day, t, year;
			String parts[];
			int month;
			HashMap<Integer, String> monthMap = new HashMap<Integer, String>();
			monthMap.put(1, "Jan");	monthMap.put(2, "Feb");
			monthMap.put(3, "Mar");	monthMap.put(4, "Apr");
			monthMap.put(5, "May");	monthMap.put(6, "Jun");
			monthMap.put(7, "Jul");	monthMap.put(8, "Aug");
			monthMap.put(9, "Sep");	monthMap.put(10, "Oct");
			monthMap.put(11, "Nov");	monthMap.put(12, "Dec");
			
			auction = auctionDAO.findById(auction_id);
			
			//root element
			Element rootElement = doc.createElement("Item");
			doc.appendChild(rootElement);
			
			//set itemID
			rootElement.setAttribute("ItemID", Long.toString(auction.getId()));
			
			//set name
			Element nameElement = doc.createElement("Name");
			rootElement.appendChild(nameElement);
			nameElement.appendChild(doc.createTextNode(auction.getName()));
			
			List<Category> cat = auction.getCategories();
			
			for (int i = 0; i < cat.size(); i++) {
				if (cat.get(i).getId() == 1)
					continue;
				Element categoryElement = doc.createElement("Category");
				rootElement.appendChild(categoryElement);
				categoryElement.appendChild(doc.createTextNode(cat.get(i).getName()));
			}
			
			//set currently
			Element currentlyElement = doc.createElement("Currently");
			rootElement.appendChild(currentlyElement);
			currentlyElement.appendChild(doc.createTextNode("$" + Float.toString(auction.getCurrently())));
			
			//set buy price
			if (auction.getBuy_price() != 0) {
				Element buypriceElement = doc.createElement("Buy_Price");
				rootElement.appendChild(buypriceElement);
				buypriceElement.appendChild(doc.createTextNode("$" + Float.toString(auction.getBuy_price())));
			}
			
			//set first_bid
			Element first_bidElement = doc.createElement("First_Bid");
			rootElement.appendChild(first_bidElement);
			first_bidElement.appendChild(doc.createTextNode("$" + Float.toString(auction.getFirst_bid())));
			
			//set number of bids
			long bidNumber = bidDAO.countByAuctionId(auction);
			Element bidNumberElement = doc.createElement("Number_of_Bids");
			rootElement.appendChild(bidNumberElement);
			bidNumberElement.appendChild(doc.createTextNode(Long.toString(bidNumber)));
			
			//set bids
			Element bidsElement = doc.createElement("Bids");
			rootElement.appendChild(bidsElement);
			
			//set bid
			List<Bid> bidList = bidDAO.findByAuctionIdOrderByAmountAsc(auction);
			for (int i = 0; i < bidList.size(); i++) {
				Element bidElement = doc.createElement("Bid");
				bidsElement.appendChild(bidElement);
				Element bidderElement = doc.createElement("Bidder");
				bidElement.appendChild(bidderElement);
				bidderElement.setAttribute("UserID", bidList.get(i).getBidder().getUsername());
				bidderElement.setAttribute("Rating", Integer.toString(bidList.get(i).getBidder().getBidderRating()));
				Element bidderLocationElement = doc.createElement("Location");
				bidderElement.appendChild(bidderLocationElement);
				bidderLocationElement.appendChild(doc.createTextNode(bidList.get(i).getBidder().getLocation()));
				Element bidderCountryElement = doc.createElement("Country");
				bidderElement.appendChild(bidderCountryElement);
				bidderCountryElement.appendChild(doc.createTextNode(bidList.get(i).getBidder().getCountry()));
				Element bidTimeElement = doc.createElement("Time");
				bidElement.appendChild(bidTimeElement);
				
				String bidTime = auction.getStarted().toString();
				parts = bidTime.split("-");
				year = parts[0].substring(2, 4);
				month = Integer.parseInt(parts[1]);
				parts = parts[2].split(" ");
				day = parts[0];
				t = parts[1].replace(".", " ");
				parts = t.split(" ");
				t = parts[0];
				bidTime = monthMap.get(month) + "-" + day + "-" + year + " " + t;
				bidTimeElement.appendChild(doc.createTextNode(bidTime));
				
				Element amountElement = doc.createElement("Amount");
				bidElement.appendChild(amountElement);
				amountElement.appendChild(doc.createTextNode("$" + Float.toString(bidList.get(i).getAmount())));
			}
			
			//set location
			Element locationElement = doc.createElement("Location");
			rootElement.appendChild(locationElement);
			locationElement.appendChild(doc.createTextNode(auction.getLocation()));
			
			String latitude = auction.getLatitude();
			String longitude = auction.getLongitude();
			if (!longitude.equals("0"))
				locationElement.setAttribute("Longitude", longitude);
			if (!latitude.equals("0"))
				locationElement.setAttribute("Latitude", latitude);
			
			//set country
			Element countryElement = doc.createElement("Country");
			rootElement.appendChild(countryElement);
			countryElement.appendChild(doc.createTextNode(auction.getCountry()));
			
			//set start date
			Element startElement = doc.createElement("Started");
			rootElement.appendChild(startElement);
			String started = auction.getStarted().toString();
			parts = started.split("-");
			year = parts[0].substring(2, 4);
			month = Integer.parseInt(parts[1]);
			parts = parts[2].split(" ");
			day = parts[0];
			t = parts[1].replace(".", " ");
			parts = t.split(" ");
			t = parts[0];
			started = monthMap.get(month) + "-" + day + "-" + year + " " + t;
			startElement.appendChild(doc.createTextNode(started));
			
			
			//set end date
			Element endElement = doc.createElement("Ends");
			rootElement.appendChild(endElement);
			String ends = auction.getEnds().toString();
			parts = ends.split("-");
			year = parts[0].substring(2, 4);
			month = Integer.parseInt(parts[1]);
			parts = parts[2].split(" ");
			day = parts[0];
			t = parts[1].replace(".", " ");
			parts = t.split(" ");
			t = parts[0];
			ends = monthMap.get(month) + "-" + day + "-" + year + " " + t;
			endElement.appendChild(doc.createTextNode(ends));
			
			//seller id and rating
			Element sellerElement = doc.createElement("Seller");
			rootElement.appendChild(sellerElement);
			sellerElement.setAttribute("UserID", auction.getUser_seller_id().getUsername());
			sellerElement.setAttribute("Rating", Integer.toString(auction.getUser_seller_id().getSellerRating()));
			
			//set description
			Element descElement = doc.createElement("Description");
			rootElement.appendChild(descElement);
			descElement.appendChild(doc.createTextNode(auction.getDescription()));
			
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("./src/main/resources/static/" + auction.getId() + ".xml"));
			transformer.transform(source, result);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return Long.toString(auction.getId());
	}
	
	@RequestMapping(value = "/load/{filename}", method = RequestMethod.GET)
	public void read(@PathVariable String filename) {

	    try {

	    File xmlFile = new File(filename + ".xml");
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	    DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	    Document doc = dBuilder.parse(xmlFile);
	    
	    doc.getDocumentElement().normalize();

	    NodeList nList = doc.getElementsByTagName("Item");

	    for (int i = 0; i < nList.getLength(); i++) {
//	    for (int i = 2; i < 3; i++) {

	        Node nNode = nList.item(i);

	        if (nNode.getNodeType() == Node.ELEMENT_NODE) {

	            Element eElement = (Element) nNode;
	            Auction auction = new Auction();
	            auction.setName(eElement.getElementsByTagName("Name").item(0).getTextContent());

//	            System.out.println("ItemID : " + eElement.getAttribute("ItemID"));
//	            System.out.println("Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
	            
	            
	            String catName = (String)eElement.getElementsByTagName("Category").item(0).getTextContent();
	            Category parent = categoryDAO.findById(1);
	            if (categoryDAO.countByNameAndParent(catName, parent) == 0) {
	            	Category category = new Category();
	            	category.setName(catName);
	            	category.setParent(parent);
	            	parent = categoryDAO.save(category);
	            }
	            else
	            	parent = categoryDAO.findByNameAndParent(catName, parent);
	            
	            for (int j = 1; j < eElement.getElementsByTagName("Category").getLength(); j++) {
	            	catName = (String)eElement.getElementsByTagName("Category").item(j).getTextContent();
		            if (categoryDAO.countByNameAndParent(catName, parent) == 0) {
		            	Category category = new Category();
		            	category.setName(catName);
		            	category.setParent(parent);
		            	parent = categoryDAO.save(category);
		            }
		            else
		            	parent = categoryDAO.findByNameAndParent(catName, parent);
	            }
	            
//	            System.out.println("Currently : " + eElement.getElementsByTagName("Currently").item(0).getTextContent());
//	            System.out.println("First_bid : " + eElement.getElementsByTagName("First_Bid").item(0).getTextContent());
         
	            auction.setCurrently(Float.parseFloat(eElement.getElementsByTagName("Currently").item(0).getTextContent().substring(1)));
	            auction.setFirst_bid(Float.parseFloat(eElement.getElementsByTagName("First_Bid").item(0).getTextContent().substring(1)));
	            int bidNumber = Integer.parseInt(eElement.getElementsByTagName("Number_of_Bids").item(0).getTextContent());
	            
	            Element bidsElement = (Element) eElement.getElementsByTagName("Bids").item(0);
	            
	            for (int j = 0; j < bidNumber; j++) {
	            	Element bidElement = (Element) bidsElement.getElementsByTagName("Bid").item(j);
	            	Element bidderElement = (Element) bidElement.getElementsByTagName("Bidder").item(0);
//	            	System.out.println("	Bidder Location : " + bidderElement.getElementsByTagName("Location").item(0).getTextContent());
//	            	System.out.println("	Bidder Country : " + bidderElement.getElementsByTagName("Country").item(0).getTextContent());
	            	if (userDAO.countByUsername(bidderElement.getAttribute("UserID")) == 0) {
			            User user = new User();
			            user.setUsername(bidderElement.getAttribute("UserID"));
			            user.setBidderRating(Integer.parseInt(bidderElement.getAttribute("Rating")));
			            user.setPassword("password");
			            user.setEmail(bidderElement.getAttribute("UserID") + "@gmail.com");
			            user.setRemember(false);
			            user.setActivation(true);
			            user.setSuperuser(false);
			            user.setName(bidderElement.getAttribute("UserID"));
			            user.setSurname("Jones");
			            user.setAddress("St Pauls 89");
			            user.setCountry("Italy");
			            user.setTelephone("11880");
			            user.setTrn("666");
			            user.setLocation("Venezia");
			            user.setSellerRating(0);
			            user.setAvatar(new Avatar("./img/avatars/avatar0.png"));
			            userDAO.save(user);
		            }
		            else {
		            	User user = userDAO.findByUsername(bidderElement.getAttribute("UserID"));
		            	user.setBidderRating(Integer.parseInt(bidderElement.getAttribute("Rating")));
		            	user.setCountry("Italy");
		            	user.setLocation("Venezia");
		            	userDAO.save(user);
		            }
//	            	System.out.println("	Time : " + bidElement.getElementsByTagName("Time").item(0).getTextContent());
//	            	System.out.println("	Amount : " + bidElement.getElementsByTagName("Amount").item(0).getTextContent());
//	            	System.out.println("");
	            }
            
//	            Element lElement = (Element) eElement.getElementsByTagName("Location").item(bidNumber);
//	            System.out.println("Location Longitude : " + lElement.getAttribute("Longitude"));
//	            System.out.println("Location Latitude : " + lElement.getAttribute("Latitude"));
//	            System.out.println("Location : " + lElement.getTextContent());
//	            auction.setLocation(lElement.getTextContent());
	            auction.setCountry(eElement.getElementsByTagName("Country").item(0).getTextContent());
//	            System.out.println("Country : " + eElement.getElementsByTagName("Country").item(0).getTextContent());
//	            System.out.println("Started : " + eElement.getElementsByTagName("Started").item(0).getTextContent());
//	            System.out.println("Ends : " + eElement.getElementsByTagName("Ends").item(0).getTextContent());
	           
	            auction.setStarted(new Date());
	            auction.setCreated(auction.getStarted());
	            auction.setEnds(new Date());
	            Element sElement = (Element) eElement.getElementsByTagName("Seller").item(0);
	            
	            if (userDAO.countByUsername(sElement.getAttribute("UserID")) == 0) {
		            User user = new User();
		            user.setUsername(sElement.getAttribute("UserID"));
		            user.setSellerRating(Integer.parseInt(sElement.getAttribute("Rating")));
		            user.setPassword("password");
		            user.setEmail(sElement.getAttribute("UserID") + "@gmail.com");
		            user.setRemember(false);
		            user.setActivation(true);
		            user.setSuperuser(false);
		            user.setName(sElement.getAttribute("UserID"));
		            user.setSurname("Jones");
		            user.setAddress("St Pauls 89");
		            user.setCountry("Italy");
		            user.setTelephone("11880");
		            user.setTrn("666");
		            user.setLocation("Venezia");
		            user.setBidderRating(0);
		            user.setAvatar(new Avatar("./img/avatars/avatar0.png"));
		            userDAO.save(user);
		            auction.setUser_seller_id(user);
	            }
	            else {
	            	User user = userDAO.findByUsername(sElement.getAttribute("UserID"));
	            	user.setSellerRating(Integer.parseInt(sElement.getAttribute("Rating")));
	            	userDAO.save(user);
	            	auction.setUser_seller_id(user);
	            }
	            
	            auction.setDescription(eElement.getElementsByTagName("Description").item(0).getTextContent());
	            auctionDAO.save(auction);
//	            System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
	        }
	    }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}

