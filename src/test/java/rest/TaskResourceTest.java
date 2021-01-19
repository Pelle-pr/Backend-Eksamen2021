package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.TaskStatusDTO;
import entities.*;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class TaskResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Contact c1, c2, c3;
    private static Opportunity o1, o2, o3;
    private static OpportunityStatus s1, s2, s3,s4;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static User user;

    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
    private static HttpServer httpServer;
    private static EntityManagerFactory emf;

    static HttpServer startServer() {
        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
    }

    @BeforeAll
    public static void setUpClass() {
        //This method must be called before you request the EntityManagerFactory
        EMF_Creator.startREST_TestWithDB();
        emf = EMF_Creator.createEntityManagerFactoryForTest();

        httpServer = startServer();
        //Setup RestAssured
        RestAssured.baseURI = SERVER_URL;
        RestAssured.port = SERVER_PORT;
        RestAssured.defaultParser = Parser.JSON;
    }

    @AfterAll
    public static void closeTestServer() {
        //System.in.read();

        //Don't forget this, if you called its counterpart in @BeforeAll
        EMF_Creator.endREST_TestWithDB();
        httpServer.shutdownNow();
    }

    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        user = new User("user", "test123");
        Role userRole = new Role("user");
        user.addRole(userRole);
        c1 = new Contact("Pelle", "Pelle@mail.dk", "BornIT", "Manager", "12345678");
        c2 = new Contact("Mari", "Mari@mail.dk", "SuperBrugsen", "Sales Manager", "87654321");
        c3 = new Contact("Benjamin", "Benjamin@mail.dk", "Expert", "Manager", "22222222");

        o1 = new Opportunity("Sælg dem sko", 5000, "2021-03-04");
        o2 = new Opportunity("Sælg dem Legetøj", 10000, "2021-03-22");


        s1 = new OpportunityStatus("Active");
        s2 = new OpportunityStatus("Inactive");
        s3 = new OpportunityStatus("Won");
        s4 = new OpportunityStatus("Lost");

        TaskStatus ts1 = new TaskStatus("Non-Started");
        TaskStatus ts2 = new TaskStatus("In Progress");
        TaskStatus ts3 = new TaskStatus("Complete");

        TaskType tt1 = new TaskType("Online");
        TaskType tt2 = new TaskType("Meeting");
        TaskType tt3 = new TaskType("Email");
        TaskType tt4 = new TaskType("Call");


        c1.addOpportunity(o1);
        c1.addOpportunity(o2);

        s1.AddOpportunity(o1);
        s1.AddOpportunity(o2);



        try {
            em.getTransaction().begin();
            em.createNamedQuery("Opportunity.deleteAllRows").executeUpdate();
            em.createNamedQuery("OpportunityStatus.deleteAllRows").executeUpdate();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            em.createNamedQuery("User.deleteAllRows").executeUpdate();
            em.createNamedQuery("Roles.deleteAllRows").executeUpdate();
            em.createNamedQuery("TaskStatus.deleteAllRows").executeUpdate();
            em.createNamedQuery("TaskType.deleteAllRows").executeUpdate();
            em.createNamedQuery("Task.deleteAllRows").executeUpdate();
            em.persist(userRole);
            em.persist(user);
            em.persist(c1);
            em.persist(c2);
            em.persist(o1);
            em.persist(o2);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.persist(ts1);
            em.persist(ts2);
            em.persist(ts3);
            em.persist(tt1);
            em.persist(tt2);
            em.persist(tt3);
            em.persist(tt4);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    private static String securityToken;

    private static void login(String role, String password) {
        String json = String.format("{username: \"%s\", password: \"%s\"}", role, password);
        securityToken = given()
                .contentType("application/json")
                .body(json)
                //.when().post("/api/login")
                .when().post("/login")
                .then()
                .extract().path("token");
        //System.out.println("TOKEN ---> " + securityToken);
    }

    @Test
    public void testServerIsUp() {
        given().when().get("users/count").then().statusCode(200);
    }


    @Disabled
    @Test
    public void testGetTaskStatus (){

        List<TaskStatusDTO> taskStatusDTOList;

        login("user","test123");

        taskStatusDTOList=  given()
                .contentType("application/json")
                .header("x-access-token", securityToken)
                .get("/task/status")
                .then()
                .extract().body().jsonPath().getList("", TaskStatusDTO.class);


    }




}
