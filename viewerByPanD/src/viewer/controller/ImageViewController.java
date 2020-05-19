package viewer.controller;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private ToolBar toolBar;
    @FXML
    private AnchorPane anchorPane;

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

    //容器属性
    private Stage parentStage;

    //第一个要显示的图片，构造方法使用
    private File firstFile;

    //当前的选中的图片列表
    private List<File> imageFileList;
    private List<Image> images;

    //存入map，以便选中image时的file操作
    private HashMap<Image, File> imageMap;

    //当前选中图片的索引
    private SimpleIntegerProperty currentIndex;
    private Image currentImage;
// 初始化 -----------------------------------------------------------------------

    @FXML
    public void initialize() {
        //检测ToolBar高度用的代码
//        Button button = new Button();
//        button.setLayoutX(0);
//        button.setLayoutY(257+43);
//        anchorPane.getChildren().addAll(button);
//        System.out.println("button: " + button.getPrefHeight());
        //图片列表的初始化和装载
        currentIndex = new SimpleIntegerProperty(0);
        images = new ArrayList<>();
        imageMap = new HashMap<>();

        imageFileList.forEach(file -> {
            Image image = new Image("file:" + file.getPath(),0,0,true,true);
            images.add(image);
            imageMap.put(image,file);
            if (file.equals(firstFile)) {
                currentIndex.setValue(images.indexOf(image));
                currentImage = images.get(currentIndex.intValue());
            }
        });
        imageView.setImage(images.get(currentIndex.intValue()));

        //组件动态变换设置
        imageView.setPreserveRatio(true);

        //初始化监听
        initialListener();
//        adjustImageView();
    }

//监听--------------------------------------------------------------------------

    //不包含stageListener的初始化，stageListener初始化需要等待stage的传入装载，见@setParentStage(Stage parentStage, boolean isInitial)
    private void initialListener() {
        //根据Stage比例调整imageView
        //TODO 未悬浮时隐藏按钮
//        pageButtonListener();
        currentImageListener();

    }


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

    //当前图片相关
    public void currentImageListener() {
        currentIndex.addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                currentImage = images.get(newValue.intValue());
            }
        });
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
        int index = currentIndex.getValue().intValue() + 1;
        if (index == images.size()) {
            index = 0;
        }
        currentIndex.setValue(index);
        imageView.setImage(currentImage);

        adjustImageView();
    }

    //上一页
    private void previousPage() {
        int index = currentIndex.getValue().intValue() - 1;
        if (index == -1) {
            index = images.size() - 1;
        }
        currentIndex.setValue(index);
        imageView.setImage(currentImage);

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
    public void adjustImageView() {
        //Toolbar 高度为43,使用下面的命令查询
//            System.out.println("ToolBar: " + toolBar.prefHeight(-1));
        double toolBarHeight = toolBar.prefHeight(-1);
        double scenceHeight = parentStage.getScene().getHeight() - toolBarHeight;
        double scenceWidth = parentStage.getScene().getWidth();
        if (currentImage.getHeight() < scenceHeight
                && currentImage.getWidth() < scenceWidth) {
            imageView.setFitHeight(currentImage.getHeight());
            imageView.setFitWidth(currentImage.getWidth());
        } else {
            if (scenceHeight > scenceWidth) {
                imageView.setFitWidth(scenceWidth * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
            }
            if (scenceHeight < scenceWidth) {
                imageView.setFitHeight(scenceHeight * ImagePreviewConstant.IMAGE_PROPORTION_IN_STAGE);
                System.out.println(imageView.getFitHeight());
            }
        }
    }

//构造器 ------------------------------------------------------------------------

    //传入需要展示的图片队列，首张图片，展示窗口
    public ImageViewController(List<File> imageFileList, File firstFile) {
        this.imageFileList = imageFileList;
        this.firstFile = firstFile;
    }

//getter & setter --------------------------------------------------------------

    public void setParentStage(Stage parentStage) {
        this.parentStage = parentStage;
//        this.isInitialStage.set(isInitial);
        stageListener();
    }
}
