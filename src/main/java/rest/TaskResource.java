package rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dto.OpportunityDTO;
import dto.TaskDTO;
import dto.TaskStatusDTO;
import dto.TaskTypeDTO;
import entities.Task;
import entities.TaskStatus;
import entities.TaskType;
import errorhandling.MissingInput;
import facades.OpportunityFacade;
import facades.TaskFacade;
import utils.EMF_Creator;

import javax.annotation.security.RolesAllowed;
import javax.persistence.EntityManagerFactory;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("task")
public class TaskResource {

    private static final EntityManagerFactory EMF = EMF_Creator.createEntityManagerFactory();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    public static final TaskFacade TASK_FACADE = TaskFacade.getTaskFacade(EMF);


    @POST
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String addTask (@PathParam("id") int id ,String task) {

        TaskDTO taskDTO = GSON.fromJson(task, TaskDTO.class);

        TaskDTO addedTask = TASK_FACADE.addTask(id,taskDTO);

        return GSON.toJson(addedTask);
    }

    @Path("status")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String getStatus () {

        List<TaskStatusDTO> taskStatusList = TASK_FACADE.getTaskStatus();

        return GSON.toJson(taskStatusList);
    }

    @Path("type")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"user", "admin"})
    public String getType () {

        List<TaskTypeDTO> taskTypeList = TASK_FACADE.getTaskTypes();

        return GSON.toJson(taskTypeList);
    }




}
