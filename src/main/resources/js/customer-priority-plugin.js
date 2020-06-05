
var TEST_VALUE = 10101;

console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");
console.log("~~~ LOADING CUSTOM SCRIPT ~~~");


(function ($) {

    var url = AJS.contextPath() + "/rest/customer-priority/1.0/color";

    $(document).ready(function() {
        $.ajax({
            url: url,
            dataType: "json"
        }).done(function(config) {
            console.log("~~~ GOT COLOR:");
            console.log(JSON.stringify(config));
            console.log("~~~~~~~~~~~~~~~~~~~~~~~~~~");
            document.getElementById("details-module").style.backgroundColor = config.color;
            document.getElementById("details-module").getElementsByTagName("h4")[0].style.backgroundColor = config.color;
        });
    });

})(AJS.$ || jQuery);
