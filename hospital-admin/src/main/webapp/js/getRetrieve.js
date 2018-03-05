function checkRetrieve(id) {
    var check_retrieve = $.trim($("#check_retrieve").val()) + "?deliveryId=" + id;
    window.location.href = check_retrieve;
}

function showInfo(msg) {
    $("#div_info").text(msg);
    $("#modal_info").modal('show');
}

