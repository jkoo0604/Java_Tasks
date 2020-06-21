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
				<h3>Task: ${task.name}</h3>
			</div>
		</div>
		<div class="row padding"></div>
		<div class="row">
			<div class="col-4">
				<div class="row">
					<div class="col-sm-6">
						Creator:
					</div>
					<div class="col-sm-6">
						${task.creator.firstName } ${task.creator.lastName }
					</div>
				</div>
				<div class="row padding-sm"></div>
				<div class="row">
					<div class="col-sm-6">
						Assignee:
					</div>
					<div class="col-sm-6">
						${task.assignee.firstName } ${task.assignee.lastName }
					</div>
				</div>
				<div class="row padding-sm"></div>
				<div class="row">
					<div class="col-sm-6">
						Priority:
					</div>
					<div class="col-sm-6">
						<c:choose>
					      	<c:when test="${task.priority == 1}">High</c:when>
					      	<c:when test="${task.priority == 2}">Medium</c:when>
							<c:otherwise>Low</c:otherwise>
						</c:choose>	
					</div>
				</div>
				<c:if test="${task.creator.id == user.id }">
				<div class="row padding"></div>
				<div class="row">
					<div class="col-sm-6 text-center">
						<a href="/tasks/${task.id }/edit" class="btn btn-dark btn-sm" role="button">Edit</a>
					</div>
					<div class="col-sm-6 text-center">
						<form action="/tasks/${task.id}" method="post">
			  				<input type="hidden" name="_method" value="delete">
			  				<input type="submit" value="Delete" class="btn btn-dark btn-sm">
						</form>
					</div>
				</div>
				</c:if>
				<c:if test="${task.assignee.id == user.id }">
				<div class="row padding"></div>
				<div class="row">
					<div class="col-sm-6"></div>
					<div class="col-sm-6 text-center">
						<form action="/tasks/${task.id}/finish" method="post">
			  				<input type="hidden" name="_method" value="put">
			  				<input type="submit" value="Complete" class="btn btn-info btn-sm">
						</form>
					</div>
				</div>
				</c:if>
			
				
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