package com.hospital.service.impl;

import com.hospital.dao.ForfeitDao;
import com.hospital.dao.AnswerDao;
import com.hospital.domain.*;
import com.hospital.service.AnswerService;
import com.hospital.util.CheckUtils;
import com.hospital.util.Md5Utils;
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
import java.util.Date;
import java.util.List;
import java.util.Set;


public class AnswerServiceImpl implements AnswerService{

	private AnswerDao answerDao;

	private ForfeitDao forfeitDao;
	
	

	public void setForfeitDao(ForfeitDao forfeitDao) {
		this.forfeitDao = forfeitDao;
	}


	public void setAnswerDao(AnswerDao answerDao) {
		this.answerDao = answerDao;
	}



	@Override
	public Answer getAnswer(Answer answer) {
		return answerDao.getAnswer(answer);
	}



	@Override
	public Answer updateAnswerInfo(Answer answer) {
		return answerDao.updateAnswerInfo(answer);
	}



	@Override
	public boolean addAnswer(Answer answer) {
		return answerDao.addAnswer(answer);
	}



	@Override
	public PageBean<Answer> findAnswerByPage(int pageCode, int pageSize) {
		return answerDao.findAnswerByPage(pageCode,pageSize);
	}



	@Override
	public Answer getAnswerById(Answer answer) {
		return answerDao.getAnswerById(answer);
	}



	@Override
	public int deleteAnswer(Answer answer) {
		//删除病人需要注意的点：如果该病人有尚未答卷的问卷或者尚未设置的延期,则不能删除
		//得到该病人的分发集合
		Answer answerById = answerDao.getAnswerById(answer);
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
		boolean deleteAnswer = answerDao.deleteAnswer(answer);
		if(deleteAnswer){
			return 1;
		}
		return 0;
	}



	@Override
	public PageBean<Answer> queryAnswer(Answer answer,int pageCode, int pageSize) {
		return answerDao.queryAnswer(answer,pageCode,pageSize);
	}



	@Override
	public Answer getAnswerByopenID(Answer answer) {
		// TODO Auto-generated method stub
		return answerDao.getAnswerByopenID(answer);
	}



	@Override
	public Answer getAnswerByOpenID(Answer answer) {
		// TODO Auto-generated method stub
		return answerDao.getAnswerByopenID(answer);
	}



	@Override
	public JSONObject batchAddAnswer(String fileName,Doctor doctor) {
		List<Answer> answers = new ArrayList<Answer>();
		List<Answer> failAnswers = new ArrayList<Answer>();
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
            int success = answerDao.batchAddAnswer(answers,failAnswers);
            
            JSONObject jsonObject = new JSONObject();
            if(failAnswers.size() != 0){
            	 //把不成功的导出成excel文件
                String exportExcel = exportExcel(failAnswers,"failAnswers.xls");
                jsonObject.put("state", "2");
                jsonObject.put("message","成功" + success + "条,失败" + failAnswers.size() + "条");
                jsonObject.put("failPath", "doctor/FileDownloadAction.action?fileName=" + exportExcel);
                return jsonObject;
            }else{
            	 jsonObject.put("state", "1");
                 jsonObject.put("message","成功" + success + "条,失败" + failAnswers.size() + "条");
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
	 * @param failAnswers 失败的数据
	 * @return 返回文件路径
	 */
	public String exportExcel(List<Answer> failAnswers,String name){
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
	public String exportAnswer() {
		List<Answer> findAllAnswers = answerDao.findAllAnswers();
		String exportAnswerExcel = exportExcel(findAllAnswers,"allAnswers.xls");
		return "doctor/FileDownloadAction.action?fileName=" + exportAnswerExcel;
	}



}
