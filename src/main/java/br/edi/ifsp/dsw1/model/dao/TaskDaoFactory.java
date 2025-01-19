package br.edi.ifsp.dsw1.model.dao;

public class TaskDaoFactory {

	public TaskDao factory() {
		return new TaskDaoImpl();
	}
	
}
