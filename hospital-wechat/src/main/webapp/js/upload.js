accessid = ''
accesskey = ''
host = ''
policyBase64 = ''
signature = ''
callbackbody = ''
/*
filename = ''
*/
key = ''
expire = 0
g_object_name = ''
var quesitonIndex = 0;
var questions;
var postdata = "";
var filenames = [];
var uploaded_count = 0;

function getQuestions() {
    questions = $.parseJSON($.trim($("#questionStr").val()));
    $("#startButton").fadeIn();
}

function questionStart() {
    $('#surveyName').hide();
    $('#surveyDescription').hide();
    $('#startButton').hide();
    displayPage(quesitonIndex);
}

function displayPage(index) {
    var showIndex = index + 1;
    var question = questions[index];

    $('#questionIndex').text("问题（" + showIndex + "/" + questions.length + "）").show();
    $('#questionContent').text(question.questionContent).show();

    displayUpload();
    displayButton(index);
}

function displayUpload(){
    document.getElementById("uploaderdiv").style.display = "";
    $("#selectfiles").fadeIn();

    $("#file-list").each(function(){
        var y = $(this).children();
        y.remove();
    });
    filenames = [];

}

function next(str) {
    location.href = "#top";
    postdata = postdata + "&tq" + questions[quesitonIndex].questionId + "=" + str;

    if (quesitonIndex == questions.length -1) {
        submit();
    } else {
        quesitonIndex = quesitonIndex + 1;
        displayPage(quesitonIndex);
    }
}

function submit() {
    showLoadingToast("提交答案...");

    var po = "deliveryID=" + $.trim($("#deliveryID").val()) + "&";
    po = po + postdata;
    var xhr = $.ajax({
        method: 'POST',
        url: 'surveyAction_retrieveAnswer.action',
        data: po,
        dataType: "json",
        timeout:20000,
        scriptCharset: 'utf-8',
        success:function(data){ //请求成功的回调函数
            hideLoadingToast();
            if (data.success == 1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷提交成功!<br/><br/>" + data.msg;
                window.location.href = url;
            } else if (data.success == -1) {
                var url =  "https://" + window.location.host;
                url = url + "/hospital-wechat/message.jsp?msg=答卷出错...";
                window.location.href = url;
            } else {
                showDialog2("提交失败，请稍后再试", "确认");
            }
        },
        complete : function(XMLHttpRequest,status){ //请求完成后最终执行参数
            if(status=='timeout'){//超时,status还有success,error等值的情况
                xhr.abort();    // 超时后中断请求
                hideLoadingToast();
                showDialog2("答卷提交失败，请稍后再试", "确认");
            }
        }
        /*         callback: function (data) {
                     hideLoadingToast();
                     if (data.success == 1) {
                         var url =  "https://" + window.location.host;
                         url = url + "/hospital-wechat/message.jsp?msg=答卷提交成功!<br/><br/>" + data.msg;
                         window.location.href = url;
                     } else if (data.success == -1) {
                         var url =  "https://" + window.location.host;
                         url = url + "/hospital-wechat/message.jsp?msg=答卷出错...";
                         window.location.href = url;
                     } else {
                        showDialog2("提交失败，请稍后再试", "确认");
                     }
                 }*/
    });

}

function displayButton(index) {
    if (index == questions.length - 1) {
        $("#selectfiles").show();
        document.getElementById("nextButton").innerHTML = "提交"
        document.getElementById("nextButton").style.display = "";
    } else {
        $("#selectfiles").show();
        document.getElementById("nextButton").innerHTML = "下一题"
        document.getElementById("nextButton").style.display = "";
    }
}

function get_signature()
{
    var _ossConfig = $.parseJSON($.trim($("#ossConfig").val()));
    host = _ossConfig.host;
    policyBase64 = _ossConfig.policy;
    accessid = _ossConfig.accessid;
    signature = _ossConfig.signature;
    expire = _ossConfig.expire;
    callbackbody = _ossConfig.callback;
    key = "";
    return true;
};

function get_suffix(filename) {
    pos = filename.lastIndexOf('.')
    suffix = ''
    if (pos != -1) {
        suffix = filename.substring(pos)
    }
    return suffix;
}

function calculate_object_name(filename)
{
    suffix = get_suffix(filename)
    var deliveryId = document.getElementById("deliveryID").value
    g_object_name = deliveryId + "_" + quesitonIndex + "_" +filenames.length;
    return ''
}

/*function get_uploaded_object_name(filename)
{
    return g_object_name
}*/

function set_upload_param(up, filename, ret)
{
    if (ret == false)
    {
        ret = get_signature()
    }
    g_object_name = key;
    if (filename != '') {
        suffix = get_suffix(filename)
        calculate_object_name(filename)
    }
    new_multipart_params = {
        'key' : g_object_name,
        'policy': policyBase64,
        'OSSAccessKeyId': accessid,
        'success_action_status' : '200', //让服务端返回200,不然，默认会返回204
        'callback' : callbackbody,
        'signature': signature,
    };

    up.setOption({
        'url': host,
        'multipart_params': new_multipart_params
    });
    up.start();
}

