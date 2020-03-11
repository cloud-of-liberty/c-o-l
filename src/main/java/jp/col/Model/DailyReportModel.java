package jp.col.Model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.Length;

public class DailyReportModel {
 
	//id
    private String id;
    //社員
	private String employee;
	//出勤日付
    private String reportDate;
    //出勤区分
    @NotBlank(message = "出勤区分を選択してください。")
    private String workKind;
    @NotBlank(message = "始業時を入力してください。")
    private String beginHour;
	@NotBlank(message = "始業分を入力してください。")
    private String beginMinute;
    //始業
    private String beginTime;
    //終業
	private String endTime;
    @NotBlank(message = "終業時を入力してください。")
    private String endHour;
	@NotBlank(message = "終業分を入力してください。")
    private String endMinute;
    //日報状態
	private String reportStatus;
	//プロジェクトId1
    @NotBlank(message = "プロジェクト1を選択してください。")
	private String projectId1;
	//プロジェクトId2
	private String projectId2;
	//プロジェクトId3
	private String projectId3;
	//プロジェクトId4
	private String projectId4;
	//プロジェクトId5
	private String projectId5;
	//プロジェクトId6
	private String projectId6;
	//プロジェクトId7
	private String projectId7;
	//プロジェクトId8
	private String projectId8;
	//プロジェクトId9
	private String projectId9;
	//プロジェクトId10
	private String projectId10;
	//タスク1
    @NotBlank(message = "タスク1を入力してください。")
    @Length(max=100 , message = "タスク1が長すぎです。")
	private String task1;
	//タスク2
    @Length(max=100 , message = "タスク2が長すぎです。")
	private String task2;
	//タスク3
    @Length(max=100 , message = "タスク3が長すぎです。")
	private String task3;
	//タスク4
    @Length(max=100 , message = "タスク4が長すぎです。")
	private String task4;
	//タスク5
    @Length(max=100 , message = "タスク5が長すぎです。")
	private String task5;
	//タスク6
    @Length(max=100 , message = "タスク6が長すぎです。")
	private String task6;
	//タスク7
    @Length(max=100 , message = "タスク7が長すぎです。")
	private String task7;
	//タスク8
    @Length(max=100 , message = "タスク8が長すぎです。")
	private String task8;
	//タスク9
    @Length(max=100 , message = "タスク9が長すぎです。")
	private String task9;
	//タスク10
    @Length(max=100 , message = "タスク10が長すぎです。")
	private String task10;
	//プロジェクト名称1
	private String projectName1;
	//プロジェクト名称2
	private String projectName2;
	//プロジェクト名称3
	private String projectName3;
	//プロジェクト名称4
	private String projectName4;
	//プロジェクト名称5
	private String projectName5;
	//プロジェクト名称6
	private String projectName6;
	//プロジェクト名称7
	private String projectName7;
	//プロジェクト名称8
	private String projectName8;
	//プロジェクト名称9
	private String projectName9;
	//プロジェクト名称10
	private String projectName10;
	//仕事時間1
    @Pattern(regexp="^(\\d{1,2}(\\.\\d{1})?)?$" , message="仕事時間1の入力は不正です。")
	private String workTime1;
    @NotBlank(message = "仕事時間1の時間を入力してください。")
	private String workTime1Hours;
    @NotBlank(message = "仕事時間1の分を入力してください。")
	private String workTime1Minutes;
	//仕事時間2
	private String workTime2;
	private String workTime2Hours;
	private String workTime2Minutes;
	//仕事時間3
	private String workTime3;
	private String workTime3Hours;
	private String workTime3Minutes;
	//仕事時間4
	private String workTime4;
	private String workTime4Hours;
	private String workTime4Minutes;
	//仕事時間5
	private String workTime5;
	private String workTime5Hours;
	private String workTime5Minutes;
	//仕事時間6
	private String workTime6;
	private String workTime6Hours;
	private String workTime6Minutes;
	//仕事時間7
	private String workTime7;
	private String workTime7Hours;
	private String workTime7Minutes;
	//仕事時間8
	private String workTime8;
	private String workTime8Hours;
	private String workTime8Minutes;
	//仕事時間9
	private String workTime9;
	private String workTime9Hours;
	private String workTime9Minutes;
	//仕事時間10
	private String workTime10;
	private String workTime10Hours;
	private String workTime10Minutes;
	//仕事内容1
    @NotBlank(message = "仕事内容1を入力してください。")
    @Length(max=255 , message = "仕事内容1が長すぎです。")
	private String workContent1;
	//仕事内容2
    @Length(max=255 , message = "仕事内容2が長すぎです。")
	private String workContent2;
	//仕事内容3
    @Length(max=255 , message = "仕事内容3が長すぎです。")
	private String workContent3;
	//仕事内容4
    @Length(max=255 , message = "仕事内容4が長すぎです。")
	private String workContent4;
	//仕事内容5
    @Length(max=255 , message = "仕事内容5が長すぎです。")
	private String workContent5;
	//仕事内容6
    @Length(max=255 , message = "仕事内容6が長すぎです。")
	private String workContent6;
	//仕事内容7
    @Length(max=255 , message = "仕事内容7が長すぎです。")
	private String workContent7;
	//仕事内容8
    @Length(max=255 , message = "仕事内容8が長すぎです。")
	private String workContent8;
	//仕事内容9
    @Length(max=255 , message = "仕事内容9が長すぎです。")
	private String workContent9;
	//仕事内容10
    @Length(max=255 , message = "仕事内容10が長すぎです。")
	private String workContent10;
    private String comment;
	private String breakTime;
    @NotBlank(message = "休憩時間を入力してください。")
	private String breakHour;
    @NotBlank(message = "休憩時間を入力してください。")
	private String breakMinute;

