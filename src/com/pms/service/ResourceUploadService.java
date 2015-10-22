package com.pms.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.pms.dao.ResClassifyRelationDAO;
import com.pms.dao.ResColumnClassifyDAO;
import com.pms.dao.ResColumnClassifyRelationDAO;
import com.pms.dao.ResColumnDAO;
import com.pms.dao.ResColumnRelationDAO;
import com.pms.dao.ResDataDAO;
import com.pms.dao.ResDatasetDAO;
import com.pms.dao.ResDatasetSensitiveDAO;
import com.pms.dao.ResRowRelationDAO;
import com.pms.dao.ResValueDAO;
import com.pms.dao.ResValueSensitiveDAO;
import com.pms.dao.ResourceDAO;
import com.pms.dao.impl.ResClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnClassifyDAOImpl;
import com.pms.dao.impl.ResColumnClassifyRelationDAOImpl;
import com.pms.dao.impl.ResColumnDAOImpl;
import com.pms.dao.impl.ResColumnRelationDAOImpl;
import com.pms.dao.impl.ResDataDAOImpl;
import com.pms.dao.impl.ResDatasetDAOImpl;
import com.pms.dao.impl.ResDatasetSensitiveDAOImpl;
import com.pms.dao.impl.ResRowRelationDAOImpl;
import com.pms.dao.impl.ResValueDAOImpl;
import com.pms.dao.impl.ResValueSensitiveDAOImpl;
import com.pms.dao.impl.ResourceDAOImpl;
import com.pms.model.ResColumn;
import com.pms.model.ResColumnClassify;
import com.pms.model.ResData;
import com.pms.model.ResDataSet;
import com.pms.model.ResDataSetSensitive;
import com.pms.model.ResRelationClassify;
import com.pms.model.ResRelationColumn;
import com.pms.model.ResRelationColumnClassify;
import com.pms.model.ResRelationRow;
import com.pms.model.ResRole;
import com.pms.model.ResRoleResource;
import com.pms.model.ResRoleResourceImport;
import com.pms.model.ResValue;
import com.pms.model.ResValueSensitive;

public class ResourceUploadService {
	private final String SHEET_DATASET_SENSITIVE = "数据集敏感度字典";
	private final String SHEET_DATASET = "数据集定义";
	private final String SHEET_COLUMN_CLASSIFY = "字段分类定义";
	private final String SHEET_COLUMN = "字段";
	private final String SHEET_VALUE_SENSITIVE = "字段值敏感度字典";
	private final String SHEET_VALUE = "字段值字典";
	private final String SHEET_COLUMN_ClASSIFY_REALTION = "字段分类关系";
	private final String SHEET_ROW_RELATION = "数据集-字段-字段值";
	private final String SHEET_COLUMN_RELATION = "数据集-字段分类-字段";
	private final String SHEET_CLASSIFY_RELATION = "数据集-字段分类关系";
	
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL = "敏感度编码";
	private final String SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_DATASET_COL_DATA_SET = "数据集编码";
	private final String SHEET_DATASET_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_DATASET_COL_DATASET_NAME = "数据集名称";
	private final String SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL = "数据集敏感度";
	
	private final String SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS = "字段分类编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME = "字段分类名称";
	
	private final String SHEET_COLUMN_COL_ELEMENT = "字段编码";
	private final String SHEET_COLUMN_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_COL_DATA_SET = "数据集编码";
	private final String SHEET_COLUMN_COL_COLUMU_CN = "字段中文名称";
	private final String SHEET_COLUMN_COL_COLUMN_NAME = "字段英文名称";
	private final String SHEET_COLUMN_COL_RMK = "字段描述";
	
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID = "敏感度编码";
	private final String SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME = "敏感度名称";
	
	private final String SHEET_VALUE_COL_ELEMENT_VALUE = "字段值";
	private final String SHEET_VALUE_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_VALUE_COL_VALUE_NAME = "备注";
	private final String SHEET_VALUE_COL_VALUE_SENSITIVE_ID = "字段值敏感度编码";
	private final String SHEET_VALUE_COL_ELEMENT = "字段值所属的字段编码";
	
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS = "字段分类关系代码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE = "源字段分类编码";
	private final String SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE = "目标字段分类编码";
	
