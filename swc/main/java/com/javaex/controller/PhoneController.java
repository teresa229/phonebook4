package com.javaex.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.javaex.dao.PhoneDao;
import com.javaex.vo.PersonVo;

@Controller  //첫자 대문자 주의
@RequestMapping(value="/phone")
public class PhoneController {

	// 필드
	@Autowired
	private PhoneDao phoneDao; //대상에 넣지 않으면 오류가 난다.  applicationContext.xml
	
	// 생성자 - 디폴트 사용
	// 메소드 g/s
	// 메소드 일반

	/** 메소드 일반 **/
	
	/* list */
	@RequestMapping(value ="/list", method = {RequestMethod.GET, RequestMethod.POST})
	public String list(Model model) {                                                                  //model 내가 지어준 이름. Model: import까지, 만들어 달라고 요청해야 한다. Model model = new Model(); 하면 안된다.
		System.out.println("list");
		
		//dao를 통해 리스트를 가져온다.
		//PhoneDao phoneDao = new PhoneDao();
		List<PersonVo> personList = phoneDao.getPersonList();
		System.out.println(personList.toString());
		
		// model --> data를 보내는 방법 -> 담아 놓으면 된다.
		model.addAttribute("pList", personList);   //setAttribute 비슷하게 담는다. ("만들어 준 이름",파일명)
		
		return "list";                             // "/WEB-INF/views/list.jsp" - forward 부분 spring-servlet에서 정리해 줌 
	}
	
	/* writeForm */
	@RequestMapping(value ="/writeForm", method = {RequestMethod.GET, RequestMethod.POST})
	public String writeForm() {
		System.out.println("writeForm");
		
		return "writeForm";
	}
	
	/* write */
	@RequestMapping(value="/write", method= {RequestMethod.GET, RequestMethod.POST})
	public String write(@RequestParam("name") String name,
						@RequestParam("hp") String hp,
						@RequestParam("company") String company) {
		
		System.out.println("write");
		
		System.out.println(name + "," + hp + "," + company);
		
		PersonVo personVo = new PersonVo(name, hp, company);
		System.out.println(personVo.toString());
		
		//PhoneDao phoneDao = new PhoneDao();
		phoneDao.personInsert(personVo);
		
		return "redirect:/phone/list";
	}

	/* modifyForm */
	@RequestMapping(value="/modifyForm", method= {RequestMethod.GET,RequestMethod.POST})
	public String modifyForm(@RequestParam("personId") int id, Model model) {  //Model model의 위치는 앞뒤 상관없다.
		System.out.println("modifyForm");
		System.out.println(id);
		
		//VO에 담아주어 보내기
		//PhoneDao phoneDao = new PhoneDao();
		PersonVo personVo = phoneDao.getPerson(id); //personId와 비교해 봐라.
		
		//DB에서 잘 받아왔는지 확인!
		System.out.println(personVo.toString());
		
		//model에 담아주기
		model.addAttribute("pvo", personVo);  //(별명, 실제 데이터 주소) -> 알아서 request Attribute에 넣어준다 ->forward가 이루어진다.
		
		return "modifyForm"; 
	}
	
	// 수정 --> @RequestParam
	@RequestMapping(value="/modify2", method= {RequestMethod.GET, RequestMethod.POST})
	public String modify2(@RequestParam("personId") int id, @RequestParam("name") String name, @RequestParam("hp") String hp, @RequestParam("company") String company) {
		System.out.println("수정2");
		
		PersonVo personVo = new PersonVo(id, name, hp, company);	 //내부에서 사용하는 id --> int id 
		
		System.out.println(personVo.toString());
		
		/* modify */
		//PhoneDao phoneDao = new PhoneDao();
		phoneDao.personUpdate(personVo);  //디비에 저장이 확실하게 되었는지 확인해주는 것이 좋다. --> sql로 확인
		
		return "redirect:/phone/list";
	}
	
	// 수정 --> 문법 : @ModelAttribute
	@RequestMapping(value="/modify", method= {RequestMethod.GET, RequestMethod.POST})
	public String modify(@ModelAttribute PersonVo personVo) {
		//@ModelAttribute+ 자료형 + 이름 담아준다-> 디폴트로 생성자가 있어야 함 -> set으로 데이터를 불러온다.
		
		System.out.println("수정"); 
		
		//이런  처리가 되고 있을 것이다.
		//PersonVo personVo = new PersonVo();	
		//personVo.setPersonId(personId);    <--set으로 된 데이터 읽기.
		
		System.out.println(personVo.toString());
		
		/* modify */
		//PhoneDao phoneDao = new PhoneDao();
		phoneDao.personUpdate(personVo);  
		
		return "redirect:/phone/list";
	}
	
	// 삭제 --> @RequestMapping 약식 표현
	@RequestMapping(value="/delete2", method= {RequestMethod.GET, RequestMethod.POST})
	public String delete2(@RequestParam("personId") int id) { //id에 담아줘.. 그럼 id 변수 이름을 꺼내줘
		System.out.println("삭제");
		System.out.println(id);                              //id에 담아줘.. 그럼 id 변수 이름을 꺼내줘  -영향 1
		
		/* delete */
   		//PhoneDao phoneDao = new PhoneDao();         
   		phoneDao.personDelete(id);                           //id에 담아줘.. 그럼 id 변수 이름을 꺼내줘 -영향 2
		  		
		return "redirect:/phone/list";
	}
	
	// 삭제 --> 문법 : @PathVariable 간단한 url만들기
	@RequestMapping(value="/delete/{personId}", method= {RequestMethod.GET, RequestMethod.POST}) //{변수값} 무슨값을 적어도 상관없음. 2개일 수도 있다. 주소영역에 파라미터값을 넣어주는 것이다.
	public String delete(@PathVariable("personId") int id) { //url에서 꺼내서 id에 담아줘.. //@RequestParam("personId") int id : 파라미터에서 꺼내서 id에 담아줘.
		System.out.println("삭제");
		System.out.println(id);                             
		
		/* delete */
   		//PhoneDao phoneDao = new PhoneDao();         
   		phoneDao.personDelete(id);                           
		  		
		return "redirect:/phone/list";
	}
}
