package cn.tingba.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import cn.tingba.dao.SongDao;
import cn.tingba.dao.impl.SongDaoImpl;
import cn.tingba.entity.Song;
import cn.tingba.entity.User;
import cn.tingba.util.HibernateSessionFactory;

public class SongService {
	
	SongDao dao = new SongDaoImpl();
	
	public Song findById(Integer songId) {
		return dao.findById(songId);
	}
	
	public void logPlay(Song song) {
		// DEBUG
		System.err.println("Song:" + song);
		System.err.println("�Ѿ�����" + song.getPlaycount() + "��");
		// DEBUG
		int playCount = song.getPlaycount();
		++playCount;
		song.setPlaycount(playCount);
		dao.update(song);
	}
	
	public void logGood(Song song) {
		// DEBUG
		System.err.println("Song:" + song);
		System.err.println("�Ѿ�����" + song.getGoodcount() + "��");
		// DEBUG
		int goodCount = song.getGoodcount();
		++goodCount;
		song.setGoodcount(goodCount);
		dao.update(song);		
	}
	
	public void logBad(Song song) {
		// DEBUG
		System.err.println("Song:" + song);
		System.err.println("�Ѿ�����" + song.getBadcount() + "��");
		// DEBUG
		int badCount = song.getBadcount();
		++badCount;
		song.setBadcount(badCount);
		dao.update(song);			
	}
	
	/**
	 * ע��������ϴ�ʱ��
	 * */
	public Song register(String name, String singer, String style, String description, String savePath, User user) {
		Song song = new Song();
		// basic info
		song.setName(name);
		song.setSinger(singer);
		song.setStyle(style);
		song.setDescription(description);
		// upload info
		song.setUser(user);
		song.setSavepath(savePath);
		song.setUploadTime(new Timestamp(new Date().getTime()));
		
		// play info(��ѡ)
		song.setGoodcount(0);
		song.setBadcount(0);
		song.setPlaycount(0);
		song.setComcount(0);
		
		dao.add(song); // write to DB.
		return song;
	}

	/**
	 * �г�ȫ��
	 * */
	public List<Song> listAll() {
		return dao.findAll();
	}
	
	/**
	 * ��һ��Id�б�ȡ����
	 * */
	public List<Song> getSongList(List<Integer> songIdList) {
		if( songIdList == null || songIdList.size() == 0 ) {
			return null;
		}
		List<Song> res = new ArrayList<Song>();
		for( Integer sid : songIdList ) {
			res.add(dao.findById(sid));
		}
		return res;
	}
	
	/**
	 * ������ ģ��(like)��ѯ
	 * */
	public List<Song> searchByName(String songName) {
		if(songName == null || songName.equals("")) {
			return null;
		}
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(Restrictions.like("name", songName)); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();
		return list;
	}

	/**
	 * ������ ģ��(like)��ѯ
	 * */
	public List<Song> searchBySinger(String singer) {
		if(singer == null || singer.equals("")) {
			return null;
		}
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(Restrictions.like("singer", singer)); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();
		return list;
	}
	
	/**
	 * �����  ģ��(like)��ѯ
	 * */
	public List<Song> searchByStyle(String Style) {
		if(Style == null || Style.equals("")) {
			return null;
		}
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(Restrictions.like("style", Style)); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();
		return list;		
	}
	
	public List<Song> searchByDescription(String Desc) {
		if(Desc == null || Desc.equals("")) {
			return null;
		}
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(Restrictions.like("description", Desc)); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();
		return list;			
	}
	
	public List<Song> searchByAllTextInfo(String value) {
		if(value == null || value.equals("")) {
			return null;
		}
		Session session = dao.getSession(); // (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(	
						Restrictions.or(
							Restrictions.like("name", value),
							Restrictions.or(
								Restrictions.like("singer", value),
								Restrictions.or(
									Restrictions.like("style", value),
									Restrictions.like("description", value)
								)								
							)
					  	)
					 ); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();	
		return list;
	}
	
	/**
	 * ���û���ѯ
	 * */	
	public List<Song> searchByUser(User user) {
		if(user == null || user.equals("")) {
			return null;
		}
		Session session = (Session) HibernateSessionFactory.getSession();
		Criteria criteria = session.createCriteria(Song.class);
		criteria.add(Restrictions.eq("user", user)); // MAGIC
//		criteria.addOrder(Order.asc("id"));
		List list = criteria.list();
		return list;
//		Song song = new Song();
//		song.setUser(user);
//		return dao.search(song);
	}
}
