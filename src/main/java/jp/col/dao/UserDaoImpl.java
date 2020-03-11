package jp.col.dao;

import jp.col.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import jp.col.Model.UserModel;
import jp.col.Model.CertificationModel;
import jp.col.Model.ContractInfoModel;
import jp.col.dao.IUserDao;
import java.util.List;

public class UserDaoImpl implements IUserDao {

	private SqlSession sqlSession;

	@Override
	public UserModel findByEmail(String email) {
		
		UserModel user = new UserModel();
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
  		
			user.setEmail(email);
			user = sqlSession.selectOne("findByEmail" , user);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return user;
	}

	@Override
	public UserModel findUserInfoById(long id){
		UserModel user = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
  		
			user = sqlSession.selectOne("findUserInfoById" , id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return user;
	}

	@Override
	public List<CertificationModel> findCertificationInfoById(long id){
		List<CertificationModel> certificationList = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			certificationList = sqlSession.selectList("findCertificationInfoById" , id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return certificationList;
	}
	
	@Override
	public void updatePassword(UserModel user) throws Exception {
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			sqlSession.update("updatePassword" , user);
			sqlSession.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("エラーが発生しました、システム管理員に連絡してください。");
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
	}
	@Override
	public ContractInfoModel findContractInfoById(long id) throws Exception{
		ContractInfoModel contractInfo = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			contractInfo = sqlSession.selectOne("findContractInfoById" , id);
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return contractInfo;
	}	
}
