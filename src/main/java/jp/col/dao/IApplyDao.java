package jp.col.dao;

import jp.col.Model.ApplyModel;
import jp.col.Model.UserModel;

import java.util.List;

public interface IApplyDao {

	void insertApply(ApplyModel apply);

	List<ApplyModel> findApplyHistoryByEmployeeId(String employeeSfid);

	ApplyModel findApplyById(int id);
}