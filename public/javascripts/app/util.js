/**
 * Scroll event for making an infiniteScroll
 * When the user is near the bottom of the page, show the next result page
 * and preload the next set
 */
zen.util.infiniteScroll = function() {
	
	if (!zen.config.infiniteScrollOn) return;
	
	if (zen.util.isAtBottom(150))
	{
		if (zen.results.length==0) {
			$(".no-more-results:hidden").fadeIn(1000);
		} else {
			zen.config.infiniteScrollOn = false;
			for (var i=0; i<zen.config.pagination_max && zen.results.length>0; i++) {
				zen.model.Repo.loadAndShow(zen.results.shift());
			}
			zen.util.preloadNextRepos();
			_.delay(function() { zen.config.infiniteScrollOn = true; }, 500);
		}
	}
}

/**
 * Preload and cache next repositories
 */
zen.util.preloadNextRepos = function(nb, delay) {
	if (!zen.config.preload) return;
	
	_(zen.results).first(nb || zen.config.pagination_max).forEach(function(repoId) {
		_.delay(function() { 
			zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, $.noop);
		}, delay || 1000);
	});
}

/**
 * Is browser connected to internet ?
 */
zen.util.isOnline = function() {
	return !('onLine' in navigator) || navigator.onLine;
}

/**
 * Helper to load Json from cache and fallback on WS
 */
zen.util.getJSONCache = function(url, success, error) {
	zen.store.get(url, function(cache) {
		if (cache) {
			success(cache.data);
		} else {
			$.ajax(url, {
				dataType: 'json', 
				success: function(res) {
					zen.store.save({ key: url, data: res });
					success(res);
				}, 
				error: (error || $.noop)()
			});
		}
	})
}

/**
 * Start motion to left
 */
zen.util.scrollLeft = function($elem) {
	var div = $elem.get(0);
	$elem.on('mouseover', function() { $(this).addClass('hover'); });
	$elem.on('mouseleave', function() { $(this).removeClass('hover')});
	
	setInterval(function() {
		if (!$elem.hasClass('hover') && div.scrollLeft < div.scrollWidth - div.clientWidth)
	    	div.scrollLeft += 2;
	}, 50);
}

/**
 * Start motion to right
*/
zen.util.scrollRight = function($elem) {
	var div = $elem.get(0);
	$elem.on('mouseover', function() { $(this).addClass('hover'); });
	$elem.on('mouseleave', function() { $(this).removeClass('hover')});
	div.scrollLeft = div.scrollWidth - div.clientWidth;
	
	setInterval(function() {
		if (!$elem.hasClass('hover') && 0 < div.scrollWidth - div.clientWidth)
	    	div.scrollLeft -= 2;
	}, 50);
}

zen.util.isAtBottom = function(marge) {
	return $(window).scrollTop() >= $(document).height() - $(window).height() - marge;
}
