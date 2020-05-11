package viewer.model;

import javafx.beans.property.SimpleSetProperty;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
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

    //VBOX中的组件----------------
    private Canvas canvas;

    private Label nameLabel;

    private boolean isSelected;

    public ImagePreViewItem(File imageFile, PictureOverviewController pictureOverviewController) {
        super();
        this.setHeight(ImagePreviewConstant.VBOX_HEIGHT);
        this.setWidth(ImagePreviewConstant.VBOX_WIDTH);

        this.imageFile = imageFile;
        this.pictureOverviewController = pictureOverviewController;

        nameLabel = new Label();
        isSelected = false;

        initImagePreview();
        initMouseEvent();
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
//            渐变使用：ImagePreViewItem.this.setStyle("-fx-background-color:linear-gradient(to bottom,#000000 1%,  #ffffff 98%);");
            if (isSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:#A2A2A2;");
            }
        });

        //离开
        this.setOnMouseExited(event -> {
            if (isSelected() == false) {
                ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
            }
        });

        //选中
        this.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                //取消其他选中状态
                if (event.isControlDown()) {
                } else {

                    if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                        if (isSelected() == false) {
                            setSelected(true);
                            ImagePreViewItem.this.pictureOverviewController.selectedImagePreViewSetProperty().add(ImagePreViewItem.this);
                            ImagePreViewItem.this.setStyle("-fx-background-color:#A2A2A2;");
                        } else {
                            setSelected(false);
                            ImagePreViewItem.this.pictureOverviewController.selectedImagePreViewSetProperty().remove(ImagePreViewItem.this);
                            ImagePreViewItem.this.setStyle("-fx-background-color:transparent;");
                        }
                    }
                    if (event.getClickCount() == 1 && event.getButton() == MouseButton.SECONDARY) {

                    }
                    if (event.getClickCount() >= 2 && event.getButton() == MouseButton.PRIMARY) {

                    }
                }
            }
        });
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
