let $customerPriorityAdmin = {
    updateConfig: function() {
        let projects   = $customerPriorityCommon.get_select_values_string("projects");
        let roles      = $customerPriorityCommon.get_select_values_string("roles");
        let fieldId    = AJS.$("#custom-field-id-input").val();

        AJS.log("~~~ SAVING CONFIGURATION:");
        AJS.log("       projects: " + projects);
        AJS.log("       roles   : " + roles);
        AJS.log("       field id: " + fieldId);
        AJS.log("~~~~~~~~~~~~~~~~~~~~~~~~~~")
        AJS.$.ajax({
            url: $customerPriorityCommon.restBaseUrl + "/admin",
            type: "PUT",
            contentType: "application/json",
            data: '{ "projects": "' + projects + '", "roles": "' +  roles + '", "fieldId": "' + fieldId + '"}',
            processData: false
        }).done(function () {
            JIRA.Messages.showSuccessMsg("configuration saved")
        }).error(function (error, message) {
            // AJS.log("---------------------------------------------------");
            // AJS.log(error);
            // AJS.log("---------------------------------------------------");
            // AJS.log(message);
            // AJS.log("---------------------------------------------------");
            JIRA.Messages.showErrorMsg("could not save configuration: <br><br>" + error.responseText)
        });
    }
}
AJS.toInit(function() {
    // AJS.log("[CUSTOMER PRIORITY ADMIN JS LOADED]");
});