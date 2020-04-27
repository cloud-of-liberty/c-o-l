package jp.col.controller.dailyreport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import io.micrometer.core.instrument.util.StringUtils;
import jp.col.Model.DailyReportModel;
import jp.col.Model.UserModel;
import jp.col.dao.DailyReportDaoImpl;
import jp.col.dao.IDailyReportDao;

class Task {
	private String name;
	private String projectName;
	private String taskContent;
	private String spentTime;
	public Task(String name, String projectName, String taskContent, String spentTime) {
		this.name = name;
		this.projectName = projectName;
		this.taskContent = taskContent;
		this.spentTime = spentTime;
	}
	public Task() {
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getTaskContent() {
		return taskContent;
	}
	public void setTaskContent(String taskContent) {
		this.taskContent = taskContent;
	}
	public String getSpentTime() {
		return spentTime;
	}
	public void setSpentTime(String spentTime) {
		this.spentTime = spentTime;
	}
}

class Report {
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private String label;
	private String dateStr;
	private List<Task> taskList;
	private String workKind;
	private String yobi;
	private String beginTime;
	private String endTime;
	private String breakTime;
	private String workTime;
	private String offTime;
	private String reportStatus;
	public Report() {
		this.label = "";
		this.dateStr = "";
		this.taskList = new ArrayList<Task>();
	}
	public Report(Calendar c) {
		this.label = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		this.dateStr = sdf.format(c.getTime());
		this.taskList = new ArrayList<Task>();
	}
	public String getLabel() {
		return label == null ? "" : label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public String getDateStr() {
		return dateStr == null ? "" : dateStr;
	}
	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}
	public List<Task> getTaskList() {
		return taskList;
	}
	public void setTaskList(List<Task> taskList) {
		this.taskList = taskList;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}
	public String getYobi() {
		return yobi;
	}
	public void setYobi(String yobi) {
		this.yobi = yobi;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	public String getWorkTime() {
		return workTime;
	}
	public void setWorkTime(String workTime) {
		this.workTime = workTime;
	}
	public String getOffTime() {
		return offTime;
	}
	public void setOffTime(String offTime) {
		this.offTime = offTime;
	}

}

@Controller
public class DailyReportListController {
	
	private static final String DATEFORMAT_JA = "yyyy年M月";

	private static Map<String, String> reportStatusMap = new HashMap<String, String>();
	private static Map<String, String> workKindMap = new HashMap<String, String>();
	
	static {
		reportStatusMap.put("saved","保存済");
		reportStatusMap.put("Submitted","提出済");
		reportStatusMap.put("Confirmed","確認済");
		workKindMap.put("Predetermined", "所定");
		workKindMap.put("HolidayWorkSat", "休出（土・祝）");
		workKindMap.put("HolidayWorkSun", "休出（日）");
		workKindMap.put("Holiday", "有休");
		workKindMap.put("HalfADayOff", "半休");
		workKindMap.put("SpecialHoliday", "特休");
		workKindMap.put("Absence", "欠勤");	
	}
	IDailyReportDao dailyReportDao;
	
	@CacheEvict
    @RequestMapping("/DailyList")
    String init(Map<String, Object> model,HttpSession ses, HttpServletRequest req , String currentDate , String mode) {
    	Object userObj = ses.getAttribute("user");
	    if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
	    }
	    
	    UserModel user = (UserModel)userObj;
	    Calendar now = Calendar.getInstance();
	    if (!StringUtils.isBlank(currentDate)) {
	    	SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_JA);
	    	try {
				now.setTime(sdf.parse(currentDate));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	now.add(Calendar.MONTH, "p".equals(mode) ? -1 : 1);
	    }
	    
        return initReportList(model, user, now);
    }
    
    @RequestMapping("/dailyReportListPre")
    String dailyReportListPre(Map<String, Object> model, HttpSession ses, HttpServletRequest req) {
    	
    	Object userObj = ses.getAttribute("user");
	    if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
	    }
	    UserModel user = (UserModel)userObj;
	    String date = req.getParameter("currentDate");
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_JA);
	    try {
	    	calendar.setTime(sdf.parse(date));
	    	calendar.add(Calendar.MONTH, -1);
	    	return initReportList(model, user, calendar);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return "dailyReportList";
    }
    
