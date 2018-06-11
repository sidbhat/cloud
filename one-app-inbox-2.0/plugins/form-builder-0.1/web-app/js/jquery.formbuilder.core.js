var index1 = 0,firstDiv,controlHolderText,secondDiv,thirdDiv,imageSourceDir = "/form-builder/plugins/form-builder-0.1";
var inputTypeFields = ['SingleLineDate','SingleLineText','Phone','Email','AddressField','SingleLineNumber','MultiLineText','dropdown','GroupButton','CheckBox','ScaleRating','LookUp','Likert','NameTypeField']
var typeWidgetTextReplacement={'PlainText':'Rich Text','LinkVideo':'Embed HTML','Email':'Email Address'};

var FormBuilder = {
    options: { // default options. values are stored in prototype
        fields: 'PlainText,SingleLineDate,SingleLineText,Phone,Email,SingleLineNumber,MultiLineText,dropdown,GroupButton,CheckBox,PlainTextHref,ScaleRating,ImageUpload,LinkVideo,FormulaField,FileUpload,LookUp,SubForm,PageBreak,LikeDislikeButton,Paypal,Likert,AddressField,NameTypeField',
        tabSelected: 0,
        readOnly: false,
        tabDisabled: [],
        formCounter: 1,
        language: 'en',
        settings: {
            en: {
                name: 'Untitled Form',
                description: 'Please fill out this form. Thank you!',
                classes: ['leftAlign'],
                heading: 'h2',
                styles: {
                    fontFamily: 'Lucida Sans Unicode',
                    fontSize: '13',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN: {
                name: '??',
                classes: ['rightAlign'],
                heading: 'h2',
                styles: {
                    fontFamily: 'default',
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            styles : {
            	form : {
            		color : 'default', // browser default
                    backgroundColor : 'default'
            	},
                heading : {
                	color : 'default', // browser default
                    backgroundColor : 'default'
                },
                description : {
                	color : 'default', // browser default
                    backgroundColor : 'default'
                }
            }
        },
        _id: '#container',
        _languages : [ 'en' ],
        _builderPanel: '#builderPanel',
        _builderForm: '#builderForm',
        _emptyBuilderPanel: '#emptyBuilderPanel',
        _paletteTabs: '#paletteTabs',
        _standardFieldsPanel: '#standardFields',
        _fancyFieldsPanel: '#fancyFields',
        _advanceFieldsPanel: '#advanceFields',
        _fieldSettingsPanel: '#fieldSettings',
        _fieldSettingsLanguageSection: '#fieldSettings fieldset.language:first',
        _fieldSettingsGeneralSection: '#fieldSettings div.general:first',
        _formSettingsLanguageSection: '#formSettings fieldset.language:first',
        _formSettingsGeneralSection: '#formSettings div.general:first',
        _languagesSupportIdGeneration: ['en'],
        _dragBoxCss: {
            opacity: 0.6,
            zIndex: 8888,
            border: "5px solid #cccccc"
        },
        _formControls: '#builderPanel fieldset',
        _draggableClass: 'draggable',
        _dropPlaceHolderClass: 'dropPlaceHolder'
    },
    _create: function() {
        // called on construction
        this._log('FormBuilder._create called. this.options.widgets = ' + this.options.widgets);
        this._initBrowserDefaultSettings();
        this._initBuilderPalette();
        this._initBuilderPanel();
    },
    _initBrowserDefaultSettings: function() {
        var $html = $('html');
        var options = this.options;
        options._fontFamily = $html.css('fontFamily');
        options._fontSize = $html.css('fontSize');
        options._color = $html.css('color');
        options._backgroundColor = $html.css('backgroundColor');
        var pxIndex = options._fontSize.lastIndexOf('px');
        if (pxIndex > -1) {
            options._fontSize = options._fontSize.substring(0, pxIndex);
        }
    },
    _initBuilderPalette: function() {
        $(window).scroll( checkScroll );

        $(this.options._paletteTabs).tabs({
            selected: this.options.tabSelected,
            disabled: this.options.tabDisabled,
            select: this._isFieldSettingsTabCanOpen
        });

        var widgets = this.options.fields;
        widgets = widgets.split(',');
        var length = widgets.length;
        var widgetOptions;
        var widget;
        var i;
        for (i = 0; i < length; i++) {
        	if($.trim(widgets[i]) != 'SubForm' || $('#formCat').val() == 'N'){
	            widgetOptions = $['fb']['fb' + $.trim(widgets[i])].prototype.options;
	            widget = $('<a id="' + widgetOptions._type + '" href="#" class="fbWidget">' + widgetOptions.name + '</a>');
	            widget.button()['fb' + widgetOptions._type]().appendTo(widgetOptions.belongsTo);
	            this._initDraggable(widget, widgetOptions._type);
        	}
        }
    },
    _initDraggable: function(widget, type) {
    	widget.draggable({
            cursor: 'move',
            distance: 10,
            helper: function (event, ui) {
            	var helper = $(this).data('fb' + type)
                        ._createFbWidget(event)
                if(helper == null){
                	helper = $('<div class="notAllowed" style="display:none;"></div>')
                }
                  helper.css($.fb.formbuilder.prototype.options._dragBoxCss)
                        .css({width: $('.formHeading').css('width')})
                        .addClass($.fb.formbuilder.prototype.options._draggableClass);
                $.fb.formbuilder.prototype._log('helper.html() = ' + helper.html());
                return helper;
            } ,
            drag: function(event, ui) {
                $.fb.formbuilder.prototype._log('ui.helper: w x h = ' + ui.helper.css('width') + " x " + ui.helper.css('height'));
                var $prevCtrlHolder = $.fb.formbuilder.prototype._getPreviousCtrlHolder(ui);
                var fbOptions = $.fb.formbuilder.prototype.options;
                if ($prevCtrlHolder && $prevCtrlHolder.attr('rel') != ui.helper.attr('rel')) {
                    ui.helper.attr('rel', $prevCtrlHolder.attr('rel'));
                    $('.' + fbOptions._dropPlaceHolderClass).remove();
                    $('<div></div>').addClass(fbOptions._dropPlaceHolderClass)
                            .css('height', (ui.helper.attr('class').indexOf('notAllowed')>-1?'0px':'30px')).insertAfter($prevCtrlHolder);
                } else {
                    var $ctrlHolder = $('.' + $.fb.fbWidget.prototype.options._styleClass +
                            ':visible:not(.' + fbOptions._draggableClass + '):first');

                    if ($ctrlHolder.length && ui.offset.top < $ctrlHolder.offset().top) {
                        $('.' + fbOptions._dropPlaceHolderClass).remove();
                    }
                }
            },
            stop: function(event, ui) {
                $('.' + $.fb.formbuilder.prototype.options._dropPlaceHolderClass).remove();
            }
        });
    },
    _isFieldSettingsTabCanOpen: function(event, ui) {
        if (ui.index == 1) { // Field Settings tab selected
            var options = $.fb.formbuilder.prototype.options;
            var canOpen = true;
            if ($(options._emptyBuilderPanel).is(':visible')) {
                $(options._standardFieldsPanel).qtip({
                    content: 'No field was created. Please select a field.',
                    position: { my: 'bottom left', at: 'top center' },
                    show: {
                        event: false,
                        ready: true,
                        effect: function() {
                            $(this).show('drop', { direction:'up'});
                        }
                    },
                    hide: {
                        target: $(options._standardFieldsPanel + ', ' + options._fancyFieldsPanel + ', ' + options._advanceFieldsPanel)
                    },
                    style: {
                        widget: true,
                        classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                        tip: true
                    }
                });
                canOpen = false;
            } else if ($(options._builderForm + ' .' + $.fb.fbWidget.prototype.options._selectedClass).length === 0) {
                $('.' + $.fb.fbWidget.prototype.options._styleClass + ':first').qtip({
                    content: "Please select field below to see it's Field Settings.",
                    position: { my: 'bottom center', at: 'top center' },
                    show: {
                        event: false,
                        ready: true,
                        effect: function() {
                            $(this).show('drop', { direction:'up'});
                        }
                    },
                    hide: {
                    	event: 'click',
                    	target: $(document)
                    },
                    style: {
                        widget: true,
                        classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                        tip: true
                    }
                });
                canOpen = false;
            }
            if (!canOpen) {
                // activate Add Field tab
                $(this).tabs('select', 0);
            }
            return canOpen;
        }
    },
    _initBuilderPanel: function() {

        this._initFormSettings();
        if (!this.options.readOnly) {
            this._initSortableWidgets();
            this._initDroppable();
        } else {
            $('input:not(div.buttons input, #id)').attr("disabled", true);
            $('select:not(#language)').attr("disabled", true);
            $('textarea').attr("disabled", true);
        }
        this._initWidgetsEventBinder();
    },
    _initDroppable: function() {
        var fbOptions = this.options;
        var $formControls = $(fbOptions._formControls);
        $formControls.droppable({
            drop: function(event, ui) {
                $.fb.formbuilder.prototype._log('drop executing. ui.helper.attr(rel) = ' + ui.helper.attr('rel') + ', ui.offset.top = ' + ui.offset.top);
                $.fb.formbuilder.prototype._log('ui.draggable.attr(id) = ' + ui.draggable.attr('id'));
                // to make sure the drop event is trigger by draggable instead of sortable
                if(ui.helper.attr('class').indexOf("notAllowed")>-1){
                	ui.helper.remove();
                	return;
                }
                if (ui.helper.attr('class').lastIndexOf(fbOptions._draggableClass) > -1) {
                    $('.' + fbOptions._dropPlaceHolderClass).remove();
                    var $widget = ui.draggable.data('fb' + ui.draggable.attr('id'));
                    var $prevCtrlHolder = $.fb.formbuilder.prototype._getPreviousCtrlHolder(ui);
                    var $ctrlHolder = $widget._createFbWidget(event);
                    var $elements;
                    if ($prevCtrlHolder) {
                        $widget._log('$prevCtrlHolder.text() = ' + $prevCtrlHolder.text());
                        $ctrlHolder.insertAfter($prevCtrlHolder);
                        $elements = $prevCtrlHolder.next().nextAll(':visible'); // $ctrlHolder.next() not works. :visible to prevent select the invisible ctrlHolder and emptyBuilderPanel
                    } else {
                        $(fbOptions._emptyBuilderPanel + ':visible').hide();
                        $elements = $('.' + $.fb.fbWidget.prototype.options._styleClass +
                                ':visible:not(.' + fbOptions._draggableClass + ')', $formControls);
                        $formControls.prepend($ctrlHolder).sortable('refresh');
                    }
                    $ctrlHolder.toggle('slide', {direction: 'up'}, 'slow');

                    if ($elements.length) {
                        // set next widget's sequence as my sequence
                        $widget._log('$elements.first().text() = ' + $elements.first().text());
                        $.fb.formbuilder.prototype._getSequence($ctrlHolder).val(
                                $.fb.formbuilder.prototype._getSequence($elements.first()).val()).change();
                        $elements.each(function(index) {
                            $.fb.formbuilder.prototype._increaseSequence($(this));
                        });
                    }
                    setWidthSetter();
                }
            }
        });
    },
    _getPreviousCtrlHolder: function(ui) {
        var $ctrlHolders = $('.' + $.fb.fbWidget.prototype.options._styleClass +
                ':visible:not(.' + $.fb.formbuilder.prototype.options._draggableClass + ')');
        var $this, $prevCtrlHolder;

        $ctrlHolders.each(function(i) {
            $this = $(this);
            $.fb.formbuilder.prototype._log(i + ') top = ' + $this.offset().top);
            if (ui.offset.top > $this.offset().top) {
                $prevCtrlHolder = $this;
            } else {
                return false;
            }
        });
        return $prevCtrlHolder;
    },
    _initFormSettings: function() {
        var $fbWidget = $.fb.fbWidget.prototype;
        var options = this.options;
        var $builderPanel = $(options._builderPanel);
        var $builderForm = $(options._builderForm);
        var $formSettingsLanguageSection = $(options._formSettingsLanguageSection);
        var $formSettingsGeneralSection = $(options._formSettingsGeneralSection);
        var defaultLanguage = $.inArray(options.language, options._languages) > -1 ? options.language : 'en';
        var $language = $('#language', $formSettingsLanguageSection).val(defaultLanguage).change(this._languageChange);
        var settings;
        var $this = this;
        var $formHeading = $('.formHeading', $builderPanel);
        var $settings = $('#settings', $builderForm);
        // first creation
        if ($settings.val() == '') {
            settings = options.settings[$language.val()];
            var $description = $('#formDescription', $builderForm);
            $description.text(settings.description);
            
            $formHeading.addClass(settings.classes[0]).append('<' + settings.heading + ' class="heading">' + settings.name + '</' + settings.heading + '>').append($description);
            $('#name', $builderForm).val($fbWidget._propertyName(options.settings['en'].name));
            $this._updateSettings($this);
        } else {
            options.settings = $.parseJSON(unescape($settings.val()));
            settings = options.settings[$language.val()];
        }

        $fbWidget._log('settings.name = ' + settings.name);

        var $name = $fbWidget._label({ label: 'Name', name: 'form.name' })
                .append('<input type="text" id="form.name" value="' + settings.name + '" />');
        $('input', $name).keyup(function(event) {
            var value = $(this).val();
            $fbWidget._log('options.disabledNameChange = ' + options.disabledNameChange);
            if (!options.disabledNameChange &&
                    $.inArray($language.val(), options._languagesSupportIdGeneration) > -1) {
                var name = $fbWidget._propertyName(value);
                $('#name', $builderForm).val(name).change();
            }
            $fbWidget._log('$(this).val() = ' + value);
            settings.name = value;
            $(settings.heading, $formHeading).text(value);
            $this._updateSettings($this);
        });
        var $formDescription = $fbWidget._label({ label: 'Description', name: 'form.description' })
        .append('<textArea id="form.description" style="resize: vertical" />');
        $('textArea', $formDescription).keyup(function(event) {
	        	var value = $(this).val();
	        	value = removeScriptTag(value);
	        	while(value.indexOf("\n")>-1){
	        		value = value.replace("\n","<br/>")
	        	}
	        	$('#formDescription', $formHeading).html(value);
	        	settings.description = value;
	        	$this._updateSettings($this);
	        });
        var tempDesc = ""+(settings.description!=undefined?settings.description:'');
        while(tempDesc.indexOf("<br/>")>-1 || tempDesc.indexOf("<br>")>-1){
        	tempDesc = tempDesc.replace("<br>","\n");
        	tempDesc = tempDesc.replace("<br/>","\n");
    	}
        $('textArea', $formDescription).val(tempDesc);
        var masterForms = '<select id="form.masterForms" multiple="multiple">';
    	for(var i=0;i<masterFormData.length;i++){
    		masterForms += ('<option value="'+masterFormData[i].id+'" selected=selected>'+masterFormData[i].label+'</option>');
    	}
        masterForms += '</select>';
        var $formMasters = $fbWidget._label({ label: 'Master Forms', name: 'form.masterForms' })
        		.append(masterForms);
        $('select',$formMasters).change(function(){
        	settings.masterForms = $(this).val();
        	$this._updateSettings($this);
        });
        $('select',$formMasters).parent().hide()
        
        if(!options.settings.reCaptcha){
        	options.settings.reCaptcha = false
        }
        var $addCaptcha = $fbWidget._twoColumns($('<div><input type="checkbox" id="form.reCaptcha"/></div>'),$('<div>Add ReCAPTCHA to form</div>'));
        $('.col1', $addCaptcha).css('width', '10%').removeClass('labelOnTop').css('padding','5px 0');
        $('.col2', $addCaptcha).css('marginLeft', '10%').removeClass('labelOnTop').css('padding','8px 0');
        $('input',$addCaptcha).change(function(){
        	options.settings.reCaptcha = this.checked;
        	$this._updateSettings($this);
        }).attr('checked',options.settings.reCaptcha).trigger('change');
        
        var $formWidth = $fbWidget._twoColumns($('<div>Form width: </div>'),$('<div><input type="text" id="form.width"/></div>'));
        $('.col1', $formWidth).css('width', '40%').removeClass('labelOnTop').css('padding','3px 0');
        $('.col2', $formWidth).css('marginLeft', '40%').removeClass('labelOnTop').css('padding','0');
        $('input',$formWidth).keyup(function(){
        	$('#builderPanel').width($(this).val());
        	options.settings.width = $(this).val();
        	$this._updateSettings($this);
        }).val((options.settings.width)?(options.settings.width):'640px').change(function(){$(this).trigger('keyup')}).trigger('change');
        
        var themeList={"Theme-1":"theme1","Theme-2":"theme2","Theme-3":"theme3","Theme-4":"theme4"};
        var themeText='<select><option value="none">default</option>'
        	$.each(themeList,function(k, v){ 
        		themeText+='<option value="'+v+'">'+k+'</option>'
        	});
        themeText+='</select>';
        $formTheme =$fbWidget._label({
            label: 'Themes',
            name: 'form.labelDisplay'
        	}).append( $(themeText));
        $('select',$formTheme).change(function(event) {
        	$("#builderPanel").removeClass().addClass($(this).val());
        	options.settings.themeText =$(this).val();
        	$this._updateSettings($this);
        }).val(""+(options.settings.themeText != null?options.settings.themeText:'none'))
        $(document).ready(function(){
        	$('select',$formTheme).trigger("change");
        });
        var $labelDisplay = $fbWidget._label({
            label: 'Display label/field',
            name: 'form.labelDisplay'
        }).append('<select id="form.labelDisplay">\
	        		<option value="0">Label on top</option>\
	        		<option value="1">Label on left</option>\
        			<option value="2">Label on right</option>\
        		</select>\
        		')
        $('select',$labelDisplay).change(function(event) {
        	if ($(this).val()=="1" || $(this).val()=="2" || $(this).val()=="true") {
        		$(".fullLengthLabel").removeClass("fullLengthLabel").addClass("customLengthLabel");
        		$(".fullLengthField").removeClass("fullLengthField").addClass("customLengthField");
        		var $slider = $(".widthSetterSlider");
        		if($(this).val()=="1"){
        			$(".customLengthLabel").css("text-align","");
        		}else{
        			$(".customLengthLabel").css("text-align","right");
        		}
//        		var labelWidth = options.settings.labelWidth != null?options.settings.labelWidth:43;
//        		var labelHeight = options.settings.labelHeight != null?options.settings.labelHeight:53;
//        		var leftCenterMargin = (100 - (labelWidth + labelHeight))/2 - 1
//        		var startPosition = labelWidth + leftCenterMargin;
//        		$(".customLengthLabel").css("width",startPosition - leftCenterMargin +"%")
//        		if($(this).val()=="1"){
//        			$(".customLengthLabel").css("text-align","");
//        		}else{
//        			$(".customLengthLabel").css("text-align","right");
//        		}
//        		$(".customLengthField").css("width",100 - startPosition - leftCenterMargin-2 +"%")
//        		if($(".widthSetterSlider").children().size() == 0){
//        			$slider.slider({
//                    	value:startPosition, min: 0, max: 100,
//                    	slide: function( event, ui ) {
//                    		$(".customLengthLabel").css("width",ui.value - leftCenterMargin +"%");
//                    		$(".customLengthField").css("width",100 - ui.value - leftCenterMargin*2 +"%");
//                    		options.settings.labelWidth = ui.value - leftCenterMargin
//                    		options.settings.labelHeight = 100 - ui.value - leftCenterMargin-2
//                    		$this._updateSettings($this);
//                            //$('a',$slider).attr('style', 'background-image: url('+imageSourceDir+'/images/face-'+Math.round(ui.value/25).toFixed(0)+'.png) !important;');
//                    	}
//                    });
//        		}
        		setWidthSetter();
        		$('a',$slider).height($('a',$slider).parent().parent().parent().height()-24);
        		$(".widthSetterSlider").show();
        		setImageWidthAndHeight();
            } else {
            	$(".customLengthLabel").removeClass("customLengthLabel").addClass("fullLengthLabel").css("width","").css("text-align","");
            	$(".customLengthField").removeClass("customLengthField").addClass("fullLengthField").css("width","");
            	$(".widthSetterSlider").hide();
            	setImageWidthAndHeight();
            }
        	options.settings.labelDisplay =$(this).val();
        	$this._updateSettings($this);
        });
        $('select',$labelDisplay).val(""+(options.settings.labelDisplay != null?options.settings.labelDisplay:false))
        $(document).ready(function(){
        	$('select',$labelDisplay).trigger("change");
        });
        var $heading = $fbWidget._label({
            label: 'Heading',
            name: 'form.heading'
        }).append('<select> \
				<option value="h1">Heading 1</option> \
				<option value="h2">Heading 2</option> \
				<option value="h3">Heading 3</option> \
				<option value="h4">Heading 4</option> \
				<option value="h5">Heading 5</option> \
				<option value="h6">Heading 6</option> \
			</select>');

        $('select', $heading)
                .val(settings.heading)
                .attr('id', 'form.heading')// unable to set value if specify in select tag
                .change(function(event) {
            var heading = $(this).val();
            var text = $(settings.heading, $formHeading).text();
            var $heading = $('<' + heading + ' class="heading">' + text + '</' + heading + '>');
            if (settings.styles.fontStyles[0] === 0) {
                $heading.css('fontWeight', 'normal');
            }
            if (settings.styles.fontStyles[1] == 1) {
                $heading.css('fontStyle', 'italic');
            }
            if (settings.styles.fontStyles[2] == 1) {
                $heading.css('textDecoration', 'underline');
            }
            if (options.settings.styles.heading) {
                $heading.css('color', (options.settings.styles.heading.color == 'transparent'?'':'#')+options.settings.styles.heading.color);
            }
            $(settings.heading, $formHeading).replaceWith($heading);
            settings.heading = heading;
            $this._updateSettings($this);
        });

        var $horizontalAlignment = $fbWidget._horizontalAlignment({ name: 'form.horizontalAlignment', value: settings.classes[0] });
        $('select', $horizontalAlignment).change(function(event) {
            var value = $(this).val();
            $formHeading.removeClass(settings.classes[0]).addClass(value);
            settings.classes[0] = value;
            $this._updateSettings($this);
        });

        settings.styles.fontFamily = settings.styles.fontFamily == 'default' ? options._fontFamily : settings.styles.fontFamily;
        settings.styles.fontSize = settings.styles.fontSize == 'default' ? options._fontSize : settings.styles.fontSize;
        var $fontPanel = $fbWidget._fontPanel({
            fontFamily: settings.styles.fontFamily,
            fontSize: settings.styles.fontSize,
            fontStyles: settings.styles.fontStyles,
            idPrefix: 'form.', nofieldset: true });

        $("input[id$='form.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                $(settings.heading, $formHeading).css('fontWeight', 'bold');
                settings.styles.fontStyles[0] = 1;
            } else {
                $(settings.heading, $formHeading).css('fontWeight', 'normal');
                settings.styles.fontStyles[0] = 0;
            }
            $this._updateSettings($this);
        });
        $("input[id$='form.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                $(settings.heading, $formHeading).css('fontStyle', 'italic');
                settings.styles.fontStyles[1] = 1;
            } else {
                $(settings.heading, $formHeading).css('fontStyle', 'normal');
                settings.styles.fontStyles[1] = 0;
            }
            $this._updateSettings($this);
        });
        $("input[id$='form.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                $(settings.heading, $formHeading).css('textDecoration', 'underline');
                settings.styles.fontStyles[2] = 1;
            } else {
                $(settings.heading, $formHeading).css('textDecoration', 'none');
                settings.styles.fontStyles[2] = 0;
            }
            $this._updateSettings($this);
        });

        $("input[id$='form.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            $builderPanel.css('fontFamily', value);
            settings.styles.fontFamily = value;
            $this._updateSettings($this);
        });


        $("select[id$='form.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            $builderPanel.css('fontSize', value + 'px');
            settings.styles.fontSize = value;
            $this._updateSettings($this);
        });

        if (options.settings.styles.color == 'default') {
            options.settings.styles.color = options._color;
        }

        if (options.settings.styles.backgroundColor == 'default') {
            options.settings.styles.backgroundColor = options._backgroundColor;
        }
        var $colorPanel
        if(options.settings.styles.form){
        	$colorPanel = $fbWidget._colorPanel({ form: {color: options.settings.styles.form.color,
                backgroundColor: options.settings.styles.form.backgroundColor},heading: {color: options.settings.styles.heading.color,
                backgroundColor: options.settings.styles.heading.backgroundColor},description: {color: options.settings.styles.description.color,
                backgroundColor: options.settings.styles.description.backgroundColor} });
        }else{
        	$colorPanel = $fbWidget._colorPanel({ form: {color: options.settings.styles.color,
                backgroundColor: options.settings.styles.backgroundColor},heading: {color: '000000',
                backgroundColor: 'transparent'},description: {color: '000000',
                backgroundColor: 'transparent'} });
        }
        var showTextarea = function(pele,ele){
        	$('textArea', pele).show();
        	var icon = $('.ui-icon',ele);
        	icon.removeClass('ui-icon-carat-1-s').addClass('ui-icon-carat-1-n')
        		.removeClass('ui-icon-circle-triangle-s').addClass('ui-icon-circle-triangle-n')
        		.attr('title','Collapse');
        }
        var hideTextarea = function(pele,ele){
        	$('textArea', pele).hide();
        	var icon = $('.ui-icon',ele);
        	icon.removeClass('ui-icon-carat-1-n').addClass('ui-icon-carat-1-s')
        		.addClass('ui-icon-circle-triangle-s').removeClass('ui-icon-circle-triangle-n')
        		.attr('title','Expand');
        }
        var arrowMouseOver = function(pele,ele){
        	var icon = $('.ui-icon',ele);
        	if($('textArea', pele).is(':visible')){
        		icon.addClass('ui-icon-circle-triangle-n').attr('title','Collapse');
        	}else{
        		icon.addClass('ui-icon-circle-triangle-s').attr('title','Expand');
        	}
        }
        var arrowMouseOut = function(ele){
        	$('.ui-icon',ele).removeClass('ui-icon-circle-triangle-n').removeClass('ui-icon-circle-triangle-s');
        }
        var $formCSS = $fbWidget._fieldset({ text: 'Embed CSS <a class="ui-corner-all" href="#" id="cssArrow" style="background-color:#E4DFD5;display:inline-block"><span class="ui-icon ui-icon-carat-1-s">Expand</span></a>'})
        .append('<textArea id="form.CSS" style="resize: vertical;width:95%;display:none" />');
        $('textArea', $formCSS).keyup(function(event) {
        	var value = $(this).val();
        	$('#FormStyles').html(value);
        	settings.CSS = value;
        	$this._updateSettings($this);
        }).val(settings.CSS?settings.CSS:'.formBodyStyle{\nfont-size:13px;\n}\ninput[type="submit"]{\ncolor:white;\n}');
        $('#cssArrow',$formCSS).toggle(function(){
        	showTextarea($formCSS,this);
        },function(){
        	hideTextarea($formCSS,this);
        }).mouseover(function(){
        	arrowMouseOver($formCSS,this);
        }).mouseout(function(){
        	arrowMouseOut(this);
        });
        
        var $formJS = $fbWidget._fieldset({ text: 'Embed Javascript <a class="ui-corner-all" href="#" id="jsArrow" style="background-color:#E4DFD5;display:inline-block"><span class="ui-icon ui-icon-carat-1-s">Expand</span></a>'})
        .append('<textArea id="form.js" style="resize:vertical;width:95%;display:none" />');
        $('textArea', $formJS).keyup(function(event) {
        	var value = $(this).val();
        	$('#FormJS').html(value);
        	settings.js = value;
        	$this._updateSettings($this);
        }).val(settings.js?settings.js:'function beforeSubmit(){\n   //code for validation before save\n   return true;//or false accordingly\n}');
        $('#jsArrow',$formJS).toggle(function(){
        	showTextarea($formJS,this);
        },function(){
        	hideTextarea($formJS,this);
        }).mouseover(function(){
        	arrowMouseOver($formJS,this);
        }).mouseout(function(){
        	arrowMouseOut(this);
        });

        $("input[id$='form.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $builderPanel.css('color', (value=='transparent'?'':'#') + value);
            $this.options.settings.styles.form.color = value;
            if($this.options.settings.styles.form){
            	$this.options.settings.styles.form.color = value;
            }else{
            	$this.options.settings.styles.form = {color : value,backgroundColor : 'transparent'};
            	$this.options.settings.styles.heading = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.description = {color : 'default',backgroundColor : 'transparent'};
            }
            $this._updateSettings($this);
        });
        $("input[id$='heading.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $('.formHeading '+$(document.getElementById('form.heading')).val(),$builderPanel).css('color', (value=='transparent'?'':'#') + value);
            $this.options.settings.styles.heading.color = value;
            if($this.options.settings.styles.heading){
            	$this.options.settings.styles.heading.color = value;
            }else{
            	$this.options.settings.styles.form = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.heading = {color : value,backgroundColor : 'transparent'};
            	$this.options.settings.styles.description = {color : 'default',backgroundColor : 'transparent'};
            }
            $this._updateSettings($this);
        });
        $("input[id$='description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $('#formDescription').css('color', (value=='transparent'?'':'#') + value);
            if($this.options.settings.styles.description){
            	$this.options.settings.styles.description.color = value;
            }else{
            	$this.options.settings.styles.form = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.heading = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.description = {color : value,backgroundColor : 'transparent'};
            }
            $this._updateSettings($this);
        });

        $("input[id$='form.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $this._log('background color change: value ' + value);
            $builderPanel.css('backgroundColor', (value=='transparent'?'':'#') + value);
            if($this.options.settings.styles.form){
            	$this.options.settings.styles.form.backgroundColor = value;
            }else{
            	$this.options.settings.styles.form = {color : 'default',backgroundColor : value};
            	$this.options.settings.styles.heading = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.description = {color : 'default',backgroundColor : 'transparent'};
            }
            $this._updateSettings($this);
        });
        $("input[id$='heading.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $this._log('background color change: value ' + value);
            $('.formHeading',$builderPanel).css('backgroundColor', (value=='transparent'?'':'#') + value);
            if($this.options.settings.styles.heading){
            	$this.options.settings.styles.heading.backgroundColor = value;
            }else{
            	$this.options.settings.styles.form = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.heading = {color : 'default',backgroundColor : value};
            	$this.options.settings.styles.description = {color : 'default',backgroundColor : 'transparent'};
            }
            $this._updateSettings($this);
        });
        $("input[id$='description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $this._log('background color change: value ' + value);
            $('#formDescription').css('backgroundColor', (value=='transparent'?'':'#') + value);
            if($this.options.settings.styles.description){
            	$this.options.settings.styles.description.backgroundColor = value;
            }else{
            	$this.options.settings.styles.form = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.heading = {color : 'default',backgroundColor : 'transparent'};
            	$this.options.settings.styles.description = {color : 'default',backgroundColor : value};
            }
            $this._updateSettings($this);
        });
        
        $formSettingsLanguageSection.append($fbWidget._oneColumn($name))
        		.append($fbWidget._oneColumn($formDescription))
        		.append($fbWidget._oneColumn($formMasters))
        		.append($fbWidget._oneColumn($addCaptcha).removeClass('clear').removeClass('labelOnTop'))
        		.append($fbWidget._oneColumn($formWidth).removeClass('clear'))
        		.append($fbWidget._twoColumns( $formTheme,$labelDisplay))
                .append($fbWidget._twoColumns($heading, $horizontalAlignment))
                .append($fontPanel);
        $formSettingsGeneralSection.append($colorPanel).append($formCSS).append($formJS);

    },
    _languageChange:function(event) {
        $.fb.formbuilder.prototype._log('languageChange(' + $(this).val() + ', ' + $('option:selected', this).text() + ')');
        var fbOptions = $.fb.fbWidget.prototype._getFbOptions();
        var $ctrlHolders = $('.' + $.fb.fbWidget.prototype.options._styleClass + ':visible');
        var language = $(this).val();
        var languageText = $('option:selected', this).text();
        var formSettings = fbOptions.settings[language];
        var $formHeading = $('.formHeading');
        var $formSettingsLanguageSection = $(fbOptions._formSettingsLanguageSection);
        var $builderPanel = $(fbOptions._builderPanel);
        var settings, type, $widget, selected, fb;

        $("input[id$='form.name']", $formSettingsLanguageSection).val(formSettings.name);

        $("select[id$='form.heading'] option[value='" + formSettings.heading + "']",
                $formSettingsLanguageSection).attr('selected', 'true');

        var $heading = $('<' + formSettings.heading + ' class="heading">' + formSettings.name + '</' + formSettings.heading + '>')
                .css('fontWeight', formSettings.styles.fontStyles[0] == 1 ? 'bold' : 'normal')
                .css('fontStyle', formSettings.styles.fontStyles[1] == 1 ? 'italic' : 'normal')
                .css('textDecoration', formSettings.styles.fontStyles[2] == 1 ? 'underline' : 'none');
        $('.heading', $formHeading).replaceWith($heading);
        $.fb.formbuilder.prototype._log('formSettings.fontStyles[2] = ' + formSettings.styles.fontStyles[2]);
        $("input[id$='form.bold']", $formSettingsLanguageSection).attr('checked', formSettings.styles.fontStyles[0]);
        $("input[id$='form.italic']", $formSettingsLanguageSection).attr('checked', formSettings.styles.fontStyles[1]);
        $("input[id$='form.underline']", $formSettingsLanguageSection).attr('checked', formSettings.styles.fontStyles[2]);

        $formHeading.removeClass('leftAlign centerAlign rightAlign').addClass(formSettings.classes[0]);
        $("select[id$='form.horizontalAlignment'] option[value='" + formSettings.classes[0] + "']",
                $formSettingsLanguageSection).attr('selected', 'true');

        formSettings.styles.fontFamily = formSettings.styles.fontFamily == 'default' ? fbOptions._fontFamily : formSettings.styles.fontFamily;
        formSettings.styles.fontSize = formSettings.styles.fontSize == 'default' ? fbOptions._fontSize : formSettings.styles.fontSize;
        $builderPanel.css('fontFamily', formSettings.styles.fontFamily);
        $builderPanel.css('fontSize', formSettings.styles.fontSize + 'px');
        $("select[id$='form.fontSize']", $formSettingsLanguageSection).val(formSettings.styles.fontSize);
        $('.fontPicker', $formSettingsLanguageSection).fontPicker('fontFamily', formSettings.styles.fontFamily);

        $ctrlHolders.each(function(i) {
            var $widget = $(this);
            selected = $widget.attr('class').indexOf($.fb.fbWidget.prototype.options._selectedClass) > -1;
            if (selected) {
                $(fbOptions._fieldSettingsLanguageSection + ' legend').text('Language: ' + languageText);
            }
            if (!$widget.data('fbWidget')) { // widgets loaded from server
                var $settings = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].settings']");
                // settings is JavaScript encoded when return from server-side
                $widget.data('fbWidget', $.parseJSON(unescape($settings.val())));
            }
            settings = $widget.data('fbWidget');
            $.fb.formbuilder.prototype._log(i + ') settings = ' + settings);
            type = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].type']").val();
            $.fb.formbuilder.prototype._log('type = ' + type);
            $this = $('#' + type).data('fb' + type);
            fb = {target: $this, item: $widget, settings: settings[language]};
            fb.item.selected = selected;
            if (selected) { // refresh field settings tab
                $this._createFieldSettings(event, $widget);
            }
            $this._languageChange(event, fb);
        });
    },
    _updateSettings: function($this) {
        $('#settings').val($.toJSON($this.options.settings)).change();
    } ,
    _initWidgetsEventBinder: function() { // for widgets loaded from server
        var $ctrlHolders = $('.' + $.fb.fbWidget.prototype.options._styleClass);
        var size = $ctrlHolders.size();
        if (size > 0) {
            var $this, widget;
            var fieldsUpdateStatus = ['name', 'settings', 'sequence'];

            $(this.options._emptyBuilderPanel + ':visible').hide();
            $ctrlHolders.each(function(i) {
                $this = $(this);
                widget = $this.find("input[id$='fields[" + i + "].type']").val();
                $this.click($['fb']['fb' + widget].prototype._createFieldSettings);
                for (var j = 0; j < fieldsUpdateStatus.length; j++) {
                    $this.find("input[id$='fields[" + i + "]." + fieldsUpdateStatus[j] + "']")
                            .change($.fb.fbWidget.prototype._updateStatus);
                }
            });
            if (!this.options.readOnly) {
                $ctrlHolders.find(".closeButton").click($.fb.fbWidget.prototype._deleteWidget)
                        .mouseover(function () {
                    $('span', this).removeClass('ui-icon-close').addClass('ui-icon-circle-close');
                })
                        .mouseout(function () {
                    $('span', this).removeClass('ui-icon-circle-close').addClass('ui-icon-close');
                });
                $ctrlHolders.find(".copyButton").click($.fb.fbWidget.prototype._copyWidget)
		                .mouseover(function () {
		            $('span', this).removeClass('ui-icon-plus').addClass('ui-icon-circle-plus');
		        })
		                .mouseout(function () {
		            $('span', this).removeClass('ui-icon-circle-plus').addClass('ui-icon-plus');
		        });
            }
        }
    },
    _initSortableWidgets: function() {
        var $formControls = $(this.options._formControls);
        $formControls.sortable({
            axis: 'y',
            cursor: 'move',
            distance: 10,
            helper: function (event, ui) {
                return $(ui).clone().css($.fb.formbuilder.prototype.options._dragBoxCss);
            },
            start: function (event, ui) {
                var $previousElement = $(ui.item).prev();
                if ($previousElement.attr('rel')) {
                    ui.item.prevIndex = $previousElement.attr('rel');
                    ui.item.originalOffsetTop = $previousElement.offset().top;
                }
            },
            update: $.fb.formbuilder.prototype._updateSequence
        });

        // Making text elements, or elements that contain text, not text-selectable.
        $formControls.disableSelection();
    },
    _updateSequence: function (event, ui) {
        var $uiItem = $(ui.item);
        var $elements;
        var moveDown = ui.offset.top > ui.item.originalOffsetTop;
        $.fb.formbuilder.prototype._log('moveDown = ' + moveDown + ', ui.offset.top = ' + ui.offset.top + ", ui.item.originalOffsetTop = " + ui.item.originalOffsetTop);
        if (ui.item.prevIndex) {
            var prevElementSelector = '[rel="' + ui.item.prevIndex + '"]';
            if (moveDown) {
                $elements = $uiItem.prevUntil(prevElementSelector);
                $.fb.formbuilder.prototype._moveDown($uiItem, $elements);
            } else {
                // set next widget's sequence as my sequence
                $.fb.formbuilder.prototype._getSequence($uiItem).val(
                        $.fb.formbuilder.prototype._getSequence($uiItem.next()).val()).change();
                $elements = $uiItem.nextUntil(prevElementSelector);
                $elements.each(function(index) {
                    $.fb.formbuilder.prototype._increaseSequence($(this));
                });
                // process the last one
                $.fb.formbuilder.prototype._increaseSequence($(prevElementSelector));
            }
        } else {
            $elements = $uiItem.prevAll();
            $.fb.formbuilder.prototype._moveDown($uiItem, $elements);
        }
    },
    _init: function() {
        // called on construction and re-initialization
        this._log('FormBuilder._init called.');
        this.method1('calling from FormBuilder._init');
    },
    destroy: function() {
        // called on removal
        this._log('FormBuilder.destroy called.');

        // call the base destroy function.
        $.Widget.prototype.destroy.call(this);
    },
    // _logging to the firebug's console, put in 1 line so it can be removed easily for production
    _log: function($message) {
        if (window.console && window.console.log) window.console.log($message);
    },
    _moveDown: function($widget, $elements) {
        // set previous widget's sequence as my sequence
        $.fb.formbuilder.prototype._getSequence($widget).val(
                $.fb.formbuilder.prototype._getSequence($widget.prev()).val()).change();
        $elements.each(function(index) {
            $.fb.formbuilder.prototype._decreaseSequence($(this));
        });
    },
    _getSequence: function($widget) {
        return $widget.find("input[id$='fields[" + $widget.attr('rel') + "].sequence']");
    },
    _increaseSequence: function($widget) {
        if ($widget.is(":visible")) {
            var $sequence = $.fb.formbuilder.prototype._getSequence($widget);
            $sequence.val($sequence.val() * 1 + 1);
            $sequence.change();
        }
    },
    _decreaseSequence: function($widget) {
        if ($widget.is(":visible")) {
            var $sequence = $.fb.formbuilder.prototype._getSequence($widget);
            $sequence.val($sequence.val() - 1);
            $sequence.change();
        }
    },
    method1: function(params) {
        // plugin specific method
        this._log('FormBuilder.method1 called. params = ' + params);
    }
};

