(function ($) {
    $(document).ready(function() {
        $.ajax({
            url: AJS.contextPath() + "/rest/customer-priority/1.0/config/" + JIRA.API.Projects.getCurrentProjectKey(),
            dataType: "json"
        }).done(function(config) {
            $("#viewers").val(config.viewers);
            $("#style1").val(config.style1);
            $("#style2").val(config.style2);
            $("#style3").val(config.style3);
            $("#style4").val(config.style4);
            $("#text1").val(config.text1);
            $("#text2").val(config.text2);
            $("#text3").val(config.text3);
            $("#text4").val(config.text4);
            $("#list1").val(config.list1);
            $("#list2").val(config.list2);
            $("#list3").val(config.list3);
            $("#list4").val(config.list4);
            $("#color1").val(config.color1);
            $("#color2").val(config.color2);
            $("#color3").val(config.color3);
            $("#color4").val(config.color4);
            $("#viewers").val(config.viewers);

            $("#color-span-1").style.backgroundColor = config.color1;
            $("#color-span-2").style.backgroundColor = config.color2;
            $("#color-span-3").style.backgroundColor = config.color3;
            $("#color-span-4").style.backgroundColor = config.color4;
        });
    });

})(AJS.$ || jQuery);

function updateConfig() {
    AJS.$.ajax({
        url: AJS.contextPath() + "/rest/customer-priority/1.0/config/" + JIRA.API.Projects.getCurrentProjectKey(),
        type: "PUT",
        contentType: "application/json",
        processData: false,
        data:'{ ' +
            // '  "viewers":"'+ sanitize(AJS.$("#viewers").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            '  "style1":"' + sanitize(AJS.$("#style1").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"style2":"' + sanitize(AJS.$("#style2").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"style3":"' + sanitize(AJS.$("#style3").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"style4":"' + sanitize(AJS.$("#style4").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"text1": "' + sanitize(AJS.$("#text1").attr("value"), false)  + '"' +
            ' ,"text2": "' + sanitize(AJS.$("#text2").attr("value"), false)  + '"' +
            ' ,"text3": "' + sanitize(AJS.$("#text3").attr("value"), false)  + '"' +
            ' ,"text4": "' + sanitize(AJS.$("#text4").attr("value"), false)  + '"' +
            ' ,"list1":"'  + sanitize(AJS.$("#list1").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"list2":"'  + sanitize(AJS.$("#list2").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"list3":"'  + sanitize(AJS.$("#list3").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"list4":"'  + sanitize(AJS.$("#list4").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"color1":"' + sanitize(AJS.$("#color1").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"color2":"' + sanitize(AJS.$("#color2").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"color3":"' + sanitize(AJS.$("#color3").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"color4":"' + sanitize(AJS.$("#color4").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            ' ,"viewers":"'+ sanitize(AJS.$("#viewers").attr("value").replace(/\n/g, "\\n"), true) + '"' +
            '}',
    }).done(function () {
        JIRA.Messages.showSuccessMsg("configuration saved")
    }).error(function () {
        JIRA.Messages.showErrorMsg("could not save configuration")
    });
}

var ESC_MAP = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;'
};
function sanitize(s, forAttribute) {
    return s.replace(forAttribute ? /[&<>'"]/g : /[&<>]/g, function(c) {
        return ESC_MAP[c];
    });
}