    @RequestMapping("/dailyReportListLater")
    String dailyReportListLater(Map<String, Object> model,HttpSession ses, HttpServletRequest req) {
    	
    	Object userObj = ses.getAttribute("user");
	    if(userObj == null){
            model.put("message", "セッションタイムアウトが発生しました。\r\n再度ログインから実行してください。");
            return "login";
	    }
	    UserModel user = (UserModel)userObj;
	    String date = req.getParameter("currentDate");
	    Calendar calendar = Calendar.getInstance();
	    SimpleDateFormat sdf = new SimpleDateFormat(DATEFORMAT_JA);
	    try {
	    	calendar.setTime(sdf.parse(date));
	    	calendar.add(Calendar.MONTH, 1);
	    	return initReportList(model, user, calendar);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    return "dailyReportList";
    }
/*    
    private String initReportList(Map<String, Object> model, UserModel user, Calendar now) {
    	Calendar firstDay = (Calendar) now.clone();
	    firstDay.set(Calendar.DAY_OF_MONTH, 1);
	    while (firstDay.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	    	firstDay.add(Calendar.DATE, -1);
	    }
	    
	    Calendar lastDay = (Calendar) now.clone();
	    lastDay.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
	    while (lastDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
	    	lastDay.add(Calendar.DATE, 1);
	    }

	    dailyReportDao = new DailyReportDaoImpl();
	    DailyReportModel dailyReport = new DailyReportModel();
	    dailyReport.setEmployee(user.getSfid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
	    String reportDate = sdf.format(now.getTime());
	    dailyReport.setReportDate(reportDate);
	    List<DailyReportModel> dailyReportList = dailyReportDao.findDailyReportByMonth(dailyReport);
	    
	    List<List<Report>> reportList = new ArrayList<List<Report>>();
	    List<Report> rowReports = new ArrayList<Report>();
	    List<Report> detailReportList = new ArrayList<Report>();
	    Report tempReport;
	    do {
            if (firstDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            	rowReports = new ArrayList<Report>();
	    	}
            if (firstDay.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            	tempReport = new Report(firstDay);
            	DailyReportModel dr = getDailyReport(dailyReportList , tempReport.getDateStr());
            	if (dr != null) {
            		if(StringUtils.isNotEmpty(dr.getTask1())) {
            			tempReport.getTaskList().add(new Task(dr.getTask1(),dr.getProjectName1(),dr.getWorkContent1(),dr.getWorkTime1() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask2())) {
            			tempReport.getTaskList().add(new Task(dr.getTask2(),dr.getProjectName2(),dr.getWorkContent2(),dr.getWorkTime2() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask3())) {
            			tempReport.getTaskList().add(new Task(dr.getTask3(),dr.getProjectName3(),dr.getWorkContent3(),dr.getWorkTime3() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask4())) {
            			tempReport.getTaskList().add(new Task(dr.getTask4(),dr.getProjectName4(),dr.getWorkContent4(),dr.getWorkTime4() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask5())) {
            			tempReport.getTaskList().add(new Task(dr.getTask5(),dr.getProjectName5(),dr.getWorkContent5(),dr.getWorkTime5() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask6())) {
            			tempReport.getTaskList().add(new Task(dr.getTask6(),dr.getProjectName6(),dr.getWorkContent6(),dr.getWorkTime6() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask7())) {
            			tempReport.getTaskList().add(new Task(dr.getTask7(),dr.getProjectName7(),dr.getWorkContent7(),dr.getWorkTime7() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask8())) {
            			tempReport.getTaskList().add(new Task(dr.getTask8(),dr.getProjectName8(),dr.getWorkContent8(),dr.getWorkTime8() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask9())) {
            			tempReport.getTaskList().add(new Task(dr.getTask9(),dr.getProjectName9(),dr.getWorkContent9(),dr.getWorkTime9() + "h"));
            		}
            		if(StringUtils.isNotEmpty(dr.getTask10())) {
            			tempReport.getTaskList().add(new Task(dr.getTask10(),dr.getProjectName10(),dr.getWorkContent10(),dr.getWorkTime10() + "h"));
            		}
            	}
            	rowReports.add(tempReport);
            	detailReportList.add(tempReport);
            } else {
            	tempReport = new Report();
            	rowReports.add(tempReport);
            }
            
            if (firstDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            	reportList.add(rowReports);
            }
            firstDay.add(Calendar.DATE, 1);
	    } while (!firstDay.after(lastDay));
	    
        model.put("reportList", reportList);
        model.put("detailReportList", detailReportList);
        
        SimpleDateFormat sdfJa = new SimpleDateFormat(DATEFORMAT_JA);
	    String currentDate = sdfJa.format(now.getTime());
        model.put("currentDate", currentDate);
        
        return "dailyReportList";
    }*/

    private DailyReportModel getDailyReport(List<DailyReportModel> dailyReportList , String dateStr) {
    	for (DailyReportModel model : dailyReportList) {
    		if(model.getReportDate().equals(dateStr)) {
    			return model;
    		}
    	}
    	return null;
    }
    
    private String initReportList(Map<String, Object> model, UserModel user, Calendar now) {
    	
    	SimpleDateFormat sdfE = new SimpleDateFormat("E", new Locale("ja"));

    	Calendar firstDay = (Calendar) now.clone();
	    firstDay.set(Calendar.DAY_OF_MONTH, 1);
	    while (firstDay.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
	    	firstDay.add(Calendar.DATE, -1);
	    }
	    
	    Calendar lastDay = (Calendar) now.clone();
	    lastDay.set(Calendar.DAY_OF_MONTH, now.getActualMaximum(Calendar.DAY_OF_MONTH));
	    while (lastDay.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
	    	lastDay.add(Calendar.DATE, 1);
	    }

	    dailyReportDao = new DailyReportDaoImpl();
	    DailyReportModel dailyReport = new DailyReportModel();
	    dailyReport.setEmployee(user.getSfid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM");
	    String reportDate = sdf.format(now.getTime());
	    dailyReport.setReportDate(reportDate);
	    List<DailyReportModel> dailyReportList = dailyReportDao.findDailyReportByMonth(dailyReport);
	    
	    List<List<Report>> reportList = new ArrayList<List<Report>>();
	    List<Report> rowReports = new ArrayList<Report>();
	    List<Report> detailReportList = new ArrayList<Report>();
	    Report tempReport;
	    String totalTime = "0:00";
	    do {
            if (firstDay.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            	rowReports = new ArrayList<Report>();
	    	}
            if (firstDay.get(Calendar.MONTH) == now.get(Calendar.MONTH)) {
            	tempReport = new Report(firstDay);
            	     
            	DailyReportModel drm = getDailyReport(dailyReportList , tempReport.getDateStr());
            	tempReport.setYobi(sdfE.format(firstDay.getTime()));
            	if (drm != null) {
            		
            		if(StringUtils.isNotEmpty(drm.getWorkKind())) {
            			tempReport.setWorkKind(workKindMap.get(drm.getWorkKind()));
            		}
            		if(StringUtils.isNotEmpty(drm.getBeginTime())) {
            			tempReport.setBeginTime(drm.getBeginTime().substring(0,5));
            		}
            		if(StringUtils.isNotEmpty(drm.getEndTime())) {
            			tempReport.setEndTime(drm.getEndTime().substring(0,5));
            		}
            		if(StringUtils.isNotEmpty(drm.getBreakTime())) {
            			tempReport.setBreakTime(getBreakTimeHHmm(drm.getBreakTime()));
            		}
            		tempReport.setWorkTime(getWorkTimeHHmm(drm.getBeginTime(),drm.getEndTime(),tempReport.getBreakTime()));
            		
            		totalTime = addTime(totalTime, tempReport.getWorkTime());
            		
            		tempReport.setOffTime(getOffTimeHHmm(tempReport.getWorkTime(), tempReport.getWorkKind()));
            		
            		if(StringUtils.isNotEmpty(drm.getReportStatus())) {
            			tempReport.setReportStatus(reportStatusMap.get(drm.getReportStatus()));
            		}
            		tempReport.getTaskList().add(new Task());
            	}
            	rowReports.add(tempReport);
            	detailReportList.add(tempReport);
            } else {
            	tempReport = new Report();
            	rowReports.add(tempReport);
            }

            if (firstDay.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            	reportList.add(rowReports);
            }
            firstDay.add(Calendar.DATE, 1);
	    } while (!firstDay.after(lastDay));
	    
        model.put("reportList", reportList);
        model.put("detailReportList", detailReportList);
        model.put("totalTime", "出勤時間: " + totalTime);
        
        SimpleDateFormat sdfJa = new SimpleDateFormat(DATEFORMAT_JA);
	    String currentDate = sdfJa.format(now.getTime());
        model.put("currentDate", currentDate);
        
        return "dailyReportList";
    }
    
	private String getBreakTimeHHmm(String breakTime){
		String breakTimeHHmm = "";
		if (StringUtils.isNotEmpty(breakTime)) {
			double dblBreakTime = Double.parseDouble(breakTime);
			int intBreakTime = (int)dblBreakTime;
			breakTimeHHmm = "" + intBreakTime;
			//dailyReport.setBreakHour(String.valueOf(intBreakTime));
			double dblBreakMinute = dblBreakTime - intBreakTime;
			if (dblBreakMinute == 0) {
				breakTimeHHmm += ":00";
			}
			if (dblBreakMinute == 0.25) {
				breakTimeHHmm += ":15";
			}
			if (dblBreakMinute == 0.5) {
				breakTimeHHmm += ":30";
			}
			if (dblBreakMinute == 0.75) {
				breakTimeHHmm += ":45";
			}
		}
		return breakTimeHHmm;
	}
	
	private String getWorkTimeHHmm(String beginTime, String endTime, String breakTime){
		
		String workTime = "";
		
		if (StringUtils.isNotEmpty(beginTime) && StringUtils.isNotEmpty(endTime) && StringUtils.isNotEmpty(breakTime)) {

			int intBeginTimeHour = Integer.parseInt(beginTime.split(":")[0]);
			int intEndTimeHour = Integer.parseInt(endTime.split(":")[0]);
			int intBreakTimeHour = Integer.parseInt(breakTime.split(":")[0]);
			
			int intBeginTimeMin = Integer.parseInt(beginTime.split(":")[1]);
			int intEndTimeMin = Integer.parseInt(endTime.split(":")[1]);
			int intBreakTimeMin = Integer.parseInt(breakTime.split(":")[1]);
			
			int workTimeHour = intEndTimeHour - intBeginTimeHour - intBreakTimeHour;
			int workTimeMin = intEndTimeMin - intBeginTimeMin - intBreakTimeMin;
			
			if(workTimeMin < 0) {
				workTimeHour = workTimeHour - 1;
				workTimeMin = 60 + workTimeMin;
			}
			workTime = "" + workTimeHour + ":" + workTimeMin;
			if(workTimeMin == 0) {
				workTime += "0";
			}
		}
		return workTime;
	}
	
	private String getOffTimeHHmm(String workTime, String workKind){

		String offTime = "";
		if (StringUtils.isNotEmpty(workTime) && StringUtils.isNotEmpty(workKind) ) {
	
			switch (workKind) {
			case "有休" :
			case "特休" :
				offTime = "0:00";
				break;
			default :
				int workTimeHour = Integer.parseInt(workTime.split(":")[0] );
				if (workTimeHour >= 8) {
					offTime = "" + (workTimeHour - 8) + ":";
					offTime += workTime.split(":")[1];
				} else {
					offTime = "0:00" ;
				}
			}
		}
		return offTime;
	}
	private static String addTime(String totalTime, String workTime) {
		String returnTotalTime = "";
		
		int totalTimeHour = Integer.parseInt(totalTime.split(":")[0]);
		int totalTimeMin = Integer.parseInt(totalTime.split(":")[1]);
		int workTimeHour = Integer.parseInt(workTime.split(":")[0]);
		int workTimeMin = Integer.parseInt(workTime.split(":")[1]);		
		
		int returnTotalTimeHour = totalTimeHour + workTimeHour;
		int returnTotalTimeMin = totalTimeMin + workTimeMin;
		if (returnTotalTimeMin >= 60) {
			returnTotalTimeHour += 1;
			returnTotalTimeMin -= 60;
		}
		returnTotalTime = returnTotalTimeHour + ":" + returnTotalTimeMin;
		if (returnTotalTimeMin == 0 ) {
			returnTotalTime += "0";
		}
		return returnTotalTime;
	}

}

