package Auction_Project.AuctionProject.ws.image;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.AuctionDAO;
import Auction_Project.AuctionProject.dao.ImageDAO;
import Auction_Project.AuctionProject.dto.image.saveImageResponse;
import Auction_Project.AuctionProject.ws.auction.Auction;

@RestController
@RequestMapping("/ws/image")
public class AuctionImageController {
	
	@Autowired
	private ImageDAO imageDAO;
	
	@Autowired
	private AuctionDAO auctionDAO;
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean upload(@RequestBody saveImageResponse image) {
		
		try {
			String[] parts;
			String fileType, data;
			byte[] decoded;
			FileOutputStream fos;
			AuctionImage img = new AuctionImage();
			long auction_id = image.getId();
			boolean flag = false;
			Auction auction = auctionDAO.findById(auction_id);
			
			img.setAuctionId(auction);
			
			if (!image.getImgA().equals("")) {
				parts = image.getImgA().split(";base64,");
				fileType = parts[0];
				data = parts[1];
				fileType = fileType.replace("data:image/", "");
				decoded = Base64.getDecoder().decode(data);
				fos = new FileOutputStream("./src/main/resources/static/img/auction_images/imgA" + auction_id + "." + fileType);
				fos.write(decoded);
				fos.close();
				img.setImgPath("./img/auction_images/imgA" + auction_id + "." + fileType);
			}
			else {
				flag = true;
				img.setImgPath("");
			}
			
			imageDAO.save(img);
			
			img = new AuctionImage();
			img.setAuctionId(auction);
			
			if (!image.getImgB().equals("")) {
				parts = image.getImgB().split(";base64,");
				fileType = parts[0];
				data = parts[1];
				fileType = fileType.replace("data:image/", "");
				decoded = Base64.getDecoder().decode(data);
				fos = new FileOutputStream("./src/main/resources/static/img/auction_images/imgB" + auction_id + "." + fileType);
				fos.write(decoded);
				fos.close();
				img.setImgPath("./img/auction_images/imgB" + auction_id + "." + fileType);
			}
			else {
				if (flag == true)	//imgA is also emtpy
					img.setImgPath("./img/auction_images/imgA0.jpg");
				else
				img.setImgPath("");
			}
			
			imageDAO.save(img);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/get/{auction_id}", method = RequestMethod.GET)
	public List<String> get(@PathVariable long auction_id) {
		List<String> returnList = new ArrayList<String>();
		try {
			Auction auction = auctionDAO.findById(auction_id);
			List<AuctionImage> images = imageDAO.findByAuctionId(auction);
			for (int i = 0; i < images.size(); i++) {
				AuctionImage img = images.get(i);
				returnList.add(img.getImgPath());
			}
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return returnList;
	}

}
