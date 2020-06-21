<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page isErrorPage="true" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
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
				<h3>Welcome, ${user.firstName} ${user.lastName}</h3>
			</div>
		</div>
		<div class="row padding"></div>
		<div class="row">
			<div class="col text-right">
				<a href="/tasks" class="btn btn-dark btn-sm" disabled role="button">Priority: High - Low</a>
				<a href="/tasks/asc" class="btn btn-outline-dark btn-sm" role="button">Priority: Low - High</a>
			</div>
		</div>
		<div class="row padding-sm"></div>
		<div class="row">
			<div class="col">
				<table class="table table-striped">
				<thead class="thead-dark">
				    <tr>
				      <th scope="col">Task</th>
				      <th scope="col">Creator</th>
				      <th scope="col">Assignee</th>
				      <th scope="col">Priority</th>
				    </tr>
				</thead>
				<tbody>
				  	<c:forEach items="${tasksAsc}" var="e">
				  	<tr>
				      <th scope="row"><a href="/tasks/${e.id}"><c:out value="${e.name}"/></a></th>
				      <td>${e.creator.firstName} ${e.creator.lastName}</td>
				      <td>${e.assignee.firstName} ${e.assignee.lastName}</td>
				      <td>
				      	<c:choose>
					      	<c:when test="${e.priority == 1}">High</c:when>
					      	<c:when test="${e.priority == 2}">Medium</c:when>
							<c:otherwise>Low</c:otherwise>
						</c:choose>			      	
					  </td>
				    </tr>
				    </c:forEach>
				</tbody>
				</table>
			</div>
		</div>
		<div class="row padding"></div>
		<div class="row">
			<div class="col text-right">
				<a href="/tasks/new" class="btn btn-dark btn-sm">Create Task</a>
			</div>
		</div>
		
	</div>
<script src="/webjars/jquery/3.5.1/jquery.min.js "></script>
<script src="/webjars/popper.js/1.16.0/umd/popper.min.js"></script>
<script src="/webjars/bootstrap/4.5.0/js/bootstrap.min.js"></script>   
</body>
</html>