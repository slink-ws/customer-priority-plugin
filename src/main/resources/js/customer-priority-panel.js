
function colorRequest() {
    var url = AJS.contextPath() + "/rest/customer-priority/1.0/color/" + JIRA.Issue.getIssueKey();
    $.ajax({
        url: url,
        dataType: "json"
    }).done(function(config) {
        console.log("~~~ GOT COLOR:");
        console.log(JSON.stringify(config));
        console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        if (null != config.color) {
            console.log("~~~ SET COLOR TO: " + config.color);
            console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            document.getElementById("details-module").style.backgroundColor = config.color;
            document.getElementById("details-module").getElementsByTagName("h4")[0].style.backgroundColor = config.color;
        }
    }).error(function (error) {
        console.log("~~~ ERROR QUERYING FOR COLOR: ");
        console.log("~~~ " + JSON.stringify(error));
        console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
    });
}

// JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function(e, context, reason) {
//     if (reason==JIRA.CONTENT_ADDED_REASON.pageLoad) {
//         // colorRequest();
//         console.warn("~~~~ page load: " + JIRA.Issue.getIssueKey());
//     }
// });

JIRA.bind("issueRefreshed", function() {
    console.warn("~~~~ issue refreshed: " + JIRA.Issue.getIssueKey());
    colorRequest();
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

/*
BEFORE_INLINE_EDIT_CANCEL: "inlineEditCancelled"
​CHECKBOXMULITSELECT_READY: "checkboxMultiSelectReady"
​INLINE_EDIT_BLURRED: "inlineEditBlurred"
​INLINE_EDIT_FOCUSED: "inlineEditFocused"
​INLINE_EDIT_REQUESTED: "inlineEditRequested"
​INLINE_EDIT_SAVE_COMPLETE: "inlineEditSaveComplete"
​INLINE_EDIT_STARTED: "inlineEditStarted"
​ISSUE_REFRESHED: "issueRefreshed"
​LAYOUT_RENDERED: "layoutRendered"
​LOCK_PANEL_REFRESHING: "lockPanelRefreshing"
​NEW_CONTENT_ADDED: "newContentAdded"
​NEW_PAGE_ADDED: "newPageAdded"
​PANEL_REFRESHED: "panelRefreshed"
​REFRESH_ISSUE_PAGE: "refreshIssuePage"
​REFRESH_TOGGLE_BLOCKS: "refreshToggleBlocks"
UNLOCK_PANEL_REFRESHING: "unlockPanelRefreshing"
VALIDATE_TIMETRACKING: "validateTimeTracking"
__amdModuleName: "jira/util/events/types"
*/