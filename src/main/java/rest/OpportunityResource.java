package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.ContactDTO;
import dto.OpportunityDTO;
import errorhandling.MissingInput;
import facades.ContactFacade;
import facades.OpportunityFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("opportunity")
public class OpportunityResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final OpportunityFacade OPPORTUNITY_FACADE = OpportunityFacade.getOpportunityFacade(EMF);


    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed({"user", "admin"})
    public String addContact (@PathParam("id") int id ,String opportunity) throws MissingInput {

        OpportunityDTO opportunityDTO = GSON.fromJson(opportunity, OpportunityDTO.class);

        OpportunityDTO addedOpportunityDTO = OPPORTUNITY_FACADE.addOpportunity(id,opportunityDTO);

        return GSON.toJson(addedOpportunityDTO);
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)

    public String getOpps(@PathParam("id") int id){

        List<OpportunityDTO> opportunityDTOList = OPPORTUNITY_FACADE.getAllOppById(id);

        return GSON.toJson(opportunityDTOList);
    }





}
