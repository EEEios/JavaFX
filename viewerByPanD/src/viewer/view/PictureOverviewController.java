package viewer.view;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.util.Callback;
import viewer.model.DirTreeItem;

import javax.swing.filechooser.FileSystemView;
import java.io.File;

/**
 * Created by PanD
 */

public class PictureOverviewController {

    @FXML
    private TreeView<File> dirTree;
    @FXML
    private Label stateLabel;
    @FXML
    private Button slidPlayButton;

    @FXML
    public void initialize() {

        initDirTree();
    }

    public void initDirTree() {
        DirTreeItem root = new DirTreeItem(new File("root"), true);
        root.load();
        root.setExpanded(true);

        dirTree.setRoot(root);
        dirTree.setShowRoot(false);
        dirTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


        //设置TreeView单元样式
        dirTree.setCellFactory(new Callback<TreeView<File>, TreeCell<File>>() {
            @Override
            public TreeCell<File> call(TreeView<File> param) {
                return new TreeCell<File>() {
                    @Override
                    protected void updateItem(File item, boolean empty) {
                        super.updateItem(item, empty);
                        setFont(new Font("Microsoft YaHei", 14.0));
                        if (!empty) {
                            ImageView icon = null;
                            icon = new ImageView(new Image("file:resources/images/folder-solid.png", 20, 20, true, true)); // 磁盘
                            setGraphic(icon);

                            String name = FileSystemView.getFileSystemView().getSystemDisplayName(item);
                            setText(name);
                        } else {
                            setText(null);
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        //选项选中事件
        dirTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue,
                                TreeItem<File> newValue) {
                if (newValue == null) {
                    return;
                }
                //newValue为选中值
                System.out.println(newValue);

//                viewerPane.setSelectedFolder(newValue.getValue()); // 将选中目录设置到viewerPane
            }

        });

        //加载root下子节点
        for (TreeItem<File> item :  root.getChildren()) {
            ((DirTreeItem) item).load();
        }
    }
}
