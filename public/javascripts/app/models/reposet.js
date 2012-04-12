zen.model.RepoSet = Backbone.Collection.extend({
	model : zen.model.Repo,
	
	url: function url() {
		return '/api/v1/repository/search/'+this.query+'/'+this.page;
	},
	
	initialize : function RepoSet(obj) {
		this.query = obj.query;
		this.page = obj.page;
	}
})

/**
 * Load repository list from WS or from Cache
 */
zen.model.RepoSet.load = function load(query, page, callback) {	
	var key = query+"/"+page;
	zen.store.get(key, function(data) {
		if (data) {
			callback(new zen.model.RepoSet(data));
		} else {
			if (!zen.util.isOnline()) return;
			
			var nodeSet = new zen.model.RepoSet({ query: query, page: page });
			nodeSet.fetch({success: function(list) {
				var nodeSetSave = nodeSet.toJSON(); nodeSetSave.key = key;
				zen.store.save(nodeSetSave);
				callback(nodeSet);
			}})
		}
	})
}