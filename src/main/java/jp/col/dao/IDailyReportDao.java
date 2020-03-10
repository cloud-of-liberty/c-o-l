package jp.col.dao;

import java.util.List;

import jp.col.Model.DailyReportModel;

public interface IDailyReportDao {

	void insertDailyReport(DailyReportModel dailyReport);

	DailyReportModel findDailyReportByDate(DailyReportModel dailyReport);
	
	void updateDailyReportByDate(DailyReportModel dailyReport);
	
	List<DailyReportModel> findDailyReportByMonth(DailyReportModel dailyReport);
}
