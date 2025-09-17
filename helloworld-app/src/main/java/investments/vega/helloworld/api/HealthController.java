package investments.vega.helloworld.api;

import static org.springframework.http.ResponseEntity.ok;

import io.micrometer.observation.annotation.Observed;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Observed
public class HealthController {

  @GetMapping("/health")
  public ResponseEntity<Health> healthCheck() {
    return ok(Health.up().build());
  }
}
