package eu.trixner.base.server.auth.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TokenHandler {
    private static final Logger log = LoggerFactory.getLogger(TokenHandler.class);

    private static List<String> blackList = new ArrayList<>();

    public static List<String> getBlackList() {
        return blackList;
    }

    /**
     * Runs every minute
     */
    @Scheduled(fixedRateString = "${jwt.token.removeStaleTokenRate}")
    public void cleanUp() {
        List<String> toDelete = blackList.stream().filter(JwtUtils::isExpired).collect(Collectors.toList());
        log.debug("Removing {} old Session Tokens", toDelete.size());
        blackList.removeAll(toDelete);
    }
}
