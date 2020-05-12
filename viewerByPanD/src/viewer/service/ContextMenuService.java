package viewer.service;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 * 2020/5/12 15:10
 */

public interface ContextMenuService {

    void rename(List<File> fileList);

    void copy(List<File> fileList);

    void paste(List<File> fileList);

    void cut(List<File> fileList);
}
