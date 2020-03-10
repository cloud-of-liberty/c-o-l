package jp.col.controller.user;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.col.Model.PassChangeModel;
import jp.col.Model.UserModel;
import jp.col.dao.IUserDao;
import jp.col.dao.UserDaoImpl;

@Controller

public class PassChangeController {

	IUserDao dao;

	@RequestMapping("/passwordChangeInit")
    public String passwordChangeInit(Map<String, Object> model ,HttpSession ses) {
	  	try{
	  		Object userObj = ses.getAttribute("user");
	  		if(userObj == null){
	            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
	            return "login";
	  		}

	  		PassChangeModel passchange = new PassChangeModel();
	  		String email = ((UserModel)userObj).getEmail();
	  		passchange.setEmail(email);
            model.put("passchange", passchange);
	  		
	        return "passchange";
	  	} catch (Exception e) {
	      	model.put("message", e.toString());
		    return "error";
	    }
    }

  @RequestMapping("/passChange")
  public String passChange(@Validated PassChangeModel passchange,BindingResult bindingResult,Map<String, Object> model,HttpSession ses ) {
  	try{
  		Object userObj = ses.getAttribute("user");
  		if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
  		}
        if(bindingResult.hasErrors()){
        	StringBuilder errorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
            	errorMessage.append(fieldError.getDefaultMessage());
            	errorMessage.append("\n");
            }
            model.put("passchange", passchange);
      		model.put("message", errorMessage.toString());
            return "passchange";
        }
        UserModel user = (UserModel)userObj;
        //パスワード重複チェック
        if (passchange.getCurrentPassword().equals(passchange.getNewPassword())){
            model.put("passchange", passchange);
      		model.put("message", "このパスワードは使用されていたためご利用になれません。");
            return "passchange";
        }
        if (!passchange.getNewPassword().equals(passchange.getPasswordConfirm())){
            model.put("passchange", passchange);
      		model.put("message", "パスワード（確認入力）と新しいパスワードが一致しません。");
            return "passchange";
        }
        if (!BCrypt.checkpw(passchange.getCurrentPassword() , user.getPassword())){
            model.put("passchange", passchange);
      		model.put("message", "パスワードが正しくありません。");
            return "passchange";
        }
        String passHash = BCrypt.hashpw(passchange.getNewPassword(), BCrypt.gensalt());
        user.setPassword(passHash);
        dao = new UserDaoImpl();
        dao.updatePassword(user);
		ses.invalidate();
        model.put("message", "パスワードの変更が完了しました。もう一度ログインしてください。");
    	return "login";
  	} catch (Exception e) {
      	model.put("message", e.toString());
      return "error";
    }
  }
}