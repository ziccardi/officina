package it.garage;

public class OperationNotSupportedException extends Exception {
    public OperationNotSupportedException (String msg) {
        super(msg);
    }
}
