package viewer.utils;

import javafx.beans.property.SimpleSetProperty;
import viewer.model.ImagePreViewItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by PanD
 * 进行类型转换的工具类
 */

public class ConvertUtil {

    /**
     * description: 将propertySet转为List，提取其中的File
     * @param selectedImagePreViewSet
     * @return java.util.List<java.io.File>
     */
    public static List<File> simpleSetPropertyToList(SimpleSetProperty<ImagePreViewItem> selectedImagePreViewSet) {
        List<File> fileList = new ArrayList<>();
        selectedImagePreViewSet.forEach(imagePreViewItem -> {
            fileList.add(imagePreViewItem.getImageFile());
        });
        return fileList;
    }
}
