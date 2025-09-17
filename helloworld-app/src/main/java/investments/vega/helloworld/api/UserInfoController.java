package investments.vega.helloworld.api;

import java.security.Principal;
import java.util.Map;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserInfoController {

  @GetMapping("/me")
  public Map<String, Object> me(@AuthenticationPrincipal Object principal) {
    if (principal instanceof OidcUser oidcUser) {
      return Map.of(
          "authType", "OIDC_USER",
          "name", oidcUser.getFullName(),
          "username", oidcUser.getPreferredUsername(),
          "claims", oidcUser.getClaims());
    }
    if (principal instanceof Jwt jwt) {
      return Map.of(
          "authType", "JWT",
          "subject", jwt.getSubject(),
          "claims", jwt.getClaims());
    }
    if (principal instanceof Principal p) {
      return Map.of("authType", "PRINCIPAL", "name", p.getName());
    }
    return Map.of("authType", "ANONYMOUS");
  }
}
