package facades;

import dto.ContactDTO;
import entities.Contact;
import errorhandling.MissingInput;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class ContactFacade {

    private static EntityManagerFactory emf;
    private static ContactFacade instance;

    private ContactFacade() {
    }

    public static ContactFacade getCrmFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ContactFacade();
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


    public ContactDTO getContactById (int id ) {

        EntityManager em = emf.createEntityManager();

        Contact contact = em.find(Contact.class, id);

        return new ContactDTO(contact);
    }

    public ContactDTO editContact (ContactDTO c) throws MissingInput {
        EntityManager em = emf.createEntityManager();
        isInputValid(c);
        Contact contact = em.find(Contact.class, c.getId());

        contact.setName(c.getName());
        contact.setEmail(c.getEmail());
        contact.setCompany(c.getCompany());
        contact.setJobtitle(c.getJobtitle());
        contact.setPhone(c.getPhone());

        try{
            em.getTransaction().begin();
            em.persist(contact);
            em.getTransaction().commit();
            return new ContactDTO(contact);
        }
        finally {
            em.close();
        }

    }

    public ContactDTO deleteContact (int id) {

        EntityManager em = emf.createEntityManager();

        Contact contact = em.find(Contact.class, id);

        try {
            em.getTransaction().begin();
            em.remove(contact);
            em.getTransaction().commit();
            return new ContactDTO(contact);
        }finally {
            em.close();
        }

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
