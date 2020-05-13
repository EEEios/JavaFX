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
import viewer.controller.ImageViewController;
import viewer.controller.PictureOverviewController;
import viewer.utils.ConvertUtil;

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
            PictureOverviewController parentController = ImagePreViewItem.this.pictureOverviewController;
            //左键
            if (event.getClickCount() == 1 && (event.getButton() == MouseButton.PRIMARY)) {
                //没有ctrl进行多选
                if (event.isControlDown() == false) {
                    clearAllSelected();
                }
                //单击，会取消掉其他的选择状态
                if (isIsSelected() == false) {
                    setIsSelected(true);
                    parentController.selectedImagePreViewSetProperty().add(ImagePreViewItem.this);
                } else {
                    setIsSelected(false);
                    parentController.selectedImagePreViewSetProperty().remove(ImagePreViewItem.this);
                }
            }

            //双击左键(打开当前目录全部图片
            if (event.getClickCount() == 2 && (event.getButton() == MouseButton.PRIMARY)) {
                parentController.imageViewSerivce.openImageViewStage(
                        ConvertUtil.simpleSetPropertyToList(parentController.imagePreViewSetProperty()),
                                ImagePreViewItem.this.imageFile
                );
            }

            //右键
            if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {
                //右键点击的地方不是选中的文件则相当于 鼠标左键单击后右键打开菜单
                if (parentController.selectedImagePreViewSetProperty().contains(event.getSource()) == false){
                    clearAllSelected();
                    setIsSelected(true);
                    parentController.selectedImagePreViewSetProperty().add(ImagePreViewItem.this);
                }
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

    /**
     * description: 清除当前选中的图片
     * @param
     * @return void
     */
    private void clearAllSelected() {
        //清空选择
        ImagePreViewItem.this.pictureOverviewController
                .selectedImagePreViewSetProperty().clear();
        //将所有图片设置为 未选中
        ImagePreViewItem.this.pictureOverviewController
                .getImagePreViewSet().forEach(loadedImage -> {
            loadedImage.setIsSelected(false);
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

    public File getImageFile() {
        return imageFile;
    }
}
