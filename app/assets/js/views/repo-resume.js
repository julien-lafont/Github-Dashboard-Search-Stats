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
	
	// Open detail view
	showDetail: function showDetail() {
		var $stats = $("#repo-resume-"+this.model.toPresenter().id+' .stats');
		new zen.view.RepoStatsView({ el: $stats, model: this.model, fromList: true }).render();
	}
	
});