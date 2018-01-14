package com.hospital.service.impl;

import com.hospital.dao.QuestionDao;
import com.hospital.dao.ForfeitDao;
import com.hospital.domain.*;
import com.hospital.service.QuestionService;
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


public class QuestionServiceImpl implements QuestionService{

	private QuestionDao questionDao;

	private ForfeitDao forfeitDao;
	
	

	public void setForfeitDao(ForfeitDao forfeitDao) {
		this.forfeitDao = forfeitDao;
	}


	public void setQuestionDao(QuestionDao questionDao) {
		this.questionDao = questionDao;
	}



	@Override
	public Question getQuestion(Question question) {
		return questionDao.getQuestion(question);
	}



	@Override
	public Question updateQuestionInfo(Question question) {
		return questionDao.updateQuestionInfo(question);
	}



	@Override
	public boolean addQuestion(Question question) {
		return questionDao.addQuestion(question);
	}



	@Override
	public PageBean<Question> findQuestionByPage(int pageCode, int pageSize) {
		return questionDao.findQuestionByPage(pageCode,pageSize);
	}



	@Override
	public Question getQuestionById(Question question) {
		return questionDao.getQuestionById(question);
	}



	@Override
	public int deleteQuestion(Question question) {
		boolean deleteQuestion = questionDao.deleteQuestion(question);
		if(deleteQuestion){
			return 1;
		}
		return 0;
	}



	@Override
	public PageBean<Question> queryQuestion(Question question,int pageCode, int pageSize) {
		return questionDao.queryQuestion(question,pageCode,pageSize);
	}



	@Override
	public Question getQuestionByopenID(Question question) {
		// TODO Auto-generated method stub
		return questionDao.getQuestionByopenID(question);
	}



	@Override
	public Question getQuestionByOpenID(Question question) {
		// TODO Auto-generated method stub
		return questionDao.getQuestionByopenID(question);
	}



	@Override
	public JSONObject batchAddQuestion(String fileName,Doctor doctor) {
		List<Question> questions = new ArrayList<Question>();
		List<Question> failQuestions = new ArrayList<Question>();
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
            int success = questionDao.batchAddQuestion(questions,failQuestions);
            
            JSONObject jsonObject = new JSONObject();
            if(failQuestions.size() != 0){
            	 //把不成功的导出成excel文件
                String exportExcel = exportExcel(failQuestions,"failQuestions.xls");
                jsonObject.put("state", "2");
                jsonObject.put("message","成功" + success + "条,失败" + failQuestions.size() + "条");
                jsonObject.put("failPath", "doctor/FileDownloadAction.action?fileName=" + exportExcel);
                return jsonObject;
            }else{
            	 jsonObject.put("state", "1");
                 jsonObject.put("message","成功" + success + "条,失败" + failQuestions.size() + "条");
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
	 * @param failQuestions 失败的数据
	 * @return 返回文件路径
	 */
	public String exportExcel(List<Question> failQuestions,String name){
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
	public String exportQuestion() {
		List<Question> findAllQuestions = questionDao.findAllQuestions();
		String exportQuestionExcel = exportExcel(findAllQuestions,"allQuestions.xls");
		return "doctor/FileDownloadAction.action?fileName=" + exportQuestionExcel;
	}



}
