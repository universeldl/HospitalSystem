$(function () {


    $('#addSurvey').click(function () {


        if (!validAddSurvey()) {
            return;
        }

        var postdata = "surveyName=" + replaceSpectialChar($.trim($("#addSurveyName").val()))
            + "&author=" + replaceSpectialChar($.trim($("#addAuthor").val()))
            + "&frequency=" + $.trim($("#addFrequency").val())
            + "&department=" + replaceSpectialChar($.trim($("#addDepartment").val()))
            + "&description=" + replaceSpectialChar($.trim($("#addDescription").val()))
            + "&sendOnRegister=" + $.trim($("#addSendOnRegister").val())
            + "&bday=" + $.trim($("#addBday").val())
            + "&times=" + $.trim($("#addTimes").val()) + "&surveyTypeId=" + $.trim($("#addSurveyType").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_addSurvey.action',
                params: postdata,
                callback: function (data) {
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");

                    } else {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加失败");
                    }

                }
            }
        );


    });


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


    $('#btn_add').click(function () {
        $("#addSurveyType option[value!=-1]").remove();//移除先前的选项
        $('#loading').show();
        ajax(
            {
                url: "doctor/surveyManageAction_getAllSurveyTypes.action",
                type: "json",
                callback: function (data) {
                    $('#loading').hide();
                    // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                    for (var index in data) {
                        var op = document.createElement("option");//创建一个指名名称元素
                        op.value = data[index].typeId;//设置op的实际值为当前的问卷分类编号
                        var textNode = document.createTextNode(data[index].typeName);//创建文本节点
                        op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                        document.getElementById("addSurveyType").appendChild(op);
                    }
                }
            }
        );
    });


});


function validAddSurvey() {
    var flag = true;

    var surveyName = $.trim($("#addSurveyName").val());
    if (surveyName == "") {
        $('#addSurveyName').parent().addClass("has-error");
        $('#addSurveyName').next().text("请输入问卷名称");
        $("#addSurveyName").next().show();
        flag = false;
    } else {
        $('#addSurveyName').parent().removeClass("has-error");
        $('#addSurveyName').next().text("");
        $("#addSurveyName").next().hide();
    }

    var surveySendOnRegister = $.trim($("#addSendOnRegister").val());
    if (surveySendOnRegister == -1) {
        $('#addSendOnRegister').parent().addClass("has-error");
        $('#addSendOnRegister').next().text("请选择是否用户注册时立即发送问卷");
        $("#addSendOnRegister").next().show();
        flag = false;
    } else {
        $('#addSendOnRegister').parent().removeClass("has-error");
        $('#addSendOnRegister').next().text("");
        $("#addSendOnRegister").next().hide();
    }

    var surveyFreq = $.trim($("#addFrequency").val());
    if (surveyFreq == -1) {
        $('#addFrequency').parent().addClass("has-error");
        $('#addFrequency').next().text("请选择分发周期");
        $("#addFrequency").next().show();
        flag = false;
    } else {
        $('#addFrequency').parent().removeClass("has-error");
        $('#addFrequency').next().text("");
        $("#addFrequency").next().hide();
    }

    var surveyType = $.trim($("#addSurveyType").val());
    if (surveyType == -1) {
        $('#addSurveyType').parent().addClass("has-error");
        $('#addSurveyType').next().text("请选择问卷分类");
        $("#addSurveyType").next().show();
        flag = false;
    } else {
        $('#addSurveyType').parent().removeClass("has-error");
        $('#addSurveyType').next().text("");
        $("#addSurveyType").next().hide();
    }

    var author = $.trim($("#addAuthor").val());
    if (author == "") {
        $('#addAuthor').parent().addClass("has-error");
        $('#addAuthor').next().text("请输入作者名称");
        $("#addAuthor").next().show();
        flag = false;
    } else {
        $('#addAuthor').parent().removeClass("has-error");
        $('#addAuthor').next().text("");
        $("#addAuthor").next().hide();
    }


    var department = $.trim($("#addDepartment").val());
    if (department == "") {
        $('#addDepartment').parent().addClass("has-error");
        $('#addDepartment').next().text("请输入科室名称");
        $("#addDepartment").next().show();
        flag = false;
    } else {
        $('#addDepartment').parent().removeClass("has-error");
        $('#addDepartment').next().text("");
        $("#addDepartment").next().hide();
    }

    var bday = $.trim($("#addBday").val());
    if (bday == "") {
        $('#addBday').parent().addClass("has-error");
        $('#addBday').next().text("请输入允许答卷最大天数");
        $("#addBday").next().show();
        flag = false;
    } else if (bday <= 0 || bday != parseInt(bday)) {
        $('#addBday').parent().addClass("has-error");
        $('#addBday').next().text("允许答卷最大天数必须为正整数");
        $("#addBday").next().show();
        flag = false;
    } else {
        $('#addBday').parent().removeClass("has-error");
        $('#addBday').next().text("");
        $("#addBday").next().hide();
    }

    var times = $.trim($("#addTimes").val());
    if (times == "") {
        $('#addTimes').parent().addClass("has-error");
        $('#addTimes').next().text("请输入随访次数");
        $("#addTimes").next().show();
        flag = false;
    } else if (times <= 0 || times != parseInt(times)) {
        $('#addTimes').parent().addClass("has-error");
        $('#addTimes').next().text("随访次数必须为正整数");
        $("#addTimes").next().show();
        flag = false;
    } else {
        $('#addTimes').parent().removeClass("has-error");
        $('#addTimes').next().text("");
        $("#addTimes").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


