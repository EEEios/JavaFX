package viewer.service.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import viewer.Main;
import viewer.service.ImageViewSerivce;

import java.io.IOException;

/**
 * Created by PanD
 */

public class ImageViewSerivceImpl implements ImageViewSerivce {

    @Override
    public void openImageViewStage() {
        Stage imagerViewStage = new Stage();
        imagerViewStage.setTitle("Picture Viewer");
        imagerViewStage.getIcons().add(new Image("file:resources/images/title.png"));

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ImageView.fxml"));
            AnchorPane pictureOverview = (AnchorPane) loader.load();

            Scene scene = new Scene(pictureOverview);
            imagerViewStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagerViewStage.show();
    }

}
