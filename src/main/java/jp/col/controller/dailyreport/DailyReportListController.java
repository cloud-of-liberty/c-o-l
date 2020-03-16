package jp.col.controller.dailyreport;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
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
}

@Controller
public class DailyReportListController {
	
	private static final String DATEFORMAT_JA = "yyyy年M月";

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
    }

    private DailyReportModel getDailyReport(List<DailyReportModel> dailyReportList , String dateStr) {
    	for (DailyReportModel model : dailyReportList) {
    		if(model.getReportDate().equals(dateStr)) {
    			return model;
    		}
    	}
    	return null;
    }
}

