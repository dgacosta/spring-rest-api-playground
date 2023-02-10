# spring-rest-api-playground
Proyecto playground para el curso "Building a REST API with Spring Boot" de Spring Academy.

* Cursos: https://spring.academy/courses
* **"Building a REST API with Spring Boot"**: https://spring.academy/courses/building-a-rest-api-with-spring-boot

## Apuntes

### Spring

~~~
@Autowired
~~~
@Autowired is an annotation that directs Spring to create an object of the requested type.

### Modelo
~~~
public record UserModel(Long id, String name) {}
~~~
Records are classes in Java for storing immutable data. Records carry a fixed set of values – the records components. They have a concise syntax in Java and save you from having to write boilerplate code. The compiler automatically generates a final class inherited from java.lang.Record with the following members:
- a private final field for each record component
- a public constructor with parameters for all fields 
- a set of methods to implement structural equality: equals(), hashCode(), toString()
- a public method for reading each record component

Records are very similar to Kotlin data classes.

#### Test Modelo

~~~
@JsonTest
public class UserJsonTest {}
~~~
The @JsonTest annotation marks the UserJsonTest as a test class which uses the Jackson framework (which is included as part of Spring). This provides extensive JSON testing and parsing support. It also establishes all the related behavior to test JSON objects.

~~~
@Autowired
private JacksonTester<User> json;
~~~
JacksonTester is a convenience wrapper to the Jackson JSON parsing library. It handles serialization and deserialization of JSON objects.

@Autowired is an annotation that directs Spring to create an object of the requested type.

~~~
String json.write(user)
User json.parse(string)
~~~
Deserialization is the reverse process of serialization. It transforms data from a file or byte stream back into an object for your application.  This makes it possible for an object serialized on one platform to be deserialized on a different platform. For example, your client application can serialize an object on Windows while the backend would deserialize it on Linux.

~~~
// Serialization
assertThat(json.write(user)).isStrictlyEqualToJson("expected.json");
assertThat(json.write(user)).hasJsonPathNumberValue("@.id");
assertThat(json.write(user)).extractingJsonPathStringValue("@.name").isEqualTo("Federico");

// Deserialization
assertThat(json.parse(expected)).isEqualTo(new UserModel(1L, "Federico"));
assertThat(json.parseObject(expected).id()).isEqualTo(1L);
assertThat(json.parseObject(expected).name()).isEqualTo("Federico");
~~~

## BasicController

~~~
@GetMapping("/{requestedText}") 
    public ResponseEntity<String> findById(@PathVariable String requestedText) {
        return ResponseEntity.ok(requestedText);
    }
~~~
@GetMapping marks a method as a handler method. 

Method to hande HTTP GET requests that match user/{requestedText} will be handled by this method.

~~~
@RestController
@RequestMapping("/basic/")
~~~
Anotación de clase para indicar que esta será un controlador de peticiones Rest.

@RestController This tells Spring that this class is a Component of type RestController and capable of handling HTTP requests.

@RequestMapping: This is a companion to @RestController that indicates which address requests must have to access this Controller.

### Tests BasicController

~~~
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
~~~
This will start our Spring Boot application and make it available for our test to perform requests to it.

~~~
@Autowired
TestRestTemplate restTemplate;
~~~
We've asked Spring to inject a test helper that’ll allow us to make HTTP requests to the locally running application.

While @Autowired is a form of Spring dependency injection it’s best used only in tests. We'll discuss this in more detail later.

~~~
ResponseEntity<String> response = restTemplate.getForEntity("/basic/", String.class);
assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

~~~
Here we make an HTTP GET request to our application at /basic/.

Next, restTemplate will return a ResponseEntity that we've named response. This is another helpful Spring object that provides valuable information about what happened with our request.





We can inspect many aspects of the response, including the HTTP Response Status code, which we expect to be 200 OK.

### Spring Data

#### Dependencies

~~~
implementation 'org.springframework.data:spring-data-jdbc'
~~~
Spring Data has many implementations for a variety of relational and non-relational database technologies. Spring Data also has several abstractions on top of those technologies. These are commonly called an Object-Relational Mapping framework, or ORM.

Here we'll elect to use Spring Data JDBC. From the Spring Data JDBC documentation:

Spring Data JDBC aims at being conceptually easy...This makes Spring Data JDBC a simple, limited, opinionated ORM.

~~~
testImplementation 'com.h2database:h2'
~~~
Database management frameworks only work if they have a linked database. H2 is a "very fast, open source, JDBC API" SQL database implemented in Java. It works seamlessly with Spring Data JDBC.

This tells Spring Boot to make the H2 database available only when running tests. Eventually we'll need a database outside of a testing context, but not yet.

~~~
public interface UserRepository extends CrudRepository<UserModel, Long> {
}
~~~
This is where we tap into the magic of Spring Data and its data repository pattern.

CrudRepository is an interface supplied by Spring Data. When we extend it (or other sub-Interfaces of Spring Data's Repository), Spring Boot and Spring Data work together to automatically generate the CRUD methods that we need to interact with a database.

We'll use one of these CRUD methods, findById, later in the lab.

Spring Data will automatically configure a database by tests if we provide src/test/resources/schema.sql.

~~~
org.springframework.beans.factory.NoSuchBeanDefinitionException: No qualifying bean of type 'example.cashcard.CashCardRepository' available: expected at least 1 bean which qualifies as autowire candidate. Dependency annotations: {}
~~~

Clues such as NoSuchBeanDefinitionException, No qualifying bean, and expected at least 1 bean which qualifies as autowire candidate tell us that Spring is trying to find a property configured class to provide during the dependency injection phase of Auto Configuration, but none qualify. We can satisfy this DI requirement by implementing the CrudRepository.

### UserController

~~~
private UserRepository userRepository;

public UserController(UserRepository userRepository) {
    this.userRepository = userRepository;
}
~~~

~~~
Optional<UserModel> userOptional = userRepository.findById(requestedId);
~~~
We're calling CrudRepository.findById which returns an Optional. This smart object might or might not contain the CashCard for which we're searching. Learn more about Optional here.

~~~
if (!userOptional.isEmpty()) {
    return ResponseEntity.ok(userOptional.get());
} else {
    return ResponseEntity.notFound().build();
}
~~~
This is how you determine if findById did or did not find the CashCard with the supplied id. If cashCardOptional.isPresent() is true then the repository successfully found the CashCard and we can retrieve it with cashCardOptional.get().  If not, the repository has not found the CashCard.

#### Tests

~~~
DocumentContext documentContext = JsonPath.parse(response.getBody());
~~~
This converts the response String into a JSON-aware object with lots of helper methods.

~~~
Number id = documentContext.read("$.id");
assertThat(id).isNotNull();
assertThat(id).isEqualTo(1);
~~~
We expect that when we request a Cash Card with id of 99 a JSON object will be returned with something in the id field. For now assert that the id is not null.
