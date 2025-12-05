package net.catsnap.global.security.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;
import net.catsnap.global.security.authenticationToken.CatsnapAuthenticationToken;
import net.catsnap.global.security.authenticationToken.MemberAuthenticationToken;
import net.catsnap.global.security.authenticationToken.PhotographerAuthenticationToken;
import net.catsnap.global.security.authority.CatsnapAuthority;
import org.springframework.security.core.GrantedAuthority;

@RequiredArgsConstructor
public class JwtAuthTokenAuthenticator implements AuthTokenAuthenticator {

    private final SecretKey secretKey;

    public CatsnapAuthenticationToken authTokenAuthenticate(String jwt)
        throws SignatureException, UnsupportedJwtException, MalformedJwtException, ExpiredJwtException {

        JwtParser jwtParser = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build();
        Claims claims = jwtParser.parseClaimsJws(jwt)
            .getBody();

        String identifier = claims.get("identifier", String.class); // 로그인 시 사용하는 id값
        Long id = claims.get("id", Long.class); // 데이터베이스에서 사용되는 유저의 id값
        List<String> authorities = claims.get("authorities", List.class);

        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        CatsnapAuthenticationToken authenticationToken = null;
        if (authorities.get(0).equals(CatsnapAuthority.MEMBER.name())) {
            grantedAuthorities.add(CatsnapAuthority.MEMBER);
            authenticationToken = new MemberAuthenticationToken(identifier, null,
                grantedAuthorities,
                id);
        } else if (authorities.get(0).equals(CatsnapAuthority.PHOTOGRAPHER.name())) {
            grantedAuthorities.add(CatsnapAuthority.PHOTOGRAPHER);
            authenticationToken = new PhotographerAuthenticationToken(identifier, null,
                grantedAuthorities,
                id);
        } else {
            throw new MalformedJwtException("CatSnap에 등록되지 않은 사용자입니다.");
        }
        return authenticationToken;
    }
}
