zen.Router = Backbone.Router.extend({
	
	routes : { 
		"" : 							"home", 
		"client" : 						"home", 
		"client/search/:query" : 		"search", 
		"client/:user/:repo" : 			"detail"
	},

	
	home : function() {
		
	},
	
	search : function(query) {
		new zen.view.SearchLayout().render();
		var repoList = zen.model.RepoSet.load(query, 1, function(list) {
			new zen.view.SearchView({ collection: list });
		});
	},
	
	detail : function(user, repo) {
		var id = { user : user, repo : repo };
		var repo = zen.model.Repo.load(id, function(repo) {
 			new zen.view.RepoResumeView({ model: repo, el: $("#main_content").empty()}).render();
			new zen.view.RepoStatsView({ model: repo, el: $("#main_content .stats:first	")}).render();
		});
	}

});

$(function() {
	zen.router = new zen.Router();
	Backbone.history.start({ pushState : true });
});