	private final String SHEET_ROW_RELATION_COL_ID = "关系的唯一标识";
	private final String SHEET_ROW_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_ROW_RELATION_COL_ELEMENT = "字段编码";
	private final String SHEET_ROW_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_ROW_RELATION_COL_ELEMENT_VALUE = "字段值";
	
	private final String SHEET_COLUMN_RELATION_COL_ID = "关系唯一标识";
	private final String SHEET_COLUMN_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_COLUMN_RELATION_COL_COLUMN_CLASS_ID = "字段分类编码";
	private final String SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	private final String SHEET_COLUMN_RELATION_COL_ELEMENT = "字段";

	private final String SHEET_CLASSIFY_RELATION_COL_ID = "关系唯一标识";
	private final String SHEET_CLASSIFY_RELATION_COL_DATA_SET = "数据集编码";
	private final String SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS = "字段分类关系编码";
	private final String SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS = "所属节点编码";
	
	
	private final String SHEET_ROLE_RESOURCE = "关系对照";
	private final String SHEET_ROLE_RESOURCE_ROLE_TYPE = "角色类型";
	private final String SHEET_ROLE_RESOURCE_ROLE_CODE = "角色代码";
	private final String SHEET_ROLE_RESOURCE_ELEMENT_B050016 = "数据来源";
	private final String SHEET_ROLE_RESOURCE_ELEMENT_B050016_VALUE = "数据来源编码";
	private final String SHEET_ROLE_RESOURCE_DATASET = "协议编码";
	private final String SHEET_ROLE_RESOURCE_SECTION_CLASS = "字段分类编码";
	
	public void UploadResource(File inData) throws Exception {
		InputStream in=new FileInputStream(inData);
        Workbook workbook = WorkbookFactory.create(in);
        
        int sheetCount = workbook.getNumberOfSheets();  //Sheet的数量  
        //deal with seven elements and relationships
        for (int s = 0; s < sheetCount; s++) {
            Sheet sheet = workbook.getSheetAt(s);
            String sheetName = sheet.getSheetName();
            if ( SHEET_DATASET_SENSITIVE.equals(sheetName) ) {
            	updateDatasetSensitive(sheet);
            } else if ( SHEET_DATASET.equals(sheetName) ) {
            	updateDataset(sheet);
            } else if ( SHEET_COLUMN_CLASSIFY.equals(sheetName) ) {
            	updateColumnClassify(sheet);
            } else if ( SHEET_COLUMN.equals(sheetName) ) {
            	updateColumn(sheet);
            } else if ( SHEET_VALUE_SENSITIVE.equals(sheetName) ) {
            	updateValueSensitive(sheet);
            } else if ( SHEET_VALUE.equals(sheetName) ) {
            	updateValue(sheet);
            } else if ( SHEET_COLUMN_ClASSIFY_REALTION.equals(sheetName) ) {
            	updateColumnClassifyRelation(sheet);
            } else if ( SHEET_ROW_RELATION.equals(sheetName) ) {
            	updateRowRelation(sheet);
            } else if ( SHEET_COLUMN_RELATION.equals(sheetName) ) {
            	updateColumnRelation(sheet);
            } else if ( SHEET_CLASSIFY_RELATION.equals(sheetName) ) {
            	updateClassifyRelation(sheet);
            } else if ( SHEET_ROLE_RESOURCE.equals(sheetName) ) {
            	updateRoleResource(sheet);
            }
        }
        
        in.close();
        
        //update resource;
        updateResources();
        
        updateRoleAndResourceRelatioinship();
        
        return;
	}
	
