zen.view.RepoTimelineView = Backbone.View.extend({
	model: zen.model.Repo,
	
	initialize: function initialize() {
		_.bindAll(this, 'render', 'showTimeline');
		
		this.infos = this.model.toPresenter();
		zen.router.navigate("client/"+this.infos.owner.login+"/"+this.infos.name+'/commits');	// permalink
	},

	render: function render() {
		this.tpl = Handlebars.compile($("#template-repo-timeline").html());	// show detail layout
		this.$el.html(this.tpl());

		$("#repo-resume-"+this.infos.id).find('.showTimeline').hide();
		$("#repo-resume-"+this.infos.id).find('.showDetail').show();
		
		zen.util.getJSONCache('/api/v1/repository/'+this.infos.owner.login+'/'+this.infos.name+'/commits', this.showTimeline);
	},
	
	showTimeline: function showTimeline(data) {
		var $base = this.$el.find(".timeline").empty().hide();
		var tplCommit = Handlebars.compile($("#template-commit").html());
		console.log(data);
		$base.append(tplCommit({ items: data }));
		$base.fadeIn(1000);
	}
	
});