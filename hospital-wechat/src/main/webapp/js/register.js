function reloadCaptchaImg() {
    var timenow = new Date().getTime();
    var verify=document.getElementById('CAPTCHA');
    verify.setAttribute('src','captchaAction_getCaptchaImg.action?d='+timenow);

}

