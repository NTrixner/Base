package at.ntrixner.base.config;

import com.sun.tools.sjavac.Log;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@Component
public class MyLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    public static final String LOGIN_FAILED_MESSAGE = "Login failed for Request URL %s, remote Address was %s";

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException{
        Log.debug(String.format(LOGIN_FAILED_MESSAGE, request.getRequestURI(), request.getRemoteAddr()));
        super.onAuthenticationFailure(request, response, exception);
    }
}
