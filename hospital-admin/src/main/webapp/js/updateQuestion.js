let questionType = 1;
let surveyId = 1;
let questionId = 1;

function updateQuestion(sid, qid){
    surveyId = sid;
    questionId = qid;
}

$(function () {


  $('#update_Question').click(function () {


 	if (!validUpdateQuestion()) {
        return;
    }

    var postdata;
    if ( questionType == 1) {
      postdata = "questionType=1&questionContent="+$.trim($("#updateQuestionContent").val())
          +"&surveyId="+ surveyId+"&questionId="+ questionId
          +"&choiceOption1="+ $.trim($("#updateOption1").val())
          +"&choiceOption2="+ $.trim($("#updateOption2").val())
          +"&choiceOption3="+ $.trim($("#updateOption3").val())
          +"&choiceOption4="+ $.trim($("#updateOption4").val())
          +"&choiceOption5="+ $.trim($("#updateOption5").val());
    }
    else if ( questionType == 2) {
        postdata = "questionType=2&questionContent="+$.trim($("#updateQuestionContent").val())
	}

	ajax(
    		  {
			  	method:'POST',
	    		url:'doctor/surveyManageAction_updateQuestion.action',
				params: postdata,
	    		callback:function(data) {
					if (data == 1) {
						$("#updateQuestionModal").modal("hide");//关闭模糊框
						showInfo("添加成功");

	                }else {
						$("#updateQuestionModal").modal("hide");//关闭模糊框
						showInfo("添加失败");
					}

				}
			}

    	);


	});


	$('#modal_info').on('hide.bs.modal',function() {//提示模糊框隐藏时候触发
       	location.reload();  	//刷新当前页面
    });


});


/*
function choiceDisplay()
{
    questionType = 1;
    var options = new Array("updateOptionA", "updateOptionB", "updateOptionC", "updateOptionD", "updateOptionE");
    for(var i=0;i<options.length;i++)
      document.getElementById(options[i]).style.display="block";
}



function choiceHide()
{
    questionType = 2;
    var options = new Array("updateOptionA", "updateOptionB", "updateOptionC", "updateOptionD", "updateOptionE");
    for(var i=0;i<options.length;i++)
        document.getElementById(options[i]).style.display="none";
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

*/

function validUpdateQuestion() {
    var flag = true;

    var questionContent = $.trim($("#updateQuestionContent").val());
    var updateOption1 = $.trim($("#updateOption1").val());
    var updateOption2 = $.trim($("#updateOption2").val());
    var updateOption3 = $.trim($("#updateOption3").val());
    var updateOption4 = $.trim($("#updateOption4").val());
    var updateOption5 = $.trim($("#updateOption5").val());

    if (questionContent == "") {
        $('#updateQuestionContent').parent().updateClass("has-error");
        $('#updateQuestionContent').next().text("请输入问题");
        $("#updateQuestionContent").next().show();
        flag = false;
    }else {
        $('#updateQuestionContent').parent().removeClass("has-error");
        $('#updateQuestionContent').next().text("");
        $("#updateQuestionContent").next().hide();
    }

    var x = Array(updateOption1, updateOption2, updateOption3, updateOption4, updateOption5);
    var y = unique(x);
    if(y.length < 2) {
    	$('#updateOption1').parent().updateClass("has-error");
        $('#updateOption1').next().text("每个问题至少需要有两个不重复的选项");
        $("#updateOption1").next().show();
        flag = false;
	}else {
        $('#updateOption1').parent().removeClass("has-error");
        $('#updateOption1').next().text("");
        $("#updateOption1").next().hide();
    }

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

