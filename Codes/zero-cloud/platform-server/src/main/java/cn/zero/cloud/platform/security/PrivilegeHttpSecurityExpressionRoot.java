package cn.zero.cloud.platform.security;

import cn.zero.cloud.platform.security.authentication.AuthenticationUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

/**
 * @author Xisun Wang
 * @since 6/18/2024 13:49
 */
public class PrivilegeHttpSecurityExpressionRoot extends SecurityExpressionRoot {
    public final HttpServletRequest request;

    public PrivilegeHttpSecurityExpressionRoot(Authentication authentication, RequestAuthorizationContext context) {
        super(authentication);
        this.request = context.getRequest();
    }

    public boolean isLoggedIn() {
        return AuthenticationUtil.isUserLogin();
    }

    public boolean hasIpAddress(String ipAddress) {
        return (new IpAddressMatcher(ipAddress)).matches(this.request);
    }
}
