package viewer.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

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
    private AnchorPane imagePane;
    @FXML
    private ImageView imageView;

    private List<File> imageFileList;
    private List<Image> images;
    private HashMap<Image, File> imageMap;
    private File firstFile;
    private int currentIndex;

// 初始化 --------------------------------------------------------------------
    @FXML
    public void initialize() {
        int count = 0;
        currentIndex = 0;
        images = new ArrayList<>();
        imageMap = new HashMap<>();

        imageFileList.forEach(file -> {
            Image image = new Image("file:" + file.getPath());
            images.add(image);
            imageMap.put(image,file);
            if (file.equals(firstFile)) {
                System.out.println("搜到了" + file + firstFile);
                currentIndex = images.indexOf(image);
            }
        });
        imageView.setImage(images.get(currentIndex));
    }

//Action -----------------------------------------------------------------------
    public void nextButton() {
        if ((++currentIndex) == images.size()) {
            currentIndex = 0;
        }
        imageView.setImage(images.get(currentIndex));
    }

    public void previousButton() {
        if ((--currentIndex) == images.size()) {
            currentIndex = 0;
        }
        imageView.setImage(images.get(currentIndex));
    }
//构造器 ------------------------------------------------------------------------

    public ImageViewController(List<File> imageFileList, File firstFile) {
        this.imageFileList = imageFileList;
        this.firstFile = firstFile;
    }

//getter & setter --------------------------------------------------------------

}
