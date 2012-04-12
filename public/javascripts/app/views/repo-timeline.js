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

	// Show timeline : list of commits for a project
	showTimeline: function showTimeline(data) {
		var $base = this.$el.find(".timeline").empty().hide();
		var tplCommit = Handlebars.compile($("#template-commit").html());
		
		// Remap Avatar from owner of the repo
		data = _(data).map(_.bind(function(commit) { 
			if (commit.author.avatar == "") commit.author.avatar = this.infos.owner.avatar; 
			return commit; 
		}, this));
		
		// Show commits in timeline
		$base.append(tplCommit({ items: data }));
		$base.slideDown(1000);
	}
	
});