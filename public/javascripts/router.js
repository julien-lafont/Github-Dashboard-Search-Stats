zen.Router = Backbone.Router.extend({
	
	routes : { 
		"" : 														"home", 
		"client" : 											"home", 
		"client/search/:query" : 				"search", 
		"client/:user/:repo" : 					"detail", 
		"client/:user/:repo/commits" : 	"commits" 
	},
	
	initialize : function() {
		// this.createAppView();
		// this.renderAppView();
	},
	
	createAppView : function() {
		// zen.view.app = new zen.view.AppView({el: '#todoapp'});
	},
	
	renderAppView : function() {
	
	},
	
	home : function() {
		console.log("home");
	},
	
	search : function(query) {
		new zen.view.SearchLayout().render();
		var repoList = zen.model.RepoSet.load(query, 1);
		new zen.view.SearchView({model: repoList});
	},
	
	detail : function(user, repo) {
		var id = { user : user, repo : repo };
		var repo = zen.model.Repo.load(id);
		new zen.view.RepoDetailView({ model : repo });
	},
	
	commits : function(user, repo) {
		console.log("Commits " + user + " " + repo);
		
	}

});

/*
 * 
 * 
 * var NodeView = Backbone.View.extend({ events: { 'click a': 'loadNode' },
 * 
 * initialize: function initialize() { var selector = this.model.get('dir') ||
 * this.model.get('items') ? '#dirTemplate' : '#fileTemplate'; this.template =
 * $(selector).html(); _.bindAll(this, 'render'); this.model.bind('change',
 * this.render); },
 * 
 * loadNode: function loadNode(e) { var link = $(e.currentTarget); // Ou si on
 * veut du vieil IE : $(e.target).closest('a');
 * browser.navigate(link.attr('href').substring(1), true); return false; },
 * 
 * render: function render() { $(this.el).html(Mustache.to_html(this.template,
 * this.model.toPresenter())). removeClass('loading'); return this; } });
 * 
 * 
 * 
 * function ensureView(node, el) { if (node.view) return; el = el || $('#frames
 * .frame[data-path="' + node.url() + '"]').first(); node.view = new NodeView({
 * model: node, el: el }).render(); }
 * 
 * function revealView(node) { var zone = $('#frames'), view = zone &&
 * zone.children('.frame[data-path="' + node.url() + '"]').first(), active =
 * zone.children('.frame.active').first(); if (view[0] == active[0]) return; if
 * (!active[0]) { zone.children(':not(.frame)').remove();
 * view.addClass('active').show(); } else { active.removeClass('active'); var
 * activeBefore = (0 == node.url().indexOf(active.attr('data-path'))); var sign =
 * activeBefore ? 1 : -1; view.css('marginLeft', sign *
 * 960).addClass('active').show().animate({ marginLeft: 0 }); active.animate({
 * marginLeft: -sign * 960 }, function() { active.hide(); }); } node.fetch({
 * success: function successfulFetch() { ensureView(node, view); } }); }
 * 
 * function setupViewForNode(nodeJSON) { var node = nodes[nodeJSON.path] = new
 * Node(nodeJSON); $('#frames').append('<div class="frame loading" data-path="' +
 * node.url() + '"></div>'); setTimeout(function() { revealView(node); }, 10); }
 * 
 * 
 */

$(function() {
	zen.router = new zen.Router();
	Backbone.history.start({ pushState : true });
});