$.widget('fb.formbuilder', FormBuilder);


var FbWidget = {
    options: { // default options. values are stored in widget's prototype
        _styleClass: 'ctrlHolder',
        _selectedClass: 'ctrlHolderSelected'
    },
    // logging to the firebug's console, put in 1 line so it can be removed
    // easily for production
    _log: function($message) {
        if (window.console && window.console.log) window.console.log($message);
    },
    _create: function() {
        this._log('FbWidget._create called.');
    },
    _init: function() {
        this._log('FbWidget._init called.');
        this.element.click(this._createFbWidget);
    },
    destroy: function() {
        this._log('FbWidget.destroy called.');
        this.element.button('destroy');

        // call the base destroy function.
        $.Widget.prototype.destroy.call(this);

    },
    _getFbOptions: function() {
        return $($.fb.formbuilder.prototype.options._id).formbuilder('option');
    },
    _getFbLocalizedSettings: function() {
        return $($.fb.formbuilder.prototype.options._id).formbuilder('option').settings[$('#language').val()];
    },
    _createField: function(name, widget, options, settings) {
    	var fbOptions = $.fb.formbuilder.prototype.options;
        var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        index = index1 - 1;
        $('<a class="ui-corner-all ui-state-error closeButton" href="#"><span class="ui-icon ui-icon-close">delete this widget</span></a>')
                .prependTo(widget).click($.fb.fbWidget.prototype._deleteWidget)
                .mouseover(function () {
		            $('span', this).removeClass('ui-icon-close').addClass('ui-icon-circle-close');
		        }).mouseout(function () {
		            $('span', this).removeClass('ui-icon-circle-close').addClass('ui-icon-close');
		        });
        if(options._type!='Paypal'){
        	$('<a class="ui-corner-all ui-state-highlight copyButton" href="#"><span class="ui-icon ui-icon-plus" title="Duplicate">duplicate this widget</span></a>')
		        .prependTo(widget).click($.fb.fbWidget.prototype._copyWidget)
			    .mouseover(function () {
				    $('span', this).removeClass('ui-icon-plus').addClass('ui-icon-circle-plus');
				}).mouseout(function () {
				    $('span', this).removeClass('ui-icon-circle-plus').addClass('ui-icon-plus');
				});
        }
        widget.attr('rel', index1);
        var divIdForP = name + '' + index1;
        var strList = (options._html).split('</span>')
            widget.append($.fb.fbWidget.prototype._createFieldProperties(name, options, settings, index1));
        $(fbOptions._emptyBuilderPanel + ':visible').hide();
    },
    _propertyName: function (value) {
        var propertyName;
        propertyName = value.replace(/ /gi, '');
        propertyName = propertyName.charAt(0).toLowerCase() + propertyName.substring(1);
        return propertyName;
    },
    _deleteWidget: function(event) {
    	var targetButton = $(event.target);
    	var $widget = targetButton.parent().hasClass('ctrlHolder')?targetButton.parent():targetButton.parent().parent();
    	var $deleteButton = $('.closeButton',$widget);
    	var index = $widget.attr('rel');
        var options = $.fb.fbWidget.prototype.options;
        var fbOptions = $.fb.formbuilder.prototype.options;
        
        //*****Start->Find if the field's reference is used in some formula
        var thisUsedInArr = new Array()
        $('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
        	var $this = $(this);
        	var rel = $this.attr('rel');
        	if($this.find("input[id$='fields[" + rel + "].type']").val() == 'FormulaField'){
        		var settings = $.parseJSON(unescape($this.find("input[id$='fields[" + rel + "].settings']").val()))
        		if($.inArray($widget.find("input[id$='fields[" + index + "].name']").val(), settings[$('#language').val()].value) > -1){
        			thisUsedInArr[thisUsedInArr.length] = settings[$('#language').val()].label
        		}
        	}
        });
        //*****End->Find if the field's reference is used in some formula
        
        //show message if used in any formula(s)
        if(thisUsedInArr.length>0){
        	var message = "This field is used in Calculated Field: ";
        	for(var i = 0; i<thisUsedInArr.length; i++){
        		message += thisUsedInArr[i] + (i < thisUsedInArr.length-1?', ':'');
        	}
        	$widget.qtip({
            	content: message,
            	position: { my: 'bottom center', at: 'top center' },
		        show: { event: false, ready: true, effect: function() { $(this).show('drop', { direction:'up'}); } },
		        hide: { event: 'click', target: $(document) },
		        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
    	    });
        	event.stopPropagation();
        	return;
        }

        // new record that not stored in database
        if ($widget.find("input[id$='fields[" + index + "].id']").val() == 'null') {
            $widget.remove();
            setWidthSetter();
        } else {
            $widget.find("input[id$='fields[" + index + "].status']").val('D');
            
            $deleteButton.after('<a class="ui-corner-all ui-state-highlight undoButton" title="Undo Delete" href="#"><span class="ui-icon ui-icon-arrowreturnthick-1-w">undo delete this widget</span></a>');
        	var $undoDeleteButton = $('.undoButton',$widget);
        	$undoDeleteButton.click($.fb.fbWidget.prototype._undoDeleteWidget);
        	
        	$deleteButton.hide();
            $widget.addClass('deletedCtrlHolder');
            $widget.qtip({
            	content: "Field deleted. To Undo, click here.",
    	        position: { my: 'bottom right', at: 'top right' },
    	        show: { event: 'mouseover', effect: function() { $(this).show('drop', { direction:'up'}); } },
    	        hide: 'mouseout',
    	        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
    	    });
        }
        var $ctrlHolders = $('.' + options._styleClass + ':visible');
        $.fb.fbWidget.prototype._log('_deleteWidget(). $ctrlHolders.size() = ' + $ctrlHolders.size());
        // activate Add Field tab
        $(fbOptions._paletteTabs).tabs('select', 0);
        $('.ctrlHolderSelected').removeClass('ctrlHolderSelected');
        
        if ($ctrlHolders.size() === 0) {
            $(fbOptions._emptyBuilderPanel).show();
        }
        event.stopPropagation();
        return false;
    },
    _copyWidget: function(event) {
    	var targetButton = $(event.target);
    	var $widget = targetButton.parent().hasClass('ctrlHolder')?targetButton.parent():targetButton.parent().parent();
    	var rel = $widget.attr('rel');
        var options = $.fb.fbWidget.prototype.options;
        var fbOptions = $.fb.formbuilder.prototype.options;
        
    	var $copyForm = $('<div style="display:none;"><form><input type="hidden" name="name" value=""><input type="hidden" name="settings" value=""><input type="hidden" name="type" value=""><input type="hidden" name="rel" value=""></form></div>')
    	$('body').append($copyForm);
    	var type=$('input[name="fields['+rel+'].type"]').val();
		$('input[name="name"]',$copyForm).val("field"+new Date().getTime());
		$('input[name="settings"]',$copyForm).val($('input[name="fields['+rel+'].settings"]').val());
		$('input[name="type"]',$copyForm).val(type);
		var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
		$('input[name="rel"]',$copyForm).val(index);
		loadScreenBlock();
		$.ajax({ 
			url: "/form-builder/form/getControlHolder",
	        type: "POST",
	        data: $('form',$copyForm).serialize(),
	        dataType: "text",
	        success: function(data) {
	        	controlHolderText = data;
	        	$("#"+type).trigger('click');
	        	controlHolderText = null;
	        	document.getElementById("spinner").style.display = "none";
	        	$copyForm.remove();
	        },
			error: function(){
				alert("Problem copying!")
				document.getElementById("spinner").style.display = "none";
				$copyForm.remove();
			}
        });
        event.stopPropagation();
        return false;
    },
    _undoDeleteWidget: function(event) {
    	var allControlHolders = $('#builderForm div.ctrlHolder:not(.deletedCtrlHolder)')
    	var totalCtrlHolders = allControlHolders.size()
    	if(maxFormControls <= totalCtrlHolders){
    		$('.' + $.fb.fbWidget.prototype.options._styleClass + ':first').qtip({
                content: 'Can not undo. Number of used controls can not be more than '+maxFormControls+'. Please contact administrator.',
                position: { my: 'bottom center', at: 'top center' },
                show: {
                    event: false,
                    ready: true,
                    effect: function() {
                        $(this).show('drop', { direction:'up'});
                    }
                },
                hide: {
                	event: 'click',
                	target: $(document)
                },
                style: {
                    widget: true,
                    classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                    tip: true
                }
            });
    		return false;
    	}
    	var targetButton = $(event.target);
    	var $widget = targetButton.parent().hasClass('ctrlHolder')?targetButton.parent():targetButton.parent().parent();
    	var $undoDeleteButton = $('.undoButton',$widget);
    	var index = $widget.attr('rel');
    	var thisWidgetType = $widget.find("input[id$='fields[" + index + "].type']").val()
    	//Start->Check if there is any paypal control
    	if(thisWidgetType == 'Paypal'){
    		var paypalControlPresent = false;
    		allControlHolders.each(function(){
    			var thisControlHolder = $(this);
    			if($("input[id$='fields[" + $(this).attr("rel") + "].type']",thisControlHolder).val() == 'Paypal'){
    				paypalControlPresent = true;
    			}
    		});
    		if(paypalControlPresent){
	    		$widget.qtip({
	            	content: ppOne,
	            	position: { my: 'bottom center', at: 'top center' },
			        show: { event: false, ready: true, effect: function() { $(this).show('drop', { direction:'up'}); } },
			        hide: { event: 'click', target: $(document) },
			        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
	    	    });
				event.stopPropagation();
				return false;
    		}
    	}
    	//End->Check if there is any paypal control
    	//Start->Check if it's a formula and it's used references are deleted
    	if(thisWidgetType == 'FormulaField'){
    		var usedDelRefArr = new Array();
    		var settings = $.parseJSON(unescape($widget.find("input[id$='fields[" + index + "].settings']").val()))
    		var formulaValArr = settings[$('#language').val()].value
    		$('.deletedCtrlHolder').each(function(){
    			var $this = $(this);
    			var rel = $this.attr('rel');
    			if($.inArray($this.find("input[id$='fields["+rel+"].name']").val(),formulaValArr) > -1){
    				var itsSettings = $.parseJSON(unescape($this.find("input[id=fields["+rel+"].settings]").val()));
    				usedDelRefArr[usedDelRefArr.length] = itsSettings[$('#language').val()].label;
    			}
    		});
    		if(usedDelRefArr.length>0){
    			var message = "The Calculated Field has following reference(s) which is/are deleted: ";
    			for(var i = 0; i<usedDelRefArr.length; i++){
    				message += usedDelRefArr[i] + (i < usedDelRefArr.length-1?', ':'');
    			}
    			$widget.qtip({
                	content: message,
                	position: { my: 'bottom center', at: 'top center' },
    		        show: { event: false, ready: true, effect: function() { $(this).show('drop', { direction:'up'}); } },
    		        hide: { event: 'click', target: $(document) },
    		        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
        	    });
    			event.stopPropagation();
    			return false;
    		}
    	}
    	//End->Check if it's a formula and it's used references are deleted
        $(".closeButton",$widget).css('display','');

        // new record that not stored in database
        $widget.find("input[id$='fields[" + index + "].status']").val('');
        $widget.removeClass('deletedCtrlHolder');
        $widget.qtip('destroy');
        $widget.removeAttr('title');
        $undoDeleteButton.remove();
        event.stopPropagation();
        return false;
    },                 
    _createFieldProperties: function(name, options, settings, index) {
	
        var fieldId = 'fields[' + index + '].';
        var $fieldProperties = $('<div class="fieldProperties" id="' + index + '"> \
		<input type="hidden" id="' + fieldId + 'id" name="' + fieldId + 'id" value="null" /> \
		<input type="hidden" id="' + fieldId + 'name" name="' + fieldId + 'name" value="field' + new Date().getTime() + '" /> \
		<input type="hidden" id="' + fieldId + 'type" name="' + fieldId + 'type" value="' + options._type + '" /> \
		<input type="hidden" id="' + fieldId + 'settings" name="' + fieldId + 'settings" /> \
		<input type="hidden" id="' + fieldId + 'sequence" name="' + fieldId + 'sequence" value="' + index + '" /> \
		<input type="hidden" id="' + fieldId + 'status" name="' + fieldId + 'status" /> \
		</div>');
        $fieldProperties.find("input[id$='" + fieldId + "settings']").val($.toJSON(settings));
        return $fieldProperties;


    },
    _createFieldProperties1: function(name, options, settings, index) {
        var fieldId = 'fields[' + index + '].';
        var $fieldProperties = $('<div class="fieldProperties" id="' + index + '"> \
		<input type="hidden" id="' + fieldId + 'id" name="' + fieldId + 'id" value="null" /> \
		<input type="hidden" id="' + fieldId + 'name" name="' + fieldId + 'name" value="' + name + '" /> \
		<input type="hidden" id="' + fieldId + 'type" name="' + fieldId + 'type" value="PlainText" /> \
		<input type="hidden" id="' + fieldId + 'settings" name="' + fieldId + 'settings" /> \
		<input type="hidden" id="' + fieldId + 'sequence" name="' + fieldId + 'sequence" value="' + index + '" /> \
		<input type="hidden" id="' + fieldId + 'status" name="' + fieldId + 'status" /> \
		</div>');
        $fieldProperties.find("input[id$='" + fieldId + "settings']").val($.toJSON(settings));
        return $fieldProperties;


    },
    _updateStatus: function(event) {
        $widget = $(event.target);
        $.fb.fbWidget.prototype._log($widget.attr('id') + " updated");
        if ($widget.parent().find('input:first').val() != 'null') {
            $.fb.fbWidget.prototype._log("field status updated");
            $widget.parent().find('input:last').val('U');
        }
    },
    _createFbWidget: function(event) {
    	var thisElement = $(this);
    	var allControlHolders = $('#builderForm div.ctrlHolder:not(.deletedCtrlHolder)')
    	var totalCtrlHolders = allControlHolders.size()
    	if(maxFormControls <= totalCtrlHolders){
    		$('.' + $.fb.fbWidget.prototype.options._styleClass + ':first').qtip({
                content: 'Can not create controls more than '+maxFormControls+'. Please contact administrator.',
                position: { my: 'bottom center', at: 'top center' },
                show: {
                    event: false,
                    ready: true,
                    effect: function() {
                        $(this).show('drop', { direction:'up'});
                    }
                },
                hide: {
                	event: 'click',
                	target: $(document)
                },
                style: {
                    widget: true,
                    classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                    tip: true
                }
            });
    		event.stopPropagation();
    		return null;
    	}
    	//Start->Check if there is any paypal control
    	
    	//current control's type
    	var currentType = "";
    	var currentId = "";
    	try{
    		if(this.id){
    			currentId=this.id;
    		}
    		if(currentId == "")
    		currentType = this.options._type;
    	}catch(e){}
    	if(currentId == 'Paypal' || currentType == 'Paypal'){
    		if(ppb){
    			$("#Paypal").qtip({
	            	content: ppbMsg,
	            	position: { my: 'bottom left', at: 'top center' },
			        show: { event: false, ready: true, effect: function() { $(this).show('drop', { direction:'up'}); } },
			        hide: { event: 'click', target: $(document) },
			        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
	    	    });
				event.stopPropagation();
				return null;
    		}else{
    			var paypalControlPresent = false;
        		allControlHolders.each(function(){
        			var thisControlHolder = $(this);
        			if($("input[id$='fields[" + $(this).attr("rel") + "].type']",thisControlHolder).val() == 'Paypal'){
        				paypalControlPresent = true
        			}
        		});
        		if(paypalControlPresent){
        			$('html, body').stop().animate({
                        scrollTop: 0
                    }, 100, 'easeInOutExpo');
        			$("#Paypal").qtip({
    	            	content: ppOne,
    	            	position: { my: 'bottom left', at: 'top center' },
    			        show: { event: false, ready: true, effect: function() { $(this).show('drop', { direction:'up'}); } },
    			        hide: { event: 'click', target: $(document) },
    			        style: { widget: true, classes: 'ui-tooltip-shadow ui-tooltip-rounded', tip: true }
    	    	    });
    				event.stopPropagation();
    				return null;
        		}
    		}
    	}
    	//End->Check if there is any paypal control
    	$.fb.fbWidget.prototype._log('_createFbWidget executing. event.type = ' + event.type);
        var $this;
        if (this.options) { // from draggable, event.type == 'mousedown'
            $this = this;
        } else { // from click event
            var type = 'fb' + $(this)['fbWidget']('option', '_type');
            $.fb.fbWidget.prototype._log('type = ' + type);
            $this = $(this).data(type);
        }
        // Clone an instance of plugin's option settings.
        // From: http://stackoverflow.com/questions/122102/what-is-the-most-efficient-way-to-clone-a-javascript-object
        var settings = jQuery.extend(true, {}, $this.options.settings);
        var counter = $this._getCounter($this);
        var languages = $this.options._languages;
        for (var i = 0; i < languages.length; i++) {
            settings[languages[i]][$this.options._counterField] += ' ' + counter;
            $this._log(settings[languages[i]][$this.options._counterField]);
        }
        
        var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        index1 = (index1==0)?(index):++index1;
        var $ctrlHolder
        if(controlHolderText){
        	$ctrlHolder = $(controlHolderText);
        	$("div.ctrlHolder").parent().append($ctrlHolder);
        	$ctrlHolder.hide();
        	$ctrlHolder.toggle('slide', {direction: 'up'}, 'slow');
        	$this._scroll(event);
        	$ctrlHolder.click($this._createFieldSettings);
        	$(".closeButton",$ctrlHolder).click($.fb.fbWidget.prototype._deleteWidget)
		            .mouseover(function () {
		        $('span', this).removeClass('ui-icon-close').addClass('ui-icon-circle-close');
		    })
		            .mouseout(function () {
		        $('span', this).removeClass('ui-icon-circle-close').addClass('ui-icon-close');
		    });
        	$(".copyButton",$ctrlHolder).click($.fb.fbWidget.prototype._copyWidget)
            .mouseover(function () {
		        $('span', this).removeClass('ui-icon-plus').addClass('ui-icon-circle-plus');
		    })
		            .mouseout(function () {
		        $('span', this).removeClass('ui-icon-circle-plus').addClass('ui-icon-plus');
		    });
        	var sideBySide = document.getElementById("form.labelDisplay").value
            if(sideBySide == "1" || sideBySide == "2"){
            	try{
    	        	$(".fullLengthLabel",$ctrlHolder).removeClass('fullLengthLabel').addClass('customLengthLabel').css("text-align",(sideBySide=="1"?"":"right"));
    	        	$(".fullLengthField",$ctrlHolder).removeClass('fullLengthField').addClass('customLengthField');
    	        	if($(this).val()=="1"){
            			$(".customLengthLabel").css("text-align","");
            		}else{
            			$(".customLengthLabel").css("text-align","right");
            		}
            	}catch(e){}
            }else{
            	try{
    	        	$(".customLengthLabel",$ctrlHolder).removeClass('customLengthLabel').addClass('fullLengthLabel').css("text-align","");
    	        	$(".customLengthField",$ctrlHolder).removeClass('customLengthField').addClass('fullLengthField');
            	}catch(e){}
            }
        	if($this.options._type == 'ImageUpload'){
        		$("img",$ctrlHolder).show().load(function(){setImageWidthAndHeight(this)}).error(function(){setImageWidthAndHeight(this)});
        	}
        }else{
        	$ctrlHolder = $('<div class="' + $this.options._styleClass + '" rel="'+index1+'"></div>').hide();
            // store settings to be used in _createFieldSettings() and _languageChange()
            $ctrlHolder.data('fbWidget', settings);
            $this._log("b4. text = " + settings[$('#language').val()].text);
            var fb = {target: $this, item: $ctrlHolder, settings: settings[$('#language').val()], _settings: settings};
            var $widget = $this._getWidget(event, fb);
            $this._log("at. text = " + settings[$('#language').val()].text);
            $ctrlHolder.append($widget);
            
            var sideBySide = document.getElementById("form.labelDisplay").value
            if(sideBySide == "1" || sideBySide == "2"){
            	try{
            		//.css("width",$(".customLengthLabel").css("width"))
    	        	$(".fullLengthLabel",$ctrlHolder).removeClass('fullLengthLabel').addClass('customLengthLabel').css("text-align",(sideBySide=="1"?"":"right"));
    	        	//.css("width",$(".customLengthField").css("width"))
    	        	$(".fullLengthField",$ctrlHolder).removeClass('fullLengthField').addClass('customLengthField');
            	}catch(e){}
            }
            
            if (event.type == 'click' || event.type == 'drop') {
                var name = $this._propertyName($this.options._type + counter);
                $ctrlHolder.click($this._createFieldSettings);
                $this._createField(name, $ctrlHolder, $this.options, settings);
                if (event.type == 'click') {
                    $($.fb.formbuilder.prototype.options._formControls).append($ctrlHolder).sortable('refresh');
                    $ctrlHolder.toggle('slide', {direction: 'up'}, 'slow');
                    $this._scroll(event);
                    event.preventDefault();
                    setWidthSetter();
                } else {
                    return $ctrlHolder;
                }
            } else {
                return $ctrlHolder.show();
            }
        }
        //REMOVED FROM HERE NEELA
        $this._log('_createFbWidget executed');
    },
    _scroll: function(event) {
        var $builderPanel = $($.fb.formbuilder.prototype.options._builderPanel);
        this._log("builderPanel: min-height = " + $builderPanel.css('minHeight') + ", height = " + $builderPanel.css('height'));
        var minHeight = $builderPanel.css('minHeight');
        var height = $builderPanel.css('height');
        minHeight = minHeight.substring(0, minHeight.lastIndexOf('px')) * 1;
        height = height.substring(0, height.lastIndexOf('px')) * 1;

        if (height > minHeight) {
            this._log("builderPanel: scrolling... height: " + height + ", minHeight: " + minHeight);
            var y = height - minHeight;
            this._log("y = " + y);
            $('html, body').stop().animate({
                scrollTop: y
            }, 1500, 'easeInOutExpo');
        }
    },
    _createFieldSettings: function(event, $widget) {
    	$.fb.fbWidget.prototype._log('_createFieldSettings executing.');
        if (!$widget) { // calling from click event
            $widget = $(this);
        }
        var selectedClass = $.fb.fbWidget.prototype.options._selectedClass;
        $widget = $widget.attr('class').indexOf($.fb.fbWidget.prototype.options._styleClass) > -1 ? $widget : $widget.parent();
        $widget.parent().find('.' + selectedClass).removeClass(selectedClass);
        $widget.addClass(selectedClass);
        var name = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].name']").val();

        var type = "";
        if (name.indexOf("check") >= 0) {
            type = "CheckBox";
        }
        if (name.indexOf("group") >= 0) {
            type = "GroupButton"
        }
        if ((name.indexOf("check") < 0) && name.indexOf("group") < 0) {
            type = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].type']").val();
        }

        $.fb.fbWidget.prototype._log('type = ' + type);
        var $this = $('#' + type).data('fb' + type);

        var fbOptions = $this._getFbOptions();
        $this._log('$widget.text() = ' + $widget.text() + ", fbOptions.readOnly = " + fbOptions.readOnly);

        if (!$widget.data('fbWidget')) {
        	// widgets loaded from server
            var $settings = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].settings']");
            $this._log('_createFieldSettings. unescaped settings = ' + unescape($settings.val()));
            // settings is JavaScript encoded when return from server-side
            $widget.data('fbWidget', $.parseJSON(unescape($settings.val())));
        }
        var settings = $widget.data('fbWidget');
        var $languageSection = $(fbOptions._fieldSettingsLanguageSection);
        var $language = $('#language');
        $('legend', $languageSection).text('Widget: ' + (typeWidgetTextReplacement[type]?typeWidgetTextReplacement[type]:type));//$language.find('option:selected').text()
        var fbLanguageSection = {target: $this, item: $widget, settings: settings[$language.val()]};
        var fbGeneralSection = {target: $this, item: $widget, settings: settings};
        var fieldSettings = $this._getFieldSettingsLanguageSection(event, type=='Paypal'?fbGeneralSection:fbLanguageSection);
        // remote all child nodes except legend
        $languageSection.children(':not(legend)').remove();
        if($.inArray(type,inputTypeFields)>-1)
        	$languageSection.append($('<div><b>Unique id: '+name+'</b></div>'));
        for (var i = 0; i < fieldSettings.length; i++) {
            $languageSection.append(fieldSettings[i]);
        }
        fieldSettings = $this._getFieldSettingsGeneralSection(event, fbGeneralSection);
        $this._log('fieldSettings.length = ' + fieldSettings.length);
        var $generalSection = $(fbOptions._fieldSettingsGeneralSection);
        // remote all child nodes
        $generalSection.children().remove();
        for (var i = 0; i < fieldSettings.length; i++) {
            $generalSection.append(fieldSettings[i]);
        }

        if (fbOptions.readOnly) {

            var $fieldSettingsPanel = $(fbOptions._fieldSettingsPanel);
            $('input', $fieldSettingsPanel).attr("disabled", true);
            $('select', $fieldSettingsPanel).attr("disabled", true);
            $('textarea', $fieldSettingsPanel).attr("disabled", true);
        }

        $.fb.fbWidget.prototype._log('_createFieldSettings. event.type = ' + event.type);
        if (event.type == 'click') {
            // activate field settings tab
            $(fbOptions._paletteTabs).tabs('select', 1);

            // highlight and select the 1st input component
            $('input:first', $fieldSettingsPanel).select();
        }
        $.fb.fbWidget.prototype._log('_createFieldSettings executed.');
        var textArea = $('textarea[id$="field.text"]')
        textArea.hide();
        textArea.css("width",textArea.parent().parent().innerWidth()*1-28+"px");
        textArea.show();
        $('textarea[id$="field.text"]').htmlarea({
            loaded: function() {
                $(this.editor).keyup(function(event) {
                	//$('textarea[id$="field.text"]').trigger("keyup");
                	return true;
                });
            }
        });
        checkScroll();
    },
    _getCounter: function($this) {
        var $ctrlHolders = $('.' + $this.options._styleClass + ':visible:not(.' + this._getFbOptions()._draggableClass + ')');
        var counter = 1;
        if ($ctrlHolders.size() > 0) {
            var $ctrlHolder, index, name;

            var propertyName = $this._propertyName($this.options._type);
            $ctrlHolders.each(function(i) {
                $ctrlHolder = $(this);
                index = $ctrlHolder.attr('rel');
                name = $ctrlHolder.find("input[id$='fields[" + index + "].type']").val();
                if (name.toLowerCase().indexOf(propertyName.toLowerCase()) > -1) {
                    $this._log('Counter = ' + counter);
                    counter++;
                }
            });
        }
        $this._log('counter = ' + counter);
        return counter;
    },
    _updateSettings: function($widget) {
        var settings = $widget.data('fbWidget');
        this._log('_updateSettings: \n' + $.toJSON(settings));
        var $settings = $widget.find("input[id$='fields[" + $widget.attr('rel') + "].settings']");
        $settings.val($.toJSON(settings)).change();
    } ,
    _updateName: function($widget, value) {
    } ,
    _threeColumns: function($e1, $e2, $e3) {
        return $('<div class="threeCols"></div>')
                .append($e1.addClass('col1'))
                .append($e2.addClass('col2'))
                .append($e3.addClass('col3'));
    } ,
    _twoColumns: function($e1, $e2) {
        return $('<div class="twoCols"></div>')
                .append($e1.addClass('labelOnTop col1 noPaddingBottom'))
                .append($e2.addClass('labelOnTop col2'));
    } ,
    _oneColumn: function($e) {
        return $e.addClass('clear labelOnTop');
    } ,
    _help: function(options) {
        var $help;
        if (options.description) {
            $help = $('<span>&nbsp;(<a href="#" title="' + options.description + '">?</a>)</span>');
            var $link = $('a', $help);
            $link.qtip({
                content: $link.attr('title'),
                position: { my: 'bottom left', at: 'top center' },
                show: {
                    event: 'click',
                    effect: function() {
                        $(this).show('drop', { direction:'up'});
                    }
                },
                hide: 'mouseout',
                style: {
                    widget: true,
                    classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                    tip: true
                }
            });
        }
        return $help;
    },
    _label: function(options) {
        var $label = $('<div><label for="' + options.name + '">' + options.label + '</label></div>')
                .append(this._help(options));
        if (!options.nobreak) $label.append('<br />');
        return $label;
    },
    _horizontalAlignment: function(options) {
        var o = $.extend({}, options);
        o.label = o.label ? o.label : 'Horizontal Align';
        var $horizontalAlignment = this._label(o)
                .append('<select> \
			<option value="leftAlign">left</option> \
			<option value="centerAlign">center</option> \
			<option value="rightAlign">right</option> \
		</select>');
        $('select', $horizontalAlignment).val(o.value).attr('id', o.name);
        return $horizontalAlignment;
    },
    _verticalAlignment: function(options) {
        var o = $.extend({}, options);
        o.label = o.label ? o.label : 'Vertical Align';
        var $verticalAlignment = this._label(o)
                .append('<select> \
				<option value="topAlign">top</option> \
				<option value="middleAlign">middle</option> \
				<option value="bottomAlign">bottom</option> \
			</select></div>');
        $('select', $verticalAlignment).val(o.value).attr('id', o.name);
        return $verticalAlignment;
    },
    _name: function($widget) {
        var index = $widget.attr('rel');
        var $name = $('<label for="field.name">Name (?)</label><br/> \
 				  <input type="text" id="field.name" />');
        $("input[id$='field.name']", $name)
                .val($widget.find("input[id$='fields[" + index + "].name']").val())
                .keyup(function(event) {
            $widget.find("input[id$='fields[" + index + "].name']")
                    .val($(event.target).val()).change();
        });
        return $name;
    },
    _colorPicker: function(options) {
        var $colorPicker;
        if (options.label) {
            $colorPicker = this._label(options);
        } else {
            $colorPicker = $('<div></div>');
        }

        if (!options.type || options.type == 'basic') {
            $colorPicker.colorPicker({
                name: options.name,
                value: options.value,
                ico: imageSourceDir+'/images/jquery.colourPicker.gif',
                disabledIco: imageSourceDir+'/images/jquery.colourPicker.disabled.gif',
                title: 'Basic Colors',
                disabled: this._getFbOptions().readOnly
            });
        } else {
            $colorPicker.colorPicker({
                name: name,
                value: value,
                ico: imageSourceDir+'/images/jquery.colourPicker.gif',
                disabledIco: imageSourceDir+'/images/jquery.colourPicker.disabled.gif',
                title: 'Web Safe Colors',
                type: 'webSafe',
                width: 360,
                disabled: this._getFbOptions().readOnly
            });
        }
        return $colorPicker;
    },
    _fontPicker: function(options) {
        var o = $.extend({}, options);
        this._log('fontPicker(' + $.toJSON(o) + ')');
        o.value = o.value != 'default' ? o.value : this._getFbOptions()._fontFamily;
        if (!o.label) o.label = 'Font';
        var $fontPicker = this._label(o).append('<div class="fontPicker" rel="' + o.name + '"></div>');

        $('.fontPicker', $fontPicker).fontPicker({
            name: o.name,
            defaultFont: o.value,
            disabled: this._getFbOptions().readOnly
        });
        return $fontPicker;
    },
    _fontSize: function(options) {
        this._log('fontSize(' + $.toJSON(options) + ')');
        options.nobreak = true;
        var $fontSize = this._label(options).append('&nbsp;<select> \
		    <option value="9">9</option> \
		    <option value="10">10</option> \
		    <option value="11">11</option> \
		    <option value="12">12</option> \
		    <option value="13">13</option> \
		    <option value="14">14</option> \
		    <option value="15">15</option> \
		    <option value="16">16</option> \
		    <option value="17">17</option> \
		    <option value="18">18</option> \
		    <option value="20">20</option> \
		    <option value="22">22</option> \
		    <option value="24">24</option> \
		    <option value="28">28</option> \
		    <option value="32">32</option> \
		  </select>');
        var $select = $('select', $fontSize);
        if (options.value == 'default') {
            $select.val(this._getFbOptions().settings.styles.fontSize);
        } else {
            $select.val(options.value);
        }
        $select.attr('id', options.name);
        return $fontSize;
    } ,
    _fontStyles: function(options) {
        var names = options.names;
        var checked = options.checked;
        var $fontStyles = $('<div> \
		  <input type="checkbox" id="' + names[0] + '" />&nbsp;Bold<br /> \
	    <input type="checkbox" id="' + names[1] + '" />&nbsp;Italic<br /> \
	    <input type="checkbox" id="' + names[2] + '" />&nbsp;Underline \
	  </div>');
        if (checked) {
            for (var i = 0; i < checked.length; i++) {
                $("input[id$='" + names[i] + "']", $fontStyles).attr('checked', checked[i]);
            }
        }
        return $fontStyles;
    },
    _twoRowsOneRow: function(row1col1, row2col1, row1col2) {
        var $twoRowsOneRow = $('<div class="twoRowsOneRow"> \
			    <div class="row1col1"> \
			      <div class="row2col1"> \
			      </div> \
			    </div> \
			    <div class="row1col2"> \
			    </div> \
			  </div>');
        $('.row1col1', $twoRowsOneRow).prepend(row1col1);
        $('.row2col1', $twoRowsOneRow).append(row2col1);
        $('.row1col2', $twoRowsOneRow).append(row1col2);
        return $twoRowsOneRow;
    },
    _fieldset: function(options) {
        return $('<fieldset '+(options['class']?('class="'+options['class']+'"'):'')+'><legend>' + options.text + '</legend></fieldset>');
    },
    _fontPanel:function(options) {
        //fontFamily, fontSize, styles.fontStyles
        var idPrefix = options.idPrefix ? options.idPrefix : '';
        var names = [idPrefix + 'bold', idPrefix + 'italic', idPrefix + 'underline'];
        var fontPanel = this._twoRowsOneRow(
                this._fontPicker({ name: idPrefix + 'fontFamily', value: options.fontFamily }),
                this._fontSize({ label: 'Size', name: idPrefix + 'fontSize', value: options.fontSize }),
                this._fontStyles({ names: names, checked: options.fontStyles }).css('paddingLeft', '2em')
                );
        if (options.nofieldset)
            return fontPanel;
        else
            return this._fieldset({ text: 'Fonts' }).append(fontPanel);
    },
    _colorPanel: function(options) {
        //textColorValue, backgroundColorValue, idPrefix
        var o = $.extend({}, options);
        if (o.form.color == 'default') {
            o.form.color = this._getFbOptions().settings.styles.color;
        }
        if (o.form.backgroundColor == 'default' || o.form.backgroundColor == 'rgba(0, 0, 0, 0)') {
            o.form.backgroundColor = 'transparent';
        }
        var $colorPanel = this._fieldset({ text: 'Colors' })
//        	.append(
//                this._twoColumns(this._colorPicker({ label: 'Text', name: o.idPrefix + 'color', value: o.color }),
//                        this._colorPicker({ label: 'Background', name: o.idPrefix + 'backgroundColor', value: o.backgroundColor }))
//	        )
	        .append(this._threeColumns($('<div></div>'), $('<div>Text</div>'), $('<div>Background</div>')).css('paddingBottom', '2px'))
	        .append(this._threeColumns($('<div>Form</div>'),
                this._colorPicker({ name: 'form.color', value: o.form.color }),
                this._colorPicker({ name: 'form.backgroundColor', value: o.form.backgroundColor })))
            .append(this._threeColumns($('<div>Heading</div>'),
                this._colorPicker({ name: 'heading.color', value: o.heading.color }),
                this._colorPicker({ name: 'heading.backgroundColor', value: o.heading.backgroundColor })))
            .append(this._threeColumns($('<div>Description</div>'),
                this._colorPicker({ name: 'description.color', value: o.description.color }),
                this._colorPicker({ name: 'description.backgroundColor', value: o.description.backgroundColor })));
        $colorPanel.css('paddingTop', '0.5em');
        $('input', $colorPanel).addClass('floatClearLeft');
        $('.col1', $colorPanel).css('verticalAlign', 'top');
//        $colorPanel.find('.2cols .col2').addClass('noPaddingBottom');
//        $colorPanel.find('input:first').addClass('floatClearLeft');
        return $colorPanel;
    },
    //label.color, label.backgroundColor, value.color, value.backgroundColor, description.color, description.backgroundColor
    _labelValueDescriptionColorPanel: function(options) {
        var o = $.extend({}, options);
        var fbStyles = this._getFbOptions().settings.styles;
        if (o.label.color == 'default') {
            o.label.color = fbStyles.color;
        }
        if (o.label.backgroundColor == 'default') {
            o.label.backgroundColor = fbStyles.backgroundColor;
        }
        if (o.description.color == 'default') {
            o.description.color = fbStyles.color;
        }
        if (o.description.backgroundColor == 'default') {
            o.description.backgroundColor = fbStyles.backgroundColor;
        }
        var $colorPanel = this._fieldset({ text: 'Colors' })
                .append(this._threeColumns($('<div></div>'), $('<div>Text</div>'), $('<div>Background</div>')).css('paddingBottom', '2px'))
                .append(this._threeColumns($('<div>Label</div>'),
                this._colorPicker({ name: 'field.label.color', value: o.label.color }),
                this._colorPicker({ name: 'field.label.backgroundColor', value: o.label.backgroundColor })))
                .append(this._threeColumns($('<div>Value</div>'),
                this._colorPicker({ name: 'field.value.color', value: o.value.color }),
                this._colorPicker({ name: 'field.value.backgroundColor', value: o.value.backgroundColor })))
                .append(this._threeColumns($('<div>Description</div>'),
                this._colorPicker({ name: 'field.description.color', value: o.description.color }),
                this._colorPicker({ name: 'field.description.backgroundColor', value: o.description.backgroundColor })));
        $colorPanel.css('paddingTop', '0.5em');
        $('input', $colorPanel).addClass('floatClearLeft');
        $('.col1', $colorPanel).css('verticalAlign', 'top');
        return $colorPanel;
    },
    _labelColorPanel: function(options) {
        var o = $.extend({}, options);
        var fbStyles = this._getFbOptions().settings.styles;
        if (o.color == 'default') {
            o.color = fbStyles.color;
        }
        if (o.backgroundColor == 'default') {
            o.backgroundColor = fbStyles.backgroundColor;
        }
        var $colorPanel = this._fieldset({ text: 'Colors' })
                .append(this._threeColumns($('<div></div>'), $('<div>Text</div>'), $('<div>Background</div>')).css('paddingBottom', '2px'))
                .append(this._threeColumns($('<div>Label</div>'),
                this._colorPicker({ name: 'field.color', value: o.color }),
                this._colorPicker({ name: 'field.backgroundColor', value: o.backgroundColor })))
        $colorPanel.css('paddingTop', '0.5em');
        $('input', $colorPanel).addClass('floatClearLeft');
        $('.col1', $colorPanel).css('verticalAlign', 'top');
        return $colorPanel;
    },
    _getWidget: function(event, fb) {
        $.fb.fbWidget.prototype._log('getWidget(event, fb) should be overriden by subclass.');
    },
    _getFieldSettingsLanguageSection: function(event, fb) {
        $.fb.fbWidget.prototype._log('getFieldSettingsLanguageSection(event, fb) should be overriden by subclass.');
    },
    _getFieldSettingsGeneralSection: function(event, fb) {
        $.fb.fbWidget.prototype._log('getFieldSettingsLanguageSection(event, fb) should be overriden by subclass.');
    } ,
    _languageChange : function(event, fb) {
        $.fb.fbWidget.prototype._log('_languageChange(event, fb) should be overriden by subclass.');
    }
};

