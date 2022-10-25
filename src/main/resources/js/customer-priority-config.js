(function(window) {
  window.$cupriConfig = {

    ESC_MAP: {
        '&': '&amp;',
        '<': '&lt;',
        '>': '&gt;',
        '"': '&quot;',
        "'": '&#39;'
    },

    deleteStyleConfirmation: function(styleId) {
      AJS.$("#style-id-to-delete").html(styleId);
      this.deleteConfirmationDialog = AJS.dialog2("#delete-confirm-dialog");
      this.deleteConfirmationDialog.show();
    },
    confirmDialogCancel: function() {
      this.deleteConfirmationDialog.hide();
      this.deleteConfirmationDialog = null;
    },
    confirmDialogSubmit: function() {
      let caller = this;
      let styleId = AJS.$("#style-id-to-delete").html();
      AJS.$.ajax({
        url: $cupriCommon.baseUrl() + "/styles/" + $cupriCommon.getProjectKey() + "/" + encodeURIComponent(styleId),
        type: "DELETE",
        contentType: "application/json",
        processData: false
      }).done(function () {
        JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-remove.success'));
        caller.removeStyle(styleId);
      }).error(function (error, message) {
        AJS.log("[style remove] error");
        AJS.log(error);
        AJS.log(message);
        JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.style-remove.error'))
      });
      this.deleteConfirmationDialog.hide();
      this.deleteConfirmationDialog = null;
    },

    editDialogShow: function(styleId) {
      AJS.$("#style-id-to-delete").html(styleId);
      let row = this.getStyleRowElement(styleId);
      let styleTitle  = row.find(".styles-list-title").attr("title");
      let glanceStyle = row.find(".styles-list-glance-style").attr("style");
      let glanceText  = row.find(".styles-list-glance-style").text();
      let issueColor  = this.rgb2hex(row.find(".styles-list-issue-color").css("background-color"));
      let listColor   = this.rgb2hex(row.find(".styles-list-list-color").css("background-color"));
      let reporters   = row.find(".style-list-reporters").val();
      if (issueColor == "#000000" || issueColor.toUpperCase() == "#FFFFFF") issueColor = "";
      if (listColor == "#000000"  || listColor.toUpperCase()  == "#FFFFFF")  listColor = "";
      AJS.$("#edit-style-id").val(styleId);
      AJS.$("#edit-style-id").prop("disabled", true);
      AJS.$("#edit-style-title").val(styleTitle);
      AJS.$("#edit-glance-style").val(glanceStyle);
      AJS.$("#edit-glance-text").val(glanceText.trim());
      AJS.$("#edit-color-issue").spectrum("set", issueColor);
      AJS.$("#edit-color-list").spectrum("set", listColor);
      AJS.$("#edit-reporters").val(reporters);
      this.editStyleDialog = AJS.dialog2("#style-edit-dialog");
      this.editStyleDialogSubmitCallback = this.editDialogSubmit;
      this.editStyleDialog.show();
    },
    editDialogCancel: function() {
      this.resetStyleForm();
      this.editStyleDialog.hide();
      this.editStyleDialog = null;
    },
    editDialogSubmit: function() {
      let caller = this;
      let request = this.getStyleFormData();
      AJS.$.ajax({
        url: $cupriCommon.baseUrl() + "/styles/" + $cupriCommon.getProjectKey(),
        type: "PUT",
        contentType: "application/json",
        data: JSON.stringify(request, null, 2),
        processData: false
      }).done(function () {
        JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-update.success'));
        caller.updateStyle(request);
        caller.resetStyleForm();
        caller.editStyleDialog.hide();
        caller.editStyleDialog = null;
      }).error(function (error, message) {
        AJS.log("[style update] error");
        AJS.log(error);
        AJS.log(message);
        JIRA.Messages.showErrorMsg(AJS.I18n.getText('cp.config.message.style-update.error'))
      });
    },

    createDialogShow: function() {
      this.resetStyleForm();
      this.editStyleDialog = AJS.dialog2("#style-edit-dialog");
      this.editStyleDialogSubmitCallback = this.createDialogSubmit;
      this.editStyleDialog.show();
    },
    createDialogSubmit: function() {
      let caller = this;
      let request = this.getStyleFormData();
      AJS.$.ajax({
        url: $cupriCommon.baseUrl() + "/styles/" + $cupriCommon.getProjectKey(),
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(request, null, 2),
        processData: false
      }).done(function () {
        JIRA.Messages.showSuccessMsg(AJS.I18n.getText('cp.config.message.style-create.success'));
        caller.editStyleDialog.hide();
        caller.editStyleDialog = null;
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
      let viewers = this.sanitize(AJS.$("#viewers-input").val().replaceAll(","," ").replaceAll(";", " ")).split(" ");
      AJS.$.ajax({
        url        : $cupriCommon.baseUrl() + "/viewers/" + $cupriCommon.getProjectKey(),
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
      AJS.$("#edit-reporters").val("");

      AJS.$("#edit-color-issue").val("");
      AJS.$("#edit-color-issue").css("background-color", "");
      AJS.$("#edit-color-list").val("");
      AJS.$("#edit-color-list").css("background-color", "");

      AJS.$("#edit-color-issue").spectrum("set", null);
      AJS.$("#edit-color-list").spectrum("set", null);
    },
    getStyleFormData: function() {
      let request          = {};
      request.style        = {};
      request.reporters    = [];
      request.id           = this.sanitize(AJS.$("#edit-style-id").val().trim(), false);
      request.title        = this.sanitize(AJS.$("#edit-style-title").val().trim(), false);
      request.text         = this.sanitize(AJS.$("#edit-glance-text").val().trim(), false);
      request.style.glance = this.sanitize(AJS.$("#edit-glance-style").val().trim(), true);
      request.style.issue  = AJS.$("#edit-color-issue").spectrum("get") || "";
      request.style.list   = AJS.$("#edit-color-list").spectrum("get")  || "";
      if (request.style.issue != "") request.style.issue = request.style.issue.toHexString();
      if (request.style.list  != "") request.style.list  = request.style.list.toHexString();
      if (request.style.issue.toLowerCase() == "#ffffff")  request.style.issue = "";
      if (request.style.list.toLowerCase() == "#ffffff")   request.style.list  = "";
      this.sanitize(AJS.$("#edit-reporters").val().trim(), false)
        .replaceAll(",", " ")
        .replaceAll(";", " ")
        .split(" ")
        .forEach(function(item) {
            request.reporters.push(item.trim());
          }
        );
      return request;
    },
    getStyleRowElement: function(styleId) {
        return AJS.$(".styles-list-row .styles-list-title").filter(function() {
            return $(this).text() == styleId;
        }).parent();
    },
    addStyle: function(style) {
      AJS.$("#styles-list").append(this.makeStyleRow(style));
      this.sortStyles();
    },
    updateStyle: function(style) {
      let row = this.getStyleRowElement(style.id);
      if (row) {
        row.find(".styles-list-title").attr("title", style.title);
        row.find(".styles-list-glance-style").attr("style", style.style.glance);
        row.find(".styles-list-glance-style").text(style.text);
        row.find(".styles-list-issue-color").css("background-color", style.style.issue);
        row.find(".styles-list-list-color").css("background-color", style.style.list);
        row.find(".style-list-reporters").val(style.reporters);
      }
    },
    removeStyle: function(styleId) {
      this.getStyleRowElement(styleId).remove();
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
    sanitize: function (s, forAttribute) {
      return s.replace(forAttribute ? /[&<>'"]/g : /[&<>]/g, function(c) {
        return this.ESC_MAP[c];
      });
    },
    reportersStr: function(reporters) {
      let result = "";
      reporters.forEach(function(item) {
        result += item + " ";
      });
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
      AJS.$("#styles-list").html("");
      stylesSorted.forEach(function(style) {
        AJS.$("#styles-list").append(this.makeStyleRow(style));
      });
    },
    getStyles: function() {
      let styles = [];
      AJS.$(".styles-list-title").each(function(index, item) {
        styles.push(this.getStyle($(item).text()));
      });
      return styles;
    },
    getStyle: function(styleId) {
      let row = this.getStyleRowElement(styleId);
      let style = {};
      style.reporters = [];
      style.style = {};
      if (row) {
        style.id           = styleId;
        style.title        = row.find(".styles-list-title").attr("title");
        style.style.glance = row.find(".styles-list-glance-style").attr("style").trim();
        style.text         = row.find(".styles-list-glance-style").text().trim();
        style.style.issue  = this.rgb2hex(row.find(".styles-list-issue-color").css("background-color"));
        style.style.list   = this.rgb2hex(row.find(".styles-list-list-color").css("background-color"));
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
      styleRow += '<div class="styles-list-item styles-list-issue-color" style="background-color: ' + style.style.issue + ';">Details</div>';
      styleRow += '<div class="styles-list-item styles-list-list-color" style="background-color: ' + style.style.list + ';">List</div>';
      styleRow += '<div class="styles-list-item">';
      styleRow += '<div onClick=\'editStyle("' + style.id.trim() + '")\' title="Edit style" class="aui-icon aui-icon-small aui-iconfont-new-edit style-button style-button-edit">EDIT</div>';
      styleRow += '<div onClick=\'deleteStyle("' + style.id.trim() + '")\' title="Delete style" class="aui-icon aui-icon-small aui-iconfont-trash style-button style-button-delete">DELETE</div>';
      styleRow += '</div>'; // buttons
      styleRow += '<input type="hidden" className="style-list-reporters" value="' + this.reportersStr(style.reporters) + '"/>';
      styleRow += '</div>'; // styles-list-row

      return styleRow;
    },
  };
})(window);

AJS.toInit(function() {

    AJS.log("[CUSTOMER PRIORITY CONFIG JS LOADED]");

    let spectrum_config = {
        preferredFormat: "hex",
        allowEmpty: true,
        // showAlpha: true,
        showInput: true,
        showButtons: false,
        showPaletteOnly: true,
        showPalette:true,
        togglePaletteOnly: true,
        togglePaletteMoreText: 'more',
        togglePaletteLessText: 'less',
        hideAfterPaletteSelect:true,
        palette: [
            ["rgba(255, 255, 255, 0);", "#000","#444","#666","#999","#ccc","#eee","#f3f3f3"],
            ["#f00","#f90","#ff0","#0f0","#0ff","#00f","#90f","#f0f"],
            ["#f4cccc","#fce5cd","#fff2cc","#d9ead3","#d0e0e3","#cfe2f3","#d9d2e9","#ead1dc"],
            ["#ea9999","#f9cb9c","#ffe599","#b6d7a8","#a2c4c9","#9fc5e8","#b4a7d6","#d5a6bd"],
            ["#e06666","#f6b26b","#ffd966","#93c47d","#76a5af","#6fa8dc","#8e7cc3","#c27ba0"],
            ["#c00","#e69138","#f1c232","#6aa84f","#45818e","#3d85c6","#674ea7","#a64d79"],
            ["#900","#b45f06","#bf9000","#38761d","#134f5c","#0b5394","#351c75","#741b47"],
            ["#600","#783f04","#7f6000","#274e13","#0c343d","#073763","#20124d","#4c1130"]
        ]
    };
    $("#edit-color-issue").spectrum(spectrum_config);

    $("#edit-color-list").spectrum(spectrum_config);

});
