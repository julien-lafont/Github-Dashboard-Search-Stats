/**
 * Show statistics for a project
 */
zen.view.RepoStatsView = Backbone.View.extend({
	model: zen.model.Repo,
	initialize: function initialize() {
		_.bindAll(this, 'render', 'showImpact', 'showCommiters', 'showActivity', 'showWatchers', 'showLanguages', 
						'loadStats', 'fetchGeolocInformations', 'showGeolocCommiters');
		
		this.infos = this.model.toPresenter();
		zen.router.navigate("client/"+this.infos.owner.login+"/"+this.infos.name);	// permalink

		this.tpl = Handlebars.compile($("#template-repo-stats").html());	// show detail layout
	},
	
	render: function render() {
		this.$el.html(this.tpl());
		$("#repo-resume-"+this.infos.id).find('.showDetail').hide();
		$("#repo-resume-"+this.infos.id).find('.showTimeline').show();
		
		this.loadStats();
		
		return this;
	},
	
	// Load data async
	loadStats: function loadStats() {
		var user = this.infos.owner.login; 
		var repo = this.infos.name;
		
		zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/contributors', this.showCommiters);
		zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/contributors', this.fetchGeolocInformations);
		zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/watchers', this.showWatchers);
		zen.util.getJSONCache('/api/v1/repository/'+user+'/'+repo+'/languages', this.showLanguages);
		zen.util.getJSONCache('/api/v1/stats/'+user+'/'+repo+'/impact', this.showImpact);
		zen.util.getJSONCache('/api/v1/stats/'+user+'/'+repo+'/timeline', this.showActivity);
	},
	
	// For each contributor, fetch its country and show in map
	fetchGeolocInformations: function fetchGeolocInformations(commiters) {
		var data = {};
		var showCallback = this.showGeolocCommiters;
	
		var max = Math.min(commiters.length, 200)-1;
		_(commiters).first(200).forEach(function(commiter, key) {
			zen.util.getJSONCache('/api/v1/user/'+commiter.login+'/geolocalisation', 
				function(res) {
					if (res) // If geoposition is valid, add the location
						data[res.countrycode] = (data[res.countrycode] || 0) + 1;
						
					if (key == max) // If it's the last call, show map in 1s 
						_.delay(function() { showCallback(data) }, 1000);
				}
			);
		});
		
	},

	// Show contributors avatars
	showCommiters: function showCommiters(commiters) {
		var $base = this.$el.find(".commiters").empty().hide();
		var tplCommiter = Handlebars.compile($("#template-stats-commiters").html());
		
		$base.append(tplCommiter({
			items: _(commiters).first(100)
		}));
		$base.fadeIn(1000);
		
		zen.util.scrollRight($base);
	},
	
	// Show watchers avatars
	showWatchers: function showWatchers(watchers) {
		var $base = this.$el.find(".watchers").empty().hide();
		var tplCommiter = Handlebars.compile($("#template-stats-commiters").html());
	
		$base.append(tplCommiter({
			items: _(watchers).first(100)
		}));
		$base.fadeIn(1000);
		
		zen.util.scrollLeft($base);
	},
	
	// Show activity graph
	showActivity: function showTimeline(timeline) {
		var $base = this.$el.find(".timeline").empty().hide();
		
		var data = new google.visualization.DataTable();
		data.addColumn('string', 'Date');
		data.addColumn('number', 'Commits');
		_(timeline).forEach(function(tuple) { data.addRow([tuple.date, tuple.nb]) });
	
		var chart = new google.visualization.AreaChart($base.get(0));
		chart.draw(data, { width:640, height: 300, legend: {position: 'none'}});
		$base.fadeIn(1000);
	},
	
	// Show impact of each users
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

	// Show languages of repositories
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
	
	// Show map of contributors
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