$.widget('fb.fbWidget', FbWidget);

var ColorPicker = {
    options : { // default options. values are stored in widget's prototype
        name: 'jquery-color-picker',
        value: '',
        ico:          'ico.gif',                // SRC to color-picker icon
        disabledIco: 'ico.gif',
        title:        'Pick a colour',        // Default dialogue title
        inputBG:    true,                    // Whether to change the input's background to the selected colour's
        showColorCode: false, // whether to display 6 digits color code in text box, value stored in title attr.
        speed:        500,                    // Speed of dialogue-animation
        openTxt:    'Open colour picker',
        type: 'basic',  // color picker panel type: 'basic', 'webSafe' and 'custom' supported
        width: 180,
        basicColors: ["ffffff","ffccc9", "ffce93", "fffc9e", "ffffc7",
            "9aff99", "96fffb", "cdffff", "cbcefb", "cfcfcf",
            "fd6864", "fe996b", "fffe65", "fcff2f", "67fd9a",
            "38fff8", "68fdff", "9698ed", "c0c0c0", "fe0000",
            "f8a102", "ffcc67", "f8ff00", "34ff34", "68cbd0",
            "34cdf9", "6665cd", "9b9b9b", "cb0000", "f56b00",
            "ffcb2f", "ffc702", "32cb00", "00d2cb",  "3166ff",
            "6434fc", "656565", "9a0000", "ce6301", "cd9934",
            "999903", "009901", "329a9d", "3531ff", "6200c9",
            "343434", "680100",  "963400", "986536", "646809",
            "036400",  "34696d", "00009b",  "303498", "000000",
            "330001",  "643403", "663234", "343300", "013300",
            "003532", "010066", "340096"],
        webSafeColors: ["000000","000033","000066","000099","0000CC","0000FF",
            "003300","003333","003366","003399","0033CC",
            "0033FF","006600","006633","006666","006699",
            "0066CC","0066FF","009900","009933","009966",
            "009999","0099CC","0099FF","00CC00","00CC33",
            "00CC66","00CC99","00CCCC","00CCFF","00FF00",
            "00FF33","00FF66","00FF99","00FFCC","00FFFF",
            "330000","330033","330066","330099","3300CC",
            "3300FF","333300","333333","333366","333399",
            "3333CC","3333FF","336600","336633","336666",
            "336699","3366CC","3366FF","339900","339933",
            "339966","339999","3399CC","3399FF","33CC00",
            "33CC33","33CC66","33CC99","33CCCC","33CCFF",
            "33FF00","33FF33","33FF66","33FF99","33FFCC",
            "33FFFF","660000","660033","660066","660099",
            "6600CC","6600FF","663300","663333","663366",
            "663399","6633CC","6633FF","666600","666633",
            "666666","666699","6666CC","6666FF","669900",
            "669933","669966","669999","6699CC","6699FF",
            "66CC00","66CC33","66CC66","66CC99","66CCCC",
            "66CCFF","66FF00","66FF33","66FF66","66FF99",
            "66FFCC","66FFFF","990000","990033","990066",
            "990099","9900CC","9900FF","993300","993333",
            "993366","993399","9933CC","9933FF","996600",
            "996633","996666","996699","9966CC","9966FF",
            "999900","999933","999966","999999","9999CC",
            "9999FF","99CC00","99CC33","99CC66","99CC99",
            "99CCCC","99CCFF","99FF00","99FF33","99FF66",
            "99FF99","99FFCC","99FFFF","CC0000","CC0033",
            "CC0066","CC0099","CC00CC","CC00FF","CC3300",
            "CC3333","CC3366","CC3399","CC33CC","CC33FF",
            "CC6600","CC6633","CC6666","CC6699","CC66CC",
            "CC66FF","CC9900","CC9933","CC9966","CC9999",
            "CC99CC","CC99FF","CCCC00","CCCC33","CCCC66",
            "CCCC99","CCCCCC","CCCCFF","CCFF00","CCFF33",
            "CCFF66","CCFF99","CCFFCC","CCFFFF","FF0000",
            "FF0033","FF0066","FF0099","FF00CC","FF00FF",
            "FF3300","FF3333","FF3366","FF3399","FF33CC",
            "FF33FF","FF6600","FF6633","FF6666","FF6699",
            "FF66CC","FF66FF","FF9900","FF9933","FF9966",
            "FF9999","FF99CC","FF99FF","FFCC00","FFCC33",
            "FFCC66","FFCC99","FFCCCC","FFCCFF","FFFF00",
            "FFFF33","FFFF66","FFFF99","FFFFCC","FFFFFF"],
        customColors:[],
        disabled: false
    },
    _init : function() {
        // called on construction and re-initialization
        var ico = this.options.disabled ? this.options.disabledIco : this.options.ico;
        this._log('ColorPicker._init called.');
        // Add the colour-picker dialogue if not added
        this.element.append('<input type="text" id="' + this.options.name + '" name="' + this.options.name + '" /> \
				<a class="floatLeft" href="#" rel="' + this.options.name + '"> \
				<img border="0" src="' + ico + '" alt="' + this.options.openTxt + '"/></a>');
        var $colorPickerInput = $('input', this.element).attr('readonly', true);
        var $this = this.element.data('colorPicker');
        var id = "colorpicker_" + this.options.type;

        if ($this.options.showColorCode) {
            $colorPickerInput.val(this.options.value);
        } else {
            $colorPickerInput.attr('disabled', 'true');
        }

        $colorPickerInput.data('colorPicker', { color: this.options.value });

        var $colorPickerPanel = $('#' + id);

        if ($colorPickerPanel.length == 0 && !this.options.disabled) {
            var loc = '';
            var colors = this.options[this.options.type + 'Colors'];
            var color;
            loc += '<li style="width:100%;background-repeat: no-repeat;"><a class="noColor" href="#" style="background-image: url('+imageSourceDir+'/images/transparent1.png);" title="None" rel="transparent">'
                + 'None</a></li>';
            for (var i = 0; i < colors.length; i++) {
                color = colors[i];
                if (color != '')
                    loc += '<li><a href="#" title="'
                            + color
                            + '" rel="'
                            + color
                            + '" style="background: #'
                            + color
                            + '; color: '
                            + this._hexInvert(color)
                            + ';">'
                            + color
                            + '</a></li>';
            }
            var heading = this.options.title ? '<h2>' + this.options.title + '</h2>' : '';
            $colorPickerPanel = $('<div id="' + id + '" class="colorPicker">' + heading + '<ul>' + loc + '</ul>' + '</div>').appendTo(document.body).hide();

            // Remove the colour-picker panel if you click outside it (on body)
            $(document.body).click(function(event) {
                if (!($(event.target).is('.colorPicker') || $(event.target).parents('.colorPicker').length)) {
                    $colorPickerPanel.hide($this.options.speed);
                }
            });
        }

        if (this.options.inputBG) {
            var colorCode = this.options.value;
            if (colorCode.length == 6) colorCode = '#' + colorCode;
            if(colorCode=='transparent')
            	$colorPickerInput.css({background: 'url('+imageSourceDir+'/images/transparent1.png)', color: colorCode});
            else
            	$colorPickerInput.css({background: colorCode, color: '#' + this._hexInvert(colorCode)});
        }

        if (!this.options.disabled) {
            var $colorPickerIcon = $('a', this.element);
            $colorPickerIcon.click(function () {
                // Show the colour-picker next to the icon and fill it with the colours in the select that used to be there
            	if(this.className == 'noColor'){
            		var iconPos = $colorPickerIcon.offset();
	                $colorPickerPanel.css({
	                    position: 'absolute',
	                    left: iconPos.left + 'px',
	                    top: iconPos.top + 'px'
	                }).show($this.options.speed).attr('rel', $colorPickerIcon.attr('rel'));
            	}else{
	                var iconPos = $colorPickerIcon.offset();
	                $colorPickerPanel.css({
	                    position: 'absolute',
	                    left: iconPos.left + 'px',
	                    top: iconPos.top + 'px',
	                    width: $this.options.width + 'px'
	                }).show($this.options.speed).attr('rel', $colorPickerIcon.attr('rel'));
            	}
                return false;
            });
        }

        // When you click a color in the color picker panel
        $('a', $colorPickerPanel).click(function (event) {
            // The hex is stored in the link's rel-attribute
        	 var $colorInput = $("input[id$='" + $colorPickerPanel.attr('rel') + "']");
         	// The hex is stored in the link's rel-attribute
         	 var hex = $(this).attr('rel');
         	if(this.className=='noColor'){
 	            //$.fb.colorPicker.prototype._log('colorPicker. input id = ' + $colorPickerPanel.attr('rel') + ', hex = ' + hex + ', title = ' + $(this).attr('title') + ', text = ' + $(this).text());
 	            var options = $colorInput.parent().data('colorPicker').options;
 	            if (options.showColorCode) {
 	                $colorInput.val(hex);
 	            }
 	
 	            $colorInput.data('colorPicker', { color: hex });
 	
 	            // If user wants to, change the input's BG to reflect the newly selected colour
 	            if (options.inputBG) {
 	                $colorInput.css({background: 'url('+imageSourceDir+'/images/transparent1.png)' , color: hex});
 	            }
 	
 	            // Trigger change-event on input
 	            $colorInput.change();
         	}else{
 	            $.fb.colorPicker.prototype._log('colorPicker. input id = ' + $colorPickerPanel.attr('rel') + ', hex = ' + hex + ', title = ' + $(this).attr('title') + ', text = ' + $(this).text());
 	            var options = $colorInput.parent().data('colorPicker').options;
 	            if (options.showColorCode) {
 	                $colorInput.val(hex);
 	            }
 	
 	            $colorInput.data('colorPicker', { color: hex });
 	
 	            // If user wants to, change the input's BG to reflect the newly selected colour
 	            if (options.inputBG) {
 	                $colorInput.css({background: '#' + hex, color: '#' + $this._hexInvert(hex)});
 	            }
 	
 	            // Trigger change-event on input
 	            $colorInput.change();
         	}
            // Hide the colour-picker and return false
            $colorPickerPanel.hide($this.options.speed);
            event.stopImmediatePropagation();
            return false;
        });

    },
    // logging to the firebug's console, put in 1 line so it can be removed
    // easily for production
    _log : function($message) {
        if (window.console && window.console.log) window.console.log($message);
    },
    _hexInvert: function (hex) {
        var r = hex.substr(0, 2);
        var g = hex.substr(2, 2);
        var b = hex.substr(4, 2);

        return 0.212671 * r + 0.715160 * g + 0.072169 * b < 0.5 ? 'ffffff' : '000000';
    }
};

