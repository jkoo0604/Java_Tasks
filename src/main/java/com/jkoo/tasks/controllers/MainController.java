package com.jkoo.tasks.controllers;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.jkoo.tasks.models.Task;
import com.jkoo.tasks.models.User;
import com.jkoo.tasks.services.MainService;
import com.jkoo.tasks.validator.UserValidator;

@Controller
public class MainController {
	@Autowired
	private MainService mService;
	
	@Autowired
	private UserValidator uVal;
	
	@RequestMapping("/")
	public String index(HttpSession session, Model model, @ModelAttribute("user") User user) {		
		
		if (session.getAttribute("userID") == null) {
			return "index.jsp";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {		
			return "redirect:/logout";
		}
		return "redirect:/tasks";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String loginUser(@RequestParam("logemail") String email, @RequestParam("logpassword") String password, @Valid @ModelAttribute("user") User user, BindingResult result, Model model, HttpSession session) {
		boolean auth = mService.authenticateUser(email, password);
		if (auth) {
			User loggedin = mService.findByEmail(email);
			session.setAttribute("userID", loggedin.getId());
			return "redirect:/tasks";
		}
		model.addAttribute("loginerr", "Login failed. Please verify email and password.");
		return "index.jsp";
	}
	
	@RequestMapping(value="/registration", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") User user, BindingResult result, HttpSession session, Model model) {
		uVal.validate(user, result);
		boolean dupeEmail = mService.duplicateEmail(user.getEmail());
		if (dupeEmail) {
			model.addAttribute("emailerr", "An account already exists for this email");
		}		
		if (result.hasErrors() || dupeEmail) {
			return "index.jsp";
		}
		User newuser = mService.registerUser(user);
		session.setAttribute("userID", newuser.getId());
		return "redirect:/";
	}
	
	@RequestMapping("/tasks")
	public String home(HttpSession session, Model model) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		List<Task> openTasksDesc = mService.getOpenTasksDesc();
		List<Task> openTasksAsc = mService.getOpenTasksAsc();
		model.addAttribute("tasksDesc", openTasksDesc);
		model.addAttribute("tasksAsc", openTasksAsc);
		model.addAttribute("user", loggedin);
		return "/tasks/welcome.jsp";
	}
	
	@RequestMapping("/tasks/asc")
	public String homeSortedAsc(HttpSession session, Model model) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		List<Task> openTasksDesc = mService.getOpenTasksDesc();
		List<Task> openTasksAsc = mService.getOpenTasksAsc();
		model.addAttribute("tasksDesc", openTasksDesc);
		model.addAttribute("tasksAsc", openTasksAsc);
		model.addAttribute("user", loggedin);
		return "/tasks/welcome-asc.jsp";
	}
	
