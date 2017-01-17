(function() {
	'use strict';

	angular.module('jhipappApp').config(bootstrapMaterialDesignConfig);

	// compileServiceConfig.$inject = [];
	bootstrapMaterialDesignConfig.$inject = [];

	function bootstrapMaterialDesignConfig() {
		$.material.init();

	}
})();
