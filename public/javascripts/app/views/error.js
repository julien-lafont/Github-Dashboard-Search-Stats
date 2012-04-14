zen.view.ErrorView = Backbone.View.extend({
	el: "#main_content",
	
	initialize: function initialize() {
		_.bindAll(this, 'render');
	},
	
	render: function render() {
		var msg = this.options.msg || "Oops an error occurred";
		this.$el.html('<div class="error">'+msg+'</div>').show();
		return this;
	}
	
});