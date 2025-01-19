package br.edi.ifsp.dsw1.model.entity;

public class Task {

	private Integer id;
	private String title;
	private boolean done;
	
	public Task() {
		this.id = null;
		this.title = null;
		this.done = false;
	}
	
	public Task(String title) {
		this();
		this.title = title;
	}
	
	public Task(int id, String title, boolean done) {
		this.id = id;
		this.title = title;
		this.done = done;
	}

	public int getId() {
		return this.id.intValue();
	}
	
	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}
	
}
