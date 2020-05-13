package viewer.service.impl;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import viewer.Main;
import viewer.controller.ImageViewController;
import viewer.service.ImageViewSerivce;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by PanD
 */

public class ImageViewSerivceImpl implements ImageViewSerivce {

    @Override
    public void openImageViewStage(List<File> imageList, File firstFile) {
        Stage imagerViewStage = new Stage();
        imagerViewStage.setTitle("Picture Viewer");
        imagerViewStage.getIcons().add(new Image("file:resources/images/title.png"));
        imagerViewStage.setMinWidth(800);
        imagerViewStage.setMinHeight(600);

        //加载控制器
        ImageViewController imageViewController = new ImageViewController(imageList, firstFile, imagerViewStage);
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("view/ImageView.fxml"));
            loader.setController(imageViewController);
            AnchorPane pictureOverview = (AnchorPane) loader.load();

            Scene scene = new Scene(pictureOverview);
            imagerViewStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }

        imagerViewStage.show();
    }
}
