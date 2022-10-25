(function(window) {
  window.$cupriCommon = {
    pluginUrlPath: "/rest/ws-slink-customer-priority/2.0",
    baseUrl: function () {
      return AJS.contextPath() + this.pluginUrlPath;
    },
    getSelectValuesStr: function (element) {
      let result = "";
      let arr = AJS.$("#" + element + " option:selected");
      for (let i = 0; i < arr.length; i++) {
        result += arr[i].value;
        if (i < arr.length - 1)
          result += ",";
      }
      return result;
    },
    getInputValue: function(element) {
      return AJS.$("#" + element).val();
    },
    getProjectKey: function () {
      return AJS.$('meta[name=projectKey]').attr('content');
    }
  };
})(window);
