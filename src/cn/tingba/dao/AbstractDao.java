package cn.tingba.dao;

import java.io.Serializable;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;

import cn.tingba.util.HibernateSessionFactory;

public abstract class AbstractDao {
	static Session session = null;
	
	protected AbstractDao() {
		if( session == null ) { // ����ʽ ����ģʽ
			session = HibernateSessionFactory.getSession();
			System.err.println("��ȡHibernate.Session...");
		}
	}
	
//	protected void finalize() throws Throwable {
////		session.close();
//		System.err.println("DAO�����գ��ر�Hibernate.session...");
//	}
	
	protected Session getSession() {
		return session;
	}
	
	/**
	 * �������
	 * not-null�ֶα�����ֵ
	 * @param object
	 */
	protected void insert(Object object){
		Transaction tran=null;
		//��ȡsession
//		Session session=HibernateSessionFactory.getSession();
		try{
			//��ʼ����
			tran=session.beginTransaction();
			//�־û�����
		    session.save(object); // **
		    //�ύ����
		    tran.commit();
		}catch (Exception e) {
			if(tran!=null){
				//����ع�
				tran.rollback();
			}
			e.printStackTrace();
		}finally{
			//�ر�session
//			session.close();
		}		
	}	
	
	/**
	 * ��������
	 * ����id�ֶβ�ѯ
	 * @param cla
	 * @param id
	 * @return
	 */
	protected Object get(Class cla,Serializable id){
		Object object=null;
//		Session session=HibernateSessionFactory.getSession();
		try{
			object=session.get(cla, id); // **
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
//			session.close();
		}
		return object;
	}	
	
	/**
	 * ɾ������
	 * @param object
	 */
	protected void remove(Object object){
		Transaction tran=null;
//		Session session=HibernateSessionFactory.getSession();
		try{
			tran=session.beginTransaction();
		    session.delete(object); // **
		    tran.commit();
		}catch (Exception e) {
			if(tran!=null){
				tran.rollback();
			}
			e.printStackTrace();
		}finally{
//			session.close();
		}
	}	
	
	/**
	 * �޸�����
	 * @param object
	 */
	protected void rewrite(Object object){
		Transaction tran=null;
//		Session session=HibernateSessionFactory.getSession();
		try{
			tran=session.beginTransaction();
		    session.update(object); // **
		    tran.commit();
		}catch (Exception e) {
			if(tran!=null){
				tran.rollback();
			}
			e.printStackTrace();
		}finally{
//			session.close();
		}
	}	
	
	/**
	 * ��ѯ����
	 * ����null�ֶβ�ѯ
	 * @param cla
	 * @param condition
	 * @return
	 */
	protected List find(Class cla,Object condition){
//		Session session=null;
		List list=null;
		try {
//			session = HibernateSessionFactory.getSession();
			list = session.createCriteria(cla).add(Example.create(condition)).list();
		} catch (Exception e) {
			// TODO: handle exception
		} finally{
//			session.close();
		}
		return list;		
	}

}
