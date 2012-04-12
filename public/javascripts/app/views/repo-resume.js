zen.view.RepoResumeView = Backbone.View.extend({
	el: "#results",
	model: zen.model.Repo,
	
	initialize: function initialize() {
		_.bindAll(this, 'render', 'showDetail', 'showTimeline', 'closeDetail');
		this.tpl = Handlebars.compile($("#template-repo-resume").html());
		
		this.model.bind('change', this.render);
		if (this.model.rendered) this.render();
	},
	
	render: function render() {
		this.model.rendered = true;
		
		var repo = this.model.toPresenter();
		this.$el.append(this.tpl(repo));
		
		this.$el.find(".waiting").hide();
		var $base = $("#repo-resume-"+repo.id).fadeIn(1000);
		
		this.initEvents($base);
		
		return this;
	},
	
	initEvents: function initEvents($base) {
		$base.find('a.showDetail').on('click', this.showDetail);
		$base.find('a.showTimeline').on('click', this.showTimeline);
		$base.find('a.closeDetail').on('click', this.closeDetail);
	},
	
	showDetail: function showDetail() {
		var $stats = $("#repo-resume-"+this.model.toPresenter().id+' .stats').empty();
		new zen.view.RepoStatsView({ el: $stats, model: this.model, fromList: true }).render();
	},
	
	showTimeline: function showTimeline() {
		var $stats = $("#repo-resume-"+this.model.toPresenter().id+' .stats').empty();
		new zen.view.RepoTimelineView({ el: $stats, model: this.model, fromList: true }).render();
	},
	
	closeDetail: function closeDetail() {
		var $base = $("#repo-resume-"+this.model.toPresenter().id);
		$base.find('.stats').empty();
		
		$base.find('a.closeDetail').hide();
		$base.find('a.showDetail, a.showTimeline').show();
	}
	
});