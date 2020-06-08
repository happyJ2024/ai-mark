package cn.airesearch.aimarkserver.tool;

public class PDFTextObject {
    public int pageNumber;
    public float x;
    public float y;
    public String text;

    @Override
    public String toString() {
        return "PDFTextObject{" +
                "pageNumber=" + pageNumber +
                ", x=" + x +
                ", y=" + y +
                ", text='" + text + '\'' +
                '}';
    }
}
