function deleteDoctor(id) {
  if(confirm('删除后医生数据不可恢复，确定要执行此操作吗?')) {
      $('#loading').show();
      ajax(
          {
              method: 'POST',
              url: 'doctor/doctorManageAction_deleteDoctor.action',
              params: "id=" + id,
              callback: function (data) {
                  $('#loading').hide();
                  if (data == 1) {
                      showInfo("删除成功");
                  }
                  else {
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


