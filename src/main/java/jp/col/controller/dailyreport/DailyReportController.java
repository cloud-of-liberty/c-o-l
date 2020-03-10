package jp.col.controller.dailyreport;

import jp.col.Model.UserModel;
import jp.col.Model.ProjectModel;
import jp.col.Model.DailyReportModel;
import jp.col.dao.IProjectDao;
import jp.col.dao.IDailyReportDao;
import jp.col.dao.ProjectDaoImpl;
import jp.col.dao.DailyReportDaoImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.micrometer.core.instrument.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;

@Controller
public class DailyReportController{

	IProjectDao projectDao;
	IDailyReportDao dailyReportDao;

	@RequestMapping("/newDailyReportInit")
    public String newDailyReportInit(Map<String, Object> model ,HttpSession ses ,HttpServletRequest request) {
	  	try{
	  		Object userObj = ses.getAttribute("user");
	  		if(userObj == null){
	            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
	            return "login";
	  		}

			projectDao = new ProjectDaoImpl();
			dailyReportDao = new DailyReportDaoImpl();

	  		List<ProjectModel> projectList = projectDao.findAllProjects();
			String reportDate = request.getParameter("reportDate").toString();
			if (reportDate == null) {
		      	model.put("message", "reportDate null");
			    return "error";
			}
			List<String> hourList = new ArrayList<String>();
			for(int i=0;i<24;i++) {
				hourList.add(String.format("%02d", i));
			}
			List<String> minuteList = new ArrayList<String>();
			for(int i=0;i<60;i=i+15) {
				minuteList.add(String.format("%02d", i));
			}
			model.put("hours", hourList);
			model.put("minutes", minuteList);
			
			DailyReportModel currentDailyReport = new DailyReportModel();
			currentDailyReport.setReportDate(reportDate);
    		UserModel user = (UserModel)userObj;
			currentDailyReport.setEmployee(user.getSfid());
			currentDailyReport = dailyReportDao.findDailyReportByDate(currentDailyReport);
			if(currentDailyReport != null) {
				setTimeForPage(currentDailyReport);
				setEveryWorkDisplay(model, currentDailyReport);
				model.put("report", currentDailyReport);
				
				setBreakHourMinute(currentDailyReport);
			} else {
				DailyReportModel dailyReport = new DailyReportModel();
				dailyReport.setBreakTime("1.00");
				dailyReport.setBreakHour("1");
				dailyReport.setBreakMinute("00");
				dailyReport.setReportDate(reportDate);
				dailyReport.setEmployee(user.getSfid());
				model.put("report", dailyReport);
				
				setEveryWorkDisplay(model, null);
			}

            model.put("projects", projectList);
	        return "newDailyReport";
	  	} catch (Exception e) {
	      	model.put("message", e.toString());
		    return "error";
	    }
    }
	
	@RequestMapping("/saveDailyReport")
    public String saveDailyReport(@Validated DailyReportModel dailyReport ,BindingResult bindingResult , Map<String, Object> model , HttpSession ses) {
    	try {
			Object userObj = ses.getAttribute("user");
			if(userObj == null){
				model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
				return "login";
			}
			projectDao = new ProjectDaoImpl();
	  		List<ProjectModel> projectList = projectDao.findAllProjects();
			List<String> hourList = new ArrayList<String>();
			for(int i=0;i<24;i++) {
				hourList.add(String.format("%02d", i));
			}
			List<String> minuteList = new ArrayList<String>();
			for(int i=0;i<60;i=i+15) {
				minuteList.add(String.format("%02d", i));
			}

			if(bindingResult.hasErrors()){
	        	StringBuilder errorMessage = new StringBuilder();
	            for (FieldError fieldError : bindingResult.getFieldErrors()) {
	            	errorMessage.append(fieldError.getDefaultMessage());
	            	errorMessage.append("\n");
	            }
	            model.put("report", dailyReport);
	      		model.put("message", errorMessage.toString());
	            model.put("projects", projectList);
				model.put("hours", hourList);
				model.put("minutes", minuteList);
				
				setEveryWorkDisplay(model, dailyReport);
	            return "newDailyReport";
	        }
	        
			setTimeForDB(dailyReport);
	        String relationError = checkDailyReport(dailyReport);
	        if(StringUtils.isNotEmpty(relationError)) {
	            model.put("report", dailyReport);
	      		model.put("message", relationError);
	            model.put("projects", projectList);
				model.put("hours", hourList);
				model.put("minutes", minuteList);
				setEveryWorkDisplay(model, dailyReport);

	            return "newDailyReport";
	        }

			UserModel user = (UserModel)userObj;
			dailyReportDao = new DailyReportDaoImpl();
			
			dailyReport.setEmployee(user.getSfid());
			setReportBreakTime(dailyReport);
			dailyReport.setReportStatus("saved");
			DailyReportModel currentDailyReport = dailyReportDao.findDailyReportByDate(dailyReport);
			if (currentDailyReport == null) {
	    		dailyReport.setEmployee(user.getSfid());
				dailyReportDao.insertDailyReport(dailyReport);
			} else {
				String reportStatus = currentDailyReport.getReportStatus();
				if (reportStatus.equals("Submitted") || reportStatus.equals("Confirmed")) {
		      		model.put("message", reportStatus + "の日報は修正できません。");
		      		return "error";
				} else {
					dailyReportDao.updateDailyReportByDate(dailyReport);
				}
			}
	      	return "redirect:dailyReportListinit";
    	} catch (Exception e) {
      		model.put("message", e.getMessage());
      		return "error";
    	}
	}
	
