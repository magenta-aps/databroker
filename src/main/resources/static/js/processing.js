var startProcessingListener = function(onStatus, uuid, idleInterval, runningInterval, startRunning) {
    idleInterval = idleInterval || 10000;
    runningInterval = runningInterval || 2000;

    var statusTimer = null,
        lastStatus = null,
        Status = {
            ENABLED: "ENABLED",
            DISABLED: "DISABLED",
            RUNNING: "RUNNING",
            PAUSED: "PAUSED"
        },
        lastWasError = false;
    var startTimer = function(interval) {
        stopTimer();
        statusTimer = setInterval(function () {
            $.ajax({
                type: "GET",
                url: "/dataproviders/processingStatus",
                data: {
                    uuid: uuid
                },
                dataType: "json",
                success: function (responseJSON, status, response) {
                    if (responseJSON != null) {
                        var newStatus = responseJSON.status;

                        if ($.isFunction(onStatus)) {
                            try {
                                onStatus(responseJSON);
                            } catch (e) {
                            }
                        }
                        if (newStatus != lastStatus) {
                            if ((lastStatus == Status.ENABLED) && (newStatus == Status.RUNNING || newStatus == Status.PAUSED)) {
                                // Go from an idle status to an active status
                                startTimer(runningInterval);
                            } else if ((lastStatus == Status.RUNNING || lastStatus == Status.PAUSED) && newStatus == Status.ENABLED) {
                                // Go from an active status to an idle status
                                startTimer(idleInterval);
                            } else if (newStatus == Status.DISABLED) {
                                stopTimer();
                            }
                            lastStatus = newStatus;
                        }
                    }
                    lastWasError = false;
                },
                error: function(){
                    if (lastWasError) {
                        stopTimer();
                    } else {
                        lastWasError = true;
                    }
                }
            });
        }, interval);
    };

    var stopTimer = function() {
        if (statusTimer != null) {
            clearInterval(statusTimer);
            statusTimer = null;
        }
    }

    startTimer(startRunning ? runningInterval : idleInterval);
};


