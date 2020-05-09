package viewer.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
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
    private Label pathLabel;
    @FXML
    private FlowPane previewPane;

    protected SimpleObjectProperty<File> selectedDir;

    public void setSelectedDir(File selectedDir) {
        this.selectedDir.set(selectedDir);
    }

    @FXML
    public void initialize() {

        initDirTree();
        initTopOfPreview();

    }

    public void initDirTree() {
        DirTreeItem root = new DirTreeItem(new File("root"), true);
        root.load();
        root.setExpanded(true);

        dirTree.setRoot(root);
        dirTree.setShowRoot(false);
        dirTree.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        //加载根节点的子节点，例如C:、D:..
        for (TreeItem<File> item :  root.getChildren()) {
            ((DirTreeItem) item).load();
        }
        //设置初始TreeView单元样式----------------------------------------------------------------------
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
                            if (!this.getTreeItem().isExpanded()) {
                                if (((DirTreeItem) getTreeItem().getParent()).isRoot()) {
                                    icon = new ImageView(new Image("file:resources/images/portable-power-solid.png", 16, 16, true, true)); // 磁盘
                                } else {
                                    icon = new ImageView(new Image("file:resources/images/folder-solid.png", 16, 16, true, true)); // 磁盘
                                }
                            } else {
                                if (((DirTreeItem) getTreeItem().getParent()).isRoot()) {
                                    icon = new ImageView(new Image("file:resources/images/portable-power-solid (1).png", 16, 16, true, true)); // 磁盘
                                } else {
                                    icon = new ImageView(new Image("file:resources/images/folder-open-solid.png", 16, 16, true, true)); // 磁盘
                                }
                            }
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

        //选项选中事件----------------------------------------------------------------------
        dirTree.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<TreeItem<File>>() {
            @Override
            public void changed(ObservableValue<? extends TreeItem<File>> observable, TreeItem<File> oldValue,
                                TreeItem<File> newValue) {
                if (newValue == null) {
                    return;
                }
                //newValue为选中值
                //TODO 右侧页面要显示
                System.out.println(newValue);
                selectedDir.setValue(newValue.getValue());
            }
        });

        //展开/收起事件----------------------------------------------------------------------
//        //展开之后改变节点样式
//        root.addEventHandler(DirTreeItem.<File>branchExpandedEvent(), new EventHandler<DirTreeItem.TreeModificationEvent<File>>() {
//            @Override
//            public void handle(DirTreeItem.TreeModificationEvent<File> event) {
//
//                System.out.println(event.getTreeItem().getValue()+"open");
//            }
//        });
//
//        //收起之后改变节点样式
//        root.addEventHandler(DirTreeItem.<File>branchCollapsedEvent(), new EventHandler<DirTreeItem.TreeModificationEvent<File>>() {
//            @Override
//            public void handle(DirTreeItem.TreeModificationEvent<File> event) {
//                System.out.println(event.getTreeItem().getValue()+"close");
//            }
//        });

    }

    public void initTopOfPreview() {
        selectedDir = new SimpleObjectProperty<File>();
        selectedDir.addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
                pathLabel.setText(newValue.getPath());
            }
        });
    }

}
