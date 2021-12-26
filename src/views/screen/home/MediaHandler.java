package views.screen.home;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Logger;

import common.exception.MediaNotAvailableException;
import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.media.Media;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.Utils;
import views.screen.FXMLScreenHandler;
import views.screen.popup.PopupScreen;

public class MediaHandler extends FXMLScreenHandler{

    @FXML
    protected ImageView mediaImage;

    @FXML
    protected Label mediaTitle;

    @FXML
    protected Label mediaPrice;

    @FXML
    protected Label mediaAvail;

    @FXML
    protected Spinner<Integer> spinnerChangeNumber;

    @FXML
    protected Button addToCartBtn;

    @FXML
    protected Button btnSubtract;

    @FXML
    protected Button btnAdd;

    @FXML
    protected TextField textQuantily;

    private static Logger LOGGER = Utils.getLogger(MediaHandler.class.getName());
    private Media media;
    private HomeScreenHandler home;
    private int quantily;
    public MediaHandler(String screenPath, Media media, HomeScreenHandler home) throws SQLException, IOException{
        super(screenPath);
        this.quantily = Integer.valueOf(textQuantily.getText());
        this.media = media;
        this.home = home;
        addToCartBtn.setOnMouseClicked(event -> {
            try {
                if (quantily > media.getQuantity()) throw new MediaNotAvailableException();
                Cart cart = Cart.getCart();
                // if media already in cart then we will increase the quantity by 1 instead of create the new cartMedia
                CartMedia mediaInCart = home.getBController().checkMediaInCart(media);
                if (mediaInCart != null) {
                    mediaInCart.setQuantity(mediaInCart.getQuantity() + 1);
                }else{
                    CartMedia cartMedia = new CartMedia(media, cart, quantily, media.getPrice());
                    cart.getListMedia().add(cartMedia);
                    LOGGER.info("Added " + cartMedia.getQuantity() + " " + media.getTitle() + " to cart");
                }

                // subtract the quantity and redisplay
                media.setQuantity(media.getQuantity() - quantily);
                mediaAvail.setText(String.valueOf(media.getQuantity()));
                home.getNumMediaCartLabel().setText(String.valueOf(cart.getTotalMedia() ));
                PopupScreen.success("The media " + media.getTitle() + " added to Cart");
            } catch (MediaNotAvailableException exp) {
                try {
                    String message = "Not enough media:\nRequired: " + quantily + "\nAvail: " + media.getQuantity();
                    LOGGER.severe(message);
                    PopupScreen.error(message);
                } catch (Exception e) {
                    LOGGER.severe("Cannot add media to cart: ");
                }
                
            } catch (Exception exp) {
                LOGGER.severe("Cannot add media to cart: ");
                exp.printStackTrace();
            }
        });
        setMediaInfo();
    }

    public Media getMedia(){
        return media;
    }

    private void setMediaInfo() throws SQLException {
        // set the cover image of media
        File file = new File(media.getImageURL());
        Image image = new Image(file.toURI().toString());
        mediaImage.setFitHeight(160);
        mediaImage.setFitWidth(152);
        mediaImage.setImage(image);

        mediaTitle.setText(media.getTitle());
        mediaPrice.setText(Utils.getCurrencyFormat(media.getPrice()));
        mediaAvail.setText(Integer.toString(media.getQuantity()));
        textQuantily.setText("1");
//        spinnerChangeNumber.setValueFactory(
//            new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 1)
//        );

        setImage(mediaImage, media.getImageURL());
    }

    public void addQuantity(){
        quantily++;
        textQuantily.setText(String.valueOf(quantily));
    }

    public void subQuantity(){
        quantily--;
        textQuantily.setText(String.valueOf(quantily));
    }
}
