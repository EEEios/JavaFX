package viewer.controller;

import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import viewer.model.DirTreeItem;
import viewer.model.ImagePreViewItem;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.FileFilter;

/**
 * Created by PanD
 */

public class PictureOverviewController {

    @FXML
    private FlowPane previewPane;
    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TreeView<File> dirTree;

    @FXML
    private Label stateLabel;
    @FXML
    private Label pathLabel;

    @FXML
    private Button slidPlayButton;
    @FXML
    private Button backToParentFileButton;

    protected SimpleObjectProperty<File> selectedDir;

    protected SimpleIntegerProperty selectedDirIndex;

    public void setSelectedDir(File selectedDir) {
        this.selectedDir.set(selectedDir);
    }

    @FXML
    public void initialize() {
        initDirTree();
        initPreview();

    }

    //初始化-----------------------------------------------------------------------------------

    /**
     * description: 目录树的初始化
     * @param
     * @return void
     */
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
                //observable.getValue().getValue()为选中的目录
                System.out.println(observable.getValue().getValue());
                setSelectedDir(observable.getValue().getValue());
            }
        });

    }

    /**
     * description: 图片预览区域的初始化
     * @param
     * @return void
     */
    public void initPreview() {
        previewPane.prefWidthProperty().bind(scrollPane.widthProperty());

        selectedDir = new SimpleObjectProperty<File>();
        pathLabel.setText("");
        //对文件选择的监听
        selectedDir.addListener(new ChangeListener<File>() {
            @Override
            public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {

                File currentDir = observable.getValue();

                //上方导航栏初变化
                pathLabel.setText(currentDir.getPath());

                //筛选对应的图片文件
                File[] images = currentDir.listFiles(
                        pathname -> {
                            if (pathname.isFile()) {
                                String name = pathname.getName().toLowerCase();
                                if (name.endsWith(".jpg") || name.endsWith(".jpge") || name.endsWith(".gif")
                                        || name.endsWith(".png") || name.endsWith("bmp")) {
                                    return true;
                                }
                            }
                            return false;
                        }
                );
                stateLabel.setText(String.format("已加载 0 张 | 共 %d 张图片", images.length));
                previewPane.getChildren().clear();

                //加载图片
                loadPicture(images);
            }
        });
    }

    private void loadPicture(File[] images) {
        int count = 0;
        for (File image : images) {
            ImagePreViewItem ipItem = new ImagePreViewItem(image, this.getPreviewPane());
            this.getPreviewPane().getChildren().add(ipItem);
            count++;
            stateLabel.setText(String.format("已加载 %d 张 | 共 %d 张图片", count, images.length));
        }
    }

    //按钮Action-------------------------------------------------------------------------------
    public void backToParentDirectory() {
        //没有选择目录时返回上级
        if (selectedDir == null) {
            return;
        }
        //选择目录返回上级时
        if (selectedDir.getValue().getParentFile() != null) {
            setSelectedDir(selectedDir.getValue().getParentFile());
            System.out.println("parent file:" + selectedDir.getValue().getPath());
        }
    }

    public FlowPane getPreviewPane() {
        return previewPane;
    }
}
