zen.model.Repo = Backbone.Model.extend({
	url : function url() {
		return '/api/v1/repository/' + this.get('user') + '/' + this.get('repo') + '/detail';
	},
	
	toPresenter: function toPresenter() {
		var repo = this.toJSON();
		repo.id = repo.owner.login+"-"+repo.name;
		return repo;
	},
	
	validate: function validate(attrs) {
		var valid = _.isUndefined(attrs.error) && !_.isUndefined(attrs.description) && !_.isEmpty(attrs.name) && !_.isUndefined(attrs.owner);
		if (!valid) return "invalid";
	}
});

/**
 * Load repository from WS or from Cache
 */
zen.model.Repo.load = function load(id, callbackSuccess, callbackError) {
	var key = id.user+"/"+id.repo;
	zen.store.get(key, function(data) { // Try to load repository from localstorage
		
		if (data) {	// Json in cache
			callbackSuccess(new zen.model.Repo(data));
			
		} else {    // Call WS
			if (!zen.util.isOnline()) return;
			
			var node = new zen.model.Repo(id);	// Fetch and save repo in cache
			node.fetch({ 
				success: function() {
					zen.store.save(node.toJSON());
					callbackSuccess(node);
				},
				error: function() {
					callbackError("This repository seems to be invalid !")
				}
			});
		}
		
	});
};

/**
 * Load and show immediately the repository 
 */
zen.model.Repo.loadAndShow = function loadAndShowResume(repoId) {
	zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, function(node) {
		new zen.view.RepoResumeView({model: node}).render();
	});
};
