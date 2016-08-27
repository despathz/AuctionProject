package Auction_Project.AuctionProject.ws.image;

import java.io.FileOutputStream;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import Auction_Project.AuctionProject.dao.AvatarDAO;
import Auction_Project.AuctionProject.dao.UserDAO;
import Auction_Project.AuctionProject.dto.avatar.saveAvatarResponse;
import Auction_Project.AuctionProject.ws.user.User;

@RestController
@RequestMapping("/ws/avatar")

public class AvatarController {
	
	@Autowired
	private UserDAO userDAO;

	@Autowired
	private AvatarDAO avatarDAO;	
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean upload(@RequestBody saveAvatarResponse image) {
		
		try {
			String[] parts;
			String fileType, data;
			byte[] decoded;
			FileOutputStream fos;
			Avatar img = new Avatar();
			long user_id = image.getId();
			User user = userDAO.findById(user_id);
			
			img.setUser(user);
			
			if (!image.getImgA().equals("")) {
				parts = image.getImgA().split(";base64,");
				fileType = parts[0];
				data = parts[1];
				fileType = fileType.replace("data:image/", "");
				decoded = Base64.getDecoder().decode(data);
				fos = new FileOutputStream("./src/main/resources/static/img/avatars/imgA" + user_id + "." + fileType);
				fos.write(decoded);
				fos.close();
				img.setImgPath("./img/avatars/imgA" + user_id + "." + fileType);
			}
			else {
				img.setImgPath("");
			}
			user.setAvatar(img);
			
//			Avatar deleteAvatar = new Avatar(img.getImgPath());
//			avatarDAO.delete(deleteAvatar);
			avatarDAO.save(img);
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return true;
	}
	
	@RequestMapping(value = "/get/{user_id}", method = RequestMethod.GET)
	public String get(@PathVariable long user_id) {
		
		String imgPath = "";
		try {
			User user = userDAO.findById(user_id);
			Avatar image = avatarDAO.findByUserId(user);
			imgPath = image.getImgPath();
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return imgPath;
	}

}
