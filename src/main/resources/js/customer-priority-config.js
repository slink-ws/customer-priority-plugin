let $customerPriorityConfig = {

    ESC_MAP: {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    },

    deleteStyleConfirmation: function(styleId) {
        AJS.$("#style-id-to-delete").html(styleId);
        $customerPriorityConfig.deleteConfirmationDialog = AJS.dialog2("#delete-confirm-dialog");
        $customerPriorityConfig.deleteConfirmationDialog.show();
    },
    confirmDialogCancel: function() {
        $customerPriorityConfig.deleteConfirmationDialog.hide();
        $customerPriorityConfig.deleteConfirmationDialog = null;
    },
    confirmDialogSubmit: function() {
        let styleId = AJS.$("#style-id-to-delete").html();
        // let projectKey = AJS.$('meta[name=projectKey]').attr('content'); //AJS.Meta.get("projectKey");//AJS.$("#projectKey").val();
        // console.log("----> removing " + styleId + " from " + $customerPriorityConfig.getProjectKey());
        AJS.$.ajax({
            url: $common.restBaseUrl + "/styles/" + $customerPriorityConfig.getProjectKey() + "/" + encodeURIComponent(styleId),
            type: "DELETE",
            contentType: "application/json",
            processData: false
        }).done(function () {
            JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-remove.success'));
            $customerPriorityConfig.removeStyle(styleId);
        }).error(function (error, message) {
            AJS.log("[style remove] error");
            AJS.log(error);
            AJS.log(message);
            JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.style-remove.error'))
        });
        $customerPriorityConfig.deleteConfirmationDialog.hide();
        $customerPriorityConfig.deleteConfirmationDialog = null;
    },

    editDialogShow: function(styleId) {
        AJS.$("#style-id-to-delete").html(styleId);
        let row = $customerPriorityConfig.getStyleRowElement(styleId);
        let styleTitle  = row.find(".styles-list-title").attr("title");
        let glanceStyle = row.find(".styles-list-glance-style").attr("style");
        let glanceText  = row.find(".styles-list-glance-style").text();
        let issueColor  = $customerPriorityConfig.rgb2hex(row.find(".styles-list-issue-color").css("background-color"));
        let listColor   = $customerPriorityConfig.rgb2hex(row.find(".styles-list-list-color").css("background-color"));
        let reporters   = row.find(".style-list-reporters").val();
        if (issueColor == "#000000" || issueColor.toUpperCase() == "#FFFFFF") issueColor = "";
        if (listColor == "#000000"  || listColor.toUpperCase()  == "#FFFFFF")  listColor = "";
        AJS.$("#edit-style-id").val(styleId);
        AJS.$("#edit-style-id").prop("disabled", true);
        AJS.$("#edit-style-title").val(styleTitle);
        AJS.$("#edit-glance-style").val(glanceStyle);
        AJS.$("#edit-glance-text").val(glanceText.trim());
        AJS.$("#edit-color-issue").val(issueColor);
        AJS.$("#edit-color-issue").css("background-color", issueColor);
        AJS.$("#edit-color-list").val(listColor);
        AJS.$("#edit-color-list").css("background-color", listColor);
        AJS.$("#edit-reporters").val(reporters);
        $customerPriorityConfig.editStyleDialog = AJS.dialog2("#style-edit-dialog");
        $customerPriorityConfig.editStyleDialogSubmitCallback = $customerPriorityConfig.editDialogSubmit;
        $customerPriorityConfig.editStyleDialog.show();
    },
    editDialogCancel: function() {
        $customerPriorityConfig.resetStyleForm();
        $customerPriorityConfig.editStyleDialog.hide();
        $customerPriorityConfig.editStyleDialog = null;
    },
    editDialogSubmit: function() {
        let request = $customerPriorityConfig.getStyleFormData();
        AJS.$.ajax({
            url: $common.restBaseUrl + "/styles/" + $customerPriorityConfig.getProjectKey(),
            type: "PUT",
            contentType: "application/json",
            data: JSON.stringify(request, null, 2),
            processData: false
        }).done(function () {
            JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-update.success'));
            $customerPriorityConfig.updateStyle(request);
            $customerPriorityConfig.resetStyleForm();
            $customerPriorityConfig.editStyleDialog.hide();
            $customerPriorityConfig.editStyleDialog = null;
        }).error(function (error, message) {
            AJS.log("[style update] error");
            AJS.log(error);
            AJS.log(message);
            JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.style-update.error'))
        });
    },

    createDialogShow: function() {
        $customerPriorityConfig.resetStyleForm();
        $customerPriorityConfig.editStyleDialog = AJS.dialog2("#style-edit-dialog");
        $customerPriorityConfig.editStyleDialogSubmitCallback = $customerPriorityConfig.createDialogSubmit;
        $customerPriorityConfig.editStyleDialog.show();
    },
    createDialogSubmit: function() {
        let request = $customerPriorityConfig.getStyleFormData();
        // console.log("create style: " + JSON.stringify(request));
        AJS.$.ajax({
            url: $common.restBaseUrl + "/styles/" + $customerPriorityConfig.getProjectKey(),
            type: "POST",
            contentType: "application/json",
            data: JSON.stringify(request, null, 2),
            processData: false
        }).done(function () {
            JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-create.success'));
            // $customerPriorityConfig.addStyle(request);
            // $customerPriorityConfig.resetStyleForm();
            $customerPriorityConfig.editStyleDialog.hide();
            $customerPriorityConfig.editStyleDialog = null;
            window.onbeforeunload = null;
            location.replace(location.pathname);
        }).error(function (error, message) {
            AJS.log("[style create] error");
            AJS.log(error);
            AJS.log(message);
            JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.style-create.error'))
        });
    },

    saveViewers: function() {
        let viewers = $customerPriorityConfig.sanitize(AJS.$("#viewers-input").val().replaceAll(","," ").replaceAll(";", " ")).split(" ");
        $.ajax({
            url        : $common.restBaseUrl + "/viewers/" + $customerPriorityConfig.getProjectKey(),
            type       : "POST",
            data       : JSON.stringify(viewers),
            contentType: "application/json; charset=utf-8",
            dataType   : "json",
            success    : function(data) {
                JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.viewers-save.success'));
                let viewersUpdated = "";
                data.forEach(function(item) {
                    viewersUpdated += item + " ";
                })
                AJS.$("#viewers-input").val(viewersUpdated.trim());
            },
            error      : function(XMLHttpRequest, message, error) {
                AJS.log("[viewers set] error");
                AJS.log(JSON.stringify(error, null ,2));
                AJS.log(message);
                JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.viewers-save.error'))
            }
        });
    },

    resetStyleForm: function() {
        AJS.$("#edit-style-id").val("");
        AJS.$("#edit-style-id").prop("disabled", false);
        AJS.$("#edit-style-title").val("");
        AJS.$("#edit-glance-style").val("");
        AJS.$("#edit-glance-text").val("");
        AJS.$("#edit-color-issue").val("");
        AJS.$("#edit-color-issue").css("background-color", "");
        AJS.$("#edit-color-list").val("");
        AJS.$("#edit-color-list").css("background-color", "");
        AJS.$("#edit-reporters").val("");
    },
    getStyleFormData: function() {
        let request          = {};
        request.style        = {};
        request.reporters    = [];
        request.id           = $customerPriorityConfig.sanitize(AJS.$("#edit-style-id").val().trim(), false);
        request.title        = $customerPriorityConfig.sanitize(AJS.$("#edit-style-title").val().trim(), false);
        request.text         = $customerPriorityConfig.sanitize(AJS.$("#edit-glance-text").val().trim(), false);
        request.style.glance = $customerPriorityConfig.sanitize(AJS.$("#edit-glance-style").val().trim(), true);
        request.style.issue  = $customerPriorityConfig.sanitize(AJS.$("#edit-color-issue").val().trim(), true);
        request.style.list   = $customerPriorityConfig.sanitize(AJS.$("#edit-color-list").val().trim(), true);
        if (request.style.issue == "#000000" || request.style.issue == "#FFFFFF" ) request.style.issue = "";
        if (request.style.list  == "#000000" || request.style.list  == "#FFFFFF" ) request.style.list  = "";
        $customerPriorityConfig.sanitize(AJS.$("#edit-reporters").val().trim(), false)
            .replaceAll(",", " ")
            .replaceAll(";", " ")
            .split(" ")
            .forEach(function(item) {
                request.reporters.push(item.trim());
        });
        return request;
    },
    getStyleRowElement: function(styleId) {
        return  $(".styles-list-row .styles-list-title").filter(function() {
            return $(this).text() == styleId;
        }).parent();
    },
    addStyle: function(style) {
        // console.log("adding style " + style.id);
        AJS.$("#styles-list").append(this.makeStyleRow(style));
        $customerPriorityConfig.sortStyles();
    },
    updateStyle: function(style) {
        // console.log("updating style " + style.id);
        let row = $customerPriorityConfig.getStyleRowElement(style.id);
        if (row) {
            row.find(".styles-list-title").attr("title", style.title);
            row.find(".styles-list-glance-style").attr("style", style.style.glance);
            row.find(".styles-list-glance-style").text(style.text);
            row.find(".styles-list-issue-color").css("background-color", this.fixColor(style.style.issue));
            row.find(".styles-list-list-color").css("background-color", this.fixColor(style.style.list));
            row.find(".style-list-reporters").val(style.reporters);
        }
    },
    removeStyle: function(styleId) {
        // console.log("removing style " + styleId);
        $customerPriorityConfig.getStyleRowElement(styleId).remove();
    },
    getProjectKey: function() {
        return AJS.$('meta[name=projectKey]').attr('content');
    },
    rgb2hex: function (rgb) {
        if (!rgb) {
            return "";
        }
        if (  rgb.search("rgb") == -1 ) {
            return rgb;
        } else {
            rgb = rgb.match(/^rgba?\((\d+),\s*(\d+),\s*(\d+)(?:,\s*(\d+))?\)$/);
            function hex(x) {
                return ("0" + parseInt(x).toString(16)).slice(-2);
            }
            return "#" + hex(rgb[1]) + hex(rgb[2]) + hex(rgb[3]);
        }
    },
    fixColor: function(color) {
        console.log("---> fix color '" + color + "'");
        if (color.trim() == "" || color.trim() == "#")
            return "";
        if (!color.startsWith("#"))
              return "#" + color;
        else
              return color;
    },
    sanitize: function (s, forAttribute) {
        return s.replace(forAttribute ? /[&<>'"]/g : /[&<>]/g, function(c) {
            return $customerPriorityConfig.ESC_MAP[c];
        });
    },
    reportersStr: function(reporters) {
        let result = "";
        reporters.forEach(function(item) {
            result += item + " ";
        });
        // console.log("----> reporters    : " + reporters);
        // console.log("----> reporters str: " + result);
        return result.trim();
    },
    sortStyles: function() {
        let styles = this.getStyles();
        let stylesSorted = styles.sort(function(a, b) {
           if (a.id > b.id)
               return 1;
           else if (a.id < b.id)
               return -1;
           else
               return 0;
        });
        // console.log(JSON.stringify(styles, null, 2));
        AJS.$("#styles-list").html("");
        stylesSorted.forEach(function(style) {
            AJS.$("#styles-list").append($customerPriorityConfig.makeStyleRow(style));
        });
    },
    getStyles: function() {
        let styles = [];
        AJS.$(".styles-list-title").each(function(index, item) {
            styles.push($customerPriorityConfig.getStyle($(item).text()));
        });
        return styles;
    },
    getStyle: function(styleId) {
        let row = $customerPriorityConfig.getStyleRowElement(styleId);
        let style = {};
        style.reporters = [];
        style.style = {};
        if (row) {
            style.id           = styleId;
            style.title        = row.find(".styles-list-title").attr("title");
            style.style.glance = row.find(".styles-list-glance-style").attr("style").trim();
            style.text         = row.find(".styles-list-glance-style").text().trim();
            style.style.issue  = $customerPriorityConfig.rgb2hex(row.find(".styles-list-issue-color").css("background-color"));
            style.style.list   = $customerPriorityConfig.rgb2hex(row.find(".styles-list-list-color").css("background-color"));
            AJS.$("#edit-reporters").val()
                .split(" ")
                .forEach(function(item) {
                    style.reporters.push(item);
                });
            if (style.style.issue == "#000000")
                style.style.issue = "";
            if (style.style.list == "#000000")
                style.style.list = "";
        }
        return style;
    },
    makeStyleRow: function(style) {
        let styleRow = "";

        styleRow += '<div class="styles-list-row">';
        styleRow += '<div class="styles-list-item styles-list-title" title="' + style.title + '">' + style.id + '</div>';

        styleRow += '<div className="styles-list-item styles-list-glance-style-wrapper">';
        styleRow += '<div class="styles-list-glance-style" style="' + style.style.glance + '" width="100%">';
        styleRow += style.text + "&nbsp;";
        styleRow += '</div></div>';
        // styleRow += '<div class="styles-list-item styles-list-glance-style" style=' + style.style.glance + '>';
        // styleRow += style.text + '&nbsp;</div>';
        styleRow += '<div class="styles-list-item styles-list-issue-color" style="background-color: ' + $customerPriorityConfig.fixColor(style.style.issue) + ';">Details</div>';
        styleRow += '<div class="styles-list-item styles-list-list-color" style="background-color: ' + $customerPriorityConfig.fixColor(style.style.list) + ';">List</div>';
        styleRow += '<div class="styles-list-item">';
        styleRow += '<div onClick=\'editStyle("' + style.id.trim() + '")\' title="Edit style" class="aui-icon aui-icon-small aui-iconfont-new-edit style-button style-button-edit">EDIT</div>';
        styleRow += '<div onClick=\'deleteStyle("' + style.id.trim() + '")\' title="Delete style" class="aui-icon aui-icon-small aui-iconfont-trash style-button style-button-delete">DELETE</div>';
        styleRow += '</div>'; // buttons
        styleRow += '<input type="hidden" className="style-list-reporters" value="' + $customerPriorityConfig.reportersStr(style.reporters) + '"/>';
        styleRow += '</div>'; // styles-list-row

        return styleRow;
    },
}

AJS.toInit(function() {
    $( "#edit-color-issue" ).on('input', function() {
        if ($("#edit-color-issue").val().trim() == "" || $("#edit-color-issue").val().trim() == "#") {
            $("#edit-color-issue").css("background-color", "");
            $("#edit-color-issue").css("color", "");
        }
    });
    $( "#edit-color-list" ).on('input', function() {
        if ($("#edit-color-list").val().trim() == "" || $("#edit-color-list").val().trim() == "#") {
            $("#edit-color-list").css("background-color", "");
            $("#edit-color-list").css("color", "");
        }
    });
});
