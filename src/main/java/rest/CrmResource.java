package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.StudentDTO;
import errorhandling.MissingInput;
import facades.CrmFacade;
import facades.UserFacade;
import fetchers.ExampleFetcher;
import fetchers.StudentFetcher;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

@Path("crm")
public class CrmResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final CrmFacade CRM_FACADE = CrmFacade.getCrmFacade(EMF);


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed({"user", "admin"})
    public String addContact (String contact) throws MissingInput {

        ContactDTO newContactDTO = GSON.fromJson(contact, ContactDTO.class);

        ContactDTO addedContactDTO = CRM_FACADE.addContact(newContactDTO);

        return GSON.toJson(addedContactDTO);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllContacts (){

        List<ContactDTO> contactDTOList = CRM_FACADE.getAllContacts();

        return GSON.toJson(contactDTOList);
    }

}
