package in.woowa.platform.osori.admin.config.exception;

/**
 * Error 타입에 대한 Interface
 */
public interface Err {

    String code();

    String message();

    String message(Object... args);


}
