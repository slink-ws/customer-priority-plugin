(function(window) {
  window.$cupriAdmin = {
    updatePluginConfig: function(projectsField, rolesField, customIdField) {
      let projects = $cupriCommon.getSelectValuesStr(projectsField);
      let roles    = $cupriCommon.getSelectValuesStr(rolesField);
      let fieldId  = $cupriCommon.getInputValue(customIdField);
      AJS.$.ajax({
        url: $cupriCommon.baseUrl() + "/admin",
        type: "PUT",
        contentType: "application/json",
        data: '{ "projects": "' + projects + '", "roles": "' +  roles + '", "fieldId": "' + fieldId + '"}',
        processData: false
      }).done(function () {
        JIRA.Messages.showSuccessMsg("configuration saved")
      }).error(function (error, message) {
        JIRA.Messages.showErrorMsg("could not save configuration: <br><br>" + error.responseText)
      });
    }
  };
})(window);
