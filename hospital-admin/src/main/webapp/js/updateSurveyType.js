/**
 * ajax提交修改问卷分类的信息
 * @param {Object} '#updateSurveyType'
 */
$(function () {


    $('#updateSurveyType').click(function () {


        if (!validUpdateSurveyType()) {
            return;
        }

        var postdata = "id=" + $.trim($("#updateSurveyTypeId").val()) + "&typeName=" + $.trim($("#updateSurveyTypeName").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyTypeManageAction_updateSurveyType.action',
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


/**
 * 获取需要修改问卷分类信息
 * @param {Object} id 需要修改的问卷分类id
 */
function updateSurveyType(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/surveyTypeManageAction_getSurveyType.action',
            params: "id=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#updateSurveyTypeId").val(data.typeId);
                $("#updateSurveyTypeName").val(data.typeName);

            }
        }
    );


}


function validUpdateSurveyType() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var surveyType = $.trim($("#updateSurveyTypeName").val());
    if (surveyType == "") {
        $('#updateSurveyTypeName').parent().addClass("has-error");
        $('#updateSurveyTypeName').next().text("请输入问卷分类名称");
        $("#updateSurveyTypeName").next().show();
        flag = false;
    } else if (!reg.test(surveyType)) {
        $('#updateSurveyTypeName').parent().addClass("has-error");
        $('#updateSurveyTypeName').next().text("问卷分类名称必须为中文");
        $("#updateSurveyTypeName").next().show();
        flag = false;
    } else {
        $('#updateSurveyTypeName').parent().removeClass("has-error");
        $('#updateSurveyTypeName').next().text("");
        $("#updateSurveyTypeName").next().hide();
    }


    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


