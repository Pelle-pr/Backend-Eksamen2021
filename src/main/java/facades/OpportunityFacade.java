package facades;

import dto.OpportunityDTO;
import dto.TaskDTO;
import dto.UserDTO;
import entities.*;
import errorhandling.MissingInput;
import security.errorhandling.AuthenticationException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class OpportunityFacade {

    private static EntityManagerFactory emf;
    private static OpportunityFacade instance;

    private OpportunityFacade() {
    }

    public static OpportunityFacade getOpportunityFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new OpportunityFacade();
        }
        return instance;
    }


    public OpportunityDTO addOpportunity (int contact_id, OpportunityDTO o) {

        EntityManager em = emf.createEntityManager();

        Opportunity opportunity = new Opportunity(o.getName(),o.getAmount(),o.getCloseDate());

        Contact contact = em.find(Contact.class, contact_id);

        contact.addOpportunity(opportunity);

        TypedQuery<OpportunityStatus> statusQuery = em.createQuery("SELECT s from OpportunityStatus s where s.name = 'Active'",OpportunityStatus.class);

        OpportunityStatus status = statusQuery.getSingleResult();

        status.AddOpportunity(opportunity);

        try{
            em.getTransaction().begin();
            em.persist(opportunity);
            em.getTransaction().commit();
            return new OpportunityDTO(opportunity);
        }finally {
            em.close();
        }


    }

    public List<OpportunityDTO> getAllOppById (int id) {

        EntityManager em = emf.createEntityManager();

        TypedQuery<Opportunity> query = em.createQuery("SELECT o from Opportunity o join o.contact c where c.id = :id", Opportunity.class);
        query.setParameter("id", id);

        List<Opportunity> opportunityList = query.getResultList();
        List<OpportunityDTO> opportunityDTOList = new ArrayList<>();

        for(Opportunity o : opportunityList){
            opportunityDTOList.add(new OpportunityDTO(o));
        }

        return opportunityDTOList;
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

}
