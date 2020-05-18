package viewer.service;

import java.io.File;
import java.util.List;

/**
 * Created by PanD
 */

public interface FileOperationService {

    void rename(List<File> fileList);

    void copy(List<File> fileList);

    void paste(String path);

    void cut(List<File> fileList);
}
