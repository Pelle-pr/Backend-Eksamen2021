package facades;


import dto.TaskDTO;
import dto.TaskStatusDTO;
import dto.TaskTypeDTO;
import entities.*;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class TaskFacade {

    private static EntityManagerFactory emf;
    private static TaskFacade instance;

    private TaskFacade() {
    }

    public static TaskFacade getTaskFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new TaskFacade();
        }
        return instance;
    }

    public TaskDTO addTask (int id, TaskDTO taskDTO) {

        EntityManager em = emf.createEntityManager();

        Opportunity opportunity = em.find(Opportunity.class, id);

        Task task = new Task(taskDTO.getTitle(), taskDTO.getComment(), taskDTO.getDueDate());

        opportunity.addTask(task);

        TypedQuery<TaskStatus> query1 = em.createQuery("select t from TaskStatus t where t.name =:statusName", TaskStatus.class);
        query1.setParameter("statusName", taskDTO.getTaskStatus());
        task.setTaskStatus(query1.getSingleResult());

        TypedQuery<TaskType> query2 = em.createQuery("select t from TaskType t where t.name =:taskType", TaskType.class);
        query2.setParameter("taskType", taskDTO.getTaskType());
        task.setTaskType(query2.getSingleResult());

        try{
            em.getTransaction().begin();
            em.persist(task);
            em.getTransaction().commit();

            return new TaskDTO(task);
        }finally {
            em.close();
        }
    }

    public List<TaskStatusDTO> getTaskStatus () {

        EntityManager em = emf.createEntityManager();

        TypedQuery<TaskStatus> query = em.createQuery("Select t from TaskStatus t", TaskStatus.class);
        List<TaskStatus> taskStatusList = query.getResultList();

        List<TaskStatusDTO> taskStatusDTOS = new ArrayList<>();

        for(TaskStatus t : taskStatusList){
            taskStatusDTOS.add(new TaskStatusDTO(t));
        }

        return taskStatusDTOS;
    }

    public List<TaskTypeDTO> getTaskTypes () {

        EntityManager em = emf.createEntityManager();

        TypedQuery<TaskType> query = em.createQuery("Select t from TaskType t", TaskType.class);
        List<TaskType> taskTypeList = query.getResultList();

        List<TaskTypeDTO> taskTypeDTOS = new ArrayList<>();

        for(TaskType t : taskTypeList){
            taskTypeDTOS.add(new TaskTypeDTO(t));
        }

        return taskTypeDTOS;

    }
}
