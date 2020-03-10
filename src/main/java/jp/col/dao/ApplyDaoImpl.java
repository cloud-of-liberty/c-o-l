package jp.col.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import jp.col.Model.ApplyModel;
import jp.col.utils.MyBatisUtils;

public class ApplyDaoImpl implements IApplyDao {

	private SqlSession sqlSession;

	@Override
	public void insertApply(ApplyModel apply) {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			sqlSession.insert("insertApply" , apply);
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
	public List<ApplyModel> findApplyHistoryByEmployeeId(String employeeSfid){
		List<ApplyModel> applyModelList = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			applyModelList = sqlSession.selectList("findApplyHistoryByEmployeeId" , employeeSfid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return applyModelList;
	}

	@Override
	public ApplyModel findApplyById(int id){
		ApplyModel applyModel = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			applyModel = sqlSession.selectOne("findApplyById" , id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return applyModel;
	}
}
