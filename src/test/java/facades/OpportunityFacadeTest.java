package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.OpportunityDTO;
import entities.Contact;
import entities.Opportunity;
import entities.OpportunityStatus;
import errorhandling.MissingInput;
import org.junit.jupiter.api.*;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class OpportunityFacadeTest {

    private static EntityManagerFactory emf;
    private static OpportunityFacade facade;
    private static Contact c1, c2, c3;
    private static Opportunity o1, o2, o3;
    private static OpportunityStatus s1, s2, s3,s4;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public OpportunityFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = OpportunityFacade.getOpportunityFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST

    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        c1 = new Contact("Pelle", "Pelle@mail.dk", "BornIT", "Manager", "12345678");
        c2 = new Contact("Mari", "Mari@mail.dk", "SuperBrugsen", "Sales Manager", "87654321");
        c3 = new Contact("Benjamin", "Benjamin@mail.dk", "Expert", "Manager", "22222222");

        o1 = new Opportunity("Sælg dem sko", 5000, "2021-03-04");
        o2 = new Opportunity("Sælg dem Legetøj", 10000, "2021-03-22");


        s1 = new OpportunityStatus("Active");
        s2 = new OpportunityStatus("Inactive");
        s3 = new OpportunityStatus("Won");
        s4 = new OpportunityStatus("Lost");

        c1.addOpportunity(o1);
        c1.addOpportunity(o2);

        s1.AddOpportunity(o1);
        s1.AddOpportunity(o2);



        try {
            em.getTransaction().begin();
            em.createNamedQuery("Opportunity.deleteAllRows").executeUpdate();
            em.createNamedQuery("OpportunityStatus.deleteAllRows").executeUpdate();
            em.createNamedQuery("Contact.deleteAllRows").executeUpdate();
            em.persist(c1);
            em.persist(c2);
            em.persist(o1);
            em.persist(o2);
            em.persist(s1);
            em.persist(s2);
            em.persist(s3);
            em.persist(s4);
            em.getTransaction().commit();

        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }


    @Test
    public void testAddOpp() {

        o3 = new Opportunity("Test", 5000, "2021-03-04");
        o3.setOpportunityStatus(s1);

        OpportunityDTO opportunityDTOtoAdd = new OpportunityDTO(o3);

        OpportunityDTO opportunityDTO = facade.addOpportunity(c1.getId(), opportunityDTOtoAdd);
        assertTrue(opportunityDTO.getName().equals("Test"));
    }

    @Test
    public void testGetAllOppsByID() {

        List<OpportunityDTO> opportunityDTOList = facade.getAllOppById(c1.getId());

        assertTrue(opportunityDTOList.size() == 2);
    }



}

