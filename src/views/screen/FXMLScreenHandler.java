package views.screen;

import java.io.File;
import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

public class FXMLScreenHandler {

	protected Parent content;

	public FXMLScreenHandler(String screenPath) throws IOException {
		content = FXMLLoader.load(getClass().getResource(screenPath));
		// Set this class as the controller
//		this.loader.setController(this);
	}

	public Parent getContent() {
		return this.content;
	}

	public void setImage(ImageView imv, String path){
		File file = new File(path);
		Image img = new Image(file.toURI().toString());
		imv.setImage(img);
	}
}
