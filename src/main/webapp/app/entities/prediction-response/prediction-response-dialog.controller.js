(function() {
    'use strict';

    angular
        .module('predictimoApp')
        .controller('PredictionResponseDialogController', PredictionResponseDialogController);

    PredictionResponseDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PredictionResponse', 'UserProfile', 'Prediction', 'Principal', '$q'];

    function PredictionResponseDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PredictionResponse, UserProfile, Prediction, Principal, $q) {
        var vm = this;

        vm.predictionId = $stateParams.predictionId;
        console.log(vm.predictionId);
        vm.predictionResponse = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.userprofiles = UserProfile.query();
        vm.predictions = Prediction.query();
        vm.datePickerOpenStatus.responseDate = false;
        vm.predictionResponse.responseDate = new Date();
        vm.userFetch = Principal.identity().then(function (account) {
            vm.user = account;
        });
        $q.all([vm.userprofiles.$promise, vm.userFetch.$promise]).then(function () {
            console.log(vm.user);
            for (var i = 0; i < vm.userprofiles.length; i++) {
                if (vm.userprofiles[i].user.id === vm.user.id) {
                    vm.predictionResponse.userProfile = vm.userprofiles[i];
                }
            }
        });
        $q.all([vm.predictions.$promise]).then(function () {
           for (var i = 0; i < vm.predictions.length; i++) {
               console.log(vm.predictions[i].id);
               console.log(vm.predictionId);
               if (vm.predictions[i].id == vm.predictionId) {
                   vm.predictionResponse.prediction = vm.predictions[i];
               }
           } 
        });

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