	@RequestMapping("/commitDailyReport")
    public String commitDailyReport(@Validated DailyReportModel dailyReport ,BindingResult bindingResult , Map<String, Object> model,HttpSession ses,BindingResult result) {
    	try {
			Object userObj = ses.getAttribute("user");
			if(userObj == null){
				model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
				return "login";
			}
			projectDao = new ProjectDaoImpl();
	  		List<ProjectModel> projectList = projectDao.findAllProjects();
			List<String> hourList = new ArrayList<String>();
			for(int i=0;i<24;i++) {
				hourList.add(String.format("%02d", i));
			}
			List<String> minuteList = new ArrayList<String>();
			for(int i=0;i<60;i=i+15) {
				minuteList.add(String.format("%02d", i));
			}

			if(bindingResult.hasErrors()){
	        	StringBuilder errorMessage = new StringBuilder();
	            for (FieldError fieldError : bindingResult.getFieldErrors()) {
	            	errorMessage.append(fieldError.getDefaultMessage());
	            	errorMessage.append("\n");
	            }
	            model.put("report", dailyReport);
	      		model.put("message", errorMessage.toString());
	            model.put("projects", projectList);
				model.put("hours", hourList);
				model.put("minutes", minuteList);
				
				setEveryWorkDisplay(model, dailyReport);
	            return "newDailyReport";
	        }
	        
			setTimeForDB(dailyReport);
	        String relationError = checkDailyReport(dailyReport);
	        if(StringUtils.isNotEmpty(relationError)) {
	            model.put("report", dailyReport);
	      		model.put("message", relationError);
	            model.put("projects", projectList);
				model.put("hours", hourList);
				model.put("minutes", minuteList);
	            return "newDailyReport";
	        }

	        UserModel user = (UserModel)userObj;
			dailyReportDao = new DailyReportDaoImpl();
			
			dailyReport.setEmployee(user.getSfid());
			setReportBreakTime(dailyReport);

			DailyReportModel currentDailyReport = dailyReportDao.findDailyReportByDate(dailyReport);
			if (currentDailyReport == null){
	    		dailyReport.setEmployee(user.getSfid());
				dailyReport.setReportStatus("Submitted");
				
				dailyReportDao.insertDailyReport(dailyReport);
			} else {
				String reportStatus = currentDailyReport.getReportStatus();
				if (reportStatus.equals("Submitted")){
		      		model.put("message", "該当日付の日報は既に提出済みです。");
		      		return "error";
				} 
				if (reportStatus.equals("Confirmed")) {
		      		model.put("message", "該当日付の日報は既に確認済みです。");
		      		return "error";
				} else {
					dailyReport.setReportStatus("Submitted");
					dailyReportDao.updateDailyReportByDate(dailyReport);
				}
			}
	      	return "redirect:dailyReportListinit";
    	} catch (Exception e) {
      		model.put("message", e.getMessage());
      		return "error";
    	}
	}

