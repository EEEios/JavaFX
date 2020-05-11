package viewer.model;

import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import viewer.constants.ImagePreviewConstant;

import java.io.File;

/**
 * Created by PanD
 */

public class ImagePreViewItem extends VBox {

    private File imageFile;

    private FlowPane parentPane;

    private Label nameLabel;

    private Canvas canvas;

    public ImagePreViewItem(File imageFile, FlowPane parentPane) {
        super();
        this.imageFile = imageFile;
        this.parentPane = parentPane;
        nameLabel = new Label();
        this.setHeight(ImagePreviewConstant.VBOX_HEIGHT);
        this.setWidth(ImagePreviewConstant.VBOX_WIDTH);
        initImagePreview();
    }

    private void initImagePreview() {
        canvas = new Canvas(ImagePreviewConstant.VBOX_HEIGHT, ImagePreviewConstant.VBOX_WIDTH);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        Image image = new Image("file:" + imageFile.getPath(),
                ImagePreviewConstant.VBOX_WIDTH-4, ImagePreviewConstant.VBOX_HEIGHT-4,
                true, true);
        gc.drawImage(image,
                (ImagePreviewConstant.VBOX_WIDTH - image.getWidth())/2,
                (ImagePreviewConstant.VBOX_HEIGHT - image.getHeight())/2);

        nameLabel.setText(imageFile.getName());
        nameLabel.setPrefWidth(ImagePreviewConstant.VBOX_WIDTH);
        nameLabel.setPrefHeight(20);
        nameLabel.setAlignment(Pos.CENTER);
        nameLabel.setTooltip(new Tooltip(imageFile.getName()));


        this.getChildren().add(canvas);
        this.getChildren().add(nameLabel);
    }
}
