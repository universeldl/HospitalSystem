/**
 * ajax提交修改随访计划的信息
 * @param {Object} '#updatePlan'
 */

let planId = 1;

$(function () {


    $('#update_Plan').click(function () {


        if (!validUpdatePlan()) {
            return;
        }

        var postdata = "active=1&planId=" + planId
            + "&patientType=" + $.trim($("#updatePatientType").val())
            + "&oldPatientOnly=" + $.trim($("#updateOldPatientOnly").val())
            + "&sex=" + $.trim($("#updateSex").val())
            + "&" + $("#updateForm").serialize();

        ajax(
            {
                method: 'POST',
                url: 'doctor/planManageAction_updatePlan.action',
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


/**
 * 显示修改前的数据在输入框中
 * @param {Object} id
 */
function updatePlan(id) {
    planId = id;
    $("#updatePatientType option[value!=-1]").remove();//移除先前的选项
    $("#updateSurveys option[value!=-1]").remove();//移除先前的选项

    ajax(
        {
            url: "doctor/patientTypeManageAction_getAllPatientTypes.action",
            type: "json",
            callback: function (data) {
                // 循环遍历每个病人分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].patientTypeId;//设置op的实际值为当前的病人分类编号
                    var textNode = document.createTextNode(data[index].patientTypeName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("updatePatientType").appendChild(op);
                }
                ajax(
                    {
                        url: "doctor/surveyManageAction_findAllSurveys.action",
                        type: "json",
                        callback: function (data) {
                            // 循环遍历每个问卷，每个名称生成一个option对象，添加到<select>中
                            for (var index in data) {
                                var op = document.createElement("option");//创建一个指名名称元素
                                op.value = data[index].surveyId;//设置op的实际值为当前的问卷编号
                                var textNode = document.createTextNode(data[index].surveyName);//创建文本节点
                                op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                                document.getElementById("updateSurveys").appendChild(op);
                            }
                        }
                    }
                );
                ajax(
                    {
                        method: 'POST',
                        url: 'doctor/planManageAction_getPlan.action',
                        params: "planId=" + id,
                        type: "json",
                        callback: function (data) {
                            $("#updateOldPatientOnly").val(data.oldPatientOnly);
                            $("#updatePatientType").val(data.patientType.patientTypeId);
                        }
                    }
                );
            }
        }
    );
}


function validUpdatePlan() {
    var flag = true;

    var oldPatientOnly = $.trim($("updateOldPatientOnly").val());
    if (oldPatientOnly == -1) {
        $('#updateOldPatientOnly').parent().addClass("has-error");
        $('#updateOldPatientOnly').next().text("请选择是否仅限既往病例");
        $("#updateOldPatientOnly").next().show();
        flag = false;
    } else {
        $('#updateOldPatientOnly').parent().removeClass("has-error");
        $('#updateOldPatientOnly').next().text("");
        $("#updateOldPatientOnly").next().hide();
    }

    var sex = $.trim($("#updateSex").val());
    if (sex == -1) {
        $('#updateSex').parent().addClass("has-error");
        $('#updateSex').next().text("请选择病人性别");
        $("#updateSex").next().show();
        flag = false;
    } else {
        $('#updateSex').parent().removeClass("has-error");
        $('#updateSex').next().text("");
        $("#updateSex").next().hide();
    }

    var patientType = $.trim($("#updatepatientType").val());
    if (patientType == -1) {
        $('#updatepatientType').parent().addClass("has-error");
        $('#updatepatientType').next().text("请选择病人类型");
        $("#updatepatientType").next().show();
        flag = false;
    } else {
        $('#updatepatientType').parent().removeClass("has-error");
        $('#updatepatientType').next().text("");
        $("#updatepatientType").next().hide();
    }

    var numSurvey = 0;
    $("#updateSurveys option:selected").each(function(){
        numSurvey++;
    });

    if (numSurvey == 0) {
        $("#updateSurveys").parent().addClass("has-error");
        $("#updateSurveys").next().text("至少需要选择一份问卷");
        $("#updateSurveys").next().show();
        flag = false;
    } else {
        $("#updateSurveys").parent().removeClass("has-error");
        $("#updateSurveys").next().text("");
        $("#updateSurveys").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

