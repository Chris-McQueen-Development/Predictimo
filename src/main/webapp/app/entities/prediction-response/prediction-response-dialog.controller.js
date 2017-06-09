(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionResponseDialogController', PredictionResponseDialogController);

    PredictionResponseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PredictionResponse', 'UserProfile', 'Prediction'];

    function PredictionResponseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PredictionResponse, UserProfile, Prediction) {
        var vm = this;

        vm.predictionResponse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userprofiles = UserProfile.query();
        vm.predictions = Prediction.query();
        vm.datePickerOpenStatus.responseDate = false;
        vm.predictionResponse.responseDate = new Date();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.predictionResponse.id !== null) {
                PredictionResponse.update(vm.predictionResponse, onSaveSuccess, onSaveError);
            } else {
                PredictionResponse.save(vm.predictionResponse, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:predictionResponseUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.responseDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
