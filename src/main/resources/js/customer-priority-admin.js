
(function ($) { // This pattern is known as an "iife" - immediately invoked function expression

    var url = AJS.contextPath() + "/rest/customer-priority/1.0/";

    $(document).ready(function() {
        $.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) {
            console.log("~~~ LOADING CONFIGURATION:")
            console.log(JSON.stringify(config));
            console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~")
            $("#projects").val(config.projects);
            $("#roles").val(config.roles);
        });
    });

})(AJS.$ || jQuery);

function updateConfig() {
    console.log("~~~ SAVING CONFIGURATION:");
    console.log("       projects: " + AJS.$("#projects").attr("value"));
    console.log("       roles   : " + AJS.$("#roles").attr("value"));
    console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~")
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/customer-priority/1.0/",
        type: "PUT",
        contentType: "application/json",
        data: '{ "projects": "' + AJS.$("#projects").attr("value") + '", "roles": "' +  AJS.$("#roles").attr("value") + '" }',
        processData: false
    });
}
