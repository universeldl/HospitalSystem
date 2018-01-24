$(function () {


    $('#updateSurvey').click(function () {


        if (!validUpdateSurvey()) {
            return;
        }

        var postdata = "surveyName=" + $.trim($("#updateSurveyName").val()) + "&surveyTypeId=" + $.trim($("#updateSurveyType").val())
            + "&frequency=" + $.trim($("#updateFrequency").val()) + "&author=" + $.trim($("#updateAuthor").val())
            + "&department=" + $.trim($("#updateDepartment").val()) + "&description=" + $.trim($("#updateDescription").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_updateSurvey.action',
                params: postdata,
                callback: function (data) {
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
    $("#updateSurveyType option[value!=-1]").remove();//移除先前的选项
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
                            $("#updateSurveyId").val(data.surveyId);
                            $("#updateSurveyName").val(data.surveyName);
                            $("#updateSurveyType").val(data.surveyType.typeId);
                            $("#updateAuthor").val(data.author);
                            $("#updateDepartment").val(data.department);
                            $("#updateDescription").val(data.description);

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

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


