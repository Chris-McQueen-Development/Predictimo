(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionDialogController', PredictionDialogController);

    PredictionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Prediction', 'PredictionPoll', 'PredictionType', 'UserProfile'];

    function PredictionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Prediction, PredictionPoll, PredictionType, UserProfile) {
        var vm = this;

        vm.prediction = entity;
        // TODO: Manage default prediction worth from REST end-point //
        vm.prediction.predictionWorth = 1;
        
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.pollnames = PredictionPoll.query({filter: 'predictiontitle-is-null'});
        $q.all([vm.prediction.$promise, vm.pollnames.$promise]).then(function() {
            if (!vm.prediction.pollName || !vm.prediction.pollName.id) {
                return $q.reject();
            }
            return PredictionPoll.get({id : vm.prediction.pollName.id}).$promise;
        }).then(function(pollName) {
            vm.pollnames.push(pollName);
        });
        vm.predictiontypes = PredictionType.query();
        vm.userprofiles = UserProfile.query();
        $q.all([vm.userprofiles.$promise]).then(function() {
            vm.prediction.creator = vm.userprofiles[0];
            console.log(vm.prediction.creator);
        })
        

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.prediction.id !== null) {
                Prediction.update(vm.prediction, onSaveSuccess, onSaveError);
            } else {
                Prediction.save(vm.prediction, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('predictimoApp:predictionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.predictionCreatedDate = false;
        vm.prediction.predictionCreatedDate = new Date();

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
