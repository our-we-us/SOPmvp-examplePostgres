# Prepare

## Create Project

- e.g. formService

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

[http://localhost:8024/](http://localhost:8024/)

---

## Step By Step

### All in `com.example.formservice`

1. Create class `rest.FormController.java`

```java
@RestController
@RequestMapping("/form")
public class FormController {

    @PostMapping
    public String createTest(@RequestBody CreateFormRestModel model) {
        return "create : " + model.getName();
    }
}
```

2. Create class `rest.CreateFormRestModel.java`

```java
@Data
public class CreateFormRestModel {
    private String name;
    private String description;
}
```

3. Run and Test Web Service

```json
### POST http://localhost:8080/form

{
    "name": "name",
    "description": "description"
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

5. Create class `command.CreateFormCommand`

```java
@Builder
@Data
public class CreateFormCommand {
    @TargetAggregateIdentifier
    private final String formId;
    private final String name;
    private final String description;
}
```

6. Edit class `FormController.java`

```java
@RestController
@RequestMapping("/form")
public class FormController {

    private final CommandGateway commandGateway;

    @Autowired
    public FormController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }

    @PostMapping
    public String createTest(@RequestBody CreateFormRestModel model) {
        CreateFormCommand command = CreateFormCommand.builder()
                .formId(UUID.randomUUID().toString())
                .name(model.getName())
                .description(model.getDescription())
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

7. Create class `command.FormAggregate.java`

```java
@Aggregate
public class FormAggregate {

    @AggregateIdentifier
    private String formID;
    private String name;
    private String description;

    public FormAggregate() {
    }

    @CommandHandler
    public FormAggregate(CreateFormCommand createFormCommand) {
        if(createFormCommand.getName() == null || createFormCommand.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
    }
}
```

8. Create class `event.FormCreatedEvent.java`

```java
@Data
public class FormCreatedEvent {
    private String formId;
    private String name;
    private String description;
}
```

9. Edit class `FormAggregate.java`

```java
@Aggregate
public class FormAggregate {

    @AggregateIdentifier
    private String formID;
    private String name;
    private String description;

    public FormAggregate() {
    }

    @CommandHandler
    public FormAggregate(CreateFormCommand createFormCommand) {
        if(createFormCommand.getName() == null || createFormCommand.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }

        FormCreatedEvent formCreatedEvent = new FormCreatedEvent();
        BeanUtils.copyProperties(createFormCommand, formCreatedEvent);
        AggregateLifecycle.apply(formCreatedEvent);
    }

    @EventSourcingHandler
    public void on(FormCreatedEvent formCreatedEvent) {
        this.formID = formCreatedEvent.getFormId();
        this.name = formCreatedEvent.getName();
        this.description = formCreatedEvent.getDescription();
    }
}
```

10. Start Axon Server

```
java -jar axonserver.jar
```

11. Run and Test Web Service
    - See Result on AxonDashboard

---

## Connect Postgres Database and PGadmin

- You can run Postgres in another way.

1. Create file `docker-compose.ymal`

```yaml
version: "3.7"

services:
  postgres:
    image: postgres:14.1
    env_file: .env
    environment:
      POSTGRES_USER: ${POSTGRES_USER}
      POSTGRES_PASSWORD: ${POSTGRES_PASSWORD}
      POSTGRES_DB: ${POSTGRES_DATABASE}
    ports:
      - ${POSTGRES_PORT}:5432
  pg_admin:
    image: dpage/pgadmin4:6.1
    environment:
      PGADMIN_DEFAULT_EMAIL: admin@admin.com
      PGADMIN_DEFAULT_PASSWORD: changeme
    ports:
      - 8081:80
    volumes:
      - pgadmin_pv:/var/lib/pgadmin

volumes:
  pgadmin_pv:
```

2. Create file `.env`

```
POSTGRES_HOST=127.0.0.1
POSTGRES_PORT=5432
POSTGRES_USER=postgres
POSTGRES_PASSWORD=password
POSTGRES_DATABASE=formService
```

3. Run and See PGadmin

```
docker-compose up
```

- See PGadmin [http://localhost:8081](http://localhost:8081)

1. Login PGadmin

   ![login](https://imgur.com/nEvHSqX)

2. ## Add Service

   ![add service](https://imgur.com/hfR8zCn)

3. See Database

   ![database](https://imgur.com/5XfteIn)

---
