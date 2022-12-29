package eu.trixner.base.server.auth.filters;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class TokenHandler {

    private static final List<String> blackList = new ArrayList<>();

    public static List<String> getBlackList() {
        return blackList;
    }

    public final JwtUtils jwtUtils;


    /**
     * Runs every minute
     */
    @Scheduled(fixedRateString = "${jwt.token.removeStaleTokenRate}")
    public void cleanUp() {
        List<String> toDelete = blackList.stream().filter(jwtUtils::isExpired).toList();
        log.debug("Removing {} old Session Tokens", toDelete.size());
        blackList.removeAll(toDelete);
    }
}
