package jp.col.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import jp.col.Model.ApplyTypeModel;
import jp.col.utils.MyBatisUtils;

public class ApplyTypeDaoImpl implements IApplyTypeDao {

	private SqlSession sqlSession;

	@Override
	public List<ApplyTypeModel> findAllApplyTypes(){
		List<ApplyTypeModel> applyTypeList = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			applyTypeList = sqlSession.selectList("findAllApplyTypes");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return applyTypeList;
	}

	@Override
	public ApplyTypeModel findContentBySfid(String sfid){
		ApplyTypeModel applyType = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			applyType = sqlSession.selectOne("findContentBySfid" , sfid);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return applyType;
	}
}
