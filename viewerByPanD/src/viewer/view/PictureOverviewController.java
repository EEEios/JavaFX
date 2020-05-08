package viewer.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Created by PanD
 */

public class PictureOverviewController {

    @FXML
    private TreeView<String> fileTree;
    @FXML
    private Label stateLabel;
    @FXML
    private Button slidPlayButton;

    @FXML
    public void initialize() {
        TreeItem<String> root = new TreeItem<>("中国");
        TreeItem<String> hlj = new TreeItem<>("黑龙江");
        TreeItem<String> city1 = new TreeItem<>("哈尔滨");
        TreeItem<String> city2 = new TreeItem<>("大庆");

        fileTree.setRoot(root);
        hlj.getChildren().add(city1);
        hlj.getChildren().add(city2);

        root.getChildren().add(hlj);
        fileTree.setShowRoot(false);
    }
}
