package cn.tingba.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import cn.tingba.dao.SongDao;
import cn.tingba.dao.impl.SongDaoImpl;
import cn.tingba.entity.Song;

public class SongTopNService {
	
	SongDao dao = new SongDaoImpl();
	
	public List<Song> byProperty(String propertyName, boolean asc, int n) {
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		
		// ����������
		if(asc)
			criteria.addOrder(Order.asc(propertyName));
		else 
			criteria.addOrder(Order.desc(propertyName)); // MAGIC
		
		// ����ץȡ��Ŀ
		criteria.setMaxResults(n); // criteria.setFetchSize(n);
		
		List list = criteria.list();
		return list;
	}
		
	public List<Song> mostPopular(int n) {
		// TODO �ҵ��������n�׸�
		return byProperty("playcount", false, n);
	}
	
	public List<Song> mostRecently(int n) {
		// TODO �ҵ�����ϴ���n�׸�
		return byProperty("uploadTime", false, n);
	}
	
	public Map<String, List<Song>> eachStyleTopN(int nTop) {
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		
		// 1.�Ȼ�ȡ����style
		criteria.setProjection(Projections.groupProperty("style"));
		List list = criteria.list();	
		ArrayList<String> styleList = (ArrayList<String>) list;
//		for(Object obj : list) System.err.println((String)str);
		
		if(styleList == null || styleList.size()==0) return null;
		
		Map<String, List<Song>> result = new TreeMap<String, List<Song>>();
		
		// 2. ��ȡÿ�� style�� TOP n
		for (String style : styleList){
			Criteria cri = dao.getSession().createCriteria(Song.class);
			
			// ����ץȡ��Ŀ
//			cri.setFetchSize(nTop);
			cri.setMaxResults(nTop);
			
			// ���ҵ�ǰ ���
			cri.add(Restrictions.eq("style", style));
			
			// �����Ŵ�������
			cri.addOrder(Order.desc("playcount"));	
			
			result.put(style, cri.list()); // ִ�в�ѯ������¼���
		} 

		return result;
	}
}
