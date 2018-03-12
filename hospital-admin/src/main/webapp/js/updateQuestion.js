let questionType = 1;
let surveyId = 1;
let questionId = 1;
let updateTextChoice = 0;

function updateQuestion(sid, qid) {
    surveyId = sid;
    questionId = qid;

    ajax(
        {
            method: 'POST',
            url: 'doctor/surveyManageAction_getQuestion.action',
            params: "questionId=" + qid,
            type: "json",
            callback: function (data) {
                $("#updateQuestionContent").val(data.questionContent);

                if (data.textChoice == 1) {
                    document.getElementById("updateTextChoice").checked = true;
                }
                else if (data.textChoice == 0) {
                    document.getElementById("updateTextChoice").checked = false;
                }

                if (data.startAge == -1) {
                    $("#updateStartAge").val("");
                } else {
                    $("#updateStartAge").val(data.startAge);
                }

                if (data.endAge == -1) {
                    $("#updateEndAge").val("");
                } else {
                    $("#updateEndAge").val(data.endAge);
                }

                if (data.questionType == 1) {
                    updateMultiChoiceDisplay();
                }
                else if (data.questionType == 2) {
                    updateSingleChoiceDisplay();
                }
                else if (data.questionType == 3) {
                    updateTextDisplay();
                }

                var str = '';
                $("#updateChoicesDiv").nextUntil("p").each(function () {//clean first
                    $(this).outerHTML = '';
                    $(this).remove();
                });
                for (let index in data.choices) {
                    choiceCount++;
                    str += '<div class="form-group">'
                        + '<label for="choiceOption' + index + '" class="col-sm-3 control-label">选项: </label>'
                        + '<div class="col-sm-5">'
                        + '<input type="text" class="form-control" name="choiceOption' + index + '" id="choiceOption' + index + '" value="' + data.choices[index].choiceContent + '">'
                        + '<label class="control-label" for="choiceOption' + index + '" style="display: none;"></label>'
                        + '</div>'
                        + '<div class="col-sm-2">'
                        + '<input type="text" class="form-control" name="score' + index + '" id="score' + index + '" value="' + data.choices[index].score + '">'
                        + '<label class="control-label" for="score' + index + '" style="display: none;"></label>'
                        + '</div>'
                        + '<p><span class="removeUpdateVar">删除</span></p>'
                        + '</div>';
                }
                document.getElementById("updateChoicesDiv").innerHTML = str;//then set
            }
        }
    );

}

