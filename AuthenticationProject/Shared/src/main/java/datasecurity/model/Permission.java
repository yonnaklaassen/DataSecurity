package datasecurity.model;

import java.io.Serializable;

public enum Permission implements Serializable {
    START,
    STOP,
    RESTART,
    PRINT,
    QUEUE,
    TOP_QUEUE,
    STATUS,
    READ_CONFIG,
    SET_CONFIG

}
