(function() {

	window.zen = {};
	zen.view = {};
	zen.model = {};
	zen.util = {};
	zen.cache = {}
	
})();

$(function() {
	$("#search").zenSearch({
		url: "/client/search/"
	});
	
	$("#search select").chosen();
	$().UItoTop({ easingType: 'easeOutQuart' });
	
});

(function($) {

	zen.cache.repos = {};
	zen.results = [];
	zen.config = {
		pagination_max: 10
	};
	zen.store = window.localStorage;
	
	zen.model.Repo = Backbone.Model.extend({

		url : function url() {
			return '/api/v1/repository/' + this.get('user') + '/' + this.get('repo') + '/detail';
		},

		initialize : function Repository() { 
		  this.key = this.get('user') + '-' + this.get('repo');
		},
		
		toPresenter: function toPresenter() {
			var repo = this.toJSON();
			repo.id = repo.owner.login+"-"+repo.name;
			return repo;
		},
		
		fetchCache: function fetch() {
			var json = zen.store.getItem(this.key);
			if (!json) {
				
			}
		}

	});

	zen.model.Repo.load = function load(id) {
		var key = id.user+"-"+id.repo;
		var repo = zen.cache.repos[key];
		if (!repo) {
			repo = new zen.model.Repo(id)
			repo.fetchCache();
			zen.cache.repos[key] = repo;
		} else {
			console.log("IN CACHE");
		}
		return repo;
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
	
	zen.model.RepoSet.load = function load(query, page) {
		var id = query+"/"+page;
		repoSet = new zen.model.RepoSet({query: query, page: page});
		repoSet.fetch({add: true});
		return repoSet;
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
			this.model.bind('add', this.add);
			
			this.pagination = zen.config.pagination_max;
			console.log(this.pagination);
			
			zen.results = [];
			$(window).scroll(zen.util.infiniteScroll);
		},
		
		add: function add(e) {
			
			// Fetch and display first results immediatly
			if (this.pagination >= 0) {
				this.pagination--;
				var repo = zen.model.Repo.load({user: e.get('username'), repo: e.get('name')});
				new zen.view.RepoResumeView({model: repo})
			// Store other results for pagination
			} else {
				zen.results.push(e);
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
		
		if($(window).scrollTop() >= $(document).height() - $(window).height() - 100)
		{
			if (zen.results.length==0) {
				$(".no-more-results:hidden").fadeIn(1000);
			} else {
				zen.config.infiniteScrollOn = false;
				for (var i=0; i<zen.config.pagination_max && zen.results.length>0; i++) {
					var e = zen.results.shift();
					var repo = zen.model.Repo.load({user: e.get('username'), repo: e.get('name')});
					new zen.view.RepoResumeView({model: repo})
				}
				zen.config.infiniteScrollOn = true;
				
			}
		}
	}
	
	

})(window.jQuery);

