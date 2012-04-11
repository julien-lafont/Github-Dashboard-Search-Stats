/*
 *  Project: Zenexigit / MoreSearch
 *  Description: Show/Hide marker when navigator is offline
 *  Author: jlafont
 */

;(function($) {

	$.fn.offline = function(options) {

		var opts = $.extend({}, $.fn.offline.defaults, options);

		return this.each(function() {

			var $base = $(this);

			var isOnline = function isOnline() {
				return !('onLine' in navigator) || navigator.onLine;
			};

			var updateMarker = function update() {
				$base[navigator.onLine ? 'fadeOut' : 'fadeIn']('fast');
			};
				
			var init = function init() {
				if (!('onLine' in navigator)) return; // Not supported
				
				$(document.body).on('online, offline', updateMarker);
				updateMarker();
			}();
		});			
	};
	
	$.fn.offline.defaults = { };

})(jQuery);