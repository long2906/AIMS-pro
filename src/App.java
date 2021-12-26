import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.FXMLScreenHandler;
import views.screen.home.*;

public class App extends Application  {

	private Stage stage;

	@Override
	public void start(Stage primaryStage) {
		this.stage = primaryStage;
		try {
			StackPane splashScene = initSplashScene();

			// Load splash screen with fade in effect
			FadeTransition fade = initEffect(1, splashScene);
			runEffect(fade);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private StackPane initSplashScene() throws IOException{
		stage.initStyle(StageStyle.UNDECORATED);

		// initialize the scene
		StackPane root = (StackPane) FXMLLoader.load(getClass().getResource(Configs.SPLASH_SCREEN_PATH));
		stage.setScene(new Scene(root));
		stage.show();
		return root;
	}

	private void runEffect(FadeTransition fade){
		fade.play();
		fade.setOnFinished((e) -> {
			try {
				HomeScreenHandler homeScreenHandler = new HomeScreenHandler(stage, Configs.HOME_PATH);
				homeScreenHandler.show();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
	}

	private FadeTransition initEffect(double time, StackPane root){
		FadeTransition fade = new FadeTransition(Duration.seconds(time), root);
		fade.setFromValue(0.5);
		fade.setToValue(1);
		fade.setCycleCount(1);
		return fade;
	}

	public static void main(String[] args) {
		launch(args);
	}


}
