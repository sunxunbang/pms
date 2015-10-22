package com.pms.dao;

import java.util.List;

import com.pms.model.Privilege;
import com.pms.model.ResRole;

public interface PrivilegeDAO {
	public Privilege PrivilegeAdd(Privilege priv) throws Exception;
	public void PrivilegeMod(Privilege priv) throws Exception;
	public void PrivilegeDel(Privilege priv) throws Exception;

//	public List<Privilege> GetPrivileges(int pid, int page, int rows) throws Exception;

	public void UpdatePrivilegeByOwnerId(String ownerid, int ownertype,
			List<Privilege> privileges) throws Exception;
	public List<Privilege> QueryPrivilegesByOwnerId(String ownerid, int ownertype) throws Exception;
	public int QueryPrivilegesCountByOwnerId(int id, int ownertypeuser) throws Exception;
	public List<ResRole> QueryPrivInfosByUserid(String userid) throws Exception;
	public List<ResRole> QueryPrivInfosByUsersOrg(String userid) throws Exception;
	public List<ResRole> QueryPrivInfosByUsersGroup(String groupid) throws Exception;
}