	private void updateDatasetSensitive(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResDataSetSensitive dss = null;
		ResDatasetSensitiveDAO dao = new ResDatasetSensitiveDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		dss = new ResDataSetSensitive();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

            	if(r == 0) {
            		if ( SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL, c);
            		} else if ( SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_LEVEL) ) {
            			dss.setDATASET_SENSITIVE_LEVEL(cellValue);
            		} else if ( c == idx.get(SHEET_DATASET_SENSITIVE_COL_CLUE_SRC_SYS) ) {
            			dss.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_SENSITIVE_COL_DATASET_SENSITIVE_NAME) ) {
            			dss.setDATASET_SENSITIVE_NAME(cellValue);
            		}
            	}
            }
            
            if( r > 0 ) {
            	if(dss.isValid()) {
		            dss.setDELETE_STATUS(ResDataSetSensitive.DELSTATUSNO);
		            dao.ResDataSetSensitiveSave(dss);
            	}
            }
        }
	}
	
	private void updateDataset(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResDataSet ds = null;
		ResDatasetDAO dao = new ResDatasetDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		ds = new ResDataSet();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_DATASET_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATA_SET, c);
            		} else if ( SHEET_DATASET_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_DATASET_COL_DATASET_NAME.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATASET_NAME, c);
            		} else if ( SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL.equals(cellValue) ) {
            			idx.put(SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL, c);
            		}
            		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_DATASET_COL_DATA_SET) ) {
            			ds.setDATA_SET(cellValue);
            		} else if ( c == idx.get(SHEET_DATASET_COL_CLUE_SRC_SYS) ) {
            			ds.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_COL_DATASET_NAME) ) {
            			ds.setDATASET_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_DATASET_COL_DATASET_SENSITIVE_LEVEL) ) {
            			ds.setDATASET_SENSITIVE_LEVEL(cellValue);
            		}
            		
            	}
            }
            
            if( r > 0 ) {
            	if( ds.isValid() ) {
		            ds.setDELETE_STATUS(ResDataSet.DELSTATUSNO);
		            dao.ResDataSetSave(ds);
            	}
            }
        }
	}

	private void updateColumnClassify(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResColumnClassify cc = null;
		ResColumnClassifyDAO dao = new ResColumnClassifyDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		cc = new ResColumnClassify();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS, c);
            		} else if ( SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME, c);
            		}             		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_CLASSIFY_COL_SECTION_CLASS) ) {
            			cc.setSECTION_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_CLASSIFY_COL_CLUE_SRC_SYS) ) {
            			cc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_CLASSIFY_COL_CLASSIFY_NAME) ) {
            			cc.setCLASSIFY_NAME(cellValue);
            		}             		
            	}
            }
            
            if( r > 0 ) {
            	if( cc.isValid() ) {
		            cc.setDELETE_STATUS(ResColumnClassify.DELSTATUSNO);
		            dao.ResColumnClassifySave(cc);
            	}
            }
        }
	}
	
	private void updateColumn(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResColumn col = null;
		ResColumnDAO dao = new ResColumnDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		col = new ResColumn();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	if(r == 0) {
            		if ( SHEET_COLUMN_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_ELEMENT, c);
            		} else if ( SHEET_COLUMN_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_DATA_SET, c);
            		} else if ( SHEET_COLUMN_COL_COLUMU_CN.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_COLUMU_CN, c);
            		} else if ( SHEET_COLUMN_COL_COLUMN_NAME.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_COLUMN_NAME, c);
            		} else if ( SHEET_COLUMN_COL_RMK.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_COL_RMK, c);
            		}            		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_COL_ELEMENT) ) {
            			col.setELEMENT(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_COL_CLUE_SRC_SYS) ) {
            			col.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_DATA_SET) ) {
            			col.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_COLUMU_CN) ) {
            			col.setCOLUMU_CN(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_COLUMN_NAME) ) {
            			col.setCOLUMN_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_COL_RMK) ) {
            			col.setRMK(cellValue);
            		}             		
            	}
            }
            
            if( r > 0 ) {
            	if( col.isValid() ) {
		            col.setDELETE_STATUS(ResColumn.DELSTATUSNO);
		            dao.ResColumnSave(col);
            	}
            }
        }
	}
	
	private void updateValueSensitive(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResValueSensitive vs = null;
		ResValueSensitiveDAO dao = new ResValueSensitiveDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		vs = new ResValueSensitive();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID, c);
            		} else if ( SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME, c);
            		}          		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_ID) ) {
            			vs.setVALUE_SENSITIVE_ID(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_SENSITIVE_COL_CLUE_SRC_SYS) ) {
            			vs.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_SENSITIVE_COL_VALUE_SENSITIVE_NAME) ) {
            			vs.setVALUE_SENSITIVE_NAME(cellValue);
            		}             		
            	}
            }
            
            if( r > 0 ) {
            	if( vs.isValid() ) {
		            vs.setDELETE_STATUS(ResValueSensitive.DELSTATUSNO);
		            dao.ResValueSensitiveSave(vs);
            	}
            }
        }
	}

	private void updateValue(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResValue val = null;
		ResValueDAO dao = new ResValueDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		val = new ResValue();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_VALUE_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_ELEMENT_VALUE, c);
            		} else if ( SHEET_VALUE_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_VALUE_COL_VALUE_NAME.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_VALUE_NAME, c);
            		} else if ( SHEET_VALUE_COL_VALUE_SENSITIVE_ID.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_VALUE_SENSITIVE_ID, c);
            		} else if ( SHEET_VALUE_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_VALUE_COL_ELEMENT, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_VALUE_COL_ELEMENT_VALUE) ) {
            			val.setELEMENT_VALUE(cellValue);
            		} else if ( c == idx.get(SHEET_VALUE_COL_CLUE_SRC_SYS) ) {
            			val.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_VALUE_NAME) ) {
            			val.setVALUE_NAME(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_VALUE_SENSITIVE_ID) ) {
            			val.setVALUE_SENSITIVE_ID(cellValue);
            		} else if ( c== idx.get(SHEET_VALUE_COL_ELEMENT) ) {
            			val.setELEMENT(cellValue);
            		}           		
            	}
            }
            
            if( r > 0 ) {
            	if( val.isValid() ) {
		            val.setDELETE_STATUS(ResValue.DELSTATUSNO);
		            dao.ResValueSave(val);
            	}
            }
        }
	}

	private void updateColumnClassifyRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationColumnClassify rcc = null;
		ResColumnClassifyRelationDAO dao = new ResColumnClassifyRelationDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rcc = new ResRelationColumnClassify();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE, c);
            		} else if ( SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_SECTION_RELATIOIN_CLASS) ) {
            			rcc.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_CLUE_SRC_SYS) ) {
            			rcc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_SRC_CLASS_CODE) ) {
            			rcc.setSRC_CLASS_CODE(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_ClASSIFY_REALTION_COL_DST_CLASS_CODE) ) {
            			rcc.setDST_CLASS_CODE(cellValue);
            		}          		
            	}
            }
            
            if( r > 0 ) {
            	if( rcc.isValid() ) {
		            rcc.setDELETE_STATUS(ResRelationColumnClassify.DELSTATUSNO);
		            dao.ResRelationColumnClassifySave(rcc);
            	}
            }
        }
	}

	private void updateRowRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationRow rr = null;
		ResRowRelationDAO dao = new ResRowRelationDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rr = new ResRelationRow();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_ROW_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ID, c);
            		} else if ( SHEET_ROW_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_ROW_RELATION_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ELEMENT, c);
            		} else if ( SHEET_ROW_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_ROW_RELATION_COL_ELEMENT_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROW_RELATION_COL_ELEMENT_VALUE, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_ROW_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_ROW_RELATION_COL_DATA_SET) ) {
            			rr.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_ELEMENT) ) {
            			rr.setELEMENT(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_CLUE_SRC_SYS) ) {
            			rr.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_ROW_RELATION_COL_ELEMENT_VALUE) ) {
            			rr.setELEMENT_VALUE(cellValue);
            		}        		
            	}
            }
            
            if( r > 0 ) {
            	if( rr.isValid() ) {
		            rr.setDELETE_STATUS(ResRelationRow.DELSTATUSNO);
		            dao.ResRelationRowSave(rr);
            	}
            }
        }
	}
	
	private void updateColumnRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationColumn rc = null;
		ResColumnRelationDAO dao = new ResColumnRelationDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rc = new ResRelationColumn();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            	
            	if(r == 0) {
            		if ( SHEET_COLUMN_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_ID, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_COLUMN_CLASS_ID.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_COLUMN_CLASS_ID, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS, c);
            		} else if ( SHEET_COLUMN_RELATION_COL_ELEMENT.equals(cellValue) ) {
            			idx.put(SHEET_COLUMN_RELATION_COL_ELEMENT, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_COLUMN_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_COLUMN_RELATION_COL_DATA_SET) ) {
            			rc.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_COLUMN_CLASS_ID) ) {
            			rc.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_CLUE_SRC_SYS) ) {
            			rc.setCLUE_SRC_SYS(cellValue);
            		} else if ( c== idx.get(SHEET_COLUMN_RELATION_COL_ELEMENT) ) {
            			rc.setELEMENT(cellValue);
            		}        		
            	}
            }
            
            if( r > 0 ) {
            	if( rc.isValid() ) {
		            rc.setDELETE_STATUS(ResRelationColumn.DELSTATUSNO);
		            dao.ResRelationColumnSave(rc);
            	}
            }
        }
	}
	
	private void updateClassifyRelation(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();
		ResRelationClassify rc = null;
		ResClassifyRelationDAO dao = new ResClassifyRelationDAOImpl();
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	if( r > 0 ) {
        		rc = new ResRelationClassify();
        	}
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);
            		
            	if(r == 0) {
            		if ( SHEET_CLASSIFY_RELATION_COL_ID.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_ID, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_DATA_SET.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_DATA_SET, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS, c);
            		} else if ( SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS.equals(cellValue) ) {
            			idx.put(SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS, c);
            		}
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_CLASSIFY_RELATION_COL_ID) ) {
            			//rr.setId(cellValue);
            		} else if ( c == idx.get(SHEET_CLASSIFY_RELATION_COL_DATA_SET) ) {
            			rc.setDATA_SET(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RELATION_COL_SECTION_RELATIOIN_CLASS) ) {
            			rc.setSECTION_RELATIOIN_CLASS(cellValue);
            		} else if ( c== idx.get(SHEET_CLASSIFY_RELATION_COL_CLUE_SRC_SYS) ) {
            			rc.setCLUE_SRC_SYS(cellValue);
            		}       		
            	}
            }
            
            if( r > 0 ) {
            	if( rc.isValid() ) {
		            rc.setDELETE_STATUS(ResRelationClassify.DELSTATUSNO);
		            dao.ResRelationClassifySave(rc);
            	}
            }
        }
	}
	
	private void updateRoleResource(Sheet sheet) throws Exception {
		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数
		Map<String, Integer> idx = new HashMap<String, Integer>();

		ResRoleResourceImport rrri = null;
		ResourceDAO dao = new ResourceDAOImpl();
		dao.ResRoleResourceImportClear();
		
		String roleName = "";
		String roleId = "";
		String element = "B050016";
		String elemnetValue = "";
		String dataset = "";
		String sectionClass = "";
		String [] dataset0SectionClasses = null; 
		//遍历每一行  
        for (int r = 0; r < rowCount; r++) {
        	Row row = sheet.getRow(r);
        	if(row == null) {
        		continue;
        	}
        	
        	int cellCount = row.getPhysicalNumberOfCells(); //获取总列数 
        	//遍历每一列  
            for (int c = 0; c < cellCount; c++) {
            	Cell cell = row.getCell(c);
            	String cellValue = getCellValue(cell);

//            	if(cellValue.equals("111053000004")) {
//            		System.out.print(cellValue);
//            	}
//            	if(cellValue.equals("111053000006")) {
//            		System.out.print(cellValue);
//            	}
//            	if(cellValue.equals("111053000007")) {
//            		System.out.print(cellValue);
//            	}
//            	if(r == 3585) {
//            		System.out.println(r);
//            	}
            	
            	if(r == 0) {
            		if ( SHEET_ROLE_RESOURCE_ROLE_TYPE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_ROLE_TYPE, c);
            		} else if ( SHEET_ROLE_RESOURCE_ROLE_CODE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_ROLE_CODE, c);
            		} else if ( SHEET_ROLE_RESOURCE_ELEMENT_B050016.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_ELEMENT_B050016, c);
            		} else if ( SHEET_ROLE_RESOURCE_ELEMENT_B050016_VALUE.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_ELEMENT_B050016_VALUE, c);
            		} else if ( SHEET_ROLE_RESOURCE_DATASET.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_DATASET, c);
            		} else if ( SHEET_ROLE_RESOURCE_SECTION_CLASS.equals(cellValue) ) {
            			idx.put(SHEET_ROLE_RESOURCE_SECTION_CLASS, c);
            		}            		
            	} else {
            		if(idx.size() == 0) {
            			throw new Exception("导入数据文件格式不正确!");
            		}
            		if( c == idx.get(SHEET_ROLE_RESOURCE_ROLE_TYPE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				roleName = cellValue;
            			}
            		} else if ( c == idx.get(SHEET_ROLE_RESOURCE_ROLE_CODE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				roleId = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_ELEMENT_B050016) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				//save element name, so nothing to do
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_ELEMENT_B050016_VALUE) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
            				elemnetValue = cellValue;
            				dataset0SectionClasses = null;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_DATASET) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
                			dataset = cellValue;
            			}
            		} else if ( c== idx.get(SHEET_ROLE_RESOURCE_SECTION_CLASS) ) {
            			if( cellValue != null && cellValue.length() > 0 ) {
                			sectionClass = cellValue;
            			}
            		}
            	}
            }
            
            if( r > 0 ) {
            	if( "WA_SOURCE_0000".equals(dataset) ) {
            		dataset0SectionClasses = sectionClass.split("、");
            		continue;
            	}
            	String [] elemnetValues = elemnetValue.split("、");
            	String [] sectionClasses = sectionClass.split("、");
            	Set<String> sectionClassesSet = new HashSet<String>();
            	for(int i = 0; i < sectionClasses.length; i++) {
            		sectionClassesSet.add(sectionClasses[i]);
            	}
            	if(dataset.startsWith("WA_SOURCE") && !"WA_SOURCE_0000".equals(dataset) && dataset0SectionClasses != null) {
            		for(int i = 0; i < dataset0SectionClasses.length; i++) {
                		sectionClassesSet.add(dataset0SectionClasses[i]);
                	}
            	}
            	
            	for(int i = 0; i < elemnetValues.length; i++) {
            		Iterator<String> it = sectionClassesSet.iterator();
					while(it.hasNext())
					{
						String sectionClassItem = it.next();
						rrri = new ResRoleResourceImport();
						rrri.setRoleName(roleName);
						rrri.setRoleId(roleId);
						rrri.setElement(element);
						rrri.setElemnetValue(elemnetValues[i]);
						rrri.setDataSet(dataset);
						rrri.setSectionClass(sectionClassItem);
		            	if( rrri.isValid() ) {
				            dao.ResRoleResourceImportAdd(rrri);
		            	}
					}
            	}
            }
        }
        return;
	}
	
	private void updateRoleAndResourceRelatioinship() throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		// clear old resource and role relationship
		dao.ClearPublicRoleAndDataResourceRelationship();
		
		List<ResRoleResourceImport> rrris = dao.GetResRoleResourceImport();
		for(int i = 0; i < rrris.size(); i++) {
			ResRoleResourceImport rrri = rrris.get(i);
			
			// update role
			createOrUpdateRole(rrri.getRoleName(), rrri.getRoleId());
			
			// update role and resource relationship
			updateRoleAndResourceRelationshipOfColumn(rrri.getRoleId(), rrri.getDataSet(), rrri.getElement());
			
			updateRoleAndResourceRelationshipOfRelationRow(rrri.getRoleId(), rrri.getDataSet(), rrri.getElement(), rrri.getElemnetValue());
			
			updateRoleAndResourceRelationshipOfRelationColumn(rrri.getRoleId(), rrri.getDataSet(), rrri.getSectionClass(), rrri.getElement());
		}
		return;
	}
	
	private void updateRoleAndResourceRelationshipOfRelationColumn(
			String roleId, String dataSet, String sectionClass, String element) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData resource = dao.GetDataByRelationColumn(dataSet, sectionClass, element);
		if(resource != null) {
			addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
		}
		return;
	}

	private void updateRoleAndResourceRelationshipOfRelationRow(String roleId,
			String dataSet, String element, String elemnetValue) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData resource = dao.GetDataByRelationRow(dataSet, element, elemnetValue);
		if(resource != null) {
			addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
		}
		return;
	}

	private void updateRoleAndResourceRelationshipOfColumn(String roleId,
			String dataSet, String element) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResData resource = dao.GetDataByColumn(dataSet, element);
		if(resource != null) {
			addPublicRoleAndDataResourceRelationship(roleId, resource.getRESOURCE_ID());
		}
		return;
	}

	private void addPublicRoleAndDataResourceRelationship(String roleId,
			String resId) throws Exception {
		ResourceDAO dao = new ResourceDAOImpl();
		ResRoleResource rrr = new ResRoleResource();
		rrr.setBUSINESS_ROLE(roleId);
		rrr.setRESOURCE_ID(resId);
		rrr.setRESOURCE_CLASS(ResRoleResource.RESCLASSDATA);
		rrr.setDATA_VERSION(1);
		rrr.setDELETE_STATUS(ResRoleResource.DELSTATUSNO);
		dao.ResRoleResourceAdd(rrr);
		return;
	}

	private void createOrUpdateRole(String roleName, String roleId) throws Exception {
		ResRole role = new ResRole();
		role.setBUSINESS_ROLE_NAME(roleName);
		role.setBUSINESS_ROLE(roleId);
		role.setBUSINESS_ROLE_TYPE(ResRole.RESROLETYPEPUBLIC);
		role.setDATA_VERSION(1);
		role.setDELETE_STATUS(ResRole.DELSTATUSNO);
		role.setROLE_DESC(roleName);
		ResourceDAO dao = new ResourceDAOImpl();
		dao.RoleImport(role);
		return;
	}

	private String getCellValue(Cell cell) {
		if(cell == null) {
			return "";
		}
		int cellType = cell.getCellType();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String cellValue = null;  
        switch(cellType) {
            case Cell.CELL_TYPE_STRING: //文本  
                cellValue = cell.getStringCellValue();  
                break;  
            case Cell.CELL_TYPE_NUMERIC: //数字、日期  
                if(DateUtil.isCellDateFormatted(cell)) {  
                    cellValue = sdf.format(cell.getDateCellValue()); //日期型  
                }  
                else {  
                    cellValue = String.valueOf(cell.getNumericCellValue()); //数字  
                    if(cellValue.contains(".")) {
                    	cellValue = cellValue.substring(0, cellValue.indexOf('.'));
                    }
                }  
                break;  
            case Cell.CELL_TYPE_BOOLEAN: //布尔型  
                cellValue = String.valueOf(cell.getBooleanCellValue());  
                break;  
            case Cell.CELL_TYPE_BLANK: //空白  
                cellValue = cell.getStringCellValue();  
                break;  
            case Cell.CELL_TYPE_ERROR: //错误  
                cellValue = "错误";  
                break;  
            case Cell.CELL_TYPE_FORMULA: //公式  
                cellValue = "错误";  
                break;  
            default:  
                cellValue = "错误";  
        }
        return cellValue;
	}
	
	private void updateResources() throws Exception {
		//perpare dataset 
		ResDatasetDAO rdsdao = new ResDatasetDAOImpl();
		List<ResDataSet> rdss = rdsdao.QueryAllDataSet();
		
		Map<String, ResDataSet> rdsMap = new HashMap<String, ResDataSet>();
		for(int i = 0; i<rdss.size(); i++) {
			rdsMap.put(rdss.get(i).getDATA_SET(), rdss.get(i));
		}
		
		//updateColumn
		ResColumnDAO rcdao = new ResColumnDAOImpl();
		List<ResColumn> rcs = rcdao.QueryAllColumn();
		
		for(int i = 0; i<rcs.size(); i++) {
			updateResourceOfColumn( rcs.get(i), rdsMap );
		}
		
		//updateColumnRelation
		ResColumnRelationDAO rcrdao = new ResColumnRelationDAOImpl();
		List<ResRelationColumn> rrcs = rcrdao.QueryAllResRelationColumn();
		
		for(int i = 0; i<rrcs.size(); i++) {
			updateResourceOfRelationColumn( rrcs.get(i), rdsMap );
		}
		
		//updateRowRelation
		ResRowRelationDAO rrrdao = new ResRowRelationDAOImpl();
		List<ResRelationRow> rrrs = rrrdao.QueryAllResRelationRow();
		
		for(int i = 0; i<rrrs.size(); i++) {
			updateResourceOfRelationRow( rrrs.get(i), rdsMap );
		}
		
		//updateClassifyRelation
		ResClassifyRelationDAO rclassrdao = new ResClassifyRelationDAOImpl();
		List<ResRelationClassify> rrclasss = rclassrdao.QueryAllResRelationClassify();
		
		for(int i = 0; i<rrclasss.size(); i++) {
			updateResourceOfRelationClassify( rrclasss.get(i), rdsMap );
		}
	}
	
	private void updateResourceOfColumn( ResColumn rc, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResData rd = new ResData();
		rd.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rd.setRESOURCE_DESCRIBE("字段数据资源");
		rd.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rc.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
		rd.setDATA_SET(rc.getDATA_SET());
		rd.setELEMENT(rc.getELEMENT());
		rd.setDELETE_STATUS(ResData.DELSTATUSNO);
		rd.setResource_type(ResData.RESTYPEPUBLIC);
		rd.setName(rc.getCOLUMU_CN());
		rddao.ResDataOfColumnSave(rd, rc.getCLUE_SRC_SYS());
	}
	
	private void updateResourceOfRelationColumn( ResRelationColumn rrc, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResData rd = new ResData();
		rd.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rd.setDELETE_STATUS(ResData.DELSTATUSNO);
		rd.setResource_type(ResData.RESTYPEPUBLIC);
		rd.setRESOURCE_DESCRIBE("数据集-字段分类-字段数据资源");
		rd.setDATA_SET(rrc.getDATA_SET());
		rd.setELEMENT(rrc.getELEMENT());
		rd.setSECTION_CLASS(rrc.getSECTION_RELATIOIN_CLASS());
		
		rddao.ResDataOfRelationColumnSave(rd, rrc.getCLUE_SRC_SYS());
	}

	private void updateResourceOfRelationRow( ResRelationRow rrr, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResData rd = new ResData();
		rd.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rd.setDELETE_STATUS(ResData.DELSTATUSNO);
		rd.setResource_type(ResData.RESTYPEPUBLIC);
		rd.setRESOURCE_DESCRIBE("数据集-字段-字段值数据资源");
		rd.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rrr.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
		rd.setDATA_SET(rrr.getDATA_SET());
		rd.setELEMENT(rrr.getELEMENT());
		rd.setELEMENT_VALUE(rrr.getELEMENT_VALUE());
		rd.setOPERATE_SYMBOL("等于");
		rd.setName(rrr.getELEMENT());
		
		rddao.ResDataOfRelationRowSave(rd, rrr.getCLUE_SRC_SYS());
	}
	
	
	
	private void updateResourceOfRelationClassify( ResRelationClassify rrc, Map<String, ResDataSet> rdsMap ) throws Exception {
		ResDataDAO rddao = new ResDataDAOImpl();
		ResData rd = new ResData();
		rd.setRESOURCE_STATUS(ResData.RESSTATUSENABLE);
		rd.setDELETE_STATUS(ResData.DELSTATUSNO);
		rd.setResource_type(ResData.RESTYPEPUBLIC);
		rd.setRESOURCE_DESCRIBE("数据集-字段分类关系数据资源");
		rd.setDATASET_SENSITIVE_LEVEL( rdsMap.get(rrc.getDATA_SET()).getDATASET_SENSITIVE_LEVEL() );
		rd.setDATA_SET(rrc.getDATA_SET());
		rd.setSECTION_RELATIOIN_CLASS(rrc.getSECTION_RELATIOIN_CLASS());
		rd.setName("字段分类关系" + rrc.getSECTION_RELATIOIN_CLASS());
		
		rddao.ResDataOfRelationClassifySave(rd, rrc.getCLUE_SRC_SYS());
	}
	
