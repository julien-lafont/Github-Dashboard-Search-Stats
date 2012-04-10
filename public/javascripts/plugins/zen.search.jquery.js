/*
 *  Project: Zenexigit / MoreSearch
 *  Description: Handle events in Search zone
 *  Author: jlafont
 */

;(function($) {

	$.fn.zenSearch = function(options) {

		var opts = $.extend({}, $.fn.zenSearch.defaults, options);

		return this.each(function() {

			var $base = $(this);

			var init = function() {
				opts.initialSize = $base.find(opts.parent).height();

				$base.find(opts.button).on("click", function(event) {
					event.preventDefault();
					toggle();
				});

				$base.find(opts.buttonSearch).on('click', function(event) {
					event.preventDefault();
					search();
				});

			}();

			var toggle = function() {
				var $box = $base.find(opts.parent);
				var $btn = $base.find(opts.button);

				if ($box.is(":not(:animated)")) {
					if ($box.hasClass(opts.classHidden)) {
						$box.animate({ height : '270px' }, function() {
							$box.removeClass(opts.classHidden).addClass(opts.classVisible);
						});
						$btn.val(opts.txtOn);
					} else {
						$box.animate({ height : opts.initialSize + 'px' }, function() {
							$box.addClass(opts.classHidden).removeClass(opts.classVisible);
						});
						$btn.val(opts.txtOff);
					}
				}
			}
			
			var search = function(event) {
				var url = opts.url;
				var valid = false;
				
				// Build query with extra fields
				$base.find("input, select").each(function() {
					$elem = $(this);
					if ($elem.data("query") && $elem.val() != "") {
						url += $elem.data("query")+$elem.val()+" ";
						valid = true;
					}
				});
				
				// Add keywork to query
				var $query = $base.find(".search-query");
				if ($query.val() != "") {
					url += $query.val();
					valid = true;
				}

				if (!valid) {
					alert("You must fill at least one field !");
				} else {
					zen.router.navigate(url, {trigger: true});
				}
				
			}

		});
	};

	// Default settings for the plugin
	$.fn.zenSearch.defaults = { 
		button : ".btn-more-options", 
		buttonSearch : ".btn-search", 
		parent : "form", 
		txtOff : "More options", 
		txtOn : "Less options", 
		classVisible : "after", 
		classHidden : "before" 
	};

})(jQuery);