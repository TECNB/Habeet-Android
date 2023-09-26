package Item;

public class StoreItem {
    private String storeName;
    private String storeDescribe;

    private String storePoint;

    private String storeHour;
    private String storeMinute;

    private boolean isDetailsVisible; // 新增的字段

    // 构造函数和其他方法...

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }

    public void setDetailsVisible(boolean visible) {
        isDetailsVisible = visible;
    }

    public StoreItem(String storeName, String storeDescribe,String storePoint,String storeHour,String storeMinute) {
        this.storeName = storeName;
        this.storeDescribe = storeDescribe;
        this.storePoint = storePoint;
        this.storeHour = storeHour;
        this.storeMinute = storeMinute;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreDescribe() {
        return storeDescribe;
    }

    public String getStorePoint() {
        return storePoint;
    }

    public String getStoreHour() {
        return storeHour;
    }

    public String getStoreMinute() {
        return storeMinute;
    }
}
