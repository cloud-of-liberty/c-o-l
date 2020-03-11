package jp.col.Model;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.Length;

public class ApplyModel {
 
    private String id;
    private String name;
    @NotBlank(message = "申請種類を選択してください。")
    /* 申請種類のsfid */
    private String applyType;
    private String applyTypeName;
    private String applyDateTime;
    @NotBlank(message = "申請内容を入力してください。")
    @Length(max=32000 , message = "申請内容が長すぎです。")
    private String applyContent;
    private String applyStatus;
    private String employeeId;
	private String rejectReason;
    private String applicantMail;
	private String applyVacationDays;
    private String applyVacationHours;
    private String restVacationDays;
    private String restVacationHours;
    private String applyVacation;
    private String HolidayStartDate;
    private String HolidayEndDate;

	public String getHolidayStartDate() {
		return HolidayStartDate;
	}

	public void setHolidayStartDate(String holidayStartDate) {
		HolidayStartDate = holidayStartDate;
	}

	public String getHolidayEndDate() {
		return HolidayEndDate;
	}

	public void setHolidayEndDate(String holidayEndDate) {
		HolidayEndDate = holidayEndDate;
	}

	public String getApplyVacation() {
		return applyVacation;
	}

	public void setApplyVacation(String applyVacation) {
		this.applyVacation = applyVacation;
	}

	public String getRestVacationDays() {
		return restVacationDays;
	}

	public void setRestVacationDays(String restVacationDays) {
		this.restVacationDays = restVacationDays;
	}

	public String getRestVacationHours() {
		return restVacationHours;
	}

	public void setRestVacationHours(String restVacationHours) {
		this.restVacationHours = restVacationHours;
	}

	public String getApplicantMail() {
		return applicantMail;
	}

	public String getApplyVacationDays() {
		return applyVacationDays;
	}

	public void setApplyVacationDays(String applyVacationDays) {
		this.applyVacationDays = applyVacationDays;
	}

	public String getApplyVacationHours() {
		return applyVacationHours;
	}

	public void setApplyVacationHours(String applyVacationHours) {
		this.applyVacationHours = applyVacationHours;
	}

	public void setApplicantMail(String applicantMail) {
		this.applicantMail = applicantMail;
	}

	public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
 
	public void setApplyType(String applyType) {
        this.applyType = applyType;
    }
 
    public String getApplyType() {
        return applyType;
    }
 
    public String getApplyDateTime() {
        return applyDateTime;
    }
 
    public void setApplyDateTime(String applyDateTime) {
        this.applyDateTime = applyDateTime;
    }
 
    public String getApplyContent() {
        return applyContent;
    }
 
    public void setApplyContent(String applyContent) {
        this.applyContent = applyContent;
    }
 
    public String getApplyStatus() {
        return applyStatus;
    }
 
    public void setApplyStatus(String applyStatus) {
        this.applyStatus = applyStatus;
    }

    public String getEmployeeId() {
        return employeeId;
    }
 
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRejectReason() {
        return rejectReason;
    }
 
    public void setRejectReason(String rejectReason) {
        this.rejectReason = rejectReason;
    }

	public String getApplyTypeName() {
		return applyTypeName;
	}

	public void setApplyTypeName(String applyTypeName) {
		this.applyTypeName = applyTypeName;
	}
    
}