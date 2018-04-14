var quesitonIndex = 0;
var questions;
var postdata = "";

function getQuestions() {
    showLoadingToast("正在加载问题...");

    var po = "deliveryID=" + $.trim($("#deliveryID").val());
    ajax(
        {
            method: 'POST',
            url: 'patient/surveyAction_getQuestions.action',
            params: po,
            type: "json",
            scriptCharset: 'utf-8',
            callback: function (data) {
                $("#startButton").fadeIn();
                hideLoadingToast();
                questions = data;
            }
        }
    );
}

function questionStart() {
    $('#surveyName').hide();
    $('#surveyDescription').hide();
    $('#startButton').hide();
    displayPage(quesitonIndex);
}

function displayPage(index) {
    var showIndex = index + 1;
    var question = questions[index];

    $('#questionIndex').text("问题（" + showIndex + "/" + questions.length + "）").show();
    $('#questionContent').text(question.questionContent).show();

    displayChoices(question);
    displayButton(index);
}


function displayButton(index) {
    if (index == 0) {
        document.getElementById("lastButton").style.display = "none";

        document.getElementById("nextButton").innerHTML = "下一题"
        document.getElementById("nextButton").style.display = "";
    } else if (index == questions.length - 1) {
        document.getElementById("lastButton").innerHTML = "上一题"
        document.getElementById("lastButton").style.display = "";

        document.getElementById("nextButton").innerHTML = "提  交"
        document.getElementById("nextButton").style.display = "";
    } else {
        document.getElementById("lastButton").innerHTML = "上一题"
        document.getElementById("lastButton").style.display = "";

        document.getElementById("nextButton").innerHTML = "下一题"
        document.getElementById("nextButton").style.display = "";
    }
}

function displayChoices(question) {
    $("#checkBoxArea").empty().hide();
    $("#radioBoxArea").empty().hide();
    $("#textArea").hide();
    $("#dateSelection").hide();

    $("#textQuestionArea").val("");
    $("#textQuestionArea").attr("name","")
    $("#dateQuestion").attr("name","")

    if (question.questionType == 1) {
        $("#checkBoxArea").append(getMultiChoiceLabel(question));
        $("#checkBoxArea").show();
        if (question.textChoice == 1) {
            $("#textArea").show();
            $("#textQuestionArea").attr("placeholder","其他...");
            $("#textQuestionArea").attr("name","textquestion"+question.questionId);
            $("#textQuestionArea").attr("disabled",false);
        } else {
            $("#textArea").hide();
        }
    } else if (question.questionType == 2) {
        $("#radioBoxArea").append(getSingleChoiceLabel(question));
        $("#radioBoxArea").show();
        if (question.textChoice == 1) {
            $("#textArea").show();
            $("#textQuestionArea").attr("placeholder","其他...");
            $("#textQuestionArea").attr("name","textquestion"+question.questionId);
            $("#textQuestionArea").attr("disabled",true);
        }
    } else if (question.questionType == 3) {
        $("#textArea").show();
        $("#textQuestionArea").attr("placeholder","请输入答案...");
        $("#textQuestionArea").attr("name","textquestion"+question.questionId);
        $("#textQuestionArea").attr("disabled",false);
    } else if (question.questionType == 4) {
        $("#dateSelection").show();
        $("#dateQuestion").attr("name","textquestion"+question.questionId);
    }
}

function getMultiChoiceLabel(question) {
    var str = "";
    var choices = question.choices;
    for (var cindex = 0; cindex < choices.length; cindex++) {
        var choice = choices[cindex];

        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choice" + choice.choiceId + "'>"
        if (choice.choiceImgPath != "") {
            str = str + "<img src='" + choice.choiceImgPath + "' width=80px height=80px>"
        }
        str = str + "<div class='weui-cell__bd'> <p>" + choice.choiceContent + "</p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='checkbox' name='question";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choice" + choice.choiceId + "'";
        str = str + " value='" + choice.choiceId + "' /> </div> </label>"
    }
    return str;
}

