<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Tasks</title>
<link href="/webjars/bootstrap/4.5.0/css/bootstrap.min.css" rel="stylesheet" />
<link rel="stylesheet" type="text/css" href="/css/style.css">
</head>
<body>
	<nav class="navbar navbar-expand-lg navbar-dark bg-dark sticky-top">
	  <a class="navbar-brand" href="#">Tasks</a>
	  <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavDropdown" aria-controls="navbarNavDropdown" aria-expanded="false" aria-label="Toggle navigation">
	    <span class="navbar-toggler-icon"></span>
	  </button>
	  <div class="collapse navbar-collapse" id="navbarNavDropdown">
	    <ul class="navbar-nav mr-auto">
	      <li class="nav-item active">
	        <a class="nav-link" href="/tasks">Home</a>
	      </li>
	      <li class="nav-item dropdown">
	        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdownMenuLink" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
	          Your Tasks
	        </a>
	        <div class="dropdown-menu" aria-labelledby="navbarDropdownMenuLink">
	          <a class="dropdown-item" href="/user/create">Created Tasks</a>
	          <a class="dropdown-item" href="/user/assign">Assigned Tasks</a>
	        </div>
	      </li>
	    </ul>
	    <span class="navbar-text">
	      <a href="/logout">Logout</a>
	    </span>
	  </div>
	</nav>
	<div class="container">
		<div class="row">
			<div class="col">
				<h3>Create a new task</h3>
			</div>
		</div>
		<div class="row padding"></div>
		<div class="row">
			<div class="col">
				<form action="/tasks" method="post">
	  				<p class="err">${nameerr}</p>
	  			<div class="form-row">
	  				<div class="form-group col-sm-4">	  				
					    <label for="name">Task:</label>
	  				</div>
				    <div class="form-group col-sm-8">				    	
				    	<input type="text" class="form-control" name="name">
				    </div>
				</div>
	  				<p class="err">${assignerr}</p>
	  			<div class="form-row">
	  				<div class="form-group col-sm-4">	  				
					    <label for="assigneeID">Assignee:</label>
	  				</div>
				    <div class="form-group col-sm-8">				    	
				    	<select class="form-control" name="assigneeID">
					    	<c:forEach items="${users}" var="c">
				        	<option value="${c.id}" ${c.id == user.id ? 'selected' : '' }><c:out value="${c.firstName} ${c.lastName}"/></option>
				        	</c:forEach>
					    </select>
				    </div>
				</div>
	  			<div class="form-row">
	  				<div class="form-group col-sm-4">	  				
					    <label for="priority">Priority:</label>
	  				</div>
				    <div class="form-group col-sm-8">				    	
				    	<select class="form-control" name="priority">
				        	<option value="1">High</option>
				        	<option value="2" selected>Medium</option>
				        	<option value="3">Low</option>
					    </select>
				    </div>
				</div>
				<div class="row form-group">
					<div class="col text-center">
					
						<input class="btn btn-dark btn-sm" type="submit" value="Create">
					</div>
				</div>
				</form>
			</div>
		</div>
		<div class="row padding"></div>
		<div class="row">
			<div class="col text-right">
				<a href="/tasks" class="btn btn-outline-dark btn-sm">Back</a>
			</div>
		</div>
		
	</div>
<script src="/webjars/jquery/3.5.1/jquery.min.js "></script>
<script src="/webjars/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="/webjars/bootstrap/4.5.0/js/bootstrap.min.js"></script>  
</body>
</html>