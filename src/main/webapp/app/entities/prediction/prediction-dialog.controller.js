(function () {
    'use strict';
    angular.module('predictimoApp').controller('PredictionDialogController', PredictionDialogController);
    PredictionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Prediction', 'PredictionType', 'UserProfile', 'PredictionResponse', 'Principal'];

    function PredictionDialogController($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Prediction, PredictionType, UserProfile, PredictionResponse, Principal) {
        var vm = this;
        vm.prediction = entity;
        // TODO: Manage default prediction worth from REST end-point //
        vm.prediction.predictionWorth = 1;
        vm.prediction.predictionFinished = false;
        vm.user = {};
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.predictiontypes = PredictionType.query();
        vm.userprofiles = UserProfile.query();
        vm.userFetch = Principal.identity().then(function (account) {
            vm.user = account;
        });
        $q.all([vm.userprofiles.$promise, vm.userFetch.$promise]).then(function () {
            console.log(vm.user);
            for (var i = 0; i < vm.userprofiles.length; i++) {
                if (vm.userprofiles[i].user.id === vm.user.id) {
                    console.log("Found match!")
                    console.log(vm.userprofiles[i]);
                    console.log(vm.user);
                    vm.prediction.creator = vm.userprofiles[i];
                    console.log(vm.userprofiles)
            console.log(vm.prediction.creator);
                }
            }
        })
        vm.predictionResponse = {};
        $timeout(function () {
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear() {
            $uibModalInstance.dismiss('cancel');
        }

        function save() {
            vm.isSaving = true;
            if (vm.predictionResponse.answer !== null) {
                if (vm.prediction.id !== null) {
                    Prediction.update(vm.prediction, onSaveSuccess, onSaveError);
                }
                else {
                    Prediction.save(vm.prediction, onSaveSuccess, onSaveError);
                }
            }
        }

        function onSaveSuccess(result) {
            console.log(result);
            vm.predictionResponse.responseDate = new Date();
            vm.predictionResponse.prediction = result;
            vm.predictionResponse.userProfile = result.creator;
            console.log(vm.predictionResponse);
            PredictionResponse.save(vm.predictionResponse);
            $scope.$emit('predictimoApp:predictionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
        vm.datePickerOpenStatus.predictionCreatedDate = false;
        vm.prediction.predictionCreatedDate = new Date();

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();