$(function () {


    $('#update_Question').click(function () {

        if (document.getElementById("updateTextChoice").checked) {
            updateTextChoice = 1;
        } else {
            updateTextChoice = 0;
        }

        if (!validUpdateQuestion()) {
            return;
        }

        var postdata;
        if (questionType == 1 || questionType == 2) {
            postdata = "questionType=" + questionType + "&questionContent=" + $.trim($("#updateQuestionContent").val())
                + "&surveyId=" + surveyId + "&textChoice=" + updateTextChoice + "&questionId=" + questionId;
            if ($.trim($("#updateStartAge").val()) != "" && $.trim($("#updateEndAge").val()) != "") {
                postdata = postdata + "&startAge=" + $.trim($("#updateStartAge").val())
                    + "&endAge=" + $.trim($("#updateEndAge").val());
            } else {
                postdata = postdata + "&startAge=-1&endAge=-1";
            }
            postdata = postdata + "&" + $("#updateForm").serialize();
        }
        /*
        if (questionType == 2) {
            postdata = "questionType=2&questionContent=" + $.trim($("#updateQuestionContent").val())
                + "&surveyId=" + surveyId + "&textChoice=" + updateTextChoice + "&questionId=" + questionId + "&" + $("#updateForm").serialize();
        }
        */
        else if (questionType == 3) {
            postdata = "questionType=3&surveyId=" + surveyId + "&questionId=" + questionId + "&textChoice=0"
                + "&questionContent=" + $.trim($("#updateQuestionContent").val());
            if ($.trim($("#updateStartAge").val()) != "" && $.trim($("#updateEndAge").val()) != "") {
                postdata = postdata + "&startAge=" + $.trim($("#updateStartAge").val())
                    + "&endAge=" + $.trim($("#updateEndAge").val());
            } else {
                postdata = postdata + "&startAge=-1&endAge=-1";
            }
        }

        //alert(postdata);

        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_updateQuestion.action',
                params: postdata,
                callback: function (data) {
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


function updateMultiChoiceDisplay() {
    questionType = 1;
    document.getElementById("updateChoicesBlock").style.display = "block";
    $("#update1").removeClass();
    $("#update1").addClass("btn btn-primary");
    $("#update2").removeClass();
    $("#update2").addClass("btn btn-pinterest");
    $("#update3").removeClass();
    $("#update3").addClass("btn btn-pinterest");
}

function updateSingleChoiceDisplay() {
    questionType = 2;
    document.getElementById("updateChoicesBlock").style.display = "block";
    $("#update1").removeClass();
    $("#update1").addClass("btn btn-pinterest");
    $("#update2").removeClass();
    $("#update2").addClass("btn btn-primary");
    $("#update3").removeClass();
    $("#update3").addClass("btn btn-pinterest");
}

function updateTextDisplay() {
    questionType = 3;
    document.getElementById("updateChoicesBlock").style.display = "none";
    $("#update1").removeClass();
    $("#update1").addClass("btn btn-pinterest");
    $("#update2").removeClass();
    $("#update2").addClass("btn btn-pinterest");
    $("#update3").removeClass();
    $("#update3").addClass("btn btn-primary");
}

function getUpdateCount() {
    var updateCount = 0;
    $("#updateChoicesBlock").find("div.col-sm-5").children(":text").each(function () {
        updateCount++;
    });
    return updateCount;
}

function validUpdateQuestion() {

    var flag = true;
    var x = Array();

    var questionContent = $.trim($("#updateQuestionContent").val());
    if (questionContent == "") {
        $('#updateQuestionContent').parent().addClass("has-error");
        $('#updateQuestionContent').next().text("请输入问题");
        $("#updateQuestionContent").next().show();
        flag = false;
    } else {
        $('#updateQuestionContent').parent().removeClass("has-error");
        $('#updateQuestionContent').next().text("");
        $("#updateQuestionContent").next().hide();
    }

    var sAge = $.trim($("#updateStartAge").val());
    var eAge = $.trim($("#updateEndAge").val());

    //var regPos = /^\d+$/;

    if ((sAge == "" && eAge != "") ||
        (sAge != "" && eAge == "")) {
        $('#updateStartAge').parent().addClass("has-error");
        $('#updateStartAge').next().text("请同时设置起始与结束年龄");
        $("#updateStartAge").next().show();
        $('#updateEndAge').parent().addClass("has-error");
        $('#updateEndAge').next().text("请同时设置起始与结束年龄");
        $("#updateEndAge").next().show();
        flag = false;
    } else if ((/^\d+$/.test(sAge)) && (/^\d+$/.test(eAge))) {
        if (sAge < 0) {
            $('#updateStartAge').parent().addClass("has-error");
            $('#updateStartAge').next().text("起始年龄必须大于零");
            $("#updateStartAge").next().show();
            flag = false;
        } else if (sAge > 99) {
            $('#updateStartAge').parent().addClass("has-error");
            $('#updateStartAge').next().text("起始年龄不能超过99");
            $("#updateStartAge").next().show();
            flag = false;
        }
        if (eAge < 0) {
            $('#updateEndAge').parent().addClass("has-error");
            $('#updateEndAge').next().text("结束年龄必须大于零");
            $("#updateEndAge").next().show();
            flag = false;
        } else if (eAge > 99) {
            $('#updateEndAge').parent().addClass("has-error");
            $('#updateEndAge').next().text("结束年龄不能超过99");
            $("#updateEndAge").next().show();
            flag = false;
        }
        if (sAge > eAge) {
            $('#updateStartAge').parent().addClass("has-error");
            $('#updateStartAge').next().text("起始年龄必须小于或等于结束年龄");
            $("#updateStartAge").next().show();
            $('#updateEndAge').parent().addClass("has-error");
            $('#updateEndAge').next().text("起始年龄必须小于或等于结束年龄");
            $("#updateEndAge").next().show();
            flag = false;
        }
    } else if (!(/^\d+$/.test(sAge))){
        $('#updateStartAge').parent().addClass("has-error");
        $('#updateStartAge').next().text("起始年龄必须是正整数");
        $("#updateStartAge").next().show();
        flag = false;
    } else if (!(/^\d+$/.test(eAge))) {
        $('#updateEndAge').parent().addClass("has-error");
        $('#updateEndAge').next().text("结束年龄必须是正整数");
        $("#updateEndAge").next().show();
        flag = false;
    }

    if(questionType == 1 || questionType ==2) {  //is a selection question
        $("#updateChoicesBlock").find("div.col-sm-5").children(":text").each(function () {
            var tmp = $(this).val();
            if (tmp == "") {
                $(this).parent().addClass("has-error");
                $(this).next().text("选项不能为空");
                $(this).next().show();
                flag = false;
            }
            else if (x.includes(tmp)) {
                $(this).parent().addClass("has-error");
                $(this).next().text("选项不能重复");
                $(this).next().show();
                flag = false;
            } else {
                x.push(tmp);
                $(this).parent().removeClass("has-error");
                $(this).next().text("");
                $(this).next().hide();
            }
        });

        $("#updateChoicesBlock").find("div.col-sm-2").children(":text").each(function () {
            var tmp = $(this).val();
            if (tmp == "") {
                $(this).parent().addClass("has-error");
                $(this).next().text("分数不能为空");
                $(this).next().show();
                flag = false;
            }
            else if (isNaN(parseInt(tmp)) || parseInt(tmp) < 0 || parseInt(tmp) > 99) {
                $(this).parent().addClass("has-error");
                $(this).next().text("分数必须为0～99之间的正整数");
                $(this).next().show();
                flag = false;
            } else {
                $(this).parent().removeClass("has-error");
                $(this).next().text("");
                $(this).next().hide();
            }
        });
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

