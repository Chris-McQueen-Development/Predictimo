(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionPollDialogController', PredictionPollDialogController);

    PredictionPollDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PredictionPoll', 'UserPollVote', 'Prediction'];

    function PredictionPollDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PredictionPoll, UserPollVote, Prediction) {
        var vm = this;

        vm.predictionPoll = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userpollvotes = UserPollVote.query();
        vm.predictions = Prediction.query();

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

        vm.datePickerOpenStatus.pollEndDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
