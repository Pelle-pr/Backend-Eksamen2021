package facades;

import dto.ContactDTO;
import dto.UserDTO;
import entities.Contact;
import entities.Role;
import entities.User;
import errorhandling.MissingInput;
import security.errorhandling.AuthenticationException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class CrmFacade {

    private static EntityManagerFactory emf;
    private static CrmFacade instance;

    private CrmFacade() {
    }

    public static CrmFacade getCrmFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new CrmFacade();
        }
        return instance;
    }

    public ContactDTO addContact(ContactDTO contactDTO) throws MissingInput {

        EntityManager em = emf.createEntityManager();

        isInputValid(contactDTO);

        Contact newContact = prepareContact(contactDTO);

        try{
            em.getTransaction().begin();
            em.persist(newContact);
            em.getTransaction().commit();
            return new ContactDTO(newContact);
        }finally {
            em.close();
        }

    }

    public List<ContactDTO> getAllContacts () {
        EntityManager em = emf.createEntityManager();

        TypedQuery<Contact> query = em.createQuery("SELECT c from Contact c", Contact.class);
        List<Contact> contactList = query.getResultList();

        List<ContactDTO> contactDTOList = new ArrayList<>();

        for (Contact c : contactList){
            contactDTOList.add(new ContactDTO(c));
        }

        return  contactDTOList;
    }

    private Contact prepareContact(ContactDTO contactDTO) {
        Contact newContact = new Contact(contactDTO.getName(), contactDTO.getEmail(), contactDTO.getCompany(), contactDTO.getJobtitle(), contactDTO.getPhone());
        return newContact;
    }


    private void isInputValid(ContactDTO contactDTO) throws MissingInput {
        if(contactDTO.getName().length() < 2){
            throw new MissingInput("Please enter at least 2 characters in name");
        }
        if(!contactDTO.getEmail().contains("@")){
            throw new MissingInput("Please enter a valid Email Address");
        }
        if(contactDTO.getCompany().length() < 1){
            throw new MissingInput("Please enter a valid company");
        }
        if(contactDTO.getJobtitle().length() < 1){
            throw new MissingInput("Please enter at least 2 characters in Job Title");
        }
        if(contactDTO.getPhone().length() < 8 ){
            throw new MissingInput("Please enter a valid phone number");
        }
    }

}
