zen.Router = Backbone.Router.extend({

	routes : { 
		"client/search/:query" : 		"search", 
		"client/:user/:repo" : 			"detail",
		"client/:user/:repo/commits" :  "commits"
	},

	// Show search results
	search : function search(query) {
		new zen.view.SearchLayoutView().render();
		var repoList = zen.model.RepoSet.load(query, 1, function(list) {
			new zen.view.SearchView({ collection: list });
		});
	},

	// Show repository details (graphs, activity, watchers...)
	detail : function detail(user, repo) {
		var id = { user : user, repo : repo };
		var repo = zen.model.Repo.load(id, function(repo) {
				new zen.view.RepoResumeView({ model: repo, el: $("#main_content").empty()}).render();
				new zen.view.RepoStatsView({ model: repo, el: $("#main_content .stats:first")}).render();
		}, function(error) {
			new zen.view.ErrorView({msg: error}).render()
		});
	},
	
	// Show commits timeline
	commits: function commits(user, repo) {
		var id = { user : user, repo : repo };
		var repo = zen.model.Repo.load(id, function(repo) {
 			new zen.view.RepoResumeView({ model: repo, el: $("#main_content").empty()}).render();
			new zen.view.RepoTimelineView({ model: repo, el: $("#main_content .stats:first")}).render();
		}, function(error) {
			new zen.view.ErrorView({msg: error}).render()
		});
	}

});

$(function() {
	// Start backbone routing
	zen.router = new zen.Router();
	Backbone.history.start({ pushState : true });
});