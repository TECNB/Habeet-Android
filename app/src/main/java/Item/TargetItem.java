package Item;

public class TargetItem {
    private String targetName;
    private String targetDescribe;

    private String targetPoint;

    private String deadline;
    private String status;
    private boolean isDetailsVisible; // 新增的字段

    public TargetItem(String targetName,String targetDescribe, String targetPoint,String deadline) {
        this.targetName = targetName;
        this.targetDescribe = targetDescribe;
        this.targetPoint = targetPoint;
        this.deadline = deadline;
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
}
