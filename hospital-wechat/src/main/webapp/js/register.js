function registerSubmit() {
    if (!validLogin()) {
        return;
    }
    showLoadingToast("提交中...");

    var postdata = "username="+$.trim($("#username").val());
    postdata = postdata +"&tel="+ $.trim($("#tel").val());
    postdata = postdata +"&sex="+ $.trim($("#sex option:selected").val());
    postdata = postdata +"&hospital="+ $.trim($("#hospital option:selected").val());
    postdata = postdata +"&doctor="+ $.trim($("#doctor option:selected").val());
    postdata = postdata +"&birthday="+ $.trim($("#birthday").val());
    postdata = postdata +"&captcha="+ $.trim($("#captchaIN").val());
    postdata = postdata +"&typeID=1";
    ajax(
        {
            method:'POST',
            url:'patientRegisterAction_register.action',
            params: postdata,
            callback:function(data) {
                hideLoadingToast();

                if (data == 1) {
                    // 注册成功
                    window.location.href = "helloworld.jsp";
                } else if (data == -1) {
                    $('captchaIN').focus();
                    reloadCaptchaImg();
                    showDialog2("验证码错误", "确认")
                } else {
                    // 登陆失败
                    reloadCaptchaImg();
                    showDialog2("登录失败，请重试", "确认");
                }
            }
        }

    );

}

function validLogin() {
    var reg = new RegExp("[\\u4E00-\\u9FFF]+","g");
    if ($('#username').val().length == 0) {
        $('#username').focus();
        return false;
    }else if(!reg.test($.trim($('#username').val()))){
        showDialog2("姓名必须为中文", "确定");
        $('#username').focus();
        return false;
    }

    if ($('#tel').val().length == 0) {
        $('#tel').focus();
        return false;
    }else if(!(/^1[34578]\d{9}$/.test($.trim($('#tel').val())))){
        showDialog2("电话号码有误", "确定");
        $('#tel').focus();
        return false;
    }

    if ($('#sex option:selected').val().length == 0) {
        showDialog2("请选择宝宝性别", "确定");
        return false;
    }

    if ($('#hospital option:selected').val().length == 0) {
        showDialog2("请选择首诊医院", "确定");
        return false;
    }
    if ($('#doctor option:selected').val().length == 0) {
        showDialog2("请选择首诊医生", "确定");
        return false;
    }
    if ($('#birthday').val().length == 0) {
        showDialog2("请填写宝宝出生日期", "确定");
        return false;
    } else {
        // check correctness of birthday
    }

    if ($('#captchaIN').val().length == 0) {
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
    var verify=document.getElementById('CAPTCHAIMG');
    verify.setAttribute('src','captchaAction_getCaptchaImg.action?d='+timenow);

}

function showDialog2(str1, str2) {
    var $dialog = $('#iosDialog2');
    $("#dialog2Str1").html(str1);
    $("#dialog2Str2").html(str2);
    $dialog.fadeIn(200);

    $dialog.on('click', '.weui-dialog__btn', function(){
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
