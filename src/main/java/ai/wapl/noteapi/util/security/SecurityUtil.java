package ai.wapl.noteapi.util.security;

import ai.wapl.noteapi.util.jwt.CustomPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

@Slf4j
public class SecurityUtil {

    private SecurityUtil() {
    }

    public static Optional<Long> getAccountId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(principal.getAccountId());
    }

    public static Optional<UUID> getUserId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        String userId = principal.getUserId();

        // if (userId == null) {
        // throw new RuntimeException("Security Context 에 User ID가 없습니다.");
        // }

        UUID userUUID = null;
        if (userId != null) {
            userUUID = UUID.fromString(userId);
        }
        return Optional.ofNullable(userUUID);
    }

    public static Optional<Long> getGroupId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
        }

        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();

        return Optional.ofNullable(principal.getGroupId());
    }

    // TODO: getRoles가 필요한지 검증 필요
    // public static List<GrantedAuthority> getRoles() {
    // final Authentication authentication =
    // SecurityContextHolder.getContext().getAuthentication();
    //
    // if (authentication == null || authentication.getAuthorities() == null) {
    // throw new RuntimeException("Security Context 에 인증 정보가 없습니다.");
    // }
    //
    // return new ArrayList<>(authentication.getAuthorities());
    //
    // }
}
