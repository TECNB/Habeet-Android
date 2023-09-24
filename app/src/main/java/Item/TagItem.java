package Item;

public class TagItem {
    private String tagName;
    private String tagDescription;

    private boolean isDetailsVisible; // 新增的字段

    // 构造函数和其他方法...

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }

    public void setDetailsVisible(boolean visible) {
        isDetailsVisible = visible;
    }

    public TagItem(String tagName, String tagDescription) {
        this.tagName = tagName;
        this.tagDescription = tagDescription;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagDescription() {
        return tagDescription;
    }

}


