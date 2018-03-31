let surveyId = 0;

$(function () {

    $('#updateSurvey').click(function () {

        if (!validUpdateSurvey()) {
            return;
        }

        var postdata = "surveyId=" + surveyId
            + "&surveyName=" + replaceSpectialChar($.trim($("#updateSurveyName").val()))
            + "&surveyTypeId=" + $.trim($("#updateSurveyType").val())
            + "&frequency=" + $.trim($("#updateFrequency").val())
            + "&author=" + replaceSpectialChar($.trim($("#updateAuthor").val()))
            + "&times=" + $.trim($("#updateTimes").val())
            + "&department=" + replaceSpectialChar($.trim($("#updateDepartment").val()))
            + "&bday=" + $.trim($("#updateBday").val())
            + "&sendOnRegister=" + $.trim($("#updateSendOnRegister").val())
            + "&description=" + replaceSpectialChar($.trim($("#updateDescription").val()));
        //alert(postdata);
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_updateSurvey.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("修改成功");

                    } else {
                        $("#updateinfo").modal("hide");//关闭模糊框
                        showInfo("修改失败");
                    }

                }
            }
        );


    });


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function updateSurvey(id) {
    surveyId = id;
    $("#updateSurveyType option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            url: "doctor/surveyManageAction_getAllSurveyTypes.action",
            type: "json",
            callback: function (data) {
                // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].typeId;//设置op的实际值为当前的问卷分类编号
                    var textNode = document.createTextNode(data[index].typeName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("updateSurveyType").appendChild(op);
                }

                ajax(
                    {
                        method: 'POST',
                        url: 'doctor/surveyManageAction_getSurvey.action',
                        params: "surveyId=" + id,
                        type: "json",
                        callback: function (data) {
                            $('#loading').hide();
                            $("#updateSurveyId").val(data.surveyId);
                            $("#updateSurveyName").val(data.surveyName);
                            $("#updateSurveyType").val(data.surveyType.typeId);
                            $("#updateAuthor").val(data.author);
                            $("#updateDepartment").val(data.department);
                            $("#updateDescription").val(data.description);
                            $("#updateTimes").val(data.times);
                            $("#updateBday").val(data.bday);
                            $("#updateFrequency").val(data.frequency);

                        }
                    }
                );
            }
        }
    );


}


function validUpdateSurvey() {
    var flag = true;

    var surveyName = $.trim($("#updateSurveyName").val());
    if (surveyName == "") {
        $('#updateSurveyName').parent().addClass("has-error");
        $('#updateSurveyName').next().text("请输入问卷名称");
        $("#updateSurveyName").next().show();
        flag = false;
    } else {
        $('#updateSurveyName').parent().removeClass("has-error");
        $('#updateSurveyName').next().text("");
        $("#updateSurveyName").next().hide();
    }

    var surveySendOnRegister = $.trim($("#updateSendOnRegister").val());
    if (surveySendOnRegister == -1) {
        $('#updateSendOnRegister').parent().addClass("has-error");
        $('#updateSendOnRegister').next().text("请选择是否用户注册时立即发送问卷");
        $("#updateSendOnRegister").next().show();
        flag = false;
    } else {
        $('#updateSendOnRegister').parent().removeClass("has-error");
        $('#updateSendOnRegister').next().text("");
        $("#updateSendOnRegister").next().hide();
    }

    var surveyFreq = $.trim($("#updateFrequency").val());
    if (surveyFreq == -1) {
        $('#updateFrequency').parent().addClass("has-error");
        $('#updateFrequency').next().text("请选择分发周期");
        $("#updateFrequency").next().show();
        flag = false;
    } else {
        $('#updateFrequency').parent().removeClass("has-error");
        $('#updateFrequency').next().text("");
        $("#updateFrequency").next().hide();
    }

    var surveyType = $.trim($("#updateSurveyType").val());
    if (surveyType == -1) {
        $('#updateSurveyType').parent().addClass("has-error");
        $('#updateSurveyType').next().text("请选择问卷分类");
        $("#updateSurveyType").next().show();
        flag = false;
    } else {
        $('#updateSurveyType').parent().removeClass("has-error");
        $('#updateSurveyType').next().text("");
        $("#updateSurveyType").next().hide();
    }

    var author = $.trim($("#updateAuthor").val());
    if (author == "") {
        $('#updateAuthor').parent().addClass("has-error");
        $('#updateAuthor').next().text("请输入作者名称");
        $("#updateAuthor").next().show();
        flag = false;
    } else {
        $('#updateAuthor').parent().removeClass("has-error");
        $('#updateAuthor').next().text("");
        $("#updateAuthor").next().hide();
    }


    var department = $.trim($("#updateDepartment").val());
    if (department == "") {
        $('#updateDepartment').parent().addClass("has-error");
        $('#updateDepartment').next().text("请输入科室名称");
        $("#updateDepartment").next().show();
        flag = false;
    } else {
        $('#updateDepartment').parent().removeClass("has-error");
        $('#updateDepartment').next().text("");
        $("#updateDepartment").next().hide();
    }

    var times = $.trim($("#updateTimes").val());
    if (times == "") {
        $('#updateTimes').parent().addClass("has-error");
        $('#updateTimes').next().text("请输入随访次数");
        $("#updateTimes").next().show();
        flag = false;
    } else if (times <= 0 || times != parseInt(times)) {
        $('#updateTimes').parent().addClass("has-error");
        $('#updateTimes').next().text("随访次数必须为正整数");
        $("#updateTimes").next().show();
        flag = false;
    } else {
        $('#updateTimes').parent().removeClass("has-error");
        $('#updateTimes').next().text("");
        $("#updateTimes").next().hide();
    }

    var bday = $.trim($("#updateBday").val());
    if (bday == "") {
        $('#updateBday').parent().addClass("has-error");
        $('#updateBday').next().text("请输入允许答卷最大天数");
        $("#updateBday").next().show();
        flag = false;
    } else if (bday <= 0 || bday != parseInt(bday)) {
        $('#updateBday').parent().addClass("has-error");
        $('#updateBday').next().text("允许答卷最大天数必须为正整数");
        $("#updateBday").next().show();
        flag = false;
    } else {
        $('#updateBday').parent().removeClass("has-error");
        $('#updateBday').next().text("");
        $("#updateBday").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


