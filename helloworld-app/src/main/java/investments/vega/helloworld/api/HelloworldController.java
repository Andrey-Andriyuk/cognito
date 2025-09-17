package investments.vega.helloworld.api;

import static org.springframework.http.ResponseEntity.ok;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloworldController {

  @GetMapping("/")
  public ResponseEntity<String> helloworld() {
    return ok("Hello, world!");
  }
}
