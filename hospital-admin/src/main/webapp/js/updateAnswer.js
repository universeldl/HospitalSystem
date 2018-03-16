let questionType = 1;
let answerId = 1;

function updateAnswerById(ansId) {
    answerId = ansId;

    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/retrieveManageAction_getAnswerById.action',
            params: "answerId=" + answerId,
            type: "json",
            callback: function (data) {
                $('#loading').hide();

                var str = '';
                //$("#updateChoicesDiv").next().each(function () {//clean first
                //    $(this).outerHTML = '';
                //    $(this).remove();
                //});

                if (data.questionType == 1) {//多选
                    questionType = 1;
                    str += '<div class="form-group">';
                    for (let index in data.choices) {
                        str += '<div>'
                            + '<input type="checkbox" name="multiChoiceOption" id="multiChoiceOption' + data.choices[index].choiceId + '"  value="' + data.choices[index].choiceId + '">' + data.choices[index].choiceContent
                            + '</div>';
                    }
                    if (data.textChoice == 1) {
                        //let sz = data.choices.size + 1;
                        str += '<input type="checkbox" name="multiChoiceOption" value="99999' + '">其它:'
                            + '<textarea class="form-control" rows="3" name="updateTextChoice" id="updateTextChoice">' + data.textChoiceContent + '</textarea>';
                    }
                    str += '</div>';
                }
                else if (data.questionType == 2) {//单选
                    questionType = 2;
                    str += '<div class="form-group">';
                    for (let index in data.choices) {
                        str += '<div>'
                            + '<input type="radio" name="singleChoiceOption" id="singleChoiceOption' + data.choices[index].choiceId + '" value="' + data.choices[index].choiceId + '">' + data.choices[index].choiceContent
                            + '</div>';
                    }
                    if (data.textChoice == 1) {
                        //let sz = data.choices.size + 1;
                        //str += '<input type="radio" name="singleChoiceOption" value="choiceOption' + sz + '">其它:'
                        str += '<input type="radio" name="singleChoiceOption" value="99999' + '">其它:'
                            + '<textarea class="form-control" rows="3" name="updateTextChoice" id="updateTextChoice">' + data.textChoiceContent + '</textarea>';
                    }
                    str += '</div>';
                }
                else if (data.questionType == 3) {//问答
                    questionType = 3;
                    str += '<div class="form-group">';
                    if (data.textChoice == 1) {
                        str += '<p>答案:</p>'
                            + '<textarea class="form-control" rows="3" name="updateTextChoice" id="updateTextChoice">' + data.textChoiceContent + '</textarea>';
                    }
                    str += '</div>';
                }

                document.getElementById("updateChoicesDiv").innerHTML = str;//then set
            }
        }
    );

}

$(function () {


    $('#update_answer').click(function () {

        if (!validUpdateAnswer()) {
            return;
        }

        var postdata;
        postdata = "answerId=" + answerId + "&" + $("#updateForm").serialize();

        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/retrieveManageAction_updateAnswer.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateQuestionModal").modal("hide");//关闭模糊框
                        showInfo("更新成功");

                    } else {
                        $("#updateQuestionModal").modal("hide");//关闭模糊框
                        showInfo("更新失败");
                    }

                }
            }
        );


    });


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function validUpdateAnswer() {

    var flag = true;

    if (questionType == 1) {//多选
        let elm = document.getElementsByName('multiChoiceOption')
        let checkedCount = 0;
        for (ii = 0; ii < elm.length; ii++) {
            if(elm[ii].checked) checkedCount++;
        }
        if (checkedCount < 2) {
            alert("多选题至少选择两项答案");
            flag = false;
        }
    }
    if (questionType == 2) {//单选
        let elm = document.getElementsByName('singleChoiceOption')
        let checkedCount = 0;
        for (ii = 0; ii < elm.length; ii++) {
            if(elm[ii].checked) checkedCount++;
        }
        if (checkedCount != 1) {
            alert("请选择答案");
            flag = false;
        }
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

