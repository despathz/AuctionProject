package Auction_Project.AuctionProject.ws.xml;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import Auction_Project.AuctionProject.dao.CategoryDAO;
import Auction_Project.AuctionProject.ws.category.Category;

import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

@RestController
@RequestMapping("/ws/xml")
public class XML_IO {

	@Autowired
	private CategoryDAO categoryDAO;
	
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

//	            System.out.println("ItemID : " + eElement.getAttribute("ItemID"));
//	            System.out.println("Name : " + eElement.getElementsByTagName("Name").item(0).getTextContent());
	            
	            for (int j = 0; j < eElement.getElementsByTagName("Category").getLength(); j++) {
	            	String cat = (String)eElement.getElementsByTagName("Category").item(j).getTextContent();
		            if (categoryDAO.countByName(cat) == 0) {
		            	Category category = new Category();
		            	category.setName(cat);
		            	categoryDAO.save(category);
		            }
	            }
	            
//	            System.out.println("Currently : " + eElement.getElementsByTagName("Currently").item(0).getTextContent());
//	            System.out.println("First_bid : " + eElement.getElementsByTagName("First_Bid").item(0).getTextContent());
//	            
//	            int bidNumber = Integer.parseInt(eElement.getElementsByTagName("Number_of_Bids").item(0).getTextContent());
//	            System.out.println("Number_of_Bids : " + bidNumber);
//	            
//	            Element bidsElement = (Element) eElement.getElementsByTagName("Bids").item(0);
//	            
//	            for (int j = 0; j < bidNumber; j++) {
//	            	Element bidElement = (Element) bidsElement.getElementsByTagName("Bid").item(j);
//	            	Element bidderElement = (Element) bidElement.getElementsByTagName("Bidder").item(0);
//	            	System.out.println("	Bidder UserID : " + bidderElement.getAttribute("UserID"));
//	            	System.out.println("	Bidder Rating : " + bidderElement.getAttribute("Rating"));
//	            	System.out.println("	Bidder Location : " + bidderElement.getElementsByTagName("Location").item(0).getTextContent());
//	            	System.out.println("	Bidder Country : " + bidderElement.getElementsByTagName("Country").item(0).getTextContent());
//	            	System.out.println("	Time : " + bidElement.getElementsByTagName("Time").item(0).getTextContent());
//	            	System.out.println("	Amount : " + bidElement.getElementsByTagName("Amount").item(0).getTextContent());
//	            	System.out.println("");
//	            }
//	            
//	            Element lElement = (Element) eElement.getElementsByTagName("Location").item(bidNumber);
//	            System.out.println("Location Longitude : " + lElement.getAttribute("Longitude"));
//	            System.out.println("Location Latitude : " + lElement.getAttribute("Latitude"));
//	            System.out.println("Location : " + lElement.getTextContent());
//	            
//	            System.out.println("Country : " + eElement.getElementsByTagName("Country").item(0).getTextContent());
//	            System.out.println("Started : " + eElement.getElementsByTagName("Started").item(0).getTextContent());
//	            System.out.println("Ends : " + eElement.getElementsByTagName("Ends").item(0).getTextContent());
//	            
//	            Element sElement = (Element) eElement.getElementsByTagName("Seller").item(0);
//	            System.out.println("Seller UserID : " + sElement.getAttribute("UserID"));
//	            System.out.println("Seller Rating : " + sElement.getAttribute("Rating"));
//	            
//	            System.out.println("Description : " + eElement.getElementsByTagName("Description").item(0).getTextContent());
	        }
	    }
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
	}
}