$.widget('fb.colorPicker', ColorPicker);

var FontPicker = {
    fonts: new Array(
    		'',
            'Arial,Arial,Helvetica,sans-serif',
            'Arial Black,Arial Black,Gadget,sans-serif',
            'Comic Sans MS,Comic Sans MS,cursive',
            'Courier New,Courier New,Courier,monospace',
            'Georgia,Georgia,serif',
            'Impact,Charcoal,sans-serif',
            'Lucida Console,Monaco,monospace',
            'Lucida Sans Unicode,Lucida Grande,sans-serif',
            'Palatino Linotype,Book Antiqua,Palatino,serif',
            'Tahoma,Geneva,sans-serif',
            'Times New Roman,Times,serif',
            'Trebuchet MS,Helvetica,sans-serif',
            'Verdana,Geneva,sans-serif'),
    options : { // default options. values are stored in widget's prototype
        defaultFont: 'Tahoma',              // default font to display in selector
        id:             'fontbox',                // id of font picker container
        name:    'fontPickerInput',
        selClass:         'fontPicker',        // class of font selector field
        fontclass:   'singlefont',            // class for the font divs
        speed:         100,                    // speed of dialog animation, default is fast
        hoverColor:  '#efefff',                // background color of font div on mouse hover
        bgColor:     '#ffffee',              // regular background color of font div
        disabled: false
    },
    // logging to the firebug's console, put in 1 line so it can be removed
    // easily for production
    _log : function($message) {
        if (window.console && window.console.log) window.console.log($message);
    },
    _init : function() {
        var options = this.options;
        var fontPicker = $('#' + options.id);
        this.element.parent().append('<input type="hidden" id="' + options.name + '" />');
        if (!fontPicker.length && !options.disabled) {
            fontPicker = $('<div id="' + options.id + '" ></div>').appendTo(document.body).hide();

            /* add individual font divs to fontbox */
            $.each(this.fonts, function(i, item) {

                fontPicker.append('<div class="singlefont" onmouseover="this.style.backgroundColor=\'' + options.hoverColor
                        + '\'" onmouseout="this.style.backgroundColor=\'' + options.bgColor + '\'" style="font-family: ' + item + ';" value="' + item + '"> ' + (item==''?'Theme Default Font':item.split(',')[0]) + '</div>');
            });

            // Remove the font-picker if you click outside it (on body)
            $(document.body).click(function(event) {
                if ($(event.target).is('.' + options.selClass) || $(event.target).is('#' + options.id)) return;
                fontPicker.slideUp(options.speed);
            });
        }

        if (!options.disabled) {
            this.element.click(function () {
                // toggle the font picker
                if (fontPicker.is(':hidden')) {
                    var $this = $(this);
                    fontPicker.css({
                        position: 'absolute',
                        left: $this.offset().left + 'px',
                        top: ($this.offset().top + $this.height() + 3) + 'px'
                    }).attr('rel', $this.attr('rel'));
                    fontPicker.slideDown(options.speed);
                }
                else
                    fontPicker.slideUp(options.speed);
            });
        }
        // select initial value
        if (options.defaultFont.length) {
            this.fontFamily(options.defaultFont);
        }

        $('.' + options.fontclass).click(function(event) {
            var $this = $(this);
            var $fontPickerInput = $("input[id$='" + $this.parent().attr('rel') + "']");
            var fontFamily = $this.attr('value');
        	$fontPickerInput.prev().text($this.text()).css('fontFamily', fontFamily);
            $fontPickerInput.val(fontFamily).change();
            fontPicker.slideUp(options.speed);
            event.stopImmediatePropagation();
        });

    },
    fontFamily : function(value) {
        this._log('fontFamily(' + value + ')');
        var fontFamilyValue = value.replace(/'/gi, ''); // remove single quote for chrome browser
        var fontFamilyText = fontFamilyValue.split(',', 1)[0]; // taking the 1st font type
        this._log('fontFamilyValue = ' + fontFamilyValue + ', fontFamilyText = ' + fontFamilyText);
        this.element.text(fontFamilyText).css('fontFamily', fontFamilyValue);
        $('input', this.element.parent()).val(fontFamilyValue);
    }
};

$.widget('fb.fontPicker', FontPicker);

var checkBoxid = "checkBox";
var FbCheckBox = $.extend({}, $.fb.fbWidget.prototype, {
        options: { // default options. values are stored in widget's prototype
                  name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/checkbox.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Checkbox</span>',
                belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
                _type: 'CheckBox',
                _html: '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label><p style="line-height:5px;">&nbsp;</p></div> \
                		<div class="fullLengthField"><div><input type="checkbox" name="mycheckbox" value="My Check 1"><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">My Check 1</span><br></div>\
						<div><input type="checkbox" name="mycheckbox" value="My Check 2" ><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">My Check 2</span><br></div>\
						<div><input type="checkbox" name="mycheckbox" value="My Check 3" ><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">My Check 3</span><br></div>\
                	    <div style="display:none;"><input type="text" name="mycheckbox" placeholder="Other" style="width:auto;"/></div>\
		                </div><p class="formHint" style="color: #777777; background-color: #transparent;"></p></div>',
                _counterField: 'field1',
                _languages: [ 'en', 'zh_CN' ],
                settings: {
                        en: {
                                label: 'Check Box',
                                value: ["My Check 1","My Check 2","My Check 3"],
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
                        zh_CN: {
                                label: '',
								value: '',
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
						_persistable: true,
                        required: false,
                        hideFromUser: false,
                        fieldLayout:'oneCol',
                        otherOption:false,
                        restriction: 'no',
                        _randomize:false,
                        styles: {
                                color: 'default',
                                backgroundColor: 'default',
								label: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
								value: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
                                description: {
                                        color : '777777',
                                        backgroundColor : 'default'
								}
                        }
                }
        },

    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
    	
    	var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        (index1==0)?(index1 = index):index1;
    	
                var $jqueryObject = $(fb.target.options._html);
                fb.target._log('fbCheckBox._getWidget executing...');
				$('label span', $jqueryObject).text(fb.settings.label);
                if (fb._settings.required) {
                        $('label em', $jqueryObject).text('*'); 
                }
				var counter = 1;
				$('input[type=checkbox]',$($jqueryObject)).each(function() {
					$(this).attr("id","checkboxid_"+index1+"_"+counter);
					$(this).removeAttr('checked')
					$(this).val(fb.settings.value[counter-1]);
					counter =  counter+1;
				});

				$('.formHint', $jqueryObject).text(fb.settings.description);
                fb.target._log('fbCheckBox._getWidget executed.');
                return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
    	var $jqueryObjects = [];
		fb.target._log('fbCheckBox._getFieldSettingsLanguageSection executing...');
 
		var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
				 .append('<textarea id="field.label" />');
		$('textarea', $label).val(fb.settings.label)
		  .keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('label span').text(value);
				  fb.settings.label = value;
				  fb.target._updateSettings(fb.item);
				  fb.target._updateName(fb.item, value);
			 });

		var counter = 1;
		var str = "<div >";
		$('input[type=checkbox]',fb.item).each(function() {
				$(this).val(fb.settings.value[counter-1])
				if(counter==1){
					str = str + '<div id="ws' + $(this).attr("id") + '"><input style="width:180px" type="text" id="txt'+$(this).attr("id")+'" /><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a></div>';
				}else{
					str = str + '<div id="ws' + $(this).attr("id") + '"><input style="width:180px" type="text" id="txt'+$(this).attr("id")+'" /><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>';					
				}
				counter = counter +1;
		});
		str = str +"</div>";
		var $choice = $(str)
		var $choicePanel = fb.target._fieldset({ text: 'Choices'})
                .append(fb.target._oneColumn($choice));
		var counter = 1;
		$('input[type=text]',$choicePanel).each(function() {
			$(this).val(fb.settings.value[counter-1])
			.keyup(function(event) {
			  var cid = ($(this).attr("id")).replace("txt","");
			  $("#"+cid).val($(this).val());
			  $('span',$("#"+cid).parent()).text($(this).val())
			  var value = new Array();
			  //fb.item.find('.textInput').val(value);
			  var count=0;
			  $('input[type=checkbox]',fb.item).each(function() {
					value[count] = jQuery.trim($(this).val());
					count++;
			  });
			  fb.settings.value = value;
			  fb.target._updateSettings(fb.item);
			});			
			counter = counter + 1;
		});
		
		var choiceRemove = function(){
			var confirmation = confirm("Are you sure?")
			if(confirmation){
				var value=new Array();
			    var curid = $(this).attr('id').replace('ChoRem','');
			    var wsDiv = $('#ws' + curid);
			    	wsDiv.remove();
			    $('#'+curid).parent().remove();
			    var count = 0;
			    $('input[type=checkbox]',fb.item).each(function() {
					value[count] = jQuery.trim($(this).val());
					count++;
			    });
			    fb.settings.value = value;
			    fb.target._updateSettings(fb.item);
			}
			return false;
		}
		$('a[class="RemoveChoice"]',$choice).click(choiceRemove);
		var choiceAdd = function(){
			var value=new Array();
			var uniqid = new Date().getTime();
		    var curid = $(this).attr('id').replace('ChoAdd','');
		    $('#ws' + curid).after('<div id="ws' + uniqid + '"><input type="Text" style="width:180px" value="New check Box" id="txt' + uniqid + '"><a href="#" class="AddChoice" id="ChoAdd'+uniqid+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem' + uniqid + '"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>');
		    $('#'+curid).parent().after('<div><input type="checkbox" name="mycheckbox" value="New Check Box" id="'+uniqid+'" ><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;"> New Check Box &nbsp;&nbsp;</span><br></div>');
		    var wsDiv = $('#ws' + uniqid)
		    $('a[class="RemoveChoice"]',wsDiv).click(choiceRemove);
		    $('a[class="AddChoice"]',wsDiv).click(choiceAdd);
		    $('#txt'+uniqid).keyup(function(event) {
				  var cid = ($(this).attr("id")).replace("txt","");
				  $("#"+cid).val($(this).val());
				  $('span',$("#"+cid).parent()).text($(this).val());
				  var value = new Array();
				  //fb.item.find('.textInput').val(value);
				  var count=0;
				  $('input[type=checkbox]',fb.item).each(function() {
					  value[count] = jQuery.trim($(this).val());
					  count++;
				  });
				  fb.settings.value = value;
				  fb.target._updateSettings(fb.item);
				});
		    var count = 0;
		    $('input[type=checkbox]',fb.item).each(function() {
				value[count] = jQuery.trim($(this).val());
				count++;
			});
			fb.settings.value = value;
		    fb.target._updateSettings(fb.item);
		    return false;
		}
		$('a[class="AddChoice"]',$choice).click(choiceAdd);
			
		//---------------------------------------------------------------------
		var $description = fb.target._label({ label: 'Description', name: 'field.description' })
		 .append('<textarea id="field.description"></textarea>');
		 $('textarea', $description).val(fb.settings.description)
				.keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('.formHint').text(value);
				  fb.settings.description = value;
				  fb.target._updateSettings(fb.item);
		});             
						
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbCheckBox._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),fb.target._oneColumn($description),$choicePanel, $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbCheckBox._getFieldSettingsGeneralSection executing...');
        var $fieldLayoutLabel = $('<div>Field Layout</div>');
        var $fieldLayoutDD = $('<div><select id="field.layout" style="width: 99%"> \
				<option value="oneCol">One Column</option> \
				<option value="twoCol">Two Column</option> \
				<option value="threeCol">Three Column</option> \
        		<option value="sbs">Side by Side</option> \
			</select></div>');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $otherOption = $('<div><input type="checkbox" id="field.showOther" />&nbsp;Show Other Option</div>');
        var $randomize= $('<div><input type="checkbox" id="field.randomize" />&nbsp;Randomize</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        		.append(fb.target._twoColumns($fieldLayoutLabel, $fieldLayoutDD))
                .append(fb.target._twoColumns($required, $hideFromUser))
                .append(fb.target._twoColumns($randomize, $otherOption))
               // .append($hideFromUser)
        $('.col1', $valuePanel).css('width', '35%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        $('input', $randomize).attr('checked', fb.settings._randomize?fb.settings._randomize:false)
        .change(function(event) {
            if ($(this).attr('checked')) {
                 fb.settings._randomize = true;
            } else {
            	fb.settings._randomize= false;
            }
            fb.target._updateSettings(fb.item);
        });
        $('input', $randomize).trigger('change');

        $('input', $otherOption).attr('checked', fb.settings.otherOption?fb.settings.otherOption:false)
        .change(function(event) {
            if ($(this).attr('checked')) {
            	 var inputPar = fb.item.find('input[type=text]').parent()
            	 inputPar.show()
                 fb.settings.otherOption = true;
            } else {
            	var inputPar = fb.item.find('input[type=text]').parent()
           	 	inputPar.hide()
                fb.settings.otherOption = false;
            }
            fb.target._updateSettings(fb.item);
        });
        $('input', $otherOption).trigger('change');

        $('input', $required).attr('checked', fb.settings.required)
                .change(function(event) {
		            if ($(this).attr('checked')) {
		            	$('input', $hideFromUser).attr('disabled','disabled');
		        		fb.item.find('em').text(' *');
		                fb.settings.required = true;
		            } else {
		            	$('input', $hideFromUser).removeAttr('disabled');
		                fb.item.find('em').text('');
		                fb.settings.required = false;
		            }
		            fb.target._updateSettings(fb.item);
		        });
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        
        if(!fb.settings.fieldLayout){
        	fb.settings.fieldLayout = "oneCol"
        }
        $('select', $fieldLayoutDD).val(fb.settings.fieldLayout).change(function(event) {
            fb.settings.fieldLayout = $(this).val();
            var inputPar = fb.item.find('input[type="checkbox"]').parent().parent();
            if($(inputPar).hasClass("oneCol")){
            	$(inputPar).removeClass("oneCol")
            }else if(inputPar.hasClass("twoCol")){
            	$(inputPar).removeClass("twoCol")
            }else if(inputPar.hasClass("threeCol")){
            	$(inputPar).removeClass("threeCol")
            }else if(inputPar.hasClass("sbs")){
            	$(inputPar).removeClass("sbs")
            }
            $(inputPar).addClass(fb.settings.fieldLayout)
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbCheckBox._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbCheckBox.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbCheckBox.languageChange executed.');
    }
});

$.widget('fb.fbCheckBox', FbCheckBox);

var formulaField = "formulaField";
var FbFormulaField = $.extend({}, $.fb.fbWidget.prototype, {
        options: { // default options. values are stored in widget's prototype
                  name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/streamlined/icons/calculator.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Formula Field</span>',
                belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
                _type: 'FormulaField',
                _html: '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div> \
                		<div class="fullLengthField mClass"><input type="text" disabled value="Calculated Field" class="textInput" />\
						<p class="formHint"></p></div></div>',
                _counterField: 'field1',
                _languages: [ 'en', 'zh_CN' ],
                settings: {
                        en: {
                                label: 'Formula',
                                oldResultType:'',
                                newResultType:'NumberResult',
                                value: [],
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
                        zh_CN: {
                                label: '',
                                oldResultType:'',
                                newResultType:'NumberResult',
                                value: [],
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
                        isEditable: true,
						_persistable: true,
                        required: false,
                        hideFromUser: false,
                        fieldSize:'mClass',
                        restriction: 'no',
                        styles: {
                                color: 'default',
                                backgroundColor: 'default',
								label: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
								value: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
                                description: {
                                        color : '777777',
                                        backgroundColor : 'default'
								}
                        }
                }
        },

    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
    	
    	var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        (index1==0)?(index1 = index):index1;
    	
                var $jqueryObject = $(fb.target.options._html);
                fb.target._log('fbFormulaField._getWidget executing...');
				$('label span', $jqueryObject).text(fb.settings.label);
                
                //Consider value of the formula here.
                
				$('.formHint', $jqueryObject).text(fb.settings.description);
                fb.target._log('fbFormulaField._getWidget executed.');
                return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
    	var $jqueryObjects = [];
		fb.target._log('fbFormulaField._getFieldSettingsLanguageSection executing...');

		var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
				 .append('<input type="text" id="field.label" />');
		$('input', $label).val(fb.settings.label)
		  .keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('label span').text(value);
				  fb.settings.label = value;
				  fb.target._updateSettings(fb.item);
				  fb.target._updateName(fb.item, value);
			 });
		var $description = fb.target._label({ label: 'Description', name: 'field.description' })
		 .append('<textarea id="field.description"></textarea>');
		 $('textarea', $description).val(fb.settings.description)
				.keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('.formHint').text(value);
				  fb.settings.description = value;
				  fb.target._updateSettings(fb.item);
		});
		//------Value Text Area------------------------------------------------
		var $formulaValue = $('<div><textarea id="field.value" disabled></textarea></div>');
		var addToValue = function(item){
			var actValArrLen = fb.settings.value.length;
			if(item == '&lt;-'){
				if(!isNaN(fb.settings.value[actValArrLen-1]) && fb.settings.value[actValArrLen-1].length>1){
					fb.settings.value[actValArrLen-1] = fb.settings.value[actValArrLen-1].substring(0,fb.settings.value[actValArrLen-1].length-1);
				}else{
					fb.settings.value.splice(actValArrLen-1,1);
				}
			}else{
				if( actValArrLen == 0 ){
					fb.settings.value[actValArrLen] = ""+item;
				}else{
					if(!isNaN(item) && !isNaN(fb.settings.value[actValArrLen-1])){
						fb.settings.value[actValArrLen-1] = fb.settings.value[actValArrLen-1]+item;
					}else{
						fb.settings.value[actValArrLen] = ""+item;
					}
				}
			}
			setValue();
		}
		var setValue = function(){
			var value = '';
			var isNumericResult = true;
			for(var i = 0; i<fb.settings.value.length; i++){
				var currItem = fb.settings.value[i]
				if(!isNaN(currItem) || currItem == '+' || currItem == '+' || currItem == '-' || currItem == '*' || currItem == '/' || currItem == '(' || currItem == ')'){
					value += fb.settings.value[i]+' ';
				}else{
					var $button = $('#button'+currItem,$allFieldsButtons);
					value += '('+($button.val() == undefined?'Field Deleted':$button.val())+') ';
					if($button.attr('itsType') == 'SingleLineDate'){
						isNumericResult = !isNumericResult;
					}
				}
			}
			if(isNumericResult){
				fb.settings.newResultType = 'NumberResult';
			}else{
				fb.settings.newResultType = 'DateResult';
			}
			$('textarea',$formulaValue).val(value);
			fb.target._updateSettings(fb.item);
		}
		var $allFieldsButtons = $('<div style="height:120px;overflow-y:scroll;"></div>');
		$('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
			var $ctrlHolder = $(this);
			var rel = $ctrlHolder.attr('rel');
			var type = $ctrlHolder.find("input[id$='fields[" + rel + "].type']").val();
			//if(type == 'SingleLineNumber' || type == 'SingleLineDate' || (type == 'FormulaField' && rel != fb.item.attr('rel'))){
			if(type == 'SingleLineNumber' || type == 'SingleLineDate'){
				var $settings = $ctrlHolder.find("input[id$='fields[" + rel + "].settings']");
				var $fieldName = $ctrlHolder.find("input[id$='fields[" + rel + "].name']").val();
                // settings is JavaScript encoded when return from server-side
				var settings = $.parseJSON(unescape($settings.val()))
				var $button = $('<input type="button" id="button'+$fieldName+'" style="width:100px;overflow:hidden;" itsType="'+type+'">')
								.val(settings[$('#language').val()].label)
								.attr({title:settings[$('#language').val()].label})
								.click(function(){
									addToValue($fieldName);
								});
				$allFieldsButtons.append($button);
			}
		});
		if($allFieldsButtons.children().size() == 0){
			$allFieldsButtons.append($('<input type="button" value="No Fields" title="No Fields of type Number or Date" style="width:105px;">'));
		}
		var $formulaCalculator = $('<div><table class="formulaCalculator"><tr><td>1</td><td>2</td><td>3</td><td style="white-space:nowrap;"><-</td><td rowspan="5" class="formulaTDForFields" id="formulaTDForFields"></td></tr>\
										<tr><td>4</td><td>5</td><td>6</td><td>+</td></tr>\
										<tr><td>7</td><td>8</td><td>9</td><td>-</td></tr>\
										<tr><td>(</td><td>0</td><td>)</td><td>*</td></tr>\
										<tr><td class="blankTD"></td><td class="blankTD"> </td><td class="blankTD"> </td><td>/</td></tr>\
									</table></div>\
								');
		$('td[class=""]',$formulaCalculator).each(function(){
			var $thisTD = $(this);
			$thisTD.css('cursor','pointer');
			$thisTD.click(function(){
				addToValue($(this).html());
			});
		});
		$('#formulaTDForFields',$formulaCalculator).append($allFieldsButtons);
		
		setValue();
		var $value = fb.target._fieldset({ text: 'Formula Value' })
		 .append(fb.target._oneColumn($formulaValue))
		 .append(fb.target._oneColumn($formulaCalculator));
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbFormulaField._getFieldSettingsLanguageSection executed.');
//        return [fb.target._oneColumn($label),$choicePanel, $fontPanel];
        return [fb.target._oneColumn($label),fb.target._oneColumn($description),fb.target._oneColumn($value), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbFormulaField._getFieldSettingsGeneralSection executing...');
        var $isEditable = $('<div><input type="checkbox" id="field.isEditable" />&nbsp;Allow User Edit</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        		.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append($isEditable)
        		.append($hideFromUser);
		$('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
		$('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        $('input', $isEditable).attr('checked', fb.settings.isEditable)
    	.change(function(event){
    		if ($(this).attr('checked')) {
                fb.settings.isEditable = true;
            } else {
                fb.settings.isEditable = false;
            }
            fb.target._updateSettings(fb.item);
    	});
        $('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
		.change(function(event){
			if ($(this).attr('checked')) {
	            fb.settings.hideFromUser = true;
	            fb.item.css('backgroundColor', '#CFCFCF');
	            fb.settings.styles.label.backgroundColor = 'CFCFCF';
	            fb.target._updateSettings(fb.item);
	            if(ctrlHClicked==1)
	            	ctrlHClicked=2;
	        } else {
	            fb.settings.hideFromUser = false;
	            fb.item.css('backgroundColor', '');
	            fb.settings.styles.label.backgroundColor = '';
	            if(ctrlHClicked==1)
	            	ctrlHClicked=2;
	        }
			if(ctrlHClicked==2){
				fb.item.trigger('click');
				ctrlHClicked = "";
			}
	        fb.target._updateSettings(fb.item);
		});
		ctrlHClicked=3;
		$('input', $hideFromUser).trigger('change');
		ctrlHClicked=1;
        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbFormulaField._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbFormulaField.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbFormulaField.languageChange executed.');
    }
});

$.widget('fb.fbFormulaField', FbFormulaField);

var fileUploadid = "fileUpload";
var FbFileUpload = $.extend({}, $.fb.fbWidget.prototype, {
        options: { // default options. values are stored in widget's prototype
                  name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/fileUpload.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;File Upload</span>',
                belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
                _type: 'FileUpload',
                _html: '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label> </div>\
						<div class="fullLengthField"><input type="file" name="myfileupload" disabled="disabled" class="textInput">\
		                <p class="formHint"></p></div></div>',
                _counterField: 'field1',
                _languages: [ 'en', 'zh_CN' ],
                settings: {
                        en: {
                                label: 'File Upload',
                                value: '',
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
                        zh_CN: {
                                label: '',
								value: '',
                                description: '',
                                classes: [],
                                styles: {
                                        fontFamily: 'default', // form builder default
                                        fontSize: 'default',
                                        fontStyles: [1, 0, 0] // bold, italic, underline                                        
                                }                               
                        },
						_persistable: true,
                        required: false,
                        hideFromUser: false,
                        multipleUpload: true,
                        maxSize: 2,
                        unit: 'MB',
                        restriction: 'no',
                        styles: {
                                color: 'default',
                                backgroundColor: 'default',
								label: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
								value: {
                                  color : 'default',
                                  backgroundColor : 'default'
                                },
                                description: {
                                        color : '777777',
                                        backgroundColor : 'default'
								}
                        }
                }
        },

    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
    	
    	var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        (index1==0)?(index1 = index):index1;
    	
                var $jqueryObject = $(fb.target.options._html);
                fb.target._log('fbFileUpload._getWidget executing...');
				$('label span', $jqueryObject).text(fb.settings.label);
//                if (fb._settings.required) {
//                        $('label em', $jqueryObject).text('*'); 
//                }
				$('input[type=file]',$($jqueryObject)).each(function() {
					$(this).attr("id","fileUpload_"+index1);
				});

				$('.formHint', $jqueryObject).text(fb.settings.description);
                fb.target._log('fbFileUpload._getWidget executed.');
                return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
    	var $jqueryObjects = [];
		fb.target._log('fbFileUpload._getFieldSettingsLanguageSection executing...');

		var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
				 .append('<textarea id="field.label" />');
		$('textarea', $label).val(fb.settings.label)
		  .keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('label span').text(value);
				  fb.settings.label = value;
				  fb.target._updateSettings(fb.item);
				  fb.target._updateName(fb.item, value);
			 });

//		var $choice = $(str)
//		var $choicePanel = fb.target._fieldset({ text: 'Choices'})
//                .append(fb.target._oneColumn($choice));
			
		//---------------------------------------------------------------------
		var $description = fb.target._label({ label: 'Description', name: 'field.description' })
		 .append('<textarea id="field.description"></textarea>');
		 $('textarea', $description).val(fb.settings.description)
				.keyup(function(event) {
				  var value = $(this).val();
				  fb.item.find('.formHint').text(value);
				  fb.settings.description = value;
				  fb.target._updateSettings(fb.item);
		});             
						
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbFileUpload._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbFileUpload._getFieldSettingsGeneralSection executing...');
        var $multipleUpload = $('<div><input type="checkbox" id="field.multipleUpload" />&nbsp;Allow Multiple Uploads</div>');
        var $maxSize = $('<div>Max Size Per File&nbsp;<input type="text" id="field.maxSize" style="width:70px;"/><select id="field.unit"><option value="KB">KB</option><option value="MB">MB</option></select></div>');
        $('input', $multipleUpload).attr('checked', fb.settings.multipleUpload)
    	.change(function(event){
    		if ($(this).attr('checked')) {
                fb.settings.multipleUpload = true;
            } else {
                fb.settings.multipleUpload = false;
            }
            fb.target._updateSettings(fb.item);
    	});
        $('input', $maxSize).val(fb.settings.maxSize);
    	$('input', $maxSize).change(function(event){
    		var $value = isNaN($(this).val())?fb.settings.maxSize:Number($(this).val())
    		if($value != 0){
    			fb.settings.maxSize = $value;
    		}else{
    			fb.settings.maxSize = 2;
    		}
    		$(this).val( fb.settings.maxSize );
            fb.target._updateSettings(fb.item);
    	});
        $('select', $maxSize).val(fb.settings.unit);
    	$('select', $maxSize).change(function(event){
    		fb.settings.unit = $(this).val();
            fb.target._updateSettings(fb.item);
    	});
        var $valuePanel = fb.target._fieldset({ text: 'Uploads'})
                //.append($multipleUpload)
                .append($maxSize);

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbFileUpload._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
        //return [$colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbFileUpload.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbFileUpload.languageChange executed.');
    }
});

$.widget('fb.fbFileUpload', FbFileUpload);

var DatePicker = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/date_input.png" style="position:absolute;left:-7px;z-index:2;width:20px" >&nbsp;Date Input</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'DatePicker',
        _html : '<div><label><span></span></label>\
	        <input id="datepicker" type="text"></div>\
               ',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Date',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {

        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');

        var uuid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
        $("#datepicker").attr("id", uuid)
        console.log(uuid)
        $(function() {
            $("#" + uuid).datepicker();
        });
        uuid = "";
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbDatePicker._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._log('Update setting of '+fb.item+' with value '+value);
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
				<option value="no">Any character</option> \
				<option value="alphanumeric">Alphanumeric only</option> \
				<option value="letterswithbasicpunc">Letters or punctuation only</option> \
				<option value="lettersonly">Letters only</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
                .append(fb.target._twoColumns($required, $restriction))
        		.append($hideFromUser);
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
		    if ($(this).attr('checked')) {
		    	$('input', $hideFromUser).attr('disabled','disabled');
				fb.item.find('em').text(' *');
		        fb.settings.required = true;
		    } else {
		    	$('input', $hideFromUser).removeAttr('disabled');
		        fb.item.find('em').text('');
		        fb.settings.required = false;
		    }
		    fb.target._updateSettings(fb.item);
		});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        $("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
        $('select', $restriction).change(function(event) {
            fb.settings.restriction = $(this).val();
            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
            fb.target._updateSettings(fb.item);
        });

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbDatePicker', DatePicker);
var dropDownIndex = 0;

var FbDropDown = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/dropdown.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Select</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'dropdown',
        _html : '<div><div Class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label> </div>\
            	<div class="fullLengthField" ><select>\
            <option id="oFirstChoice" >First Choice</option>  \
            <option id="oSecondChoice" >Second Choice</option> \
            <option id="oThirdChoice" >Third Choice</option>  \
          </select> \
          <p class="formHint"></p></div></div>',
        _counterField: 'label',
	_choices:['First Choice','Second Choice','Third Choice'],
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Dropdown Text',
                value: ["First Choice","Second Choice","Third Choice"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: ["First Choice","Second Choice","Third Choice"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            restriction: 'no',
            fieldSize:'mClass',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        dropDown = "dropdown1";

        var counter = FbWidget._getCounter(this);
        var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        dropDownIndex = (index1==0)?(index1 = index):index1;
        index = index1 - 1;
        var $jqueryObject = $(fb.target.options._html);
		$('select', $jqueryObject).attr("id","dropdown"+dropDownIndex);
        fb.target._log('fbDropDown._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbDropDown._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbdropdown._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
		var $curdd = $('div > select',$(fb.item))
		var $curddid = $curdd.attr('id');
		fb.target._log('selected dropdown::'+$curddid);
		
		var $description = fb.target._label({ label: 'Description', name: 'field.description' })
        .append('<textarea id="field.description"></textarea>');
		$('textarea', $description).val(fb.settings.description)
		        .keyup(function(event) {
		    var value = $(this).val();
		    fb.item.find('.formHint').text(value);
		    fb.settings.description = value;
		    fb.target._updateSettings(fb.item);
		});

		
        var v1 = "";
		var i=0;
		$("#"+$curddid+" option").each(function() {
			  $(this).attr('id',$curddid+"_"+i);
			  $(this).val(fb.settings.value[i])
			  if(i==0){
				  v1=v1 + '<div id="ws'+$(this).attr('id')+'"> <input type="Text" style="width:180px" value="" id="Cho'+$(this).attr('id')+'" ><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr('id')+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a></div>';
			  }else{
				  v1=v1 + '<div id="ws'+$(this).attr('id')+'"> <input type="Text" style="width:180px" value="" id="Cho'+$(this).attr('id')+'" ><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr('id')+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem'+$(this).attr('id')+'"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>';  
			  }
			  i=i+1;
	    });
		v1 = '<div>'+v1+'</div>';
		var $choice = $(v1);
		
		var counter = 1;
		var inputEach = function() {
			$(this).val(fb.settings.value[counter-1])
			.keyup(function(event) {
			  var cid = ($(this).attr("id")).replace("Cho","");
			  $("#"+cid).val($(this).val());
			  $("#"+cid).text($(this).val());
			  var value = new Array();
			  //fb.item.find('.textInput').val(value);
			  var count=0;
			  $("#"+$curddid+" option").each(function() {
					value[count] = jQuery.trim($(this).val());
					count++;
			  });
			  fb.settings.value = value;
			  fb.target._updateSettings(fb.item);
			});
			counter = counter + 1;
		}
		$('input[type=text]',$choice).each(inputEach);
		var choiceRemove = function(){
			var confirmation = confirm("Are you sure?")
			if(confirmation){
				var value=new Array();
			    var curid = $(this).attr('id').replace('ChoRem','');
			    var wsDiv = $('#ws' + curid);
			    	wsDiv.remove();
			    $('#'+curid).remove();
			    var count = 0;
				$('#'+$curddid+' option').each(function() {
			        value[count] = jQuery.trim($(this).val());
			        count++;
			    });
			    fb.settings.value = value;
			    fb.target._updateSettings(fb.item);
			}
			return false;
		}
		$('a[class="RemoveChoice"]',$choice).click(choiceRemove);
		var choiceAdd = function(){
			var value=new Array();
			var uniqid = new Date().getTime();
		    var curid = $(this).attr('id').replace('ChoAdd','');
		    $('#ws' + curid).after('<div id="ws' + uniqid + '"><input type="Text" style="width:180px" value="Enter your Choice" id="Cho' + uniqid + '"><a href="#" class="AddChoice" id="ChoAdd'+uniqid+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem' + uniqid + '"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>');
		    $('#'+curid).after('<option id="' + uniqid + '">Enter your Choice</option> ');
		    var wsDiv = $('#ws' + uniqid)
		    $('a[class="RemoveChoice"]',wsDiv).click(choiceRemove);
		    $('a[class="AddChoice"]',wsDiv).click(choiceAdd);
		    $('#Cho'+uniqid).keyup(function(event) {
				  var cid = ($(this).attr("id")).replace("Cho","");
				  $("#"+cid).val($(this).val());
				  $("#"+cid).text($(this).val());
				  var value = new Array();
				  //fb.item.find('.textInput').val(value);
				  var count=0;
				  $("#"+$curddid+" option").each(function() {
						value[count] = jQuery.trim($(this).val());
						count++;
				  });
				  fb.settings.value = value;
				  fb.target._updateSettings(fb.item);
				});
		    var count = 0;
			$('#'+$curddid+' option').each(function() {
		        value[count] = jQuery.trim($(this).val());
		        count++;
		    });
		    fb.settings.value = value;
		    fb.target._updateSettings(fb.item);
		    return false;
		}
		$('a[class="AddChoice"]',$choice).click(choiceAdd);
        
        var $choicePanel = fb.target._fieldset({ text: 'Choices'})
                .append(fb.target._oneColumn($choice));

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbDropDown._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label), fb.target._oneColumn($description), $choicePanel,$fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbDropDown._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        //End ---->>> Master form field adding
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        .append(fb.target._twoColumns($required, $hideFromUser))
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('select').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");


        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
		    if ($(this).attr('checked')) {
		    	$('input', $hideFromUser).attr('disabled','disabled');
				fb.item.find('em').text(' *');
		        fb.settings.required = true;
		    } else {
		    	$('input', $hideFromUser).removeAttr('disabled');
		        fb.item.find('em').text('');
		        fb.settings.required = false;
		    }
		    fb.target._updateSettings(fb.item);
		});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbDropDown._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbdropdown', FbDropDown);

var radioId = "checkbox";
var fbGroupButton = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/radio.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Radio Button</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'GroupButton',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label><p style="line-height:5px;">&nbsp;</p></div> \
        		<div class="fullLengthField"> <div id="FirstChoicerd"><input type="radio" value="First Radio" name="myGroup" id="FirstChoicert"><span id="FirstChoicesr" style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">First Radio </span><br/></div> \
				<div id="SecondChoicerd"><input type="radio" value="Second Radio" name="myGroup" id="SecondChoicert"><span id="SecondChoicesr" style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">Second Radio</span><br/></div>\
				<div id="ThirdChoicerd"><input type="radio" value="Third Radio" name="myGroup" id="ThirdChoicert"><span id="ThirdChoicesr" style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">Third Radio </span><br/></div>\
        	    <div style="display:none;"><input type="text" name="mycheckbox" placeholder="Other" style="width:auto;"/></div> \
        		</div><p class="formHint" style="color: #777777; background-color: #transparent;"></p></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Radio Button',
                value: ["First Radio","Second Radio","Third Radio"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: ["First Radio","Second Radio","Third Radio"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            fieldLayout:'oneCol',
            otherOption:false,
            restriction: 'no',
            _randomize:false,
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {

        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
		var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        var radID = (index1==0)?(index1 = index):index1;
        index = index1 - 1;
        
        var $jqueryObject = $(fb.target.options._html);
		$('#GrpBtnPanel', $jqueryObject).attr("id","GrpBtnPanel"+radID);
		var counter = 1;
		$('input',$jqueryObject).each(function() {
			$(this).attr("id","radio" + radID + "_" + counter);
			$(this).val(fb.settings.value[counter-1]);
			counter =  counter+1;
		});
        fb.target._log('fbGroupButton._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        
        //$('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbGroupButton._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbGroupButton._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
                .append('<textarea id="field.label" />');
        $('textarea', $label).val(fb.item.find('label span').text())
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
		
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
        .append('<textarea id="field.description"></textarea>');
		$('textarea', $description).val(fb.settings.description)
		        .keyup(function(event) {
		    var value = $(this).val();
		    fb.item.find('.formHint').text(value);
		    fb.settings.description = value;
		    fb.target._updateSettings(fb.item);
		});

		//--------------------------------------------------
		var counter = 1;
		var str = "<div >";
		$('input[type=radio]',fb.item).each(function() {
			$(this).val(fb.settings.value[counter-1])
			if(counter==1){
				str = str + '<div id="ws' + $(this).attr("id") + '"><input style="width:180px" type="text" id="txt'+$(this).attr("id")+'" /><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a></div>';
			}else{
				str = str + '<div id="ws' + $(this).attr("id") + '"><input style="width:180px" type="text" id="txt'+$(this).attr("id")+'" /><a href="#" class="AddChoice" id="ChoAdd'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem'+$(this).attr("id")+'"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>';
			}
			counter = counter +1;
		});
		str = str +"</div>";
		var $choice = $(str)
		var $choicePanel = fb.target._fieldset({ text: 'Choices'})
                .append(fb.target._oneColumn($choice));
		var counter = 1;
		$('input[type=text]',$choice).each(function() {
			$(this).val(fb.settings.value[counter-1])
			.keyup(function(event) {
			  var cid = ($(this).attr("id")).replace("txt","");
			  $("#"+cid).val($(this).val());
			  $('span',$("#"+cid).parent()).text($(this).val())
			  var value = new Array();
			  //fb.item.find('.textInput').val(value);
			  var count=0;
			  $('input[type=radio]',fb.item).each(function() {
					value[count] = jQuery.trim($(this).val());
					count++;
			  });
			  fb.settings.value = value;
			  fb.target._updateSettings(fb.item);
			});			
			counter = counter + 1;
		});
		
		var choiceRemove = function(){
			var confirmation = confirm("Are you sure?")
			if(confirmation){
				var value=new Array();
			    var curid = $(this).attr('id').replace('ChoRem','');
			    var wsDiv = $('#ws' + curid);
			    	wsDiv.remove();
			    $('#'+curid).parent().remove();
			    var count = 0;
			    $('input[type=radio]',fb.item).each(function() {
					value[count] = jQuery.trim($(this).val());
					count++;
			    });
			    fb.settings.value = value;
			    fb.target._updateSettings(fb.item);
			}
			return false;
		}
		$('a[class="RemoveChoice"]',$choice).click(choiceRemove);
		var choiceAdd = function(){
			var value=new Array();
			var uniqid = new Date().getTime();
		    var curid = $(this).attr('id').replace('ChoAdd','');
		    $('#ws' + curid).after('<div id="ws' + uniqid + '"><input type="Text" style="width:180px" value="New radio" id="txt' + uniqid + '"><a href="#" class="AddChoice" id="ChoAdd'+uniqid+'"><img src="'+imageSourceDir+'/images/add.png" style="width:20px;"></a><a href="#" class="RemoveChoice" id="ChoRem' + uniqid + '"><img src="'+imageSourceDir+'/images/images.png" style="width:20px;"></a></div>');
		    $('#'+curid).parent().after('<div><input type="radio" name="myradio" value="New radio" id="'+uniqid+'" ><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">New radio</span><br></div>');
		    var wsDiv = $('#ws' + uniqid)
		    $('a[class="RemoveChoice"]',wsDiv).click(choiceRemove);
		    $('a[class="AddChoice"]',wsDiv).click(choiceAdd);
		    $('#txt'+uniqid).keyup(function(event) {
				  var cid = ($(this).attr("id")).replace("txt","");
				  $("#"+cid).val($(this).val());
				  $('span',$("#"+cid).parent()).text($(this).val());
				  var value = new Array();
				  //fb.item.find('.textInput').val(value);
				  var count=0;
				  $('input[type=radio]',fb.item).each(function() {
					  value[count] = jQuery.trim($(this).val());
					  count++;
				  });
				  fb.settings.value = value;
				  fb.target._updateSettings(fb.item);
				});
		    var count = 0;
		    $('input[type=radio]',fb.item).each(function() {
				value[count] = jQuery.trim($(this).val());
				count++;
			});
			fb.settings.value = value;
		    fb.target._updateSettings(fb.item);
		    return false;
		}
		$('a[class="AddChoice"]',$choice).click(choiceAdd);
		//--------------------------------------------------

        
        var $choicePanel = fb.target._fieldset({ text: 'Choices'})
                .append(fb.target._oneColumn($choice));

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),fb.target._oneColumn($description),$choicePanel, $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        var $fieldLayoutLabel = $('<div>Field Layout</div>');
        var $fieldLayoutDD = $('<div><select id="field.layout" style="width: 99%"> \
				<option value="oneCol">One Column</option> \
				<option value="twoCol">Two Column</option> \
				<option value="threeCol">Three Column</option> \
        		<option value="sbs">Side by Side</option> \
			</select></div>');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $otherOption = $('<div><input type="checkbox" id="field.showOther" />&nbsp;Show Other Option</div>');
         var $randomize=$('<div><input type="checkbox" id="field.randomize" />&nbsp;Randomize</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        		.append(fb.target._twoColumns($fieldLayoutLabel, $fieldLayoutDD))
                .append(fb.target._twoColumns($required, $hideFromUser))
                .append(fb.target._twoColumns($randomize, $otherOption))
        		//.append($hideFromUser);
        $('.col1', $valuePanel).css('width', '35%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        $('input', $randomize).attr('checked', fb.settings._randomize?fb.settings._randomize:false)
        .change(function(event) {
            if ($(this).attr('checked')) {
                 fb.settings._randomize = true;
            } else {
            	fb.settings._randomize= false;
            }
            fb.target._updateSettings(fb.item);
        });
        $('input', $randomize).trigger('change');
        $('input', $otherOption).attr('checked', fb.settings.otherOption?fb.settings.otherOption:false)
        .change(function(event) {
            if ($(this).attr('checked')) {
            	 var inputPar = fb.item.find('input[type=text]').parent()
            	 inputPar.show()
                 fb.settings.otherOption = true;
            } else {
            	var inputPar = fb.item.find('input[type=text]').parent()
           	 	inputPar.hide()
                fb.settings.otherOption = false;
            }
            fb.target._updateSettings(fb.item);
        });
        $('input', $otherOption).trigger('change');
        
        if(!fb.settings.fieldLayout){
        	fb.settings.fieldLayout = "oneCol"
        }
        $('select', $fieldLayoutDD).val(fb.settings.fieldLayout).change(function(event) {
            fb.settings.fieldLayout = $(this).val();
            var inputPar = fb.item.find('input[type="radio"]').parent().parent();
            if($(inputPar).hasClass("oneCol")){
            	$(inputPar).removeClass("oneCol")
            }else if(inputPar.hasClass("twoCol")){
            	$(inputPar).removeClass("twoCol")
            }else if(inputPar.hasClass("threeCol")){
            	$(inputPar).removeClass("threeCol")
            }else if(inputPar.hasClass("sbs")){
            	$(inputPar).removeClass("sbs")
            }
            $(inputPar).addClass(fb.settings.fieldLayout)
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
		    if ($(this).attr('checked')) {
		    	$('input', $hideFromUser).attr('disabled','disabled');
				fb.item.find('em').text(' *');
		        fb.settings.required = true;
		    } else {
		    	$('input', $hideFromUser).removeAttr('disabled');
		        fb.item.find('em').text('');
		        fb.settings.required = false;
		    }
		    fb.target._updateSettings(fb.item);
		});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbGroupButton', fbGroupButton);

var LinkVideo = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/lightning_input.png" style="position:absolute;left:-7px;z-index:2;width:20px" >&nbsp;Embed HTML</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'LinkVideo',
        _html : '<div>\
	        		<div class="fullLengthLabel">\
	        			<label style="font-weight:bold;"><span></span></label>\
	        		</div>\
			        <div class="fullLengthField">\
	        			<div id="videoId" class="linkVideo"></div>\
	        		</div>\
        		</div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Embed HTML',
                height: 200,
                width: 200,
                value: '',
                description: '',
                embedHTML:'',
                urlOrEmbed:true,
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                height: 200,
                width: 200,
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            required: false,
            hideFromUser: false,
            _persistable: true,
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var uuid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1)
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);

        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = $('<div><input type="text" id="field.value" /></div>');
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            /*http://www.youtube.com/watch?v=-_GKRtWBceQ&feature=related*/
            if (value.indexOf("watch?v=") > 0) {
                value = value.replace("watch?v=", "v/")
            }
            
            $('.ctrlHolderSelected .linkVideo').html('<object width="'+fb.settings.width+'" height="'+fb.settings.height+'" ><param name="movie" value="" id="video" /><param name="wmode" value="transparent" /><embed src="'+value+'" type="application/x-shockwave-flash" width="200" height="200" id="vId"/></object>');

            //$('.ctrlHolderSelected embed').attr("src", value);
            fb.settings.value = value;

            fb.target._updateSettings(fb.item);
        });
        
        var $height = $('<div> \
                height : <input type="text">\
  					</div>');
        var $width = $('<div> \
                width : <input type="text">\
  					</div>');
        var $choicePanel = fb.target._fieldset({ text: 'Choices'})
        .append(fb.target._twoColumns($height, $width));
        
        var $urlRadio = $('<div><input type="radio" name="urlOrEmbedHtml" style="width:auto;margin:0 4px 4px 0"> Embed URL</div>');
        var $embedRadio = $('<div><input type="radio" name="urlOrEmbedHtml" style="width:auto;margin:0 4px 4px 0"> Embed HTML</div>');
        var $embedHTML = $('<div><textarea rows="12" /></div>');
        var $urlOrEmbedPanel = fb.target._fieldset({ text: 'Embed HTML'})
	        .append(fb.target._twoColumns($urlRadio, $embedRadio))
	        .append(fb.target._oneColumn($value))
	        .append(fb.target._oneColumn($embedHTML));
        
        $('textarea',$embedHTML).keyup(function(){
        	fb.settings.embedHTML = $(this).val();
        	$('.ctrlHolderSelected .linkVideo').html(fb.settings.embedHTML);
        	fb.target._updateSettings(fb.item);
        }).val(fb.settings.embedHTML);
        var checkURLorEmbed = function(){
        	if($('input',$urlRadio).attr('checked')){
        		$embedHTML.hide();
        		$('input',$value.show()).trigger('keyup');
        		$choicePanel.show();
        		fb.settings.urlOrEmbed = false;
        	}
        	if($('input',$embedRadio).attr('checked')){
        		$('textarea',$embedHTML.show()).trigger('keyup');
        		$value.hide();
        		$choicePanel.hide();
        		fb.settings.urlOrEmbed = true;
        	}
        	fb.target._updateSettings(fb.item);
        }
        $('input',$urlRadio).change(function(){
        	checkURLorEmbed();
        });
        $('input',$embedRadio).change(function(){
        	checkURLorEmbed();
        });
        
        //start--->>>remove video url code is here
        fb.settings.urlOrEmbed = true;
        fb.target._updateSettings(fb.item);
        $urlRadio.hide();
        $embedRadio.hide();
        $value.hide();
        //end--->>>remove video url code is here
        
        if(fb.settings.urlOrEmbed){
        	$('input',$embedRadio).attr('checked',true).trigger('change');
        }else{
        	$('input',$urlRadio).attr('checked',true).trigger('change');
        }

        $('input',$height).val(fb.settings.height)
        $('input',$height).keyup(function(event){
    		var heightValue = $(this).val();
    		if (heightValue != '') {
    			heightValue = heightValue.replace(/([^0-9]*)/g, "")
    		}
    		if(fb.settings.urlOrEmbed){
    			//$('.ctrlHolderSelected embed').height(heightValue*1);
    		}else{
    			$('.ctrlHolderSelected embed').height(heightValue*1);
    		}
    		$(this).val(heightValue);
    		fb.settings.height = heightValue;
    		fb.target._updateSettings(fb.item);
    	}).blur(function(event){
    		$(this).trigger('keyup');
    	});
        
        $('input',$width).val(fb.settings.width)
        $('input',$width).keyup(function(event){
    		var widthValue = $(this).val();
    		if(fb.settings.urlOrEmbed){
    			//$('.ctrlHolderSelected embed').height(heightValue*1);
    		}else{
    			$('.ctrlHolderSelected embed').height(heightValue*1);
	    		if (widthValue != '') {
	    			widthValue = widthValue.replace(/([^0-9]*)/g, "")
	    			var pWidthValue = $('.ctrlHolderSelected embed').parent().parent().parent().innerWidth();
	    			widthValue = (widthValue > pWidthValue)?(pWidthValue-16):widthValue;
	    		}else{
	    			$('.ctrlHolderSelected embed').width("");
	    			$('.ctrlHolderSelected embed').removeAttr("width");
	    			$('.ctrlHolderSelected embed').height("");
	    			$('.ctrlHolderSelected embed').removeAttr("height");
	    			$('.ctrlHolderSelected embed').hide();
	    			var pWidthValue = $('.ctrlHolderSelected embed').parent().parent().parent().innerWidth();
	    			var cWidthValue = $('.ctrlHolderSelected embed').innerWidth();
	    			widthValue = (cWidthValue>pWidthValue?pWidthValue:cWidthValue);
	    			$('.ctrlHolderSelected embed').show();
	    		}
	    		$('.ctrlHolderSelected embed').width(widthValue*1);
    		}
    		$(this).val(widthValue);
    		fb.settings.width = widthValue;
    		fb.target._updateSettings(fb.item);
    	}).blur(function(event){
    		$(this).trigger('keyup');
    	});
        
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),$urlOrEmbedPanel,$choicePanel,$fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
      fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#')+ value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
//        return [$valuePanel, $colorPanel];
        return [$colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineText.languageChange executed.');
    }
});

