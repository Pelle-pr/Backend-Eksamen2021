package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.UserDTO;
import entities.Contact;
import entities.Role;
import entities.User;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.util.HttpStatus;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;
//Uncomment the line below, to temporarily disable this test
//@Disabled

public class CrmResourceTest {

    private static final int SERVER_PORT = 7777;
    private static final String SERVER_URL = "http://localhost/api";
    private static Contact c1, c2, c3;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();


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
        c1 = new Contact("Pelle", "Pelle@mail.dk", "BornIT", "Manager", "12345678");
        c2 = new Contact("Mari", "Mari@mail.dk", "SuperBrugsen", "Sales Manager", "87654321");
        c3 = new Contact("Benjamin", "Benjamin@mail.dk", "Expert", "Manager", "22222222");

        try {
            em.getTransaction().begin();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
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





    @Test
    public void testAddContact(){
        Contact contact = new Contact("TestName", "Test@mail.dk", "TestCompany", "TestJobTitle", "88888888" );
        ContactDTO contactDTO = new ContactDTO(contact);


        given()
                .contentType("application/json")
                .body(contactDTO)
                .when()
                .post("/crm/")
                .then()
                .assertThat()
                .statusCode(200)
                .and()
                .body("name", equalTo("TestName"));
    }

    @Test
    public void testMissingInput(){

        Contact contact = new Contact("", "Test@mail.dk", "TestCompany", "TestJobTitle", "88888888" );
        ContactDTO contactDTO = new ContactDTO(contact);


        given()
                .contentType("application/json")
                .body(contactDTO)
                .when()
                .post("/crm/")
                .then()
                .assertThat()
                .statusCode(404)
                .and()
                .body("message", equalTo("Please enter at least 2 characters in name"));

    }
}
