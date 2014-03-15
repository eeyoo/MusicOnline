package cn.tingba.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.tingba.common.Const;
import cn.tingba.entity.Song;
import cn.tingba.entity.User;
import cn.tingba.entity.util.SortSongList;
import cn.tingba.service.SongService;
import cn.tingba.service.UserLogService;
import cn.tingba.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String username;
	private String password;
	
	private UserService userService = new UserService();
	private SongService songService = new SongService();
	private UserLogService userLogService = new UserLogService();
	
	public String login() {
		// ����Ƿ� �Ѿ���½��
		Map<String, Object> session = ActionContext.getContext().getSession();
		if (session.get(Const.LOGINED_USER) != null) { 
			return SUCCESS; // ����Ѿ���¼��ֱ������
		}
		
		// ��֤�û�������
		User user = userService.findByNamePass(username, password);
		if ( user == null ) { // execute SQL select
			addFieldError("username", "�û������������");
			return INPUT;	
		}
		
		User curUser = user; // userService.getUser(username);  // execute SQL select
		System.err.println("��ǰ�û�:" + curUser);
		
		List<Song> mySongList = new ArrayList<Song>(curUser.getSongs()); 
		SortSongList.byTime(mySongList); // SORT Songs
		
		session.put(Const.MY_SONG_LIST, mySongList);
		session.put(Const.LOGINED_USER, curUser); //д����Ϣ������ѵ�¼���û�

		userLogService.logUserAction(curUser, "LOGIN");
		return SUCCESS;		
	}
	
	public String logout() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		if(session.get(Const.LOGINED_USER) != null) {
			userLogService.logUserAction((User)session.get(Const.LOGINED_USER), "LOGOUT");
			session.remove(Const.LOGINED_USER); //ɾ��֮ǰд���û�����Ϣ	
		}
		
		if( session.get(Const.MY_SONG_LIST) != null ) { 
			session.remove(Const.MY_SONG_LIST); // ɾ�����ϴ��ĸ����б� 
		}
		
		if( session.get(Const.CURRENT_SONG_LIST) != null ) { 
			session.remove(Const.CURRENT_SONG_LIST); // ɾ�� �����б� 
		}
		return SUCCESS;
	}
 
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
