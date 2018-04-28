function registerSubmit() {
    if (!validLogin()) {
        return;
    }
    showLoadingToast("提交中...");

    var postdata = "username=" + replaceSpectialChar($.trim($("#username").val()));
    postdata = postdata + "&tel=" + $.trim($("#tel").val());
    postdata = postdata + "&sex=" + $.trim($("#sex option:selected").val());
    postdata = postdata + "&typeID=" + $.trim($("#patientType option:selected").val());
    postdata = postdata + "&hospitalID=" + $.trim($("#hospitallist option:selected").val());
    postdata = postdata + "&doctorID=" + $.trim($("#doctorlist option:selected").val());
    postdata = postdata + "&birthday=" + $.trim($("#birthday").val());
    postdata = postdata + "&captcha=" + $.trim($("#captchaIN").val());
    postdata = postdata + "&invitationCode=" + $.trim($("#invitationCode").val());
    postdata = postdata + "&oldPatient=" + $.trim($("#oldPatient").val());
    //var oldPatient = document.getElementById("oldPatient");
    var xhr = $.ajax(
        {
            method: 'POST',
            url: 'patientRegisterAction_register.action',
            data: postdata,
            timeout:20000,
            dataType: "text",
            success:function(data){ //请求成功的回调函数
                hideLoadingToast();
                if (data == 1) {
                    // 注册成功
                    window.location.href = "message.jsp?msg=注册完成！";
                } else if (data == -1) {
                    $('captchaIN').focus();
                    reloadCaptchaImg();
                    showDialog2("验证码错误", "确认")
                } else if (data == -2) {
                    $('invitationCode').focus();
                    reloadCaptchaImg();
                    showDialog2("邀请码错误", "确认")
                } else if (data == -4) {
                    showDialog2("用户已经存在，不能重复注册", "确认")
                } else {
                    // 登陆失败
                    reloadCaptchaImg();
                    showDialog2("登录失败，请重试", "确认");
                }
            },
            complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
                if(status=='timeout'){//超时,status还有success,error等值的情况
                    xhr.abort();    // 超时后中断请求
                    reloadCaptchaImg();
                    hideLoadingToast();
                    showDialog2("网络连接失败，请稍后再试", "确认");
                }
            }
        }
    );

}

function validLogin() {
    var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
    if ($('#username').val().length == 0) {
        $('#username').focus();
        showDialog2("请输入姓名", "确定");
        return false;
    }

    if ($('#tel').val().length == 0) {
        showDialog2("请输入电话号码", "确定");
        $('#tel').focus();
        return false;
    } else if (!(/^1\d{10}$/.test($.trim($('#tel').val())))) {
        showDialog2("电话号码有误", "确定");
        return false;
    }

    if ($('#sex option:selected').val().length == 0) {
        showDialog2("请选择宝宝性别", "确定");
        return false;
    }

    if ($('#patientType option:selected').val().length == 0) {
        showDialog2("请选择疾病类型", "确定");
        return false;
    }

    if ($('#oldPatient option:selected').val().length == 0) {
        showDialog2("请选择病例类型", "确定");
        return false;
    }
    if ($('#province option:selected').val().length == 0) {
        showDialog2("请选择所在省份", "确定");
        return false;
    }
    if ($('#citylist option:selected').val().length == 0) {
        showDialog2("请选择所在城市（区、县）", "确定");
        return false;
    }
    if ($('#hospitallist option:selected').val().length == 0) {
        showDialog2("请选择首诊医院", "确定");
        return false;
    }
    if ($('#doctorlist option:selected').val().length == 0) {
        showDialog2("请选择首诊医生", "确定");
        return false;
    }
    if ($('#birthday').val().length == 0) {
        showDialog2("请填写宝宝出生日期", "确定");
        return false;
    } else {
        var birthday = new Date(($("#birthday").val()).replace(/-/g,"/"));
        var curDate = new Date();
        if (birthday >= curDate) {
            showDialog2("请填写正确的出生日期", "确定");
            return false;
        }
    }
    if ($('#invitationCode').val().length == 0) {
        showDialog2("请填写邀请码", "确定");
        $('#captchaIN').focus();
        return false;
    }
    if ($('#captchaIN').val().length == 0) {
        showDialog2("请填写验证码", "确定");
        $('#captchaIN').focus();
        return false;
    }
    return true;
}

function checkLength(str1, str2, str3) {
    if (str1.length == 0) {
        showDialog2(str2, str3);
        return false;
    }
    return true;
}

function reloadCaptchaImg() {
    var timenow = new Date().getTime();
    var verify = document.getElementById('CAPTCHAIMG');
    verify.setAttribute('src', 'captchaAction_getCaptchaImg.action?d=' + timenow);

}

function loadCites() {
    var postdata = "provinceID=";
    postdata = postdata + $('#province option:selected').val();
    ajax(
        {
            method: 'POST',
            url: 'patientRegisterAction_findCityByProvinceID.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {

                $('#citylist').html("<option disabled selected value></option>");
                $('#hospitallist').html("<option disabled selected value>请先选择城市（区、县）</option>");
                $('#doctorlist').html("<option disabled selected value>请先选择首诊医院</option>");

                for(var i = 0; i < data.length; i++) {
                    $("#citylist").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}

function loadHospitals() {
    var postdata = "cityID=";
    postdata = postdata + $('#citylist option:selected').val();
    ajax(
        {
            method: 'POST',
            url: 'patientRegisterAction_findHospitalByCityID.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#hospitallist').html("<option disabled selected value></option>");
                $('#doctorlist').html("<option disabled selected value>请先选择首诊医院</option>");
                for(var i = 0; i < data.length; i++) {
                    $("#hospitallist").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}

function loadDoctors() {
    var postdata = "hospitalID=";
    postdata = postdata + $('#hospitallist option:selected').val();
    ajax(
        {
            method: 'POST',
            url: 'patientRegisterAction_findDoctorsByHospital.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#doctorlist').html("<option disabled selected value></option>");
                for(var i = 0; i < data.length; i++) {
                    $("#doctorlist").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}

function showDialog2(str1, str2) {
    var $dialog = $('#iosDialog2');
    $("#dialog2Str1").html(str1);
    $("#dialog2Str2").html(str2);
    $dialog.fadeIn(200);

    $dialog.on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });
}

function showToast(str) {
    var $toast = $('#toast');
    if ($toast.css('display') != 'none') return;
    $("#toastStr").html(str);
    $toast.fadeIn(100);
    setTimeout(function () {
        $toast.fadeOut(100);
    }, 2000);

}

function showLoadingToast(str) {
    var $loadingToast = $('#loadingToast');
    if ($loadingToast.css('display') != 'none') {
        return;
    }
    $("#loadToastStr").html(str);
    $loadingToast.fadeIn(100);
}

function hideLoadingToast() {
    $('#loadingToast').fadeOut(100);
}

