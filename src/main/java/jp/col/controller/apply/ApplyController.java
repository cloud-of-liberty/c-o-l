package jp.col.controller.apply;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.micrometer.core.instrument.util.StringUtils;
import jp.col.Model.ApplyModel;
import jp.col.Model.ApplyTypeModel;
import jp.col.Model.UserModel;
import jp.col.dao.ApplyDaoImpl;
import jp.col.dao.ApplyTypeDaoImpl;
import jp.col.dao.IApplyDao;
import jp.col.dao.IApplyTypeDao;

@Controller
public class ApplyController{

	IApplyDao dao;
	IApplyTypeDao applyTypeDao;

	@RequestMapping("/NewApply")
	public ModelAndView newApply(RedirectAttributes attributes , 
			ApplyModel apply , Map<String, Object> model ,HttpSession ses ,
			@ModelAttribute(name = "message") String errorMessage ,
			@ModelAttribute(name = "errorflg") String errorflg ,
			@ModelAttribute(name = "inputrecord") ApplyModel errorApply ) {
		ModelAndView view = new ModelAndView();
		try{
			Object userObj = ses.getAttribute("user");
			if(userObj == null){
				attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
				view.setViewName("redirect:login");
				return view;
			}
			
			applyTypeDao = new ApplyTypeDaoImpl();
			List<ApplyTypeModel> applyTypeList = applyTypeDao.findAllApplyTypes();
			model.put("applyTypes", applyTypeList);

			if ("1".equals(errorflg)) {
				if(StringUtils.isNotEmpty(errorApply.getApplyType())){
					ApplyTypeModel content = applyTypeDao.findContentBySfid(errorApply.getApplyType());
					if (content != null) {
						if (content.getName().equals("休暇申請（有休）")) {
							UserModel user = (UserModel)userObj;
							apply.setRestVacationDays(user.getRestVacationDays());
							apply.setRestVacationHours(user.getRestVacationHours());
	
							List<String> daysList = this.getDaysList(Integer.parseInt(user.getRestVacationDays()));
							List<String> hoursList = getHoursList();
	
							model.put("daysList", daysList);
							model.put("hoursList", hoursList);
							model.put("isPaidHoliday", true);
							model.put("isHoliday", true);
						} 
						if (content.getName().equals("休暇申請")) {
							model.put("isHoliday", true);
						}
					}
				}
				
				model.put("message", errorMessage);
				model.put("record", errorApply);
				view.setViewName("employeeApply");
				return view;
			} else {
	
				apply.setApplyContent("");
				boolean isHoliday = false;
				if(StringUtils.isNotEmpty(apply.getApplyType())){
					ApplyTypeModel content = applyTypeDao.findContentBySfid(apply.getApplyType());
					if (content != null) {
						apply.setApplyContent(content.getContent());
						if (content.getName().equals("休暇申請（有休）")) {
							UserModel user = (UserModel)userObj;
							apply.setRestVacationDays(user.getRestVacationDays());
							apply.setRestVacationHours(user.getRestVacationHours());
	
							List<String> daysList = this.getDaysList(Integer.parseInt(user.getRestVacationDays()));
							List<String> hoursList = getHoursList();
	
							model.put("daysList", daysList);
							model.put("hoursList", hoursList);
							model.put("isPaidHoliday", true);
							model.put("isHoliday", true);
							isHoliday = true;
						} 
						if (content.getName().equals("休暇申請")) {
							model.put("isHoliday", true);
							isHoliday = true;
						}
					}
				}else {
					apply.setApplyContent("");
				}
				if (isHoliday) {
					Date date=new Date();
					Calendar calendar = Calendar.getInstance();
					calendar.setTime(date);
					calendar.add(Calendar.DATE,1);
					date=calendar.getTime();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
					String dateString = formatter.format(date);
					apply.setHolidayStartDate(dateString + "T09:00");
					apply.setHolidayEndDate(dateString + "T18:00");
		  		}
		  		//apply.setHolidayStartDate();
	            model.put("record", apply);
				view.setViewName("employeeApply");
				return view;
			}
	  	} catch (Exception e) {
	  		attributes.addFlashAttribute("message", e.toString());
	  		view.setViewName("redirect:systemerror");
			return view;
		}
	}

