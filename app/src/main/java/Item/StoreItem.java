package Item;

public class StoreItem {
    private String storeName;
    private String storeDescription;

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

    public StoreItem(String storeName, String storeDescription) {
        this.storeName = storeName;
        this.storeDescription = storeDescription;
    }

    public String getStoreName() {
        return storeName;
    }

    public String getStoreDescription() {
        return storeDescription;
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
