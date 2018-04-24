$(function () {
    $('#addHospital').click(function () {

        if(!isValidHospitalCityProvince()) {
            return;
        }

        var postdata = "provinceId=" + $.trim($("#addHospitalProvince option:selected").val())
            + "&cityId=" + $.trim($("#addHospitalCity option:selected").val())
            + "&hospitalName=" + $.trim($("#addHospitalName").val());

        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_addHospital.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#addCityModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");
                    } else if (data == -2) {
                        $("#addCityModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个医院，不能重复添加");
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


    function isValidHospitalCityProvince() {
        var flag = true;

        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        var name = $.trim($("#addHospitalName").val());
        if (name == "") {
            $('#addHospitalName').parent().addClass("has-error");
            $('#addHospitalName').next().text("请输入医院名称");
            $("#addHospitalName").next().show();
            flag = false;
        } else if (!reg.test(name)) {
            $('#addHospitalName').parent().addClass("has-error");
            $('#addHospitalName').next().text("医院名称必须为中文");
            $("#addHospitalName").next().show();
            flag = false;
        } else {
            $('#addHospitalName').parent().removeClass("has-error");
            $('#addHospitalName').next().text("");
            $("#addHospitalName").next().hide();
        }

        var provinceId = $.trim($("#addHospitalProvince option:selected").val());
        if (provinceId == -1) {
            $('#addHospitalProvince').parent().addClass("has-error");
            $('#addHospitalProvince').next().text("请选择省份（直辖市）");
            $("#addHospitalProvince").next().show();
            flag = false;
        } else {
            $('#addHospitalProvince').parent().removeClass("has-error");
            $('#addHospitalProvince').next().text("");
            $("#addHospitalProvince").next().hide();
        }


        var cityId = $.trim($("#addHospitalCity option:selected").val());
        if (cityId == -1 || cityId == "") {
            $('#addHospitalCity').parent().addClass("has-error");
            $('#addHospitalCity').next().text("请选择城市（区、县）");
            $("#addHospitalCity").next().show();
            flag = false;
        } else {
            $('#addHospitalCity').parent().removeClass("has-error");
            $('#addHospitalCity').next().text("");
            $("#addHospitalCity").next().hide();
        }


        return flag;
    }

});

function loadCity() {
    var postdata = "provinceId=";
    postdata = postdata + $.trim($('#addHospitalProvince option:selected').val());
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_findCityByProvinceID.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#addHospitalCity').html("<option disabled selected value></option>");
                for(var i = 0; i < data.length; i++) {
                    $("#addHospitalCity").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}


$('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
    location.reload();  	//刷新当前页面
});


function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

