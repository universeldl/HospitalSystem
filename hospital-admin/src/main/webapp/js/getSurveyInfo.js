$(function () {


    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});


function getSurveyInfo(id) {

    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/surveyManageAction_getSurvey.action',
            params: "surveyId=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#findSurveyName").val(data.surveyName);
                $("#findSurveyType").val(data.surveyType.typeName);
                $("#findAuthor").val(data.author);
                $("#findDepartment").val(data.department);
                $("#findDescription").val(data.description);
                $("#findNum").val(data.num);
                $("#findBday").val(data.bday);
                $("#findDoctor").val(data.doctor.name);
                $("#findCurrentNum").val(data.currentNum);
                $("#findTimes").val(data.times);
                if (data.sendOnRegister == true)
                    $("#findSendOnRegister").val("是");
                else
                    $("#findSendOnRegister").val("否");
                if (data.frequency == 1)
                    $("#findFrequency").val("1个月");
                else if (data.frequency == 2)
                    $("#findFrequency").val("2个月");
                else if (data.frequency == 3)
                    $("#findFrequency").val("3个月");
                else if (data.frequency == 6)
                    $("#findFrequency").val("6个月");
                else if (data.frequency == 12)
                    $("#findFrequency").val("12个月");
            }
        }
    );


}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}


