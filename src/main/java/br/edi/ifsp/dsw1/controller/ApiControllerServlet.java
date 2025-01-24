package br.edi.ifsp.dsw1.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.edi.ifsp.dsw1.model.dao.TaskDao;
import br.edi.ifsp.dsw1.model.dao.TaskDaoFactory;
import br.edi.ifsp.dsw1.model.entity.Task;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@WebServlet("/api/*")
public class ApiControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private TaskDao dao = new TaskDaoFactory().factory();
	private ObjectMapper mapper = new ObjectMapper();

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var task = mapper.readValue(request.getInputStream(), Task.class);
		
		if (dao.insert(task)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_CREATED);
			response.getWriter().write(mapper.writeValueAsString(task));
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Task> tasks = dao.findAll();
		
		String jsonResponse = mapper.writeValueAsString(tasks);
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().write(jsonResponse);
	}
	
	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		var task = mapper.readValue(request.getInputStream(), Task.class);
		if (dao.update(task)) {
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(mapper.writeValueAsString(task));
		} else {
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}
	
	protected void doPatch(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo(); // Retorna, por exemplo, /5, onde 5 é o id da Task
		if (pathInfo == null || pathInfo.length() <= 1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da tarefa é necessário.");
			return;
		}
		
		int taskId = Integer.parseInt(pathInfo.substring(1)); // Extrair o ID
	    BufferedReader reader = request.getReader();
	    String body = reader.lines().collect(Collectors.joining());
	    
	    Task updatedTask = mapper.readValue(body, Task.class);
	    Task fromSystem = dao.findById(taskId);
	    if (fromSystem == null) {
	    	response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tarefa não encontrada.");
	        return;
	    }
	    
	    fromSystem.setTitle(updatedTask.getTitle());
	    if (dao.update(fromSystem) ) {
	    	response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(mapper.writeValueAsString(fromSystem));
	    } else {
	    	response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	    }
	}
	
	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String pathInfo = request.getPathInfo(); // Retorna, por exemplo, /5, onde 5 é o id da Task
		if (pathInfo == null || pathInfo.length() <= 1) {
			response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID da tarefa é necessário.");
			return;
		}
		
		int taskId = Integer.parseInt(pathInfo.substring(1)); // Extrair o ID
		
		var fromSystem = dao.findById(taskId);
		if (fromSystem != null) {
			dao.delete(fromSystem);
			response.setStatus(HttpServletResponse.SC_NO_CONTENT); // 204 - Sem conteúdo
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "Tarefa não encontrada.");
		}
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if ("PATCH".equalsIgnoreCase(request.getMethod()) ) {
			doPatch(request, response);
		} else {
			super.service(request, response);
		}
	}

}
