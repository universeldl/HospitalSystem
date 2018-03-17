function getPatientInfo(id) {

    $('#loading').show();
    ajax(
        {
            method: 'POST',
            url: 'doctor/patientManageAction_getPatient.action',
            params: "patientId=" + id,
            type: "json",
            callback: function (data) {
                $('#loading').hide();
                $("#findOpenID").val(data.openID);
                $("#findPatientName").val(data.name);
                $("#findEmail").val(data.email);
                $("#findPhone").val(data.phone);
                $("#findPlan").val(data.planId);
                $("#findPatientType").val(data.patientType.patientTypeName);
                $("#findDoctor").val(data.doctorName);
                $("#findAddnDoctor").val(data.addnDoctorName);
                if (data.sex == 1)
                    $("#findSex").val("男");
                else if (data.sex == 0)
                    $("#findSex").val("女");
            }
        }
    );
}
