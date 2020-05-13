package viewer.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import viewer.constants.ImagePreviewConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
    private Button nextButton;
    @FXML
    private Button playButton;

    @FXML
    private StackPane imagePane;
    @FXML
    private ImageView imageView;

    private Stage parentStage;

    //第一个要显示的图片，构造方法使用
    private File firstFile;
    //当前的选中的图片列表
    private List<File> imageFileList;
    private List<Image> images;

    //存入map，以便选中image时的file操作
    private HashMap<Image, File> imageMap;

    //当前选中图片的索引
    private int currentIndex;

// 初始化 -----------------------------------------------------------------------
    @FXML
    public void initialize() {
        //图片列表的初始化和装载
        currentIndex = 0;
        images = new ArrayList<>();
        imageMap = new HashMap<>();

        imageFileList.forEach(file -> {
            Image image = new Image("file:" + file.getPath(),0,0,true,true);
            images.add(image);
            imageMap.put(image,file);
            if (file.equals(firstFile)) {
                currentIndex = images.indexOf(image);
            }
        });
        imageView.setImage(images.get(currentIndex));

        //组件动态变换设置
        //imageView保持比例
        imageView.setPreserveRatio(true);
        parentStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < parentStage.heightProperty().doubleValue()) {
                    imageView.setFitWidth(newValue.doubleValue() * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
                }
            }
        });
        parentStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                if (newValue.doubleValue() < parentStage.widthProperty().doubleValue()) {
                    imageView.setFitHeight(newValue.doubleValue() * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
                }
            }
        });
    }

//Action -----------------------------------------------------------------------
    //下一张
    public void nextButton() {
        if ((++currentIndex) == images.size()) {
            currentIndex = 0;
        }
        imageView.setImage(images.get(currentIndex));
    }

    //前一张
    public void previousButton() {
        if ((--currentIndex) == -1) {
            currentIndex = images.size() - 1;
        }
        imageView.setImage(images.get(currentIndex));
    }
//构造器 ------------------------------------------------------------------------

    public ImageViewController(List<File> imageFileList, File firstFile,Stage stage) {
        this.imageFileList = imageFileList;
        this.firstFile = firstFile;
        this.parentStage = stage;
    }

//getter & setter --------------------------------------------------------------

}
