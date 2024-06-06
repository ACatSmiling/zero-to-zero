package cn.zero.cloud.platform.common.constants;

/**
 * @author Xisun Wang
 * @since 2024/4/1 11:59
 */
public enum CustomizeStatusConstants {
    ABC(9001, "ABC"),

    ;

    private final int code;

    private final String description;

    CustomizeStatusConstants(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int value() {
        return this.code;
    }

    public String getDescription() {
        return this.description;
    }
}
