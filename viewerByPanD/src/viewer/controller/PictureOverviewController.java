package viewer.controller;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleSetProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.FlowPane;
import javafx.scene.text.Font;
import javafx.util.Callback;
import viewer.model.DirTreeItem;
import viewer.model.ImagePreViewItem;
import viewer.service.ContextMenuService;
import viewer.service.ImageViewSerivce;
import viewer.service.ServiceFactory;
import viewer.utils.ConvertUtil;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PanD
 */

public class PictureOverviewController {

    public ContextMenuService contextMenuService = ServiceFactory.getContextMenuService();
    public ImageViewSerivce imageViewSerivce = ServiceFactory.getImageViewSerivce();


    @FXML
    private FlowPane previewPane;
    @FXML
    private ScrollPane scrollPane;
    //目录树
    @FXML
    private TreeView<File> dirTree;
    //上下文菜单
    @FXML
    private ContextMenu contextMenu;

    @FXML
    private MenuItem copyMenuItem;
    @FXML
    private MenuItem cutMenuItem;
    @FXML
    private MenuItem pasteMenuItem;
    @FXML
    private MenuItem renameMenuItem;
    @FXML
    private MenuItem selectAllMenuItem;
    @FXML
    private MenuItem openMenuItem;

    //页面下方说明选中的Label
    @FXML
    private Label stateLabel;
    //路径导航栏的Label
    @FXML
    private Label pathLabel;
    //幻灯片播放
    @FXML
    private Button slidPlayButton;
    //返回上级目录
    @FXML
    private Button backToParentFileButton;

    //被选中的目录
    private SimpleObjectProperty<File> selectedDir;

    //当前目录载入的缩略图
    private SimpleSetProperty<ImagePreViewItem> imagePreViewSet;
    private SimpleSetProperty<ImagePreViewItem> selectedImagePreViewSet;

//初始化-----------------------------------------------------------------------------------
    @FXML
    public void initialize() {

        this.imagePreViewSet = new SimpleSetProperty<>(FXCollections.observableSet());
        this.selectedImagePreViewSet = new SimpleSetProperty<>(FXCollections.observableSet());
        this.selectedDir = new SimpleObjectProperty<File>();

        initDirTree();
        initPreview();
    }

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
        pathLabel.setText("");
        pathLabelListener();
        selectImageListener();
    }

    /**
     * description: 载入当前目录的图片的缩略图
     * @param images
     * @return void
     */
    private void loadPicture(File[] images) {
        imagePreViewSet.clear();
        for (File image : images) {
            ImagePreViewItem ipItem = new ImagePreViewItem(image, this);
            imagePreViewSet.add(ipItem);

            //在页面载入缩略图
            this.getPreviewPane().getChildren().add(ipItem);
        }
    }

