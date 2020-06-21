package com.jkoo.tasks.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jkoo.tasks.models.Task;
import com.jkoo.tasks.models.User;

@Repository
public interface TaskRepository extends CrudRepository<Task, Long>{
	List<Task> findAll();
	List<Task> findByStatus(String status);
	List<Task> findByStatusOrderByPriority(String status);
	List<Task> findByStatusOrderByPriorityDesc(String status);
	List<Task> findByAssigneeOrderByPriority(User user);
	List<Task> findByCreatorOrderByPriority(User user);
	//@Query("select count(t.id) as count_assigned from Task t left outer join t.assignee u where u.id = group by c.name order by count(t.id) desc")
	//int countTask(Long assigneeID);
	
	@Query(value="select count(*) from tasks where assignee_id in (select id from users where id=?1) and status = 'open'",nativeQuery=true)
	int countTask(Long assigneeID);
	
	@Query(value="select count(*) from tasks where assignee_id in (select id from users where id=?1) and status = 'open' and id !=?2",nativeQuery=true)
	int countTask2(Long assigneeID, Long taskID);
}
