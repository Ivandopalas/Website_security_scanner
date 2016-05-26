package com.bsu.famcs.wscan.zap;

public class ZapException  extends RuntimeException{
    public ZapException() {
        super();
    }

    public ZapException(String message) {
        super(message);
    }

    public ZapException(String message, Throwable cause) {
        super(message, cause);
    }

    public ZapException(Throwable cause) {
        super(cause);
    }
}
