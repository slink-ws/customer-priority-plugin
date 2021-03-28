let $customerPriorityCommon = {
    restBaseUrl: AJS.contextPath() + "/rest/ws-slink-customer-priority/2.0",
    get_select_values_string: function(element) {
        let result = "";
        let arr = AJS.$("#" + element + " option:selected");
        for(let i = 0; i < arr.length; i++) {
            result += arr[i].value;
            if (i < arr.length - 1)
                result += ",";
        }
        return result;
    }
}
AJS.toInit(function() {
   // alert("ready!");
   // console.log("loaded common.js")
   // AJS.log("[CUSTOMER PRIORITY COMMON JS LOADED]");
});
