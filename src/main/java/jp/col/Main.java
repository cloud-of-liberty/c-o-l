package jp.col;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@SpringBootApplication
public class Main {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);
	}

	@RequestMapping("/")
	ModelAndView index() {
		ModelAndView view = new ModelAndView();
		view.setViewName("redirect:login");
		return view;
	}

	@RequestMapping("/login")
	ModelAndView login(@ModelAttribute(name = "message") String message ,HttpSession ses, Map<String, Object> model) {
		ModelAndView view = new ModelAndView();
		view.setViewName("login");
		ses.invalidate();
		if (StringUtils.isNotEmpty(message)) {
			model.put("message", message);
		}
		return view;
	}

	@RequestMapping("/systemerror")
	ModelAndView systemerror(@ModelAttribute(name = "message") String message , Map<String, Object> model ) {
		ModelAndView view = new ModelAndView();
		view.setViewName("error");
		return view;
	}
}
