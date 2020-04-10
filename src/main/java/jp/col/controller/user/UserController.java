package jp.col.controller.user;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

	@RequestMapping("/systemlogin")
	ModelAndView systemlogin(RedirectAttributes attributes , UserModel user) {
		ModelAndView view = new ModelAndView();
		attributes.addFlashAttribute("user", user);
		attributes.addFlashAttribute("loginflg", "1");
		view.setViewName("redirect:menu");
		return view;
	}
	
	@RequestMapping("/menu")
	ModelAndView menu(@ModelAttribute(name = "user") UserModel user , 
				@ModelAttribute(name = "loginflg") String loginflg ,
				RedirectAttributes attributes , 
				Map<String, Object> model,HttpSession ses , HttpServletResponse response) {
		ModelAndView view = new ModelAndView();
		try{
			if ("1".equals(loginflg)) {
				//ログイン画面からの場合
				String encryptedPass = user.getPassword();
				String decryptedPass = AesUtil.aesDecrypt(encryptedPass);
				dao = new UserDaoImpl();
				user = dao.findByEmail(user.getEmail());
				if (user == null) {
					attributes.addFlashAttribute("message", "該当ユーザが存在しません。");
					view.setViewName("redirect:login");
					return view;
				} else {
					if (BCrypt.checkpw(decryptedPass,user.getPassword())) {
				    	ses.setAttribute("user", user);
				  		model.put("welcomemessage", user.getUserName() + "様、お疲れ様です！");
					} else {
						attributes.addFlashAttribute("message", "ユーザID、パスワードの入力に誤りがあるか登録されていません。");
						view.setViewName("redirect:login");
						return view;
					}
				}
				model.put("userinfo", user);
			} else {
				/* 戻る場合 */
				Object userObj = ses.getAttribute("user");
				if(userObj == null){
					attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。再度ログインから実行してください。");
					view.setViewName("redirect:login");
					return view;
		  		} else {
		  			model.put("welcomemessage", ((UserModel)userObj).getUserName() + "様、お疲れ様です！");
		  		}
			}
			view.setViewName("index");
			return view;
		} catch (Exception e) {
			attributes.addFlashAttribute("message", e.toString());
			view.setViewName("redirect:systemerror");
			return view;
		}
	}

	@RequestMapping("/UserInfo")
	ModelAndView UserInfo(RedirectAttributes attributes , Map<String, Object> model,HttpSession ses) {
		ModelAndView view = new ModelAndView();
		try{
			Object userObj = ses.getAttribute("user");
			if(userObj == null){
				attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
				view.setViewName("redirect:login");
				return view;
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
			view.setViewName("userInfo");
			return view;
		} catch (Exception e) {
			attributes.addFlashAttribute("message", e.toString());
			view.setViewName("redirect:systemerror");
			return view;
		}
	}

	@RequestMapping("/logout")
	ModelAndView logout(RedirectAttributes attributes , HttpSession ses , HttpServletResponse response) {

		ModelAndView view = new ModelAndView();

		try{
			ses.invalidate();
			attributes.addFlashAttribute("message", "ログアウトしました。");
			view.setViewName("redirect:login");
			return view;
		} catch (Exception e) {
			attributes.addFlashAttribute("message", e.toString());
			view.setViewName("redirect:systemerror");
			return view;
		}
	}
}//abc