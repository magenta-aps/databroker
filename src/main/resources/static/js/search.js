function doSearch(url, formfields, resultelement, loadingIndicator) {
    var params = {};
    for (var i=0; i<formfields.length; i++) {
        var el = formfields[i];
        if (el instanceof jQuery) {
            el = el.get(0);
        }
        params[el.name||el.id] = el.value;
    }
    if (loadingIndicator) {
        loadingIndicator.show();
    }
    $.ajax({
        type: "GET",
        url: url,
        data: params,
        dataType: "text",
        success: function(responseText, status, response) {
            if (status == "success") {
                if ($.isFunction(resultelement)) {
                    resultelement(responseText);
                } else if (resultelement instanceof jQuery) {
                    resultelement.text(responseText);
                    resultelement.show();
                }
            } else {
                if ($.isFunction(resultelement)) {
                    resultelement(null);
                } else if (resultelement instanceof jQuery) {
                    resultelement.text("");
                }
            }
        },
        complete: function() {
            if (loadingIndicator) {
                loadingIndicator.hide();
            }
        }
    });
}