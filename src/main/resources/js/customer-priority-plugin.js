let $customerPriority = {
    colorUrl: $cupriCommon.baseUrl() + "/color",
    focusedColor: '#DEEBFF',

    colorQuery: async function(path, issueKey) {
        return await jQuery.get($customerPriority.colorUrl + path + issueKey)
    },
    setIssueColor: function () {
        let issueKey = AJS.$("#currentIssueKey").val();
        if (issueKey) {
            $customerPriority.colorQuery("/issue/", issueKey)
                .then(function(config) {
                    if (config && config.color) {
                        AJS.$("#details-module").css("background-color", config.color);
                        AJS.$("#details-module").find("h4").css("background-color", config.color);
                    }
                })
                .catch(function(error) {
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
            })
        ;
    },
    setFocusedColor: function(item) {
        item.parentNode.style.backgroundColor = $customerPriority.focusedColor;
    },
    updateIssuesList: function () {
        AJS.$(".splitview-issue-link").each(function() {
            if (this.parentNode.className.split(' ').indexOf('focused')>=0) {
                $customerPriority.setFocusedColor(this);
            } else {
                $customerPriority.setListItemColor(this, this.href.substring(this.href.lastIndexOf('/') + 1));
            }
        })
    },

    issueRefreshed: function() {
        $customerPriority.setIssueColor();
    },
    pageLoaded: function() {
    },
}
AJS.toInit(function() {
    AJS.log("[CUSTOMER PRIORITY PLUGIN JS LOADED]");
    JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function(e, context, reason) {
        if (reason==JIRA.CONTENT_ADDED_REASON.pageLoad) {
            $customerPriority.updateIssuesList();
        }
    });
    JIRA.bind("issueRefreshed", function()  {
        $customerPriority.issueRefreshed();
    });
});
