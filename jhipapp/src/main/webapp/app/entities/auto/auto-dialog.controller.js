(function() {
    'use strict';

    angular
        .module('jhipappApp')
        .controller('AutoDialogController', AutoDialogController);

    AutoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Auto'];

    function AutoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Auto) {
        var vm = this;

        vm.auto = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.auto.id !== null) {
                Auto.update(vm.auto, onSaveSuccess, onSaveError);
            } else {
                Auto.save(vm.auto, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('jhipappApp:autoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.datainserimento = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
