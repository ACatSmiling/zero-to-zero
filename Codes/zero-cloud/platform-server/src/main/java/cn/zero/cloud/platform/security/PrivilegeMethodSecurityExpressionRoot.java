package cn.zero.cloud.platform.security;

import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

/**
 * @author Xisun Wang
 * @since 6/18/2024 13:21
 */
public class PrivilegeMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {
    private Object filterObject;
    private Object returnObject;
    private Object target;

    public PrivilegeMethodSecurityExpressionRoot(Authentication authentication) {
        super(authentication);
    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return returnObject;
    }

    /*public void setTarget(Object target) {
        this.target = target;
    }*/

    @Override
    public Object getThis() {
        return target;
    }

    /**
     * 实现自定义权限逻辑
     *
     * @param scope 权限
     * @return 是否具备权限
     */
    public boolean hasCustomPermission(String scope) {
        if ("get".equals(scope)) {
            return true;
        }
        return false;
    }
}
