package cn.tingba.action;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.Servlet;

import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;

import cn.tinba.comon.Const;
import cn.tingba.entity.Song;
import cn.tingba.entity.User;
import cn.tingba.service.SongService;
import cn.tingba.service.UserLogService;
import cn.tingba.service.UserService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class UploadAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private File song;
	private String songFileName;
	private String songContentType; // �ļ���ʽ�� �����ɴ�У��
	private String name;
	private String singer;
	private String style;
	private String description;
	private String validateNum;
	private String serverFilePath;

	public static String SONG_REPOSITORY = "upload/song"; // repository
	
	private UserService userService = new UserService();
	private SongService songService = new SongService();
	private UserLogService userLogService = new UserLogService();
	
	public String execute() throws Exception {
		// У����֤��
		String randstr = (String) ActionContext.getContext().
						getSession().get(ImageAction.IMAGE_STR_KEY);
		if (!randstr.equals(validateNum)) {
			addFieldError("validateNum", "��֤���������");
			return INPUT;
		}
		
		// ����ļ���ʽ
		if (!songContentType.equals("audio/mp3")) {
			addFieldError("song", "�ļ���ʽ��֧�֣�");
			return INPUT;
		}
		
		// ʵ�ָ����ϴ�
		String repoPath = ServletActionContext.getServletContext().getRealPath(SONG_REPOSITORY);
		
		String uuidName = UUID.randomUUID().toString();
		if(songFileName.indexOf('.') != -1) {
			int dot = songFileName.lastIndexOf('.');
			uuidName += songFileName.substring(dot); // ��չ��
		}	
		
		if(song != null) {
			serverFilePath = SONG_REPOSITORY + "/" + uuidName;
			File saveFile = new File(new File(repoPath), uuidName);
			if(!saveFile.getParentFile().exists()) {
				saveFile.getParentFile().mkdirs();
			}
			
			int KBs = (int)Math.round(song.length()/1024.0);
			
//			System.out.println("saveFile:" + saveFile.getPath());
			System.err.println("���ϴ����ļ���ʽΪ��" + songContentType);
			System.err.println("���������ļ���Ϊ��" + serverFilePath);
			System.err.println("�ļ���С��" + song.length());

			FileUtils.copyFile(song, saveFile); // ����
			
			// �Ǽǵ����ݿ�
			Map<String, Object> session = ActionContext.getContext().getSession();
			User user = (User)session.get(Const.LOGINED_USER);
			Song song = songService.register(name, singer, style, description, serverFilePath, user);
			userService.useSpace(user, KBs);
			
			// ����ȫ�������б�
//			List<Song> songList = songService.listAll();
//			
//			if( session.get(Const.ALL_SONG_LIST) != null ) { // ����Ѿ���ѯ���ˣ�ɾ���ϴβ�ѯ���
//				session.remove(Const.ALL_SONG_LIST);
//			}
//			session.put(Const.ALL_SONG_LIST, songList);
			
			// �������ϴ��ĸ����б�
			List<Song> mySongList = (List<Song>)session.get(Const.MY_SONG_LIST);
			if( mySongList == null) {
				mySongList = new ArrayList<Song>();		
				session.put(Const.MY_SONG_LIST, mySongList);
			}
			mySongList.add(song);
			
			System.err.println("current user:" + user);
			
			userLogService.logUserAction(user, "UPLOAD");
			return SUCCESS;
		}
		else {
			System.err.println("�����ļ�Ϊ�գ�");
			return INPUT;
		}
	}
	
	// getter and setters

	public void setSongContentType(String songContentType) {
		this.songContentType = songContentType;
	}

	public String getSongContentType() {
		return songContentType;
	}

	public File getSong() {
		return song;
	}

	public void setSong(File song) {
		this.song = song;
	}

	public String getSongFileName() {
		return songFileName;
	}

	public void setSongFileName(String songFileName) {
		this.songFileName = songFileName;
	}	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSinger() {
		return singer;
	}

	public void setSinger(String singer) {
		this.singer = singer;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getServerFilePath() {
		return serverFilePath;
	}

	public void setServerFilePath(String serverFilePath) {
		this.serverFilePath = serverFilePath;
	}

	public String getValidateNum() {
		return validateNum;
	}

	public void setValidateNum(String validateNum) {
		this.validateNum = validateNum;
	}
}
