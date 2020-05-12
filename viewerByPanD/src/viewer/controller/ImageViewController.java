package viewer.controller;

import javafx.beans.property.SimpleSetProperty;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import viewer.model.ImagePreViewItem;

import java.awt.*;

/**
 * Created by PanD
 */

/**
 * description: 该控制器不由FXML自动状态生成，
 * 而是在ImageViewService提供了装在fxml和controller的服务，
 * 以便不同的传递参数（即选中的图片）
 */
public class ImageViewController {

    @FXML
    private Button previousButton;
    @FXML
    private Button next;
    @FXML
    private Button play;

    @FXML
    private AnchorPane imagePane;
    @FXML
    private ImageView imageView;

    private SimpleSetProperty<ImagePreViewItem> ImageList;

//构造器 ------------------------------------------------
    public ImageViewController(SimpleSetProperty<ImagePreViewItem> imageList) {
        ImageList = imageList;
    }

//getter & setter --------------------------------------------------------------
    public void setImageList(ObservableSet<ImagePreViewItem> imageList) {
        this.ImageList.set(imageList);
    }
}
