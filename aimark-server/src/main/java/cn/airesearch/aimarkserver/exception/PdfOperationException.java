package cn.airesearch.aimarkserver.exception;

/**
 * @author ZhangXi
 */
public class PdfOperationException extends Exception {

    public PdfOperationException(String message) {
        super(message);
    }

    public PdfOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
