package ddwu.mobile.finalproject.ma01_20180988;

import java.io.Serializable;

public class Manual implements Serializable {
    private static final String TAG = "Manual";
    private int step;
    private String content;
    private String imageLink;

    public Manual() {
    }

    public Manual(int step, String content, String imageLink) {
        this.step = step;
        this.content = content;
        this.imageLink = imageLink;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }
}
