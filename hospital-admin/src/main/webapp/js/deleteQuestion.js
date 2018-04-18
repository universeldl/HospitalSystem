function deleteQuestion(id) {
  if(confirm('删除问题会影响所有该问卷的答卷，确定要执行此操作吗?')) {
      $('#loading').show();
      ajax(
          {
              method: 'POST',
              url: 'doctor/surveyManageAction_deleteQuestion.action',
              params: "questionId=" + id,
              callback: function (data) {
                  $('#loading').hide();
                  if (data == 1) {
                      showInfo("删除成功");
                  } else {
                      showInfo("删除失败");
                  }

              }
          }
      );
  }


}

$('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
    location.reload();  	//刷新当前页面
});


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