	@RequestMapping("/tasks/new")
	public String newTask(HttpSession session, Model model) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		List<User> allUsers = mService.getAllUsers();
		model.addAttribute("users", allUsers);
		model.addAttribute("user", loggedin);
		return "/tasks/new.jsp";
	}
	
	@RequestMapping(value="/tasks", method=RequestMethod.POST)
	public String createTask(HttpSession session, Model model, @RequestParam("name") String name, @RequestParam("assigneeID") Long assigneeID, @RequestParam("priority") int priority) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		if (name.length() < 5) {
			model.addAttribute("nameerr", "Task name must be at least 5 characters in length");
			List<User> allUsers = mService.getAllUsers();
			model.addAttribute("users", allUsers);
			model.addAttribute("user", loggedin);
			return "/tasks/new.jsp";
		}
		User assignee = mService.findUserById(assigneeID);
		if (assignee == null) {
			model.addAttribute("assignerr", "Select an assignee from the list");
			List<User> allUsers = mService.getAllUsers();
			model.addAttribute("users", allUsers);
			model.addAttribute("user", loggedin);
			return "/tasks/new.jsp";
		}
		boolean tasklimit = mService.countWithinLimit(assigneeID);
		if (!tasklimit) {
			model.addAttribute("assignerr", "This user already has 3 open tasks");
			List<User> allUsers = mService.getAllUsers();
			model.addAttribute("users", allUsers);
			model.addAttribute("user", loggedin);
			return "/tasks/new.jsp";
		}
		mService.createTask(name, priority, "Open", loggedin, assignee);
		return "redirect:/tasks";
	}
	
	@RequestMapping("/tasks/{id}/edit")
	public String editTask(Model model, HttpSession session, @PathVariable("id") Long id) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		Task t = mService.getTask(id);
		if (t == null) {
			return "redirect:/tasks";
		}
		if (!t.getCreator().getId().equals(loggedin.getId())) {
			return "redirect:/tasks/" + id;
		}
		List<User> allUsers = mService.getAllUsers();
		model.addAttribute("users", allUsers);
		model.addAttribute("user", loggedin);
		model.addAttribute("task", t);
		return "/tasks/edit.jsp";
		
	}
	
	@RequestMapping(value="/tasks/{id}", method=RequestMethod.PUT)
	public String updateTask(@PathVariable("id") Long id, Model model, RedirectAttributes redirectA, HttpSession session, @RequestParam("name") String name, @RequestParam("assigneeID") Long assigneeID, @RequestParam("priority") int priority) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		Task t = mService.getTask(id);
		if (t == null) {
			return "redirect:/tasks";
		}
		if (!t.getCreator().getId().equals(loggedin.getId())) {
			return "redirect:/tasks/" + id;
		}
		if (name.length() < 5) {
			//model.addAttribute("nameerr", "Task name must be at least 5 characters in length");
			redirectA.addFlashAttribute("nameerr", "Task name must be at least 5 characters in length");
			return "redirect:/tasks/" + t.getId() + "/edit";
		}
		User assignee = mService.findUserById(assigneeID);
		if (assignee == null) {
//			model.addAttribute("assignerr", "Select an assignee from the list");
//			List<User> allUsers = mService.getAllUsers();
//			model.addAttribute("users", allUsers);
//			model.addAttribute("user", loggedin);
//			model.addAttribute("task", t);
//			return "/tasks/edit.jsp";
			redirectA.addFlashAttribute("assignerr", "Select an assignee from the list");
			return "redirect:/tasks/" + t.getId() + "/edit";
		}
		boolean tasklimit1 = mService.countWithinLimitExisting(assigneeID, t.getId());
		if (!tasklimit1) {
//			model.addAttribute("assignerr", "This user already has 3 open tasks");
//			List<User> allUsers = mService.getAllUsers();
//			model.addAttribute("users", allUsers);
//			model.addAttribute("user", loggedin);
//			model.addAttribute("task", t);
//			return "/tasks/edit.jsp";
			redirectA.addFlashAttribute("assignerr", "This user already has 3 open tasks");
			return "redirect:/tasks/" + t.getId() + "/edit";
		}
		mService.updateTask(id, name, priority, assignee);
		return "/tasks/" + id;
	}
	
	@RequestMapping(value="/tasks/{id}", method=RequestMethod.DELETE)
	public String deleteTask(@PathVariable("id") Long id, Model model, HttpSession session) {
		
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		Task t = mService.getTask(id);
		if (t == null) {
			return "redirect:/tasks";
		}
		if (!t.getCreator().getId().equals(loggedin.getId())) {
			return "redirect:/tasks/" + id;
		}
		mService.deleteTask(id);
		return "redirect:/tasks";
	}
	
	@RequestMapping(value="/tasks/{id}/finish", method=RequestMethod.PUT)
	public String finishTask(@PathVariable("id") Long id, Model model, HttpSession session) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		Task t = mService.getTask(id);
		if (t == null) {
			return "redirect:/tasks";
		}
		if (!t.getAssignee().getId().equals(loggedin.getId())) {
			return "redirect:/tasks/" + id;
		}
		mService.finishTask(id);
		return "redirect:/tasks";
	}
	
	@RequestMapping("/tasks/{id}")
	public String showTask(@PathVariable("id") Long id, Model model, HttpSession session) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		Task t = mService.getTask(id);
		if (t == null) {
			return "redirect:/tasks";
		}
		model.addAttribute("user", loggedin);
		model.addAttribute("task", t);
		return "/tasks/show.jsp";
		
	}
	
	@RequestMapping("/user/create")
	public String tasksCreated(HttpSession session, Model model) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		List<Task> tasks = mService.tasksCreated(loggedin);
		model.addAttribute("user", loggedin);
		model.addAttribute("tasks", tasks);
		return "/users/create.jsp";
		
	}
	
	@RequestMapping("/user/assign")
	public String tasksAssigned(HttpSession session, Model model) {
		if (session.getAttribute("userID") == null) {
			return "redirect:/";
		}
		Long userID = (Long) session.getAttribute("userID");
		User loggedin = mService.getUser(userID);
		if (loggedin == null) {
			return "redirect:/logout";
		}
		List<Task> tasks = mService.tasksAssigned(loggedin);
		model.addAttribute("user", loggedin);
		model.addAttribute("tasks", tasks);
		return "/users/assign.jsp";
		
	}
	
	@RequestMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		return "redirect:/login";
	}
	
	@RequestMapping(value = "/**/{[path:[^\\.]*}")
	public String allRoutes() {
		return "redirect:/";
	}
}
