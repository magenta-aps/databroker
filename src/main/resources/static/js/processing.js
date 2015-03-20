var startProcessingListener = function(onStatus, uuid, idleInterval, runningInterval, startRunning) {
    idleInterval = idleInterval || 15000;
    runningInterval = runningInterval || 5000;

    var statusTimer = null,
        lastStatus = null,
        Status = {
            ENABLED: "ENABLED",
            DISABLED: "DISABLED",
            RUNNING: "RUNNING",
            PAUSED: "PAUSED"
        },
        lastWasError = false;
    var url = window.dataprovidersUrl + "processingStatus";

    var run = function () {
        $.ajax({
            type: "GET",
            url: url,
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
                rerun();
            },
            error: function(){
                if (lastWasError) {
                    stopTimer();
                } else {
                    lastWasError = true;
                    rerun();
                }
            }
        });
    };

    var keepRunning = false;
    var interval;

    var rerun = function(){
        if (keepRunning && !statusTimer) {
            statusTimer = setTimeout(function(){
                statusTimer = null;
                run();
            }, interval);
        }
    };

    var startTimer = function(int) {
        interval = int;
        stopTimer();
        statusTimer = setTimeout(function(){
            statusTimer = null;
            run();
        }, interval);
        keepRunning = true;
    };

    var stopTimer = function() {
        if (statusTimer != null) {
            clearTimeout(statusTimer);
            statusTimer = null;
        }
        keepRunning = false;
    }

    startTimer(startRunning ? runningInterval : idleInterval);
};


