package cn.edu.scnu.Interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CustomInterceptor implements HandlerInterceptor{
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 检查请求的URL是否包含alipay相关的路径
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/alipay/") || requestURI.endsWith("/alipay")) {
            // 如果是alipay相关的接口，则不执行拦截逻辑，直接返回true
            return true;
        }

        // 如果不是alipay接口，可以执行其他的拦截逻辑，比如权限检查等
        // ...

        // 如果请求通过拦截，返回true；否则返回false，将不会进入后续的Controller
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        // 处理完请求后的逻辑（可选）
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 整个请求处理完毕后的逻辑（可选）
    }
}
