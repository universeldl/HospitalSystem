function managePatient(id) {
    var patient_action = $.trim($("#patient_action").val()) + "?patientId=" + id;
    window.location.href = patient_action;
}

