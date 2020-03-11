package jp.col.controller.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.col.Model.CertificationModel;
import jp.col.Model.ContractInfoModel;
import jp.col.Model.UserModel;
import jp.col.dao.IUserDao;
import jp.col.dao.UserDaoImpl;
import jp.col.utils.AesUtil;

import org.mindrot.jbcrypt.BCrypt;

@Controller

public class UserController {

	IUserDao dao;

  @RequestMapping("/login")
  String login(UserModel user,Map<String, Object> model,HttpSession ses , HttpServletResponse response) {
  	try{
		String encryptedPass = user.getPassword();
		String decryptedPass = AesUtil.aesDecrypt(encryptedPass);
  		dao = new UserDaoImpl();
  		user = dao.findByEmail(user.getEmail());
  		if (user == null) {
      	    model.put("message", "該当ユーザが存在しません。");
            return "login";
  		} else {
  			if (BCrypt.checkpw(decryptedPass,user.getPassword())) {
  		        ses.setAttribute("user", user);
  		  		model.put("welcomemessage", user.getUserName() + "様、お疲れ様です！");
  			} else {
  	      	    model.put("message", "ユーザID、パスワードの入力に誤りがあるか登録されていません。");
  	            return "login";
  			}
  		}
  		model.put("userinfo", user);
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
  	    return "index";
  	} catch (Exception e) {
      	model.put("message", e.toString());
      	return "error";
    }
  }

  @RequestMapping("/personInfo")
  String personInfo(Map<String, Object> model,HttpSession ses) {
  	try{
  		Object userObj = ses.getAttribute("user");
  		if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
  		}
  		long userId = ((UserModel)userObj).getId();
  		dao = new UserDaoImpl();
  		UserModel user = dao.findUserInfoById(userId);
	  	model.put("record", user);
  		List<CertificationModel> certificationList = dao.findCertificationInfoById(userId);
  		ContractInfoModel contractInfo = dao.findContractInfoById(userId);

  		if (contractInfo == null) {
  			contractInfo = new ContractInfoModel();
  		}
	  	model.put("certifications", certificationList);
	  	model.put("contractInfo", contractInfo);
  	    return "userInfo";
  	} catch (Exception e) {
      	model.put("message", e.toString());
      return "error";
    }
  }

  @RequestMapping("/returnIndex")
  String returnIndex(Map<String, Object> model,HttpSession ses , HttpServletResponse response) {
  	try{
  		Object userObj = ses.getAttribute("user");
  		if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
  		}
  		model.put("welcomemessage", ((UserModel)userObj).getUserName() + "様、お疲れ様です！");
  		model.put("userinfo", (UserModel)userObj);
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
  	    return "index";
  	} catch (Exception e) {
      	model.put("message", e.toString());
      return "error";
    }
  }

  @RequestMapping("/logout")
  String logout(Map<String, Object> model,HttpSession ses , HttpServletResponse response) {
  	try{
		ses.invalidate();
        model.put("message", "ログアウトしました。");
        response.setHeader("Cache-control", "no-cache");
        response.setHeader("pragma", "no-cache");
        response.setDateHeader("expires", -1);
    	return "login";
  	} catch (Exception e) {
      	model.put("message", e.toString());
      return "error";
    }
  }
}