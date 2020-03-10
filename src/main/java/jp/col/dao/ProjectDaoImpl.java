package jp.col.dao;

import jp.col.utils.MyBatisUtils;
import org.apache.ibatis.session.SqlSession;
import jp.col.Model.ProjectModel;
import jp.col.dao.IProjectDao;
import java.util.List;

public class ProjectDaoImpl implements IProjectDao {

	private SqlSession sqlSession;

	@Override
	public List<ProjectModel> findAllProjects(){
		List<ProjectModel> projectModelList = null;
		try {
			SqlSession  sqlSession = MyBatisUtils.getSqlSession();
			projectModelList = sqlSession.selectList("findAllProjects");
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			if(sqlSession !=null) {
				sqlSession.close();
			}
		}
		return projectModelList;
	}
}
