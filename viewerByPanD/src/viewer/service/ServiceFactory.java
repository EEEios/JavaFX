package viewer.service;

import viewer.service.impl.FileOperationServiceImpl;
import viewer.service.impl.ImageViewSerivceImpl;

/**
 * Created by PanD
 * Service工厂，用于提供Serivce服务
 */


public class ServiceFactory {

    private static FileOperationService fileOperationService = new FileOperationServiceImpl();

    private static ImageViewSerivce imageViewSerivce = new ImageViewSerivceImpl();

    public static FileOperationService getFileOperationService() {
        return fileOperationService;
    }

    public static ImageViewSerivce getImageViewSerivce() {
        return imageViewSerivce;
    }
}
