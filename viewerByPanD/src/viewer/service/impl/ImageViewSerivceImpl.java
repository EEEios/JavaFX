package viewer.service.impl;

import javafx.scene.image.Image;
import javafx.stage.Stage;
import viewer.service.ImageViewSerivce;

/**
 * Created by PanD
 */

public class ImageViewSerivceImpl implements ImageViewSerivce {

    @Override
    public void createImageViewStage() {
        Stage imagerViewStage = new Stage();
        imagerViewStage.setTitle("Picture Viewer");
        imagerViewStage.getIcons().add(new Image("file:resources/images/title.png"));
    }

}
