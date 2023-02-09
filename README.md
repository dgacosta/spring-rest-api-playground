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




