package viewer.service.impl;

import javafx.beans.property.SimpleListProperty;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import viewer.model.ImagePreViewItem;
import viewer.service.FileOperationService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by PanD
 */

public class FileOperationServiceImpl implements FileOperationService {

    @Override
    public void rename(List<File> fileList) {

    }

    @Override
    public void copy(List<File> fileList) {
        if(fileList.size() <=0) {
            return;
        }
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboard.clear();
        clipboardContent.putFiles(fileList);
        clipboard.setContent(clipboardContent);
    }

    @Override
    public List<File> paste(String path) {
        //获取剪切板的内容
        List<File> clipboardFile = getClipboardContent();
        if (clipboardFile.size() <= 0){
            return null;
        }

        //列出当前目录的所有文件
        File[] images = (new File(path)).listFiles(
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
        List<File> fileList = new ArrayList<>();
        Collections.addAll(fileList, images);

        for (File oldFile : clipboardFile) {
            String name = pasteRename(oldFile.getName(), fileList);
            File newFile = new File(path + File.separator + name);
            System.out.println(newFile.getPath());
            try {
                newFile.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            if (newFile.exists()) {
                try {
                    copyFile(oldFile, newFile);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }

        return clipboardFile;
    }

    @Override
    public void cut(List<File> fileList) {
        copy(fileList);
    }

    @Override
    public boolean delete(List<File> fileList) {
        if(this.showDeleteAlert("是否删除选中的图片？", "")) {
            for(File file: fileList) {
                file.delete();
            }
            return true;
        }
        return false;
    }

    /**
     * description: 将文件列表载入剪切板
     * @param fileList
     * @return void
     */
    private void putFileInClipBoard(List<File> fileList) {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        ClipboardContent clipboardContent = new ClipboardContent();
        clipboard.clear();
        clipboardContent.putFiles(fileList);
        clipboard.setContent(clipboardContent);
    }

    /**
     * description: 获取剪切板的内容
     * @param
     * @return java.util.List<java.io.File>
     */
    private List<File> getClipboardContent() {
        Clipboard clipboard = Clipboard.getSystemClipboard();
        return (List<File>) (clipboard.getContent(DataFormat.FILES));
    }

    /**
     * description: 删除时显示的对话框
     * @param header
     * @param message
     * @return boolean
     */
    private boolean showDeleteAlert(String header,String message) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION, header,
                new ButtonType("取消", ButtonBar.ButtonData.NO),
                new ButtonType("确定", ButtonBar.ButtonData.YES));

        alert.setTitle("删除");
        alert.setHeaderText(header);
        Optional<ButtonType> buttonType = alert.showAndWait();

        //根据按键选择结果返回
        if(buttonType.get().getButtonData().equals(ButtonBar.ButtonData.YES)){
            return true;
        } else {
            return false;
        }
    }

    private String pasteRename(String name, List<File> fileList) {
        while (findSameName(name, fileList)) {
            int end = name.lastIndexOf(".");
            name = name.substring(0, end) + "_副本" + name.substring(end);
        }
        return name;
    }

    private boolean findSameName(String name, List<File> fileList) {
        for (File file:fileList) {
            if (name.equals(file.getName())) {
                return true;
            }
        }
        return false;
    }

    private void copyFile(File fromFile, File toFile) throws IOException {
        FileInputStream inputStream = new FileInputStream(fromFile);
        FileOutputStream outputStream = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int byteRead;
        while ((byteRead = inputStream.read(b)) > 0) {
            outputStream.write(b, 0, byteRead);
        }
        inputStream.close();
        outputStream.close();
    }
}