//	private void sheetProcess(Sheet sheet){
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//		int rowCount = sheet.getPhysicalNumberOfRows(); //获取总行数  
//        //遍历每一行  
//        for (int r = 0; r < rowCount; r++) {  
//            Row row = sheet.getRow(r);
//            int cellCount = row.getPhysicalNumberOfCells(); //获取总列数  
//            //遍历每一列  
//            for (int c = 0; c < cellCount; c++) {  
//                Cell cell = row.getCell(c);  
//                int cellType = cell.getCellType();  
//                String cellValue = null;  
//                switch(cellType) {  
//                    case Cell.CELL_TYPE_STRING: //文本  
//                        cellValue = cell.getStringCellValue();  
//                        break;  
//                    case Cell.CELL_TYPE_NUMERIC: //数字、日期  
//                        if(DateUtil.isCellDateFormatted(cell)) {  
//                            cellValue = sdf.format(cell.getDateCellValue()); //日期型  
//                        }  
//                        else {  
//                            cellValue = String.valueOf(cell.getNumericCellValue()); //数字  
//                        }  
//                        break;  
//                    case Cell.CELL_TYPE_BOOLEAN: //布尔型  
//                        cellValue = String.valueOf(cell.getBooleanCellValue());  
//                        break;  
//                    case Cell.CELL_TYPE_BLANK: //空白  
//                        cellValue = cell.getStringCellValue();  
//                        break;  
//                    case Cell.CELL_TYPE_ERROR: //错误  
//                        cellValue = "错误";  
//                        break;  
//                    case Cell.CELL_TYPE_FORMULA: //公式  
//                        cellValue = "错误";  
//                        break;  
//                    default:  
//                        cellValue = "错误";  
//                }  
//                System.out.print(cellValue + "\t");
//                    
//            }  
//            System.out.println(); 
//        }
//	}
	
}
