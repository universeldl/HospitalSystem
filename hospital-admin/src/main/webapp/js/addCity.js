$(function () {
    $('#addCity').click(function () {

        if(!isValidCityName()) {
            return;
        }
        if(!isValidProvince()) {
            return;
        }

        var postdata = "provinceId=" + $.trim($("#addCityProvince option:selected").val());
        postdata = postdata + "&cityName=" + $.trim($("#addCityName").val());

        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_addCity.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#addCityModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");
                    } else if (data == -2) {
                        $("#addCityModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个城市（区、县），不能重复添加");
                    } else {
                        $("#addCityModal").modal("hide");//关闭模糊框
                        showInfo("添加失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


    function isValidProvince() {
        var provinceId = $.trim($("#addCityProvince option:selected").val());
        if (provinceId == -1) {
            $('#addCityProvince').parent().addClass("has-error");
            $('#addCityProvince').next().text("请选择省份（直辖市）");
            $("#addCityProvince").next().show();
            return false;
        } else {
            $('#addCityProvince').parent().removeClass("has-error");
            $('#addCityProvince').next().text("");
            $("#addCityProvince").next().hide();
        }
        return true;
    }

    function isValidCityName() {
        var flag = true;

        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        var name = $.trim($("#addCityName").val());
        if (name == "") {
            $('#addCityName').parent().addClass("has-error");
            $('#addCityName').next().text("请输入城市（区县）名称");
            $("#addCityName").next().show();
            flag = false;
        } else if (!reg.test(name)) {
            $('#addCityName').parent().addClass("has-error");
            $('#addCityName').next().text("城市（区县）名称必须为中文");
            $("#addCityName").next().show();
            flag = false;
        } else {
            $('#addCityName').parent().removeClass("has-error");
            $('#addCityName').next().text("");
            $("#addCityName").next().hide();
        }

        return flag;
    }

});


$('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
    location.reload();  	//刷新当前页面
});


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

