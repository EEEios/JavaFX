package viewer.service;

import viewer.service.impl.ContextMenuServiceImpl;
import viewer.service.impl.ImageViewSerivceImpl;

/**
 * Created by PanD
 * Service工厂，用于提供Serivce服务
 */


public class ServiceFactory {

    private static ContextMenuService contextMenuService = new ContextMenuServiceImpl();

    private static ImageViewSerivce imageViewSerivce = new ImageViewSerivceImpl();

    public static ContextMenuService getContextMenuService() {
        return contextMenuService;
    }

    public static ImageViewSerivce getImageViewSerivce() {
        return imageViewSerivce;
    }
}
