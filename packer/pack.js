var packer = require('node.packer'),
    path = './public/';

// Pack JS App Files
packer({
 log: true,
 type: 'js',
 minify: true,
 uglify: true,
 input: [
	path + "javascripts/app/application.js",
	path + "javascripts/app/models/repo.js",
	path + "javascripts/app/models/reposet.js",
	path + "javascripts/app/views/repo-resume.js",
	path + "javascripts/app/views/repo-stats.js",
	path + "javascripts/app/views/repo-timeline.js",
	path + "javascripts/app/views/search-layout.js",
	path + "javascripts/app/views/search-view.js",
	path + "javascripts/app/util.js",
	path + "javascripts/app/router.js"
 ],
 output: path + 'javascripts/app.pack.min.js',
});

// Pack JS plugins Files
packer({
 log: true,
 type: 'js',
 minify: true,
 uglify: true,
 input: [
   	path + "javascripts/plugins/easing.js",
	path + "javascripts/plugins/chosen.jquery.min.js",
	path + "javascripts/plugins/totop.jquery.js",
	path + "javascripts/plugins/zen.search.jquery.js",
	path + "javascripts/plugins/zen.offline.jquery.js",
 ],
 output: path + 'javascripts/plugins.pack.min.js',
});

// Pack CSS files
packer({
 log: true,
 type: 'css',
 minify: true,
 input: [
   	path + "stylesheets/bootstrap.min.css",
	path + "stylesheets/chosen.css",
	path + "stylesheets/stylesheet.css",
	path + "stylesheets/custom.css",
 ],
 output: path + 'stylesheets/styles.pack.min.css',
});
