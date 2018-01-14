$(function () {

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getQuestionInfo(id) {

    ajax(
        {
            method: 'POST',
            url: 'doctor/surveyManageAction_getQuestion.action',
            params: "questionId=" + id,
            type: "json",
            callback: function (data) {
                $("#findQuestionContent").val(data.questionContent);
                if(data.questionType == 1)
                    $("#findQuestionType").val("多选题");
                else if(data.questionType == 2)
                    $("#findQuestionType").val("单选题");
                else if(data.questionType == 3)
                    $("#findQuestionType").val("问答题");
                let str = '';
                for(let index in data.choices) {
                    str +='<div class="form-group">'
                        +'<label for="firstname" class="col-sm-3 control-label">选项:</label>'
                        +'<div class="col-sm-7">'
                        +'<input type="text" class="form-control" id="findOptionA" value="' + data.choices[index].choiceContent + '" readonly="readonly">'
                        +'<label class="control-label" for="findOptionA" style="display:none;"></label>'
                        +'</div>'
                        +'</div>';
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


