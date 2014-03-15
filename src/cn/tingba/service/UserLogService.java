package cn.tingba.service;

import java.sql.Timestamp;
import java.util.Date;

import cn.tingba.dao.UserLogDao;
import cn.tingba.dao.impl.UserLogDaoImpl;
import cn.tingba.entity.User;
import cn.tingba.entity.UserLog;

public class UserLogService {
	UserLogDao dao = new UserLogDaoImpl();
	
	public void logUserAction(User user, String userAction) {
		// ʵ���û���Ϊ�Ǽ�
		UserLog log = new UserLog();
		log.setUser(user);
		log.setAction(userAction);
		log.setTime(new Timestamp(new Date().getTime()));
		dao.add(log);
		System.err.println("�ѵǼ��û���" + user + "\n��Ϊ��" + userAction);
	}
}
