package viewer.service;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 */

public interface ImageViewSerivce {

    //打开查看图片页面，进行初始化
    void openImageViewStage(List<File> imageList, File firstFile);
}
