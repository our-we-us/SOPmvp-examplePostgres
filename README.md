# Prepare

## Create Project

**e.g. examplePostgres**

- with Spring Initializr

### Added dependencies

- [x] Lombok
- [x] Spring Web
- [ ] Axon Framework Spring Support
- [ ] Spring Data JPA
- [ ] PostgreSQL Driver

## Prepare Axon Server

- install Axon Server [https://axoniq.io/product-overview/axon-server]
- **cd ~/AxonServer-4.5.9**

### Start Axon Server

```
java -jar axonserver.jar
```

### Open AxonDashboard

http://localhost:8024/

---

## Step By Step

### All in ---> **com.example.examplepostgres**

1. Create class **rest.NurseController.java**

```java
@RestController
@RequestMapping("/nurse")
public class NurseController {

    @PostMapping
    public String createNurse(@RequestBody CreateNurseRestModel model) {
        return "create : " + model.getFirstname();
    }
}
```

2. Create class **rest.CreateNurseRestModel.java**

```java
@Data
public class CreateNurseRestModel {
    private String NID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
```

3. Run and Test Web Service

```json
### POST http://localhost:8080/nurse

{
    "NID": "{{$randomUUID}}",
    "firstname": "{{$randomFirstName}}",
    "lastname": "{{$randomLastName}}",
    "username": "{{$randomUserName}}",
    "password": "{{$randomPassword}}"
}

```

### Event Sourcing by Axon Server

4. Add Axon Framework Spring Support

```
// pom.xml

<!-- https://mvnrepository.com/artifact/org.axonframework/axon-spring -->
<dependency>
    <groupId>org.axonframework</groupId>
    <artifactId>axon-spring</artifactId>
    <version>4.5.5</version>
</dependency>

```

- Added dependencies
  - [x] Lombok
  - [x] Spring Web
  - [x] Axon Framework Spring Support
  - [ ] Spring Data JPA
  - [ ] PostgreSQL Driver

5. Create class **command.CreateNurseCommand**

```java
@Builder
@Data
public class CreateNurseCommand {
    @TargetAggregateIdentifier
    private final String id;
    private final String NID;
    private final String firstname;
    private final String lastname;
    private final String username;
    private final String password;
}
```

6. Edit class **NurseController.java**

```java
@RestController
@RequestMapping("/nurse")
public class NurseController {

    private final CommandGateway commandGateway;

    @Autowired
    public NurseController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createNurse(@RequestBody CreateNurseRestModel model) {
        CreateNurseCommand command = CreateNurseCommand.builder()
                .id(UUID.randomUUID().toString())
                .NID(model.getNID())
                .firstname(model.getFirstname())
                .lastname(model.getLastname())
                .username(model.getUsername())
                .password(model.getPassword())
                .build();
        String result;
        try {
            result = commandGateway.sendAndWait(command);
        }catch (Exception e) {
            result = e.getLocalizedMessage();
        }
        return result;
    }
}
```

7. Create class **command.NurseAggregate.java**

```java
@Aggregate
public class NurseAggregate {
    @AggregateIdentifier
    private String id;
    private String NID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;

    public NurseAggregate() {
    }

    @CommandHandler
    public NurseAggregate(CreateNurseCommand createNurseCommand) {

        // just example, have more!
        if(createNurseCommand.getNID() == null || createNurseCommand.getNID().isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }
    }
```

8. Create class **event.NurseCreatedEvent.java**

```java
@Data
public class NurseCreatedEvent {
    private String id;
    private String NID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;
}
```

9. Edit class **NurseAggregate.java**

```java
@Aggregate
public class NurseAggregate {
    @AggregateIdentifier
    private String id;
    private String NID;
    private String firstname;
    private String lastname;
    private String username;
    private String password;

    public NurseAggregate() {
    }

    @CommandHandler
    public NurseAggregate(CreateNurseCommand createNurseCommand) {
        // just example, have more!
        if(createNurseCommand.getNID() == null || createNurseCommand.getNID().isBlank()) {
            throw new IllegalArgumentException("ID cannot be empty");
        }

        NurseCreatedEvent nurseCreatedEvent = new NurseCreatedEvent();
        BeanUtils.copyProperties(createNurseCommand,nurseCreatedEvent);
        AggregateLifecycle.apply(nurseCreatedEvent);
    }

    @EventSourcingHandler
    public void on(NurseCreatedEvent nurseCreatedEvent) {
        this.id = nurseCreatedEvent.getId();
        this.NID = nurseCreatedEvent.getNID();
        this.firstname = nurseCreatedEvent.getFirstname();
        this.lastname = nurseCreatedEvent.getLastname();
        this.username = nurseCreatedEvent.getUsername();
        this.password = nurseCreatedEvent.getPassword();
    }
}
```

10. Start Axon Server

```
java -jar axonserver.jar
```

11. Run and Test Web Service ---> See Result on AxonDashboard
