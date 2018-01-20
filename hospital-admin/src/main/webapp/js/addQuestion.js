//var questionType = 1;
//var surveyId = 1;

function addQuestion(sid) {
    surveyId = sid;
}

$(function () {


    $('#add_Question').click(function () {


        if (!validAddQuestion()) {
            return;
        }

        var postdata;
        if (questionType == 1) {
            postdata = "questionType=1&questionContent=" + $.trim($("#addQuestionContent").val())
                + "&surveyId=" + surveyId + "&" + $("#addForm").serialize();
        }
        if (questionType == 2) {
            postdata = "questionType=2&questionContent=" + $.trim($("#addQuestionContent").val())
                + "&surveyId=" + surveyId + "&" + $("#addForm").serialize();
        }
        else if (questionType == 3) {
            postdata = "questionType=3&surveyId=" + surveyId + "&questionContent=" + $.trim($("#addQuestionContent").val());
        }

        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_addQuestion.action',
                params: postdata,
                callback: function (data) {
                    if (data == 1) {
                        $("#addQuestionModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");

                    } else {
                        $("#addQuestionModal").modal("hide");//关闭模糊框
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


function addMultiChoiceDisplay() {
    questionType = 1;
    document.getElementById("choicesBlock").style.display = "block";
    $("#add1").removeClass();
    $("#add1").addClass("btn btn-primary");
    $("#add2").removeClass();
    $("#add2").addClass("btn btn-pinterest");
    $("#add3").removeClass();
    $("#add3").addClass("btn btn-pinterest");
}

function addSingleChoiceDisplay() {
    questionType = 2;
    document.getElementById("choicesBlock").style.display = "block";
    $("#add1").removeClass();
    $("#add1").addClass("btn btn-pinterest");
    $("#add2").removeClass();
    $("#add2").addClass("btn btn-primary");
    $("#add3").removeClass();
    $("#add3").addClass("btn btn-pinterest");
}

function addTextDisplay() {
    questionType = 3;
    document.getElementById("choicesBlock").style.display = "none";
    $("#add1").removeClass();
    $("#add1").addClass("btn btn-pinterest");
    $("#add2").removeClass();
    $("#add2").addClass("btn btn-pinterest");
    $("#add3").removeClass();
    $("#add3").addClass("btn btn-primary");
}

function unique(arr) {
    var result = [], hash = {};
    for (var i = 0, elem; (elem = arr[i]) != null; i++) {
        if (!hash[elem]) {
            result.push(elem);
            hash[elem] = true;
        }
    }
    return result;
}


function getAddCount() {
    var addCount = 0;
    $("#choicesBlock").find("div.col-sm-5").children(":text").each(function () {
        addCount++;
    });
    return addCount;
}

function validAddQuestion() {

    var flag = true;
    var x = Array();

    var questionContent = $.trim($("#addQuestionContent").val());
    if (questionContent == "") {
        $('#addQuestionContent').parent().addClass("has-error");
        $('#addQuestionContent').next().text("请输入问题");
        $("#addQuestionContent").next().show();
        flag = false;
    } else {
        $('#addQuestionContent').parent().removeClass("has-error");
        $('#addQuestionContent').next().text("");
        $("#addQuestionContent").next().hide();
    }

    if(questionType == 1 || questionType ==2) {  //is a selection question
        $("#choicesBlock").find("div.col-sm-5").children(":text").each(function () {
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

        $("#choicesBlock").find("div.col-sm-2").children(":text").each(function () {
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


