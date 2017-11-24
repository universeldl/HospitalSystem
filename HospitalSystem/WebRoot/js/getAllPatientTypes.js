window.onload = new function(){
	ajax(
		  {
	    		url:"doctor/patientTypeManageAction_getAllPatientTypes.action",
	    		type:"json",
	    		callback:function(data) {
					// 循环遍历每个病人分类，每个名称生成一个option对象，添加到<select>中
					for(var index in data) {
						var op = document.createElement("option");//创建一个指名名称元素
						op.value = data[index].patientTypeId;//设置op的实际值为当前的病人分类编号
						var textNode = document.createTextNode(data[index].patientTypeName);//创建文本节点
						op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值
						
						document.getElementById("patientType").appendChild(op);
					}
				}
		   }
	);
};