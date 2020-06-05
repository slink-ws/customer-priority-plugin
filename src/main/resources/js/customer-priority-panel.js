
function colorRequest() {
    var url = AJS.contextPath() + "/rest/customer-priority/1.0/color/" + JIRA.Issue.getIssueKey();
    $.ajax({
        url: url,
        dataType: "json"
    }).done(function(config) {
        console.log("~~~ GOT COLOR:");
        console.log(JSON.stringify(config));
        console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        document.getElementById("details-module").style.backgroundColor = config.color;
        document.getElementById("details-module").getElementsByTagName("h4")[0].style.backgroundColor = config.color;
    }).error(function (error) {
        console.log("~~~ ERROR QUERYING FOR COLOR: ");
        console.log("~~~ " + JSON.stringify(error));
        console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        document.getElementById("details-module").style.backgroundColor = "#FFFFFF";
        document.getElementById("details-module").getElementsByTagName("h4")[0].style.backgroundColor = "#FFFFFF";
    });
}

JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function(e, context, reason) {
    if (reason==JIRA.CONTENT_ADDED_REASON.pageLoad) {
        colorRequest();
    }
});

(function ($) {
    var url = AJS.contextPath() + "/rest/customer-priority/1.0/color";
    $(document).ready(function() {
        colorRequest();
    });
})(AJS.$ || jQuery);









// require([
//     'jira/util/events',
//     'jira/util/events/types',
//     'jira/util/events/reasons'
// ], function(Events, EventTypes, EventReasons) {
//     "use strict";
//     Events.bind(EventTypes.NEW_CONTENT_ADDED, function (e, jiraContext, reason) {
//         alert("hello!")
//     })
// });


