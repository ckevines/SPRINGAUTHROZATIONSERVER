package com.example.resourceserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.annotation.Id;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.repository.ListCrudRepository;


@SpringBootApplication
public class ResourceserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceserverApplication.class, args);
	}

}

// @Controller
// @ResponseBody
// class GreetingsController {
// 	private final GreetingsService greetingsService;

//     GreetingsController(GreetingsService greetingsService) {
//         this.greetingsService = greetingsService;
//     }

//     @GetMapping("/hello")
//     Map<String, String> hello() {
//         return this.greetingsService.greet();
//     }
// }
// @Service
// class GreetingsService {
// 	@PreAuthorize("hasAuthority('SCOPE_user.read')")
//     public Map<String, String> greet() {
// 		var jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//         return Map.of("message", "hello, " + jwt.getSubject());
//     }
// }

@Repository
interface CustomerRepository extends ListCrudRepository<Customer, Long> {
}

record Customer(@Id Integer id, String name, String email) {
}


@RestController
@ResponseBody
class MeController {
	@GetMapping("/me")
	Map<String, String> priciple(Principal principal) {
		return Map.of("name", principal.getName());
	}
}