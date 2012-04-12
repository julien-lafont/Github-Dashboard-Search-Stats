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

		var $base = $("#repo-resume-"+this.infos.id);
		$base.find('.showDetail').show();
		$base.find('.showTimeline').hide();
		if ($('.result').length>1) $base.find('.closeDetail').show();
		
		zen.util.getJSONCache('/api/v1/repository/'+this.infos.owner.login+'/'+this.infos.name+'/commits', this.showTimeline);
	},

	// Show timeline : list of commits for a project
	showTimeline: function showTimeline(data) {
		var $base = this.$el.find(".timeline").empty().hide();
		var tplCommit = Handlebars.compile($("#template-commit").html());
		
		// FIX : Owner avatar in commits object is blank -> Copy its avatar from the owner object
		data = _(data).map(_.bind(function(commit) { 
			if (commit.author.avatar == "") commit.author.avatar = this.infos.owner.avatar; 
			return commit; 
		}, this));
		
		// Show commits in timeline
		$base.append(tplCommit({ items: data }));
		$base.slideDown(1000);
	}
	
});