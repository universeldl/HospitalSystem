var textChoice = 0; //if there's a text choice in the question

function addQuestion(sid) {
    surveyId = sid;
}

$(function () {


    $('#add_Question').click(function () {

        if (document.getElementById("textChoice").checked) {
            textChoice = 1;
        } else {
            textChoice = 0;
        }

        if (!validAddQuestion()) {
            return;
        }

        var postdata;

        var questionContent = $.trim($("#addQuestionContent").val());
        questionContent = replaceSpectialChar(questionContent);

        if (questionType == 1 || questionType == 2) {
            postdata = "questionType=" + questionType + "&questionContent=" + questionContent
                + "&surveyId=" + surveyId + "&textChoice=" + textChoice;
            if ($.trim($("#addStartAge").val()) != "" &&  $.trim($("#addEndAge").val()) != "") {
                postdata = postdata + "&startAge=" + $.trim($("#addStartAge").val())
                    + "&endAge=" + $.trim($("#addEndAge").val());
            } else {
                postdata = postdata + "&startAge=-1&endAge=-1";
            }
            postdata = postdata + "&" + $("#addForm").serialize();
        }
        else if (questionType == 3 || questionType == 4) {
            postdata = "questionType=" + questionType + "&surveyId=" + surveyId + "&textChoice=0" + "&questionContent=" + questionContent;
            if ($.trim($("#addStartAge").val()) != "" && $.trim($("#addEndAge").val()) != "") {
                postdata = postdata + "&startAge=" + $.trim($("#addStartAge").val())
                    + "&endAge=" + $.trim($("#addEndAge").val());
            } else {
                postdata = postdata + "&startAge=-1&endAge=-1";
            }
        }

        //alert("add question " + postdata);
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/surveyManageAction_addQuestion.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
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
    $("#add4").removeClass();
    $("#add4").addClass("btn btn-pinterest");
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
    $("#add4").removeClass();
    $("#add4").addClass("btn btn-pinterest");
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
    $("#add4").removeClass();
    $("#add4").addClass("btn btn-pinterest");
}


function addTextDateDisplay() {
    questionType = 4;
    document.getElementById("choicesBlock").style.display = "none";
    $("#add1").removeClass();
    $("#add1").addClass("btn btn-pinterest");
    $("#add2").removeClass();
    $("#add2").addClass("btn btn-pinterest");
    $("#add3").removeClass();
    $("#add3").addClass("btn btn-pinterest");
    $("#add4").removeClass();
    $("#add4").addClass("btn btn-primary");
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

    var sAge = $.trim($("#addStartAge").val());
    var eAge = $.trim($("#addEndAge").val());

    if ((sAge == "" && eAge != "") ||
        (sAge != "" && eAge == "")) {
        $('#addStartAge').parent().addClass("has-error");
        $('#addStartAge').next().text("请同时设置起始与结束年龄");
        $("#addStartAge").next().show();
        $('#addEndAge').parent().addClass("has-error");
        $('#addEndAge').next().text("请同时设置起始与结束年龄");
        $("#addEndAge").next().show();
        flag = false;
    } else if (sAge != "" && eAge != "") {
        if (sAge < 0) {
            $('#addStartAge').parent().addClass("has-error");
            $('#addStartAge').next().text("起始年龄必须大于零");
            $("#addStartAge").next().show();
        }
        if (eAge < 0) {
            $('#addEndAge').parent().addClass("has-error");
            $('#addEndAge').next().text("结束年龄必须大于零");
            $("#addEndAge").next().show();
        }
        if (sAge > eAge) {
            $('#addStartAge').parent().addClass("has-error");
            $('#addStartAge').next().text("起始年龄必须小于或等于结束年龄");
            $("#addStartAge").next().show();
            $('#addEndAge').parent().addClass("has-error");
            $('#addEndAge').next().text("起始年龄必须小于或等于结束年龄");
            $("#addEndAge").next().show();
            flag = false;
        }
    }

    if (questionType == 1 || questionType == 2) {  //is a selection question
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

/*
function removeSpetialChar(str) {
    str = str.replace(/%/g, "%25");
    str = str.replace(/\&/g, "%26");
    str = str.replace(/\+/g, "%2B");
    return str;
}
    */