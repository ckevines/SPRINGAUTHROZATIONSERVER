package com.example.resourceapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ResourceappApplication {

	public static void main(String[] args) {
		SpringApplication.run(ResourceappApplication.class, args);
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

//@Repository
//interface CustomerRepository extends CrudRepository<Customer, Integer> {
//}
//
//class Customer {
//    @Id
//    private Integer id;
//    private String name;
//	private String email;
//}
//
//
//
//@RestController
//@ResponseBody
//class MeController {
//	@GetMapping("/me")
//	Map<String, String> principle(Principal principal) {
//		return Collections.singletonMap("name", principal.getName());
//	}}