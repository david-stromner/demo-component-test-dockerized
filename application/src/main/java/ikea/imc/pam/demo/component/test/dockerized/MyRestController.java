package ikea.imc.pam.demo.component.test.dockerized;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
public class MyRestController {
    @GetMapping("/getStatus")
    public ResponseEntity getStatus() {
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