var uploader = new plupload.Uploader({
    runtimes : 'html5,flash,silverlight,html4',
    browse_button : 'selectfiles',
    container: document.getElementById('container'),
    flash_swf_url : 'lib/plupload/js/Moxie.swf',
    silverlight_xap_url : 'lib/plupload/js/Moxie.xap',
    url : 'https://oss.aliyuncs.com',

    filters: {
        mime_types : [
            { title : "Image files", extensions : "image/*,jpg,jpeg,png,gif" },
        ],
        max_file_size : '10mb', //最大只能上传10mb的文件
        prevent_duplicates : true
    },

    init: {
        PostInit: function() {
            document.getElementById('ossfile').innerHTML = '';
            document.getElementById('nextButton').onclick = function() {
                set_upload_param(uploader, '', false);
                return false;
            };
        },

        FilesAdded: function(up, files) {
            var len = len = files.length;
            for(var i = 0; i<len; i++){
                var html = '<li class="weui-uploader__file" id="file-' + files[i].id +'" style=""></li>';
                $(html).appendTo('#file-list');
                !function(i){
                    previewImage(files[i],function(imgsrc){
                        document.getElementById('file-'+files[i].id).style.cssText = "background-image:url("+imgsrc+")"
                    })
                }(i);
            }
        },

        FilesRemoved: function(up, files) {
            var len = len = files.length;
            for(var i = 0; i<len; i++) {
                document.getElementById('file-'+files[i].id).remove();
            }
        },

        BeforeUpload: function(up, file) {
            set_upload_param(up, file.name, true);
            showLoadingToast("图片上传中...");
        },

        UploadProgress: function(up, file) {
            document.getElementById('file-'+file.id).setAttribute("class",
                "weui-uploader__file weui-uploader__file_status");
            document.getElementById('file-'+file.id).innerHTML =
                '<div class="weui-uploader__file-content">' + file.percent + '%</div>';
        },

        FileUploaded: function(up, file, info) {
            if (info.status == 200)
            {
                filenames.push(g_object_name);
            }
            else
            {
                //alert(info.response)
            }
        },

        UploadComplete: function(up, files) {
            if (files.length > uploaded_count) {
                hideLoadingToast();
                next(filenames.join(";"));
                uploaded_count = files.length;
            } else {
                showDialog1("本题不需要上传照片吗？","需要","不需要");
            }
        },

        Error: function(up, err) {
            if (err.code == -600) {
                showDialog2("所选图片过大，请勿超过10M:" + err.file.name, "确定");
            } else if (err.code == -601) {
                showDialog2("所选文件不是图片:" + err.file.name, "确定");
            } else if (err.code == -602) {
                showDialog2("请勿重复上传图片:" + err.file.name, "确定");
            } else {
                showDialog2("图片\'"+err.file.name+"\'上传失败，请重试", "确定");
                document.getElementById('file-'+err.file.id).setAttribute("class",
                    "weui-uploader__file weui-uploader__file_status");
                document.getElementById('file-'+err.file.id).innerHTML =
                    '<div class="weui-uploader__file-content">' +
                        '<i class="weui-icon-warn"></i>' +
                    '</div>'
            }
            hideLoadingToast();
        }
    }
});


function previewImage(file,callback){
    if(!file || !/image\//.test(file.type)) return; //确保文件是图片
    if(file.type=='image/gif'){ //gif使用FileReader进行预览,因为mOxie.Image只支持jpg和png
        var gif = new moxie.file.FileReader();
        gif.onload = function(){
            callback(gif.result);
            gif.destroy();
            gif = null;
        };
        gif.readAsDataURL(file.getSource());
    }else{
        var image = new moxie.image.Image();
        image.onload = function() {
            //image.downsize( 300, 300 );//先压缩一下要预览的图片,宽300，高300
            var imgsrc = image.type=='image/jpeg' ? image.getAsDataURL('image/jpeg',80) : image.getAsDataURL(); //得到图片src,实质为一个base64编码的数据
            callback && callback(imgsrc); //callback传入的参数为预览图片的url
            image.destroy();
            image = null;
        };
        image.load( file.getSource() );
    }
}

$("#file-list").on("click", "li", function(){
    $("#galleryImg").attr("style", this.getAttribute("style"));
    $("#gallery").fadeIn(100);
    $("#curFileName").val(this.getAttribute("id"));
});

$("#gallery").on("click", function(){
    $("#gallery").fadeOut(100);
    $("#curFileName").val("");
});

$("#galleryImgDel").on("click", function(){
    $("#gallery").fadeOut(100);
    var remove_file_name = $("#curFileName").val().slice(5);
    uploader.removeFile(remove_file_name);
});

function showDialog2(str1, str2) {
    var $dialog = $('#iosDialog2');
    $("#dialog2Str1").html(str1);
    $("#dialog2Str2").html(str2);
    $dialog.fadeIn(200);

    $dialog.on('click', '.weui-dialog__btn', function () {
        $(this).parents('.js_dialog').fadeOut(200);
    });
}

function showDialog1(str1, str2,str3) {
    var $dialog = $('#iosDialog1');
    $("#dialog1Str1").html(str1);
    $("#dialog1Str2").html(str2);
    $("#dialog1Str3").html(str3);

    $dialog.fadeIn(200);
}

$("#dialog1Str2").on('click', function () {
    $(this).parents('.js_dialog').fadeOut(200);
});

$("#dialog1Str3").on('click', function () {
    $(this).parents('.js_dialog').fadeOut(200);
    next("");
});



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

uploader.init();