$.widget('fb.fbLinkVideo', LinkVideo);

var FbMultiLineText = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/textarea.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Multi Line Text</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'MultiLineText',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div> \
        	<div class="fullLengthField"> <textarea class="textInput" /> \
	        <p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Multi Line Text',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            minRange:'0',
            maxRange : '',
            restriction: 'no',
            fieldSize:'2',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbMultiLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbMultiLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbMultiLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbMultiLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbMultiLineText._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        //End ---->>> Master form field adding
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
				<option value="no">Any character</option> \
				<option value="alphanumeric">Alphanumeric only</option> \
				<option value="letterswithbasicpunc">Letters or punctuation only</option> \
				<option value="lettersonly">Letters only</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        
        var $fieldRangeLabel = $('<div>Range in characters</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="7"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="7"/></div>')
        
        var $textAreaLabel = $('<div>Field Size</div>');
        var $textAreaSize = $('<div><select id="field.fieldSize"  style="width: 99%"> \
				<option value="1">Small</option> \
				<option value="2">Medium</option> \
				<option value="4">Large</option> \
			</select></div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        	.append(fb.target._twoColumns($fieldRangeMin,$fieldMin))
        	.append(fb.target._twoColumns($fieldRangeMax,$fieldMax))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($textAreaLabel, $textAreaSize))
        		.append(fb.target._twoColumns($required, $restriction))
        		.append($hideFromUser)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating

        
        $('select',$textAreaSize).change(function(){
        	var valueSize=$(this).val();
        	fb.item.find('textArea').height(valueSize*1*65+"px");
        	fb.settings.fieldSize = valueSize;
        	fb.target._updateSettings(fb.item);
        })
        .val(fb.settings.fieldSize);
        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        $("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
        $('select', $restriction).change(function(event) {
            fb.settings.restriction = $(this).val();
            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
            fb.target._updateSettings(fb.item);
        });
        if(!fb.settings.minRange){
        	fb.settings.minRange = 0
        }
        $("input[id$='field.minRange']", $fieldMin).val(fb.settings.minRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue=2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.minRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        
        if(!fb.settings.maxRange){
        	fb.settings.maxRange = 0
        }
        $("input[id$='field.maxRange']", $fieldMax).val(fb.settings.maxRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue=2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.maxRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbMultiLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbMultiLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbMultiLineText.languageChange executed.');
    }
});

$.widget('fb.fbMultiLineText', FbMultiLineText);