	public String getBreakHour() {
		return breakHour;
	}
	public void setBreakHour(String breakHour) {
		this.breakHour = breakHour;
	}
	public String getBreakMinute() {
		return breakMinute;
	}
	public void setBreakMinute(String breakMinute) {
		this.breakMinute = breakMinute;
	}
	public String getBreakTime() {
		return breakTime;
	}
	public void setBreakTime(String breakTime) {
		this.breakTime = breakTime;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getEmployee() {
		return employee;
	}
	public void setEmployee(String employee) {
		this.employee = employee;
	}
	public String getReportDate() {
		return reportDate;
	}
	public void setReportDate(String reportDate) {
		this.reportDate = reportDate;
	}
	public String getWorkKind() {
		return workKind;
	}
	public void setWorkKind(String workKind) {
		this.workKind = workKind;
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
	public String getReportStatus() {
		return reportStatus;
	}
	public void setReportStatus(String reportStatus) {
		this.reportStatus = reportStatus;
	}
	
	public String getTask1() {
		return task1;
	}
	public void setTask1(String task1) {
		this.task1 = task1;
	}
	public String getTask2() {
		return task2;
	}
	public void setTask2(String task2) {
		this.task2 = task2;
	}
	public String getTask3() {
		return task3;
	}
	public void setTask3(String task3) {
		this.task3 = task3;
	}
	public String getTask4() {
		return task4;
	}
	public void setTask4(String task4) {
		this.task4 = task4;
	}
	public String getTask5() {
		return task5;
	}
	public void setTask5(String task5) {
		this.task5 = task5;
	}
	public String getTask6() {
		return task6;
	}
	public void setTask6(String task6) {
		this.task6 = task6;
	}
	public String getTask7() {
		return task7;
	}
	public void setTask7(String task7) {
		this.task7 = task7;
	}
	public String getTask8() {
		return task8;
	}
	public void setTask8(String task8) {
		this.task8 = task8;
	}
	public String getTask9() {
		return task9;
	}
	public void setTask9(String task9) {
		this.task9 = task9;
	}
	public String getTask10() {
		return task10;
	}
	public void setTask10(String task10) {
		this.task10 = task10;
	}
	public String getProjectId1() {
		return projectId1;
	}
	public void setProjectId1(String projectId1) {
		this.projectId1 = projectId1;
	}
	public String getProjectId2() {
		return projectId2;
	}
	public void setProjectId2(String projectId2) {
		this.projectId2 = projectId2;
	}
	public String getProjectId3() {
		return projectId3;
	}
	public void setProjectId3(String projectId3) {
		this.projectId3 = projectId3;
	}
	public String getProjectId4() {
		return projectId4;
	}
	public void setProjectId4(String projectId4) {
		this.projectId4 = projectId4;
	}
	public String getProjectId5() {
		return projectId5;
	}
	public void setProjectId5(String projectId5) {
		this.projectId5 = projectId5;
	}
	public String getProjectId6() {
		return projectId6;
	}
	public void setProjectId6(String projectId6) {
		this.projectId6 = projectId6;
	}
	public String getProjectId7() {
		return projectId7;
	}
	public void setProjectId7(String projectId7) {
		this.projectId7 = projectId7;
	}
	public String getProjectId8() {
		return projectId8;
	}
	public void setProjectId8(String projectId8) {
		this.projectId8 = projectId8;
	}
	public String getProjectId9() {
		return projectId9;
	}
	public void setProjectId9(String projectId9) {
		this.projectId9 = projectId9;
	}
	public String getProjectId10() {
		return projectId10;
	}
	public void setProjectId10(String projectId10) {
		this.projectId10 = projectId10;
	}
	public String getProjectName1() {
		return projectName1;
	}
	public void setProjectName1(String projectName1) {
		this.projectName1 = projectName1;
	}
	public String getProjectName2() {
		return projectName2;
	}
	public void setProjectName2(String projectName2) {
		this.projectName2 = projectName2;
	}
	public String getProjectName3() {
		return projectName3;
	}
	public void setProjectName3(String projectName3) {
		this.projectName3 = projectName3;
	}
	public String getProjectName4() {
		return projectName4;
	}
	public void setProjectName4(String projectName4) {
		this.projectName4 = projectName4;
	}
	public String getProjectName5() {
		return projectName5;
	}
	public void setProjectName5(String projectName5) {
		this.projectName5 = projectName5;
	}
	public String getProjectName6() {
		return projectName6;
	}
	public void setProjectName6(String projectName6) {
		this.projectName6 = projectName6;
	}
	public String getProjectName7() {
		return projectName7;
	}
	public void setProjectName7(String projectName7) {
		this.projectName7 = projectName7;
	}
	public String getProjectName8() {
		return projectName8;
	}
	public void setProjectName8(String projectName8) {
		this.projectName8 = projectName8;
	}
	public String getProjectName9() {
		return projectName9;
	}
	public void setProjectName9(String projectName9) {
		this.projectName9 = projectName9;
	}
	public String getProjectName10() {
		return projectName10;
	}
	public void setProjectName10(String projectName10) {
		this.projectName10 = projectName10;
	}
	public String getWorkTime1() {
		return workTime1;
	}
	public void setWorkTime1(String workTime1) {
		this.workTime1 = workTime1;
	}
	public String getWorkTime2() {
		return workTime2;
	}
	public void setWorkTime2(String workTime2) {
		this.workTime2 = workTime2;
	}
	public String getWorkTime3() {
		return workTime3;
	}
	public void setWorkTime3(String workTime3) {
		this.workTime3 = workTime3;
	}
	public String getWorkTime4() {
		return workTime4;
	}
	public void setWorkTime4(String workTime4) {
		this.workTime4 = workTime4;
	}
	public String getWorkTime5() {
		return workTime5;
	}
	public void setWorkTime5(String workTime5) {
		this.workTime5 = workTime5;
	}
	public String getWorkTime6() {
		return workTime6;
	}
	public void setWorkTime6(String workTime6) {
		this.workTime6 = workTime6;
	}
	public String getWorkTime7() {
		return workTime7;
	}
	public void setWorkTime7(String workTime7) {
		this.workTime7 = workTime7;
	}
	public String getWorkTime8() {
		return workTime8;
	}
	public void setWorkTime8(String workTime8) {
		this.workTime8 = workTime8;
	}
	public String getWorkTime9() {
		return workTime9;
	}
	public void setWorkTime9(String workTime9) {
		this.workTime9 = workTime9;
	}
	public String getWorkTime10() {
		return workTime10;
	}
	public void setWorkTime10(String workTime10) {
		this.workTime10 = workTime10;
	}
	public String getWorkContent1() {
		return workContent1;
	}
	public void setWorkContent1(String workContent1) {
		this.workContent1 = workContent1;
	}
	public String getWorkContent2() {
		return workContent2;
	}
	public void setWorkContent2(String workContent2) {
		this.workContent2 = workContent2;
	}
	public String getWorkContent3() {
		return workContent3;
	}
	public void setWorkContent3(String workContent3) {
		this.workContent3 = workContent3;
	}
	public String getWorkContent4() {
		return workContent4;
	}
	public void setWorkContent4(String workContent4) {
		this.workContent4 = workContent4;
	}
	public String getWorkContent5() {
		return workContent5;
	}
	public void setWorkContent5(String workContent5) {
		this.workContent5 = workContent5;
	}
	public String getWorkContent6() {
		return workContent6;
	}
	public void setWorkContent6(String workContent6) {
		this.workContent6 = workContent6;
	}
	public String getWorkContent7() {
		return workContent7;
	}
	public void setWorkContent7(String workContent7) {
		this.workContent7 = workContent7;
	}
	public String getWorkContent8() {
		return workContent8;
	}
	public void setWorkContent8(String workContent8) {
		this.workContent8 = workContent8;
	}
	public String getWorkContent9() {
		return workContent9;
	}
	public void setWorkContent9(String workContent9) {
		this.workContent9 = workContent9;
	}
	public String getWorkContent10() {
		return workContent10;
	}
	public void setWorkContent10(String workContent10) {
		this.workContent10 = workContent10;
	}
    public String getBeginHour() {
		return beginHour;
	}
	public void setBeginHour(String beginHour) {
		this.beginHour = beginHour;
	}
	public String getBeginMinute() {
		return beginMinute;
	}
	public void setBeginMinute(String beginMinute) {
		this.beginMinute = beginMinute;
	}
	public String getEndHour() {
		return endHour;
	}
	public void setEndHour(String endHour) {
		this.endHour = endHour;
	}
	public String getEndMinute() {
		return endMinute;
	}
	public void setEndMinute(String endMinute) {
		this.endMinute = endMinute;
	}
	public String getWorkTime1Hours() {
		return workTime1Hours;
	}
	public void setWorkTime1Hours(String workTime1Hours) {
		this.workTime1Hours = workTime1Hours;
	}
	public String getWorkTime1Minutes() {
		return workTime1Minutes;
	}
	public void setWorkTime1Minutes(String workTime1Minutes) {
		this.workTime1Minutes = workTime1Minutes;
	}
	public String getWorkTime2Hours() {
		return workTime2Hours;
	}
	public void setWorkTime2Hours(String workTime2Hours) {
		this.workTime2Hours = workTime2Hours;
	}
	public String getWorkTime2Minutes() {
		return workTime2Minutes;
	}
	public void setWorkTime2Minutes(String workTime2Minutes) {
		this.workTime2Minutes = workTime2Minutes;
	}
	public String getWorkTime3Hours() {
		return workTime3Hours;
	}
	public void setWorkTime3Hours(String workTime3Hours) {
		this.workTime3Hours = workTime3Hours;
	}
	public String getWorkTime3Minutes() {
		return workTime3Minutes;
	}
	public void setWorkTime3Minutes(String workTime3Minutes) {
		this.workTime3Minutes = workTime3Minutes;
	}
	public String getWorkTime4Hours() {
		return workTime4Hours;
	}
	public void setWorkTime4Hours(String workTime4Hours) {
		this.workTime4Hours = workTime4Hours;
	}
	public String getWorkTime4Minutes() {
		return workTime4Minutes;
	}
	public void setWorkTime4Minutes(String workTime4Minutes) {
		this.workTime4Minutes = workTime4Minutes;
	}
	public String getWorkTime5Hours() {
		return workTime5Hours;
	}
	public void setWorkTime5Hours(String workTime5Hours) {
		this.workTime5Hours = workTime5Hours;
	}
	public String getWorkTime5Minutes() {
		return workTime5Minutes;
	}
	public void setWorkTime5Minutes(String workTime5Minutes) {
		this.workTime5Minutes = workTime5Minutes;
	}
	public String getWorkTime6Hours() {
		return workTime6Hours;
	}
	public void setWorkTime6Hours(String workTime6Hours) {
		this.workTime6Hours = workTime6Hours;
	}
	public String getWorkTime6Minutes() {
		return workTime6Minutes;
	}
	public void setWorkTime6Minutes(String workTime6Minutes) {
		this.workTime6Minutes = workTime6Minutes;
	}
	public String getWorkTime7Hours() {
		return workTime7Hours;
	}
	public void setWorkTime7Hours(String workTime7Hours) {
		this.workTime7Hours = workTime7Hours;
	}
	public String getWorkTime7Minutes() {
		return workTime7Minutes;
	}
	public void setWorkTime7Minutes(String workTime7Minutes) {
		this.workTime7Minutes = workTime7Minutes;
	}
	public String getWorkTime8Hours() {
		return workTime8Hours;
	}
	public void setWorkTime8Hours(String workTime8Hours) {
		this.workTime8Hours = workTime8Hours;
	}
	public String getWorkTime8Minutes() {
		return workTime8Minutes;
	}
	public void setWorkTime8Minutes(String workTime8Minutes) {
		this.workTime8Minutes = workTime8Minutes;
	}
	public String getWorkTime9Hours() {
		return workTime9Hours;
	}
	public void setWorkTime9Hours(String workTime9Hours) {
		this.workTime9Hours = workTime9Hours;
	}
	public String getWorkTime9Minutes() {
		return workTime9Minutes;
	}
	public void setWorkTime9Minutes(String workTime9Minutes) {
		this.workTime9Minutes = workTime9Minutes;
	}
	public String getWorkTime10Hours() {
		return workTime10Hours;
	}
	public void setWorkTime10Hours(String workTime10Hours) {
		this.workTime10Hours = workTime10Hours;
	}
	public String getWorkTime10Minutes() {
		return workTime10Minutes;
	}
	public void setWorkTime10Minutes(String workTime10Minutes) {
		this.workTime10Minutes = workTime10Minutes;
	}
    
    public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}