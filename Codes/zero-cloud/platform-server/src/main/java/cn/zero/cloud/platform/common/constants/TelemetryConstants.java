package cn.zero.cloud.platform.common.constants;

/**
 * @author Xisun Wang
 * @since 2024/3/14 16:38
 */
public class TelemetryConstants {
    public static final String AOP_METHOD_IS_NULL = "AOP Method is null!";

    public static final String MULTIPLE_VALUES = "Multiple values are not recommended!";

    public static final String MULTIPLE_MAPPINGS = "Multiple mappings are not recommended!";

    private interface NameAware {
        String getName();
    }

    public enum ModuleType implements NameAware {
        DEFAULT_BLANK(""),

        TEST_API("TEST_API"),

        COMMON_API("COMMON_API"),

        KAFKA_API("KAFKA_API"),

        ;

        private final String name;

        ModuleType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum MetricType implements NameAware {
        DEFAULT_BLANK(""),

        TEST_METRIC("TEST_METRIC"),

        COMMON_METRIC("COMMON_METRIC"),

        KAFKA_METRIC("KAFKA_METRIC"),

        THREAD_POOL_TASK_EXECUTE("THREAD_POOL_TASK_EXECUTE"),

        THREAD_POOL_WORK_STATE("THREAD_POOL_WORK_STATE"),

        ;

        private final String name;

        MetricType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum FeatureType implements NameAware {
        DEFAULT_BLANK(""),

        TEST_FEATURE("TEST_FEATURE"),

        COMMON_FEATURE("COMMON_FEATURE"),

        KAFKA_FEATURE("KAFKA_FEATURE"),

        THREAD_POOL_TASK_EXECUTE("THREAD_POOL_TASK_EXECUTE"),

        THREAD_POOL_WORK_STATE("THREAD_POOL_WORK_STATE"),

        ;

        private final String name;

        FeatureType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum ObjectType implements NameAware {
        DEFAULT_BLANK(""),

        TEST_OBJECT("TEST_OBJECT"),

        COMMON_OBJECT("COMMON_OBJECT"),

        KAFKA_OBJECT("KAFKA_OBJECT"),

        ;

        private final String name;

        ObjectType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum VerbType implements NameAware {
        DEFAULT_BLANK(""),

        CREATE("CREATE"),

        UPDATE("UPDATE"),

        DELETE("DELETE"),

        SELECT("SELECT"),

        ;

        private final String name;

        VerbType(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    public enum ProcessResult implements NameAware {
        SUCCESS("SUCCESS"),

        FAILURE("FAILURE"),

        ;

        private final String name;

        ProcessResult(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return this.name;
        }
    }
}
