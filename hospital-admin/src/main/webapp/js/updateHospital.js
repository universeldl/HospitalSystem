var curCityId = -1;

$(function () {


    $('#updateHospital').click(function () {

        if (!isValidUpdateHospital()) {
            return;
        }

        var postdata = "hospitalName=" + $.trim($("#updateHospitalName").val())
            + "&hospitalId=" + $.trim($("#updateHospitalId").val())
            + "&cityId=" + $.trim($("#updateHospitalCity option:selected").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_updateHospital.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("修改成功");
                    } else if (data == -2) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个医院了");
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



function isValidUpdateHospital() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#updateHospitalName").val());
    if (name == "") {
        $('#updateHospitalName').parent().addClass("has-error");
        $('#updateHospitalName').next().text("请输入省份名称");
        $("#updateHospitalName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#updateHospitalName').parent().addClass("has-error");
        $('#updateHospitalName').next().text("省份名称必须为中文");
        $("#updateHospitalName").next().show();
        flag = false;
    } else {
        $('#updateHospitalName').parent().removeClass("has-error");
        $('#updateHospitalName').next().text("");
        $("#updateHospitalName").next().hide();
    }

    var provinceId = $.trim($("#updateHospitalProvince option:selected").val());
    if (provinceId == -1) {
        $('#updateHospitalProvince').parent().addClass("has-error");
        $('#updateHospitalProvince').next().text("请选择省份（直辖市）");
        $("#updateHospitalProvince").next().show();
        flag = false;
    } else {
        $('#updateHospitalProvince').parent().removeClass("has-error");
        $('#updateHospitalProvince').next().text("");
        $("#updateHospitalProvince").next().hide();
    }

    var cityId = $.trim($("#updateHospitalCity option:selected").val());
    if (cityId == -1) {
        $('#updateHospitalCity').parent().addClass("has-error");
        $('#updateHospitalCity').next().text("请选择城市（区、县）");
        $("#updateHospitalCity").next().show();
        flag = false;
    } else {
        $('#updateHospitalCity').parent().removeClass("has-error");
        $('#updateHospitalCity').next().text("");
        $("#updateHospitalCity").next().hide();
    }

    return flag;
}

function updateHospital(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_getHospitalById.action',
            params: "hospitalId=" + id,
            type: "json",
            async:false,
            callback: function (data) {
                $('#loading').hide();
                $("#updateHospitalId").val(data.id);
                $("#updateHospitalName").val(data.name);

                $("#updateHospitalProvince option:checked").attr("selected", "");
                var provinceId = data.provinceId;
                curCityId = data.cityId;
                $("#updateHospitalProvince option[value='"+provinceId+"']").attr("selected", "selected");
                loadCity2();
            }
        }
    );


}

function loadCity2() {
    var postdata = "provinceId=";
    postdata = postdata + $.trim($('#updateHospitalProvince option:selected').val());
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_findCityByProvinceID.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#updateHospitalCity').html("<option disabled selected value></option>");
                for(var i = 0; i < data.length; i++) {
                    $("#updateHospitalCity").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
                $("#updateHospitalCity option[value='"+curCityId+"']").attr("selected", "selected");

            }
        }
    );
}


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

