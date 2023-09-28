package Item;

import android.view.View;

public class TargetItem {
    private String targetName;
    private String targetDescribe;

    private String targetPoint;

    private String deadline;
    private String status;

    private String targetId;
    private boolean isDetailsVisible; // 新增的字段

    private String dayDifference;


    // 其他成员变量
    private int visibilityState1 = View.VISIBLE;
    private int visibilityState2 = View.GONE;

    // 其他方法和构造函数


    public String getDayDifference() {
        return dayDifference;
    }

    public void setDayDifference(String dayDifference) {
        this.dayDifference = dayDifference;
    }

    public int getVisibilityState1() {
        return visibilityState1;
    }

    public void setVisibilityState1(int visibilityState1) {
        this.visibilityState1 = visibilityState1;
    }

    public int getVisibilityState2() {
        return visibilityState2;
    }

    public void setVisibilityState2(int visibilityState2) {
        this.visibilityState2 = visibilityState2;
    }

    public TargetItem(String targetName,String targetDescribe, String targetPoint,String deadline,String targetId,String dayDifference) {
        this.targetName = targetName;
        this.targetDescribe = targetDescribe;
        this.targetPoint = targetPoint;
        this.deadline = deadline;
        this.targetId = targetId;
        this.dayDifference = dayDifference;
    }

    public String getTargetName() {
        return targetName;
    }

    public String getTargetDescribe() {
        return targetDescribe;
    }

    public String getTargetPoint() {
        return targetPoint;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public boolean isDetailsVisible() {
        return isDetailsVisible;
    }

    public String getTargetId() {
        return targetId;
    }
}
