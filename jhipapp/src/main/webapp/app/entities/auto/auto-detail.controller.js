(function() {
    'use strict';

    angular
        .module('jhipappApp')
        .controller('AutoDetailController', AutoDetailController);

    AutoDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Auto'];

    function AutoDetailController($scope, $rootScope, $stateParams, previousState, entity, Auto) {
        var vm = this;

        vm.auto = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('jhipappApp:autoUpdate', function(event, result) {
            vm.auto = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
