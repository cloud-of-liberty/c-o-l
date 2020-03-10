package jp.col.dao;

import jp.col.Model.UserModel;
import jp.col.Model.CertificationModel;
import jp.col.Model.ContractInfoModel;

import java.util.List;

public interface IUserDao {
    UserModel findByEmail(String email) throws Exception;

	UserModel findUserInfoById(long id) throws Exception;

	List<CertificationModel> findCertificationInfoById(long id) throws Exception;
	
	void updatePassword(UserModel user) throws Exception;

	ContractInfoModel findContractInfoById(long id) throws Exception;
}