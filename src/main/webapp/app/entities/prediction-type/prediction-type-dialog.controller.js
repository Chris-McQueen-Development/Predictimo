(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionTypeDialogController', PredictionTypeDialogController);

    PredictionTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PredictionType', 'Prediction'];

    function PredictionTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PredictionType, Prediction) {
        var vm = this;

        vm.predictionType = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.predictions = Prediction.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.predictionType.id !== null) {
                PredictionType.update(vm.predictionType, onSaveSuccess, onSaveError);
            } else {
                PredictionType.save(vm.predictionType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:predictionTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.typeExpirationDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
