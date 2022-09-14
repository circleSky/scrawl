package com.cennavi.doodle.image;

public class ImageCacheKeyGenerator {
    public ImageCacheKeyGenerator() {
    }

    public String generateCacheKey(int[] size, String path, ImageLoaderConfig config) {
        int width = size[0];
        int height = size[1];
        String key = config.isLoadOriginal() ? path + "_" + config.isAutoRotate() + "_" + config.isExtractThumbnail() : "" + path + "=" + width + "_" + height + "_" + config.isAutoRotate() + "_" + config.isExtractThumbnail();
        return key;
    }
}
