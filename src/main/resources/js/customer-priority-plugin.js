let $customerPriority = {
    colorUrl: AJS.contextPath() + "/rest/ws-slink-customer-priority/2.0/color",
    focusedColor: '#DEEBFF',

    colorQuery: async function(path, issueKey) {
        return await jQuery.get($customerPriority.colorUrl + path + issueKey)
    },
    setIssueColor: function () {
        let issueKey = AJS.$("#currentIssueKey").val();
        if (issueKey) {
            console.log("----> set color for " + issueKey);
            $customerPriority.colorQuery("/issue/", issueKey)
                .then(function(config) {
                    if (config && config.color) {
                        AJS.$("#details-module").css("background-color", config.color);
                        AJS.$("#details-module").find("h4").css("background-color", config.color);
                    }
                })
                .catch(function(error) {
                    // $customerPriority.resetColor(issueKey);
                    // console.log("~~~~~~ ISSUE COLOR QUERY ERROR ~~~~~~");
                    // console.log(JSON.stringify(error, null, 2));
                    // console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
                })
            ;
        }
    },
    resetColor: function(item) {
        item.parentNode.style.backgroundColor = '';
    },
    setListItemColor: function(item, issueKey) {
        $customerPriority.colorQuery("/list/", issueKey)
            .then(function(config) {
                if (config && config.color) {
                    item.parentNode.style.backgroundColor = config.color;
                } else {
                    $customerPriority.resetColor(item);
                }
            })
            .catch(function(error) {
                $customerPriority.resetColor(item);
                // console.log("~~~~~~ LIST COLOR QUERY ERROR ~~~~~~");
                // console.log(JSON.stringify(error, null, 2));
                // console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
            })
        ;
    },
    setFocusedColor: function(item) {
        item.parentNode.style.backgroundColor = $customerPriority.focusedColor;
    },
    updateIssuesList: function () {
        // let issueLinks = document.getElementsByClassName('splitview-issue-link');
        AJS.$(".splitview-issue-link").each(function() {
            // console.log(this);
            if (this.parentNode.className.split(' ').indexOf('focused')>=0) {
                $customerPriority.setFocusedColor(this);
            } else {
                $customerPriority.setListItemColor(this, this.href.substring(this.href.lastIndexOf('/') + 1));
            }
        })
    },
}

JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function(e, context, reason) {
    if (reason==JIRA.CONTENT_ADDED_REASON.pageLoad) {
        $customerPriority.updateIssuesList();
    }
});
JIRA.bind("issueRefreshed", function()  {
    $customerPriority.setIssueColor();
});


// AJS.toInit(function() {
//    // alert("ready!");
//    // $customerPriority.setIssueColor();
// });
// JIRA.bind("layoutRendered", function()  {
//     setIssueColor();
// });
// (function ($) {
//     $(document).ready(function() {
//         $customerPriority.setIssueColor();
//     });
// })(AJS.$ || jQuery);

