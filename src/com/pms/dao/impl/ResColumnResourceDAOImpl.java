package com.pms.dao.impl;

import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.pms.dao.ResColumnResourceDAO;
import com.pms.model.HibernateUtil;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnPrivate;
import com.pms.util.DateTimeUtil;

public class ResColumnResourceDAOImpl implements ResColumnResourceDAO {

	@Override
	public ResColumn ResColumnSave(ResColumn c)
			throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResColumn rs = null;
		String sqlString = "select * from WA_COLUMN where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and CLUE_SRC_SYS = :CLUE_SRC_SYS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			q.setString("DATA_SET", c.getDATA_SET());
			q.setString("ELEMENT", c.getELEMENT());
			q.setString("CLUE_SRC_SYS", c.getCLUE_SRC_SYS());
			rs = (ResColumn) q.uniqueResult();
			
			if(rs != null) {
				c.setId(rs.getId());
				c.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				c.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			c.setLATEST_MOD_TIME(timenow);
			
			c = (ResColumn) session.merge(c);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名字段。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return c;
	}
	
	@Override
	public ResColumnPrivate ResColumnPrivateSave(ResColumnPrivate rcp) throws Exception {
		//打开线程安全的session对象
		Session session = HibernateUtil.currentSession();
		//打开事务
		Transaction tx = session.beginTransaction();
		
		ResColumnPrivate rs = null;
		String sqlString = "select * from WA_COLUMN_PRIVATE where DATA_SET = :DATA_SET and ELEMENT = :ELEMENT and CLUE_SRC_SYS = :CLUE_SRC_SYS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumnPrivate.class);
			q.setString("DATA_SET", rcp.getDATA_SET());
			q.setString("ELEMENT", rcp.getELEMENT());
			q.setString("CLUE_SRC_SYS", rcp.getCLUE_SRC_SYS());
			rs = (ResColumnPrivate) q.uniqueResult();
			
			if(rs != null) {
				rcp.setId(rs.getId());
				rcp.setDATA_VERSION(rs.getDATA_VERSION() + 1);
			} else {
				rcp.setDATA_VERSION( 1 );
			}
			
			String timenow = DateTimeUtil.GetCurrentTime();
			rcp.setLATEST_MOD_TIME(timenow);
			
			rcp = (ResColumnPrivate) session.merge(rcp);
			tx.commit();
		} catch(ConstraintViolationException cne){
			tx.rollback();
			System.out.println(cne.getSQLException().getMessage());
			throw new Exception("存在重名私有字段。");
		}
		catch(org.hibernate.exception.SQLGrammarException e)
		{
			tx.rollback();
			System.out.println(e.getSQLException().getMessage());
			throw e.getSQLException();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rcp;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResColumn> QueryAllColumn() throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResColumn> rs = null;
		String sqlString = "select * from WA_COLUMN ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@Override
	public ResColumn QueryColumnByElement(String dataset, String element)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		ResColumn rs = null;
		String sqlString = "select * from WA_COLUMN where 1 = 1 ";
		if( dataset != null && dataset.length() > 0 ) {
			sqlString += "and DATA_SET = :DATA_SET ";
		}
		if( element != null && element.length() > 0 ) {
			sqlString += "and ELEMENT = :ELEMENT ";
		}
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			if( dataset != null && dataset.length() > 0 ) {
				q.setString("DATA_SET", dataset);
			}
			if( element != null && element.length() > 0 ) {
				q.setString("ELEMENT", element);
			}
			rs = (ResColumn) q.uniqueResult();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}

	@Override
	public ResColumnPrivate QueryColumnPrivateByElement(String dataset, String element)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		ResColumnPrivate rs = null;
		String sqlString = "select * from WA_COLUMN_PRIVATE where 1=1 ";
		if( dataset != null && dataset.length() > 0 ) {
			sqlString += "and DATA_SET = :DATA_SET ";
		}
		if( element != null && element.length() > 0 ) {
			sqlString += "and ELEMENT = :ELEMENT ";
		}
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumnPrivate.class);
			if( dataset != null && dataset.length() > 0 ) {
				q.setString("DATA_SET", dataset);
			}
			if( element != null && element.length() > 0 ) {
				q.setString("ELEMENT", element);
			}
			rs = (ResColumnPrivate) q.uniqueResult();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResColumn> QueryRowColumn(String dataSet)
		throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResColumn> rs = null;
		String sqlString = "SELECT DISTINCT a.* FROM WA_COLUMN a,WA_ROW_RELATION b WHERE a.DATA_SET=b.DATA_SET AND a.element=b.element AND a.DATA_SET = :DATA_SET ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			q.setString("DATA_SET", dataSet);
			rs = q.list();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ResColumn> QueryColumnColumn(String dataSet,String sectionClass)
			throws Exception {
		Session session = HibernateUtil.currentSession();
		Transaction tx = session.beginTransaction();
		
		List<ResColumn> rs = null;
		String sqlString = "SELECT DISTINCT a.* FROM WA_COLUMN a, WA_COLUMN_RELATION b WHERE a.DATA_SET =b.DATA_SET AND a.ELEMENT = b.ELEMENT AND  b.DATA_SET = :DATA_SET AND b.SECTION_CLASS = :SECTION_CLASS ";
		try {
			Query q = session.createSQLQuery(sqlString).addEntity(ResColumn.class);
			q.setString("DATA_SET", dataSet);
			q.setString("SECTION_CLASS", sectionClass);
			rs = q.list();
			tx.commit();
		} catch(Exception e) {
			e.printStackTrace();
			tx.rollback();
			System.out.println(e.getMessage());
			throw e;
		}
		finally
		{
			HibernateUtil.closeSession();
		}
		return rs;
	}
	
}
