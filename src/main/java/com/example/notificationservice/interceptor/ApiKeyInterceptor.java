package com.example.notificationservice.interceptor;



import com.example.notificationservice.entity.dto.ErrorDetail;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    private static final String API_KEY_HEADER = "Key";
    private static final String EXPECTED_API_KEY = "Meesho1234@";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        String apiKey = request.getHeader(API_KEY_HEADER);

        if (apiKey == null || !apiKey.equals(EXPECTED_API_KEY)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

            response.getWriter().write("Authorization Error: You are not authorized to access this resource");
            response.getWriter().flush();

            return false;

        }

        return  true;
    }
}