// extends/inherits from superclass: FbWidget
var FbPlainText = $.extend({}, $.fb.fbWidget.prototype, {
    options : { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/label.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Rich Text</span>',
        belongsTo : $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type : 'PlainText',
        _html : '<div class="PlainText"></div><span style="display:none;"/> ',
        _counterField : 'text',
        _languages : [ 'en', 'zh_CN' ],
        settings : {
            en : {
                text : 'Rich Text',
                classes : [ 'leftAlign', 'middleAlign' ],
                styles: {
                    fontFamily: '', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                text : '?????',
                classes : [ 'rightAlign', 'middleAlign' ],
                styles: {
                    fontFamily: '', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            styles : {
                color : '000000',
                backgroundColor : 'default'
            }
        }
    },
    _init : function() {
        $.fb.fbWidget.prototype._init.call(this);
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        fb.item.addClass(fb.settings.classes[1]); // vertical alignment
        return $(fb.target.options._html).text(fb.settings.text)
                .addClass(fb.settings.classes[0]);
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        var $text = fb.target._label({ label: 'Text', name: 'field.text',
            description: 'Text entered below will display in the form.' })
                .append('<textarea id="field.text" cols="44" rows="15"></textarea> <input style="width: 50px" class="button button-green" type="button" value="Save"/>');
        $("label",$text).css("padding-bottom","0");
        $('textarea', $text).val(fb.settings.text)
                .keyup(function(event) {
            var value = $(this).val();
            
            //Commenting this code so that scripts could be added to it.
            //value = removeScriptTag(value);
            fb.item.find('div.PlainText').html(value);
            fb.settings.text = value;
            fb.target._updateSettings(fb.item);
            return true
        });
        
        $('input', $text).click(function(event){
        	$('textarea[id$="field.text"]').trigger("keyup");
        });
        
        
       
        return [fb.target._oneColumn($text)];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
    	var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        var $colorPanel = fb.target._labelColorPanel(styles);

        $("input[id$='field.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            var h1Container = fb.item.find("h1")
            if(h1Container){
            	h1Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var h2Container = fb.item.find("h2")
            if(h2Container){
            	h2Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var h3Container = fb.item.find("h3")
            if(h3Container){
            	h3Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var h4Container = fb.item.find("h4")
            if(h4Container){
            	h4Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var h5Container = fb.item.find("h5")
            if(h5Container){
            	h5Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var h6Container = fb.item.find("h6")
            if(h6Container){
            	h6Container.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            var pContainer = fb.item.find("p")
            if(pContainer){
            	pContainer.css('color', (value=='transparent'?'':'#') + value + "!important");
            }
            styles.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

//        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
//            var value = $(this).data('colorPicker').color;
//            $textInput.css('color', (value=='transparent'?'':'#') + value);
//            styles.value.color = value;
//            fb.target._updateSettings(fb.item);
//        });
//
//        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
//            var value = $(this).data('colorPicker').color;
//            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
//            styles.value.backgroundColor = value;
//            fb.target._updateSettings(fb.item);
//        });
//
//        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
//            var value = $(this).data('colorPicker').color;
//            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
//            styles.description.color = value;
//            fb.target._updateSettings(fb.item);
//        });
//
//        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
//            var value = $(this).data('colorPicker').color;
//            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
//            styles.description.backgroundColor = value;
//            fb.target._updateSettings(fb.item);
//        });
        return [$colorPanel];
    },
    _languageChange : function(event, fb) {
        this._log('languageChange = ' + $.toJSON(fb.settings));
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        fb.item.find('.PlainText').text(fb.settings.text)
                .removeClass('leftAlign centerAlign rightAlign')
                .addClass(fb.settings.classes[0]);
        fb.item.css('fontWeight', styles.fontStyles[0] == 1 ? 'bold' : 'normal')
                .css('fontStyle', styles.fontStyles[1] == 1 ? 'italic' : 'normal')
                .css('textDecoration', styles.fontStyles[2] == 1 ? 'underline' : 'none')
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px')
                .removeClass('topAlign middleAlign bottomAlign')
                .addClass(fb.settings.classes[1]);
    }
});

$.widget('fb.fbPlainText', FbPlainText);

var hrefid = "link"

var FbPlainTextHref = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/hyperlink.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Hyperlink</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'PlainTextHref',
        _html : '<div><a href="#" id="link" onclick="return false;"> \
         <label style="font-weight:bold;"><span>Your HyperLink</span><p class="formHint"></p></label></a></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'link',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            restriction: 'no',
         
           
           styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
    	hrefid = "link"+fb.item.attr('rel')
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        $('a', $jqueryObject).attr('id',hrefid);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
    	fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'URL (No http://)', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            hrefid = 'link'+fb.item.attr('rel');
            
            $('#' + hrefid).attr("href", value)
			
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        }).blur(function(){
        	var value = $(this).val();
        	while(value.indexOf('http://')!=-1){
        		value = value.replace('http://','');
        	}
        	while(value.indexOf('https://')!=-1){
        		value = value.replace('https://','');
        	}
//        	while(value.indexOf('/')!=-1){
//        		value = value.replace('/','\\');
//        	}
        	hrefid = 'link'+fb.item.attr('rel');
        	
        	$('#' + hrefid).attr("href", value)
        	$(this).val(value);
            
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value).css('font-weight','normal');
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineText.languageChange executed.');
    }
});

$.widget('fb.fbPlainTextHref', FbPlainTextHref);

var FbScaleRating = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/star.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Scale Rating</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'ScaleRating',
        _html : '<div> \
					<div>\
						<div>\
							<div class="fullLengthLabel">\
								<label style="font-weight:bold;"> <span>Scale Rating 1</span><em></em> \
								</label>\
							</div>\
        					<div class="fullLengthField">\
					        	<table class="scaleRateTable" style="margin:0;">\
						        	<thead>\
						        		<tr>\
						        			<th style="text-align:center;padding:5px;width: 32%;">\
						        				Very Satisfied\
						        			</th>\
        									<th style="text-align:center;padding:5px;">\
						        				Satisfied\
						        			</th>\
											<th style="text-align:center;padding:5px;">\
        										Neutral\
						        			</th>\
        									<th style="text-align:center;padding:5px;">\
        										Dissatisfied\
						        			</th>\
						        		</tr>\
						        	</thead>\
						        	<tr>\
						        		<td style="text-align:center;font-weight:bold;">\
						        			<input type="radio" value="VerySatisfied" name="scaleRating"><br>1\
						        		</td>\
						        		<td style="text-align:center;font-weight:bold;">\
						        			<input type="radio" value="Satisfied" name="scaleRating"><br>2\
						        		</td>\
						        		<td style="text-align:center;font-weight:bold;">\
						        			<input type="radio" value="Neutral" name="scaleRating"><br>3\
						        		</td>\
						        		<td style="text-align:center;font-weight:bold;">\
						        			<input type="radio" value="NotSatisfied" name="scaleRating"><br>4\
						        		</td>\
						        	</tr>\
						        </table>\
        						<div class="slider" style="display:none;"></div>\
        					</div>\
						</div>\
					</div> \
        			<p class="formHint" style="color: #777777; background-color: #transparent; "></p>\
		      </div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Scale Rating',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            moodRate: false,
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {

        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbdropdown._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
        .append('<textarea id="field.description"></textarea>');
		$('textarea', $description).val(fb.settings.description)
		        .keyup(function(event) {
		    var value = $(this).val();
		    fb.item.find('.formHint').text(value);
		    fb.settings.description = value;
		    fb.target._updateSettings(fb.item);
		});

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),fb.target._oneColumn($description),$fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $moodRate = $('<div><label for="field.moodRate">Rating Type </label>&nbsp;<select id="field.moodRate"><option value="false">Scale Rating</option><option value="true">Mood Rating</option></select></div>');
        
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
                .append(fb.target._twoColumns($required, $hideFromUser))
                .append($moodRate)
        		//.append($hideFromUser);
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        
        var $slider = fb.item.find('.slider');
        if($slider.children().length == 0){
        	$slider.slider({
            	value:75, min: 25, max: 100,
            	slide: function( event, ui ) {
                    $('a',$slider).attr('style', 'background-image: url('+imageSourceDir+'/images/face-'+Math.round(ui.value/25).toFixed(0)+'.png) !important;');
            	}
            });
        }

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        $('select', $moodRate).val(""+fb.settings.moodRate)
    	.change(function(event){
    		if ($(this).val() == 'true') {
                fb.settings.moodRate = true;
                fb.item.find('.scaleRateTable').hide();
                fb.item.find('.slider').show();
            } else {
                fb.settings.moodRate = false;
                fb.item.find('.scaleRateTable').show();
                fb.item.find('.slider').hide();
            }
            fb.target._updateSettings(fb.item);
    	});

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbScaleRating', FbScaleRating);
var FbAddressField = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/address.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Address</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'AddressField',
        _html : '<div> \
        			<div class="fullLengthLabel">\
        				<label style="font-weight:bold;"> <span>Address Field 1</span><em></em></label>\
        			</div>\
					<div class="fullLengthField mClass">\
						<div class="addressControl">\
	    					<div class="addressLargeWrapper">\
	    						<input type="text" class="addressInput">\
	    						<label> Address Line 1</label>\
	    					</div>\
	    					<div class="addressLargeWrapper">\
	    						<input type="text" class="addressInput">\
	    						<label> Address Line 2</label>\
	    					</div>\
        					<div>\
		    					<div class="addressSmallWrapper addressLeft">\
		    						<input type="text" class="addressInput"><label>City</label >\
		    					</div>\
		    					<div class="addressSmallWrapper addressRight">\
		    						<input type="text" class="addressInput" ><label>State</label>\
		    					</div>\
        					</div>\
        					<div>\
		    					<div class="addressSmallWrapper addressLeft">\
        							<input type="text" class="addressInput" ><label>Zip/Postal code</label>\
		    					</div>\
		    					<div class="addressSmallWrapper addressRight">\
		    						<input type="text" class="addressInput" ><label>Country</label>\
		    					</div>\
        					</div>\
        				</div>\
        				<p style="color: #777777; background-color: #transparent;" class="formHint"></p>\
			        </div>\
		      </div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Address Field',
                value: '',
                description: '',
                rows:["Usability","Interface Look","Colors","Clarity"],
                columns:["Very Satisfied","Satisfied","Neutral","Dissatisfied"],
                switchRowCol:false,
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            mapMasterField: '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        var $fieldRangeLabel = $('<div>Range in characters</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="7"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="7"/></div>')
        //End ---->>> Master form field adding
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        $valuePanel.append(fb.target._twoColumns($required, $hideFromUser))
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineText.languageChange executed.');
    }
});

$.widget('fb.fbAddressField', FbAddressField);



var FbNameTypeField = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/address.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Name</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'NameTypeField',
        _html : '<div> \
        			<div class="fullLengthLabel">\
        				<label style="font-weight:bold;"> <span> 1</span><em></em></label>\
        			</div>\
					<div class="fullLengthField mClass">\
						<div class="nameFieldControl">\
		    					<div class="nameFieldSmallWrapper nameFieldPrefix hidethis">\
		    						<input type="text" class="nameFieldInputPrefix"><label>Prefix</label >\
		    					</div>\
		    					<div class="nameFieldSmallWrapper nameFieldName nameFieldFn">\
		    						<input type="text" class="nameFieldInput" ><label>First Name</label>\
		    					</div>\
		    					<div class="nameFieldSmallWrapper nameFieldName nameFieldMn hidethis">\
        							<input type="text" class="nameFieldInput" ><label>Middle Name</label>\
		    					</div>\
		    					<div class="nameFieldSmallWrapper nameFieldName nameFieldLn">\
		    						<input type="text" class="nameFieldInput" ><label>Last Name</label>\
		    					</div>\
							<p style="color: #777777; background-color: #transparent;" class="formHint"></p>\
        				</div>\
			        </div>\
		      </div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Name',
                value: '',
                description: '',
                rows:["Usability","Interface Look","Colors","Clarity"],
                columns:["Very Satisfied","Satisfied","Neutral","Dissatisfied"],
                switchRowCol:false,
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            showPrefix : false,
            showMiddleName :false,
            mapMasterField: '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        var $fieldRangeLabel = $('<div>Range in characters</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="7"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="7"/></div>')
        var $isPrefix = $('<div><input type="checkbox" id="field.isPrefix" />&nbsp;Prefix</div>');
        var $isMiddle = $('<div><input type="checkbox" id="field.isMiddle" />&nbsp;Middle Name</div>');
        //End ---->>> Master form field adding
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        $valuePanel.append(fb.target._twoColumns($isPrefix, $isMiddle))
        $valuePanel.append(fb.target._twoColumns($required, $hideFromUser))
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        var PrefixInput = fb.item.find(".nameFieldPrefix");
        $('input', $isPrefix).attr('checked', fb.settings.showPrefix)
        .change(function(event){
		    if ($(this).attr('checked')) {
		        fb.settings.showPrefix = true;
		        $(PrefixInput).addClass("showthis")
		        $(PrefixInput).removeClass("hidethis")
		    } else {
		    	fb.settings.showPrefix = false;
	    	    $(PrefixInput).addClass("hidethis")
		        $(PrefixInput).removeClass("showthis")
		    }
		    fb.target._updateSettings(fb.item);
		});
        var middleNameInput = fb.item.find(".nameFieldMn");
        $('input', $isMiddle).attr('checked', fb.settings.showMiddleName)
        .change(function(event){
		    if ($(this).attr('checked')) {
		        fb.settings.showMiddleName = true;
		        $(middleNameInput).addClass("showthis")
		        $(middleNameInput).removeClass("hidethis")
		    } else {
		    	fb.settings.showMiddleName = false;
	    	    $(middleNameInput).addClass("hidethis")
		        $(middleNameInput).removeClass("showthis")
		    }
		    fb.target._updateSettings(fb.item);
		});
        
        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            if(value){
            	fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            }else{
            	fb.item.css('backgroundColor', '');
            }
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineText.languageChange executed.');
    }
});

$.widget('fb.fbNameTypeField', FbNameTypeField);



var FbLikert = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
          name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/application_view_detail.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Likert</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'Likert',
        _html : '<div> \
					<label style="font-weight:bold;"> <span>Likert 1</span><em></em> \
					</label>\
		        	<table class="lTable">\
        				<thead><tr><td>&nbsp;</td><td>Very Satisfied</td><td>Satisfied</td><td>Neutral</td><td>Dissatisfied</td></thead>\
        				<tbody>\
        					<tr><td>Usability</td>     <td><input name="likert[0]" type="radio"></td><td><input name="likert[0]" type="radio"></td><td><input name="likert[0]" type="radio"></td><td><input name="likert[0]" type="radio"></td></tr>\
							<tr><td>Interface Look</td><td><input name="likert[1]" type="radio"></td><td><input name="likert[1]" type="radio"></td><td><input name="likert[1]" type="radio"></td><td><input name="likert[1]" type="radio"></td></tr>\
							<tr><td>Colors</td>        <td><input name="likert[2]" type="radio"></td><td><input name="likert[2]" type="radio"></td><td><input name="likert[2]" type="radio"></td><td><input name="likert[2]" type="radio"></td></tr>\
							<tr><td>Clarity</td>       <td><input name="likert[3]" type="radio"></td><td><input name="likert[3]" type="radio"></td><td><input name="likert[3]" type="radio"></td><td><input name="likert[3]" type="radio"></td></tr>\
        				</tbody>\
			        </table>\
        			<p style="color: #777777; background-color: #transparent;" class="formHint"></p>\
		      </div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Likert',
                value: '',
                description: '',
                rows:["Usability","Interface Look","Colors","Clarity"],
                columns:["Very Satisfied","Satisfied","Neutral","Dissatisfied"],
                switchRowCol:false,
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            moodRate: false,
            restriction: 'no',
            _randomize:false,
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {

        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbdropdown._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
        .append('<textarea id="field.description"></textarea>');
		$('textarea', $description).val(fb.settings.description)
		        .keyup(function(event) {
		    var value = $(this).val();
		    fb.item.find('.formHint').text(value);
		    fb.settings.description = value;
		    fb.target._updateSettings(fb.item);
		});
		var $switchRowCol = fb.target._twoColumns($('<div><input type="checkbox" id="field.switchRowCol"/></div>'),$('<div>One Click Per Column</div>'));
        $('.col1', $switchRowCol).css('width', '10%').removeClass('labelOnTop').css('padding','5px 0');
        $('.col2', $switchRowCol).css('marginLeft', '10%').removeClass('labelOnTop').css('padding','8px 0');
        $('input', $switchRowCol).attr('checked',fb.settings.switchRowCol?true:false)
		        .change(function(event) {
		    fb.settings.switchRowCol = this.checked;
		    fb.target._updateSettings(fb.item);
		    repopulateTable();
		    changeLabels();
		});
        //Start---Enter the rows and columns in the table>>>
        var repopulateTable = function(){
        	var switchRowCol = fb.settings.switchRowCol?true:false;
        	var lTable = fb.item.find('table.lTable');
        	var tBody = $('tbody',lTable).remove();
        	var tHeadTR = $('thead',lTable).remove();
        	var tHead = $('<thead><tr></tr></thead>');
        	tBody = $('<tbody></tbody>');
        	lTable.append(tHead);
        	lTable.append(tBody);
        	tHeadTR = $('tr',tHead);
        	tHeadTR.append($('<td></td>'));
        	var tBodyRowElements = [];
        	var columns = fb.settings.columns;
        	var rows = fb.settings.rows;
        	var columnsToPrint = switchRowCol?rows:columns;
        	var rowsToPrint = switchRowCol?columns:rows;
        	if(columnsToPrint.length>0){
        		for(var i=0;i<columnsToPrint.length;i++){
        			tHeadTR.append($('<td></td>').text(columnsToPrint[i]));
        		}
        	}else{
        		tHeadTR.append($('<td></td>'));
        	}
        	if(rowsToPrint.length>0){
        		for(var i=0;i<rowsToPrint.length;i++){
        			var trEle = $('<tr></tr>').append($('<td></td>').text(rowsToPrint[i]));
        			tBody.append(trEle);
        			if(columnsToPrint.length>0){
        				for(var j=0;j<columnsToPrint.length;j++){
        					trEle.append($('<td><input name="likert['+(switchRowCol?j:i)+']" type="radio"></td>'));
        				}
        			}else{
        				trEle.append($('<td><input name="likert['+(switchRowCol?j:i)+']" type="radio"></td>'));
        			}
        		}
        	}else{
        		tBody.append($('<tr><td></td><td><input type="radio"></td></tr>'));
        	}
        }
        var $columns = fb.target._fieldset({ text: 'Columns','class':"innerFieldset"})
        .append('<textarea id="field.columns" rows="5" style="width:93%;height:auto;"></textarea>');
        
        var $rows = fb.target._fieldset({ text: 'Rows','class':"innerFieldset"})
        .append('<textarea id="field.rows" rows="5" style="width:93%;height:auto;"></textarea>');
        $('textarea',$columns).val(fb.settings.columns.join("\n"));
        $('textarea',$rows).val(fb.settings.rows.join("\n"));
        $('textarea',$columns).keyup(function(){
        	var columnsText = $('textarea',$columns).val().split("\n");
        	fb.settings.columns = columnsText;
        	repopulateTable();
        	fb.target._updateSettings(fb.item);
        });
        $('textarea',$rows).keyup(function(){
        	var rowsText = $('textarea',$rows).val().split("\n");
        	fb.settings.rows = rowsText;
        	repopulateTable();
        	fb.target._updateSettings(fb.item);
        });
        var changeLabels = function(){
        	var switchRowCol = fb.settings.switchRowCol?true:false;
        	$('legend',$columns).text(switchRowCol?'Rows':'Columns');
        	$('legend',$rows).text(switchRowCol?'Columns':'Rows');
        }
        changeLabels();
        //End-----Enter the rows and columns in the table>>>
        
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),fb.target._oneColumn($description),$switchRowCol,$columns,$rows,$fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $randomize = $('<div><input type="checkbox" id="field.randomize" />&nbsp;Randomize</div>');
        
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
                .append(fb.target._twoColumns($required, $hideFromUser))
        		.append($randomize);
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        $('input', $randomize).attr('checked', fb.settings._randomize?fb.settings._randomize:false)
        .change(function(event) {
            if ($(this).attr('checked')) {
                 fb.settings._randomize = true;
            } else {
            	fb.settings._randomize= false;
            }
            fb.target._updateSettings(fb.item);
        });
        $('input', $randomize).trigger('change');
        var $slider = fb.item.find('.slider');
        if($slider.children().length == 0){
        	$slider.slider({
            	value:75, min: 25, max: 100,
            	slide: function( event, ui ) {
                    $('a',$slider).attr('style', 'background-image: url('+imageSourceDir+'/images/face-'+Math.round(ui.value/25).toFixed(0)+'.png) !important;');
            	}
            });
        }

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text('*');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbLikert', FbLikert);

var FbSingleLineText = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/textfield.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Single Line Text</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'SingleLineText',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div><div \
        		class="fullLengthField"><input type="text" class="textInput" /> \
	        <p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Single Line Text',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            mapMasterField: '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineText._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineText._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        
        var $fieldRangeLabel = $('<div>Range in characters</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="7"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="7"/></div>')
        //End ---->>> Master form field adding
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
				<option value="no">Any character</option> \
				<option value="alphanumeric">Alphanumeric only</option> \
				<option value="letterswithbasicpunc">Letters or punctuation only</option> \
				<option value="lettersonly">Letters only</option> \
        		<option value="email">Email</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        	.append(fb.target._twoColumns($fieldRangeMin,$fieldMin))
        	.append(fb.target._twoColumns($fieldRangeMax,$fieldMax))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append(fb.target._twoColumns($required, $restriction))
        		.append($hideFromUser)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        
        

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        $("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
        $('select', $restriction).change(function(event) {
            fb.settings.restriction = $(this).val();
            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
            fb.target._updateSettings(fb.item);
        });
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.minRange){
        	fb.settings.minRange = 0
        }
        $("input[id$='field.minRange']", $fieldMin).val(fb.settings.minRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue = 2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.minRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.maxRange){
        	fb.settings.maxRange = ""
        }
        $("input[id$='field.maxRange']", $fieldMax).val(fb.settings.maxRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue = 2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.maxRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineText.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineText.languageChange executed.');
    }
});

$.widget('fb.fbSingleLineText', FbSingleLineText);

var FbEmail = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/email.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Email Address</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'Email',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div><div \
        		class="fullLengthField"><input type="text" class="textInput" /> \
	        <p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Email Address',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            mapMasterField: '',
            restriction: 'email',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbEmail._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbEmail._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbEmail._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbEmail._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbEmail._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        
        var $fieldRangeLabel = $('<div>Range in characters</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="7"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="7"/></div>')
        //End ---->>> Master form field adding
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        	.append(fb.target._twoColumns($fieldRangeMin,$fieldMin))
        	.append(fb.target._twoColumns($fieldRangeMax,$fieldMax))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        //$valuePanel.append(fb.target._twoColumns($required, $restriction))
        //.append($hideFromUser)
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append(fb.target._twoColumns($required, $hideFromUser))
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        
        

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

//        $("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
//        $('select', $restriction).change(function(event) {
//            fb.settings.restriction = $(this).val();
//            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
//            fb.target._updateSettings(fb.item);
//        });
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.minRange){
        	fb.settings.minRange = 0
        }
        $("input[id$='field.minRange']", $fieldMin).val(fb.settings.minRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue = 2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.minRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.maxRange){
        	fb.settings.maxRange = ""
        }
        $("input[id$='field.maxRange']", $fieldMax).val(fb.settings.maxRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
            	if(originalValue > 2147483647)
            		originalValue = 2147483647
	            $(this).val(originalValue);
            }
        	fb.settings.maxRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbEmail._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbEmail.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbEmail.languageChange executed.');
    }
});

$.widget('fb.fbEmail', FbEmail);

var FbPhone = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/phone.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Phone</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'Phone',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div><div \
        		class="fullLengthField">\
        		<input type="text" class="textInput"/>\
	        <p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Phone',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            mapMasterField: '',
            restriction: 'no',
            format: 'US',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbPhone._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbPhone._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbPhone._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbPhone._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbPhone._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $format = $('<div><select id="field.format" style="width: 99%"> \
				<option value="US">United States</option> \
        		<option value="GB">United Kingdom</option> \
        		<option value="AU">Australia</option> \
        		<option value="CA">Canada</option> \
        		<option value="ZZ">International</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append(fb.target._twoColumns($required, $format))
        		.append($hideFromUser)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating
        
        

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        $("select option[value='" + fb.settings.format + "']", $format).attr('selected', 'true');
        $('select', $format).change(function(event) {
            fb.settings.format = $(this).val();
            fb.target._log('fb.settings.format = ' + fb.settings.format);
            fb.target._updateSettings(fb.item);
        });
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");
//        if(!fb.settings.minRange){
//        	fb.settings.minRange = 0
//        }
//        $("input[id$='field.minRange']", $fieldMin).val(fb.settings.minRange).keyup(function(event) {
//        	var value = $(this).val();
//            var originalValue = value;
//
//            if (value != '') {
//            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
//            	if(originalValue > 2147483647)
//            		originalValue = 2147483647
//	            $(this).val(originalValue);
//            }
//        	fb.settings.minRange = originalValue;
//            fb.target._updateSettings(fb.item);
//        }).trigger("change");
//        if(!fb.settings.maxRange){
//        	fb.settings.maxRange = ""
//        }
//        $("input[id$='field.maxRange']", $fieldMax).val(fb.settings.maxRange).keyup(function(event) {
//        	var value = $(this).val();
//            var originalValue = value;
//
//            if (value != '') {
//            	originalValue = originalValue.replace(/([^0-9]*)/g, "")
//            	if(originalValue > 2147483647)
//            		originalValue = 2147483647
//	            $(this).val(originalValue);
//            }
//        	fb.settings.maxRange = originalValue;
//            fb.target._updateSettings(fb.item);
//        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbPhone._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbPhone.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbPhone.languageChange executed.');
    }
});

$.widget('fb.fbPhone', FbPhone);

var FbLookUp = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/textfield.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Look Up</span>',
        belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
        _type: 'LookUp',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label> </div>\
		      	<div class="fullLengthField mClass"><input type="text" class="textInput lookUpInput" /><div class="LookUpButton"></div>\
	        <p class="formHint LookUpTypeFieldHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Look Up',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            fieldSize:'mClass',
            mapMasterField: '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbLookUp._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbLookUp._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbLookUp._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" class="lookUpInput" id="field.label"/>');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbLookUp._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbLookUp._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Look-up Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Look-up Fields</div>');
        var $masterFieldDD = $('<div><select style="width: 99%" multiple="multiple"></select></div>');
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        //End ---->>> Master form field adding
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
				<option value="no">Any character</option> \
				<option value="alphanumeric">Alphanumeric only</option> \
				<option value="letterswithbasicpunc">Letters or punctuation only</option> \
				<option value="lettersonly">Letters only</option> \
        		<option value="email">Email</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
				.append(fb.target._twoColumns($required, $restriction))
        		.append($hideFromUser)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
	        		masterFieldDD.focus();
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;

        $("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
        $('select', $restriction).change(function(event) {
            fb.settings.restriction = $(this).val();
            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
            fb.target._updateSettings(fb.item);
        });
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbLookUp._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbLookUp.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbLookUp.languageChange executed.');
    }
});

$.widget('fb.fbLookUp', FbLookUp);

var FbSubForm = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/application_form_add.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Sub Form</span>',
        belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
        _type: 'SubForm',
        _html : '<div><label style="font-weight:bold;"><em></em><span></span></label>\
        	<div class="datatableDiv">\
        	<table class="datatable" cellpadding="0" cellspacing="0" style="margin:0;"> \
            <thead><tr>                \
              <th style="width:20%;font-weight:bold;">Sub Form Field 1</th>\
              <th style="width:20%;font-weight:bold;">Sub Form Field 2</th>  \
              <th style="width:20%;font-weight:bold;">Sub Form Field 3</th>  \
              <th style="width:20%;font-weight:bold;">Sub Form Field 4</th> \
              <th style="width:20%;font-weight:bold;">Sub Form Field 5</th>\
            </tr></thead> \
            <tr>     \
              <td style="width:auto;">&nbsp;</td>\
              <td>&nbsp;</td>     \
              <td>&nbsp;</td>    \
              <td>&nbsp;</td>         \
              <td>&nbsp;</td>\
            </tr> \
          </table></div>\
	        <p class="formHint" style="width:100%;"></p></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Sub Form',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            subForm:'',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSubForm._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSubForm._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSubForm._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        
//        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
//                .append('<input type="text" id="field.value" />');
//        fb.item.find('.textInput').val(fb.settings.value);
//        $('input', $value).val(fb.settings.value)
//                .keyup(function(event) {
//            var value = $(this).val();
//            fb.item.find('.textInput').val(value);
//            fb.settings.value = value;
//            fb.target._updateSettings(fb.item);
//        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSubForm._getFieldSettingsLanguageSection executed.');
        //return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
        return [fb.target._oneColumn($label), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSubForm._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Sub form field adding
        var $subFormLabel = $('<div>Sub Form</div>');
        var $subFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $numericFieldLabel = $('<div>Show total for</div>');
        var $numericFieldDD = $('<div><select style="width: 99%" multiple="multiple"></select></div>');
        
        //End ---->>> Sub form field adding
        
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._twoColumns($subFormLabel,$subFormDD))
        		.append(fb.target._twoColumns($numericFieldLabel,$numericFieldDD))
        		.append($hideFromUser)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        $('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
		.change(function(event){
			if ($(this).attr('checked')) {
	            fb.settings.hideFromUser = true;
	            fb.item.css('backgroundColor', '#CFCFCF');
	            fb.settings.styles.label.backgroundColor = 'CFCFCF';
	            fb.target._updateSettings(fb.item);
	            if(ctrlHClicked==1)
	            	ctrlHClicked=2;
	        } else {
	            fb.settings.hideFromUser = false;
	            fb.item.css('backgroundColor', '');
	            fb.settings.styles.label.backgroundColor = '';
	            if(ctrlHClicked==1)
	            	ctrlHClicked=2;
	        }
			if(ctrlHClicked==2){
				fb.item.trigger('click');
				ctrlHClicked = "";
			}
	        fb.target._updateSettings(fb.item);
		});
		ctrlHClicked=3;
		$('input', $hideFromUser).trigger('change');
		ctrlHClicked=1;
        //Start --->>>Sub form DD populating
			for(var j = 0; j < subFormData.length; j++){
				var $opt = $('<option></option>')
	    		$('select',$subFormDD).append($opt)
	    		$opt.val(subFormData[j].id)
	    		$opt.text(subFormData[j].label)
	        }
			
			$('select',$numericFieldDD).change(function(){
        		fb.settings.numericSubField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
	    	
	    	$('select',$subFormDD).change(function(){
	    		var numericFieldDD = $('select',$numericFieldDD);
        		$('option',numericFieldDD).remove();
        		
	    		fb.settings.subForm = $(this).val()
	    		var selectedFormVal = $(this).val();
	    		var $datatableDiv = fb.item.find('.datatableDiv');
    			var counter = 0;
	    		var tableHTML = '';
	    		if(selectedFormVal != ''){
	    			tableHTML = '<table class="datatable" cellpadding="0" cellspacing="0" style="margin:0;"> \
	    	            <thead><tr>                \
	    	              <th>Edit</th>';
	    	            
	    			for(var i=0;i<subFormData.length;i++){
    					if(selectedFormVal == subFormData[i].id){
    						var fieldsList = subFormData[i].fieldsList
    						while(counter < fieldsList.length && counter < 4){
    							tableHTML += '<th>'+fieldsList[counter].label+'</th>';
    							counter++;
    						}
    					}
    				}
	    			tableHTML += '</tr></thead><tr>';
	    			for(var i=counter;i>=0;i--){
	    				tableHTML += '<td>&nbsp;</td>';
	    			}
	    			tableHTML += '</tr></table>';
	    			for(var i = 0; i < subFormData.length; i++){
	        			if(subFormData[i].id == $(this).val()){
	        				var fieldsList = subFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
	        					if(fieldsList[j].isNumeric == true){
			        				var $optionField = $('<option></option>');
			        				numericFieldDD.append($optionField);
			        				$optionField.text(fieldsList[j].label);
			        				$optionField.val(fieldsList[j].name);
	        					}
	        				}
	        			}
	        		}
	        		numericFieldDD.val(fb.settings.numericSubField);
	    		}else{
	    			tableHTML = '<table class="datatable" cellpadding="0" cellspacing="0" style="margin:0;"> \
	    	            <thead><tr>                \
	    	              <th style="width:20%;font-weight:bold;">Sub Form Field 1</th>\
	    	              <th style="width:20%;font-weight:bold;">Sub Form Field 2</th>  \
	    	              <th style="width:20%;font-weight:bold;">Sub Form Field 3</th>  \
	    	              <th style="width:20%;font-weight:bold;">Sub Form Field 4</th> \
	    	              <th style="width:20%;font-weight:bold;">Sub Form Field 5</th>\
	    	            </tr></thead> \
	    	            <tr>     \
	    	              <td style="width:auto;">&nbsp;</td>\
	    	              <td>&nbsp;</td>     \
	    	              <td>&nbsp;</td>    \
	    	              <td>&nbsp;</td>         \
	    	              <td>&nbsp;</td>\
	    	            </tr> \
	    	          </table>';
	    		}
	    		$datatableDiv.html(tableHTML);
	    		fb.target._updateSettings(fb.item);
	    	})
	    	.val(fb.settings.subForm);
	    	$('select',$subFormDD).trigger('change');
        //End --->>>Sub form DD populating

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSubForm._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSubForm.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSubForm.languageChange executed.');
    }
});

$.widget('fb.fbSubForm', FbSubForm);

var FbPageBreak = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/page_break.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Page Break</span>',
        belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
        _type: 'PageBreak',
        _html : '<div class="buttons pageButtons" style="border:0;padding: 5px 1em;margin:0;">\
        			<div class="backButton" style="display:inline-block;">\
        				<input type="button" class="button button-gray" value="Back" style="width:64px;" />\
        			</div>\
		        	<div class="nextButton" style="display:inline-block;">\
						<input type="button" class="button button-gray" value="Next" style="width:64px;" /> Page m of n\
					</div>\
        		</div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: '',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            subForm:'',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbPageBreak._getWidget executing...');
        fb.target._log('fbPageBreak._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbPageBreak._getFieldSettingsLanguageSection executing...');
        fb.target._log('fbPageBreak._getFieldSettingsLanguageSection executed.');
        return [];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbPageBreak._getFieldSettingsGeneralSection executing...');
        
        fb.target._log('fbPageBreak._getFieldSettingsGeneralSection executed.');
        return [];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbPageBreak.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbPageBreak.languageChange executed.');
    }
});

$.widget('fb.fbPageBreak', FbPageBreak);

