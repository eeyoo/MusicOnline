package cn.tingba.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cn.tinba.comon.Const;
import cn.tingba.entity.Song;
import cn.tingba.service.SongService;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;

public class PlayAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String audioSrc; // audioSrc���������
	private ArrayList<Integer> songIdList; // for song_addToList
	private Integer index; // for song_removeItem
	private boolean uniqueAdd; // for checkbox
	private Integer playIndex;
	
	private SongService songService = new SongService();

	public String goodSong() {
		if(playIndex == null) {
			System.err.println("û��playIndex");
			return SUCCESS;
		}
		Map<String, Object> session = ActionContext.getContext().getSession();
		List<Song> curSongList = (List<Song>) session.get(Const.CURRENT_SONG_LIST);
		
		Song song = curSongList.get(playIndex);
		songService.logGood(song);
		return SUCCESS;
	}
	
	public String badSong() {
		if(playIndex == null) {
			System.err.println("û��playIndex");
			return SUCCESS;
		}
		Map<String, Object> session = ActionContext.getContext().getSession();
		List<Song> curSongList = (List<Song>) session.get(Const.CURRENT_SONG_LIST);
		
		Song song = curSongList.get(playIndex);
		songService.logBad(song);
		return SUCCESS;
	}
	
	public String addToList() {
		System.err.println("uniqueAdd: " + uniqueAdd);
		
		List<Song> newSongList = songService.getSongList(songIdList);
		
		Map<String, Object> session = ActionContext.getContext().getSession();
		List<Song> curSongList = (List<Song>) session.get(Const.CURRENT_SONG_LIST);
		
		// ��ǰ��  ��  �ύ�Ķ�Ϊ�� ==> û�и������Բ���
		if(newSongList == null && curSongList == null) {
			addFieldError("hint", "�����б�Ϊ�գ���ѡ�������");
			return INPUT; // ����ת������ҳ��
		}
		
		// �ύ�� Ϊ�գ���ǰ���գ�ֱ�ӹ�ȥ 
		if(newSongList == null) return SUCCESS;
		
		if( curSongList == null ) { // ��ǰΪ��
			session.put(Const.CURRENT_SONG_LIST, newSongList);
			curSongList = newSongList;
			for( Song song : newSongList ) {
				songService.logPlay(song);   // �Ǽǲ���
			}
		}
		else { // ��ǰ����
			if( uniqueAdd ) { // ���ظ� ���
				for(Song s : newSongList) {
					// ������ڲ����б���
					if( ! curSongList.contains(s) ) {
						curSongList.add(s);
						songService.logPlay(s); // �Ǽǲ���
					}
				}
			}
			else { // �ظ����
				curSongList.addAll(newSongList);
			}
		}
		
		audioSrc = curSongList.get(0).getSavepath();
		
		return SUCCESS;
	}
	
	public String removeItem() {
		Map<String, Object> session = ActionContext.getContext().getSession();
		List<Song> curSongList = (List<Song>) session.get(Const.CURRENT_SONG_LIST);
		if( curSongList == null || index == null ) {
			System.err.println("�����б�Ϊ�գ���indexΪ��");
			return SUCCESS;
		}
		
//		index -= 1; // ҳ���ϻ�ȡ�������� ��1��ʼ
		if( index > curSongList.size() ) {
			return SUCCESS;
		}
		
		curSongList.remove(index-1); 
		
		if( curSongList.size() >= 1 ) 
			this.audioSrc = curSongList.get(0).getSavepath();
		else // û�и����� 
			return INPUT; // TODO �Ժ��ٿ�Ҫ��Ҫ�ģ���ʱ��������ȥ
		
		return SUCCESS;
	}
	
	public String playSong() {
		//  ʵ�ֲ���һ�׸�
		System.err.println("׼��������ƵԴ��" + audioSrc);
		return SUCCESS;
	}	
	
	// getter and setters

	public String getAudioSrc() {
		return audioSrc;
	}

	public void setAudioSrc(String audioSrc) {
		this.audioSrc = audioSrc;
	}

	public void setSongIdList(ArrayList<Integer> songIdList) {
		this.songIdList = songIdList;
	}

	public ArrayList<Integer> getSongIdList() {
		return songIdList;
	}

	public Integer getIndex() {
		return index;
	}

	public void setIndex(Integer index) {
		this.index = index;
	}

	public void setUniqueAdd(boolean uniqueAdd) {
		this.uniqueAdd = uniqueAdd;
	}

	public boolean isUniqueAdd() {
		return uniqueAdd;
	}

	public void setPlayIndex(Integer playIndex) {
		this.playIndex = playIndex;
	}

	public Integer getPlayIndex() {
		return playIndex;
	}

}
