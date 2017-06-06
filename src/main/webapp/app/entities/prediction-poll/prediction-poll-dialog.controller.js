(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionPollDialogController', PredictionPollDialogController);

    PredictionPollDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PredictionPoll'];

    function PredictionPollDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PredictionPoll) {
        var vm = this;

        vm.predictionPoll = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.predictionPoll.id !== null) {
                PredictionPoll.update(vm.predictionPoll, onSaveSuccess, onSaveError);
            } else {
                PredictionPoll.save(vm.predictionPoll, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:predictionPollUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