var FbPaypal = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/paypal.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;<img src="'+imageSourceDir+'/images/paypal_text.png"></span>',
        belongsTo: $.fb.formbuilder.prototype.options._advanceFieldsPanel,
        _type: 'Paypal',
        _html : '<div><div id="tableDiv"></div><b>Total Payable Amount: 0 (<span class="currency">$</span>)</b></div>\
        		<div style="border:0;padding: 5px 0 0 0;margin:0;background:none;">\
        			<div class="pay_now_button"></div>\
        		</div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: '',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            itemForm:'',
            iaf:'',
            iqf:'',
            inf:'',
            iimgf:'',
            idescf:'',
            curr:'USD',
            emid:'',
            test:false,
            required: false,
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbPaypal._getWidget executing...');
        fb.target._log('fbPaypal._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbPaypal._getFieldSettingsLanguageSection executing...');
        var $itemFormLabel = $('<div>Source Form</div>');
        var $itemFormDD = $('<div><select style="width:99%"><option value="">--Please Select--</option></select></div>');
        var $infLabel = $('<div>Name Field</div>');
        var $infDD = $('<div><select style="width:99%"></select></div>');
        var $iimgfLabel = $('<div>Image Field</div>');
        var $iimgfDD = $('<div><select style="width:99%"></select></div>');
        var $idescfLabel = $('<div>Description Field</div>');
        var $idescfDD = $('<div><select style="width:99%"></select></div>');
        var $iafLabel = $('<div>Amount Field</div>');
        var $iafDD = $('<div><select style="width:99%"></select></div>');
        var $iqfLabel = $('<div>Quantity Field</div>');
        var $iqfDD = $('<div><select style="width:99%"></select></div>');
        var $currLabel = $('<div>Currency</div>');
        var $currDD = $('<div><select style="width:76px"><option value="USD">$</option><option value="GBP">&pound;</option><option value="JPY">&yen;</option><option value="EUR">&euro;</option></select></div>');
        var $emidLabel = $('<div>Paypal Id</div>');
        var $emidField = $('<div><input type="text" style="width:88%"></div>');
        var $testLabel = $('<div>Sandbox</div>');
        var $testField = $('<div><input type="checkbox"></div>');
        
        for(var j = 0; j < masterFormData.length; j++){
			var $opt = $('<option></option>')
    		$('select',$itemFormDD).append($opt)
    		$opt.val(masterFormData[j].id)
    		$opt.text(masterFormData[j].label)
        }
		
		var iafDD = $('select',$iafDD);
		var iqfDD = $('select',$iqfDD);
		var idescfDD = $('select',$idescfDD);
		var infDD = $('select',$infDD);
		var iimgfDD = $('select',$iimgfDD);
		var currDD = $('select',$currDD);
		var emidField = $('input',$emidField);
		var testField = $('input',$testField);
		var $tableDiv = fb.item.find('#tableDiv');
		iafDD.change(function(){fb.settings.iaf=$(this).val();fb.target._updateSettings(fb.item)});
		iqfDD.change(function(){fb.settings.iqf=$(this).val();fb.target._updateSettings(fb.item)});
		idescfDD.change(function(){fb.settings.idescf=$(this).val();fb.target._updateSettings(fb.item)});
		infDD.change(function(){fb.settings.inf=$(this).val();fb.target._updateSettings(fb.item)});
		iimgfDD.change(function(){fb.settings.iimgf=$(this).val();fb.target._updateSettings(fb.item)});
		testField.change(function(){fb.settings.test=this.checked;fb.target._updateSettings(fb.item)});
		if(fb.settings.test)
			testField.attr("checked", "checked");
		
		currDD.change(function(){fb.item.find('.currency').html($('option[value="'+this.value+'"]',$(this)).text());fb.settings.curr=$(this).val();fb.target._updateSettings(fb.item)});
		emidField.change(function(){fb.settings.emid=$(this).val();fb.target._updateSettings(fb.item)});
		currDD.val(fb.settings.curr);
		emidField.val(fb.settings.emid);
    	
    	$('select',$itemFormDD).change(function(){
    		$('option',iafDD).remove();
    		$('option',iqfDD).remove();
    		$('option',idescfDD).remove();
    		$('option',infDD).remove();
    		$('option',iimgfDD).remove();
    		
    		fb.settings.itemForm = $(this).val()
    		var selectedFormVal = $(this).val();
    		var $datatableDiv = fb.item.find('.datatableDiv');
			var counter = 0;
    		if(selectedFormVal != ''){
    			var tableHTML = '<table class="itmTable">\
    				<tbody>\
    					<tr class="itmHeader"><td>Preview</td><td>Product Name &amp; Description</td><td>Price</td><td>Quantity</td><td>Amount</td></tr>\
    					<tr class="itm">\
							<td><img src="/form-builder/images/previewImage.png" style="width: 80px;"></td>\
							<td><b>Item Name</b><br>Item description here.</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>\
    					</tr>\
    				</tbody>\
    				</table>';
    			$tableDiv.html(tableHTML);
    	        for(var i = 0; i < masterFormData.length; i++){
        			if(masterFormData[i].id == $(this).val()){
        				var fieldsList = masterFormData[i].fieldsList
        				for(var j = 0; j < fieldsList.length; j++){
        					if(fieldsList[j].isNumeric || fieldsList[j].isString || fieldsList[j].isFileUpload){
	        					var $optionField = $('<option></option>');
	        					$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        					if(fieldsList[j].isNumeric){
			        				iafDD.append($optionField);
			        				iqfDD.append($optionField.clone());
	        					}else if(fieldsList[j].isString){
	        						infDD.append($optionField);
	        						idescfDD.append($optionField.clone());
	        					}else if(fieldsList[j].isFileUpload){
	        						iimgfDD.append($optionField);
	        					}
        					}
        				}
        			}
        		}
    		}else{
    			$tableDiv.html('');
    			var $optionFieldEmpty = $('<option></option>');
				$optionFieldEmpty.text("--Please select--");
				$optionFieldEmpty.val('');
				iqfDD.append($optionFieldEmpty);
				infDD.append($optionFieldEmpty.clone());
				idescfDD.append($optionFieldEmpty.clone());
				
    			$('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
    				var $ctrlHolder = $(this);
    				var rel = $ctrlHolder.attr('rel');
    				var type = $ctrlHolder.find("input[id$='fields[" + rel + "].type']").val();
    				var isNumeric = false,isString = false
    				if(type == 'SingleLineNumber' || type == 'FormulaField'){
    					isNumeric = true
    				}else if(type == 'SingleLineText' || type == 'MultiLineText'){
    					isString = true
    				}					
    				if(isNumeric || isString){
    					var $settings = $ctrlHolder.find("input[id$='fields[" + rel + "].settings']");
    					var $fieldName = $ctrlHolder.find("input[id$='fields[" + rel + "].name']").val();
    					var settings = $.parseJSON(unescape($settings.val()))
    					var $optionField = $('<option></option>');
    					$optionField.text(settings.en.label);
        				$optionField.val($fieldName);
    					if(isNumeric){
    						iafDD.append($optionField);
	        				iqfDD.append($optionField.clone());
    					}else{
    						infDD.append($optionField);
    						idescfDD.append($optionField.clone());
    					}//No need because here we are picking things form same page.
    				}
    			});
    		}
    		var setDD = function(dd,value){
    			value = dd.val(value!=null?value:"").val();
    			dd.val(value)
    			return value;
    		}
    		fb.settings.inf = setDD(infDD,fb.settings.inf);
    		fb.settings.iimgf = setDD(iimgfDD,fb.settings.iimgf);
    		fb.settings.idescf = setDD(idescfDD,fb.settings.idescf);
    		fb.settings.iaf = setDD(iafDD,fb.settings.iaf);
    		fb.settings.iqf = setDD(iqfDD,fb.settings.iqf);
    		fb.target._updateSettings(fb.item);
    	})
    	.val(fb.settings.itemForm);
    	$('select',$itemFormDD).trigger('change');
    	var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        fb.target._log('fbPaypal._getFieldSettingsLanguageSection executed.');
        var returnArr = [fb.target._twoColumns($itemFormLabel,$itemFormDD),
                         fb.target._twoColumns($infLabel,$infDD),
                         fb.target._twoColumns($iimgfLabel,$iimgfDD),
                         fb.target._twoColumns($idescfLabel,$idescfDD),
                         fb.target._twoColumns($iafLabel,$iafDD),
                         fb.target._twoColumns($iqfLabel,$iqfDD),
                         fb.target._twoColumns($currLabel,$currDD),
                         fb.target._twoColumns($emidLabel,$emidField),
                         fb.target._twoColumns($testLabel,$testField)
                        ];
        for(var i=0;i<returnArr.length;i++){
        	$('.col1', returnArr[i]).css('width', '40%').css('padding-top','4px').removeClass('labelOnTop');
        	$('.col2', returnArr[i]).css('marginLeft', '42%').removeClass('labelOnTop');
        }
        return returnArr;
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbPaypal._getFieldSettingsGeneralSection executing...');
        
        fb.target._log('fbPaypal._getFieldSettingsGeneralSection executed.');
        return [];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbPaypal.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbPaypal.languageChange executed.');
    }
});

$.widget('fb.fbPaypal', FbPaypal);

var ImageUpload = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
         name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/image_add.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Link Image</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'ImageUpload',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span></label></div> \
        	<div class="fullLengthField"><img src="" class="imgDisplay" width="100px" height="100px" id="image"/><p></p><input type="button" class="button button-green" style="display:none;" value="Select Image"><input type="file" name="imageFile" style="display:none;"/></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Image',
                height: 100,
                width: 100,
                value: '',
                description: '',
                uploadImage:false,
                clickable:false,
                clickableURL:'',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                height: 100,
                width: 100,
                value: '',
                description: '',
                uploadImage:false,
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [0, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var uuid = (((1 + Math.random()) * 0x10000) | 0).toString(16).substring(1);
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbImageUpload._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);

        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbImageUpload._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Image Url', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        
        $("input", $value).val(fb.settings.value)
                .keyup(function(event){
                	var value = $(this).val();
    	            fb.item.find('img').attr("src", value);
    	            fb.settings.value = value;
    	            fb.target._updateSettings(fb.item);
                });
        $("input[type='button'].button").unbind('click')
        $("input[type='button'].button").click(function(event){
        	var inputPar = fb.item.find("input[type='file']");
        	inputPar.trigger("click");
        });
        
        $("input[type='file']").change(function(event){
        	var imagePath = $(this).val()
        	 fb.item.find('p').html(imagePath)
        });
        
        var $imageUpload = fb.target._twoColumns($('<div><input type="checkbox" id="field.imageUpload"/></div>'),$('<div>Upload image</div>'));
        $('.col1', $imageUpload).css('width', '10%').removeClass('labelOnTop').css('padding','5px 0');
        $('.col2', $imageUpload).css('marginLeft', '10%').removeClass('labelOnTop').css('padding','8px 0');
        
        var $clickable = fb.target._twoColumns($('<div><input type="checkbox" id="field.clickable"/></div>'),$('<div>Clickable</div>'));
        $('.col1', $clickable).css('width', '10%').removeClass('labelOnTop').css('padding','5px 0');
        $('.col2', $clickable).css('marginLeft', '10%').removeClass('labelOnTop').css('padding','8px 0');
		
        var $clickableURL = fb.target._label({ label: 'Clickable Url (with http:// or https://)', name: 'field.clickableURL' })
        .append('<input type="text" id="field.clickableURL" />');
        

		if(!fb.settings.uploadImage){
			fb.settings.uploadImage = false;
		}
        $("input[id$='field.imageUpload'][type='checkbox']", $imageUpload).attr('checked', fb.settings.uploadImage).change(function(event) {
        	var inputPar = fb.item.find("input[type='file']");
        	var buttonPar = fb.item.find("input[type='button']")
			var $fieldName = fb.item.find("input[id$='fields[" + fb.item.attr('rel') + "].name']").val();
			if (this.checked) {
				buttonPar.val('Select Image')
				buttonPar.show()
				fb.settings.uploadImage = true;
			} else {
				buttonPar.hide()
				fb.settings.uploadImage = false;
				$('input',$value).trigger('keyup');
			}
			inputPar.attr('name',$fieldName)
		    fb.target._updateSettings(fb.item);
		}).trigger('change');
        
		if(!fb.settings.clickable){
			fb.settings.clickable = '';
		}
        $("input[id$='field.clickable'][type='checkbox']", $clickable).attr('checked', fb.settings.clickable).change(function(event) {
			if (this.checked) {
				$clickableURL.show();
				fb.settings.clickable = true;
			} else {
				$clickableURL.hide()
				fb.settings.clickable = false;
			}
		    fb.target._updateSettings(fb.item);
		}).trigger('change');
        
		if(!fb.settings.clickableURL){
			fb.settings.clickableURL = '';
		}
        $("input[id$='field.clickableURL']", $clickableURL).val(fb.settings.clickableURL).keyup(function(event){
        	var clickableURL = $(this).val();
        	fb.settings.clickableURL = clickableURL;
        	fb.target._updateSettings(fb.item);
        });

        var $height = $('<div> \
            height : <input type="text">\
                  </div>');
        var $width = $('<div> \
            width <input type="text">\
                  </div>');
        $('input',$height).val(fb.settings.height)
        $('input',$height).keyup(function(event){
    		var heightValue = $(this).val();
    		if (heightValue != '') {
    			heightValue = heightValue.replace(/([^0-9]*)/g, "")
    		}else{
    			$('.ctrlHolderSelected img').height("");
    			heightValue = $('.ctrlHolderSelected img').height();
    		}
    		$('.ctrlHolderSelected img').height(heightValue*1);
    		$(this).val(heightValue);
    		fb.settings.height = heightValue;
    		fb.target._updateSettings(fb.item);
    	}).keydown(function(e){
    		if (e.keyCode == 38) {
    			var heightValue = $(this).val();
    			$(this).val(heightValue * 1 + 5);
    		}else if (e.keyCode == 40) {
    			var widthValue = $(this).val();
    			$(this).val(widthValue * 1 - 5);
    		}
    	}).blur(function(event){
    		$(this).trigger('keyup');
    	});
        
        $('input',$width).val(fb.settings.width)
        $('input',$width).keyup(function(event){
    		var widthValue = $(this).val();
    		if (widthValue != '') {
    			widthValue = widthValue.replace(/([^0-9]*)/g, "")
    			var pWidthValue = $('.ctrlHolderSelected img').parent().innerWidth();
    			widthValue = widthValue > pWidthValue?pWidthValue:widthValue;
    		}else{
    			$('.ctrlHolderSelected img').width("");
    			$('.ctrlHolderSelected img').removeAttr("width");
    			$('.ctrlHolderSelected img').height("");
    			$('.ctrlHolderSelected img').removeAttr("height");
    			$('.ctrlHolderSelected img').hide();
    			var pWidthValue = $('.ctrlHolderSelected img').parent().innerWidth();
    			var cWidthValue = $('.ctrlHolderSelected img').innerWidth();
    			widthValue = (cWidthValue>pWidthValue?pWidthValue:cWidthValue);
    			$('.ctrlHolderSelected img').show();
    		}
    		$('.ctrlHolderSelected img').width(widthValue*1);
    		$(this).val(widthValue);
    		$('input',$height).val($('.ctrlHolderSelected img').height());
    		fb.settings.width = widthValue;
    		fb.settings.height = $('input',$height).val()
    		fb.target._updateSettings(fb.item);
    	}).keydown(function(e){
    		if (e.keyCode == 38) {
    			var widthValue = $(this).val();
    			$(this).val(widthValue * 1 + 5);
    		}else if (e.keyCode == 40) {
    			var widthValue = $(this).val();
    			$(this).val(widthValue * 1 - 5);
    		}
    	}).blur(function(event){
    		$(this).trigger('keyup');
    	});
        var $choicePanel = fb.target._fieldset({ text: 'Choices'})
                .append(fb.target._twoColumns($height, $width));


        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbImageUpload._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value),$imageUpload,$clickable,$clickableURL,$choicePanel,$fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbImageUpload._getFieldSettingsGeneralSection executing...');
        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbImageUpload._getFieldSettingsGeneralSection executed.');
        //return [$valuePanel, $colorPanel];
        return [$colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbImageUpload.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbImageUpload.languageChange executed.');
    }
});

$.widget('fb.fbImageUpload', ImageUpload);
var FbSingleLineNumber = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        /*name: '<span style="position:relative;z-index:1"><img src="'+imageSourceDir+'/images/abc.bmp" style="position:absolute;left:-20px;z-index:2" >Single Line Number</span>',*/
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/singlelinetext.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Number</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'SingleLineNumber',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label></div> \
        		<div class="fullLengthField"><div class="singlelineNumberCurrency singlelineNumbermargin" ></div><input type="text" class="textInput" /> \
	        	<p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Numeric Text',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            decimalPlaces:'2',
            currencyType: '',
            hideFromUser: false,
            mapMasterForm: '',
            mapMasterField: '',
            fieldSize:'mClass',
            minRange:'0',
            maxRange : '',
            restriction: 'no',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineNumber._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineNumber._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineNumber._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9.]*)/g, "")
	            var temp = "";
	            var flag = false;
	            for(var i=0;i<originalValue.length;i++){
	            	if(flag && originalValue.charAt(i)==".")
	            		break;
	            	if(!flag)
	            		flag = originalValue.charAt(i)==".";
	            	temp += originalValue.charAt(i);
	            }
	            while(temp.indexOf("0")==0 && temp.indexOf("0.")!=0 && temp.length>1){
	            	temp = temp.replace("0","");
	            }
	            originalValue = temp;
	            $(this).val(originalValue);
            }
            
            
            fb.item.find('.textInput').val(originalValue);
            fb.settings.value = originalValue;
            fb.target._updateSettings(fb.item);
        }).blur(function(event){
        	$(this).trigger('keyup');
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        
        

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineNumber._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineNumber._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        //End ---->>> Master form field adding
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        var $fieldRangeLabel = $('<div>Range for value</div>')
        var $fieldRangeMin = $('<div>Minimum</div>')
        var $fieldRangeMax = $('<div>Maximum</div>')
        var $fieldMin = $('<div><input type="text" id="field.minRange" style="width:70px;" value="0" maxlength="20"/></div>')
        var $fieldMax = $('<div><input type="text" id="field.maxRange" style="width:70px;" value="" maxlength="20"/></div>')
        var $decimalRange = $('<div>Decimal</div>')
        var $currencyTypes = $('<div>Currency</div>')
         var $decimalRangeSelect = $('<div><select id="decimal.range" style="width:85px;"> \
 				<option value="0">0</option>\
 				<option value="1">1</option>\
 				<option value="2">2</option>\
 			</select></div>');
        var $currencyTypeSelect = $('<div><select id="currency.type" style="width:85px;"> \
        		<option value=""> </option> \
				<option value="USD">&#36;</option> \
				<option value="GBP">&pound;</option> \
        		<option value="JPY">&yen;</option> \
				<option value="EUR">&euro;</option> \
			</select></div>');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
       /* var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
				<option value="no">Numeric type</option> \
			</select></div>');*/
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        $valuePanel.append(fb.target._oneColumn($fieldRangeLabel))
        	.append(fb.target._twoColumns($fieldRangeMin,$fieldMin))
        	.append(fb.target._twoColumns($fieldRangeMax,$fieldMax))
        	.append(fb.target._twoColumns($decimalRange,$decimalRangeSelect))
        	.append(fb.target._twoColumns($currencyTypes,$currencyTypeSelect))
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append(fb.target._twoColumns($required, $hideFromUser))
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        
        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	$('select',$decimalRangeSelect).change(function(){
        		fb.settings.decimalPlaces = $(this).val()
        		fb.target._updateSettings(fb.item);
        	}).val(fb.settings.decimalPlaces?fb.settings.decimalPlaces:2);
        	$('select',$currencyTypeSelect).change(function(){
        		fb.settings.currencyType = $(this).val()
        		fb.target._updateSettings(fb.item);
        		var currNum = fb.item.find(".singlelineNumberCurrency");
        		var currNumTextInput = fb.item.find(".textInput");
        		if($(this).val()!=""){
        			$(currNum).html($(this).find('option:selected').text());
        			$(currNumTextInput).addClass("textInputCurrency")
        			$(currNum).removeClass("singlelineNumbermargin")
        		}else{
        			$(currNum).html("");
        			$(currNum).addClass("singlelineNumbermargin")
        			$(currNumTextInput).removeClass("textInputCurrency")
        		}
        	}).val(fb.settings.currencyType);;
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar = fb.item.find('input').parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.minRange){
        	fb.settings.minRange = 0
        }
        $("input[id$='field.minRange']", $fieldMin).val(fb.settings.minRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9.]*)/g, "")
	            var temp = "";
	            var flag = false;
	            for(var i=0;i<originalValue.length;i++){
	            	if(flag && originalValue.charAt(i)==".")
	            		break;
	            	if(!flag)
	            		flag = originalValue.charAt(i)==".";
	            	temp += originalValue.charAt(i);
	            }
	            while(temp.indexOf("0")==0 && temp.indexOf("0.")!=0 && temp.length>1){
	            	temp = temp.replace("0","");
	            }
	            if(temp > 2147483647)
	            	temp = 2147483647
	            originalValue = temp;
            }
        	fb.settings.minRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");
        if(!fb.settings.maxRange){
        	fb.settings.maxRange = ""
        }
        $("input[id$='field.maxRange']", $fieldMax).val(fb.settings.maxRange).keyup(function(event) {
        	var value = $(this).val();
            var originalValue = value;

            if (value != '') {
            	originalValue = originalValue.replace(/([^0-9.]*)/g, "")
	            var temp = "";
	            var flag = false;
	            for(var i=0;i<originalValue.length;i++){
	            	if(flag && originalValue.charAt(i)==".")
	            		break;
	            	if(!flag)
	            		flag = originalValue.charAt(i)==".";
	            	temp += originalValue.charAt(i);
	            }
	            while(temp.indexOf("0")==0 && temp.indexOf("0.")!=0 && temp.length>1){
	            	temp = temp.replace("0","");
	            }
	            if(temp > 2147483647)
	            	temp = 2147483647
	            originalValue = temp;
            }
        	fb.settings.maxRange = originalValue;
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        /*$("select option[value='" + fb.settings.restriction + "']", $restriction).attr('selected', 'true');
        $('select', $restriction).change(function(event) {
            fb.settings.restriction = $(this).val();
            fb.target._log('fb.settings.restriction = ' + fb.settings.restriction);
            fb.target._updateSettings(fb.item);
        });*/

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineNumber._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineNumber.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineNumber.languageChange executed.');
    }
});

$.widget('fb.fbSingleLineNumber', FbSingleLineNumber);



var FbSingleLineDate = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/date_input.png" style="position:absolute;left:-7px;z-index:2;width:15px" >&nbsp;Date</span>',
        belongsTo: $.fb.formbuilder.prototype.options._standardFieldsPanel,
        _type: 'SingleLineDate',
        _html : '<div><div class="fullLengthLabel"><label style="font-weight:bold;"><span></span><em></em></label> </div>\
        		<div class="fullLengthField"><div class="singleDateField mClass"><input type="text" class="textInput"/><img src="'+imageSourceDir+'/images/calendar_16.png" style="position:absolute;margin:5px;" ></div>\
        		<div class="boxDateField" style="display:none;width:50%;"><input type="text" style="width:22px !important;" placeholder="MM"/>/<input type="text" style="width:22px !important;" placeholder="DD"/>/<input type="text" style="width:39px !important;" placeholder="YYYY"/></div>\
        		<div class="timeFormatDiv" style="display:none;width:50%;"><select class="hours" style="margin-top:3px;width:60px;margin-left:2px;clear:left;"><option>hh</option></select>\
        		<select class="minutes" style="margin-top:3px;width:60px;margin-left:2px;"><option>mm</option></select>\
        		<select class="meridian" style="margin-top:3px;width:60px;margin-left:2px;"><option>AM</option></select></div>\
        	<p class="formHint"></p></div></div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Date',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: '',
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            fieldSize:'mClass',
            hideFromUser: false,
            showCalendar: true,
            mapMasterForm: '',
            mapMasterField: '',
            restriction: 'no',
            timeFormat:'',
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {
        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
        var $jqueryObject = $(fb.target.options._html);
        fb.target._log('fbSingleLineDate._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        $('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbSingleLineDate._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbSingleLineDate._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Label', name: 'field.label' })
                .append('<input type="text" id="field.label" />');
        $('input', $label).val(fb.settings.label)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        var $value = fb.target._label({ label: 'Value', name: 'field.value' })
                .append('<input type="text" id="field.value" />');
        fb.item.find('.textInput').val(fb.settings.value);
        $('input', $value).val(fb.settings.value)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.textInput').val(value);
            fb.settings.value = value;
            fb.target._updateSettings(fb.item);
        });

        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
                .append('<textarea id="field.description"></textarea>');
        $('textarea', $description).val(fb.settings.description)
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('.formHint').text(value);
            fb.settings.description = value;
            fb.target._updateSettings(fb.item);
        });

        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineDate._getFieldSettingsLanguageSection executed.');
        return [fb.target._twoColumns($label, $value), fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineDate._getFieldSettingsGeneralSection executing...');
        //Start ---->>> Master form field adding
        var $masterFormsMulSel = $("select[id$='form.masterForms']")
        var selectedMasterForms = $masterFormsMulSel.val()
        
        var $masterFormLabel = $('<div>Mapped Form</div>');
        var $masterFormDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        
        var $masterFieldLabel = $('<div>Mapped Field</div>');
        var $masterFieldDD = $('<div><select style="width: 99%"><option value="">--Please Select--</option></select></div>');
        //End ---->>> Master form field adding
        
        var $fieldSizeLabel = $('<div>Field Size</div>');
        var $fieldSizeDD = $('<div><select id="field.size" style="width: 99%"> \
				<option value="sClass">Small</option> \
				<option value="mClass">Medium</option> \
				<option value="lClass">Large</option> \
			</select></div>');
        
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
//        var $restriction = $('<div><select id="field.restriction" style="width: 99%"> \
//				<option value="no">date type</option> \
//			</select></div>');
        var $timeFormat = $('<div><select id="field.format" style="width: 99%"> \
				<option value="">None</option> \
        		<option value="HH:mm">23:59</option> \
        		<option value="hh:mm a">11:59 PM</option> \
			</select></div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $showCalendar = $('<div><input type="checkbox" id="field.showCalendar" />&nbsp;Show Calendar</div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
        
        //Start ---->>> Master form field adding
        if(selectedMasterForms != null){
        	$valuePanel.append(fb.target._twoColumns($masterFormLabel,$masterFormDD))
    		.append(fb.target._twoColumns($masterFieldLabel,$masterFieldDD))
        }
        //End ---->>> Master form field adding
        		
        $valuePanel.append(fb.target._twoColumns($fieldSizeLabel,$fieldSizeDD))
        		.append(fb.target._twoColumns($('<div>Time Format</div>'), $timeFormat))
        		.append(fb.target._twoColumns($required,$hideFromUser))
        		.append($showCalendar);
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');
        
        //Start --->>>Master form DD populating
        if(selectedMasterForms != null){
        	$('option',$masterFormsMulSel).each(function(){
        		for(var j = 0; j < selectedMasterForms.length; j++){
		        	if($(this).val() == selectedMasterForms[j]){
		        		$('select',$masterFormDD).append($(this).clone())
		        	}
		        }
        	});
        	
        	$('select',$masterFieldDD).change(function(){
        		fb.settings.mapMasterField = $(this).val()
        		fb.target._updateSettings(fb.item);
        	});
        	
        	$('select',$masterFormDD).change(function(){
        		var masterFieldDD = $('select',$masterFieldDD);
        		$('option',masterFieldDD).remove();
        		masterFieldDD.append($('<option value="">--Please Select--</option>'));
        		if($(this).val() != ''){
	        		for(var i = 0; i < masterFormData.length; i++){
	        			if(masterFormData[i].id == $(this).val()){
	        				var fieldsList = masterFormData[i].fieldsList
	        				for(var j = 0; j < fieldsList.length; j++){
		        				var $optionField = $('<option></option>');
		        				masterFieldDD.append($optionField);
		        				$optionField.text(fieldsList[j].label);
		        				$optionField.val(fieldsList[j].name);
	        				}
	        			}
	        		}
	        		masterFieldDD.val(fb.settings.mapMasterField);
        		}
        		fb.settings.mapMasterForm = $(this).val();
        		fb.target._updateSettings(fb.item);
        	})
        	.val(fb.settings.mapMasterForm);
        	$('select',$masterFormDD).trigger('change');
        }
        //End --->>>Master form DD populating

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        
        if(!fb.settings.showCalendar){
        	fb.settings.showCalendar=false
        }
        $('input', $showCalendar).attr('checked', fb.settings.showCalendar)
		.change(function(event){
			var boxDateFieldPar = fb.item.find(".boxDateField")
			var singleDateFieldPar = fb.item.find(".singleDateField")
			if ($(this).attr('checked')) {
				$(boxDateFieldPar).hide()
				$(singleDateFieldPar).show()
				fb.settings.showCalendar = true;
	        } else {
	        	$(singleDateFieldPar).hide()
	        	$(boxDateFieldPar).css("display","inline-block")
	        	fb.settings.showCalendar = false;
	        }
	        fb.target._updateSettings(fb.item);
		}).trigger('change');
        
        if(!fb.settings.fieldSize){
        	fb.settings.fieldSize="mClass"
        }
        $('select', $fieldSizeDD).val(fb.settings.fieldSize).change(function(event) {
            fb.settings.fieldSize = $(this).val();
            var inputPar =  fb.item.find("img").parent()
            if($(inputPar).hasClass("sClass")){
            	$(inputPar).removeClass("sClass")
            }else if(inputPar.hasClass("mClass")){
            	$(inputPar).removeClass("mClass")
            }else if(inputPar.hasClass("lClass")){
            	$(inputPar).removeClass("lClass")
            }
            $(inputPar).addClass(fb.settings.fieldSize)
            fb.target._updateSettings(fb.item);
        }).trigger("change");

        $("select option[value='" + fb.settings.timeFormat + "']", $timeFormat).attr('selected', 'true');
        $('select', $timeFormat).change(function(event) {
            fb.settings.timeFormat = $(this).val();
            if(fb.settings.timeFormat!=''){
            	var timeFormatDiv = fb.item.find('.timeFormatDiv');
            	timeFormatDiv.show();
            	if(fb.settings.timeFormat=='HH:mm'){
            		$('.hours option',timeFormatDiv).text('HH');
            		$('.meridian',timeFormatDiv).hide()
            	}else if(fb.settings.timeFormat=='hh:mm a'){
            		$('.hours option',timeFormatDiv).text('hh');
            		$('.meridian',timeFormatDiv).show()
            	}
            }else{
            	fb.item.find('.timeFormatDiv').hide();
            }
            fb.target._log('fb.settings.timeFormat = ' + fb.settings.timeFormat);
            fb.target._updateSettings(fb.item);
        });

        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineDate._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbSingleLineDate.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbSingleLineDate.languageChange executed.');
    }
});