	private String checkDailyReport(DailyReportModel dailyReport) {
		StringBuilder errorMessage = new StringBuilder();
		if(StringUtils.isNotEmpty(dailyReport.getTask2()) || StringUtils.isNotEmpty(dailyReport.getProjectId2()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent2()) || StringUtils.isNotEmpty(dailyReport.getWorkTime2())) {
			if(StringUtils.isEmpty(dailyReport.getTask2())) {
				errorMessage.append("タスク2を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId2())) {
				errorMessage.append("プロジェクト2を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime2())) {
				errorMessage.append("仕事時間2を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent2())) {
				errorMessage.append("仕事内容2を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask3()) || StringUtils.isNotEmpty(dailyReport.getProjectId3()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent3()) || StringUtils.isNotEmpty(dailyReport.getWorkTime3())) {
			if(StringUtils.isEmpty(dailyReport.getTask3())) {
				errorMessage.append("タスク3を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId3())) {
				errorMessage.append("プロジェクト3を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime3())) {
				errorMessage.append("仕事時間3を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent3())) {
				errorMessage.append("仕事内容3を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask4()) || StringUtils.isNotEmpty(dailyReport.getProjectId4()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent4()) || StringUtils.isNotEmpty(dailyReport.getWorkTime4())) {
			if(StringUtils.isEmpty(dailyReport.getTask4())) {
				errorMessage.append("タスク4を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId4())) {
				errorMessage.append("プロジェクト4を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime4())) {
				errorMessage.append("仕事時間4を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent4())) {
				errorMessage.append("仕事内容4を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask5()) || StringUtils.isNotEmpty(dailyReport.getProjectId5()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent5()) || StringUtils.isNotEmpty(dailyReport.getWorkTime5())) {
			if(StringUtils.isEmpty(dailyReport.getTask5())) {
				errorMessage.append("タスク5を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId5())) {
				errorMessage.append("プロジェクト5を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime5())) {
				errorMessage.append("仕事時間5を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent5())) {
				errorMessage.append("仕事内容5を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask6()) || StringUtils.isNotEmpty(dailyReport.getProjectId6()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent6()) || StringUtils.isNotEmpty(dailyReport.getWorkTime6())) {
			if(StringUtils.isEmpty(dailyReport.getTask6())) {
				errorMessage.append("タスク6を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId6())) {
				errorMessage.append("プロジェクト6を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime6())) {
				errorMessage.append("仕事時間6を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent6())) {
				errorMessage.append("仕事内容6を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask7()) || StringUtils.isNotEmpty(dailyReport.getProjectId7()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent7()) || StringUtils.isNotEmpty(dailyReport.getWorkTime7())) {
			if(StringUtils.isEmpty(dailyReport.getTask7())) {
				errorMessage.append("タスク7を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId7())) {
				errorMessage.append("プロジェクト7を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime7())) {
				errorMessage.append("仕事時間7を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent7())) {
				errorMessage.append("仕事内容7を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask8()) || StringUtils.isNotEmpty(dailyReport.getProjectId8()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent8()) || StringUtils.isNotEmpty(dailyReport.getWorkTime8())) {
			if(StringUtils.isEmpty(dailyReport.getTask8())) {
				errorMessage.append("タスク8を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId8())) {
				errorMessage.append("プロジェクト8を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime8())) {
				errorMessage.append("仕事時間8を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent8())) {
				errorMessage.append("仕事内容8を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask9()) || StringUtils.isNotEmpty(dailyReport.getProjectId9()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent9()) || StringUtils.isNotEmpty(dailyReport.getWorkTime9())) {
			if(StringUtils.isEmpty(dailyReport.getTask9())) {
				errorMessage.append("タスク9を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId9())) {
				errorMessage.append("プロジェクト9を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime9())) {
				errorMessage.append("仕事時間9を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent9())) {
				errorMessage.append("仕事内容9を入力してください。\n");
			}
		}
		if(StringUtils.isNotEmpty(dailyReport.getTask10()) || StringUtils.isNotEmpty(dailyReport.getProjectId10()) ||
				StringUtils.isNotEmpty(dailyReport.getWorkContent10()) || StringUtils.isNotEmpty(dailyReport.getWorkTime10())) {
			if(StringUtils.isEmpty(dailyReport.getTask10())) {
				errorMessage.append("タスク２を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getProjectId10())) {
				errorMessage.append("プロジェクト２を選択してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkTime10())) {
				errorMessage.append("仕事時間２を入力してください。\n");
			}
			if(StringUtils.isEmpty(dailyReport.getWorkContent10())) {
				errorMessage.append("仕事内容２を入力してください。\n");
			}
		}
    	return errorMessage.toString();
    }
	
	private void setTimeForPage(DailyReportModel dailyReport) {
		String beginTime = dailyReport.getBeginTime();
		String endTime = dailyReport.getEndTime();
		
		if(StringUtils.isNotEmpty(beginTime)) {
			String beginHour = beginTime.substring(0, 2);
			dailyReport.setBeginHour(beginHour);
			String beginMinute = beginTime.substring(3, 5);
			dailyReport.setBeginMinute(beginMinute);
		}
		if(StringUtils.isNotEmpty(endTime)) {
			String endHour = endTime.substring(0, 2);
			dailyReport.setEndHour(endHour);
			String endMinute = endTime.substring(3, 5);
			dailyReport.setEndMinute(endMinute);
		}

		String workTime1 = dailyReport.getWorkTime1();
		
		if(StringUtils.isNotEmpty(workTime1)) {
			String[] strArray=workTime1.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime1Hours(strArray[0]);
				dailyReport.setWorkTime1Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime2 = dailyReport.getWorkTime2();
		
		if(StringUtils.isNotEmpty(workTime2)) {
			String[] strArray=workTime2.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime2Hours(strArray[0]);
				dailyReport.setWorkTime2Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime3 = dailyReport.getWorkTime3();
		
		if(StringUtils.isNotEmpty(workTime3)) {
			String[] strArray=workTime3.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime3Hours(strArray[0]);
				dailyReport.setWorkTime3Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime4 = dailyReport.getWorkTime4();
		
		if(StringUtils.isNotEmpty(workTime4)) {
			String[] strArray=workTime4.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime4Hours(strArray[0]);
				dailyReport.setWorkTime4Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime5 = dailyReport.getWorkTime5();
		
		if(StringUtils.isNotEmpty(workTime5)) {
			String[] strArray=workTime5.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime5Hours(strArray[0]);
				dailyReport.setWorkTime5Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime6 = dailyReport.getWorkTime6();
		
		if(StringUtils.isNotEmpty(workTime6)) {
			String[] strArray=workTime6.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime6Hours(strArray[0]);
				dailyReport.setWorkTime6Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime7 = dailyReport.getWorkTime7();
		
		if(StringUtils.isNotEmpty(workTime7)) {
			String[] strArray=workTime7.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime7Hours(strArray[0]);
				dailyReport.setWorkTime7Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime8 = dailyReport.getWorkTime8();
		
		if(StringUtils.isNotEmpty(workTime8)) {
			String[] strArray=workTime8.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime8Hours(strArray[0]);
				dailyReport.setWorkTime8Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime9 = dailyReport.getWorkTime9();
		
		if(StringUtils.isNotEmpty(workTime9)) {
			String[] strArray=workTime9.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime9Hours(strArray[0]);
				dailyReport.setWorkTime9Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
		String workTime10 = dailyReport.getWorkTime10();
		
		if(StringUtils.isNotEmpty(workTime10)) {
			String[] strArray=workTime10.trim().split("\\.");
			if(strArray.length>=2){
				dailyReport.setWorkTime10Hours(strArray[0]);
				dailyReport.setWorkTime10Minutes(String.format("%02d", Integer.parseInt(strArray[1])*60/100));
	        }
		}
	}

	private void setTimeForDB(DailyReportModel dailyReport) {
		String beginHour = dailyReport.getBeginHour();
		String beginMinute = dailyReport.getBeginMinute();
		
		if(StringUtils.isNotEmpty(beginHour) && StringUtils.isNotEmpty(beginMinute) ) {
			dailyReport.setBeginTime(beginHour + ":" + beginMinute + ":00");
		}
		
		String endHour = dailyReport.getEndHour();
		String endMinute = dailyReport.getEndMinute();
		
		if(StringUtils.isNotEmpty(endHour) && StringUtils.isNotEmpty(endMinute) ) {
			dailyReport.setEndTime(endHour + ":" + endMinute + ":00");
		}

		String workTime1Hours = dailyReport.getWorkTime1Hours();
		String workTime1Minutes = dailyReport.getWorkTime1Minutes();
		if(StringUtils.isNotEmpty(workTime1Hours)) {
			dailyReport.setWorkTime1(workTime1Hours + getHoursByMinutes(workTime1Minutes));
		}

		String workTime2Hours = dailyReport.getWorkTime2Hours();
		String workTime2Minutes = dailyReport.getWorkTime2Minutes();
		if(StringUtils.isNotEmpty(workTime2Hours)) {
			dailyReport.setWorkTime2(workTime2Hours + getHoursByMinutes(workTime2Minutes));
		}

		String workTime3Hours = dailyReport.getWorkTime3Hours();
		String workTime3Minutes = dailyReport.getWorkTime3Minutes();
		if(StringUtils.isNotEmpty(workTime3Hours)) {
			dailyReport.setWorkTime3(workTime3Hours + getHoursByMinutes(workTime3Minutes));
		}

		String workTime4Hours = dailyReport.getWorkTime4Hours();
		String workTime4Minutes = dailyReport.getWorkTime4Minutes();
		if(StringUtils.isNotEmpty(workTime4Hours)) {
			dailyReport.setWorkTime4(workTime4Hours + getHoursByMinutes(workTime4Minutes));
		}

		String workTime5Hours = dailyReport.getWorkTime5Hours();
		String workTime5Minutes = dailyReport.getWorkTime5Minutes();
		if(StringUtils.isNotEmpty(workTime5Hours)) {
			dailyReport.setWorkTime5(workTime5Hours + getHoursByMinutes(workTime5Minutes));
		}

		String workTime6Hours = dailyReport.getWorkTime6Hours();
		String workTime6Minutes = dailyReport.getWorkTime6Minutes();
		if(StringUtils.isNotEmpty(workTime6Hours)) {
			dailyReport.setWorkTime6(workTime6Hours + getHoursByMinutes(workTime6Minutes));
		}

		String workTime7Hours = dailyReport.getWorkTime7Hours();
		String workTime7Minutes = dailyReport.getWorkTime7Minutes();
		if(StringUtils.isNotEmpty(workTime7Hours)) {
			dailyReport.setWorkTime7(workTime7Hours + getHoursByMinutes(workTime7Minutes));
		}

		String workTime8Hours = dailyReport.getWorkTime8Hours();
		String workTime8Minutes = dailyReport.getWorkTime8Minutes();
		if(StringUtils.isNotEmpty(workTime8Hours)) {
			dailyReport.setWorkTime8(workTime8Hours + getHoursByMinutes(workTime8Minutes));
		}

		String workTime9Hours = dailyReport.getWorkTime9Hours();
		String workTime9Minutes = dailyReport.getWorkTime9Minutes();
		if(StringUtils.isNotEmpty(workTime9Hours)) {
			dailyReport.setWorkTime9(workTime9Hours + getHoursByMinutes(workTime9Minutes));
		}

		String workTime10Hours = dailyReport.getWorkTime10Hours();
		String workTime10Minutes = dailyReport.getWorkTime10Minutes();
		if(StringUtils.isNotEmpty(workTime10Hours)) {
			dailyReport.setWorkTime10(workTime10Hours + getHoursByMinutes(workTime10Minutes));
		}
	}
	
	private String getHoursByMinutes(String minutes) {
		String minuteToHour = "";
		if(StringUtils.isEmpty(minutes)) {
			minuteToHour= ".0";
		}else if(minutes.equals("00")) {
			minuteToHour= ".0";
		}else if(minutes.equals("15")) {
			minuteToHour= ".25";
		}else if(minutes.equals("30")) {
			minuteToHour= ".5";
		}else if(minutes.equals("45")) {
			minuteToHour= ".75";
		}
		return minuteToHour;
	}
	
	private void setBreakHourMinute(DailyReportModel dailyReport){
		String breakTime = dailyReport.getBreakTime();
		if (StringUtils.isNotEmpty(breakTime)) {
			double dblBreakTime = Double.parseDouble(breakTime);
			int intBreakTime = (int)dblBreakTime;
			dailyReport.setBreakHour(String.valueOf(intBreakTime));
			double dblBreakMinute = dblBreakTime - intBreakTime;
			if (dblBreakMinute == 0) {
				dailyReport.setBreakMinute("00");
			}
			if (dblBreakMinute == 0.25) {
				dailyReport.setBreakMinute("15");
			}
			if (dblBreakMinute == 0.5) {
				dailyReport.setBreakMinute("30");
			}
			if (dblBreakMinute == 0.75) {
				dailyReport.setBreakMinute("45");
			}
		}
	}
	
	private void setReportBreakTime(DailyReportModel dailyReport){
		String breakHour = dailyReport.getBreakHour();
		String breakMinute = dailyReport.getBreakMinute();
		if (StringUtils.isNotEmpty(breakHour) &&StringUtils.isNotEmpty(breakMinute) ) {
			double dblBreakTime1 = Double.parseDouble(breakHour);
			double dblBreakTime2 = 0;
			if (breakMinute.equals("00")) {
				dblBreakTime2 = 0.0;
			}
			if (breakMinute.equals("15")) {
				dblBreakTime2 = 0.25;
			}
			if (breakMinute.equals("30")) {
				dblBreakTime2 = 0.5;
			}
			if (breakMinute.equals("45")) {
				dblBreakTime2 = 0.75;
			}
			double dblBreakTime = dblBreakTime1 + dblBreakTime2;
			dailyReport.setBreakTime(String.valueOf(dblBreakTime));
		}
	}
	
	private void setEveryWorkDisplay(Map<String, Object> model , DailyReportModel dailyReport) {
		if (dailyReport == null) {
			model.put("work1", "false");
			model.put("work2", "false");
			model.put("work3", "false");
			model.put("work4", "false");
			model.put("work5", "false");
			model.put("work6", "false");
			model.put("work7", "false");
			model.put("work8", "false");
			model.put("work9", "false");
			model.put("work10", "false");
		} else {
			if(StringUtils.isNotEmpty(dailyReport.getTask1()) || StringUtils.isNotEmpty(dailyReport.getProjectId1()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent1()) || StringUtils.isNotEmpty(dailyReport.getWorkTime1())) {
				model.put("work1", "true");
			} else {
				model.put("work1", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask2()) || StringUtils.isNotEmpty(dailyReport.getProjectId2()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent2()) || StringUtils.isNotEmpty(dailyReport.getWorkTime2())) {
				model.put("work2", "true");
			} else {
				model.put("work2", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask3()) || StringUtils.isNotEmpty(dailyReport.getProjectId3()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent3()) || StringUtils.isNotEmpty(dailyReport.getWorkTime3())) {
				model.put("work3", "true");
			} else {
				model.put("work3", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask4()) || StringUtils.isNotEmpty(dailyReport.getProjectId4()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent4()) || StringUtils.isNotEmpty(dailyReport.getWorkTime4())) {
				model.put("work4", "true");
			} else {
				model.put("work4", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask5()) || StringUtils.isNotEmpty(dailyReport.getProjectId5()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent5()) || StringUtils.isNotEmpty(dailyReport.getWorkTime5())) {
				model.put("work5", "true");
			} else {
				model.put("work5", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask6()) || StringUtils.isNotEmpty(dailyReport.getProjectId6()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent6()) || StringUtils.isNotEmpty(dailyReport.getWorkTime6())) {
				model.put("work6", "true");
			} else {
				model.put("work6", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask7()) || StringUtils.isNotEmpty(dailyReport.getProjectId7()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent7()) || StringUtils.isNotEmpty(dailyReport.getWorkTime7())) {
				model.put("work7", "true");
			} else {
				model.put("work7", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask8()) || StringUtils.isNotEmpty(dailyReport.getProjectId8()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent8()) || StringUtils.isNotEmpty(dailyReport.getWorkTime8())) {
				model.put("work8", "true");
			} else {
				model.put("work8", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask9()) || StringUtils.isNotEmpty(dailyReport.getProjectId9()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent9()) || StringUtils.isNotEmpty(dailyReport.getWorkTime9())) {
				model.put("work9", "true");
			} else {
				model.put("work9", "false");
			}
			if(StringUtils.isNotEmpty(dailyReport.getTask10()) || StringUtils.isNotEmpty(dailyReport.getProjectId10()) ||
					StringUtils.isNotEmpty(dailyReport.getWorkContent10()) || StringUtils.isNotEmpty(dailyReport.getWorkTime10())) {
				model.put("work10", "true");
			} else {
				model.put("work10", "false");
			}
		}
	}
}

