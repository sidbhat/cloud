function showShareOptionPopup(){var a={container:null,init:function(){$("a.question").click(function(b){b.preventDefault();$("#osx-modal-title").html("Share/Unshare Feed");$("#question-modal-content").modal({overlayId:"osx-overlay",containerId:"osx-container",closeHTML:null,minHeight:80,opacity:65,position:["0"],overlayClose:true,onOpen:a.open,onClose:a.close})});$("a.leave").click(function(b){b.preventDefault();$("#osx-modal-title").html("Create Task");$("#approval-modal-content").modal({overlayId:"osx-overlay",containerId:"osx-container",closeHTML:null,minHeight:80,opacity:65,position:["0"],overlayClose:true,onOpen:a.open,onClose:a.close})});$("a.sales").click(function(b){b.preventDefault();$("#osx-modal-content").modal({overlayId:"osx-overlay",containerId:"osx-container",closeHTML:null,minHeight:80,opacity:65,position:["0"],overlayClose:true,onOpen:a.open,onClose:a.close})});$("a.purchase").click(function(b){b.preventDefault();$("#osx-modal-content").modal({overlayId:"osx-overlay",containerId:"osx-container",closeHTML:null,minHeight:80,opacity:65,position:["0"],overlayClose:true,onOpen:a.open,onClose:a.close})});$("a.time").click(function(b){b.preventDefault();$("#osx-modal-content").modal({overlayId:"osx-overlay",containerId:"osx-container",closeHTML:null,minHeight:80,opacity:65,position:["0"],overlayClose:true,onOpen:a.open,onClose:a.close})})},open:function(a){var b=this;b.container=a.container[0];a.overlay.fadeIn("fast",function(){$("#question-modal-content",b.container).show();var c=$("#osx-modal-title",b.container);c.show();a.container.slideDown("fast",function(){setTimeout(function(){var e=$("#osx-modal-data",b.container).height()+c.height()+20;a.container.animate({height:e},200,function(){$("div.close",b.container).show();$("#osx-modal-data",b.container).show()})},300)});$("#approval-modal-content",b.container).show();var c=$("#osx-modal-title",b.container);c.show();a.container.slideDown("fast",function(){setTimeout(function(){var e=$("#osx-modal-data",b.container).height()+c.height()+20;a.container.animate({height:e},200,function(){$("div.close",b.container).show();$("#osx-modal-data",b.container).show()})},300)});$("#osx-modal-content",b.container).show();var c=$("#osx-modal-title",b.container);c.show();a.container.slideDown("fast",function(){setTimeout(function(){var e=$("#osx-modal-data",b.container).height()+c.height()+20;a.container.animate({height:e},200,function(){$("div.close",b.container).show();$("#osx-modal-data",b.container).show()})},300)})})},close:function(a){var b=this;lastOptionVal=3;a.container.animate({top:"-"+(a.container.height()+20)},500,function(){b.close()})}};a.init()}