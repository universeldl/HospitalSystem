package com.hospital.service.impl;

import com.hospital.dao.ChoiceDao;
import com.hospital.dao.ForfeitDao;
import com.hospital.domain.*;
import com.hospital.service.ChoiceService;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import net.sf.json.JSONObject;
import org.apache.struts2.ServletActionContext;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class ChoiceServiceImpl implements ChoiceService{

	private ChoiceDao choiceDao;

	private ForfeitDao forfeitDao;
	
	

	public void setForfeitDao(ForfeitDao forfeitDao) {
		this.forfeitDao = forfeitDao;
	}


	public void setChoiceDao(ChoiceDao choiceDao) {
		this.choiceDao = choiceDao;
	}



	@Override
	public Choice getChoice(Choice choice) {
		return choiceDao.getChoice(choice);
	}



	@Override
	public Choice updateChoiceInfo(Choice choice) {
		return choiceDao.updateChoiceInfo(choice);
	}



	@Override
	public boolean addChoice(Choice choice) {
		return choiceDao.addChoice(choice);
	}



	@Override
	public PageBean<Choice> findChoiceByPage(int pageCode, int pageSize) {
		return choiceDao.findChoiceByPage(pageCode,pageSize);
	}



	@Override
	public Choice getChoiceById(Choice choice) {
		return choiceDao.getChoiceById(choice);
	}



	@Override
	public int deleteChoice(Choice choice) {
		//删除病人需要注意的点：如果该病人有尚未答卷的问卷或者尚未设置的延期,则不能删除
		//得到该病人的分发集合
		Choice choiceById = choiceDao.getChoiceById(choice);
		Set<DeliveryInfo> deliveryInfos = null;
		for (DeliveryInfo deliveryInfo : deliveryInfos) {
			if(!(deliveryInfo.getState()==2 || deliveryInfo.getState()==5)){
				return -1;//有尚未答卷的问卷
			}
			//得到该分发记录的提醒信息
			ForfeitInfo forfeitInfo = new ForfeitInfo();
			forfeitInfo.setDeliveryId(deliveryInfo.getDeliveryId());
			ForfeitInfo forfeitInfoById = forfeitDao.getForfeitInfoById(forfeitInfo);
			if(forfeitInfoById!=null){
				if(forfeitInfoById.getIsPay()==0){
					return -2;//尚未设置的延期
				}
			}
		}
		boolean deleteChoice = choiceDao.deleteChoice(choice);
		if(deleteChoice){
			return 1;
		}
		return 0;
	}



	@Override
	public PageBean<Choice> queryChoice(Choice choice,int pageCode, int pageSize) {
		return choiceDao.queryChoice(choice,pageCode,pageSize);
	}



	@Override
	public Choice getChoiceByopenID(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceByopenID(choice);
	}



	@Override
	public Choice getChoiceByOpenID(Choice choice) {
		// TODO Auto-generated method stub
		return choiceDao.getChoiceByopenID(choice);
	}



	@Override
	public JSONObject batchAddChoice(String fileName,Doctor doctor) {
		List<Choice> choices = new ArrayList<Choice>();
		List<Choice> failChoices = new ArrayList<Choice>();
		String str[] = new String[]{"用户名码","姓名","病人类型","邮箱","联系方式"};
		// TODO Auto-generated method stub
		String realPath = ServletActionContext.getServletContext().getRealPath(fileName);
		 //创建workbook
        try {
			Workbook workbook = Workbook.getWorkbook(new File(realPath));
			//获取第一个工作表sheet
            Sheet sheet = workbook.getSheet(0);
            
            if(sheet.getColumns()!=5  ){
            	JSONObject jsonObject = new JSONObject();
            	jsonObject.put("error","请下载模板,填入数据上传" );
            	jsonObject.put("state","-1" );
            	return jsonObject;
            }else{
            	  //获取数据
              
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell cell = sheet.getCell(j,0);
                    if(!cell.getContents().equals(str[j])){
                    	JSONObject jsonObject = new JSONObject();
                    	jsonObject.put("error","请下载模板,填入数据上传" );
                    	jsonObject.put("state","-1" );
                    	return jsonObject;
                    }
                }
                
            }
            
               
            //接下来就是真正的模板文件，需要解析它

            //获取数据
            for (int i = 1; i < sheet.getRows(); i++) {
              

           	
                String id = sheet.getCell(0,i).getContents().trim();
                String name = sheet.getCell(1,i).getContents().trim();
                String type = sheet.getCell(2,i).getContents().trim();
                String email = sheet.getCell(3,i).getContents().trim();
                String phone = sheet.getCell(4,i).getContents().trim();
                
                if("".equals(id) && "".equals(name) && "".equals(type) && "".equals(email) && "".equals(phone)){
                	//说明这条数据是空的
                	continue;
                }

            }
            workbook.close();
            int success = choiceDao.batchAddChoice(choices,failChoices);
            
            JSONObject jsonObject = new JSONObject();
            if(failChoices.size() != 0){
            	 //把不成功的导出成excel文件
                String exportExcel = exportExcel(failChoices,"failChoices.xls");
                jsonObject.put("state", "2");
                jsonObject.put("message","成功" + success + "条,失败" + failChoices.size() + "条");
                jsonObject.put("failPath", "doctor/FileDownloadAction.action?fileName=" + exportExcel);
                return jsonObject;
            }else{
            	 jsonObject.put("state", "1");
                 jsonObject.put("message","成功" + success + "条,失败" + failChoices.size() + "条");
                 return jsonObject;
            }
            
            
            
            
		} catch (Throwable e) { 
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    	
	}
	
	/**
	 * 导出excel文件
	 * @param failChoices 失败的数据
	 * @return 返回文件路径
	 */
	public String exportExcel(List<Choice> failChoices,String name){
		//用数组存储表头
        String[] title = {"用户名码","姓名","病人类型","邮箱","联系方式"};
        String path = ServletActionContext.getServletContext().getRealPath("/download");
        String fileName = +System.currentTimeMillis()+"_" + name;
        //创建Excel文件
        File file = new File(path, fileName);
        try {
            file.createNewFile();
            //创建工作簿
            WritableWorkbook workbook = Workbook.createWorkbook(file);
            WritableSheet sheet = workbook.createSheet("sheet1", 0);

            Label label = null;

            //第一行设置列名
            for(int i = 0; i<title.length; i++){
                label = new Label(i, 0, title[i]);
                sheet.addCell(label);
            }
            //写入数据
            workbook.write();

            workbook.close();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		return fileName;
	}



	@Override
	public String exportChoice() {
		List<Choice> findAllChoices = choiceDao.findAllChoices();
		String exportChoiceExcel = exportExcel(findAllChoices,"allChoices.xls");
		return "doctor/FileDownloadAction.action?fileName=" + exportChoiceExcel;
	}



}
