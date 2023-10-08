package Item;

public class TagItem {
    private String tagId;
    private String tagName;
    private String tagDescribe;

    private String tagPoint;

    private String tagHour;
    private String tagMinute;

    private boolean isDetailsVisible; // 新增的字段

    // 构造函数和其他方法...

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }

    public void setDetailsVisible(boolean visible) {
        isDetailsVisible = visible;
    }

    public TagItem(String tagName, String tagDescribe,String tagPoint,String tagHour,String tagMinute,String tagId) {
        this.tagName = tagName;
        this.tagDescribe = tagDescribe;
        this.tagPoint = tagPoint;
        this.tagHour = tagHour;
        this.tagMinute = tagMinute;
        this.tagId=tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public String getTagDescribe() {
        return tagDescribe;
    }

    public String getTagPoint() {
        return tagPoint;
    }

    public String getTagHour() {
        return tagHour;
    }

    public String getTagMinute() {
        return tagMinute;
    }
    public String getTagId() {
        return tagId;
    }

}


