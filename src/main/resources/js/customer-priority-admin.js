
console.log("TEST MESSAGE");



(function ($) { // This pattern is known as an "iife" - immediately invoked function expression

    // form the URL
    var url = AJS.contextPath() + "/rest/customer-priority/1.0/";

    // wait for the DOM (i.e., document "skeleton") to load. This likely isn't necessary for the current case,
    // but may be helpful for AJAX that provides secondary content.
    $(document).ready(function() {
        // request the config information from the server
        $.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) { // when the configuration is returned...
            console.log("~~~ LOADING CONFIGURATION:")
            console.log(JSON.stringify(config));
            console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~")
            // ...populate the form.
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

// AJS.$("#adminForm").submit(function(e) {
//     alert("saving config");
//     e.preventDefault();
//     updateConfig();
// });