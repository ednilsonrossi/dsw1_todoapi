package br.edi.ifsp.dsw1.model.dao;

import java.util.List;

import br.edi.ifsp.dsw1.model.entity.Task;

public interface TaskDao {
	
	boolean insert(Task task);
	
	Task findById(int id);
	
	List<Task> findAll();
	
	boolean update(Task task);
	
	boolean delete(Task task);
}
