package viewer.model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.VBox;
import viewer.constants.ImagePreviewConstant;
import viewer.controller.PictureOverviewController;

import java.io.File;

/**
 * Created by PanD
 */

public class ImagePreViewItem extends VBox {

    private File imageFile;

    private PictureOverviewController pictureOverviewController;

//VBOX中的组件 -------------------------------------------------------------------
    private Canvas canvas;

    private Label nameLabel;

    private SimpleBooleanProperty isSelected;

//初始化 -------------------------------------------------------------------
    public ImagePreViewItem(File imageFile, PictureOverviewController pictureOverviewController) {
        super();
        this.setHeight(ImagePreviewConstant.VBOX_HEIGHT);
        this.setWidth(ImagePreviewConstant.VBOX_WIDTH);

        this.imageFile = imageFile;
        this.pictureOverviewController = pictureOverviewController;

        nameLabel = new Label();
        isSelected = new SimpleBooleanProperty(false);

        initImagePreview();
        initMouseEvent();
        initPropertyListener();
    }

    /**
     * description: 初始化缩略图
     *
     * @param
     * @return void
     */
    private void initImagePreview() {
        canvas = new Canvas(ImagePreviewConstant.VBOX_HEIGHT, ImagePreviewConstant.VBOX_WIDTH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image("file:" + imageFile.getPath(),
                ImagePreviewConstant.VBOX_WIDTH - 10, ImagePreviewConstant.VBOX_HEIGHT - 10,
                true, true);
        gc.drawImage(image,
                (ImagePreviewConstant.VBOX_WIDTH - image.getWidth()) / 2,
                (ImagePreviewConstant.VBOX_HEIGHT - image.getHeight()) / 2);

        nameLabel.setText(imageFile.getName());
        nameLabel.setPrefWidth(ImagePreviewConstant.VBOX_WIDTH);
        nameLabel.setPrefHeight(20);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTooltip(new Tooltip(imageFile.getName()));

        this.getChildren().add(canvas);
        this.getChildren().add(nameLabel);
    }

    /**
     * description: 为缩略图添加鼠标事件
     *
     * @param
     * @return void
     */
    private void initMouseEvent() {
        //悬浮
        this.setOnMouseEntered(event -> {
            //内部类方法使用外部类，需要使用 外部类名.this 进行映射
            //渐变使用：ImagePreViewItem.this.setStyle("-fx-background-color:linear-gradient(to bottom,#000000 1%,  #ffffff 98%);");
            if (isIsSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:#E6E6E6;");
            }
        });

        //离开
        this.setOnMouseExited(event -> {
            if (isIsSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
            }
        });

        //选中
        this.setOnMouseClicked(event -> {
            //左键对应操作
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                //没有ctrl进行多选
                if (event.isControlDown() == false) {
                    //清除所有选中的图片
                    ImagePreViewItem.this.pictureOverviewController
                            .selectedImagePreViewSetProperty().clear();
                    //将所有图片设置为 未选中
                    ImagePreViewItem.this.pictureOverviewController
                            .getImagePreViewSet().forEach(loadedImage -> {
                        loadedImage.setIsSelected(false);
                    });
                }
                //单击，会取消掉其他的选择状态
                if (isIsSelected() == false) {
                    setIsSelected(true);
                    ImagePreViewItem.this.pictureOverviewController.selectedImagePreViewSetProperty().add(ImagePreViewItem.this);
                } else {
                    setIsSelected(false);
                    ImagePreViewItem.this.pictureOverviewController.selectedImagePreViewSetProperty().remove(ImagePreViewItem.this);
                }
            }

            //TODO 双击打开图片
            if (event.getClickCount() >= 2 && event.getButton() == MouseButton.PRIMARY) {
                System.out.println("点了两次了");
            }

            //右键菜单
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {

            }
        });
    }

    /**
     * description: 对部分属性设置监听
     * @param
     * @return void
     */
    private void initPropertyListener() {
        //对是否被选中进行监听
        isSelectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (isIsSelected() == true) {
                    ImagePreViewItem.this.setStyle("-fx-background-color:#c6c6c6;");
                } else {
                    ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
                }
            }
        });
    }

//getter & setter -------------------------------------------------------------------
    public boolean isIsSelected() {
        return isSelected.get();
    }

    public SimpleBooleanProperty isSelectedProperty() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected.set(isSelected);
    }
}