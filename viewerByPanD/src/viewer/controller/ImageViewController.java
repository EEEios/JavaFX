package viewer.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        adjustImageView();

        //根据Stage比例调整imageView
        stageListener();

        //未悬浮时隐藏按钮
        nextButton.setOpacity(0);
        previousButton.setOpacity(0);
        pageButtonListener();
    }

//监听--------------------------------------------------------------------------

    //Stage属性相关
    public void stageListener() {
        parentStage.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                adjustImageView();
            }
        });
        parentStage.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                adjustImageView();
            }
        });
    }

    //翻页按钮相关
    public void pageButtonListener() {

        //下一页 ---------------------------------------------------------------
        pageButtonReaction(nextButton);

        //前一页 ---------------------------------------------------------------
        pageButtonReaction(previousButton);
    }

//Action -----------------------------------------------------------------------

    //下一张
    public void nextButton() {
        nextPage();
    }

    //前一张
    public void previousButton() {
        previousPage();
    }

//内部方法 ------------------------------------------------------------------------

    //下一页
    private void nextPage() {
        if ((++currentIndex) == images.size()) {
            currentIndex = 0;
        }
        imageView.setImage(images.get(currentIndex));

        adjustImageView();
    }

    //上一页
    private void previousPage() {
        if ((--currentIndex) == -1) {
            currentIndex = images.size() - 1;
        }
        imageView.setImage(images.get(currentIndex));

        adjustImageView();
    }

    //翻页按钮的操作，用同一模块，抽离成方法
    private void pageButtonReaction(Button pageButton) {
        //悬浮显示
        pageButton.setOnMouseEntered(event -> {
            pageButton.setOpacity(1);
        });

        //离开隐藏
        pageButton.setOnMouseExited(event -> {
            pageButton.setOpacity(0);
        });
    }

    //根据窗口大小调整ImageView
    private void adjustImageView() {
        if (parentStage.widthProperty().doubleValue() < parentStage.heightProperty().doubleValue()) {
            imageView.setFitWidth(parentStage.widthProperty().doubleValue() * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
        }
        if (parentStage.heightProperty().doubleValue() < parentStage.widthProperty().doubleValue()) {
            imageView.setFitHeight(parentStage.heightProperty().doubleValue() * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
        }
    }


//构造器 ------------------------------------------------------------------------

    //传入需要展示的图片队列，首张图片，展示窗口
    public ImageViewController(List<File> imageFileList, File firstFile,Stage stage) {
        this.imageFileList = imageFileList;
        this.firstFile = firstFile;
        this.parentStage = stage;
    }

//getter & setter --------------------------------------------------------------

}
