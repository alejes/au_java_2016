package ru.spbau.stepanov;

/**
 * Created by Alexey on 02.05.2016.
 */
public class LightExecutionException extends Exception {
    public LightExecutionException() {
        super();
    }
    public LightExecutionException(Exception ex) {
        super();
        initCause(ex);
    }
}
