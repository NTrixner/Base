package eu.trixner.base.server.auth.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component
public class TokenHandler {
    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

    private static HashMap<String, Authentication> tokens = new HashMap<>();

    public static HashMap<String, Authentication> getTokens() {
        return tokens;
    }

    /**
     * Runs every minute
     */
    @Scheduled(fixedRateString = "${jwt.token.removeStaleTokenRate}")
    public void cleanUp() {
        log.info("Removing old Session Tokens");
        tokens.entrySet().removeIf(entry -> JwtUtils.isExpired(entry.getKey()));
    }
}