function getSingleChoiceLabel(question) {
    var str = "";
    var choices = question.choices;
    for (var cindex = 0; cindex < choices.length; cindex++) {
        var choice = choices[cindex];

        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choice" + choice.choiceId + "'>"

        if (choice.choiceImgPath != "") {
            str = str + "<img src='" + choice.choiceImgPath + "' width=80px height=80px>"
        }

        str = str + "<div class='weui-cell__bd'> <p>" + choice.choiceContent + "</p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='radio' name='question";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choice" + choice.choiceId + "'";

        if (question.textChoice == 1) {
            str = str + " onclick=disableTextArea(); "
        }
        str = str + " value='" + choice.choiceId + "' /> </div> </label>"
    }
    if (question.textChoice == 1) {
        str = str + "<label class='weui-cell weui-check__label' for='question";
        str = str + question.questionId + "choiceother'>"
        str = str + "<div class='weui-cell__bd'> <p> 其他 </p> </div>";
        str = str + "<div class='weui-cell__ft'>";
        str = str + "<input type='radio' name='question";
        str = str + question.questionId + "'";
        str = str + " id='question" + question.questionId + "choiceother'";
        str = str + " onclick=enableTextArea() /> </div></label>"
    }
    return str;
}

function next() {
    location.href = "#top";
    var data = $('form').serialize();
    var RegExp = /^textquestion\d+\=$/g
    if (data) {
        if (data != "") {
            if (RegExp.test(data)) {
                showDialog2("请先答题", "确定");
                return;
            } else {
                if (postdata == "") {
                    postdata = data;
                } else {
                    postdata = postdata + "&" + data;
                }
            }
        } else {
            showDialog2("请先答题", "确定");
            return;
        }
    } else {
        showDialog2("请先答题", "确定");
        return;
    }
    if (quesitonIndex == questions.length -1) {
        submit();
    } else {
        quesitonIndex = quesitonIndex + 1;
        displayPage(quesitonIndex);
    }
}


function last() {
    location.href = "#top";
    quesitonIndex = quesitonIndex - 1;
    var questinId = questions[quesitonIndex].questionId;


    remove = "textquestion" + questinId;
    index = postdata.indexOf(remove);
    if (index > 0) {
        postdata = postdata.substr(0, index-1);
    } else if (index == 0) {
        postdata = postdata.substr(0, index);
    }

    var remove = "question" + questinId;
    var index = postdata.indexOf(remove);
    if (index > 0) {
        postdata = postdata.substr(0, index-1);
    } else if (index == 0) {
        postdata = postdata.substr(0, index);
    }

    displayPage(quesitonIndex);
}

function showLoadingToast(str) {
    var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') {
        return;
    }
    $("#loadToastStr").html(str);
    $loadingToast.fadeIn(100);
}


function showToast(str) {
    var $toast = $('#toast');
    if ($toast.css('display') != 'none') return;
    $("#toastStr").html(str);
    $toast.fadeIn(100);
    setTimeout(function () {
        $toast.fadeOut(100);
    }, 2000);

}

function showDialog2(str1, str2) {
    var $dialog = $('#iosDialog2');
    $("#dialog2Str1").html(str1);
    $("#dialog2Str2").html(str2);
    $dialog.fadeIn(200);

    $dialog.on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });
}

function enableTextArea() {
    $("#textQuestionArea").attr("disabled",false);
}
function disableTextArea(id) {
    $("#textQuestionArea").val("");
    $("#textQuestionArea").attr("disabled",true);
}

function hideLoadingToast() {
    $('#loadingToast').fadeOut(100);
}

function submit() {
    showLoadingToast("提交答案...");

    var po = "deliveryID=" + $.trim($("#deliveryID").val()) + "&";
    po = po + postdata;
    var xhr = $.ajax({
        method: 'POST',
        url: 'surveyAction_retrieveAnswer.action',
        data: po,
        dataType: "json",
        timeout:20000,
        scriptCharset: 'utf-8',
        success:function(data){ //请求成功的回调函数
            hideLoadingToast();
            if (data.success == 1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷提交成功!<br/><br/>" + data.msg;
                window.location.href = url;
            } else if (data.success == -1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷出错...";
                window.location.href = url;
            } else {
                showDialog2("提交失败，请稍后再试", "确认");
            }
        },
        complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
            if(status=='timeout'){//超时,status还有success,error等值的情况
                xhr.abort();    // 超时后中断请求
                hideLoadingToast();
                showDialog2("答卷提交失败，请稍后再试", "确认");
            }
        }
            /*         callback: function (data) {
                         hideLoadingToast();
                         if (data.success == 1) {
                             var url =  "https://" + window.location.host;
                             url = url + "/hospital-wechat/message.jsp?msg=答卷提交成功!<br/><br/>" + data.msg;
                             window.location.href = url;
                         } else if (data.success == -1) {
                             var url =  "https://" + window.location.host;
                             url = url + "/hospital-wechat/message.jsp?msg=答卷出错...";
                             window.location.href = url;
                         } else {
                            showDialog2("提交失败，请稍后再试", "确认");
                         }
                     }*/
    });

}