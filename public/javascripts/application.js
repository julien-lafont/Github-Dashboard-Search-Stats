(function() {

	window.zen 	= {};	// Zenexigit namespace
	zen.view 	= {};
	zen.model 	= {};
	zen.util 	= {}
	zen.store 	= new Lawnchair('zen', $.noop);	// Localstorage from Lawnchair
	
})();

$(function() {
	$("#search").zenSearch({
		url: "/client/search/"
	});
	
	$("#search select").chosen({ allow_single_deselect: true });
	$().UItoTop({ easingType: 'easeOutQuart' });
	
	$("#offlineMarker").offline();
	
});

(function($) {

	zen.results = [];		
	zen.config = {
		pagination_max: 10,
		preload: true
	};
	
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
				if (!zen.util.isOnline()) return;
				var node = new zen.model.Repo(id);	// Fetch and save repo in cache
				node.fetch({ success: function() {
					zen.store.save(node.toJSON());
					callback(node);
				}});
			}
		});
	}
	
	zen.model.Repo.loadAndShow = function loadAndShowResume(repoId) {
		zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, function(node) {
			new zen.view.RepoResumeView({model: node}).render();
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

	
	/*zen.view.RepoDetailView = Backbone.View.extend({

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
	})*/
	
	zen.view.RepoResumeView = Backbone.View.extend({
		el: "#results",
		model: zen.model.Repo,
		
		initialize: function initialize() {
			_.bindAll(this, 'render', 'showDetail');
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
			
			this.initEvents(repo.id);
			
			return this;
		},
		
		initEvents: function initEvents() {
			$("#repo-resume-"+this.model.toPresenter().id).find('a.showDetail').on('click', this.showDetail);
		},
		
		showDetail: function showDetail() {
			var $stats = $("#repo-resume-"+this.model.toPresenter().id+' .stats');
			new zen.view.RepoStatsView({ el: $stats, model: this.model, fromList: true }).render();
		}
		
	});
	
	
	zen.view.RepoStatsView = Backbone.View.extend({
		model: zen.model.Repo,
		initialize: function initialize() {
			_.bindAll(this, 'render', 'showImpact', 'showCommiters', 'showTimeline', 'showWatchers', 'showLanguages', 'loadStats', 'fetchGeolocInformations', 'showGeolocCommiters');
			
			this.infos = this.model.toPresenter();
			zen.router.navigate("client/"+this.infos.owner.login+"/"+this.infos.name);	// permalink

			this.tpl = Handlebars.compile($("#template-layout-detail").html());
		},
		
		render: function render() {
			this.$el.html(this.tpl());
			(this.options.fromList ? this.$el.parent().find('.showDetail') : $('.showDetail')).hide();	
			
			this.loadStats();
			
			return this;
		},
		
		loadStats: function loadStats() {
			var user = this.infos.owner.login; 
			var repo = this.infos.name;
			
			zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/contributors', this.showCommiters);
			zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/contributors', this.fetchGeolocInformations);
			zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/watchers', this.showWatchers);
			zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/languages', this.showLanguages);
			zen.util.getJSONCache('/api/v1/stats/'+user+'/'+repo+'/impact', this.showImpact);
			zen.util.getJSONCache('/api/v1/stats/'+user+'/'+repo+'/timeline', this.showTimeline);
		},
		
		fetchGeolocInformations: function fetchGeolocInformations(commiters) {
			var data = {};
			var nb = Math.min(commiters.length, 200);
			var lastCallback = this.showGeolocCommiters;
			
			_(commiters).first(200).forEach(function(commiter) {
				zen.util.getJSONCache('/api/v1/user/'+commiter.login+'/geolocalisation', 
					function(res) {
						if (res) 		data[res.countrycode] = (data[res.countrycode] || 0) + 1;
						if (--nb == 0)	lastCallback(data);	
					},
					function() { nb--; }
				);
			});
		},
	
		showCommiters: function showCommiters(commiters) {
			var $base = this.$el.find(".commiters").empty().hide();
			var tplCommiter = Handlebars.compile($("#template-stats-commiters").html());
			_(commiters).first(100).forEach(function(commiter) {
				$base.append(tplCommiter(commiter));
			});
			$base.fadeIn(1000);
			
			zen.util.scrollRight($base);
		},
		
		showWatchers: function showWatchers(watchers) {
			var $base = this.$el.find(".watchers").empty().hide();
			var tplCommiter = Handlebars.compile($("#template-stats-commiters").html());
			_(watchers).first(100).forEach(function(watcher) {
				$base.append(tplCommiter(watcher));
			});
			$base.fadeIn(1000);
			
			zen.util.scrollLeft($base);
		},
		
		showTimeline: function showTimeline(timeline) {
			var $base = this.$el.find(".timeline").empty().hide();
			
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Date');
			data.addColumn('number', 'Commits');
			_(timeline).forEach(function(tuple) { data.addRow([tuple.date, tuple.nb]) });
		
			var chart = new google.visualization.AreaChart($base.get(0));
			chart.draw(data, { width:640, height: 300, legend: {position: 'none'}});
			$base.fadeIn(1000);
		},
		
		showImpact: function showImpact(impacts) {
			var $base = this.$el.find(".impact").empty().hide();

			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Contributor');
			data.addColumn('number', 'Impact');
			_(impacts).forEach(function(tuple) { data.addRow([tuple.author.login, tuple.score]) });

			var chart = new google.visualization.PieChart($base.get(0));
			chart.draw(data, { width:640, height: 300, chartArea: {height: 300}});	
			$base.fadeIn(1000);
		},

		showLanguages: function showLanguages(languages) {
			var $base = this.$el.find(".languages").empty().hide();

			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Language');
			data.addColumn('number', 'Lines');
			_(languages).forEach(function(val, key) { data.addRow([key, val]) });

			var chart = new google.visualization.PieChart($base.get(0));
			chart.draw(data);	
			$base.fadeIn(1000);
		},
		
		showGeolocCommiters: function showGeolocCommiters(geoloc) {
			var $base = this.$el.find(".geoloc").empty().hide();
			
			var data = new google.visualization.DataTable();
			data.addColumn('string', 'Country');
			data.addColumn('number', 'Commiters');
			_(geoloc).forEach(function (val, key){ data.addRow([key, val]); });
			
			var chart = new google.visualization.GeoChart($base.get(0));
			chart.draw(data, { });
			$base.fadeIn(1000);
		}
	});
	
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
	})
	
	zen.view.SearchLayout = Backbone.View.extend({
		
		el: '#main_content',
		
		initialize: function initialize() {
			_.bindAll(this, 'render');
			this.tpl = Handlebars.compile($("#template-layout-search").html());
		},
		
		render: function render() {
			this.$el.html(this.tpl());
			return this;
		}
	})
	
	
	zen.util.infiniteScroll = function infiniteScroll() {
		
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
				zen.config.infiniteScrollOn = true;
			}
		}
	}
	
	zen.util.preloadNextRepos = function preloadNextRepos() {
		if (!zen.config.preload) return;
		_(zen.results).first(10).forEach(function(repoId) {
			_.delay(function() { 
				zen.model.Repo.load({user: repoId.get('username'), repo: repoId.get('name')}, $.noop);
			}, 1000);
		});
		
	}
	
	zen.util.isOnline = function isOnline() {
		return !('onLine' in navigator) || navigator.onLine;
	}
	
	zen.util.isAtBottom = function isAtBottom(marge) {
		return $(window).scrollTop() >= $(document).height() - $(window).height() - marge;
	}
	
	zen.util.getJSONCache = function(url, success, error) {
		zen.store.get(url, function(cache) {
			if (cache) {
				success(cache.data);
			} else {
				error |= $.noop
				
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
	
	zen.util.scrollLeft = function($elem) {
		var div = $elem.get(0);
		$elem.on('mouseover', function() { $(this).addClass('hover'); });
		$elem.on('mouseleave', function() { $(this).removeClass('hover')});
		
		setInterval(function() {
			if (!$elem.hasClass('hover') && div.scrollLeft < div.scrollWidth - div.clientWidth)
		    	div.scrollLeft += 2;
		}, 50);
	}
	
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
	

})(window.jQuery);