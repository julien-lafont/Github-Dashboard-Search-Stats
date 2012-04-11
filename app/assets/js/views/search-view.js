zen.view.SearchView = Backbone.View.extend({
	pagination: 0,
	
	initialize: function initialize() {
		_.bindAll(this,  'add');
		
		zen.results = []; // reset
		this.pagination = zen.config.pagination_max;
		
		if (this.collection.length==0) {
			$(".no-result").show();
			$(".waiting").hide();
		} else {
		
			// Add repositories in view
			this.collection.each(function(repo) { this.add(repo) }, this);
			zen.util.preloadNextRepos();
		
			// Activate infiniteScroll on this page
			$(window).scroll(zen.util.infiniteScroll);
		}
	},
	
	add: function add(repoId) {
		
		// Fetch and display first results immediatly
		if (--this.pagination >= 0) {
			zen.model.Repo.loadAndShow(repoId);
			
		// Store other results for pagination
		} else {
			zen.results.push(repoId);
			zen.config.infiniteScrollOn = true;
		}
	}
});