$.widget('fb.fbSingleLineDate', FbSingleLineDate);

var fbLikeDislikeButton = $.extend({}, $.fb.fbWidget.prototype, {
    options: { // default options. values are stored in widget's prototype
        name: '<span style="padding-left:7px;position:relative;font-size:11px"><img src="'+imageSourceDir+'/images/icons/thumb_up.png" style="position:absolute;left:-7px;z-index:2;width:16px" >&nbsp;Like/Dislike</span>',
        belongsTo: $.fb.formbuilder.prototype.options._fancyFieldsPanel,
        _type: 'LikeDislikeButton',
        _html : '<div>\
		        	<div class="fullLengthLabel">\
		        		<label style="font-weight:bold;"><span></span><em></em></label>\
		        	</div>\
        			<div class="fullLengthField">\
        				<div class="likeDislikeOption" >\
        					<div id="LikeChoicerd">\
        						<div class="thumbStyle thumbUp">&nbsp;</div>\
        						<span id="LikeChoicesr">Like (+1 Likes)</span><br/><br/>\
        					</div>\
							<div id="DislikeChoicerd">\
        						<div class="thumbStyle thumbDown">&nbsp;</div>\
        						<span id="DislikeChoicesr">Dislike (-1 Dislikes)</span><br/><br/>\
        					</div>\
					   </div>\
	        			<div class="voteOption" style="display:none;margin-top:10px;">\
	        				<div class="button1 small green" style="padding: 12px 12px;"> +1 Votes</div><br/><br/>\
							<div class="button button-gray">Vote It!</div>\
						</div>\
        				<p class="formHint"></p>\
        			</div>\
        	  </div>',
        _counterField: 'label',
        _languages: [ 'en', 'zh_CN' ],
        settings: {
            en: {
                label: 'Like/Dislike',
                value: ["Like","Dislike"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            zh_CN : {
                label: '??????',
                value: ["1","0"],
                description: '',
                styles: {
                    fontFamily: 'default', // form builder default
                    fontSize: 'default',
                    fontStyles: [1, 0, 0] // bold, italic, underline
                }
            },
            _persistable: true,
            required: false,
            hideFromUser: false,
            restriction: 'no',
            likeAndVote:false,
            styles : {
                label: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                value: {
                    color : 'default',
                    backgroundColor : 'default'
                },
                description: {
                    color : '777777',
                    backgroundColor : 'default'
                }
            }
        }
    },
    _init : function() {

        // calling base plugin init
        $.fb.fbWidget.prototype._init.call(this);
        // merge base plugin's options
        this.options = $.extend({}, $.fb.fbWidget.prototype.options, this.options);
    },
    _getWidget : function(event, fb) {
		var index = $('#builderForm div.ctrlHolder').size() + $('#builderForm div.ctrlHolderChk').size();
        var radID = (index1==0)?(index1 = index):index1;
        index = index1 - 1;
        
        var $jqueryObject = $(fb.target.options._html);
		$('#GrpBtnPanel', $jqueryObject).attr("id","GrpBtnPanel"+radID);
		var counter = 1;
		$('input',$jqueryObject).each(function() {
			$(this).attr("id","radio" + radID + "_" + counter);
			$(this).val(fb.settings.value[counter-1]);
			counter =  counter+1;
		});
        fb.target._log('fbLikeDislikeButton._getWidget executing...');
        $('label span', $jqueryObject).text(fb.settings.label);
        if (fb._settings.required) {
            $('label em', $jqueryObject).text('*');
        }
        
        //$('input', $jqueryObject).val(fb.settings.value);
        $('.formHint', $jqueryObject).text(fb.settings.description);
        fb.target._log('fbLikeDislikeButton._getWidget executed.');
        return $jqueryObject;
    },
    _getFieldSettingsLanguageSection : function(event, fb) {
        fb.target._log('fbLikeDislikeButton._getFieldSettingsLanguageSection executing...');
        var $label = fb.target._label({ label: 'Field Label (?)', name: 'field.label' })
                .append('<textarea id="field.label" />');
        $('textarea', $label).val(fb.item.find('label span').text())
                .keyup(function(event) {
            var value = $(this).val();
            fb.item.find('label span').text(value);
            fb.settings.label = value;
            fb.target._updateSettings(fb.item);
            fb.target._updateName(fb.item, value);
        });
        
        var $description = fb.target._label({ label: 'Description', name: 'field.description' })
        .append('<textarea id="field.description"></textarea>');
		$('textarea', $description).val(fb.settings.description)
		        .keyup(function(event) {
		    var value = $(this).val();
		    fb.item.find('.formHint').text(value);
		    fb.settings.description = value;
		    fb.target._updateSettings(fb.item);
		});
        
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var $fontPanel = fb.target._fontPanel({ fontFamily: fontFamily, fontSize: fontSize,
            fontStyles: styles.fontStyles, idPrefix: 'field.', nofieldset: true });

        $("input[id$='field.bold']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontWeight', 'bold');
                fb.item.find('.textInput').css('fontWeight', 'bold');
                styles.fontStyles[0] = 1;
            } else {
                fb.item.find('label').css('fontWeight', 'normal');
                fb.item.find('.textInput').css('fontWeight', 'normal');
                styles.fontStyles[0] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.italic']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label').css('fontStyle', 'italic');
                fb.item.find('.textInput').css('fontStyle', 'italic');
                styles.fontStyles[1] = 1;
            } else {
                fb.item.find('label').css('fontStyle', 'normal');
                fb.item.find('.textInput').css('fontStyle', 'normal');
                styles.fontStyles[1] = 0;
            }
            fb.target._updateSettings(fb.item);
        });
        $("input[id$='field.underline']", $fontPanel).change(function(event) {
            if ($(this).attr('checked')) {
                fb.item.find('label span').css('textDecoration', 'underline');
                fb.item.find('.textInput').css('textDecoration', 'underline');
                styles.fontStyles[2] = 1;
            } else {
                fb.item.find('label span').css('textDecoration', 'none');
                fb.item.find('.textInput').css('textDecoration', 'none');
                styles.fontStyles[2] = 0;
            }
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.fontFamily']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.css('fontFamily', value);
            fb.item.find('.textInput').css('fontFamily', value);
            styles.fontFamily = value;
            fb.target._updateSettings(fb.item);
        });

        $("select[id$='field.fontSize']", $fontPanel).change(function(event) {
            var value = $(this).val();
            fb.item.find('label').css('fontSize', value + 'px');
            fb.item.find('.textInput').css('fontSize', value + 'px');
            styles.fontSize = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsLanguageSection executed.');
        return [fb.target._oneColumn($label),fb.target._oneColumn($description), $fontPanel];
    },
    _getFieldSettingsGeneralSection : function(event, fb) {
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executing...');
        var $required = $('<div><input type="checkbox" id="field.required" />&nbsp;Required</div>');
        var $hideFromUser = $('<div><input type="checkbox" id="field.hideFromUser" />&nbsp;Hide From User</div>');
        var $voteAndLike = $('<div><label for="field.likeDislike"> Type </label>&nbsp;<select id="field.likeDislike"><option value="false">Like/Dislike</option><option value="true">Voting</option></select></div>');
        var $valuePanel = fb.target._fieldset({ text: 'Value'})
                .append(fb.target._twoColumns($required, $hideFromUser))
        		.append($voteAndLike)
        $('.col1', $valuePanel).css('width', '32%').removeClass('labelOnTop');
        $('.col2', $valuePanel).css('marginLeft', '34%').removeClass('labelOnTop');

        $('input', $required).attr('checked', fb.settings.required)
		        .change(function(event) {
				    if ($(this).attr('checked')) {
				    	$('input', $hideFromUser).attr('disabled','disabled');
						fb.item.find('em').text(' *');
				        fb.settings.required = true;
				    } else {
				    	$('input', $hideFromUser).removeAttr('disabled');
				        fb.item.find('em').text('');
				        fb.settings.required = false;
				    }
				    fb.target._updateSettings(fb.item);
				});
        $('input', $required).trigger('change');
        var ctrlHClicked = 1;
		$('input', $hideFromUser).attr('checked', fb.settings.hideFromUser)
				.change(function(event){
					if ($(this).attr('checked')) {
						$('input', $required).attr('disabled','disabled');
			            fb.settings.hideFromUser = true;
			            fb.item.css('backgroundColor', '#CFCFCF');
			            fb.settings.styles.label.backgroundColor = 'CFCFCF';
			            fb.target._updateSettings(fb.item);
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        } else {
			        	$('input', $required).removeAttr('disabled');
			            fb.settings.hideFromUser = false;
			            fb.item.css('backgroundColor', '');
			            fb.settings.styles.label.backgroundColor = '';
			            if(ctrlHClicked==1)
			            	ctrlHClicked=2;
			        }
					if(ctrlHClicked==2){
						fb.item.trigger('click');
						ctrlHClicked = "";
					}
			        fb.target._updateSettings(fb.item);
				});
		ctrlHClicked=3;
        $('input', $hideFromUser).trigger('change');
        ctrlHClicked=1;
        $('select', $voteAndLike).val(""+fb.settings.likeAndVote)
    	.change(function(event){
    		if ($(this).val() == 'true') {
                fb.settings.likeAndVote = true;
                fb.item.find('.likeDislikeOption').hide();
                fb.item.find('.voteOption').show();
            } else {
                fb.settings.likeAndVote = false;
                fb.item.find('.likeDislikeOption').show();
                fb.item.find('.voteOption').hide();
            }
            fb.target._updateSettings(fb.item);
    	});
        var $textInput = fb.item.find('.textInput');
        var styles = fb.settings.styles;
        if (styles.value.color == 'default') {
            styles.value.color = $textInput.css('color');
        }
        if (styles.value.backgroundColor == 'default') {
            styles.value.backgroundColor = $textInput.css('backgroundColor');
        }
        var $colorPanel = fb.target._labelValueDescriptionColorPanel(styles);

        $("input[id$='field.label.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('color', (value=='transparent'?'':'#') + value);
            styles.label.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.label.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.label.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('color', (value=='transparent'?'':'#') + value);
            styles.value.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.value.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            $textInput.css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.value.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.color']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('color', (value=='transparent'?'':'#') + value);
            styles.description.color = value;
            fb.target._updateSettings(fb.item);
        });

        $("input[id$='field.description.backgroundColor']", $colorPanel).change(function(event) {
            var value = $(this).data('colorPicker').color;
            fb.item.find('.formHint').css('backgroundColor', (value=='transparent'?'':'#') + value);
            styles.description.backgroundColor = value;
            fb.target._updateSettings(fb.item);
        });
        fb.target._log('fbSingleLineText._getFieldSettingsGeneralSection executed.');
        return [$valuePanel, $colorPanel];
    },
    _languageChange : function(event, fb) {
        fb.target._log('fbDropDown.languageChange executing...');
        var styles = fb.settings.styles;
        var fbStyles = fb.target._getFbLocalizedSettings().styles;
        var fontFamily = styles.fontFamily != 'default' ? styles.fontFamily : fbStyles.fontFamily;
        var fontSize = styles.fontSize != 'default' ? styles.fontSize : fbStyles.fontSize;
        var fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal';
        var fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal';
        var textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none';

        fb.item.css('fontFamily', fontFamily);
        fb.item.find('label span').text(fb.settings.label)
                .css('textDecoration', textDecoration);

        fb.item.find('label').css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.textInput').val(fb.settings.value)
                .css('fontWeight', fontWeight)
                .css('fontStyle', fontStyle)
                .css('textDecoration', textDecoration)
                .css('fontFamily', fontFamily)
                .css('fontSize', fontSize + 'px');

        fb.item.find('.formHint').text(fb.settings.description);

        fb.target._log('fbDropDown.languageChange executed.');
    }
});

$.widget('fb.fbLikeDislikeButton', fbLikeDislikeButton);


var JSON;
if (!JSON) {
    JSON = {};
}

(function () {
    'use strict';

    function f(n) {
        // Format integers to have at least two digits.
        return n < 10 ? '0' + n : n;
    }

    if (typeof Date.prototype.toJSON !== 'function') {

        Date.prototype.toJSON = function (key) {

            return isFinite(this.valueOf())
                    ? this.getUTCFullYear() + '-' +
                    f(this.getUTCMonth() + 1) + '-' +
                    f(this.getUTCDate()) + 'T' +
                    f(this.getUTCHours()) + ':' +
                    f(this.getUTCMinutes()) + ':' +
                    f(this.getUTCSeconds()) + 'Z'
                    : null;
        };

        String.prototype.toJSON =
                Number.prototype.toJSON =
                        Boolean.prototype.toJSON = function (key) {
                            return this.valueOf();
                        };
    }

    var cx = /[\u0000\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            escapable = /[\\\"\x00-\x1f\x7f-\x9f\u00ad\u0600-\u0604\u070f\u17b4\u17b5\u200c-\u200f\u2028-\u202f\u2060-\u206f\ufeff\ufff0-\uffff]/g,
            gap,
            indent,
            meta = {    // table of character substitutions
                '\b': '\\b',
                '\t': '\\t',
                '\n': '\\n',
                '\f': '\\f',
                '\r': '\\r',
                '"' : '\\"',
                '\\': '\\\\'
            },
            rep;


    function quote(string) {

        // If the string contains no control characters, no quote characters, and no
        // backslash characters, then we can safely slap some quotes around it.
        // Otherwise we must also replace the offending characters with safe escape
        // sequences.

        escapable.lastIndex = 0;
        return escapable.test(string) ? '"' + string.replace(escapable, function (a) {
            var c = meta[a];
            return typeof c === 'string'
                    ? c
                    : '\\u' + ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
        }) + '"' : '"' + string + '"';
    }


    function str(key, holder) {

        // Produce a string from holder[key].

        var i,          // The loop counter.
                k,          // The member key.
                v,          // The member value.
                length,
                mind = gap,
                partial,
                value = holder[key];

        // If the value has a toJSON method, call it to obtain a replacement value.

        if (value && typeof value === 'object' &&
                typeof value.toJSON === 'function') {
            value = value.toJSON(key);
        }

        // If we were called with a replacer function, then call the replacer to
        // obtain a replacement value.

        if (typeof rep === 'function') {
            value = rep.call(holder, key, value);
        }

        // What happens next depends on the value's type.

        switch (typeof value) {
            case 'string':
                return quote(value);

            case 'number':

                // JSON numbers must be finite. Encode non-finite numbers as null.

                return isFinite(value) ? String(value) : 'null';

            case 'boolean':
            case 'null':

                // If the value is a boolean or null, convert it to a string. Note:
                // typeof null does not produce 'null'. The case is included here in
                // the remote chance that this gets fixed someday.

                return String(value);

            // If the type is 'object', we might be dealing with an object or an array or
            // null.

            case 'object':

                // Due to a specification blunder in ECMAScript, typeof null is 'object',
                // so watch out for that case.

                if (!value) {
                    return 'null';
                }

                // Make an array to hold the partial results of stringifying this object value.

                gap += indent;
                partial = [];

                // Is the value an array?

                if (Object.prototype.toString.apply(value) === '[object Array]') {

                    // The value is an array. Stringify every element. Use null as a placeholder
                    // for non-JSON values.

                    length = value.length;
                    for (i = 0; i < length; i += 1) {
                        partial[i] = str(i, value) || 'null';
                    }

                    // Join all of the elements together, separated with commas, and wrap them in
                    // brackets.

                    v = partial.length === 0
                            ? '[]'
                            : gap
                            ? '[\n' + gap + partial.join(',\n' + gap) + '\n' + mind + ']'
                            : '[' + partial.join(',') + ']';
                    gap = mind;
                    return v;
                }

                // If the replacer is an array, use it to select the members to be stringified.

                if (rep && typeof rep === 'object') {
                    length = rep.length;
                    for (i = 0; i < length; i += 1) {
                        if (typeof rep[i] === 'string') {
                            k = rep[i];
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                } else {

                    // Otherwise, iterate through all of the keys in the object.

                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = str(k, value);
                            if (v) {
                                partial.push(quote(k) + (gap ? ': ' : ':') + v);
                            }
                        }
                    }
                }

                // Join all of the member texts together, separated with commas,
                // and wrap them in braces.

                v = partial.length === 0
                        ? '{}'
                        : gap
                        ? '{\n' + gap + partial.join(',\n' + gap) + '\n' + mind + '}'
                        : '{' + partial.join(',') + '}';
                gap = mind;
                return v;
        }
    }

    // If the JSON object does not yet have a stringify method, give it one.

    if (typeof JSON.stringify !== 'function') {
        JSON.stringify = function (value, replacer, space) {

            // The stringify method takes a value and an optional replacer, and an optional
            // space parameter, and returns a JSON text. The replacer can be a function
            // that can replace values, or an array of strings that will select the keys.
            // A default replacer method can be provided. Use of the space parameter can
            // produce text that is more easily readable.

            var i;
            gap = '';
            indent = '';

            // If the space parameter is a number, make an indent string containing that
            // many spaces.

            if (typeof space === 'number') {
                for (i = 0; i < space; i += 1) {
                    indent += ' ';
                }

                // If the space parameter is a string, it will be used as the indent string.

            } else if (typeof space === 'string') {
                indent = space;
            }

            // If there is a replacer, it must be a function or an array.
            // Otherwise, throw an error.

            rep = replacer;
            if (replacer && typeof replacer !== 'function' &&
                    (typeof replacer !== 'object' ||
                            typeof replacer.length !== 'number')) {
                throw new Error('JSON.stringify');
            }

            // Make a fake root object containing our value under the key of ''.
            // Return the result of stringifying the value.

            return str('', {'': value});
        };
    }


    // If the JSON object does not yet have a parse method, give it one.

    if (typeof JSON.parse !== 'function') {
        JSON.parse = function (text, reviver) {

            // The parse method takes a text and an optional reviver function, and returns
            // a JavaScript value if the text is a valid JSON text.

            var j;

            function walk(holder, key) {

                // The walk method is used to recursively walk the resulting structure so
                // that modifications can be made.

                var k, v, value = holder[key];
                if (value && typeof value === 'object') {
                    for (k in value) {
                        if (Object.prototype.hasOwnProperty.call(value, k)) {
                            v = walk(value, k);
                            if (v !== undefined) {
                                value[k] = v;
                            } else {
                                delete value[k];
                            }
                        }
                    }
                }
                return reviver.call(holder, key, value);
            }


            // Parsing happens in four stages. In the first stage, we replace certain
            // Unicode characters with escape sequences. JavaScript handles many characters
            // incorrectly, either silently deleting them, or treating them as line endings.

            text = String(text);
            cx.lastIndex = 0;
            if (cx.test(text)) {
                text = text.replace(cx, function (a) {
                    return '\\u' +
                            ('0000' + a.charCodeAt(0).toString(16)).slice(-4);
                });
            }

            // In the second stage, we run the text against regular expressions that look
            // for non-JSON patterns. We are especially concerned with '()' and 'new'
            // because they can cause invocation, and '=' because it can cause mutation.
            // But just to be safe, we want to reject all unexpected forms.

            // We split the second stage into 4 regexp operations in order to work around
            // crippling inefficiencies in IE's and Safari's regexp engines. First we
            // replace the JSON backslash pairs with '@' (a non-JSON character). Second, we
            // replace all simple value tokens with ']' characters. Third, we delete all
            // open brackets that follow a colon or comma or that begin the text. Finally,
            // we look to see that the remaining characters are only whitespace or ']' or
            // ',' or ':' or '{' or '}'. If that is so, then the text is safe for eval.

            if (/^[\],:{}\s]*$/
                    .test(text.replace(/\\(?:["\\\/bfnrt]|u[0-9a-fA-F]{4})/g, '@')
                    .replace(/"[^"\\\n\r]*"|true|false|null|-?\d+(?:\.\d*)?(?:[eE][+\-]?\d+)?/g, ']')
                    .replace(/(?:^|:|,)(?:\s*\[)+/g, ''))) {

                // In the third stage we use the eval function to compile the text into a
                // JavaScript structure. The '{' operator is subject to a syntactic ambiguity
                // in JavaScript: it can begin a block or an object literal. We wrap the text
                // in parens to eliminate the ambiguity.

                j = eval('(' + text + ')');

                // In the optional fourth stage, we recursively walk the new structure, passing
                // each name/value pair to a reviver function for possible transformation.

                return typeof reviver === 'function'
                        ? walk({'': j}, '')
                        : j;
            }

            // If the text is not JSON parseable, then a SyntaxError is thrown.

            throw new SyntaxError('JSON.parse');
        };
    }
}());
function submitForm(){
	$('input[type="submit"][name="_action_update"]').unbind('click').bind('click',submitFormUsingButton).trigger('click');
}
function submitFormUsingButton(){
	return setSequeceOfAllFields();
}
function vldtPPC(){
	var ppVldtMsg = "";
	var ppObj = null
	$('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
		var $this = $(this);
		if(document.getElementById('fields['+$this.attr('rel')+'].type').value == 'Paypal'){
			var settings = $.parseJSON(unescape(document.getElementById('fields['+$this.attr('rel')+'].settings').value))
			if(settings.emid == null || settings.emid == ""){
				ppVldtMsg = "Oops! PayPal id is mandatory in this control.";
			}
			ppObj = $this;
		}
	});
	if(ppVldtMsg!=""){
		ppObj.qtip({
	        content: ppVldtMsg+'[ <span onclick="hideButtonToolTip();" style="cursor:pointer;">OK</span> ]',
	        position: { my: 'bottom center', at: 'top center' },
	        show: {
	        	event: false,
	            ready: true,
	            effect: function() {
	                $(this).show('drop', { direction:'up'});
	            }
	        },
	        hide: {
	        	event: 'click',
	        	target: $(document)
	        },
	        style: {
	            widget: true,
	            classes: 'ui-tooltip-shadow ui-tooltip-rounded',
	            tip: true
	        }
	    });
		return false
	}
	return true
}
function hideButtonToolTip(){
	var ppObj = null;
	$('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
		var $this = $(this);
		if(document.getElementById('fields['+$this.attr('rel')+'].type').value == 'Paypal'){
			ppObj = $this;
		}
	});
	if(ppObj!=null){
		ppObj.qtip('destroy');
		ppObj.removeAttr('title');
	}
}
function hideToolTip(){
	$('input[type="submit"][name="_action_update"]').qtip('destroy');
	$('input[type="submit"][name="_action_update"]').removeAttr('title');
}
$(document).ready(function(){
	$('input[type="submit"][name="_action_save"]').click(checkUserLoggedIn);
	$('input[type="submit"][name="_action_update"]').click(onUpdateFunction);
	$('.formPreviewButton').click(function(event){
		if(onUpdateFunction(event,this,'preview')){
			var updateButton = $('input[type="submit"][name="_action_update"]');
			var saveButton = $('input[type="submit"][name="_action_save"]');
			updateButton.trigger('click');
			saveButton.trigger('click');
		}
		event.stopPropagation();
	});
	if($('#formCat').val() != 'S'){
		$('.formSettingsButton').click(function(event){
			if(onUpdateFunction(event,this,'settings')){
				var updateButton = $('input[type="submit"][name="_action_update"]');
				var saveButton = $('input[type="submit"][name="_action_save"]');
				updateButton.trigger('click');
				saveButton.trigger('click');
			}
			event.stopPropagation();
		});
	}else{
		$('.formSettingsButton').remove();
		$('.formPreviewButton').remove();
	}
});

function onUpdateFunction(event,ele,setExtraParam){
	var clickedElement = this;
	if(ele){
		clickedElement = ele;
	}
	var $ctrlHolder = $('.ctrlHolder');
	var $deletedCtrlHolder = $('.deletedCtrlHolder');
	var message = "";
	if($ctrlHolder.size() == $deletedCtrlHolder.size()){//if all the fields are deleted or there is no field
		var options = $.fb.formbuilder.prototype.options;
		if($deletedCtrlHolder.size() > 0){//if there is no field
			message = 'All fields are deleted. Please create at least one field.';
		}else{
			message = 'No field was created. Please select a field.';
		}
		$(options._standardFieldsPanel).qtip({
            content: message,
            position: { my: 'top right', at: 'bottom right' },
            show: {
                event: false,
                ready: true,
                effect: function() {
                    $(this).show('drop', { direction:'up'});
                }
            },
            hide: {
                target: $(options._standardFieldsPanel + ', ' + options._fancyFieldsPanel + ', ' + options._advanceFieldsPanel)
            },
            style: {
                widget: true,
                classes: 'ui-tooltip-shadow ui-tooltip-rounded',
                tip: true
            }
        });
        return false;
	}
	if(!vldtPPC()){
		return false
	}
	$('.ctrlHolder:not(.deletedCtrlHolder)').each(function(){
		var $this = $(this);
		if(document.getElementById('fields['+$this.attr('rel')+'].type').value == 'FormulaField'){
			var settings = $.parseJSON(unescape(document.getElementById('fields['+$this.attr('rel')+'].settings').value))
			var langSpecificSettings = settings[$('#language').val()]
			if(langSpecificSettings.newResultType != langSpecificSettings.oldResultType){
				message += 'Type of field "'+langSpecificSettings.label+'" changed from '+langSpecificSettings.oldResultType+' to '+langSpecificSettings.newResultType+'.<br/>';
			}
		}
	});
	if(message != ''){
		message += "So, data related to above formula field(s) will be reset.<br/>"
	}
	if($deletedCtrlHolder.size() > 0){
		message = 'Updating the form will delete the relevant data for deleted fields, if any.<br/>'+message
	}
	if(event.clientX){
		if(ele){//this is the case when user clicks on preview or settings
			//set extraParameter preview or settings
			$('#extraParameter').val(setExtraParam);
		}else{//this is the case when user clicks on update directly
			//set extraParameter empty string
			$('#extraParameter').val('');
		}
	}
	if(message != ''){
		$(clickedElement).qtip({
	        content: message+'[ <span onclick="submitForm();" style="cursor:pointer;">OK</span> ] [ <span onclick="hideToolTip();" style="cursor:pointer;">Cancel</span> ]',
	        position: { my: (setExtraParam?'right':'bottom')+' center', at: (setExtraParam?'left':'top')+' center' },
	        show: {
	        	event: false,
	            ready: true,
	            effect: function() {
	                $(this).show('drop', { direction:(setExtraParam?'left':'up')});
	            }
	        },
	        hide: {
	        	event: 'click',
	        	target: $(document)
	        },
	        style: {
	            widget: true,
	            classes: 'ui-tooltip-shadow ui-tooltip-rounded',
	            tip: true
	        }
	    });
		return false;
	}
	return setSequeceOfAllFields();
}

function updateStatusForDeletedFields(){
	var $deletedCtrlHolder = $('.deletedCtrlHolder');
	$deletedCtrlHolder.each(function(){
		var rel = $(this).attr('rel');
		document.getElementById('fields['+rel+'].status').value = 'D';
	});
}
function setSequeceOfAllFields(){
	if(!vldtPPC()){
		return false;
	}
	updateStatusForDeletedFields();
	var counter = 0;
	$('.ctrlHolder').each(function(){
		var $ctrlHolder = $(this);
		if($ctrlHolder.attr('class').indexOf('deletedCtrlHolder')==-1){
			var rel = $ctrlHolder.attr('rel');
			document.getElementById('fields['+rel+'].sequence').value = counter++;
		}
	});
	loadScreenBlock();
	return true;
}
function showToolTip(message,rel){
	var $ctrlHolder = $('.ctrlHolder[rel="'+rel+'"]')
	$('#deleteButton'+rel).mouseover(function(){
		$ctrlHolder.qtip({
	        content: message,
	        position: { my: 'bottom center', at: 'top center' },
	        show: {
	        	event: false,
	            ready: true,
	            effect: function() {
	                $(this).show('drop', { direction:'up'});
	            }
	        },
	        hide: {
	        	event: 'mouseout',
	        	target: $(this)
	        },
	        style: {
	            widget: true,
	            classes: 'ui-tooltip-shadow ui-tooltip-rounded',
	            tip: true
	        }
	    });
	});
}
function checkScroll() {
	var scrollTop = $(window).scrollTop();
	$(".floatingPanel").attr('id',"floatingPanel");
	var h = $("#floatingPanel").height();
	var wh = $(window).height();
    if($(window).scrollTop() == 0){
    	$("#floatingPanel").css("position", "relative");
        $("#floatingPanel").css("top", $(".floatingPanelIdentifier").position);
        $("#floatingPanel").css("bottom", "");
    }else if ($(window).height() > $("#floatingPanel").height()+60) {
        $("#floatingPanel").css("position", "fixed");
        $("#floatingPanel").css("top", "40");
        $("#floatingPanel").css("bottom", "");
    }else{
        if ($(window).scrollTop()+$(window).height() >= $(".floatingPanelIdentifier").position({scroll : false}).top + $("#floatingPanel").height() ){
            $("#floatingPanel").css("position", "fixed");
            $("#floatingPanel").css("bottom",'0');
            $("#floatingPanel").css("top",'');
        }else {
            $("#floatingPanel").css("position", "relative");
            $("#floatingPanel").css("top",$(".floatingPanelIdentifier").position);
            $("#floatingPanel").css("bottom", "");
        }
    }
}
function setWidthSetter(){
	try{
		var $slider = $(".widthSetterSlider");
		if($('.ctrlHolder').size()==1){
//			var center = $('a',$slider).css("left").replace("%","")*1;
//			$(".customLengthLabel").css("width",center-1+"%")
//			$(".customLengthField").css("width",100-(center+1)-2+"%")
		}
	    $('a',$slider).height($('a',$slider).parent().parent().parent().height());
	}catch(e){}
}