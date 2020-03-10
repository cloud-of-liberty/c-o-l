package jp.col.dao;

import jp.col.Model.ProjectModel;
import java.util.List;

public interface IProjectDao {
	List<ProjectModel> findAllProjects();
}