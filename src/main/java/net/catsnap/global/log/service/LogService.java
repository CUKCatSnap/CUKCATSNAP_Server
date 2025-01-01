package net.catsnap.global.log.service;

import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Scanner;
import net.catsnap.global.security.authority.CatsnapAuthority;
import net.catsnap.global.security.contextholder.GetAuthenticationInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

@Component
public class LogService {

    private final String LOG_FORMAT
        = "\n[ERROR]\n{}: {} \n[REQUEST URL] ({} {})\n[QUERY STRING] {} \n[REQUEST BODY] \n{}  \n[USER TYPE] {} \n[User DB Id] {} \n[CALLED BY] \n{}";
    private final Logger logger = LoggerFactory.getLogger("ErrorLogger");

    private final Map<Level, LogFunction> logFunctions = Map.of(
        Level.INFO, logger::info,
        Level.WARN, logger::warn,
        Level.ERROR, logger::error
    );

    public void log(Level level, Exception e, HttpServletRequest request) throws IOException {
        CatsnapAuthority catsnapAuthority = GetAuthenticationInfo.getAuthority();
        Long userId = null;
        if (catsnapAuthority != CatsnapAuthority.ANONYMOUS) {
            userId = GetAuthenticationInfo.getUserId();
        }
        logFunctions.get(level)
            .log(LOG_FORMAT, e.getClass().getSimpleName(), e.getMessage(), request.getMethod(),
                request.getRequestURI(), getQueryString(request), getRequestBody(request),
                catsnapAuthority.name(), userId, e.getStackTrace()[0]);
    }

    private String getQueryString(HttpServletRequest request) {
        return request.getQueryString() == null ? "null" : request.getQueryString();
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        ContentCachingRequestWrapper cachedRequest = (ContentCachingRequestWrapper) request;
        String requestBody = new String(cachedRequest.getContentAsByteArray(), "UTF-8");
        if (requestBody.isEmpty()) {
            try (InputStream inputStream = request.getInputStream(); Scanner scanner = new Scanner(
                inputStream,
                StandardCharsets.UTF_8)) {
                if (scanner.useDelimiter("\\A").hasNext()) {
                    requestBody = scanner.next();
                } else {
                    requestBody = "null";
                }
            }
        }
        return requestBody;
    }

    @FunctionalInterface
    interface LogFunction {

        void log(String message, Object... args);
    }
}
