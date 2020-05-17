package viewer.service;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 * 2020/5/11 15:33
 */

public interface ImageViewSerivce {

    //打开查看图片页面
    void openImageViewStage(List<File> imageList, File firstFile);
}
