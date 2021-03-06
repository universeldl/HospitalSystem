$(function () {

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getQuestionInfo(id) {

    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/surveyManageAction_getQuestion.action',
            params: "questionId=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#findQuestionContent").val(data.questionContent);

                if (data.textChoice == 1)
                    $("#findTextChoice").val("是");
                else if (data.textChoice == 0)
                    $("#findTextChoice").val("否");

                if (data.startAge == -1)
                    $("#findStartAge").val("N/A");
                else
                    $("#findStartAge").val(data.startAge);

                if (data.endAge == -1)
                    $("#findEndAge").val("N/A");
                else
                    $("#findEndAge").val(data.endAge);

                if (data.questionType == 1)
                    $("#findQuestionType").val("多选题");
                else if (data.questionType == 2)
                    $("#findQuestionType").val("单选题");
                else if (data.questionType == 3)
                    $("#findQuestionType").val("问答题");
                else if (data.questionType == 4)
                    $("#findQuestionType").val("问答题(日期)");

                if (data.questionType == 3) {
                    $("#checkTextChoice").outerHTML = '';
                    $("#checkTextChoice").remove();
                }

                var str = '';
                for (let index in data.choices) {
                    str += '<div class="form-group">'
                        + '<label for="firstname" class="col-sm-3 control-label">选项:</label>'
                        + '<div class="col-sm-5">'
                        + '<input type="text" class="form-control" id="findOptionA" value="' + data.choices[index].choiceContent + '" readonly="readonly">'
                        + '<label class="control-label" for="findOptionA" style="display:none;"></label>'
                        + '</div>'
                        + '<div class="col-sm-2">'
                        + '<input type="text" class="form-control" id="scoreA" value="' + data.choices[index].score + ' 分" readonly="readonly">'
                        + '<label class="control-label" for="scoreA" style="display:none;"></label>'
                        + '</div>'
                        + '</div>';
                }
                document.getElementById("checkChoicesDiv").innerHTML = str;
            }
        }
    );

}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


