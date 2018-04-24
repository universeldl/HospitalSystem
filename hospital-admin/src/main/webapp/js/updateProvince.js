$(function () {


    $('#updateProvince').click(function () {

        if (!isValidProvinceName()) {
            return;
        }

        var postdata = "provinceId=" + $.trim($("#updateProvinceId").val())
            + "&provinceName=" + $.trim($("#updateProvinceName").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_updateProvince.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("修改成功");
                    } else if (data == -2) {
                        $("#updateModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个省份了");
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



function isValidProvinceName() {
    var flag = true;

    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    var name = $.trim($("#updateProvinceName").val());
    if (name == "") {
        $('#updateProvinceName').parent().addClass("has-error");
        $('#updateProvinceName').next().text("请输入省份名称");
        $("#updateProvinceName").next().show();
        flag = false;
    } else if (!reg.test(name)) {
        $('#updateProvinceName').parent().addClass("has-error");
        $('#updateProvinceName').next().text("省份名称必须为中文");
        $("#updateProvinceName").next().show();
        flag = false;
    } else {
        $('#updateProvinceName').parent().removeClass("has-error");
        $('#updateProvinceName').next().text("");
        $("#updateProvinceName").next().hide();
    }

    return flag;
}

function updateProvince(id) {
    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_getProvinceById.action',
            params: "provinceId=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#updateProvinceId").val(data.id);
                $("#updateProvinceName").val(data.name);
            }
        }
    );


}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

