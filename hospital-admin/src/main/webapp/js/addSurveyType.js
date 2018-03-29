/**
 * ajax提交添加问卷分类的信息
 * @param {Object} '#addSurveyType'
 */
$(function () {


    $('#addSurveyType').click(function () {


        if (!validAddSurveyType()) {
            return;
        }

        var postdata = "typeName=" + replaceSpectialChar($.trim($("#addSurveyTypeName").val()));
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyTypeManageAction_addSurveyType.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");

                    } else if (data == -1) {
                        $("#addModal").modal("hide");//关闭模糊框
                        showInfo("该问卷分类存在");
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


function validAddSurveyType() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var surveyType = $.trim($("#addSurveyTypeName").val());
    if (surveyType == "") {
        $('#addSurveyTypeName').parent().addClass("has-error");
        $('#addSurveyTypeName').next().text("请输入问卷分类名称");
        $("#addSurveyTypeName").next().show();
        flag = false;
    } else if (!reg.test(surveyType)) {
        $('#addName').parent().addClass("has-error");
        $('#addName').next().text("问卷分类名称必须为中文");
        $("#addName").next().show();
        flag = false;
    } else {
        $('#addSurveyTypeName').parent().removeClass("has-error");
        $('#addSurveyTypeName').next().text("");
        $("#addSurveyTypeName").next().hide();
    }


    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


