package ajax.controller.chap4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import common.controller.AbstractController;

public class SecondPersonArrJSONAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		HashMap<String,String> personMap1 = new HashMap<String, String>();
		
		personMap1.put("name", "이순신");
		personMap1.put("age", "27");
		personMap1.put("phone", "01078904562");
		personMap1.put("email", "leess@gmail.com");
		personMap1.put("addr", "서울시 중구 남대문로 24");
		
		HashMap<String,String> personMap2 = new HashMap<String, String>();
		
		personMap2.put("name", "홍길동");
		personMap2.put("age", "37");
		personMap2.put("phone", "01012345678");
		personMap2.put("email", "hongkd@gmail.com");
		personMap2.put("addr", "서울시 강남구 압구정");
		
		HashMap<String,String> personMap3 = new HashMap<String, String>();
		
		personMap3.put("name", "엄정화");
		personMap3.put("age", "25");
		personMap3.put("phone", "01014785236");
		personMap3.put("email", "eom@gmail.com");
		personMap3.put("addr", "서울시 강북구 수유동");
		
		List<HashMap<String,String>> personList = new ArrayList<HashMap<String,String>>();
		
		personList.add(personMap1);
		personList.add(personMap2);
		personList.add(personMap3);
		
		JSONArray jsonArr = new JSONArray();
		
		for(HashMap<String,String> map : personList) {
			
			JSONObject jsonPerson = new JSONObject();
		
			jsonPerson.put("name", map.get("name"));
			jsonPerson.put("age", map.get("age"));
			jsonPerson.put("phone", map.get("phone"));
			jsonPerson.put("email", map.get("email"));
			jsonPerson.put("addr", map.get("addr"));
			
			jsonArr.add(jsonPerson);
			
		}
		
		String str_jsonArr = jsonArr.toString();
		//JSONArray jsonArr 객체를 String 형태로 변환
		
		req.setAttribute("str_jsonArr", str_jsonArr);
		
		//System.out.println("str_jsonArr " + str_jsonArr );
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/2personArrJSON.jsp");
		
	}

}
