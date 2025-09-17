package investments.vega.helloworld.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import jakarta.servlet.http.HttpServletRequest;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http,
      ObjectProvider<ClientRegistrationRepository> clientRegistrations,
      ObjectProvider<JwtDecoder> jwtDecoderProvider)
      throws Exception {
    http.authorizeHttpRequests(
            auth ->
                auth.requestMatchers("/actuator/health", "/actuator", "/actuator/**", "/health")
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .csrf(csrf -> csrf.disable());

    if (clientRegistrations.getIfAvailable() != null) {
      DefaultOAuth2AuthorizationRequestResolver defaultResolver =
          new DefaultOAuth2AuthorizationRequestResolver(
              clientRegistrations.getIfAvailable(), "/oauth2/authorization");

      OAuth2AuthorizationRequestResolver forcingOktaResolver =
          new OAuth2AuthorizationRequestResolver() {
            @Override
            public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
              OAuth2AuthorizationRequest req = defaultResolver.resolve(request);
              return customize(req);
            }

            @Override
            public OAuth2AuthorizationRequest resolve(
                HttpServletRequest request, String clientRegistrationId) {
              OAuth2AuthorizationRequest req =
                  defaultResolver.resolve(request, clientRegistrationId);
              return customize(req);
            }

            private OAuth2AuthorizationRequest customize(OAuth2AuthorizationRequest req) {
              if (req == null) {
                return null;
              }
              Map<String, Object> extra = new HashMap<>(req.getAdditionalParameters());
              // Routing to a specific IdP
              //extra.put("identity_provider", "Okta");
              return OAuth2AuthorizationRequest.from(req).additionalParameters(extra).build();
            }
          };
      http.oauth2Login(o -> o.authorizationEndpoint(a -> a.authorizationRequestResolver(forcingOktaResolver)));
    }

    if (jwtDecoderProvider.getIfAvailable() != null) {
      http.oauth2ResourceServer(resource -> resource.jwt(withDefaults()));
    }

    return http.build();
  }
}
