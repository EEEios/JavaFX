package viewer.service.impl;

import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import viewer.service.ContextMenuService;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 */

public class ContextMenuServiceImpl implements ContextMenuService {

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
    public void paste(String path) {

    }

    @Override
    public void cut(List<File> fileList) {

    }
}
