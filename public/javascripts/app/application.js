(function() {

	// Zenexigit namespace
	window.zen 	= {};	
	zen.view 	= {};
	zen.model 	= {};
	zen.util 	= {}
	zen.results = [];
	
	// Client local storage
	zen.store 	= new Lawnchair('zen', $.noop);		
	
	// Zenexigit configuration	
	zen.config = {
		pagination_max: 10,
		preload: true
	};

})();

$(function() {		
	// Init jquery plugins
	$("#search").zenSearch({ url: "/client/search/" });
	$("#search select").chosen({ allow_single_deselect: true });
	$().UItoTop({ easingType: 'easeOutQuart' });
	$("#offlineMarker").offline();
});
	