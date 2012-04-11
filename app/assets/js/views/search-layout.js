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
});