//监听 ------------------------------------------------------------------------------------
    /**
     * description: 对路径进行监听
     * @param
     * @return void
     */
    private void pathLabelListener() {
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
                stateLabel.setText(String.format("共 %d 张图片 |", images.length));
                previewPane.getChildren().clear();

                //加载图片
                loadPicture(images);
            }
        });
    }

    /**
     * description: 对预览图片区域鼠标事件的监听
     * @param
     * @return void
     */
    private void selectImageListener() {

        previewPane.setOnMouseClicked(event -> {

            if (event.getPickResult().getIntersectedNode() == previewPane) {
                //点击空白位置
                //点击左键取消掉所有选中
                if (event.getButton() == MouseButton.PRIMARY){
                    PictureOverviewController.this.getImagePreViewSet().forEach(image -> {
                        image.setIsSelected(false);
                    });
                    PictureOverviewController.this.getSelectedImagePreViewSet().clear();
                }
                //点击右键打开对应的上下文菜单
                if (event.getButton() == MouseButton.SECONDARY){
                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(pasteMenuItem, selectAllMenuItem);
                    contextMenu.show(previewPane, event.getScreenX(), event.getScreenY());
                }
            } else {
                if (event.getButton() == MouseButton.SECONDARY){
                    contextMenu.getItems().clear();
                    contextMenu.getItems().addAll(openMenuItem, copyMenuItem, cutMenuItem, pasteMenuItem, renameMenuItem, selectAllMenuItem);
                    contextMenu.show(previewPane, event.getScreenX(), event.getScreenY());
                }
            }

        });

        //监听选中列表，改变左下角 statLabel 的值
        selectedImagePreViewSet.addListener(new ChangeListener<ObservableSet<ImagePreViewItem>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableSet<ImagePreViewItem>> observable, ObservableSet<ImagePreViewItem> oldValue, ObservableSet<ImagePreViewItem> newValue) {
                int selected = PictureOverviewController.this.selectedImagePreViewSetProperty().size();
                if (selected == 0){
                    stateLabel.setText(String.format("共 %d 张图片 |",PictureOverviewController.this.imagePreViewSetProperty().size()));
                } else {
                    stateLabel.setText(String.format("共 %d 张图片 | %d 张被选中 |",PictureOverviewController.this.imagePreViewSetProperty().size(), selected));
                }
            }
        });
    }

//按钮/菜单Action (为 public 以便 fxml 能够读取) ------------------------------------------------------
    /**
     * description: 按钮：返回上级目录
     * @param
     * @return void
     */
    public void backToParentDirectory() {
        //没有选择目录时返回上级
        if (selectedDir == null || selectedDir.getValue() == null) {
            return;
        }
        //选择目录返回上级时
        if (selectedDir.getValue().getParentFile() != null) {
            setSelectedDir(selectedDir.getValue().getParentFile());
//            System.out.println("parent file:" + selectedDir.getValue().getPath());
        }
    }

    /**
     * description: 右键菜单：全选
     * @param
     * @return void
     */
    public void menuItemOfSelectAll() {
        selectedImagePreViewSet.clear();
        imagePreViewSet.forEach(imagePreViewItem -> {
            imagePreViewItem.setIsSelected(true);
            selectedImagePreViewSet.add(imagePreViewItem);
        });
    }

    /**
     * description: 打开选中的文件
     * @param
     * @return
     */
    public void menuItemOfOpen() {
        List<File> selectedFiles = ConvertUtil.simpleSetPropertyToList(selectedImagePreViewSetProperty());
        imageViewSerivce.openImageViewStage(selectedFiles, selectedFiles.get(0));
    }

    /**
     * description: 重命名文件
     * @param
     * @return void
     */
    public void menuItemOfRename(List<File> fileList) {
        contextMenuService.rename(fileList);

    }

    /**
     * description: 复制文件
     * @param
     * @return void
     */
    public void menuItemOfCopy(List<File> fileList) {
        contextMenuService.copy(fileList);
    }

    /**
     * description: 粘贴文件
     * @param
     * @return void
     */
    public void menuItemOfPaste(String path) {

        contextMenuService.paste(stateLabel.getText());
    }

    /**
     * description: 剪切文件
     * @param
     * @return void
     */
    public void menuItemOfCut(List<File> fileList) {
        contextMenuService.cut(fileList);
    }

//getter & setter ------------------------------------------------------------------------
    public void setSelectedDir(File selectedDir) {
        this.selectedDir.set(selectedDir);
    }

    public FlowPane getPreviewPane() {
        return previewPane;
    }

    public ObservableSet<ImagePreViewItem> getImagePreViewSet() {
        return imagePreViewSet.get();
    }

    public SimpleSetProperty<ImagePreViewItem> imagePreViewSetProperty() {
        return imagePreViewSet;
    }

    public ObservableSet<ImagePreViewItem> getSelectedImagePreViewSet() {
        return selectedImagePreViewSet.get();
    }

    public SimpleSetProperty<ImagePreViewItem> selectedImagePreViewSetProperty() {
        return selectedImagePreViewSet;
    }
}
