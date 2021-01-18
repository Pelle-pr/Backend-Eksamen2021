package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.OpportunityDTO;
import dto.TaskDTO;
import entities.Task;
import errorhandling.MissingInput;
import facades.OpportunityFacade;
import utils.EMF_Creator;

import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("task")
public class TaskResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final OpportunityFacade OPPORTUNITY_FACADE = OpportunityFacade.getOpportunityFacade(EMF);


    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    //@RolesAllowed({"user", "admin"})
    public String addTask (@PathParam("id") int id ,String task) throws MissingInput {

        TaskDTO taskDTO = GSON.fromJson(task, TaskDTO.class);

        TaskDTO addedTask = OPPORTUNITY_FACADE.addTask(id,taskDTO);

        return GSON.toJson(addedTask);
    }




}
