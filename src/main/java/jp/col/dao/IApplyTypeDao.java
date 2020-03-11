package jp.col.dao;

import jp.col.Model.ApplyTypeModel;
import java.util.List;

public interface IApplyTypeDao {
	List<ApplyTypeModel> findAllApplyTypes();
	
	ApplyTypeModel findContentBySfid(String sfid);
}