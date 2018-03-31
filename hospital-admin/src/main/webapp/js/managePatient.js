function managePatient(id) {
    var patient_action = $.trim($("#patient_action").val()) + "?patientId=" + id;
    window.location.href = patient_action;
}
function allInOne(id) {
    var patient_allInOne = $.trim($("#patient_allInOne").val()) + "?patientId=" + id;
    window.location.href = patient_allInOne;
}
