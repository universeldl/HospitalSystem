window.onload = new function () {
    $("#surveyList option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            method: 'GET',
            url: "doctor/deliveryManageAction_findAllSurveys.action",
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].surveyId;//设置op的实际值为当前的问卷分类编号
                    var textNode = document.createTextNode(data[index].surveyName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("surveyList").appendChild(op);
                }
            }
        }
    );
};