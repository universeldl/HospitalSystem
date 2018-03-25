let patientId = 0;

$(function () {

    $('#addRetrieveSubmit').click(function () {

        if (!validAddRetrieve()) {
            return;
        }

        var postdata = "surveyId=" + $.trim($("#addRetrieveDelivery").val())
                        + "&sendDate=" + $.trim($("#addDeliverySendDate").val())
                        + "&patientId=" + patientId
                        + "&retrieveDate=" + $.trim($("#addDeliveryRetrieveDate").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/retrieveManageAction_addRetrieveInfoWithoutDeliveryInfo.action',
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

});

function addRetrieveFun(id) {
    patientId = id;
    $("#addRetrieveDelivery option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: "doctor/deliveryManageAction_getUnansweredDeliveryInfos.action",
            params: "patientId=" + patientId,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].deliveryId;//设置op的实际值为当前的问卷分类编号
                    var textNode = document.createTextNode(data[index].surveyName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("addRetrieveDelivery").appendChild(op);
                }
            }
        }
    );
}


function addRetrieveWithoutDeliveryInfo(id) {
    patientId = id;
    $("#addRetrieveDelivery option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: "doctor/deliveryManageAction_findAllSurveys.action",
            params: "patientId=" + patientId,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                // 循环遍历每个问卷分类，每个名称生成一个option对象，添加到<select>中
                for (var index in data) {
                    var op = document.createElement("option");//创建一个指名名称元素
                    op.value = data[index].surveyId;//设置op的实际值为当前的问卷分类编号
                    var textNode = document.createTextNode(data[index].surveyName);//创建文本节点
                    op.appendChild(textNode);//把文本子节点添加到op元素中，指定其显示值

                    document.getElementById("addRetrieveDelivery").appendChild(op);
                }
            }
        }
    );
}


function addRetrieveForPatient(id) {
    let deliveryId = id;
    $("#addRetrieveDelivery option[value!=-1]").remove();//移除先前的选项
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_addRetrieveInfoForPatient.action',
            params: "deliveryId=" + deliveryId,
            callback: function (data) {
                $('#loading').hide();
                if (data == 1) {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("添加成功");
                } else if (data == -1) {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("添加失败，该问卷已有答卷");
                } else {
                    $("#addModal").modal("hide");//关闭模糊框
                    showInfo("添加失败");
                }

            }
        }
    );
}


function validAddRetrieve() {
    var flag = true;

    var deli = $.trim($("#addRetrieveDelivery").val());
    if (deli == -1) {
        $('#addRetrieveDelivery').parent().addClass("has-error");
        $('#addRetrieveDelivery').next().text("请选择问卷");
        $("#addRetrieveDelivery").next().show();
        flag = false;
    } else {
        $('#addRetrieveDelivery').parent().removeClass("has-error");
        $('#addRetrieveDelivery').next().text("");
        $("#addRetrieveDelivery").next().hide();
    }

    var sendDate = new Date(($("#addDeliverySendDate").val()).replace(/-/g,"/"));
    var curDate = new Date();
    if (sendDate >= curDate) {
        alert("请填写正确的问卷发送日期");
        return false;
    }

    var retrieveDate = new Date(($("#addDeliveryRetrieveDate").val()).replace(/-/g,"/"));
    if (retrieveDate >= curDate) {
        alert("请填写正确的答卷日期");
        return false;
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


