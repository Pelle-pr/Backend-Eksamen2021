package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import errorhandling.MissingInput;
import facades.ContactFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("contact")
public class ContactResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final ContactFacade CONTACT_FACADE = ContactFacade.getCrmFacade(EMF);


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed({"user", "admin"})
    public String addContact (String contact) throws MissingInput {

        ContactDTO newContactDTO = GSON.fromJson(contact, ContactDTO.class);

        ContactDTO addedContactDTO = CONTACT_FACADE.addContact(newContactDTO);

        return GSON.toJson(addedContactDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllContacts (){

        List<ContactDTO> contactDTOList = CONTACT_FACADE.getAllContacts();

        return GSON.toJson(contactDTOList);
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getContactByID (@PathParam("id") int id) {

        ContactDTO contactDTO = CONTACT_FACADE.getContactById(id);

        return GSON.toJson(contactDTO);

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String editContact (String contact) throws MissingInput {
        ContactDTO contactDTO = GSON.fromJson(contact,ContactDTO.class);

        ContactDTO editedContact = CONTACT_FACADE.editContact(contactDTO);

        return GSON.toJson(editedContact);

    }

    @Path("{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteContact (@PathParam("id") int id){

        ContactDTO contactDTO = CONTACT_FACADE.deleteContact(id);

        return GSON.toJson(contactDTO);

    }


}
