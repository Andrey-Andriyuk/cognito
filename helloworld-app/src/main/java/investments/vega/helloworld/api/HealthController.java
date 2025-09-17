package investments.vega.helloworld.api;

import static org.springframework.http.ResponseEntity.ok;

import investments.vega.common.api.HealthInternalApi;
import io.micrometer.observation.annotation.Observed;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Observed
public class HealthController implements HealthInternalApi {

  @Override
  public ResponseEntity<Health> healthCheck() {
    return ok(Health.up().build());
  }
}
