$(function () {


    $('#updateCity').click(function () {

        if (!isValidUpdateCity()) {
            return;
        }

        var postdata = "provinceId=" + $.trim($("#updateCityProvince option:selected").val())
            + "&cityId=" + $.trim($("#updateCityId").val())
            + "&cityName=" + $.trim($("#updateCityName").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_updateCity.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("修改成功");
                    } else if (data == -2) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个城市（区、县）了");
                    } else {
                        $("#updateinfo").modal("hide");//关闭模糊框
                        showInfo("修改失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


});



function isValidUpdateCity() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#updateCityName").val());
    if (name == "") {
        $('#updateCityName').parent().addClass("has-error");
        $('#updateCityName').next().text("请输入省份名称");
        $("#updateCityName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#updateCityName').parent().addClass("has-error");
        $('#updateCityName').next().text("省份名称必须为中文");
        $("#updateCityName").next().show();
        flag = false;
    } else {
        $('#updateCityName').parent().removeClass("has-error");
        $('#updateCityName').next().text("");
        $("#updateCityName").next().hide();
    }

    var provinceId = $.trim($("#updateCityProvince option:selected").val());
    if (provinceId == -1) {
        $('#updateCityProvince').parent().addClass("has-error");
        $('#updateCityProvince').next().text("请选择省份（直辖市）");
        $("#updateCityProvince").next().show();
        flag = false;
    } else {
        $('#updateCityProvince').parent().removeClass("has-error");
        $('#updateCityProvince').next().text("");
        $("#updateCityProvince").next().hide();
    }

    return flag;
}

function updateCity(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_getCityById.action',
            params: "cityId=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#updateCityId").val(data.id);
                $("#updateCityName").val(data.name);

                $("#updateCityProvince option:checked").attr("selected", "");
                var provinceId = data.provinceId;
                $("#updateCityProvince option[value='"+provinceId+"']").attr("selected", "selected");
            }
        }
    );


}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

