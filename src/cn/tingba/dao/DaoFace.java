package cn.tingba.dao;

import java.io.Serializable;
import java.util.List;
import org.hibernate.Session;

public interface DaoFace<T> {

	// ��
	public void add(T item);

	// ɾ
	public void delete(T item);

	// ��
	public void update(T item);

	// ��
	public T findById(Serializable id); // by id

	public List<T> search(T cond); // by column

	public List<T> findAll(); // find all
	
	public Session getSession();
}