	@RequestMapping("/saveApply")
	public ModelAndView saveApply(RedirectAttributes attributes , @Validated ApplyModel apply , BindingResult bindingResult , Map<String, Object> model,HttpSession ses , HttpServletResponse response) {
		ModelAndView view = new ModelAndView();

  		Object userObj = ses.getAttribute("user");
  		if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
			attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
			view.setViewName("redirect:login");
			return view;
  		}
  		
  		//check input
  		boolean hasError = false;
  		String errorMessage = null;
  		boolean isHoliday = false;
        if(bindingResult.hasErrors()){
        	hasError = true;
        	StringBuilder sbErrorMessage = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
            	sbErrorMessage.append(fieldError.getDefaultMessage());
            	sbErrorMessage.append("\n");
            }
            model.put("record", apply);
            errorMessage = sbErrorMessage.toString();
        } else {
	  		if(StringUtils.isNotEmpty(apply.getApplyType())){
	  			ApplyTypeModel content = applyTypeDao.findContentBySfid(apply.getApplyType());
	  			if (content != null) {
		  			if (content.getName().equals("休暇申請（有休）")) {
		  	    		UserModel user = (UserModel)userObj;
		  	    		apply.setRestVacationDays(user.getRestVacationDays());
		  	    		apply.setRestVacationHours(user.getRestVacationHours());

		  	    		List<String> daysList = this.getDaysList(Integer.parseInt(user.getRestVacationDays()));
		  				List<String> hoursList = getHoursList();

		  				model.put("daysList", daysList);
		  	            model.put("hoursList", hoursList);
		  	            model.put("isPaidHoliday", true);
		  		  		errorMessage = checkPaidHolidayInput(apply);
		  	            if (StringUtils.isNotEmpty(errorMessage)) {
		  	            	hasError = true;
		  	            }
		  	            isHoliday = true;
		  			} 
		  			if (content.getName().equals("休暇申請")) {
		  	            model.put("isHoliday", true);
		  	            isHoliday = true;
		  			}
	  			}
	  			if (isHoliday && !hasError) {
	  				errorMessage = checkHolidayBeginEnd(apply);
	  	            if (StringUtils.isNotEmpty(errorMessage)) {
	  	            	hasError = true;
	  	            }
	  			}
	  		}
        }
        if (hasError) {
			attributes.addFlashAttribute("message", errorMessage);
			attributes.addFlashAttribute("inputrecord", apply);
			attributes.addFlashAttribute("errorflg", "1");
			view.setViewName("redirect:NewApply");
			return view;
        }
        try {
    		UserModel user = (UserModel)userObj;
	  		dao = new ApplyDaoImpl();
    		apply.setEmployeeId(user.getSfid());
    		apply.setApplicantMail(user.getEmail());
    		apply.setApplyStatus("申請済み");
  			dao.insertApply(apply);
  			
  			attributes.addFlashAttribute("welcomemessage", ((UserModel)userObj).getUserName() + "様、お疲れ様です！");
			view.setViewName("redirect:menu");
			return view;
    	} catch (Exception e) {
    		attributes.addFlashAttribute("message", e.toString());
    		view.setViewName("redirect:systemerror");
    		return view;
    	}
    }
	
	@RequestMapping("/ApplyHistory")
	public ModelAndView applyHistory(RedirectAttributes attributes , Map<String, Object> model,HttpSession ses) {
		ModelAndView view = new ModelAndView();
		Object userObj = ses.getAttribute("user");
		if(userObj == null){
			attributes.addFlashAttribute("message", "セッションタイムアウトが発生しました。\\r\\n再度ログインから実行してください。");
			view.setViewName("redirect:login");
			return view;
		}
		try {
			String sfid = ((UserModel)userObj).getSfid();
			dao = new ApplyDaoImpl();
			List<ApplyModel> applyModelList = dao.findApplyHistoryByEmployeeId(sfid);

			model.put("applys", applyModelList);
			view.setViewName("applyHistory");
			return view;
		} catch (Exception e) {
    		attributes.addFlashAttribute("message", e.toString());
    		view.setViewName("redirect:systemerror");
    		return view;
		}
	}
	
	@RequestMapping("/ApplyInfo")
    public String applyDetail(Map<String, Object> model,HttpSession ses , HttpServletRequest req, String id) {
  		Object userObj = ses.getAttribute("user");
  		if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
  		}
    	try {
    		if (id == null && ses.getAttribute("applyId") != null) {
    			id = ses.getAttribute("applyId").toString();
    		} else {
    			ses.setAttribute("applyId", id);
    		}
    		
	  		dao = new ApplyDaoImpl();
	  		applyTypeDao = new ApplyTypeDaoImpl();
	  		ApplyModel applyModel = dao.findApplyById(Integer.parseInt(id));
    		if (!applyModel.getEmployeeId().equals(((UserModel)userObj).getSfid())){
	      		model.put("message", "他人のコレードに権限がありません。");
	      		return "error";
    		}
		  	model.put("apply", applyModel);
		  	if (applyModel.getApplyTypeName().equals("休暇申請（有休）") || applyModel.getApplyTypeName().equals("休暇申請")) {
  	            model.put("isPaidHoliday", true);
		  	}
	  	    return "applyDetail";
    	} catch (Exception e) {
      		model.put("message", e.getMessage());
      		return "error";
    	}
    }

	private String checkPaidHolidayInput(ApplyModel apply) {
		String message = "";
		String restVacationDays = apply.getRestVacationDays();
		String restVacationHours = apply.getRestVacationHours();
		String applyVacationDays = apply.getApplyVacationDays();
		String applyVacationHours = apply.getApplyVacationHours();
		if ("0".equals(applyVacationDays) && "0".equals(applyVacationHours)) {
			message = "有給利用を入力してください。";
		} else {
			int intRestDays = Integer.parseInt(restVacationDays);
			int intRestHours = Integer.parseInt(restVacationHours);
			int intApplyDays = Integer.parseInt(applyVacationDays);
			int intApplyHours = Integer.parseInt(applyVacationHours);
			if (intApplyDays > intRestDays || (intApplyDays == intRestDays && intApplyHours > intRestHours)) {
				message = "有給利用が足りません。";
			}
		}
		return message;
	}
	
	private List<String> getDaysList(int maxDays) {
		List<String> daysList = new ArrayList<String>();
		for(int i=0;i<=maxDays;i++) {
			daysList.add(String.valueOf(i));
		}
		return daysList;
	}
	
	private List<String> getHoursList(){
		List<String> hoursList = new ArrayList<String>();
		for(int i=0;i<8;i++) {
			hoursList.add(String.valueOf(i));
		}
		return hoursList;
	}
	
	private String checkHolidayBeginEnd(ApplyModel apply) {
		String message = "";
		String start = apply.getHolidayStartDate();
		String end = apply.getHolidayEndDate();
		if (StringUtils.isEmpty(start) ) {
			message = "休暇開始日時を入力してください。";
		}
		if (StringUtils.isEmpty(end) ) {
			message = "休暇終了日時を入力してください。";
		}
		if (start.substring(0, 4).compareTo("1700") < 0 ) {
			message = "休暇開始日時に正しい日付を入力してください。";
		}
		if (end.substring(0, 4).compareTo("1700") < 0 ) {
			message = "休暇終了日時に正しい日付を入力してください。";
		}
		if (start.substring(0, 4).compareTo("4000") > 0 ) {
			message = "休暇開始日時に正しい日付を入力してください。";
		}
		if (end.substring(0, 4).compareTo("4000") > 0 ) {
			message = "休暇終了日時に正しい日付を入力してください。";
		}
 		return message;
	}
}

