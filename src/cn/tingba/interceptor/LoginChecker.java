package cn.tingba.interceptor;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import cn.tingba.common.Const;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;

//����Ƿ񾭹���¼��������
public class LoginChecker implements Interceptor {
	//����Ҫ����url
	List<String> pathsNotNeedCheck = new ArrayList<String>();
	
	//��дInterceptor�ӿڵķ�������ʼ��������
	public void init() { 
		//ת���¼�͵�¼������Ҫ���
		pathsNotNeedCheck.add("/imageAction"); // ��֤��
		pathsNotNeedCheck.add("/top10"); // ��ҳ
		pathsNotNeedCheck.add("/search"); // ����
		pathsNotNeedCheck.add("/go_login"); // ��ת����½ҳ
		pathsNotNeedCheck.add("/go_register"); // ��ת��ע��ҳ��
		pathsNotNeedCheck.add("/user_login"); // �ύ��½����
		pathsNotNeedCheck.add("/user_register"); // �ύע������
		pathsNotNeedCheck.add("/song_playSong"); // ���Ÿ���
		System.err.println("intercepter INIT..."); // 
	}

	//ÿ�εĵ�ַ�����󣬶���ִ�д˷���
	public String intercept(ActionInvocation invocation) throws Exception {
		System.err.println("intercepter RUNNING...");
		String result; // ���ص��߼���ͼ��
		HttpServletRequest request = ServletActionContext.getRequest();
		String path = request.getServletPath(); //��������url
		int pos = path.indexOf(".action"); 
		if (pos != -1) {
			path = path.substring(0, pos); 
		}
		if (pathsNotNeedCheck.contains(path)) {//������Ҫ����
			result = invocation.invoke();//����
		} else { //���򣬼���Ƿ񾭹��˵�¼
			HttpSession session = request.getSession(false);
			if (session != null && session.getAttribute(Const.LOGINED_USER) != null) {
				result = invocation.invoke(); //�����˵�¼�������
			} else {
				result = "login"; //δ����¼��ת���¼ҳ
			}
		}
		return result;
	}

	//��дInterceptor�ӿڵķ���������������
	public void destroy() {
	}
}
