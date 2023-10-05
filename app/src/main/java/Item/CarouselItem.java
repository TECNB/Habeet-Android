package Item;

public class CarouselItem {
    private int imageResId;
    private String text;

    public CarouselItem(int imageResId, String text) {
        this.imageResId = imageResId;
        this.text = text;
    }

    public int getImageResId() {
        return imageResId;
    }

    public String getText() {
        return text;
    }
}

