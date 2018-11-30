package ajax.controller.chap4;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;

import common.controller.AbstractController;

public class FirstPersonJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HashMap<String,String> personMap = new HashMap<String, String>();
		
		personMap.put("name", "이순신");
		personMap.put("age", "27");
		personMap.put("phone", "01074567876");
		personMap.put("email", "leess@gmail.com");
		personMap.put("addr", "서울시 중구 남대문로 24");
		
		JSONObject jsonPerson = new JSONObject();
		
		jsonPerson.put("name", personMap.get("name"));
		jsonPerson.put("age", personMap.get("age"));
		jsonPerson.put("phone", personMap.get("phone"));
		jsonPerson.put("email", personMap.get("email"));
		jsonPerson.put("addr", personMap.get("addr"));
		
		String str_jsonPerson = jsonPerson.toString();
		//JSONObject jsonPerson 객체를 String 형태로 변환
		
		req.setAttribute("str_jsonPerson", str_jsonPerson);
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/1personJSON.jsp");
		
	}

}
