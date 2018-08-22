accessid = ''
accesskey = ''
host = ''
policyBase64 = ''
signature = ''
filename = ''
key = ''
expire = 0
now = timestamp = Date.parse(new Date()) / 1000;


//1.jsapi签名校验
function wxconfig() {
    var _config =  $.parseJSON($.trim($("#wechatConfig").val()));

    wx.config({
        beta: true,// 必须这么写，否则在微信插件有些jsapi会有问题
        debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
        appId: _config.appId, // 必填，公众号的唯一标识
        timestamp: _config.timeStamp, // 必填，生成签名的时间戳
        nonceStr: _config.nonceStr, // 必填，生成签名的随机串
        signature: _config.signature,// 必填，签名，见附录1
        jsApiList: ['checkJsApi', 'onMenuShareAppMessage',
            'onMenuShareWechat', 'startRecord', 'stopRecord',
            'onVoiceRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice',
            'uploadVoice', 'downloadVoice', 'chooseImage',
            'previewImage', 'uploadImage', 'downloadImage',
            'getNetworkType', 'openLocation', 'getLocation',
            'hideOptionMenu', 'showOptionMenu', 'hideMenuItems',
            'showMenuItems', 'hideAllNonBaseMenuItem',
            'showAllNonBaseMenuItem', 'closeWindow', 'scanQRCode',
            'previewFile', 'openEnterpriseChat',
            'selectEnterpriseContact', 'chooseInvoice']// 必填，需要使用的JS接口列表，所有JS接口列表见附录2
    });

    var uploader = new plupload.Uploader({
        runtimes : 'html5,flash,silverlight,html4',
        browse_button : 'selectfiles',
        container: document.getElementById('container'),
        flash_swf_url : 'lib/plupload-2.1.2/js/Moxie.swf',
        silverlight_xap_url : 'lib/plupload-2.1.2/js/Moxie.xap',

        url : 'http://oss.aliyuncs.com',

        init: {
            PostInit: function() {
                document.getElementById('ossfile').innerHTML = '';
                document.getElementById('postfiles').onclick = function() {
                    set_upload_param(uploader);
                    uploader.start();
                    return false;
                };
            },

            FilesAdded: function(up, files) {
                plupload.each(files, function(file) {
                    document.getElementById('ossfile').innerHTML += '<div id="' + file.id + '">' + file.name + ' (' + plupload.formatSize(file.size) + ')<b></b>'
                        +'<div class="progress"><div class="progress-bar" style="width: 0%"></div></div>'
                        +'</div>';
                });
            },

            UploadProgress: function(up, file) {
                var d = document.getElementById(file.id);
                d.getElementsByTagName('b')[0].innerHTML = '<span>' + file.percent + "%</span>";

                var prog = d.getElementsByTagName('div')[0];
                var progBar = prog.getElementsByTagName('div')[0]
                progBar.style.width= 2*file.percent+'px';
                progBar.setAttribute('aria-valuenow', file.percent);
            },

            FileUploaded: function(up, file, info) {
                console.log('uploaded')
                console.log(info.status)
                set_upload_param(up);
                if (info.status >= 200 || info.status < 200)
                {
                    document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = 'success';
                }
                else
                {
                    document.getElementById(file.id).getElementsByTagName('b')[0].innerHTML = info.response;
                }
            },

            Error: function(up, err) {
                set_upload_param(up);
                document.getElementById('console').appendChild(document.createTextNode("\nError xml:" + err.response));
            }
        }
    });

    uploader.init();
}


function set_upload_param(up)
{
    var _ossConfig = $.parseJSON($.trim($("#ossConfig").val()));
    host = _ossConfig.host;
    policyBase64 = _ossConfig.policy;
    accessid = _ossConfig.accessid;
    signature = _ossConfig.signature;
    expire = _ossConfig.expire;
    callbackbody = _ossConfig.callback;

    alert("oss host = " + host);
    /*    var ret = get_signature()
        if (ret == true)
        {
            alert("ret = true");*/

    new_multipart_params = {
        'key' : key + '${filename}',
        'policy': policyBase64,
        'OSSAccessKeyId': accessid,
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'signature': signature,
    };

    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });

    console.log('reset uploader')
    //uploader.start();
    /*
        }
    */
}

/*const fs = require('fs');
const request = require('require');
const OSS = require('ali-oss').Wrapper;
const ossClient = new OSS({
    accessKeyId: 'LTAIwCNNk4zI9NGM',
    accessKeySecret: '6yfIlHK2AdKyia1RgmOuohyOPfxSZZ',
    bucket: 'scmc-wechat',
    region: 'oss-cn-shanghai.aliyuncs.com'
});*/

