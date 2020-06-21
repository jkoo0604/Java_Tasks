package com.jkoo.tasks.services;

import java.util.List;
import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jkoo.tasks.models.Task;
import com.jkoo.tasks.models.User;
import com.jkoo.tasks.repositories.TaskRepository;
import com.jkoo.tasks.repositories.UserRepository;

@Service
public class MainService {
	@Autowired
	private UserRepository uRepo;
	
	@Autowired
	private TaskRepository tRepo;
	
	public User getUser(Long id) {
		Optional<User> checkU = uRepo.findById(id);
		if (checkU.isPresent()) {
			User user = checkU.get();
			return user;
		}
		return null;
	}
	
	public List<User> getAllUsers() {
		return uRepo.findAll();
	}
	
	public List<Task> getOpenTasksDesc() {
		return tRepo.findByStatusOrderByPriorityDesc("Open");
	}
	
	public List<Task> getOpenTasksAsc() {
		return tRepo.findByStatusOrderByPriority("Open");
	}
	
	public Task getTask(Long id) {
		Optional<Task> checkT = tRepo.findById(id);
		if (checkT.isPresent()) {
			Task task = checkT.get();
			return task;
		}
		return null;
	}
	
	public Task createTask(String name, int priority, String status, User creator, User assignee) {
		Task task = new Task();
		task.setName(name);
		task.setPriority(priority);
		task.setStatus(status);
		task.setCreator(creator);
		task.setAssignee(assignee);
		return tRepo.save(task);
	}
	
	public Task updateTask(Long id, String name, int priority, User assignee) {
		Optional<Task> checkT = tRepo.findById(id);
		if (checkT.isPresent()) {
			Task task = checkT.get();
			task.setName(name);
			task.setPriority(priority);
			task.setAssignee(assignee);
			return tRepo.save(task);
		}
		return null;
	}
	
	public Task finishTask(Long id) {
		Optional<Task> checkT = tRepo.findById(id);
		if (checkT.isPresent()) {
			Task task = checkT.get();
			task.setStatus("completed");
			return tRepo.save(task);
		}
		return null;
	}
	
	public boolean deleteTask(Long id) {
		Optional<Task> checkT = tRepo.findById(id);
		if (checkT.isPresent()) {
			Task task = checkT.get();
			tRepo.delete(task);
			return true;
		}
		return false;
	}
	
	public boolean countWithinLimit(Long id) {
		Optional<User> checkU = uRepo.findById(id);
		if (checkU.isPresent()) {
			User user = checkU.get();
			int count = tRepo.countTask(user.getId());
			return count < 3;			
		}
		return false;
	}
	
	public boolean countWithinLimitExisting(Long id, Long taskID) {
		Optional<User> checkU = uRepo.findById(id);
		Optional<Task> checkT = tRepo.findById(taskID);
		if (checkU.isPresent() && checkT.isPresent()) {
			User user = checkU.get();
			Task task = checkT.get();
			int count = tRepo.countTask2(user.getId(), task.getId());
			return count < 3;			
		}
		return false;
	}
	
	
    // register user and hash their password
    public User registerUser(User user) {
        String hashed = BCrypt.hashpw(user.getPassword(), BCrypt.gensalt());
        user.setPassword(hashed);
        return uRepo.save(user);
    }
    
    // find user by email
    public User findByEmail(String email) {
        return uRepo.findByEmail(email);
    }
    
    // find user by id
    public User findUserById(Long id) {
    	Optional<User> u = uRepo.findById(id);
    	
    	if(u.isPresent()) {
            return u.get();
    	} else {
    	    return null;
    	}
    }
    
    public List<Task> tasksCreated(User user) {
    	return tRepo.findByCreatorOrderByPriority(user);
    }
    
    public List<Task> tasksAssigned(User user) {
    	return tRepo.findByAssigneeOrderByPriority(user);
    }
    
    // authenticate user
    public boolean authenticateUser(String email, String password) {
        // first find the user by email
        User user = uRepo.findByEmail(email);
        // if we can't find it by email, return false
        if(user == null) {
            return false;
        } else {
            // if the passwords match, return true, else, return false
            if(BCrypt.checkpw(password, user.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
    }
    
    public boolean duplicateEmail(String email) {
    	User user = uRepo.findByEmail(email);
    	if (user == null) {
    		return false;
    	}
    	return true;
    }
}
