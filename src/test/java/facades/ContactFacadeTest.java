package facades;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import entities.Contact;
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
public class ContactFacadeTest {

    private static EntityManagerFactory emf;
    private static ContactFacade facade;
    private static Contact c1, c2, c3;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public ContactFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = ContactFacade.getCrmFacade(emf);
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

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }


    @Test
    public void addContactTest() throws MissingInput {

        ContactDTO contactDTO = new ContactDTO(c3);

        ContactDTO addedContact = facade.addContact(contactDTO);

        List<ContactDTO> contactDTOList = facade.getAllContacts();
        assertTrue(contactDTOList.size() == 3);

    }

    @Test
    public void missingInputTest (){

        Contact contact = new Contact("Test", "TestMail.dk", "TestCompany", "TestJobTitle", "66666666");
        ContactDTO newContactDTO = new ContactDTO(contact);

        MissingInput thrown =
                assertThrows(MissingInput.class, () -> {
                    facade.addContact(newContactDTO);
                });
        assertTrue(thrown.getMessage().equals("Please enter a valid Email Address"));

    }

    @Test
    public void getAllContactsTest (){

        List<ContactDTO> contactDTOList = facade.getAllContacts();

        assertTrue(contactDTOList.size() == 2);
    }

    @Test
    public void getContactByIdTest (){


        ContactDTO contactDTO = facade.getContactById(c1.getId());
        assertTrue(contactDTO.getName().equals(c1.getName()));
    }

    @Test
    public void editContactTest () throws MissingInput {

        c1.setName("John");

        ContactDTO contactDTO = facade.editContact(new ContactDTO(c1));

        assertTrue(contactDTO.getName().equals("John"));
    }

    @Test
    public void deleteContactTest () {

        ContactDTO contactDTO = facade.deleteContact(c1.getId());
        List<ContactDTO> dtoList = facade.getAllContacts();

        assertTrue(contactDTO.getName().equals("Pelle"));
        assertTrue(dtoList.size() == 1);

    }



}

