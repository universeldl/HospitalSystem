function loadCity() {
    var postdata = "provinceId=";
    postdata = postdata + $.trim($('#province option:selected').val());
    $('#hospital').html('<option value="-1">请选择</option>');
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_findCityByProvinceID.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#city').html('<option value="-1">请选择</option>');

                for(var i = 0; i < data.length; i++) {
                    $("#city").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}


function loadHospital() {
    var postdata = "cityId=";
    postdata = postdata + $('#city option:selected').val();
    ajax(
        {
            method: 'POST',
            url: 'doctor/hospitalManageAction_findHospitalByCityId.action',
            type: "json",
            scriptCharset: 'utf-8',
            params: postdata,
            callback: function (data) {
                $('#hospital').html('<option value="-1">请选择</option>');
                for(var i = 0; i < data.length; i++) {
                    $("#hospital").append("<option value='"+data[i].id+"' >"+data[i].name+"</option>");
                }
            }
        }
    );
}