//2.jsapi签名校验成功后执行ready
wx.ready(function(){
    // config信息验证后会执行ready方法，所有接口调用都必须在config接口获得结果之后，config是一个客户端的异步操作，所以如果需要在页面加载时就调用相关接口，则须把相关接口放在ready函数中调用来确保正确执行。对于用户触发时才调用的接口，则可以直接调用，不需要放在ready函数中。

    //2.1 提示jsapi签名验证成功
    $("#yanzheng").html("验证成功");

    $("#ceshi").click(function(){
        alert("ceshiaaa");

    });

    $("#checkJsApi").click(function(){
        wx.checkJsApi({
            jsApiList: [ 'checkJsApi', 'onMenuShareAppMessage',
                'onMenuShareWechat', 'startRecord', 'stopRecord',
                'onVoiceRecordEnd', 'playVoice', 'pauseVoice', 'stopVoice',
                'uploadVoice', 'downloadVoice', 'chooseImage',
                'previewImage', 'uploadImage', 'downloadImage',
                'getNetworkType', 'openLocation', 'getLocation',
                'hideOptionMenu', 'showOptionMenu', 'hideMenuItems',
                'showMenuItems', 'hideAllNonBaseMenuItem',
                'showAllNonBaseMenuItem', 'closeWindow', 'scanQRCode',
                'previewFile', 'openEnterpriseChat',
                'selectEnterpriseContact','chooseInvoice'

            ], // 需要检测的JS接口列表，所有JS接口列表见附录2,
            success: function(res) {
                // 以键值对的形式返回，可用的api值true，不可用为false
                // 如：{"checkResult":{"chooseImage":true},"errMsg":"checkJsApi:ok"}
            }
        });

    });



    //2.2 上传图片
    var images = {
        localId : [],
        serverId : []
    };
    $("#uploadImg").click(function(){
        //2.2.1拍照或从手机相册中选图
        alert("uploadImg clicked");
        wx.chooseImage({
            success : function(res) {
                images.localId = res.localIds;
                alert('已选择 ' + res.localIds.length + ' 张图片');
                //2.2.2 上传图片
                uploadImg();
            }
        });
    });

    // 2.2.2 上传图片
    function uploadImg() {
        if (images.localId.length == 0) {
            alert('请先使用 chooseImage 接口选择图片');
            return;
        }
        var i = 0, length = images.localId.length;
        images.serverId = [];

        function upload() {
            wx.uploadImage({
                localId : images.localId[i],
                isShowProgressTips: 1,
                success : function(res) {
                    i++;
                    alert('已上传：' + i + '/' + length);
                    images.serverId.push(res.serverId);
                    //将serverId上传至服务器
                    alert("ajax请求即将执行--");

                    alert("media id = " + res.serverId)


                    const accessToken = 'access token';
                    const mediaId = res.serverId; // 微信多媒体文件id
                    const destPath = `${mediaId}.jpg`; // OSS文件路径，按自己喜欢构造咯
                    const wxReq = request(`http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=${accessToken }&media_id=${mediaId}`);


                    /*                    $.ajax({
                                            type : "POST",
                                            url : "http://se9mxs.natappfree.cc/weixin_gz/uploadimg",
                                            data : {
                                                serverId : res.serverId
                                            },
                                            dataType : "text",
                                            success : function(data) {
                                                alert(data);
                                            }

                                        });*/


                    if (i < length) {
                        upload();
                    }
                },
                fail : function(res) {
                    alert(JSON.stringify(res));
                }
            });
        }
        upload();
    };

    //2.3 扫一扫
    $("#qrcode").click(function(){
        wx.scanQRCode({
            needResult: 1, // 默认为0，扫描结果由微信处理，1则直接返回扫描结果，
            scanType: ["qrCode","barCode"], // 可以指定扫二维码还是一维码，默认二者都有
            success: function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                alert(result);

            },
            fail:function (res) {
                var result = res.resultStr; // 当needResult 为 1 时，扫码返回的结果
                alert(result);

            }
        });

    });

    //2.4 拉起发票列表
    //注意，调用此接口时，config接口必须传入beta参数。
    $("#showInvoice").click(function(){
        alert("timestamp:"+invoice_config.timestamp);
        alert("nonceStr:"+invoice_config.nonceStr);
        alert("signType:"+invoice_config.signType);
        alert("cardSign:"+invoice_config.cardSign);

        wx.invoke('chooseInvoice', {
            'timestamp': invoice_config.timestamp, // 卡券签名时间戳
            'nonceStr' : invoice_config.nonceStr, // 卡券签名随机串
            'signType' : invoice_config.signType, // 签名方式，默认'SHA1'
            'cardSign' : invoice_config.cardSign, // 卡券签名
        }, function(res) {
            // 这里是回调函数
            alert("aaaaaa");
            alert(JSON.stringify(res)); // 返回的结果
        });

    });




});


//2.jsapi签名校验失败后执行error
wx.error(function(err){
    alert('wx error: ' + JSON.stringify(err));
    // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
});