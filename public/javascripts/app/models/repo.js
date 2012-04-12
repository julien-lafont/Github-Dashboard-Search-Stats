zen.model.Repo = Backbone.Model.extend({
	url : function url() {
		return '/api/v1/repository/' + this.get('user') + '/' + this.get('repo') + '/detail';
	},
	
	toPresenter: function toPresenter() {
		var repo = this.toJSON();
		repo.id = repo.owner.login+"-"+repo.name;
		return repo;
	}
});

/**
 * Load repository from WS or from Cache
 */
zen.model.Repo.load = function load(id, callback) {
	var key = id.user+"/"+id.repo;
	zen.store.get(key, function(data) { // Try to load repository from localstorage
		if (data) {
			callback(new zen.model.Repo(data));
		} else {
			if (!zen.util.isOnline()) return;
			var node = new zen.model.Repo(id);	// Fetch and save repo in cache
			node.fetch({ success: function() {
				zen.store.save(node.toJSON());
				callback(node);
			}});
		}
	});
};

/**
 * Load and show repository in .results zone
 */
zen.model.Repo.loadAndShow = function loadAndShowResume(repoId) {
	zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, function(node) {
		new zen.view.RepoResumeView({model: node}).render();
	});
};
