package cn.zero.cloud.platform.thread.local.context;

/**
 * @author Xisun Wang
 * @since 2024/4/15 12:19
 */
public class WbxServerContext {
    private WbxServerContext() {}

    private static final ThreadLocal<WbxServerContextToken> CONTEXT_HOLDER = new ThreadLocal<>();

    public static void clearContext() {
        CONTEXT_HOLDER.remove();
    }

    public static WbxServerContextToken getContext() {
        return CONTEXT_HOLDER.get();
    }

    public static void setContext(WbxServerContextToken context) {
        CONTEXT_HOLDER.set(context);
    }
}
