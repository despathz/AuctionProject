package Auction_Project.AuctionProject.ws.image;

import java.io.FileOutputStream;
import java.util.Base64;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ws/image")
public class AuctionImageController {
	
	@RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public boolean upload(@RequestBody AuctionImage images) {
		try {
			String[] parts = images.getImgA().split(";base64,");
			String fileType = parts[0];
			String data = parts[1];
			fileType = fileType.replace("data:image/", "");
			byte[] decoded = Base64.getDecoder().decode(data);
			FileOutputStream fos = new FileOutputStream("lol." + fileType);
			fos.write(decoded);
			fos.close();
			
			parts = images.getImgB().split(";base64,");
			fileType = parts[0];
			data = parts[1];
			fileType = fileType.replace("data:image/", "");
			decoded = Base64.getDecoder().decode(data);
			fos = new FileOutputStream("lol." + fileType);
			fos.write(decoded);
			fos.close();
		}
		catch (Exception ex){
			System.out.println(ex.getMessage());
		}
		return true;
	}

}
