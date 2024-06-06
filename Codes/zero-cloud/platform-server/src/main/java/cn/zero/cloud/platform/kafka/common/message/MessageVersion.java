package cn.zero.cloud.platform.kafka.common.message;

/**
 * @author Xisun Wang
 * @since 2024/3/8 17:07
 */
public class MessageVersion {
    public static final MessageVersion V1 = new MessageVersion(1.0);

    public static final MessageVersion V2 = new MessageVersion(2.0);

    public static final MessageVersion V3 = new MessageVersion(3.0);

    public static final MessageVersion V4 = new MessageVersion(4.0);

    public static final MessageVersion V5 = new MessageVersion(5.0);

    public static final MessageVersion V6 = new MessageVersion(6.0);

    public static final MessageVersion V7 = new MessageVersion(7.0);

    public static final MessageVersion V8 = new MessageVersion(8.0);

    public static final MessageVersion V9 = new MessageVersion(9.0);

    public static final MessageVersion V10 = new MessageVersion(10.0);

    private final double value;

    private MessageVersion(double value) {
        this.value = value;
    }

    public double getValue() {
        return value;
    }

    public boolean isGreaterThan(double version) {
        return this.value > version;
    }

    public boolean isLessThan(double version) {
        return this.value < version;
    }

    public boolean isGreaterThanOrEquals(double version) {
        return this.value >= version;
    }

    public boolean isLessThanOrEquals(double version) {
        return this.value <= version;
    }

    @Override
    public String toString() {
        int versionNum = (int) getValue();
        return "V" + versionNum;
    }
}
