package cn.airesearch.aimarkserver.tool;

public class PDFTextObject {
    public int pageNumber;
    public float x1;
    public float y1;
    public float x2;
    public float y2;
    public String text;

    @Override
    public String toString() {
        return "PDFTextObject{" +
                "pageNumber=" + pageNumber +
                ", x1=" + x1 +
                ", y1=" + y1 +
                ", x2=" + x2 +
                ", y2=" + y2 +
                ", text='" + text + '\'' +
                '}';
    }
}
