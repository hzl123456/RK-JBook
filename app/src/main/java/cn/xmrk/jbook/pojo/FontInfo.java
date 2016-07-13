package cn.xmrk.jbook.pojo;

/**
 * 字体的信息
 */
public class FontInfo {
    /**
     * id : 100008
     * display_name : 小丸子
     * resource_url : http://7xj62a.com1.z0.glb.clouddn.com/v1/font/makuro.ttf
     * thumbnail_url : {"x2":"http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@2x.png","x3":"http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@3x.png","x4":"http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@4x.png"}
     */

    private int id;
    private String display_name;
    private String resource_url;
    /**
     * x2 : http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@2x.png
     * x3 : http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@3x.png
     * x4 : http://7xj62a.com1.z0.glb.clouddn.com/v1/thumbnail/font-label-makuro@4x.png
     */

    private ThumbnailUrlBean thumbnail_url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDisplay_name() {
        return display_name;
    }

    public void setDisplay_name(String display_name) {
        this.display_name = display_name;
    }

    public String getResource_url() {
        return resource_url;
    }

    public void setResource_url(String resource_url) {
        this.resource_url = resource_url;
    }

    public ThumbnailUrlBean getThumbnail_url() {
        return thumbnail_url;
    }

    public void setThumbnail_url(ThumbnailUrlBean thumbnail_url) {
        this.thumbnail_url = thumbnail_url;
    }

    public static class ThumbnailUrlBean {
        private String x2;
        private String x3;
        private String x4;

        public String getX2() {
            return x2;
        }

        public void setX2(String x2) {
            this.x2 = x2;
        }

        public String getX3() {
            return x3;
        }

        public void setX3(String x3) {
            this.x3 = x3;
        }

        public String getX4() {
            return x4;
        }

        public void setX4(String x4) {
            this.x4 = x4;
        }
    }
}
