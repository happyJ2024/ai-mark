package cn.airesearch.aimarkserver.tool;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SplitPDFPage {
    private String pdfPath;
    private List<Integer> pages = new ArrayList<>();
}
