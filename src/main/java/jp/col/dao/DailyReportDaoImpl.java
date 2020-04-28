package jp.col.dao;

import jp.col.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;

import jp.col.Model.ApplyModel;
import jp.col.Model.DailyReportModel;
import jp.col.dao.IDailyReportDao;
import java.util.List;

public class DailyReportDaoImpl implements IDailyReportDao {

	private SqlSession sqlSession;

	@Override
	public void insertDailyReport(DailyReportModel dailyReport) {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			sqlSession.insert("insertDailyReport" , dailyReport);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
	}

	@Override
	public DailyReportModel findDailyReportByDate(DailyReportModel dailyReport) {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			dailyReport = sqlSession.selectOne("findDailyReportByDate" , dailyReport);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return dailyReport;
	}

	@Override
	public List<DailyReportModel> findDailyReportByMonth(DailyReportModel dailyReport) {
		List<DailyReportModel> dailyReportList = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			dailyReportList = sqlSession.selectList("findDailyReportByMonth" , dailyReport);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return dailyReportList;
	}

	@Override
	public void updateDailyReportByDate(DailyReportModel dailyReport) {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			sqlSession.update("updateDailyReportByDate" , dailyReport);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
	}
	@Override
	public void updateDailyReportStatus(DailyReportModel dailyReport) {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			sqlSession.update("updateDailyReportStatus" , dailyReport);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
	}
}
