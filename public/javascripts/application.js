(function() {

	window.zen 	= {};	// Zenexigit namespace
	zen.view 	= {};
	zen.model 	= {};
	zen.util 	= {};
	
})();

$(function() {
	$("#search").zenSearch({
		url: "/client/search/"
	});
	
	$("#search select").chosen({ allow_single_deselect: true });
	$().UItoTop({ easingType: 'easeOutQuart' });
	
});

(function($) {

	zen.results = [];		
	zen.config = {
		pagination_max: 10
	};
	
	zen.store = new Lawnchair('repos', $.noop);	// Localstorage from Lawnchair
	
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

	zen.model.Repo.load = function load(id, callback) {
		var key = id.user+"/"+id.repo;
		zen.store.get(key, function(data) { // Try to load repository from localstorage
			if (data) {
				callback(new zen.model.Repo(data));
			} else {
				var node = new zen.model.Repo(id);	// Fetch and save repo in cache
				node.fetch({ success: function() {
					zen.store.save(node.toJSON());
					callback(node);
				}});
			}
		});
	}

	zen.model.RepoSet = Backbone.Collection.extend({

		model : zen.model.Repo,
		
		url: function url() {
			return '/api/v1/repository/search/'+this.query+'/'+this.page;
		},

		initialize : function Repositories(obj) {
			this.query = obj.query;
			this.page = obj.page;
		}
	})
	
	zen.model.RepoSet.load = function load(query, page, callback) {	
		var key = query+"/"+page;
		zen.store.get(key, function(data) {
			if (data) {
				callback(new zen.model.RepoSet(data));
			} else {
				var nodeSet = new zen.model.RepoSet({ query: query, page: page });
				nodeSet.fetch({success: function(list) {
					var nodeSetSave = nodeSet.toJSON(); nodeSetSave.key = key;
					zen.store.save(nodeSetSave);
					callback(nodeSet);
				}})
			}
		})
	}

	
	zen.view.RepoDetailView = Backbone.View.extend({

		el: "#main_content",
		events : {},
		collection : zen.model.Repo,

		initialize : function initialize() {
			_.bindAll(this, 'render');
			this.tpl = Handlebars.compile($("#template-repo-detail").html());
			this.model.bind('change', this.render);
		},

		render : function render() {
			this.$el.append(this.tpl(this.model.toJSON()));
			return this;
		}
	})
	
	zen.view.RepoResumeView = Backbone.View.extend({
		el: "#results",
		events: {},
		collection: zen.model.Repo,
		
		initialize: function initialize() {
			_.bindAll(this, 'render');
			this.tpl = Handlebars.compile($("#template-repo-resume").html());
			
			this.model.bind('change', this.render);
			if (this.model.rendered) this.render();
		},
		
		render: function render() {
			this.model.rendered = true;
			var repo = this.model.toPresenter();
			this.$el.append(this.tpl(repo));
			
			this.$el.find(".waiting").hide();
			$("#repo-resume-"+repo.id).fadeIn(1000);
			
			return this;
		}
	});
	
	
	var paginating = 10;
	zen.view.SearchView = Backbone.View.extend({
		events: {},
		pagination: 0,
		
		initialize: function initialize() {
			_.bindAll(this,  'add');
			
			zen.results = []; // reset
			this.pagination = zen.config.pagination_max;
			
			// Add repositories in view
			this.collection.each(function(repo) { this.add(repo) }, this);
			
			// Activate infiniteScroll on this page
			$(window).scroll(zen.util.infiniteScroll);
		},
		
		add: function add(repoId) {
			
			// Fetch and display first results immediatly
			if (--this.pagination >= 0) {
				zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, function(node) {
					new zen.view.RepoResumeView({model: node}).render();
				});
				
			// Store other results for pagination
			} else {
				zen.results.push(repoId);
				zen.config.infiniteScrollOn = true;
			}
		}
	})
	
	zen.view.SearchLayout = Backbone.View.extend({
		
		el: '#main_content',
		
		initialize: function initialize() {
			_.bindAll(this, 'render');
			this.tpl = Handlebars.compile($("#template-layout-search").html());
		},
		
		render: function() {
			this.$el.html(this.tpl());
			return this;
		}
	})
	
	
	zen.util.infiniteScroll = function() {
		
		if (!zen.config.infiniteScrollOn) return;
		
		if ($(window).scrollTop() >= $(document).height() - $(window).height() - 50)
		{
			if (zen.results.length==0) {
				$(".no-more-results:hidden").fadeIn(1000);
			} else {
				zen.config.infiniteScrollOn = false;
				for (var i=0; i<zen.config.pagination_max && zen.results.length>0; i++) {
					var repoId = zen.results.shift();
					zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, function(node) {
						new zen.view.RepoResumeView({model: node}).render();
					});
				}
				zen.config.infiniteScrollOn = true;
			}
		}
	}
	
	

})(window.jQuery);

