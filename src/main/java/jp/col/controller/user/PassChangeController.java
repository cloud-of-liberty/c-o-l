package jp.col.controller.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.col.Model.DailyReportModel;
import jp.col.Model.PassChangeModel;
import jp.col.Model.UserModel;
import jp.col.dao.IUserDao;
import jp.col.dao.UserDaoImpl;

@Controller

public class PassChangeController {

	IUserDao dao;

	@RequestMapping("/PassChange")
    public ModelAndView passwordChangeInit(RedirectAttributes attributes , Map<String, Object> model ,HttpSession ses,
			@ModelAttribute(name = "message") String errorMessage ,
			@ModelAttribute(name = "errorflg") String errorflg) {

		ModelAndView view = new ModelAndView();
	  	try{
	  		Object userObj = ses.getAttribute("user");
	  		if(userObj == null){
				attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
				view.setViewName("redirect:login");
				return view;
	  		}
			if ("1".equals(errorflg)) {
	      		model.put("message", errorMessage);
			}
	  		PassChangeModel passchange = new PassChangeModel();
	  		String email = ((UserModel)userObj).getEmail();
	  		passchange.setEmail(email);
            model.put("passchange", passchange);
	  		
			view.setViewName("passchange");
			return view;
	  	} catch (Exception e) {
	  		attributes.addFlashAttribute("message", e.toString());
	  		view.setViewName("redirect:systemerror");
			return view;
	    }
    }

	@RequestMapping("/savePassChange")
	public ModelAndView passChange(RedirectAttributes attributes , @Validated PassChangeModel passchange,BindingResult bindingResult,Map<String, Object> model,HttpSession ses ) {
		ModelAndView view = new ModelAndView();

		try{
			Object userObj = ses.getAttribute("user");
			if(userObj == null){
				attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
				view.setViewName("redirect:login");
				return view;
			}
        if(bindingResult.hasErrors()){
        	StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
            	errorMessage.append(fieldError.getDefaultMessage());
            	errorMessage.append("\n");
            }
			attributes.addFlashAttribute("message", errorMessage.toString());
			attributes.addFlashAttribute("errorflg", "1");
			view.setViewName("redirect:PassChange");
			return view;
        }
        UserModel user = (UserModel)userObj;
        //パスワード重複チェック
        if (passchange.getCurrentPassword().equals(passchange.getNewPassword())){
			attributes.addFlashAttribute("message", "このパスワードは使用されていたためご利用になれません。");
			attributes.addFlashAttribute("errorflg", "1");
			view.setViewName("redirect:PassChange");
			return view;
        }
        if (!passchange.getNewPassword().equals(passchange.getPasswordConfirm())){
			attributes.addFlashAttribute("message", "パスワード（確認入力）と新しいパスワードが一致しません。");
			attributes.addFlashAttribute("errorflg", "1");
			view.setViewName("redirect:PassChange");
            return view;
        }
        if (!BCrypt.checkpw(passchange.getCurrentPassword() , user.getPassword())){
			attributes.addFlashAttribute("message", "パスワードが正しくありません。");
			attributes.addFlashAttribute("errorflg", "1");
			view.setViewName("redirect:PassChange");
            return view;
        }
        String passHash = BCrypt.hashpw(passchange.getNewPassword(), BCrypt.gensalt());
        user.setPassword(passHash);
        dao = new UserDaoImpl();
        dao.updatePassword(user);
		ses.invalidate();
		attributes.addFlashAttribute("message", "パスワードの変更が完了しました。もう一度ログインしてください。");
		view.setViewName("redirect:login");
        return view;
  	} catch (Exception e) {
  		attributes.addFlashAttribute("message", e.toString());
  		view.setViewName("redirect:systemerror");
		return view;
    }
  }
}