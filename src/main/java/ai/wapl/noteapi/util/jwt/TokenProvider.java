package ai.wapl.noteapi.util.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayInputStream;
import java.io.Serializable;
import java.security.Key;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Slf4j
@Component
public class TokenProvider {

    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_TYPE = "bearer";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String TOKEN_ACCOUNT_ID = "accountId";
    private static final String TOKEN_USER_MAP = "userMap";
    private static final String HEADER_USER_ID = "WAPL-User-Id";
    private static final String HEADER_ACCOUNT_ID = "WAPL-Account-Id";

    private final Key key;

    public TokenProvider(@Value("${jwt.secret}") String secretKey) throws CertificateException {
        System.out.println("secretKey = " + secretKey);
        System.out.println("System.getenv(\"SECRET_KEY\") = " + System.getenv("SECRET_KEY"));
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        CertificateFactory cf = CertificateFactory.getInstance("X509");
        X509Certificate certificate = (X509Certificate) cf.generateCertificate(new ByteArrayInputStream(keyBytes));
        this.key = certificate.getPublicKey();
    }

    public Authentication getAuthentication(String accessToken, HttpServletRequest request) {
        // ?????? ?????????
        Claims claims = parseClaims(accessToken);

        // ?????? ????????? uuid
        String uuid = request.getHeader(HEADER_USER_ID);

        // get WAPL data from JWT
        Long accountId = claims.get(TOKEN_ACCOUNT_ID, Long.class);
        Map<String, Object> userMap = claims.get(TOKEN_USER_MAP, Map.class);

        CustomPrincipal principal = new CustomPrincipal();
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        // ?????? ????????? ????????? ??????????????? custom principal ??? authorities ??????
        if (StringUtils.hasText(uuid) && userMap != null && userMap.get(uuid) != null) {

            ObjectMapper mapper = new ObjectMapper();
            MapperObject user = mapper.convertValue(userMap.get(uuid), MapperObject.class);

            principal.setAccountId(accountId);
            principal.setUserId(uuid);
            principal.setGroupId(user.getGroupId());

            authorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE_PREFIX + user.getRole()));
        }
        // ???????????? ????????? ?????? ????????? ??????
        else {
            principal.setAccountId(accountId);
        }
        // ????????? Authentication ?????? ???????????? ??????
        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("????????? JWT ???????????????.");
        } catch (ExpiredJwtException e) {
            log.info("????????? JWT ???????????????.");
        } catch (UnsupportedJwtException e) {
            log.info("???????????? ?????? JWT ???????????????.");
        } catch (IllegalArgumentException e) {
            log.info("JWT ????????? ?????????????????????.");
        }
        return false;
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    @Getter
    static class MapperObject implements Serializable {
        private Long groupId;
        private String role;

    }
}
