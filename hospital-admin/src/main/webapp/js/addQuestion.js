//var questionType = 1;
//var surveyId = 1;

function addQuestion(sid){
    surveyId = sid;
}

$(function () {


  $('#add_Question').click(function () {


 	if (!validAddQuestion()) {
        return;
    }

    var postdata;
    if ( questionType == 1) {
        postdata = "questionType=1&questionContent="+$.trim($("#addQuestionContent").val())
          +"&surveyId="+ surveyId + "&" + $("#addForm").serialize();
      //postdata = "questionType=1&questionContent="+$.trim($("#addQuestionContent").val())
      //    +"&surveyId="+ surveyId
      //    +"&choiceOption1="+ $.trim($("#choiceOption1").val())
      //    +"&choiceOption2="+ $.trim($("#choiceOption2").val())
      //    +"&choiceOption3="+ $.trim($("#choiceOption3").val())
      //    +"&choiceOption4="+ $.trim($("#choiceOption4").val())
      //    +"&choiceOption5="+ $.trim($("#choiceOption5").val());
    }
    else if ( questionType == 2) {
      postdata = "questionType=2&surveyId="+ surveyId + "&questionContent="+ $.trim($("#addQuestionContent").val());
	}

	ajax(
    		  {
			  	method:'POST',
	    		url:'doctor/surveyManageAction_addQuestion.action',
				params: postdata,
	    		callback:function(data) {
					if (data == 1) {
						$("#addQuestionModal").modal("hide");//关闭模糊框
						showInfo("添加成功");	

	                }else {
						$("#addQuestionModal").modal("hide");//关闭模糊框
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



function choiceDisplay()
{
    questionType = 1;
    document.getElementById("choicesBlock").style.display="block";
}

function choiceHide()
{
    questionType = 2;
    document.getElementById("choicesBlock").style.display="none";
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



function validAddQuestion() {
    var flag = true;

    var questionContent = $.trim($("#addQuestionContent").val());
    var choiceOption1 = $.trim($("#choiceOption1").val());
    var choiceOption2 = $.trim($("#choiceOption2").val());
    var choiceOption3 = $.trim($("#choiceOption3").val());
    var choiceOption4 = $.trim($("#choiceOption4").val());
    var choiceOption5 = $.trim($("#choiceOption5").val());

    if (questionContent == "") {
        $('#addQuestionContent').parent().addClass("has-error");
        $('#addQuestionContent').next().text("请输入问题");
        $("#addQuestionContent").next().show();
        flag = false;
    }else {
        $('#addQuestionContent').parent().removeClass("has-error");
        $('#addQuestionContent').next().text("");
        $("#addQuestionContent").next().hide();
    }

    var x = Array(choiceOption1, choiceOption2, choiceOption3, choiceOption4, choiceOption5);
    var y = unique(x);
    if(y.length < 2) {
    	$('#choiceOption1').parent().addClass("has-error");
        $('#choiceOption1').next().text("每个问题至少需要有两个不重复的选项");
        $("#choiceOption1").next().show();
        flag = false;
	}else {
        $('#choiceOption1').parent().removeClass("has-error");
        $('#choiceOption1').next().text("");
        $("#choiceOption1").next().hide();
    } 

    return flag;
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


