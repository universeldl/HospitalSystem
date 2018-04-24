$(function () {


    $('#addProvince').click(function () {

        if(!isValidProvinceName()) {
            return;
        }

        var postdata = "provinceName=" + $.trim($("#addProvinceName").val());
        $('#loading').show();
        ajax(
            {
                method: 'POST',
                url: 'doctor/hospitalManageAction_addProvince.action',
                params: postdata,
                callback: function (data) {
                    $('#loading').hide();
                    if (data == 1) {
                        $("#addProvinceModal").modal("hide");//关闭模糊框
                        showInfo("添加成功");
                    } else if (data == -1) {
                        $("#addProvinceModal").modal("hide");//关闭模糊框
                        showInfo("已经有这个省份，不能重复添加");
                    } else {
                        $("#addProvinceModal").modal("hide");//关闭模糊框
                        showInfo("添加失败");
                    }

                }
            }
        );


    });

    $('#modal_info').on('hide.bs.modal', function () {//提示模糊框隐藏时候触发
        location.reload();  	//刷新当前页面
    });


    function isValidProvinceName() {
        var flag = true;

        var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
        var name = $.trim($("#addProvinceName").val());
        if (name == "") {
            $('#addProvinceName').parent().addClass("has-error");
            $('#addProvinceName').next().text("请输入省份名称");
            $("#addProvinceName").next().show();
            flag = false;
        } else if (!reg.test(name)) {
            $('#addProvinceName').parent().addClass("has-error");
            $('#addProvinceName').next().text("省份名称必须为中文");
            $("#addProvinceName").next().show();
            flag = false;
        } else {
            $('#addProvinceName').parent().removeClass("has-error");
            $('#addProvinceName').next().text("");
            $("#addProvinceName").next().hide();
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

