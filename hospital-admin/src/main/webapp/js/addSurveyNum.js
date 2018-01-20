function addSurveyNum(id) {

    $("#addSurveyNumId").val(id);
}


$(function () {


    $('#add_SurveyNum').click(function () {


        if (!validAddSurveyNum()) {
            return;
        }
        var postdata = "surveyId=" + $.trim($("#addSurveyNumId").val()) + "&num=" + $.trim($("#addSurveyNum").val());
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_addSurveyNum.action',
                params: postdata,
                callback: function (data) {
                    if (data == 1) {
                        $("#addNumModal").modal("hide");//关闭模糊框
                        showInfo("新增成功");

                    } else {
                        $("#addNumModal").modal("hide");//关闭模糊框
                        showInfo("新增失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function validAddSurveyNum() {
    var flag = true;

    var num = $.trim($("#addSurveyNum").val());
    if (num == "") {
        $('#addSurveyNum').parent().addClass("has-error");
        $('#addSurveyNum').next().text("请输入新增问卷数量");
        $("#addSurveyNum").next().show();
        flag = false;
    } else if (num <= 0 || num != parseInt(num)) {
        $('#addSurveyNum').parent().addClass("has-error");
        $('#addSurveyNum').next().text("问卷数量必须为正整数");
        $("#addSurveyNum").next().show();
        flag = false;
    } else {
        $('#addSurveyNum').parent().removeClass("has-error");
        $('#addSurveyNum').next().text("");
        $("#addSurveyNum").next().hide();
    }


    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}
