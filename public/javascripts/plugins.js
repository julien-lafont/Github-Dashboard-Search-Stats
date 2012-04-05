/*
 *  Project: Zenexigit / MoreSearch
 *  Description: Toggle the More search block
 *  Author: jlafont
 */
;
(function($, window, undefined) {

	var plugin = 'searchMore', document = window.document, defaults = {
		button 			: $("#btn-more-options"),
		parent 			: $("#search form"),
		txtOff 			: "More options",
		txtOn  			: "Less options",
		classVisible	: "after",
		classHidden 	: "before"
	};
	
	var initialSize = 0;

	function Plugin(element, options) {
		this.element = element;
		this.options = $.extend({}, defaults, options);
		this._defaults = defaults;
		this._name = plugin;

		this.init();
	}

	Plugin.prototype.init = function() {
		var _this = this;
		this.initialSize = this.options.parent.height();
		_.bindAll(Plugin);
		this.options.button.on("click", _.bind(function() {
			this.toggle();
		}, this));

	};

	Plugin.prototype.toggle = function() {
		var $box = this.options.parent;
		var $btn = this.options.button;
		var _this = this;
		
		if ($box.is(":not(:animated)")) {
			if ($box.hasClass(this.options.classHidden)) {
				$box.animate({ height: '270px' }, function() {
					$(this).removeClass(_this.options.classHidden).addClass(_this.options.classVisible);
				});
				$btn.val(this.options.txtOn);
			} else {
				$box.animate({ height: this.initialSize+'px' }, function() {
					$(this).addClass(_this.options.classHidden).removeClass(_this.options.classVisible);
				});
				$btn.val(this.options.txtOff);
			}
		}
	}

	$.fn[plugin] = function(options) {
		return this.each(function() {
			if (!$.data(this, 'plugin_' + plugin)) {
				$.data(this, 'plugin_' + plugin, new Plugin(this, options));
			}
		});
	};

}(jQuery, window));