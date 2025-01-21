package br.edi.ifsp.dsw1.model.dao;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import br.edi.ifsp.dsw1.model.dao.connection.DatabaseConnection;
import br.edi.ifsp.dsw1.model.entity.Task;

class TaskDaoImpl implements TaskDao {

	@Override
	public boolean insert(Task task) {
		var sql = "INSERT INTO " + TaskContract.TABLENAME_TASKS + " (" 
				+ TaskContract.COLUMN_TITLE + ", "
				+ TaskContract.COLUMN_DONE + ") " 
				+ "VALUES (?, ?)";

		if (task != null) {
			try (var conn = DatabaseConnection.getConnection();
					var stm = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
				/*
				 * Objeto stm configurado para retornar a chave gerada pelo banco de dados
				 * ao inserir um novo dado na tabela.
				 */
				stm.setString(1, task.getTitle());
				stm.setBoolean(2, task.isDone());

				var rows = stm.executeUpdate();

				if (rows == 0) {
					throw new SQLException("Falha ao inserir tarefa");
				}

				var generateKeyResultSet = stm.getGeneratedKeys();
				if (generateKeyResultSet.next()) {
					task.setId(generateKeyResultSet.getInt(1));
				} else {
					throw new SQLException("Falha ao obter o ID gerado.");
				}

				return true;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return false;
	}

	@Override
	public Task findById(int id) {
		var sql = "SELECT " + TaskContract.COLUMN_ID + ", " + TaskContract.COLUMN_TITLE + ", "
				+ TaskContract.COLUMN_DONE + " FROM " + TaskContract.TABLENAME_TASKS + " WHERE "
				+ TaskContract.COLUMN_ID + " = ?";

		try (var stm = DatabaseConnection.getConnection().prepareStatement(sql)) {
			stm.setInt(1, id);
			var resultSet = stm.executeQuery();

			Task task = null;
			if (resultSet.next()) {
				task = new Task(resultSet.getInt(1), resultSet.getString(2), resultSet.getBoolean(3));
			}
			return task;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Task> findAll() {
		List<Task> tasks = new LinkedList<Task>();
		var sql = "SELECT * FROM " + TaskContract.TABLENAME_TASKS + " ORDER BY " + TaskContract.COLUMN_TITLE;

		try (var stm = DatabaseConnection.getConnection().prepareStatement(sql)) {
			var resultSet = stm.executeQuery();

			while (resultSet.next()) {
				var task = new Task(resultSet.getInt(TaskContract.COLUMN_ID),
						resultSet.getString(TaskContract.COLUMN_TITLE), resultSet.getBoolean(TaskContract.COLUMN_DONE));
				tasks.add(task);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public boolean update(Task task) {
		var sql = "UPDATE " + TaskContract.TABLENAME_TASKS + " SET " + TaskContract.COLUMN_TITLE + " = ?, "
				+ TaskContract.COLUMN_DONE + " = ? " + "WHERE " + TaskContract.COLUMN_ID + " = ? ";

		if (task != null) {
			try (var stm = DatabaseConnection.getConnection().prepareStatement(sql)) {
				stm.setString(1, task.getTitle());
				stm.setBoolean(2, task.isDone());
				stm.setInt(3, task.getId());

				var rows = stm.executeUpdate();

				return rows > 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	@Override
	public boolean delete(Task task) {
		var sql = "DELETE FROM " + TaskContract.TABLENAME_TASKS + " WHERE " + TaskContract.COLUMN_ID + " = ? ";

		if (task != null) {
			try (var stm = DatabaseConnection.getConnection().prepareStatement(sql)) {
				stm.setInt(1, task.getId());
				var rows = stm.executeUpdate();
				return rows > 0;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
